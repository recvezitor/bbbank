package com.dimas.bbbank.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
public class ApiLoginRequest {

    @NotEmpty
    private String login;
    @NotEmpty
    private String password;

}
