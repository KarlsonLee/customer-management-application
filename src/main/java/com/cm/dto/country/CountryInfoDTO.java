package com.cm.dto.country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Country enrichment data. Returned both by our internal mock
 * "3rd-party" country API (com.cm.mock) and by CountryServiceClient
 * after calling it
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryInfoDTO {
    private String countryName;
    private String officialName;
    private String region;
}
