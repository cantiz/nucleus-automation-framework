package com.attinad.automation.common;

public interface ICantizWebDriver {

	void sendKeyStrokes(Locators locator, String locatorValue, String keyStrokes);
	
	void sendSpecialKey(Locators locator, String locatorValue, String specialKey);
	
	void clickElement(Locators locator, String locatorValue);
	
	void selectValue(Locators locator, String locatorValue, String valueToSelect);

	void selectMenuElement(String menuFirstLevelId, String menuSecondLevelId);

	void clearElement(Locators locator, String locatorValue);

	Boolean checkValueInsideWebElement(String valueToCheck, Locators locator , String locatorValue, String elementType);
	
	Boolean isElementEmpty(Locators locator, String locatorValue, String elementType);
	
	Boolean checkElementById(String locatorValue);
	
	Boolean checkElementByXpath(String xpathValue);
	
	Boolean checkElementByCss(String cssValue);
	
	Boolean checkIfElementDisappearedById(String locatorValue);
	
	Boolean checkIfElementDisappearedByClass(String locatorValue);
	
	Boolean isElementPresentById(String locatorValue);
	
	void closeDriver();

	String getAttributeTypeOfWebElement(Locators id, String idPassword);

	void navigateToPage(String pageURL);

	String getHtmlValueById(String htmlContent, String locator);
	
	String getCookieValueByName(String cookieName);
	
	Boolean isElementSelected(Locators locator, String locatorValue);
	
	Boolean isElementEnabled(Locators locator, String locatorValue);
	
	void scrollPageUp(String numberOfPixels);
	
	void scrollPageDown(String numberOfPixels);
	
	void refreshPage();
	
}