package com.dimas.bbbank.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
public class ApiTransferResponse {

    @NotNull
    private Boolean success;

}
