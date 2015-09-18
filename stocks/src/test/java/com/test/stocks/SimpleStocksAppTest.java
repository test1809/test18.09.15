package com.test.stocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.test.stocks.exception.StockNotFoundException;
import com.test.stocks.model.Stock;
import com.test.stocks.model.StockType;
import com.test.stocks.repository.StockRepository;
import com.test.stocks.service.StockService;

public class SimpleStocksAppTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final StockRepository stockRepository = new StockRepository();
    
    private final TradeRepository tradeRepository = new TradeRepository();

    private final StockService stockService = new StockService();

    @Test
    public void testCommonStockHasNullFixedDividend() throws StockNotFoundException {
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);
	assertEquals(stock.getType(), StockType.COMMON);
	assertNull(stock.getFixedDividend());
    }

    @Test
    public void testPreferredStockHasNotNullFixedDividend() throws StockNotFoundException {
	String stockSymbol = "GIN";
	Stock stock = stockRepository.getStock(stockSymbol);
	assertEquals(stock.getType(), StockType.PREFERRED);
	assertNotNull(stock.getFixedDividend());
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

	double dividendYield = stockService.getDividendYield(stock, tickerPrice);
	double expectedDividendYield = stock.getLastDividend() / tickerPrice;

	assertEquals(expectedDividendYield, dividendYield, 0.01);
    }

    @Test
    public void testCalculateDividentForPreferredStock() {
	String stockSymbol = "PREFERRED.STOCK";
	Stock stock = new Stock(stockSymbol, 2 / 100, 50, 100);
	assertEquals(stock.getType(), StockType.PREFERRED);
	double tickerPrice = 20;

	double dividendYield = stockService.getDividendYield(stock, tickerPrice);
	double expectedDividendYield = stock.getFixedDividend() * stock.getParValue() / tickerPrice;

	assertEquals(expectedDividendYield, dividendYield, 0.01);
    }

    @Test
    public void testCalculatePerForCommonStock() {
	String stockSymbol = "COMMON.STOCK";
	Stock stock = new Stock(stockSymbol, 5, 100);
	assertEquals(stock.getType(), StockType.COMMON);

	double tickerPrice = 20;

	double perRatio = stockService.getPerRatio(stock, tickerPrice);
	double expectedPerRatio = tickerPrice / stock.getLastDividend();

	assertEquals(expectedPerRatio, perRatio, 0.01);
    }
    
    @Test
    public void testCalculatePerForCommonStockWithZeroLastDividend() {
	String stockSymbol = "ZERO.DIVIDEND";
	Stock stock = new Stock(stockSymbol, 0, 100);
	assertEquals(stock.getType(), StockType.COMMON);

	double tickerPrice = 20;

	double perRatio = stockService.getPerRatio(stock, tickerPrice);
	double expectedPerRatio = Double.NaN;

	assertEquals(expectedPerRatio, perRatio, 0.01);
    }

    @Test
    public void testCalculatePerForPreferredStock() {
	String stockSymbol = "PREFERRED.STOCK";
	Stock stock = new Stock(stockSymbol, 2 / 100, 50, 100);
	assertEquals(stock.getType(), StockType.PREFERRED);
	double tickerPrice = 20;

	double perRatio = stockService.getPerRatio(stock, tickerPrice);
	double expectedPerRatio = tickerPrice / (stock.getFixedDividend() * stock.getParValue());

	assertEquals(expectedPerRatio, perRatio, 0.01);
    }
    
    
    @Test
    public void testCalculatePerForPreferredStockWithZeroFixedDividend() {
	String stockSymbol = "ZERO.DIVIDEND";
	Stock stock = new Stock(stockSymbol, 8, 100, 0);
	assertEquals(stock.getType(), StockType.PREFERRED);

	double tickerPrice = 20;

	double perRatio = stockService.getPerRatio(stock, tickerPrice);
	double expectedPerRatio = Double.NaN;

	assertEquals(expectedPerRatio, perRatio, 0.01);
    }
    
    
    
    @Test
    public void testRecordTrade() throws StockNotFoundException {
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);
	
	long tradeTime = Calendar.getInstance().getTimeInMillis();
	double price = 50;
	int numberOfShares = 1000;
	
	Trade newTrade = new Trade(stock, tradeTime, TradeType.BUY, price, numberOfShares); 
	tradeRepository.addTrade(trade);
	
	Trade repoTrade = tradeRepository.getTrade(stock, tradeTime);
	
	assertNotNull(repoTrade);
	assertEquals(newTrade, repoTrade);
    }
}
