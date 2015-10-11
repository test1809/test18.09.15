package com.jpm.stocks.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.jpm.stocks.exception.StockNotFoundException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.repository.StockRepository;
import com.jpm.stocks.repository.impl.GBCESampleStockRepository;

public class StockService {
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
    private static final RoundingMode ROUNDING_MODE = MATH_CONTEXT.getRoundingMode();
    private static final int SCALE = 2;

    private StockRepository stockRepository = new GBCESampleStockRepository();

    public BigDecimal getDividendYield(Stock stock, BigDecimal tickerPrice) {
	BigDecimal dividendYield = null;
	switch (stock.getType()) {
	case COMMON:
	    dividendYield = stock.getLastDividend().divide(tickerPrice, SCALE, ROUNDING_MODE);
	    break;
	case PREFERRED:
	    dividendYield = stock.getFixedDividend().multiply(stock.getParValue(), MATH_CONTEXT).divide(tickerPrice, SCALE, ROUNDING_MODE);
	    break;
	}
	return dividendYield;
    }

    public BigDecimal getPerRatio(Stock stock, BigDecimal tickerPrice) {
	BigDecimal perRatio = null;

	switch (stock.getType()) {
	case COMMON:
	    if (BigDecimal.ZERO.compareTo(stock.getLastDividend()) != 0) {
		perRatio = tickerPrice.divide(stock.getLastDividend(), SCALE, ROUNDING_MODE);
	    }
	    break;
	case PREFERRED:
	    if (BigDecimal.ZERO.compareTo(stock.getFixedDividend()) != 0) {
		perRatio = tickerPrice.divide(stock.getFixedDividend().multiply(stock.getParValue(), MATH_CONTEXT), SCALE, ROUNDING_MODE);
	    }
	    break;
	}
	return perRatio;
    }
    
    public Stock getStock(String stockSymbol) throws StockNotFoundException{
	return stockRepository.getStock(stockSymbol);
    }
}
