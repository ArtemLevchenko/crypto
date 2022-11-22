package com.artbender.crypto.core.exception;

/**
 * Exception for handle format symbol in search requests
 *
 * @author Artsiom Leuchanka
 */
public class SymbolNotFoundException extends RuntimeException {

    private final static String ERROR_MESSAGE = "Crypto symbol is NOT valid. Please check request params";

    public SymbolNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
