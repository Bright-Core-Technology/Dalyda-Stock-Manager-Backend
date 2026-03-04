package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.entities.RevokedToken;
import com.example.dalyda_backend_stockmanager.repositories.RevokedTokenRepository;
import com.example.dalyda_backend_stockmanager.services.TokenRevocationService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenRevocationServiceImpl implements TokenRevocationService {

    private final RevokedTokenRepository revokedTokenRepository;
    private final JwtServiceImpl jwtService;

    @Override
    @Transactional
    public void revokeToken(String token) {
        try {
            String userEmail = jwtService.extractUsername(token);
            Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

            LocalDateTime expiresAt = expiration.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            RevokedToken revokedToken = new RevokedToken();
            revokedToken.setToken(token);
            revokedToken.setUserEmail(userEmail);
            revokedToken.setExpiresAt(expiresAt);

            revokedTokenRepository.save(revokedToken);
            log.info("Token revoked for user: {}", userEmail);
        } catch (Exception e) {
            log.error("Failed to revoke token: {}", e.getMessage());
        }
    }

    @Override
    public boolean isTokenRevoked(String token) {
        return revokedTokenRepository.existsByToken(token);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        revokedTokenRepository.deleteExpiredTokens(now);
        log.info("Cleaned up expired revoked tokens at: {}", now);
    }
}

