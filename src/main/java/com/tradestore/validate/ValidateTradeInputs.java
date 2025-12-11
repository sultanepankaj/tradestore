package com.tradestore.validate;

import com.tradestore.model.TradeStore;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ValidateTradeInputs {

    public void validateMaturityDate(String maturityDate) {
        LocalDate inputMaturityDate = LocalDate.parse(maturityDate, DateTimeFormatter.ISO_LOCAL_DATE);
        if (inputMaturityDate.isBefore(LocalDate.now())) {
            throw new UnprocessableEntityException("MATURITY_IN_PAST");
        }
    }

    public void validateTradeRequest(TradeStore tradeStore) {
        validateMaturityDate(tradeStore.getMaturityDate());
    }

}
