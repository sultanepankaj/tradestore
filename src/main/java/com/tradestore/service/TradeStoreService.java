package com.tradestore.service;

import com.tradestore.model.TradeStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TradeStoreService {
    String createOrUpdateTrade(TradeStore tradeStore);

    Optional<Integer> getTradeByTradeId(String tradeId);

    Page<TradeStore> getTrades(String counterPartyId, Boolean expired, Pageable pageable);

    Optional<Integer> getTradeByTradeIdAndVersion(String tradeId, int version);
}
