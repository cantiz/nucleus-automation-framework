package com.attinad.automation.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.attinad.automation.common.Locators;

import java.util.concurrent.TimeUnit; 



public class SeleniumDriver implements ICantizWebDriver {

    private PropertyReader propReader = null;
    private WebDriver driver = null;

    public SeleniumDriver(PropertyReader propReader) {
        this.propReader = propReader;
        initializeDriver();
    }

    
    public void initializeDriver() {
    	
        if(this.propReader.getBrowser().equalsIgnoreCase("Chrome")){
            String osName = System.getProperty("os.name");
            if(osName.startsWith("Windows")){
                System.setProperty("webdriver.chrome.driver", "./drivers/chrome/chromedriver.exe");
            }else{
                System.setProperty("webdriver.chrome.driver", "./drivers/chrome/chromedriver");
            }
            driver = new ChromeDriver();
        }
        else if(this.propReader.getBrowser().equalsIgnoreCase("ie")){
            String osName = System.getProperty("os.name");
            if(osName.startsWith("Windows")){
                System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
            }else{
                System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
            }
            driver = new ChromeDriver();
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get(this.propReader.getUrl());
        driver.manage().window().maximize();
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
	public void sendKeyStrokes(Locators locator, String locatorValue,String keyStrokes) {
		 findElement(locator,locatorValue).sendKeys(keyStrokes);
	}

    @Override
	public void clickElement(Locators locator, String locatorValue) {
		 findElement(locator,locatorValue).click();
	}

	@Override
	public void selectValue(Locators locator, String locatorValue,String valueToSelect) {
		  Select selectElement = new Select(findElement(locator,locatorValue));
	        
		  selectElement.selectByVisibleText(valueToSelect);
	}
	
	@Override
	public void selectMenuElement(String menuFirstLevelId,String menuSecondLevelId) {
		this.clickElement(Locators.ID, "menuButton");
        WebDriverWait wait = new WebDriverWait(driver, 15);
        Actions clickAction = new Actions(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(menuFirstLevelId)));
        clickAction.moveToElement(findElement(Locators.ID,menuFirstLevelId)).click();
        clickAction.build().perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(menuSecondLevelId)));
        clickAction.moveToElement(findElement(Locators.ID,menuSecondLevelId));
        clickAction.click().build().perform();;
	}

	@Override
	public void clearElement(Locators locator, String locatorValue) {
		 findElement(locator,locatorValue).clear();
	
}

	@Override
	public Boolean checkValueInsideText(String valueToCheck, String divCategory) {

		System.out.println("Inside checkValueInsideText ");

		WebElement divElement = null;
		String wholeText = "";
		Boolean returner = null;
		
		switch (divCategory) {
		case "Response":
			// Corresponding Div Category is "show_response_info_id"
			divElement = driver.findElement(By.id("show_response_info_id"));
			wholeText = divElement.getText();
			System.out.println("Response Whole Text is : \n"+wholeText+"\n___end of Response whole text");
			if(wholeText.contains(valueToCheck)){
				System.out.println("Response Test case passed for case : "+valueToCheck);
				returner = true;				
			}
			else{
				System.out.println("Response Test case failed for case : "+valueToCheck);
				returner = false;
			}
			break;

		case "Error":
			divElement = driver.findElement(By.id("error_input_parameters_id"));
			wholeText = divElement.getText();
			System.out.println("Error Whole Text is : \n" +wholeText +"\n____end of error while text");
			if(wholeText.contains(valueToCheck)){
				System.out.println("Error Test case passed  for case : "+valueToCheck);
				returner = true;				
			}
			else{
				System.out.println("Error Test case failed for case : "+valueToCheck);
				returner = false;
			}
			break;

		default:
				System.out.println("Returner");		}
		return returner;

	}

}
