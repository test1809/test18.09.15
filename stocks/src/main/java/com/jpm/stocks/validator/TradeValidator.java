package com.jpm.stocks.validator;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidStockSymbolException;
import com.jpm.stocks.exception.InvalidTradeException;
import com.jpm.stocks.model.Trade;

public class TradeValidator {
    private StockValidator stockValidator = new StockValidator();

    public void validateTrade(Trade trade) throws InvalidTradeException, InvalidStockException, InvalidStockSymbolException {
	if (trade == null || trade.getTradeType() == null || trade.getPrice() == null) {
	    throw new InvalidTradeException();
	}

	if (trade.getTradeTime() < 0) {
	    throw new InvalidTradeException();
	}

	if (trade.getNumberOfShares() == null || trade.getNumberOfShares() <= 0) {
	    throw new InvalidTradeException();
	}

	if (trade.getPrice() == null || trade.getPrice() <= 0) {
	    throw new InvalidTradeException();
	}
	stockValidator.validateStock(trade.getStock());
    }
}
