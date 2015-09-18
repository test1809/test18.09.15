package com.test.stocks;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SimpleStocksAppTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    public final StockRepository stockRepository;

    public final DividentCalculator dividentCalculator;

    @Before
    public void initTest() {
	stockRepository = new StockRepository();
    }

    @Test
    public void testCommonStockHasNullFixedDividend() {
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);
	assertEquals(stock.getType(), StockType.Common);
	assertNull(stock.getFixedDivident());
    }

    @Test
    public void testPreferredStockHasNotNullFixedDividend() {
	String stockSymbol = "GIN";
	Stock stock = stockRepository.getStock(stockSymbol);
	assertEquals(stock.getType(), StockType.Preferred);
	assertNotNull(stock.getFixedDivident());
    }

    @Test
    public void testAddStockToRepository() throws StockNotFoundException {
	String stockSymbol = "NEW.STOCK";
	Stock stock = new Stock(stockSymbol, StockType.Common, 50, 100);
	stockRepository.addStock(stock);

	stock = stockRepository.getStock(stockSymbol);
	assertNotNull(stock);
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
	Stock stock = new Stock(stockSymbol, StockType.Common, 5, 100);
	double tickerPrice = 20;

	double dividendYield = dividentCalculator.getDividendYield(stock);

	assertEquals(dividendYield, 5 / 20, 0.01);
    }

    @Test
    public void testCalculateDividentForPreferredStock() {
	String stockSymbol = "PREFERRED.STOCK";
	Stock stock = new Stock(stockSymbol, StockType.Preferred, 2 / 100, 50, 100);
	double tickerPrice = 20;

	double dividendYield = dividentCalculator.getDividendYield(stock);

	assertEquals(dividendYield, (2 / 100) * 100 / 20, 0.01);
    }

}
