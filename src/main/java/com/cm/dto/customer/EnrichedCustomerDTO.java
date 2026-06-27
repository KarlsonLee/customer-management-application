package com.cm.dto.customer;

import com.cm.dto.country.CountryInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response returned by GET /api/v1/customers/{id}/enriched.
 * Combines local customer data with data fetched from the 3rd-party country API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrichedCustomerDTO {
    private CustomerDTO customer;
    private CountryInfoDTO countryInfo;
}
