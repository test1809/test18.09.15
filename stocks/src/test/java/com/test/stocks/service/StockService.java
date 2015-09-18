package com.test.stocks.service;

import com.test.stocks.model.Stock;

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
}
