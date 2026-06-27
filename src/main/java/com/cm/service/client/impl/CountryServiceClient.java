package com.cm.service.client.impl;

import com.cm.dto.country.CountryInfoDTO;
import com.cm.exception.ThirdPartyApiException;
import com.cm.service.client.CountryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Calls the internal mock "3rd-party" country API
 * (com.cm.mock.controller.MockCountryController) over real HTTP,
 * using RestTemplate exactly as we would for any genuine external service.
 * Swapping back to a real external provider later only means changing
 * mockBaseUrl / this class - CountryClient (the interface) and everything
 * upstream of it stays untouched.
 */
@Slf4j
@Service
public class CountryServiceClient implements CountryClient {

    private final RestTemplate restTemplate;
    private final String mockBaseUrl;

    public CountryServiceClient(RestTemplate restTemplate,
                                @Value("${server.port}") int serverPort,
                                @Value("${app.third-party.country-service-path}") String path) {
        this.restTemplate = restTemplate;
        this.mockBaseUrl = "http://localhost:" + serverPort + path;
    }

    @Override
    public CountryInfoDTO fetchCountryInfo(String countryName) {
        String urlTemplate = mockBaseUrl + "/{name}";
        log.info("Calling internal mock country API [GET] {}/{}", mockBaseUrl, countryName);

        try {
            // {name} is substituted + URL-encoded safely by RestTemplate itself,
            // which matters here since country names can contain spaces
            // (e.g. "South Korea", "United Kingdom").
            return restTemplate.getForObject(urlTemplate, CountryInfoDTO.class, countryName);

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Country not found in mock API: {}", countryName);
            throw new ThirdPartyApiException("Country not found: " + countryName, e);
        } catch (Exception e) {
            log.error("Error calling mock country API for '{}': {}", countryName, e.getMessage());
            throw new ThirdPartyApiException("Failed to fetch country information from 3rd-party API", e);
        }
    }
}
