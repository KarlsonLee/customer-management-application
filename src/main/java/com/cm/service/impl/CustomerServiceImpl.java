package com.cm.service.impl;

import com.cm.dto.country.CountryInfoDTO;
import com.cm.dto.customer.CreateCustomerDTO;
import com.cm.dto.customer.CustomerDTO;
import com.cm.dto.customer.EnrichedCustomerDTO;
import com.cm.dto.customer.UpdateCustomerDTO;
import com.cm.dto.response.PagedResponse;
import com.cm.entity.Customer;
import com.cm.exception.DuplicateResourceException;
import com.cm.exception.ResourceNotFoundException;
import com.cm.repository.CustomerRepository;
import com.cm.service.CustomerService;
import com.cm.service.client.CountryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CountryClient countryClient;

    @Override
    @Transactional
    public CustomerDTO createCustomer(CreateCustomerDTO request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Customer already exists with email: " + request.getEmail());
        }

        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .country(request.getCountry())
                .build();

        Customer saved = customerRepository.save(customer);

        return mapToDto(saved);
    }


    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, UpdateCustomerDTO request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customerRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Another customer already uses email: " + request.getEmail());
            }
        });

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setDateOfBirth(customer.getDateOfBirth());
        customer.setCountry(request.getCountry());


        Customer updated = customerRepository.save(customer);
        log.info("Customer updated successfully -> id: {}", updated.getId());

        return mapToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        log.info("Fetching customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<CustomerDTO> getAllCustomers(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching customers -> page: {}, pageSize: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort sort = Sort.Direction.DESC.name().equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CustomerDTO> customerPage = customerRepository.findAll(pageable).map(this::mapToDto);

        return PagedResponse.<CustomerDTO>builder()
                .data(customerPage.getContent())
                .pageNumber(customerPage.getNumber())
                .pageSize(customerPage.getSize())
                .totalElements(customerPage.getTotalElements())
                .totalPages(customerPage.getTotalPages())
                .first(customerPage.isFirst())
                .last(customerPage.isLast())
                .build();
    }


    @Override
    @Transactional
    public void deleteCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
        log.info("Customer deleted successfully -> id: {}", id);
    }

    // Nested 3rd-party API call:
    @Override
    @Transactional(readOnly = true)
    public EnrichedCustomerDTO getEnrichedCustomerById(Long id) {
        log.info("Fetching enriched customer data for id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        log.info("Customer found (country: {}). Now calling 3rd-party API to enrich data.", customer.getCountry());
        CountryInfoDTO countryInfo = countryClient.fetchCountryInfo(customer.getCountry());

        return EnrichedCustomerDTO.builder()
                .customer(mapToDto(customer))
                .countryInfo(countryInfo)
                .build();
    }

    private CustomerDTO mapToDto(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .dateOfBirth(customer.getDateOfBirth())
                .country(customer.getCountry())
                .build();
    }
}
