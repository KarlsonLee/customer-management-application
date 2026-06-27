package com.cm.exception;

/**
 * Thrown when the call to the 3rd-party RestCountries API fails
 * (network error, 4xx/5xx response, timeout, etc.)
 */
public class ThirdPartyApiException extends RuntimeException {
    public ThirdPartyApiException(String message) {
        super(message);
    }

    public ThirdPartyApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
