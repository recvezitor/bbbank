package com.dimas.bbbank.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
public class ApiTransferRequest {

    @NotNull
    private Long recipientId;

    @Range(min = 0, max = 1000)
    private BigDecimal amount;

}
