package com.artbender.crypto.core.dto;

import com.artbender.crypto.core.model.Crypto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Global Custom Response for Crypto pojo
 *
 * @author Artsiom Leuchanka
 */
@Getter
@Builder(builderClassName = "Builder")
public class CryptoResponse {
    private List<Crypto> cryptoResult;
}
