package com.artbender.crypto.core.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Global Custom Response for aggregate resul of crypto data
 *
 * @author Artsiom Leuchanka
 */
@Getter
@Builder(builderClassName = "Builder")
public class CryptoAggregateResponse {
    List<CryptoAggregateDTO> aggregateEntities;
}
