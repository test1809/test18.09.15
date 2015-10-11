package com.jpm.test.stocks;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.Before;
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

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
    private static final RoundingMode ROUNDING_MODE = MATH_CONTEXT.getRoundingMode();
    private static final int SCALE = 2;

    private static final BigDecimal TWENTY = BigDecimal.valueOf(20);
    private static final BigDecimal FIVE = BigDecimal.valueOf(5);
    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private Stock commonStock;
    private Stock preferredStock;

    @Before
    public void initializeTestData() {
	commonStock = new Stock("COMMON.STOCK", FIVE, ONE_HUNDRED);
	preferredStock = new Stock("PREFERRED.STOCK", TWO.divide(ONE_HUNDRED, SCALE, ROUNDING_MODE), FIVE, ONE_HUNDRED);
    }
    
    @Test
    public void stockTypeCorrectlyAssigned() {
	assertEquals(commonStock.getType(), StockType.COMMON);
	assertEquals(preferredStock.getType(), StockType.PREFERRED);
    }

    @Test
    public void calculateDividentForCommonStock() {
	BigDecimal dividendYield = stockService.getDividendYield(commonStock, TWENTY);
	BigDecimal expectedDividendYield = commonStock.getLastDividend().divide(TWENTY, SCALE, ROUNDING_MODE);

	assertEquals(expectedDividendYield.compareTo(dividendYield), 0);
    }

    @Test
    public void calculateDividentForPreferredStock() {
	BigDecimal dividendYield = stockService.getDividendYield(preferredStock, TWENTY);
	BigDecimal expectedDividendYield = preferredStock.getFixedDividend().multiply(preferredStock.getParValue()).divide(TWENTY, SCALE, ROUNDING_MODE);

	assertEquals(expectedDividendYield.compareTo(dividendYield), 0);
    }

    @Test
    public void calculatePerForCommonStock() {
	BigDecimal perRatio = stockService.getPerRatio(commonStock, TWENTY);
	BigDecimal expectedPerRatio = TWENTY.divide(commonStock.getLastDividend(), SCALE, ROUNDING_MODE);

	assertEquals(expectedPerRatio.compareTo(perRatio), 0);
	assertEquals(perRatio.scale(), SCALE);
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
	BigDecimal expectedPerRatio = TWENTY.divide(preferredStock.getFixedDividend().multiply(preferredStock.getParValue()), SCALE, ROUNDING_MODE);

	assertEquals(expectedPerRatio.compareTo(perRatio), 0);
	assertEquals(perRatio.scale(), SCALE);
    }

    @Test
    public void calculatePerForPreferredStockWithZeroFixedDividend() {
	String stockSymbol = "ZERO.DIVIDEND";
	Stock stock = new Stock(stockSymbol, BigDecimal.valueOf(8), ONE_HUNDRED, BigDecimal.ZERO);

	BigDecimal perRatio = stockService.getPerRatio(stock, TWENTY);
	assertNull(perRatio);
    }

}
