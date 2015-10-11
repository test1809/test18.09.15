package com.jpm.stocks.model;

import java.math.BigDecimal;

public class Stock {
    private StockType stockType;
    private String stockSymbol;
    private BigDecimal fixedDividend;
    private BigDecimal lastDividend;
    private BigDecimal parValue;
    

    private Stock(String stockSymbol, StockType stockType, BigDecimal lastDividend, BigDecimal parValue) {
	this.stockSymbol = stockSymbol;
	this.stockType = stockType;
	this.lastDividend = lastDividend;
	this.parValue = parValue;
    }

    /** Constructor for common stocks */
    public Stock(String stockSymbol, BigDecimal lastDividend, BigDecimal parValue) {
	this(stockSymbol, StockType.COMMON, lastDividend, parValue);
    }

    /** Constructor for preferred stocks */
    public Stock(String stockSymbol, BigDecimal lastDividend, BigDecimal parValue, BigDecimal fixedDividend) {
	this(stockSymbol, StockType.PREFERRED, lastDividend, parValue);
	this.fixedDividend = fixedDividend;
    }

    public StockType getType() {
	return stockType;
    }

    public BigDecimal getFixedDividend() {
	return fixedDividend;
    }

    public String getStockSymbol() {
	return stockSymbol;
    }

    public BigDecimal getLastDividend() {
	return lastDividend;
    }

    public BigDecimal getParValue() {
	return parValue;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((fixedDividend == null) ? 0 : fixedDividend.hashCode());
	result = prime * result + ((lastDividend == null) ? 0 : lastDividend.hashCode());
	result = prime * result + ((parValue == null) ? 0 : parValue.hashCode());
	result = prime * result + ((stockSymbol == null) ? 0 : stockSymbol.hashCode());
	result = prime * result + ((stockType == null) ? 0 : stockType.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Stock other = (Stock) obj;
	if (fixedDividend == null) {
	    if (other.fixedDividend != null)
		return false;
	} else if (!fixedDividend.equals(other.fixedDividend))
	    return false;
	if (lastDividend == null) {
	    if (other.lastDividend != null)
		return false;
	} else if (!lastDividend.equals(other.lastDividend))
	    return false;
	if (parValue == null) {
	    if (other.parValue != null)
		return false;
	} else if (!parValue.equals(other.parValue))
	    return false;
	if (stockSymbol == null) {
	    if (other.stockSymbol != null)
		return false;
	} else if (!stockSymbol.equals(other.stockSymbol))
	    return false;
	if (stockType != other.stockType)
	    return false;
	return true;
    }

}
