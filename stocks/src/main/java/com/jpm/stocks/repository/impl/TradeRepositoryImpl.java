package com.jpm.stocks.repository.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidStockSymbolException;
import com.jpm.stocks.exception.InvalidTradeException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.Trade;
import com.jpm.stocks.repository.TradeRepository;
import com.jpm.stocks.validator.TradeValidator;

public class TradeRepositoryImpl implements TradeRepository {

    private Map<Stock, Map<Long, List<Trade>>> stockTradeMap = new ConcurrentHashMap<Stock, Map<Long, List<Trade>>>();

    private TradeValidator tradeValidator = new TradeValidator();

    public void addTrade(Trade newTrade) throws InvalidTradeException, InvalidStockException, InvalidStockSymbolException {
	tradeValidator.validateTrade(newTrade);

	Stock stock = newTrade.getStock();
	long tradeTime = newTrade.getTradeTime();

	Map<Long, List<Trade>> timeTradeMap = stockTradeMap.get(stock);
	if (timeTradeMap == null) {
	    timeTradeMap = new ConcurrentHashMap<Long, List<Trade>>();
	    stockTradeMap.put(stock, timeTradeMap);
	}

	List<Trade> trades = timeTradeMap.get(tradeTime);
	if (trades == null) {
	    trades = new LinkedList<Trade>();
	    timeTradeMap.put(tradeTime, trades);
	}
	trades.add(newTrade);
    }

    public List<Trade> getTradesForStockAtTimestamp(Stock stock, long tradeTime) {
	Map<Long, List<Trade>> stockTrades = stockTradeMap.get(stock);
	if (stockTrades == null) {
	    return new LinkedList<Trade>();
	} else {
	    List<Trade> tradesAtTimestamp = stockTrades.get(tradeTime);
	    if (tradesAtTimestamp == null) {
		return new LinkedList<Trade>();
	    } else {
		return tradesAtTimestamp;
	    }
	}
    }

    public List<Trade> getTradesForStockAfterTimestamp(Stock stock, long time) {
	List<Trade> tradesAfterTimestamp = new LinkedList<Trade>();

	Map<Long, List<Trade>> allTradesForStock = stockTradeMap.get(stock);

	if (allTradesForStock != null) {
	    for (Map.Entry<Long, List<Trade>> entry : allTradesForStock.entrySet()) {
		if (entry.getKey() >= time) {
		    tradesAfterTimestamp.addAll(entry.getValue());
		}
	    }
	}
	
	return tradesAfterTimestamp;
    }
}
