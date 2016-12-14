package com.attinad.automation.stepDefinition;

import com.attinad.automation.common.ICantizWebDriver;
import com.attinad.automation.common.PropertyReader;
import com.attinad.automation.common.SeleniumDriver;
import com.attinad.automation.utils.CantizAutomationCoreException;

/**
 * Created by whiterabbit (karthik@attinadsoftware.com) on 12/09/16.
 */
public class AbstractSteps {
    protected  static ICantizWebDriver cantizWebDriver;
    public AbstractSteps() throws CantizAutomationCoreException {
        if(cantizWebDriver == null){
            this.cantizWebDriver = new SeleniumDriver(PropertyReader.getInstance());
        }
    }
}