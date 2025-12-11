package com.tradestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradestore.cotroller.TradeStoreController;
import com.tradestore.model.TradeStore;
import com.tradestore.repository.TradeStoreRepository;
import com.tradestore.service.TradeExpiredScheduler;
import com.tradestore.service.TradeStoreService;
import com.tradestore.service.TradeStoreServiceImpl;
import com.tradestore.validate.ValidateTradeInputs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TradeStoreController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {TradeExpiredScheduler.class,TradeStoreServiceImpl.class, TradeStoreRepository.class}
        ))
public class TradeStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeStoreService tradeStoreService;
    @MockBean
    private ValidateTradeInputs validateTradeInputs;

    @MockBean
    private TradeStoreRepository tradeStoreRepository;


    @Autowired
    private ObjectMapper objectMapper;

    private TradeStore trade;

    @BeforeEach
    void setUp() {
        trade = new TradeStore();
        trade.setTradeId("T1");
        trade.setVersion(1);
        trade.setCounterPartyId("CP-1");
        trade.setExpired(false);
        trade.setBookId("B1");
        trade.setMaturityDate("2025-12-12");
    }

    @Test
    void testCreateOrUpdateTrade_NewVersion() throws Exception {

        Mockito.doNothing().when(validateTradeInputs).validateTradeRequest(any());
        Mockito.when(tradeStoreService.createOrUpdateTrade(any())).thenReturn("NEW_VERSION");

        mockMvc.perform(post("/api/v1/trades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trade)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateOrUpdateTrade_SameVersion() throws Exception {

        Mockito.doNothing().when(validateTradeInputs).validateTradeRequest(any());
        Mockito.when(tradeStoreService.createOrUpdateTrade(any())).thenReturn("SAME_VERSION");

        mockMvc.perform(post("/api/v1/trades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trade)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTradeByTradeId_Found() throws Exception {
        Mockito.when(tradeStoreService.getTradeByTradeId("T1"))
                .thenReturn(Optional.of(5));

        mockMvc.perform(get("/api/v1/trades/T1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testGetTradeByTradeId_NotFound() throws Exception {
        Mockito.when(tradeStoreService.getTradeByTradeId("T1"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/trades/T1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTradeByTradeIdAndVersion_Found() throws Exception {
        Mockito.when(tradeStoreService.getTradeByTradeIdAndVersion("T1", 2))
                .thenReturn(Optional.of(2));

        mockMvc.perform(get("/api/v1/trades/T1/versions/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    void testGetTradeByTradeIdAndVersion_NotFound() throws Exception {
        Mockito.when(tradeStoreService.getTradeByTradeIdAndVersion("T1", 2))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/trades/T1/versions/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTrades() throws Exception {

        Page<TradeStore> page = new PageImpl<>(List.of(trade), PageRequest.of(0, 10), 1);

        Mockito.when(tradeStoreService.getTrades(eq("CP-1"), eq(false), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/trades")
                        .param("counterPartyId", "CP-1")
                        .param("expired", "false")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tradeId").value("T1"));

    }

    @Test
    void testCreateOrUpdateTrade_BadRequest() throws Exception {
        TradeStore invalidInput = new TradeStore();
        mockMvc.perform(post("/api/v1/trades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest());
    }
}