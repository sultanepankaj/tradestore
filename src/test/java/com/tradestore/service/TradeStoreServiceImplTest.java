package com.tradestore.service;

import com.tradestore.entity.TradeStoreEntity;
import com.tradestore.model.TradeStore;
import com.tradestore.repository.TradeStoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeStoreServiceImplTest {

    @Mock
    private TradeStoreRepository tradeStoreRepository;

    @InjectMocks
    private TradeStoreServiceImpl tradeStoreService;

    private TradeStore trade;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        trade = new TradeStore();
        trade.setTradeId("T1");
        trade.setVersion(2);
        trade.setCounterPartyId("CP-1");
        trade.setBookId("B1");
        trade.setExpired(false);
        trade.setCreatedDate("");
        trade.setMaturityDate("2025-12-31");
    }

    @Test
    void testCreateOrUpdateTrade_NewVersion() {
        when(tradeStoreRepository.findMaxVersionByTradeId("T1"))
                .thenReturn(Optional.of(1));

        String result = tradeStoreService.createOrUpdateTrade(trade);

        assertEquals("NEW_VERSION", result);
        verify(tradeStoreRepository, times(1)).save(any());
    }

    @Test
    void testCreateOrUpdateTrade_SameVersion() {
        when(tradeStoreRepository.findMaxVersionByTradeId("T1"))
                .thenReturn(Optional.of(2));

        String result = tradeStoreService.createOrUpdateTrade(trade);

        assertEquals("SAME_VERSION", result);
        verify(tradeStoreRepository).save(any());
    }

    @Test
    void testCreateOrUpdateTrade_Exception() {
        when(tradeStoreRepository.findMaxVersionByTradeId("T1"))
                .thenReturn(Optional.of(5));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> tradeStoreService.createOrUpdateTrade(trade)
        );

        assertEquals("LOWER_VERSION", ex.getMessage());
        verify(tradeStoreRepository, never()).save(any());
    }

    @Test
    void testCreateOrUpdateTrade_EntityMapping() {
        when(tradeStoreRepository.findMaxVersionByTradeId("T1"))
                .thenReturn(Optional.of(1));

        tradeStoreService.createOrUpdateTrade(trade);

        ArgumentCaptor<TradeStoreEntity> captor = ArgumentCaptor.forClass(TradeStoreEntity.class);
        verify(tradeStoreRepository).save(captor.capture());

        TradeStoreEntity entity = captor.getValue();
        assertEquals("T1", entity.getTradeId());
        assertEquals(2, entity.getVersion());
        assertEquals("CP-1", entity.getCounterPartyId());
        assertEquals("B1", entity.getBookId());
        assertEquals(LocalDate.of(2025, 12, 31), entity.getMaturityDate());
    }

    @Test
    void testGetTradeByTradeId() {
        when(tradeStoreRepository.findMaxVersionByTradeId("T1"))
                .thenReturn(Optional.of(3));

        Optional<Integer> result = tradeStoreService.getTradeByTradeId("T1");

        assertTrue(result.isPresent());
        assertEquals(3, result.get());
    }

    @Test
    void testGetTradeByTradeIdAndVersion() {
        when(tradeStoreRepository.findVersion("T1", 2))
                .thenReturn(Optional.of(2));

        Optional<Integer> result = tradeStoreService.getTradeByTradeIdAndVersion("T1", 2);

        assertTrue(result.isPresent());
        assertEquals(2, result.get());
    }

    @Test
    void testGetTrades() {
        TradeStoreEntity entity = new TradeStoreEntity();
        entity.setTradeId("T1");
        entity.setVersion(1);
        entity.setCounterPartyId("CP-1");
        entity.setExpired(false);
        entity.setCreatedDate(LocalDate.of(2025, 12, 11));
        entity.setMaturityDate(LocalDate.of(2025, 12, 31));
        entity.setBookId("B1");

        Page<TradeStoreEntity> page = new PageImpl<>(List.of(entity));
        Pageable pageable = PageRequest.of(0, 10);

        when(tradeStoreRepository.findByCounterPartyIdAndExpired("CP-1", false, pageable))
                .thenReturn(page);

        Page<TradeStore> result = tradeStoreService.getTrades("CP-1", false, pageable);

        assertEquals(1, result.getContent().size());
        TradeStore dto = result.getContent().get(0);

        assertEquals("T1", dto.getTradeId());
        assertEquals("CP-1", dto.getCounterPartyId());
        assertEquals("2025-12-11", dto.getCreatedDate());
    }
}
