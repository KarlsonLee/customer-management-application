package com.cm.controller;

import com.cm.dto.customer.CreateCustomerDTO;
import com.cm.dto.customer.CustomerDTO;
import com.cm.dto.customer.EnrichedCustomerDTO;
import com.cm.dto.customer.UpdateCustomerDTO;
import com.cm.dto.response.PagedResponse;
import com.cm.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(
            @Valid @RequestBody CreateCustomerDTO request) {

        return customerService.createCustomer(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerDTO request) {

        return customerService.updateCustomer(id, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomerById(@PathVariable Long id) {

        return customerService.getCustomerById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<CustomerDTO> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return customerService.getAllCustomers(page, size, sortBy, sortDir);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
    }

    /**
     * Nested 3rd-party API call
     * Postman/Client -> this endpoint -> CustomerService -> RestCountryClient
     */
    @GetMapping("/{id}/enriched")
    @ResponseStatus(HttpStatus.OK)
    public EnrichedCustomerDTO getEnrichedCustomer(@PathVariable Long id) {

        return customerService.getEnrichedCustomerById(id);
    }
}
