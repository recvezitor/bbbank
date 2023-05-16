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
public class ApiLoginResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}
