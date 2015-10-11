package com.jpm.stocks.repository.impl;

import java.math.BigDecimal;
import java.math.MathContext;
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
    
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    public GBCESampleStockRepository(){
	try {
	    loadSampleStocks();
	} catch (InvalidStockSymbolException e) {
	    e.printStackTrace();
	} catch (InvalidStockException e) {
	    e.printStackTrace();
	}
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

    private void loadSampleStocks() throws InvalidStockSymbolException, InvalidStockException {
	addStock(new Stock("TEA", getBigDecimal(0), getBigDecimal(100)));
	addStock(new Stock("POP", getBigDecimal(8), getBigDecimal(100)));
	addStock(new Stock("ALE", getBigDecimal(23), getBigDecimal(60)));
	addStock(new Stock("GIN", getBigDecimal(8), getBigDecimal(100), getBigDecimal("0.02")));
	addStock(new Stock("JOE", getBigDecimal(13), getBigDecimal(250)));
    }

    private BigDecimal getBigDecimal(String value) {
	BigDecimal bigDecimal = new BigDecimal(value, MATH_CONTEXT);
	return bigDecimal;
    }

    private BigDecimal getBigDecimal(long value) {
	BigDecimal bigDecimal = new BigDecimal(value, MATH_CONTEXT);
	return bigDecimal;
    }

}
