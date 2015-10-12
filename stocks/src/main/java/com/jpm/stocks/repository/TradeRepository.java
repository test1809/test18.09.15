package com.jpm.stocks.repository;

import java.util.List;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidStockSymbolException;
import com.jpm.stocks.exception.InvalidTradeException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.Trade;

public interface TradeRepository {
    void addTrade(Trade newTrade) throws InvalidTradeException, InvalidStockException, InvalidStockSymbolException;

    List<Trade> getTradesForStockAtTimestamp(Stock stock, long tradeTime);
    
    List<Trade> getTradesForStockAfterTimestamp(Stock stock, long time);
}
