package com.attinad.automation.common;

import com.attinad.automation.utils.CantizAutomationCoreException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.attinad.automation.common.Locators;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SeleniumDriver implements ICantizWebDriver {

	private PropertyReader propReader = null;
	private WebDriver driver = null;

	public SeleniumDriver(PropertyReader propReader) throws CantizAutomationCoreException {
		this.propReader = propReader;
		if(propReader.getDriverType().equalsIgnoreCase("remote")){
			initializeRemoteDriver();
		}else{
			initializeDriver();
		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get(this.propReader.getUrl());
		driver.manage().window().maximize();
	}

	public void initializeDriver() {
		if (this.propReader.getBrowser().equalsIgnoreCase("Chrome")) {
			String osName = System.getProperty("os.name");
			if (osName.startsWith("Windows")) {
				System.setProperty("webdriver.chrome.driver", "./drivers/chrome/chromedriver.exe");
			} else {
				System.setProperty("webdriver.chrome.driver", "./drivers/chrome/chromedriver");
			}
			driver = new ChromeDriver();
		} else if (this.propReader.getBrowser().equalsIgnoreCase("ie")) {
			String osName = System.getProperty("os.name");
			if (osName.startsWith("Windows")) {
				System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
			} else {
				System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
			}
			driver = new ChromeDriver();
		}
	}

	private void initializeRemoteDriver() throws CantizAutomationCoreException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		String browser = (System.getProperty("browserName") != null)? System.getProperty("browserName"):propReader.getBrowser();
		capabilities.setBrowserName(browser);
		capabilities.setPlatform(resolvePlatfrom());
		try {
			driver = new RemoteWebDriver(new URL(propReader.getRemoteUrl()),capabilities);
		} catch (MalformedURLException e) {
			throw new CantizAutomationCoreException("Remote URL provided is invalid.");
		}
	}

	private Platform resolvePlatfrom(){
		String osName = (System.getProperty("target-os") != null)? System.getProperty("target-os"):propReader.getOs();
		switch (osName){
			case "MAC":
				return Platform.MAC;
			default:
				return Platform.WINDOWS;
		}
	}

	private WebElement findElement(Locators locator, String locatorValue) {
		WebElement element = null;
		switch (locator) {
		case XPATH:
			element = driver.findElement(By.xpath(locatorValue));
			break;

		case CLASS_NAME:
			element = driver.findElement(By.id(locatorValue));
			break;

		case CSS_SELECTOR:
			element = driver.findElement(By.cssSelector(locatorValue));
			break;

		case LINK_TEXT:
			element = driver.findElement(By.linkText(locatorValue));
			break;

		case NAME:
			element = driver.findElement(By.name(locatorValue));
			break;

		case PARTIAL_LINK_TEXT:
			element = driver.findElement(By.partialLinkText(locatorValue));
			break;

		case TAG_NAME:
			element = driver.findElement(By.tagName(locatorValue));
			break;

		case ID:
			element = driver.findElement(By.id(locatorValue));
			break;

		default:
			break;
		}
		return element;
	}

	@Override
	public void sendKeyStrokes(Locators locator, String locatorValue, String keyStrokes) {
		findElement(locator, locatorValue).sendKeys(keyStrokes);
	}

	@Override
	public void clickElement(Locators locator, String locatorValue) {
		findElement(locator, locatorValue).click();
	}

	@Override
	public void selectValue(Locators locator, String locatorValue, String valueToSelect) {
		Select selectElement = new Select(findElement(locator, locatorValue));

		selectElement.selectByVisibleText(valueToSelect);
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
		;
	}

	@Override
	public void clearElement(Locators locator, String locatorValue) {
		findElement(locator, locatorValue).clear();

	}

	@Override
	public Boolean checkValueInsideText(String valueToCheck, String elementId){
		WebElement divElement = null;
		String wholeText;
		divElement = driver.findElement(By.id(elementId));
		wholeText = divElement.getText();
		if (wholeText.contains(valueToCheck)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Method to close Driver once execution is completed , as open WebDriver
	 * sessions causing memory issue
	 */
	@Override
	public void closeDriver() {

		driver.close();
		driver.quit();

	}

}
