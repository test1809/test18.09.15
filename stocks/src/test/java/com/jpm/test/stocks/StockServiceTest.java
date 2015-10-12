package com.jpm.test.stocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jpm.stocks.exception.StockWithoutTradesWithinPriceIntervalException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.StockType;
import com.jpm.stocks.model.Trade;
import com.jpm.stocks.model.TradeType;
import com.jpm.stocks.repository.StockRepository;
import com.jpm.stocks.repository.TradeRepository;
import com.jpm.stocks.repository.impl.GBCESampleStockRepository;
import com.jpm.stocks.repository.impl.TradeRepositoryImpl;
import com.jpm.stocks.service.StockService;

public class StockServiceTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final StockService stockService = new StockService();

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
    private static final RoundingMode ROUNDING_MODE = MATH_CONTEXT.getRoundingMode();
    private static final int SCALE = 2;

    private static final BigDecimal TWENTY = new BigDecimal(20, MATH_CONTEXT);
    private static final BigDecimal FIVE = new BigDecimal(5, MATH_CONTEXT);
    private static final BigDecimal TWO = new BigDecimal(2, MATH_CONTEXT);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100, MATH_CONTEXT);
    private static final BigDecimal FIFTY = new BigDecimal(50, MATH_CONTEXT);
    private static final BigDecimal FOURTY = new BigDecimal(40, MATH_CONTEXT);

    private static final int MAX_MINUTES_FOR_PRICE_CALCULATION = 15;

    private Stock commonStock;
    private Stock preferredStock;

    @Before
    public void initializeTestData() throws Exception {
	commonStock = new Stock("COMMON.STOCK", FIVE, ONE_HUNDRED);
	preferredStock = new Stock("PREFERRED.STOCK", TWO.divide(ONE_HUNDRED, SCALE, ROUNDING_MODE), FIVE, ONE_HUNDRED);

	StockRepository stockRepository = new GBCESampleStockRepository();
	stockService.setStockRepository(stockRepository);
	TradeRepository tradeRepository = new TradeRepositoryImpl();
	stockService.setTradeRepository(tradeRepository);

	int numberOfShares = 1000;
	Calendar calendar = Calendar.getInstance();
	long tradeTime = calendar.getTimeInMillis();

	Stock teaStock = stockRepository.getStock("TEA");
	tradeRepository.addTrade(new Trade(teaStock, tradeTime, TradeType.SELL, FIFTY, numberOfShares));
	tradeRepository.addTrade(new Trade(teaStock, tradeTime, TradeType.BUY, FIFTY, numberOfShares));
	tradeRepository.addTrade(new Trade(teaStock, tradeTime, TradeType.SELL, FIFTY.add(TWO), numberOfShares));
	tradeRepository.addTrade(new Trade(teaStock, tradeTime, TradeType.BUY, FIFTY.add(TWO), numberOfShares));

	Stock popStock = stockRepository.getStock("POP");
	tradeRepository.addTrade(new Trade(popStock, tradeTime, TradeType.SELL, ONE_HUNDRED, numberOfShares));

	long tradeTimeOutsideInterval = calendar.getTimeInMillis() - (MAX_MINUTES_FOR_PRICE_CALCULATION + 1) * 60 * 1000;
	Stock ginStock = stockRepository.getStock("GIN");
	tradeRepository.addTrade(new Trade(ginStock, tradeTimeOutsideInterval, TradeType.SELL, TWO, numberOfShares));

	Stock joeStock = stockRepository.getStock("JOE");
	tradeRepository.addTrade(new Trade(joeStock, tradeTime, TradeType.SELL, FOURTY, numberOfShares));
	tradeRepository.addTrade(new Trade(joeStock, tradeTime, TradeType.BUY, FOURTY, numberOfShares));
	tradeRepository.addTrade(new Trade(joeStock, tradeTimeOutsideInterval, TradeType.SELL, FOURTY.add(TWO), numberOfShares));
	tradeRepository.addTrade(new Trade(joeStock, tradeTimeOutsideInterval, TradeType.BUY, FOURTY.add(TWO), numberOfShares));
    }

    @Test
    public void stockTypeCorrectlyAssigned() {
	assertEquals(StockType.COMMON, commonStock.getType());
	assertEquals(StockType.PREFERRED, preferredStock.getType());
    }

    @Test
    public void calculateDividentForCommonStock() {
	BigDecimal dividendYield = stockService.getDividendYield(commonStock, TWENTY);
	BigDecimal expectedDividendYield = commonStock.getLastDividend().divide(TWENTY, SCALE, ROUNDING_MODE);

	assertEquals(0, expectedDividendYield.compareTo(dividendYield));
    }

    @Test
    public void calculateDividentForPreferredStock() {
	BigDecimal dividendYield = stockService.getDividendYield(preferredStock, TWENTY);
	BigDecimal expectedDividendYield = preferredStock.getFixedDividend().multiply(preferredStock.getParValue(), MATH_CONTEXT).divide(TWENTY,
		SCALE, ROUNDING_MODE);

	assertEquals(0, expectedDividendYield.compareTo(dividendYield));
    }

    @Test
    public void calculatePerForCommonStock() {
	BigDecimal perRatio = stockService.getPerRatio(commonStock, TWENTY);
	BigDecimal expectedPerRatio = TWENTY.divide(commonStock.getLastDividend(), SCALE, ROUNDING_MODE);

	assertEquals(0, expectedPerRatio.compareTo(perRatio));
	assertEquals(SCALE, perRatio.scale());
    }

    @Test
    public void calculatePerForCommonStockWithZeroLastDividend() {
	String stockSymbol = "ZERO.DIVIDEND";
	Stock stock = new Stock(stockSymbol, BigDecimal.ZERO, ONE_HUNDRED);

	BigDecimal perRatio = stockService.getPerRatio(stock, TWENTY);
	assertNull(perRatio);
    }

    @Test
    public void calculatePerForPreferredStock() {
	BigDecimal perRatio = stockService.getPerRatio(preferredStock, TWENTY);
	BigDecimal expectedPerRatio = TWENTY.divide(preferredStock.getFixedDividend().multiply(preferredStock.getParValue(), MATH_CONTEXT), SCALE,
		ROUNDING_MODE);

	assertEquals(0, expectedPerRatio.compareTo(perRatio));
	assertEquals(SCALE, perRatio.scale());
    }

    @Test
    public void calculatePerForPreferredStockWithZeroFixedDividend() {
	String stockSymbol = "ZERO.DIVIDEND";
	Stock stock = new Stock(stockSymbol, new BigDecimal(8, MATH_CONTEXT), ONE_HUNDRED, BigDecimal.ZERO);
	BigDecimal perRatio = stockService.getPerRatio(stock, TWENTY);
	assertNull(perRatio);
    }

    @Test
    public void calculatePriceFor4Trades() throws Exception {
	String stockSymbol = "TEA";
	BigDecimal stockPrice = stockService.getStockPrice(stockSymbol);
	BigDecimal expectedStockPrice = FIFTY.add(BigDecimal.ONE);
	assertEquals(0, expectedStockPrice.compareTo(stockPrice));
    }

    @Test
    public void calculatePriceFor1Trade() throws Exception {
	String stockSymbol = "POP";
	BigDecimal stockPrice = stockService.getStockPrice(stockSymbol);
	BigDecimal expectedStockPrice = ONE_HUNDRED;
	assertEquals(0, expectedStockPrice.compareTo(stockPrice));
    }

    @Test
    public void calculatePriceForStockWithoutTrades() throws Exception {
	exception.expect(StockWithoutTradesWithinPriceIntervalException.class);
	String stockSymbol = "ALE";
	stockService.getStockPrice(stockSymbol);
    }

    @Test
    public void calculatePriceForStockWithoutTradesWithinPriceInterval() throws Exception {
	exception.expect(StockWithoutTradesWithinPriceIntervalException.class);
	String stockSymbol = "GIN";
	stockService.getStockPrice(stockSymbol);
    }

    @Test
    public void calculatePriceFor2TradesWithinAnd2OutsitePriceInterval() throws Exception {
	String stockSymbol = "JOE";
	BigDecimal stockPrice = stockService.getStockPrice(stockSymbol);
	BigDecimal expectedStockPrice = FOURTY;
	assertEquals(0, expectedStockPrice.compareTo(stockPrice));
    }
}
