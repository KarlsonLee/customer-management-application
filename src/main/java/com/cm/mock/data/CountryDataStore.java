package com.cm.mock.data;

import com.cm.dto.country.CountryInfoDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory "database" backing the internal mock country API
 * (com.cm.mock.controller.MockCountryController).
 * <p>
 * Fully hardcoded - no internet access, no API key, no external
 * dependency - so the nested 3rd-party-style call always behaves
 * predictably for demos and grading.
 * <p>
 * Lookup is case-insensitive on the common country name, with a few
 * common aliases mapped in (e.g. "USA" -> "united states").
 */
@Component
public class CountryDataStore {

    private final Map<String, CountryInfoDTO> countriesByName = new HashMap<>();
    private final Map<String, String> aliases = new HashMap<>();

    public CountryDataStore() {
        seedData();
        seedAliases();
    }

    public Optional<CountryInfoDTO> findByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        String key = name.trim().toLowerCase();
        key = aliases.getOrDefault(key, key);
        return Optional.ofNullable(countriesByName.get(key));
    }

    public List<CountryInfoDTO> findAll() {
        return List.copyOf(countriesByName.values());
    }

    private void add(String countryName, String officialName, String region) {
        countriesByName.put(countryName.toLowerCase(), CountryInfoDTO.builder()
                .countryName(countryName)
                .officialName(officialName)
                .region(region)
                .build());
    }

    private void seedData() {
        add("Malaysia", "Malaysia", "Asia");
        add("Singapore", "Republic of Singapore", "Asia");
        add("India", "Republic of India", "Asia");
        add("United States", "United States of America", "Americas");
        add("United Kingdom", "United Kingdom of Great Britain and Northern Ireland", "Europe");
        add("Australia", "Commonwealth of Australia", "Oceania");
        add("Japan", "Japan", "Asia");
        add("China", "People's Republic of China", "Asia");
        add("Germany", "Federal Republic of Germany", "Europe");
        add("France", "French Republic", "Europe");
        add("Canada", "Canada", "Americas");
        add("Indonesia", "Republic of Indonesia", "Asia");
        add("Thailand", "Kingdom of Thailand", "Asia");
        add("Philippines", "Republic of the Philippines", "Asia");
        add("Vietnam", "Socialist Republic of Vietnam", "Asia");
        add("South Korea", "Republic of Korea", "Asia");
        add("New Zealand", "New Zealand", "Oceania");
    }

    private void seedAliases() {
        aliases.put("usa", "united states");
        aliases.put("us", "united states");
        aliases.put("uk", "united kingdom");
        aliases.put("korea", "south korea");
        aliases.put("nz", "new zealand");
    }
}
