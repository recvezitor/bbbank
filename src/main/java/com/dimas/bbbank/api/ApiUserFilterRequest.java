package com.dimas.bbbank.api;

import com.dimas.bbbank.validation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.validation.constraints.Email;

@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
public class ApiUserFilterRequest {

    private String name;
    private String dateOfBirth;
    @PhoneNumber
    private String phone;
    @Email
    private String email;

}
