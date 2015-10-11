package com.jpm.test.stocks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jpm.stocks.exception.InvalidTradeException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.Trade;
import com.jpm.stocks.model.TradeType;
import com.jpm.stocks.repository.StockRepository;
import com.jpm.stocks.repository.TradeRepository;
import com.jpm.stocks.repository.impl.GBCESampleStockRepository;
import com.jpm.stocks.repository.impl.TradeRepositoryImpl;

public class TradeRepositoryTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final StockRepository stockRepository = new GBCESampleStockRepository();
    private final TradeRepository tradeRepository = new TradeRepositoryImpl();
    
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
    private static final BigDecimal FIFTY = new BigDecimal(50, MATH_CONTEXT);
    
    @Test
    public void recordBuyTrade() throws Exception {
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);

	long tradeTime = Calendar.getInstance().getTimeInMillis();
	int numberOfShares = 1000;

	Trade newTrade = new Trade(stock, tradeTime, TradeType.BUY, FIFTY, numberOfShares);
	tradeRepository.addTrade(newTrade);

	List<Trade> repoTrades = tradeRepository.getTradesForStockAtTimestamp(stock, tradeTime);

	boolean tradeFoundInRepository = false;
	for (Trade trade : repoTrades) {
	    if (newTrade.equals(trade)) {
		tradeFoundInRepository = true;
		break;
	    }
	}
	assertTrue(tradeFoundInRepository);
    }
    
  
    @Test
    public void recordSellTrade() throws Exception {
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);

	long tradeTime = Calendar.getInstance().getTimeInMillis();
	int numberOfShares = 1000;

	Trade newTrade = new Trade(stock, tradeTime, TradeType.SELL, FIFTY, numberOfShares);
	tradeRepository.addTrade(newTrade);

	List<Trade> repoTrades = tradeRepository.getTradesForStockAtTimestamp(stock, tradeTime);

	boolean tradeFoundInRepository = false;
	for (Trade trade : repoTrades) {
	    if (newTrade.equals(trade)) {
		tradeFoundInRepository = true;
		break;
	    }
	}
	assertTrue(tradeFoundInRepository);
    }
    
    @Test
    public void getTradesForStockAtTimestamp() throws Exception {
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);

	long tradeTime = Calendar.getInstance().getTimeInMillis();
	int numberOfShares = 1000;

	Trade newBuyTrade = new Trade(stock, tradeTime, TradeType.BUY, FIFTY, numberOfShares);
	tradeRepository.addTrade(newBuyTrade);
	
	Trade newSellTrade = new Trade(stock, tradeTime, TradeType.BUY, FIFTY, numberOfShares);
	tradeRepository.addTrade(newSellTrade);

	List<Trade> repoTrades = tradeRepository.getTradesForStockAtTimestamp(stock, tradeTime);

	boolean buyTradeFoundInRepository = false;
	boolean sellTradeFoundInRepository = false;
	
	for (Trade trade : repoTrades) {
	    if (newBuyTrade.equals(trade)) {
		buyTradeFoundInRepository = true;
	    }
	    if (newSellTrade.equals(trade)) {
		sellTradeFoundInRepository = true;
	    }
	}
	assertTrue(buyTradeFoundInRepository);
	assertTrue(sellTradeFoundInRepository);
    }

    @Test
    public void getUnexistingTradeAtTimestamp() throws Exception {
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);

	long tradeTime = Calendar.getInstance().getTimeInMillis();
	int numberOfShares = 1000;

	Trade newTrade = new Trade(stock, tradeTime, TradeType.BUY, FIFTY, numberOfShares);
	tradeRepository.addTrade(newTrade);

	List<Trade> repoTrades = tradeRepository.getTradesForStockAtTimestamp(stock, tradeTime + 1);

	boolean tradeFoundInRepository = false;
	for (Trade trade : repoTrades) {
	    if (newTrade.equals(trade)) {
		tradeFoundInRepository = true;
		break;
	    }
	}
	assertFalse(tradeFoundInRepository);
    }
    
    @Test
    public void recordBuyTradeWithInvalidTimestamp() throws Exception {
	exception.expect(InvalidTradeException.class);
	
	String stockSymbol = "TEA";
	Stock stock = stockRepository.getStock(stockSymbol);

	long tradeTime = -1;
	int numberOfShares = 1000;

	Trade newTrade = new Trade(stock, tradeTime, TradeType.BUY, FIFTY, numberOfShares);
	tradeRepository.addTrade(newTrade);
    }
}
