package com.dimas.bbbank.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
public class ApiUser {

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String password;
    private ApiAccount account;
    private List<ApiEmailData> emailData;
    private List<ApiPhoneData>  phoneData;

}
