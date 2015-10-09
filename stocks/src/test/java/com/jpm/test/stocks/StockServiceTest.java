package com.jpm.test.stocks;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.StockType;
import com.jpm.stocks.service.StockService;

public class StockServiceTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final StockService stockService = new StockService();

    @Test
    public void calculateDividentForCommonStock() {
	String stockSymbol = "COMMON.STOCK";
	Stock stock = new Stock(stockSymbol, 5, 100);
	assertEquals(stock.getType(), StockType.COMMON);

	double tickerPrice = 20;

	double dividendYield = stockService.getDividendYield(stock, tickerPrice);
	double expectedDividendYield = stock.getLastDividend() / tickerPrice;

	assertEquals(expectedDividendYield, dividendYield, 0.01);
    }

    @Test
    public void calculateDividentForPreferredStock() {
	String stockSymbol = "PREFERRED.STOCK";
	Stock stock = new Stock(stockSymbol, 2 / 100, 50, 100);
	assertEquals(stock.getType(), StockType.PREFERRED);
	double tickerPrice = 20;

	double dividendYield = stockService.getDividendYield(stock, tickerPrice);
	double expectedDividendYield = stock.getFixedDividend() * stock.getParValue() / tickerPrice;

	assertEquals(expectedDividendYield, dividendYield, 0.01);
    }

    @Test
    public void calculatePerForCommonStock() {
	String stockSymbol = "COMMON.STOCK";
	Stock stock = new Stock(stockSymbol, 5, 100);
	assertEquals(stock.getType(), StockType.COMMON);

	double tickerPrice = 20;

	double perRatio = stockService.getPerRatio(stock, tickerPrice);
	double expectedPerRatio = tickerPrice / stock.getLastDividend();

	assertEquals(expectedPerRatio, perRatio, 0.01);
    }

    @Test
    public void calculatePerForCommonStockWithZeroLastDividend() {
	String stockSymbol = "ZERO.DIVIDEND";
	Stock stock = new Stock(stockSymbol, 0, 100);
	assertEquals(stock.getType(), StockType.COMMON);

	double tickerPrice = 20;

	double perRatio = stockService.getPerRatio(stock, tickerPrice);
	double expectedPerRatio = Double.NaN;

	assertEquals(expectedPerRatio, perRatio, 0.01);
    }

    @Test
    public void calculatePerForPreferredStock() {
	String stockSymbol = "PREFERRED.STOCK";
	Stock stock = new Stock(stockSymbol, 2 / 100, 50, 100);
	assertEquals(stock.getType(), StockType.PREFERRED);
	double tickerPrice = 20;

	double perRatio = stockService.getPerRatio(stock, tickerPrice);
	double expectedPerRatio = tickerPrice / (stock.getFixedDividend() * stock.getParValue());

	assertEquals(expectedPerRatio, perRatio, 0.01);
    }

    @Test
    public void calculatePerForPreferredStockWithZeroFixedDividend() {
	String stockSymbol = "ZERO.DIVIDEND";
	Stock stock = new Stock(stockSymbol, 8, 100, 0);
	assertEquals(stock.getType(), StockType.PREFERRED);

	double tickerPrice = 20;

	double perRatio = stockService.getPerRatio(stock, tickerPrice);
	double expectedPerRatio = Double.NaN;

	assertEquals(expectedPerRatio, perRatio, 0.01);
    }

}
