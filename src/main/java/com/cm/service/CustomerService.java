package com.cm.service;


import com.cm.dto.customer.CreateCustomerDTO;
import com.cm.dto.customer.CustomerDTO;
import com.cm.dto.customer.UpdateCustomerDTO;
import com.cm.dto.customer.EnrichedCustomerDTO;
import com.cm.dto.response.PagedResponse;

public interface CustomerService {

    CustomerDTO createCustomer(CreateCustomerDTO request);

    CustomerDTO updateCustomer(Long id, UpdateCustomerDTO request);

    CustomerDTO getCustomerById(Long id);

    PagedResponse<CustomerDTO> getAllCustomers(int page, int size, String sortBy, String sortDir);

    void deleteCustomerById(Long id);

    EnrichedCustomerDTO getEnrichedCustomerById(Long id);
}
