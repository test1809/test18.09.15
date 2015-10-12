package com.jpm.test.stocks;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jpm.stocks.exception.IndexWithoutTradesWithinPriceIntervalException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.Trade;
import com.jpm.stocks.model.TradeType;
import com.jpm.stocks.repository.StockRepository;
import com.jpm.stocks.repository.TradeRepository;
import com.jpm.stocks.repository.impl.GBCESampleStockRepository;
import com.jpm.stocks.repository.impl.TradeRepositoryImpl;
import com.jpm.stocks.service.StockService;

public class AllSharesIndexTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final StockService stockService = new StockService();

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
    private static final BigDecimal FIVE = new BigDecimal(5, MATH_CONTEXT);
    private static final BigDecimal FOURTY = new BigDecimal(40, MATH_CONTEXT);
    
    private static final BigDecimal EIGHT = new BigDecimal(8, MATH_CONTEXT);
    private static final BigDecimal THIRTY_TWO = new BigDecimal(32, MATH_CONTEXT);

    private final static int NUMBER_OF_SHARES = 1000;

    @Before
    public void initializeRepositories() throws Exception {
	StockRepository stockRepository = new GBCESampleStockRepository();
	stockService.setStockRepository(stockRepository);
	
	TradeRepository tradeRepository = new TradeRepositoryImpl();
	stockService.setTradeRepository(tradeRepository);
    }

    private void resetTradeRepository() {
	TradeRepository tradeRepository = new TradeRepositoryImpl();
	stockService.setTradeRepository(tradeRepository);
    }

    private void addTrades(String stockSymbol, BigDecimal transationPrice) throws Exception {
	Stock stock = stockService.getStock(stockSymbol);

	Calendar calendar = Calendar.getInstance();
	long tradeTime = calendar.getTimeInMillis();

	stockService.addTrade(new Trade(stock, tradeTime, TradeType.SELL, transationPrice, NUMBER_OF_SHARES));
	stockService.addTrade(new Trade(stock, tradeTime, TradeType.BUY, transationPrice, NUMBER_OF_SHARES));
    }
    

    @Test
    public void calculateAllSharesIndexForEmptyTradeRepository() throws Exception {
	exception.expect(IndexWithoutTradesWithinPriceIntervalException.class);
	
	resetTradeRepository();
	stockService.getAllSharesIndex();
    }

    @Test
    public void calculateAllSharesIndexForOneStock() throws Exception {
	resetTradeRepository();
	addTrades("TEA", FOURTY);
	
	BigDecimal allSharesIndex = stockService.getAllSharesIndex();

	BigDecimal expectedAllSharesIndex = FOURTY;
	assertEquals(0, expectedAllSharesIndex.compareTo(allSharesIndex));
    }
    
    @Test
    public void calculateAllSharesIndexForTwoStocks1() throws Exception {
	resetTradeRepository();
	addTrades("TEA", EIGHT);
	addTrades("POP", THIRTY_TWO);
	
	BigDecimal allSharesIndex = stockService.getAllSharesIndex();

	BigDecimal expectedAllSharesIndex = new BigDecimal(16, MATH_CONTEXT);;
	assertEquals(0, expectedAllSharesIndex.compareTo(allSharesIndex));
    }
    

    @Test
    public void calculateAllSharesIndexForTwoStocks2() throws Exception {
	resetTradeRepository();
	addTrades("TEA", FIVE);
	addTrades("POP",  new BigDecimal(125, MATH_CONTEXT));
	
	BigDecimal allSharesIndex = stockService.getAllSharesIndex();

	BigDecimal expectedAllSharesIndex =  new BigDecimal(25, MATH_CONTEXT);;
	assertEquals(0, expectedAllSharesIndex.compareTo(allSharesIndex));
    }

}
