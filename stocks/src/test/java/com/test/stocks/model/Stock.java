package com.test.stocks.model;

public class Stock {

    private StockType stockType;
    private String stockSymbol;
    private Double fixedDividend;
    private Double lastDividend;
    private Double parValue;

    public Stock(String stockSymbol, StockType stockType, double lastDividend, double parValue) {
	this.stockSymbol = stockSymbol;
	this.stockType = stockType;
	this.lastDividend = lastDividend;
	this.parValue = parValue;
    }

    public Stock(String stockSymbol, StockType stockType,  double lastDividend, double parValue, double fixedDividend) {
	this(stockSymbol, stockType, lastDividend, parValue);
	this.fixedDividend = fixedDividend;
    }

    public StockType getType() {
	return stockType;
    }

    public Double getFixedDivident() {
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
