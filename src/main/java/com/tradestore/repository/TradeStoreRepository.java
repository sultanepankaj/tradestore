package com.tradestore.repository;

import com.tradestore.entity.TradeKey;
import com.tradestore.entity.TradeStoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeStoreRepository extends JpaRepository<TradeStoreEntity, TradeKey> {

    @Query("""
            select max(t.version) from TradeStoreEntity t where t.tradeId = :tradeId
            """)
    Optional<Integer> findMaxVersionByTradeId(@Param("tradeId") String tradeId);

    Page<TradeStoreEntity> findByCounterPartyIdAndExpired(String counterPartyId, Boolean expired, Pageable pageable);
    @Query("SELECT t.version FROM TradeStoreEntity t WHERE t.tradeId = :tradeId AND t.version = :version")
    Optional<Integer> findVersion(@Param("tradeId") String tradeId, @Param("version") int version);

    @Modifying
    @Query(value = "UPDATE TradeStoreEntity SET expired = true WHERE maturityDate < CURRENT_DATE AND expired = false AND \n" +
            "tradeId IN (SELECT tradeId\n" +
            "      FROM TradeStoreEntity\n" +
            "      WHERE maturityDate < CURRENT_DATE\n" +
            "        AND expired = false\n" +
            "      ORDER BY tradeId\n" +
            "      LIMIT : batchSize\n" +
            "  )")
    int tradesExpired(@Param("batchSize") int batchSize);
}
