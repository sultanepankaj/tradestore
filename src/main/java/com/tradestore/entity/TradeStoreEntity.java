package com.tradestore.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@IdClass(TradeKey.class)
@Table(name = "trade_store", uniqueConstraints = @UniqueConstraint(name = "uq_trade_version",
        columnNames = {"trade_id", "version"}))
public class TradeStoreEntity {
    //  @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //  private Long Id;
    @Id
    @Column(name = "trade_id", nullable = false)
    private String tradeId;
    @Id
    @Column(name = "version", nullable = false)
    private int version;
    @Column(name = "counter_party_Id", nullable = false)
    private String counterPartyId;
    @Column(name = "book_id", nullable = false)
    private String bookId;
    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;
    @Column(name = "expired", nullable = false)
    private Boolean expired;
    @Column(name = "create_date", nullable = false)
    private LocalDate createdDate;

    public TradeStoreEntity() {
    }

    public TradeStoreEntity(String tradeId, int version, String counterPartyId, String bookId,
                            LocalDate maturityDate, Boolean expired, LocalDate createdDate) {
        this.tradeId = tradeId;
        this.version = version;
        this.counterPartyId = counterPartyId;
        this.bookId = bookId;
        this.maturityDate = maturityDate;
        this.expired = expired;
        this.createdDate = createdDate;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
