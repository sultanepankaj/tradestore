package com.tradestore.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeStore {

    @NotBlank(message = "Trade is required")
    private String tradeId;
    @NotNull(message = "Version is required")
    @Positive(message = "Version must be positive")
    private int version;
    @NotBlank(message = "Counter Party Id is required")
    private String counterPartyId;
    @NotBlank(message = "Book Id is required")
    private String bookId;
    @NotBlank(message = "Maturity date is required")
    private String maturityDate;
    @NotNull(message = "Expired field is required")
    private Boolean expired;
    private String createdDate;
}
