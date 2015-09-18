package com.test.stocks.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.test.stocks.exception.StockNotFoundException;
import com.test.stocks.model.Stock;
import com.test.stocks.model.StockType;

public class StockRepository {

    private final Map<String, Stock> stocks = new ConcurrentHashMap<String, Stock>();

    public StockRepository() {
	init();
    }

    private void init() {
	addStock(new Stock("TEA", StockType.COMMON, 0, 100));
	addStock(new Stock("POP", StockType.COMMON, 8, 100));
	addStock(new Stock("ALE", StockType.COMMON, 23, 60));
	addStock(new Stock("GIN", StockType.PREFERRED, 8, 100, 2 / 100));
	addStock(new Stock("JOE", StockType.COMMON, 13, 250));
    }

    public Stock getStock(String stockSymbol) throws StockNotFoundException{
	Stock stock =  stocks.get(stockSymbol);
	if (stock == null){
	    throw new StockNotFoundException();
	}
	return stock;
    }

    public void addStock(Stock stock) {
	stocks.put(stock.getStockSymbol(), stock);
    }
}
