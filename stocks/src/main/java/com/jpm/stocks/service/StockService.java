package com.jpm.stocks.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidStockSymbolException;
import com.jpm.stocks.exception.InvalidTradeException;
import com.jpm.stocks.exception.StockNotFoundException;
import com.jpm.stocks.exception.StockWithoutTradesWithinPriceIntervalException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.Trade;
import com.jpm.stocks.repository.StockRepository;
import com.jpm.stocks.repository.TradeRepository;

public class StockService {
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
    private static final RoundingMode ROUNDING_MODE = MATH_CONTEXT.getRoundingMode();
    private static final int SCALE = 2;
    private static final int MAX_MINUTES_FOR_PRICE_CALCULATION = 15;

    private StockRepository stockRepository;
    private TradeRepository tradeRepository;

    public void setStockRepository(StockRepository stockRepository) {
	this.stockRepository = stockRepository;
    }

    public void setTradeRepository(TradeRepository tradeRepository) {
	this.tradeRepository = tradeRepository;
    }

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

    public BigDecimal getStockPrice(String stockSymbol) throws StockNotFoundException, StockWithoutTradesWithinPriceIntervalException {
	Stock stock = getStock(stockSymbol);
	return getStockPrice(stock);
    }

    private BigDecimal getStockPrice(Stock stock) throws StockWithoutTradesWithinPriceIntervalException {
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.MINUTE, -MAX_MINUTES_FOR_PRICE_CALCULATION);
	long timeLimitForPrice = calendar.getTimeInMillis();
	List<Trade> tradesForPrice = tradeRepository.getTradesForStockAfterTimestamp(stock, timeLimitForPrice);

	if (tradesForPrice == null || tradesForPrice.isEmpty()) {
	    throw new StockWithoutTradesWithinPriceIntervalException();
	}

	return calculateStockPrice(tradesForPrice);
    }

    private BigDecimal calculateStockPrice(List<Trade> trades) {
	BigDecimal value = new BigDecimal(0, MATH_CONTEXT);
	long sumShares = 0;

	for (Trade trade : trades) {
	    BigDecimal numberOfShares = new BigDecimal(trade.getNumberOfShares(), MATH_CONTEXT);
	    value = value.add(trade.getPrice().multiply(numberOfShares, MATH_CONTEXT));
	    sumShares += trade.getNumberOfShares();
	}

	return value.divide(new BigDecimal(sumShares, MATH_CONTEXT), SCALE, ROUNDING_MODE);
    }

    public Stock getStock(String stockSymbol) throws StockNotFoundException {
	return stockRepository.getStock(stockSymbol);
    }

    public void addTrade(Trade trade) throws InvalidTradeException, InvalidStockException, InvalidStockSymbolException {
	tradeRepository.addTrade(trade);
    }

}
