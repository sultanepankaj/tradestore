package com.tradestore.repository;

import com.tradestore.entity.TradeStoreEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
class TradeStoreRepositoryTest {

    @Autowired
    private TradeStoreRepository tradeStoreRepository;

    private TradeStoreEntity buildEntity(String tradeId, int version, String cp, boolean expired) {
        TradeStoreEntity entity = new TradeStoreEntity();
        entity.setTradeId(tradeId);
        entity.setVersion(version);
        entity.setCounterPartyId(cp);
        entity.setExpired(expired);
        entity.setBookId("B1");
        entity.setCreatedDate(LocalDate.now());
        entity.setMaturityDate(LocalDate.now().plusDays(10));
        return entity;
    }

    @Test
    void testFindMaxVersionByTradeId() {
        tradeStoreRepository.save(buildEntity("T1", 1, "CP-1", false));
        tradeStoreRepository.save(buildEntity("T1", 3, "CP-1", false));
        tradeStoreRepository.save(buildEntity("T1", 2, "CP-1", false));

        Optional<Integer> result = tradeStoreRepository.findMaxVersionByTradeId("T1");

        assertTrue(result.isPresent());
        assertEquals(3, result.get());
    }

    @Test
    void testFindVersion() {
        tradeStoreRepository.save(buildEntity("T1", 2, "CP-1", false));

        Optional<Integer> result = tradeStoreRepository.findVersion("T1", 2);

        assertTrue(result.isPresent());
        assertEquals(2, result.get());
    }

    @Test
    void testFindByCounterPartyIdAndExpired() {
        tradeStoreRepository.save(buildEntity("T1", 1, "CP-1", false));
        tradeStoreRepository.save(buildEntity("T2", 1, "CP-1", false));
        tradeStoreRepository.save(buildEntity("T3", 1, "CP-2", true));

        Pageable pageable = PageRequest.of(0, 10);
        Page<?> page = tradeStoreRepository.findByCounterPartyIdAndExpired("CP-1", false, pageable);

        assertEquals(2, page.getTotalElements());
    }
}
