package com.example.dalyda_backend_stockmanager.services;

public interface TokenRevocationService {
    void revokeToken(String token);

    boolean isTokenRevoked(String token);
}
