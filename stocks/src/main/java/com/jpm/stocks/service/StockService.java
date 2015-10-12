package com.jpm.stocks.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jpm.stocks.exception.NoTradesWithinIntervalException;
import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidStockSymbolException;
import com.jpm.stocks.exception.InvalidTradeException;
import com.jpm.stocks.exception.StockNotFoundException;
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

    public BigDecimal getPeRatio(Stock stock, BigDecimal tickerPrice) {
	BigDecimal peRatio = null;

	switch (stock.getType()) {
	case COMMON:
	    if (BigDecimal.ZERO.compareTo(stock.getLastDividend()) != 0) {
		peRatio = tickerPrice.divide(stock.getLastDividend(), SCALE, ROUNDING_MODE);
	    }
	    break;
	case PREFERRED:
	    if (BigDecimal.ZERO.compareTo(stock.getFixedDividend()) != 0) {
		peRatio = tickerPrice.divide(stock.getFixedDividend().multiply(stock.getParValue(), MATH_CONTEXT), SCALE, ROUNDING_MODE);
	    }
	    break;
	}
	return peRatio;
    }

    public BigDecimal getStockPrice(String stockSymbol) throws StockNotFoundException {
	Stock stock = getStock(stockSymbol);
	return getStockPrice(stock);
    }

    private long getTimeLimitForPriceCalculation() {
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.MINUTE, -MAX_MINUTES_FOR_PRICE_CALCULATION);
	long timeLimitForPrice = calendar.getTimeInMillis();

	return timeLimitForPrice;
    }

    private BigDecimal getStockPrice(Stock stock) {
	long timeLimitForPrice = getTimeLimitForPriceCalculation();
	List<Trade> tradesForPrice = tradeRepository.getTradesForStockAfterTimestamp(stock, timeLimitForPrice);

	if (tradesForPrice == null || tradesForPrice.isEmpty()) {
	    return null;
	} else {
	    return calculateStockPrice(tradesForPrice);
	}
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

    public BigDecimal getAllSharesIndex() throws NoTradesWithinIntervalException {
	Set<Stock> allStocks = stockRepository.getAllStocks();
	Map<Stock, BigDecimal> stockPriceMap = getPriceForAllStocks(allStocks);
	
	if(stockPriceMap == null || stockPriceMap.isEmpty()){
	    throw new NoTradesWithinIntervalException();
	}
	return calculateGeometricMean(stockPriceMap.values());
    }

    private BigDecimal calculateGeometricMean(Collection<BigDecimal> values) {
	int numberOfValues = values.size();
	BigDecimal sumLogarithms = new BigDecimal(0, MATH_CONTEXT);

	for (BigDecimal value : values) {
	    if (BigDecimal.ZERO.compareTo(value) == 0) {
		return BigDecimal.ZERO;
	    }

	    BigDecimal logVal = new BigDecimal(Math.log(value.doubleValue()), MATH_CONTEXT);
	    sumLogarithms = sumLogarithms.add(logVal, MATH_CONTEXT);
	}

	double geometricMeanDouble = Math.exp(sumLogarithms.doubleValue() / numberOfValues);
	BigDecimal geometricMean = new BigDecimal(geometricMeanDouble, MATH_CONTEXT);
	return geometricMean.setScale(SCALE, ROUNDING_MODE);
    }

    private Map<Stock, BigDecimal> getPriceForAllStocks(Set<Stock> stocks) {
	Map<Stock, BigDecimal> stockPriceMap = new HashMap<Stock, BigDecimal>();
	for (Stock stock : stocks) {
	    BigDecimal stockPrice = getStockPrice(stock);
	    if (stockPrice != null) {
		stockPriceMap.put(stock, stockPrice);
	    }

	}
	return stockPriceMap;
    }
}
