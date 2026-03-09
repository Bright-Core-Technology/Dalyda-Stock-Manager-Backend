package com.example.dalyda_backend_stockmanager.services.Impl;

import com.example.dalyda_backend_stockmanager.dtos.PageDto;
import com.example.dalyda_backend_stockmanager.dtos.StockAuditDto;
import com.example.dalyda_backend_stockmanager.dtos.StockDto;
import com.example.dalyda_backend_stockmanager.entities.ContainerWeights;
import com.example.dalyda_backend_stockmanager.entities.Stock;
import com.example.dalyda_backend_stockmanager.entities.StockAudit;
import com.example.dalyda_backend_stockmanager.entities.StockAuditAction;
import com.example.dalyda_backend_stockmanager.exceptions.ResourceNotFoundException;
import com.example.dalyda_backend_stockmanager.mappers.StockMapper;
import com.example.dalyda_backend_stockmanager.repositories.StockAuditRepository;
import com.example.dalyda_backend_stockmanager.repositories.StockRepository;
import com.example.dalyda_backend_stockmanager.services.StockService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockAuditRepository stockAuditRepository;
    private final PageServiceImpl pageServiceImpl;

    @Override
    public Integer getTotalStock() {
        var stock = stockRepository.getTotalStock();
        if (stock == null) {
            return 0;
        }
        return stock;
    }

    @Override
    public BigDecimal getTotalStockPrice() {
        var totalPrice = stockRepository.getTotalStockPrice();
        if (totalPrice == null) {
            return BigDecimal.ZERO;
        }
        return totalPrice;
    }

    @Override
    public Page<StockDto.ViewStockDto> viewAllStock(PageDto pageDto) {
        var pageable = pageServiceImpl.getPageable(pageDto);
        var stockPage = stockRepository.findAllStock(pageable);
        return stockPage.map(StockMapper::map);
    }

    @Override
    public Page<StockDto.ViewStockDto> searchStock(String keyword, String containerName, PageDto pageDto) {
        var pageable = pageServiceImpl.getPageable(pageDto);

        String normalizedKeyword = normalizeFilter(keyword);
        String normalizedContainer = normalizeFilter(containerName);

        return stockRepository.searchStock(normalizedKeyword, normalizedContainer, pageable)
                .map(StockMapper::map);
    }

    @Override
    public List<String> getDistinctContainerNames() {
        return stockRepository.findDistinctContainerNames();
    }

    @Override
    public StockDto.ViewStockDto addStock(StockDto.AddStockDto addStockDto, String recordedBy) {
        Stock stock = StockMapper.map(addStockDto, recordedBy);
        Stock savedStock = stockRepository.save(stock);

        logAudit(savedStock.getId(), StockAuditAction.ADD, recordedBy,
                "Added stock item [code=" + savedStock.getCode() + ", name=" + savedStock.getName() + "]");

        return StockMapper.map(savedStock);
    }

    @Override
    public void deleteStock(UUID stockId, String deletedBy) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock item with ID " + stockId + " not found"));

        String details = "Deleted stock item [code=" + stock.getCode() +
                ", name=" + stock.getName() +
                ", quantity=" + stock.getQuantity() +
                ", price=" + stock.getPrice() + "]";

        logAudit(stockId, StockAuditAction.DELETE, deletedBy, details);
        stockRepository.delete(stock);
    }

    @Override
    public StockDto.ViewStockDto updateStock(UUID stockId, StockDto.UpdateStockDto updateStockDto, String updatedBy) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock item with ID " + stockId + " not found"));

        StringBuilder changes = new StringBuilder();

        // Update only non-null fields (partial update)
        if (updateStockDto.getCode() != null && !updateStockDto.getCode().isBlank()) {
            appendChange(changes, "code", stock.getCode(), updateStockDto.getCode());
            stock.setCode(updateStockDto.getCode());
        }
        if (updateStockDto.getName() != null && !updateStockDto.getName().isBlank()) {
            appendChange(changes, "name", stock.getName(), updateStockDto.getName());
            stock.setName(updateStockDto.getName());
        }
        if (updateStockDto.getContainerName() != null && !updateStockDto.getContainerName().isBlank()) {
            appendChange(changes, "containerName", stock.getContainerName(), updateStockDto.getContainerName());
            stock.setContainerName(updateStockDto.getContainerName());
        }
        if (updateStockDto.getQuantity() != null) {
            appendChange(changes, "quantity", stock.getQuantity(), updateStockDto.getQuantity());
            stock.setQuantity(updateStockDto.getQuantity());
        }
        if (updateStockDto.getWeight() != null) {
            appendChange(changes, "weight", stock.getWeight(), updateStockDto.getWeight());
            stock.setWeight(updateStockDto.getWeight());
        }
        if (updateStockDto.getPrice() != null) {
            appendChange(changes, "price", stock.getPrice(), updateStockDto.getPrice());
            stock.setPrice(updateStockDto.getPrice());
        }

        stock.setLastUpdatedBy(updatedBy);
        stock.setLastUpdatedAt(LocalDateTime.now());

        Stock updatedStock = stockRepository.save(stock);

        String details = changes.isEmpty()
                ? "Update called with no effective field change"
                : "Updated fields: " + changes;
        logAudit(stockId, StockAuditAction.UPDATE, updatedBy, details);

        return StockMapper.map(updatedStock);
    }

    @Override
    public StockDto.BulkUploadResponse bulkUploadFromExcel(MultipartFile file, String recordedBy) {
        List<String> errors = new ArrayList<>();
        List<StockDto.ViewStockDto> addedItems = new ArrayList<>();
        int totalRows = 0;
        int successfulRows = 0;
        int failedRows = 0;

        // Validate file is not empty
        if (file.isEmpty()) {
            errors.add("File is empty. Please upload a valid Excel file with data.");
            return new StockDto.BulkUploadResponse(totalRows, successfulRows, failedRows, errors, addedItems);
        }

        // Validate file type - only .xlsx files are allowed
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".xlsx")) {
            errors.add("Invalid file type. Only Excel files (.xlsx) are allowed. File uploaded: " +
                    (fileName != null ? fileName : "unknown"));
            return new StockDto.BulkUploadResponse(totalRows, successfulRows, failedRows, errors, addedItems);
        }

        // Validate file size (optional - prevent extremely large files)
        long maxFileSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxFileSize) {
            errors.add("File size exceeds maximum limit of 10MB. Please reduce file size and try again.");
            return new StockDto.BulkUploadResponse(totalRows, successfulRows, failedRows, errors, addedItems);
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row and start from row 1
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                totalRows++;

                try {
                    // Read cells: Code, Name, Container Name, Quantity, Price, Weight
                    String code = getCellValueAsString(row.getCell(0));
                    String name = getCellValueAsString(row.getCell(1));
                    String containerName = getCellValueAsString(row.getCell(2));
                    Integer quantity = getCellValueAsInteger(row.getCell(3));
                    BigDecimal price = getCellValueAsBigDecimal(row.getCell(4));
                    String weightStr = getCellValueAsString(row.getCell(5));

                    // Validate required fields
                    if (code == null || code.isBlank()) {
                        errors.add("Row " + (i + 1) + ": Item code is required");
                        failedRows++;
                        continue;
                    }
                    if (name == null || name.isBlank()) {
                        errors.add("Row " + (i + 1) + ": Item name is required");
                        failedRows++;
                        continue;
                    }
                    if (containerName == null || containerName.isBlank()) {
                        errors.add("Row " + (i + 1) + ": Container name is required");
                        failedRows++;
                        continue;
                    }
                    if (quantity == null || quantity < 1) {
                        errors.add("Row " + (i + 1) + ": Quantity must be at least 1");
                        failedRows++;
                        continue;
                    }
                    if (price == null || price.compareTo(BigDecimal.valueOf(0.01)) < 0) {
                        errors.add("Row " + (i + 1) + ": Price must be greater than 0");
                        failedRows++;
                        continue;
                    }

                    // Parse weight enum
                    ContainerWeights weight;
                    try {
                        weight = ContainerWeights.valueOf(weightStr.toUpperCase().trim());
                    } catch (Exception e) {
                        errors.add("Row " + (i + 1) + ": Invalid weight. Must be KG_45, KG_75, or BAGS");
                        failedRows++;
                        continue;
                    }

                    // Create and save stock item
                    Stock stock = new Stock();
                    stock.setId(UUID.randomUUID());
                    stock.setCode(code);
                    stock.setName(name);
                    stock.setContainerName(containerName);
                    stock.setQuantity(quantity);
                    stock.setPrice(price);
                    stock.setWeight(weight);
                    stock.setRecordedBy(recordedBy);

                    Stock savedStock = stockRepository.save(stock);
                    logAudit(savedStock.getId(), StockAuditAction.ADD, recordedBy,
                            "Added via bulk upload [file=" + fileName + ", row=" + (i + 1) + "]");

                    addedItems.add(StockMapper.map(savedStock));
                    successfulRows++;

                } catch (Exception e) {
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                    failedRows++;
                }
            }

        } catch (IOException e) {
            errors.add("Failed to read Excel file: " + e.getMessage());
        }

        return new StockDto.BulkUploadResponse(totalRows, successfulRows, failedRows, errors, addedItems);
    }

    @Override
    public List<StockAuditDto> getStockAuditTrail(UUID stockId) {
        if (!stockRepository.existsById(stockId)) {
            throw new ResourceNotFoundException("Stock item with ID " + stockId + " not found");
        }

        return stockAuditRepository.findByStockIdOrderByPerformedAtDesc(stockId)
                .stream()
                .map(audit -> new StockAuditDto(
                        audit.getId(),
                        audit.getStockId(),
                        audit.getAction(),
                        audit.getPerformedBy(),
                        audit.getPerformedAt(),
                        audit.getDetails()
                ))
                .toList();
    }

    private void logAudit(UUID stockId, StockAuditAction action, String performedBy, String details) {
        StockAudit audit = new StockAudit();
        audit.setStockId(stockId);
        audit.setAction(action);
        audit.setPerformedBy(performedBy);
        audit.setPerformedAt(LocalDateTime.now());
        audit.setDetails(details);
        stockAuditRepository.save(audit);
    }

    private void appendChange(StringBuilder changes, String field, Object oldValue, Object newValue) {
        if (!changes.isEmpty()) {
            changes.append("; ");
        }
        changes.append(field)
                .append(": '")
                .append(oldValue)
                .append("' -> '")
                .append(newValue)
                .append("'");
    }

    private String normalizeFilter(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) {
            return null;
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> (int) cell.getNumericCellValue();
            case STRING -> {
                try {
                    yield Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    yield null;
                }
            }
            default -> null;
        };
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return null;
        }
        return switch (cell.getCellType()) {
            case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING -> {
                try {
                    yield new BigDecimal(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    yield null;
                }
            }
            default -> null;
        };
    }
}
