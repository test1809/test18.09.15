# test18.09.15


1. Started by adding unit tests for calculating the Dividend Yield.
2. Fixed the compilation errors in the unit tests by creating the missing classes.
3. Fixed the failed tests.
4. After the tests passed I did a small refactoring: now there are 2 Stock constructors, one for common stocks and another one for preferred stocks.
The Fixed Dividend constructor parameter is required for a preferred stock and should not be provided and for a common stock.
Further an Abstract Stock base class could be created which could be extended by CommonStock and PreferredStock or a factory could be used for Stock instantiation.
5. Assumed that PER for preferred stocks is calculated using the fixed dividend.

