package com.jpm.stocks.service;

import java.util.List;

import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.Trade;

public class StockService {

    public double getDividendYield(Stock stock, double tickerPrice) {
	double dividendYield = Double.NaN;

	switch (stock.getType()) {
	case COMMON:
	    dividendYield = stock.getLastDividend() / tickerPrice;
	    break;
	case PREFERRED:
	    dividendYield = stock.getFixedDividend() * stock.getParValue() / tickerPrice;
	    break;
	}

	return dividendYield;
    }

    public double getPerRatio(Stock stock, double tickerPrice) {
	double perRatio = Double.NaN;

	switch (stock.getType()) {
	case COMMON:
	    if (stock.getLastDividend() == 0) {
		perRatio = Double.NaN;
	    } else {
		perRatio = tickerPrice / stock.getLastDividend();
	    }
	    break;
	case PREFERRED:
	    if (stock.getFixedDividend() == 0) {
		perRatio = Double.NaN;
	    } else {
		perRatio = tickerPrice / (stock.getFixedDividend() * stock.getParValue());
	    }
	    break;
	}
	return perRatio;
    }

    public double getStockPrice(List<Trade> trades, long timeLimitForPrice) {
	double value = 0;
	long sumShares = 0;

	for (Trade trade : trades) {
	    value += trade.getNumberOfShares() * trade.getPrice();
	    sumShares += trade.getNumberOfShares();
	}

	return value / sumShares;
    }
}
