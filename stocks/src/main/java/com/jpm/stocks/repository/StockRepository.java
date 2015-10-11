package com.jpm.stocks.repository;

import com.jpm.stocks.exception.InvalidStockException;
import com.jpm.stocks.exception.InvalidStockSymbolException;
import com.jpm.stocks.exception.StockNotFoundException;
import com.jpm.stocks.model.Stock;

public interface StockRepository {
    Stock getStock(String stockSymbol) throws StockNotFoundException;

    void addStock(Stock stock) throws InvalidStockException, InvalidStockSymbolException;

}
