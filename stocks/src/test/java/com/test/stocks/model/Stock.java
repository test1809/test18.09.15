package com.test.stocks.model;

public class Stock {

    private StockType stockType;
    private String stockSymbol;
    private Double fixedDividend;
    private Double lastDividend;
    private Double parValue;

    private Stock(String stockSymbol, StockType stockType, double lastDividend, double parValue) {
	this.stockSymbol = stockSymbol;
	this.stockType = stockType;
	this.lastDividend = lastDividend;
	this.parValue = parValue;
    }

    /** Constructor for common stocks */
    public Stock(String stockSymbol, double lastDividend, double parValue) {
	this(stockSymbol, StockType.COMMON, lastDividend, parValue);
    }

    /** Constructor for preferred stocks */
    public Stock(String stockSymbol, double lastDividend, double parValue, double fixedDividend) {
	this(stockSymbol, StockType.PREFERRED, lastDividend, parValue);
	this.fixedDividend = fixedDividend;
    }

    public StockType getType() {
	return stockType;
    }

    public Double getFixedDividend() {
	return fixedDividend;
    }

    public String getStockSymbol() {
	return stockSymbol;
    }

    public Double getLastDividend() {
	return lastDividend;
    }

    public Double getParValue() {
	return parValue;
    }
}
