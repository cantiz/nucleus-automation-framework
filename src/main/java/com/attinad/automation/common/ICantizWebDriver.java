package com.attinad.automation.common;

public interface ICantizWebDriver {

	void sendKeyStrokes(Locators locator, String locatorValue, String keyStrokes);

	void clickElement(Locators locator, String locatorValue);

	void selectValue(Locators locator, String locatorValue, String valueToSelect);

	void selectMenuElement(String menuFirstLevelId, String menuSecondLevelId);

	void clearElement(Locators locator, String locatorValue);

	Boolean checkValueInsideWebElement(String valueToCheck, String elementId , String elementType);

	Boolean checkElementById(String locatorValue);
	void closeDriver();

	String getAttributeTypeOfWebElement(Locators id, String idPassword);

	void navigateToPage(String pageURL);

	String getHtmlValueById(String htmlContent, String locator);

	
}
