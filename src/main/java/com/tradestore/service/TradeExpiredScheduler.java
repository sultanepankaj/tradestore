package com.tradestore.service;

import com.tradestore.repository.TradeStoreRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradeExpiredScheduler {

    private final TradeStoreRepository tradeStoreRepository;

    public TradeExpiredScheduler(TradeStoreRepository tradeStoreRepository) {
        this.tradeStoreRepository = tradeStoreRepository;
    }

    /**
     * Expired trades who maturity date is less than current date
     */
    @Scheduled(cron = "0 5 0 * * *")
    @Transactional
    public void expireTrades() {
        int batchSize = 1000;
        int updated;
        do {
            updated = tradeStoreRepository.tradesExpired(batchSize);
        } while (updated > 0);
    }

}
