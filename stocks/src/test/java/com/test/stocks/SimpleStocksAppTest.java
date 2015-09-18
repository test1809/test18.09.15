package com.test.stocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.test.stocks.exception.StockNotFoundException;
import com.test.stocks.model.Stock;
import com.test.stocks.model.StockType;
import com.test.stocks.repository.StockRepository;
import com.test.stocks.service.DividentCalculator;

public class SimpleStocksAppTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final StockRepository stockRepository = new StockRepository();

    private final DividentCalculator dividentCalculator = new DividentCalculator();

    @Test
    public void testCommonStockHasNullFixedDividend() throws StockNotFoundException {
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);
	assertEquals(stock.getType(), StockType.COMMON);
	assertNull(stock.getFixedDivident());
    }

    @Test
    public void testPreferredStockHasNotNullFixedDividend() throws StockNotFoundException {
	String stockSymbol = "GIN";
	Stock stock = stockRepository.getStock(stockSymbol);
	assertEquals(stock.getType(), StockType.PREFERRED);
	assertNotNull(stock.getFixedDivident());
    }

    @Test
    public void testAddStockToRepository() throws StockNotFoundException {
	String stockSymbol = "NEW.STOCK";
	Stock stock = new Stock(stockSymbol, 50, 100);
	stockRepository.addStock(stock);

	stock = stockRepository.getStock(stockSymbol);
	assertNotNull(stock);
	assertEquals(stock.getType(), StockType.COMMON);
    }

    @Test
    public void testGetNonexistentStock() throws StockNotFoundException {
	exception.expect(StockNotFoundException.class);

	String stockSymbol = "INVALID";
	stockRepository.getStock(stockSymbol);
    }

    @Test
    public void testCalculateDividentForCommonStock() {
	String stockSymbol = "COMMON.STOCK";
	Stock stock = new Stock(stockSymbol, 5, 100);
	assertEquals(stock.getType(), StockType.COMMON);
	
	double tickerPrice = 20;

	double dividendYield = dividentCalculator.getDividendYield(stock, tickerPrice);
	double expectedDividendYield = stock.getLastDividend() / tickerPrice;

	assertEquals(expectedDividendYield, dividendYield, 0.01);
    }

    @Test
    public void testCalculateDividentForPreferredStock() {
	String stockSymbol = "PREFERRED.STOCK";
	Stock stock = new Stock(stockSymbol, 2 / 100, 50, 100);
	assertEquals(stock.getType(), StockType.PREFERRED);
	double tickerPrice = 20;

	double dividendYield = dividentCalculator.getDividendYield(stock, tickerPrice);
	double expectedDividendYield = stock.getFixedDivident() * stock.getParValue() / tickerPrice;

	assertEquals(expectedDividendYield, dividendYield, 0.01);
    }

}
