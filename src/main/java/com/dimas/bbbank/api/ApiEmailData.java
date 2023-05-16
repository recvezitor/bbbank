package com.dimas.bbbank.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
public class ApiEmailData {

    private Long id;
    private String email;

}
