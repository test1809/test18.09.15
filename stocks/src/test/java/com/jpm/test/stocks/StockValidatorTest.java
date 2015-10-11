package com.jpm.test.stocks;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidStockSymbolException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.validator.StockValidator;

public class StockValidatorTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private StockValidator stockValidator = new StockValidator();
    
    private static final BigDecimal FIFTY =  BigDecimal.valueOf(50);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    @Test
    public void nullStock() throws Exception {
	exception.expect(InvalidStockException.class);
	Stock stock = null;
	stockValidator.validateStock(stock);
    }

    @Test
    public void stockWithTooLongSymbol() throws Exception {
	exception.expect(InvalidStockSymbolException.class);
	String stockSymbol = "TOO.LONG.SYMBOL";
	Stock stock = new Stock(stockSymbol, FIFTY, ONE_HUNDRED);
	stockValidator.validateStock(stock);
    }

    @Test
    public void stockWithInvalidCharacterInSymbol() throws Exception {
	exception.expect(InvalidStockSymbolException.class);
	String stockSymbol = "STOCK_1";
	Stock stock = new Stock(stockSymbol, FIFTY, ONE_HUNDRED);
	stockValidator.validateStock(stock);
    }

    @Test
    public void stockWithNullSymbol() throws Exception {
	exception.expect(InvalidStockSymbolException.class);
	String stockSymbol = null;
	Stock stock = new Stock(stockSymbol, FIFTY, ONE_HUNDRED);
	stockValidator.validateStock(stock);
    }

    @Test
    public void stockWithEmptySymbol() throws Exception {
	exception.expect(InvalidStockSymbolException.class);
	String stockSymbol = "";
	Stock stock = new Stock(stockSymbol, FIFTY, ONE_HUNDRED);
	stockValidator.validateStock(stock);
    }
}
