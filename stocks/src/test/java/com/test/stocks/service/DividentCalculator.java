package com.test.stocks.service;

import com.test.stocks.model.Stock;

public class DividentCalculator {

    public double getDividendYield(Stock stock, double tickerPrice) {
	double dividendYield = Double.NaN;

	switch (stock.getType()) {
	case COMMON:
	    dividendYield = stock.getLastDividend() / tickerPrice;
	    break;
	case PREFERRED:
	    dividendYield = stock.getFixedDivident() * stock.getParValue() / tickerPrice;
	    break;
	}

	return dividendYield;
    }
}
