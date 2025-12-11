package com.tradestore.entity;

import java.io.Serializable;
import java.util.Objects;

public class TradeKey implements Serializable {
    private String tradeId;
    private int version;

    public TradeKey() {
    }

    public TradeKey(String tradeId, int version) {
        this.tradeId = tradeId;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeKey)) return false;
        TradeKey that = (TradeKey) o;
        return Objects.equals(tradeId, that.tradeId) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, version);
    }
}
