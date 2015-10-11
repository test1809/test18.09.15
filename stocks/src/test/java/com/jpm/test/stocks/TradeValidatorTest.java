package com.jpm.test.stocks;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidTradeException;
import com.jpm.stocks.model.Stock;
import com.jpm.stocks.model.Trade;
import com.jpm.stocks.model.TradeType;
import com.jpm.stocks.repository.StockRepository;
import com.jpm.stocks.repository.impl.GBCESampleStockRepository;
import com.jpm.stocks.validator.TradeValidator;

public class TradeValidatorTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final StockRepository stockRepository = new GBCESampleStockRepository();

    private final TradeValidator tradeValidator = new TradeValidator();

    private Stock teaStock;

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
    private static final BigDecimal FIFTY = new BigDecimal(50, MATH_CONTEXT);

    @Before
    public void getSampleStocks() throws Exception {
	teaStock = stockRepository.getStock("TEA");
    }

    @Test
    public void tradeWithInvalidTimestamp() throws Exception {
	exception.expect(InvalidTradeException.class);

	long tradeTime = -1;
	int numberOfShares = 1000;

	Trade trade = new Trade(teaStock, tradeTime, TradeType.BUY, FIFTY, numberOfShares);

	tradeValidator.validateTrade(trade);
    }

    @Test
    public void tradeWithNullStock() throws Exception {
	exception.expect(InvalidStockException.class);

	Stock stock = null;

	long tradeTime = Calendar.getInstance().getTimeInMillis();
	int numberOfShares = 1000;

	Trade trade = new Trade(stock, tradeTime, TradeType.BUY, FIFTY, numberOfShares);

	tradeValidator.validateTrade(trade);
    }

    @Test
    public void tradeWithNegativePrice() throws Exception {
	exception.expect(InvalidTradeException.class);

	long tradeTime = Calendar.getInstance().getTimeInMillis();
	BigDecimal price = FIFTY.negate(MATH_CONTEXT);
	int numberOfShares = 1000;

	Trade trade = new Trade(teaStock, tradeTime, TradeType.BUY, price, numberOfShares);
	tradeValidator.validateTrade(trade);
    }

    @Test
    public void tradeWithoutType() throws Exception {
	exception.expect(InvalidTradeException.class);

	long tradeTime = Calendar.getInstance().getTimeInMillis();
	int numberOfShares = 1000;

	Trade trade = new Trade(teaStock, tradeTime, null, FIFTY, numberOfShares);
	tradeValidator.validateTrade(trade);
    }

    @Test
    public void tradeWithInvalidSharesNumber() throws Exception {
	exception.expect(InvalidTradeException.class);

	long tradeTime = Calendar.getInstance().getTimeInMillis();
	int numberOfShares = 0;

	Trade trade = new Trade(teaStock, tradeTime, null, FIFTY, numberOfShares);
	tradeValidator.validateTrade(trade);
    }
}
