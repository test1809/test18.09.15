package com.jpm.test.stocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.StockNotFoundException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.StockType;
import com.jpm.stocks.repository.StockRepository;
import com.jpm.stocks.repository.impl.GBCESampleStockRepository;

public class StockRepositoryTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final StockRepository stockRepository = new GBCESampleStockRepository();

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
    
    private static final BigDecimal FIFTY = new BigDecimal(50, MATH_CONTEXT);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100, MATH_CONTEXT);

    
    @Test
    public void commonStockHasNullFixedDividend() throws StockNotFoundException {
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);
	assertEquals(StockType.COMMON, stock.getType());
	assertNull(stock.getFixedDividend());
    }

    @Test
    public void preferredStockHasNotNullFixedDividend() throws StockNotFoundException {
	String stockSymbol = "GIN";
	Stock stock = stockRepository.getStock(stockSymbol);
	assertEquals(StockType.PREFERRED, stock.getType());
	assertNotNull(stock.getFixedDividend());
	assertEquals(0.02, stock.getFixedDividend().doubleValue(), 0.01);
    }

    @Test
    public void addNullStock() throws Exception {
	exception.expect(InvalidStockException.class);

	Stock stock = null;
	stockRepository.addStock(stock);
    }

    @Test
    public void addStockToRepository() throws Exception {
	String stockSymbol = "NEW.STOCK";
	Stock stock = new Stock(stockSymbol, FIFTY, ONE_HUNDRED);
	stockRepository.addStock(stock);
	stock = stockRepository.getStock(stockSymbol);
	assertNotNull(stock);
	assertEquals(StockType.COMMON, stock.getType());
    }

    @Test
    public void getNonexistentStock() throws StockNotFoundException {
	exception.expect(StockNotFoundException.class);

	String stockSymbol = "INVALID";
	stockRepository.getStock(stockSymbol);
    }
}
