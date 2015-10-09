package com.jpm.stocks.model;

public class Trade {

    private Stock stock;
    private Long tradeTime;
    private TradeType tradeType;
    private Integer numberOfShares;
    private Double price;

    public Trade(Stock stock, long tradeTime, TradeType tradeType, double price, int numberOfShares) {
	this.stock = stock;
	this.tradeTime = tradeTime;
	this.tradeType = tradeType;
	this.numberOfShares = numberOfShares;
	this.price = price;
    }

    public Stock getStock() {
	return stock;
    }

    public Long getTradeTime() {
	return tradeTime;
    }

    public TradeType getTradeType() {
	return tradeType;
    }

    public Integer getNumberOfShares() {
	return numberOfShares;
    }
    
    public Double getPrice() {
        return price;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + numberOfShares;
	long temp;
	temp = Double.doubleToLongBits(price);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + ((stock == null) ? 0 : stock.hashCode());
	result = prime * result + (int) (tradeTime ^ (tradeTime >>> 32));
	result = prime * result + ((tradeType == null) ? 0 : tradeType.hashCode());
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
	Trade other = (Trade) obj;
	if (numberOfShares != other.numberOfShares)
	    return false;
	if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
	    return false;
	if (stock == null) {
	    if (other.stock != null)
		return false;
	} else if (!stock.equals(other.stock))
	    return false;
	if (tradeTime != other.tradeTime)
	    return false;
	if (tradeType != other.tradeType)
	    return false;
	return true;
    }

  

}
