package com.attinad.automation.modulemanagers;

import com.attinad.automation.common.ICantizWebDriver;


public class AbstractModuleManager {
    protected ICantizWebDriver cantizWebDriver;

    public AbstractModuleManager(ICantizWebDriver webDriver){
        this.cantizWebDriver = webDriver;
    }
}
