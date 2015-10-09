package com.jpm.stocks.validator;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidStockSymbolException;
import com.jpm.stocks.model.Stock;

public class StockValidator {
    private static final int MAX_STOCK_SYMBOL_LENGTH = 10;

    public void validateStock(Stock stock) throws InvalidStockException, InvalidStockSymbolException {
	if (stock == null || stock.getType() == null || stock.getParValue() == null) {
	    throw new InvalidStockException();
	}

	switch (stock.getType()) {
	case COMMON:
	    validateCommonStock(stock);
	    break;
	case PREFERRED:
	    validatePreferredStock(stock);
	    break;
	}

	String stockSymbol = stock.getStockSymbol();
	if (!isValidStockSymbol(stockSymbol)) {
	    throw new InvalidStockSymbolException();
	}
    }

    private void validatePreferredStock(Stock stock) throws InvalidStockException {
	if (stock.getFixedDividend() == null) {
	    throw new InvalidStockException();
	}
    }

    private void validateCommonStock(Stock stock) throws InvalidStockException {
	if (stock.getLastDividend() == null) {
	    throw new InvalidStockException();
	}
    }

    private boolean isValidStockSymbol(String stockSymbol) {
	if (stockSymbol == null || stockSymbol.equals("")) {
	    return false;
	}

	if (stockSymbol.length() > MAX_STOCK_SYMBOL_LENGTH) {
	    return false;
	}

	for (char c : stockSymbol.toCharArray()) {
	    if (!Character.isLetterOrDigit(c) && c != '.') {
		return false;
	    }
	}
	return true;
    }

}
