package com.attinad.automation.common;

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

	private String accountName;
	private String userName;
	private String userPassword;
	private String browser;
    private String url;
    private String subscription;
    private String os;
    private static String operatingSystem;
    
    public String currentdate;
    public String afterdate;
    private String beforedate;
    

    public String getAccountName() {
        return accountName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getBrowser() {
        return browser;
    }

    public String getUrl() {
        return url;
    }

    public String getSubscription() {
        return subscription;
    }

    public String getOs() {
        return os;
    }

	private PropertyReader(){
		Properties prop = initializeProperties();
		setLoginCredentials(prop);
	}	

	public static PropertyReader getInstance(){
		if(propertyReader == null){
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
		accountName = prop.getProperty("account");
		userName = prop.getProperty("user");
		userPassword = prop.getProperty("password");

		browser = prop.getProperty("browser");
		url = prop.getProperty("url");
		//We are not reading OS value from config file
		//instead we are reading from system properties os = prop.getProperty("os");
		os = getOperatingSystem();
		
	}

	private String getOperatingSystem() {
		operatingSystem = System.getProperty("os.name").toLowerCase();
		
		if (isWindows()) {
			System.out.println("This is Windows");
			return "win";
		} else if (isMac()) {
			System.out.println("This is Mac");
			return "mac";
		} else if (isUnix()) {
			System.out.println("This is Unix or Linux");
			return "unix";
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
			return "solaris";
		} else {
			System.out.println("Your OS is not support!!");
			return null;
		}
		
		
	}

	private void setSubscription(Properties prop) {
		subscription = prop.getProperty("gully");
	}
	
	public static boolean isWindows() {
		return (operatingSystem.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (operatingSystem.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (operatingSystem.indexOf("nix") >= 0 || operatingSystem.indexOf("nux") >= 0 || operatingSystem.indexOf("aix") > 0 );
	}

	public static boolean isSolaris() {
		return (operatingSystem.indexOf("sunoperatingSystem") >= 0);
	}
	
	public void timemanage() {

		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		   Date date = new Date();
		   Calendar cal = Calendar.getInstance();
   		   currentdate = dateFormat.format(cal.getTime());
		   cal.add(Calendar.DATE, -7);   
		   beforedate = dateFormat.format(cal.getTime());
		   cal.add(Calendar.DATE, +7);
		   afterdate = dateFormat.format(cal.getTime());


	 }
}
