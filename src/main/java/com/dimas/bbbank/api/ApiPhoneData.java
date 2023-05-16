package com.dimas.bbbank.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
public class ApiPhoneData {

    private Long id;
    private String phone;

}
