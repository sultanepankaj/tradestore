package com.tradestore.service;

import com.tradestore.repository.TradeStoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class TradeExpiredSchedulerTest {

    @Mock
    private TradeStoreRepository tradeStoreRepository;

    @InjectMocks
    private TradeExpiredScheduler scheduler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExpireTrades_RunsUntilNoUpdates() {
        when(tradeStoreRepository.tradesExpired(1000))
                .thenReturn(100)
                .thenReturn(50)
                .thenReturn(0);
        scheduler.expireTrades();
        verify(tradeStoreRepository, times(3)).tradesExpired(1000);
    }
}
