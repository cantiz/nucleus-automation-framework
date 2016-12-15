package com.attinad.automation.common;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PropertyReader {
    private static PropertyReader propertyReader = null;
    private String browser;
    private String url;
    private String os;
    private String driverType;
    private String remoteUrl;
    private String operatingSystem;

    public String getBrowser() {
        return browser;
    }

    public String getUrl() {
        return url;
    }


    public String getOs() {
        return os;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    private PropertyReader() {
        Properties prop = initializeProperties();
        setLoginCredentials(prop);
    }

    public static PropertyReader getInstance() {
        if (propertyReader == null) {
            propertyReader = new PropertyReader();
        }
        return propertyReader;
    }

    private Properties initializeProperties() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } // end of finally
        return prop;

    }// end of initializeProperties fn

    private void setLoginCredentials(Properties prop) {
        browser = (!StringUtils.isEmpty(System.getProperty("target-browser")))? System.getProperty("target-browser"):prop.getProperty("target-browser");
        url = (!StringUtils.isEmpty(System.getProperty("target-url")))? System.getProperty("target-url"):prop.getProperty("target-url");
        driverType = (!StringUtils.isEmpty(System.getProperty("web-driver-type")))? System.getProperty("web-driver-type"):prop.getProperty("web-driver-type");
        remoteUrl = (!StringUtils.isEmpty(System.getProperty("web-driver-url")))? System.getProperty("web-driver-url"):prop.getProperty("web-driver-url");
        String osFromProperty =  (prop.getProperty("target-os") != null)? prop.getProperty("target-os") : getOperatingSystem();
        os = (!StringUtils.isEmpty(System.getProperty("target-os")))? System.getProperty("target-os"):osFromProperty;

    }

    private String getOperatingSystem() {
        operatingSystem = System.getProperty("os.name").toLowerCase();
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "mac";
        } else if (isUnix()) {
            return "unix";
        } else if (isSolaris()) {
            return "solaris";
        } else {
            return null;
        }

    }

    public boolean isWindows() {
        return (operatingSystem.indexOf("win") >= 0);
    }

    public boolean isMac() {
        return (operatingSystem.indexOf("mac") >= 0);
    }

    public boolean isUnix() {
        return (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0
                || operatingSystem.indexOf("aix") > 0);
    }

    public boolean isSolaris() {
        return (operatingSystem.indexOf("sunoperatingSystem") >= 0);
    }
}
