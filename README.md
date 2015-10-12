# test18.09.15

Started by adding unit tests for calculating the Dividend Yield.
The dividend yield is calculated differently for a common stock and preferred stock therefore I have created 1 test method for each stock type.
Fixed the compilation errors in the unit tests by creating the missing classes.
Stock class contains data about a stock. 
StockService contains the service method for calculating the dividend yield for a given stock and its ticker price. 
Fixed the failing tests.
After the tests passed I did a small refactoring: now there are 2 Stock constructors, one for common stocks and another one for preferred stocks.
The Fixed Dividend constructor parameter is required for a preferred stock and should not be provided and for a common stock.
Further an Abstract Stock base class could be created which could be extended by CommonStock and PreferredStock or a factory could be used for Stock instantiation.

Added tests for calculating PER for common stocks and preferred stocks respectively. Added a test for a preferred stock with 0 fixed dividend but not sure if such stock exists.
Fixed the compilation errors for the unit tests by adding the service method getPerRatio.
Implemented getPerRatio so that the unit tests no longer fail. Assumed that PER for preferred stocks is calculated using the fixed dividend.

Added tests for recording a buy trade and respectively a sell trade. 
Fixed the test compilation errors by adding 
				Trade class to represent a trade, 
				StockRepository class with a method to retrieve stock data by its symbol
				TradeReposotory class with a method to get the trades for stock at a given time.				
Implemented the service methods so that the tests pass.

Renamed the packages and the test methods.

Added GBCESampleStockRepository class and StockRepository interface so that there will be a dependency of an abstraction and not on a class that contains sample data.
Similarly added TradeRepositoryImpl class and TradeRepository interface. 
This way the repository implementation could be switched easily and a database repository could be used instead of the sample data repository for stocks.
Will not add an interface for StockService as I do not plan to integrate Spring and use dependency injection for this simple test project.
GBCESampleStockRepository adds the sample data in the file provided. The data is assumed to be valid. No exception is expected while loading sample data therefore the catch clauses do not need to handle the exceptions. 
It is a bad practice to swallow the exceptions. As no logging mechanism is to be used, I left the generated printStackTrace in the catch clauses.  

I have refactored the code to change from double to BigDecimal. When exact results are required it is recommended to use BigDecimal instead of float or double. 
The reason is that Java stores values for float or double with an inexact representation.
Using BigDecimal in calculations results in a much lower performance than using primitive types. Primitives are also faster in calculations than boxed primitives. 
When precision is required but it is not possible to use BigDecimal there are other options like using long or int and keep track of the decimal point.
For this test I have decided to use BigDecimal as there are no performance requirements.

Added tests for stock price calculation.
Fixed tests compilation by creating missing method, class.
Implemented stock price calculation so that the tests will pass.
In a more elaborate application StockRepository and TradeRepository implementations would be autowired in StockService.


Added tests for all shares index calculation.













	

			

