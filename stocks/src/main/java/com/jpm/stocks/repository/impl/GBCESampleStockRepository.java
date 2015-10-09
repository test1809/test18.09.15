package com.jpm.stocks.repository.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidStockSymbolException;
import com.jpm.stocks.exception.StockNotFoundException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.repository.StockRepository;
import com.jpm.stocks.validator.StockValidator;

public class GBCESampleStockRepository implements StockRepository {
    private StockValidator stockValidator = new StockValidator();

    private final Map<String, Stock> stocks = new ConcurrentHashMap<String, Stock>();

    public GBCESampleStockRepository() {
	try {
	    addTestStocks();
	} catch (InvalidStockSymbolException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvalidStockException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void addTestStocks() throws InvalidStockSymbolException, InvalidStockException {
	addStock(new Stock("TEA", 0, 100));
	addStock(new Stock("POP", 8, 100));
	addStock(new Stock("ALE", 23, 60));
	addStock(new Stock("GIN", 8, 100, 2 / 100));
	addStock(new Stock("JOE", 13, 250));
    }

    public Stock getStock(String stockSymbol) throws StockNotFoundException {
	Stock stock = stocks.get(stockSymbol);
	if (stock == null) {
	    throw new StockNotFoundException();
	}
	return stock;
    }

    public void addStock(Stock stock) throws InvalidStockException, InvalidStockSymbolException {
	stockValidator.validateStock(stock);
	
	stocks.put(stock.getStockSymbol(), stock);
    }

   
}
