package com.cm.mock.controller;

import com.cm.dto.country.CountryInfoDTO;
import com.cm.exception.ResourceNotFoundException;
import com.cm.mock.data.CountryDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Stands in for a real 3rd-party country information API.
 * <p>
 * This is intentionally a plain, separate REST endpoint - CountryServiceClient
 * calls it over real HTTP (via RestTemplate), exactly the same way it would
 * call any external service.
 */
@RestController
@RequestMapping("/api/mock/countries")
@RequiredArgsConstructor
public class MockCountryController {

    private final CountryDataStore countryDataStore;

    @GetMapping("/{name}")
    public CountryInfoDTO getCountry(@PathVariable String name) {
        return countryDataStore.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found: " + name));
    }

    @GetMapping
    public List<CountryInfoDTO> getAllCountries() {
        return countryDataStore.findAll();
    }
}
