package com.artbender.crypto.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Global DTO class for response data
 *
 * @author Artsiom Leuchanka
 */
@AllArgsConstructor
@Getter
public class CryptoAggregateDTO {
    private Double price;
    private String symbol;
}
