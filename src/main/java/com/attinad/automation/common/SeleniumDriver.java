package com.attinad.automation.common;

import com.attinad.automation.utils.CantizAutomationCoreException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.attinad.automation.common.Locators;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleniumDriver implements ICantizWebDriver {
	private PropertyReader propReader = null;
	private WebDriver driver = null;
	Logger logger = Logger.getAnonymousLogger();
	
	public SeleniumDriver(PropertyReader propReader) throws CantizAutomationCoreException {
		this.propReader = propReader;
		if ("remote".equalsIgnoreCase(propReader.getDriverType())) {
			initializeRemoteDriver();
		} else {
			initializeDriver();
		}
		/* Implicit wait doesn't work in Safari browser
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS); */
		driver.get(this.propReader.getUrl());
		driver.manage().window().maximize();
	}

	public void initializeDriver() {
		String osName = System.getProperty("os.name");
		String osWindows = "Windows";
		String osLinux = "Linux";
		String chromeDriver = "webdriver.chrome.driver";
		String ieDriver = "webdriver.ie.driver";
		String firefoxDriver = "webdriver.gecko.driver";
		DesiredCapabilities capabilities = new DesiredCapabilities();
		
		if ("Chrome".equalsIgnoreCase(this.propReader.getBrowser())) {
			if (osName.startsWith(osWindows)) {
				System.setProperty(chromeDriver, "./drivers/chrome/Windows/chromedriver.exe");
			} else if(osName.startsWith(osLinux)){
				System.setProperty(chromeDriver, "./drivers/chrome/Linux/chromedriver");
			}
			else {
				System.setProperty(chromeDriver, "./drivers/chrome/Mac/chromedriver");
			}
			driver = new ChromeDriver();
		} else if ("ie".equalsIgnoreCase(this.propReader.getBrowser())) {
			if (osName.startsWith(osWindows)) {
				System.setProperty(ieDriver, "./drivers/internetexplorer/32bit/IEDriverServer.exe");
			} else {
				System.setProperty(ieDriver, "./drivers/internetexplorer/IEDriverServer.exe");
			}
			driver = new InternetExplorerDriver();
		} else if ("firefox".equalsIgnoreCase(this.propReader.getBrowser())) {
			capabilities.setCapability("marionette", true);
			if (osName.startsWith(osWindows)) {
				System.setProperty(firefoxDriver, "./drivers/firefox/32bitWindows/geckodriver.exe");
			} else if(osName.startsWith(osLinux)) {
				System.setProperty(firefoxDriver, "./drivers/firefox/Linux/geckodriver");
			}
			else {
				System.setProperty(firefoxDriver, "./drivers/firefox/Mac/geckodriver");
			}
			driver = new FirefoxDriver(capabilities);
		} else if ("safari".equalsIgnoreCase(this.propReader.getBrowser())) {
			driver = new SafariDriver();
		}
	}

	public void initializeRemoteDriver() throws CantizAutomationCoreException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setBrowserName(propReader.getBrowser());
		if("firefox".equalsIgnoreCase(this.propReader.getBrowser())) {
			capabilities.setCapability("marionette", true);
		}
		capabilities.setPlatform(resolvePlatfrom());
		try {
			driver = new RemoteWebDriver(new URL(propReader.getRemoteUrl()), capabilities);
		} catch (MalformedURLException e) {
			throw new CantizAutomationCoreException("Remote URL provided is invalid.");
		}
	}

	public Platform resolvePlatfrom() {
		String osName = propReader.getOs();
		switch (osName) {
		case "MAC":
			return Platform.MAC;
		case "LINUX":
			return Platform.LINUX;
		case "WINDOWS":
			return Platform.WINDOWS;
		case "ANDROID":
			return Platform.ANDROID;
		default:
			return Platform.MAVERICKS;
		}
	}

	private By getLocator(Locators locator, String locatorValue) {
		
		By byObject = null;
		switch (locator) {
		case XPATH:
			byObject = By.xpath(locatorValue);
			break;

		case CLASS_NAME:
			byObject = By.className(locatorValue);
			break;

		case CSS_SELECTOR:
			byObject = By.cssSelector(locatorValue);
			break;

		case LINK_TEXT:
			byObject = By.linkText(locatorValue);
			break;

		case NAME:
			byObject = By.name(locatorValue);
			break;

		case PARTIAL_LINK_TEXT:
			byObject = By.partialLinkText(locatorValue);
			break;

		case TAG_NAME:
			byObject = By.tagName(locatorValue);
			break;

		case ID:
			byObject = By.id(locatorValue);
			break;

		default:
			break;
		}
		return byObject;
	}
	
	public WebElement findElement(Locators locator, String locatorValue) {
		return driver.findElement(getLocator(locator, locatorValue));
		
	}

	@Override
	public void sendKeyStrokes(Locators locator, String locatorValue, String keyStrokes) {
		findElement(locator, locatorValue).sendKeys(keyStrokes);
	}

	@Override
	public void sendSpecialKey(Locators locator, String locatorValue, String specialKey) {
		switch (specialKey) {
		case "ENTER":
			if (System.getProperty("os.name").startsWith("Mac"))
				findElement(locator, locatorValue).sendKeys(Keys.RETURN);
			else
				findElement(locator, locatorValue).sendKeys(Keys.ENTER);
			break;

		case "TAB":
			findElement(locator, locatorValue).sendKeys(Keys.TAB);
			break;

		case "BACKSPACE":
			findElement(locator, locatorValue).sendKeys(Keys.BACK_SPACE);
			break;

		case "DELETE":
			findElement(locator, locatorValue).sendKeys(Keys.DELETE);
			break;
			
		default:
			break;
		}

	}

	@Override
	public void clickElement(Locators locator, String locatorValue) {
		WebElement element = findElement(locator, locatorValue);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);

	}

	@Override
	public void selectValue(Locators locator, String locatorValue, String valueToSelect) {
		
		WebElement selectElemet = findElement(locator, locatorValue);
		if (System.getProperty("os.name").startsWith("Linux"))
			selectElemet.sendKeys(valueToSelect);
		else {
			Select selectElement = new Select(selectElemet);
			selectElement.selectByVisibleText(valueToSelect);
		}

	}
	
	@Override
	public void selectMenuElement(String menuFirstLevelId, String menuSecondLevelId) {
		this.clickElement(Locators.ID, "menuButton");
		WebDriverWait wait = new WebDriverWait(driver, 15);
		Actions clickAction = new Actions(driver);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(menuFirstLevelId)));
		clickAction.moveToElement(findElement(Locators.ID, menuFirstLevelId)).click();
		clickAction.build().perform();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(menuSecondLevelId)));
		clickAction.moveToElement(findElement(Locators.ID, menuSecondLevelId));
		clickAction.click().build().perform();

	}

	@Override
	public void clearElement(Locators locator, String locatorValue) {
		findElement(locator, locatorValue).clear();

	}

	@Override
	public Boolean checkValueInsideWebElement(String valueToCheck, Locators locator, String locatorValue,
			String elementType) {
		
		String wholeText = "";
		isElementPresent(locator, locatorValue);
		WebElement webElement = findElement(locator, locatorValue);
		
		if (elementType.equalsIgnoreCase(Constants.DIVELEMENT) || elementType.equalsIgnoreCase(Constants.SPANELEMENT)) {
			wholeText = webElement.getText();
		} else if (elementType.equalsIgnoreCase(Constants.TEXTBOX)) {
		
			wholeText = webElement.getAttribute("value");
		}
		
		return wholeText.equals(valueToCheck);
	}

	@Override
	public Boolean isElementEmpty(Locators locator, String locatorValue, String elementType) {
		 WebElement webElement = findElement(locator, locatorValue);
		if (elementType.equalsIgnoreCase(Constants.DIVELEMENT) || elementType.equalsIgnoreCase(Constants.SPANELEMENT)) {
			return "".equals(webElement.getText());
			
		} else if (elementType.equalsIgnoreCase(Constants.TEXTBOX)) {
			return "".equals(webElement.getAttribute("value"));
		}
		return false;
	}

	/*
	 * Method to close Driver once execution is completed , as open WebDriver
	 * sessions causing memory issue
	 */
	@Override
	public void closeDriver() {

		/*
		 * Removing driver.close() as it is creating
		 * trouble in Mozilla Firefox
		 */
		//driver.close();
		driver.quit();

	}
	
	@Override
	public Boolean isElementVisible(Locators locator, String locatorValue) {
		if ("safari".equalsIgnoreCase(this.propReader.getBrowser()))
			return isElementPresent(locator, locatorValue);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locator, locatorValue)));
		} catch (TimeoutException e) {
			logger.log(Level.INFO, "Element Not Visible", e);
			return false;
		}

		return driver.findElement(getLocator(locator, locatorValue)).isDisplayed();
	}
	

	@Override
	public Boolean isElementPresent(Locators locator, String locatorValue) {
		WebDriverWait wait = new WebDriverWait(driver, 40);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locator, locatorValue)));
			return true;
		} catch (TimeoutException e) {
			logger.log(Level.INFO, "Element Not Present", e);
			return false;
		}
	}


	@Override
	public String getAttributeTypeOfWebElement(Locators id, String locatorvalue) {
		WebElement element = findElement(id, locatorvalue);
		return element.getAttribute("type");
	}

	@Override
	public void navigateToPage(String pageURL) {

		driver.get(pageURL);
	}

	@Override
	public String getHtmlValueById(String htmlContent, String locator) {

		Document doc = Jsoup.parse(htmlContent);
		return doc.getElementById(locator).html();
	}

	@Override
	public String getCookieValueByName(String cookieName) {
		String cookieValue = driver.manage().getCookieNamed(cookieName).getValue();
		return cookieValue;
	}

	@Override
	public Boolean isElementSelected(Locators locator, String locatorValue) {
		return findElement(locator, locatorValue).isSelected();
	}

	@Override
	public Boolean isElementEnabled(Locators locator, String locatorValue) {
		return findElement(locator, locatorValue).isEnabled();
	}

	@Override
	public void scrollPageDown(String numberOfPixels) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0," + numberOfPixels + ")", "");

	}

	@Override
	public void scrollPageUp(String numberOfPixels) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,-" + numberOfPixels + ")", "");

	}

	@Override
	public Boolean checkIfElementDisappeared(Locators locator, String locatorValue) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(getLocator(locator, locatorValue)));
		return !driver.findElement(getLocator(locator, locatorValue)).isDisplayed();
	}

	@Override
	public void refreshPage() {
		driver.navigate().refresh();
	}

	@Override
	public void navigateBack() {
		driver.navigate().back();
	}

	

	@Override
	public String getTextInWebElement(Locators locator, String locatorValue) {
		String text = null;
		
		WebElement webElement = findElement(locator, locatorValue);
		text = webElement.getText();
		
		return text;
	}
}
