package com.cm.service.client;

import com.cm.dto.country.CountryInfoDTO;

/**
 * Abstraction over the 3rd-party country information API.
 * Implementations are responsible for calling the external service
 * and translating its response into our internal CountryInfoResponse DTO.
 */
public interface CountryClient {
    CountryInfoDTO fetchCountryInfo(String countryName);
}
