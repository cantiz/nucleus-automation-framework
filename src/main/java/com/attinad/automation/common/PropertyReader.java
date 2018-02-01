package com.attinad.automation.common;

import org.apache.commons.lang3.StringUtils;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static PropertyReader propertyReader = null;
    private String browser;
    private String url;
	private String electronurl;
    private String os;
    private String driverType;
    private String remoteUrl;
    private String operatingSystem;
    private String mailStoreType;
	private String mailHost;
    private String mailPort;
    private String mailUserName;
    private String mailPassword;
    private String token;
    
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMailStoreType() {
		return mailStoreType;
	}

	public void setMailStoreType(String mailStoreType) {
		this.mailStoreType = mailStoreType;
	}
	
	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getMailPort() {
		return mailPort;
	}

	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}

	public String getMailUserName() {
		return mailUserName;
	}

	public void setMailUserName(String mailUserName) {
		this.mailUserName = mailUserName;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
    
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
    public String getElectronurl() {
		return electronurl;
	}

	public void setElectronurl(String electronurl) {
		this.electronurl = electronurl;
	}

    private PropertyReader() {
        Properties prop = initializeProperties();
        setEnvironmentProperties(prop);
        setMailerSettings(prop);
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

    private void setEnvironmentProperties(Properties prop) {
        browser = (!StringUtils.isEmpty(System.getProperty("target-browser")))?
                System.getProperty("target-browser"):prop.getProperty(Constants.TARGET_BROWSER_PROPERTY);
        url = (!StringUtils.isEmpty(System.getProperty("target-url")))?
                System.getProperty("target-url"):prop.getProperty(Constants.TARGET_URL_PROPERTY);
        electronurl = (!StringUtils.isEmpty(System.getProperty("target-electronurl")))?
                        System.getProperty("target-electronurl"):prop.getProperty(Constants.TARGET_ELECTRONURL_PROPERTY); 
        


         token = (!StringUtils.isEmpty(System.getProperty("token")))?
                                System.getProperty("token"):prop.getProperty(Constants.ELECTRON_TOKEN_PROPERTY);                        
                                
                                
                                
        driverType = (!StringUtils.isEmpty(System.getProperty("web-driver-type")))?
                System.getProperty("web-driver-type"):prop.getProperty(Constants.WEB_DRIVER_TYPE_PROPERTY);
        remoteUrl = (!StringUtils.isEmpty(System.getProperty("web-driver-url")))?
                System.getProperty("web-driver-url"):prop.getProperty(Constants.WEB_DRIVER_URL_PROPERTY);
        String osFromProperty =  (prop.getProperty(Constants.TARGET_OS_PROPERTY) != null)?
                prop.getProperty(Constants.TARGET_OS_PROPERTY) : getOperatingSystem();
        os = (!StringUtils.isEmpty(System.getProperty("target-os")))?
                System.getProperty("target-os"):osFromProperty;
    }
    
    

    private void setMailerSettings(Properties prop){
    	mailStoreType = prop.getProperty(Constants.MAIL_STORE_TYPE_PROPERTY);
        mailHost = prop.getProperty(Constants.MAIL_HOST_PROPERTY);
        mailPort = prop.getProperty(Constants.MAIL_PORT_PROPERTY);
        mailUserName =  (!StringUtils.isEmpty(System.getProperty("mail-username")))?
                System.getProperty("mail-username"):prop.getProperty(Constants.MAIL_USERNAME_PROPERTY);
        mailPassword = (!StringUtils.isEmpty(System.getProperty("mail-password")))?
                System.getProperty("mail-password"):prop.getProperty(Constants.MAIL_PSWD_PROPERTY);
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
