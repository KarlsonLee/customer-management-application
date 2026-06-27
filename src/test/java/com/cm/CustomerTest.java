package com.cm;

import com.cm.dto.customer.CreateCustomerDTO;
import com.cm.dto.customer.CustomerDTO;
import com.cm.dto.customer.UpdateCustomerDTO;
import com.cm.dto.response.PagedResponse;
import com.cm.entity.Customer;
import com.cm.exception.DuplicateResourceException;
import com.cm.exception.ResourceNotFoundException;
import com.cm.repository.CustomerRepository;
import com.cm.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CreateCustomerDTO request;
    private UpdateCustomerDTO update;
    private Customer entity;

    @BeforeEach
    void setUp() {
        request = CreateCustomerDTO.builder()
                .firstName("Alex")
                .lastName("Lee")
                .email("alex@example.com")
                .country("Malaysia")
                .dateOfBirth(LocalDate.of(1990, 1, 15))
                .build();

        update = UpdateCustomerDTO.builder()
                .firstName("Alex-update")
                .lastName("Lee-update")
                .email("alex@example.com")
                .country("Malaysia")
                .build();

        entity = Customer.builder()
                .id(1L)
                .firstName("Alex")
                .lastName("Lee")
                .email("alex@example.com")
                .country("Malaysia")
                .dateOfBirth(LocalDate.of(1990, 1, 15))
                .build();
    }

    @Test
    void Test_Create_Customer() {
        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(entity);

        CustomerDTO response = customerService.createCustomer(request);

        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Alex");
        assertThat(response.getLastName()).isEqualTo("Lee");
        assertThat(response.getEmail()).isEqualTo("alex@example.com");
        assertThat(response.getCountry()).isEqualTo("Malaysia");
        assertThat(response.getDateOfBirth()).isEqualTo("1990-01-15");
    }

    @Test
    void Test_Create_Customer_whenEmailAlreadyExists() {
        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> customerService.createCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining(request.getEmail());
    }


    @Test
    void Test_Update_Customer() {

        when(customerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(entity));
        when(customerRepository.save(any(Customer.class))).thenReturn(entity);

        customerService.updateCustomer(1L, update);

        CustomerDTO response = customerService.updateCustomer(1L, update);

        assertThat(response.getFirstName()).isEqualTo(update.getFirstName());
        assertThat(response.getLastName()).isEqualTo(update.getLastName());
        assertThat(response.getEmail()).isEqualTo(update.getEmail());
    }

    @Test
    void Test_Update_Customer_whenEmailUsedByDifferentCustomer() {
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setEmail(request.getEmail());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(customer2));

        assertThrows(DuplicateResourceException.class,
                () -> customerService.updateCustomer(1L, update));
    }

    @Test
    void Test_Get_Customer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(entity));

        CustomerDTO response = customerService.getCustomerById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("Alex");
    }

    @Test
    void Test_Get_Customer_InvalidCustomer() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void Test_GetAll_Customer() {
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Alice");
        customer2.setLastName("Tai");
        customer2.setEmail("alice@example.com");

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Customer> page = new PageImpl<>(List.of(entity, customer2), pageable, 2);
        when(customerRepository.findAll(pageable)).thenReturn(page);

        PagedResponse<CustomerDTO> response = customerService.getAllCustomers(0, 10, "id", "asc");
        List<CustomerDTO> result = response.getData();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alex");
        assertThat(result.get(0).getLastName()).isEqualTo("Lee");
        assertThat(result.get(1).getFirstName()).isEqualTo("Alice");
        assertThat(result.get(1).getLastName()).isEqualTo("Tai");
    }

    @Test
    void Test_GetAll_Customer_whenNoCustomersExist() {
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        PagedResponse<CustomerDTO> response = customerService.getAllCustomers(0, 10, "id", "asc");

        assertThat(response.getData()).isEmpty();
    }

    @Test
    void Test_Delete_Customer_InvalidCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.deleteCustomerById(1L))
                .isInstanceOf(ResourceNotFoundException.class);

    }
}
