package com.cm.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String country;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

}
