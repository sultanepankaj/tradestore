package com.tradestore.service;

import com.tradestore.entity.TradeStoreEntity;
import com.tradestore.model.TradeStore;
import com.tradestore.repository.TradeStoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class TradeStoreServiceImpl implements TradeStoreService {

    private final TradeStoreRepository tradeStoreRepository;

    public TradeStoreServiceImpl(TradeStoreRepository tradeStoreRepository) {
        this.tradeStoreRepository = tradeStoreRepository;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String createOrUpdateTrade(TradeStore tradeStore) {
        LocalDate maturityDate = formatTradeDate(tradeStore.getMaturityDate());
        LocalDate createdDate = checkAndGetCreatedDate(tradeStore.getCreatedDate());
        TradeStoreEntity trade = new TradeStoreEntity();
        trade.setTradeId(tradeStore.getTradeId());
        trade.setCreatedDate(createdDate);
        trade.setBookId(tradeStore.getBookId());
        trade.setCounterPartyId(tradeStore.getCounterPartyId());
        trade.setVersion(tradeStore.getVersion());
        trade.setExpired(tradeStore.getExpired());
        trade.setMaturityDate(maturityDate);
        int latestVersion = tradeStoreRepository.findMaxVersionByTradeId(trade.getTradeId()).orElse(0);
        if (tradeStore.getVersion() < latestVersion) {
            throw new IllegalStateException("LOWER_VERSION");
        }
        tradeStoreRepository.save(trade);
        if (tradeStore.getVersion() == latestVersion) {
            return "SAME_VERSION";
        } else {
            return "NEW_VERSION";
        }
    }

    @Override
    public Optional<Integer> getTradeByTradeId(String tradeId) {
        return tradeStoreRepository.findMaxVersionByTradeId(tradeId);
    }

    private LocalDate checkAndGetCreatedDate(String createdDate) {
        return StringUtils.isEmpty(createdDate) ? LocalDate.now() : formatTradeDate(createdDate);
    }

    @Override
    public Page<TradeStore> getTrades(String counterPartyId, Boolean expired, Pageable pageable) {
        Page<TradeStoreEntity> tradeEntity = tradeStoreRepository.findByCounterPartyIdAndExpired(counterPartyId, expired, pageable);
        return tradeEntity.map(this::convertToDto);
    }

    @Override
    public Optional<Integer> getTradeByTradeIdAndVersion(String tradeId, int version) {
        return tradeStoreRepository.findVersion(tradeId, version);
    }

    private TradeStore convertToDto(TradeStoreEntity tradeStoreEntity) {
        TradeStore trade = new TradeStore();
        trade.setTradeId(tradeStoreEntity.getTradeId());
        trade.setVersion(tradeStoreEntity.getVersion());
        trade.setCounterPartyId(tradeStoreEntity.getCounterPartyId());
        trade.setExpired(tradeStoreEntity.getExpired());
        trade.setCreatedDate(tradeStoreEntity.getCreatedDate().toString());
        trade.setMaturityDate(tradeStoreEntity.getMaturityDate().toString());
        trade.setBookId(tradeStoreEntity.getBookId());
        return trade;
    }

    private LocalDate formatTradeDate(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, inputFormatter);
    }

}
