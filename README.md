# nucleus-automation-framework
BDD automation framework used in Cantiz product team using cucumber and selenium. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Java 8 or above
Gradle/Maven

```
Give examples
```

### Installing
Create a configuration.properties file in your project source directory
Configure below values

cantiz.automation.environment.target-os=LINUX
cantiz.automation.environment.target-browser=chrome
cantiz.automation.environment.web-driver-type=local
cantiz.automation.environment.target-url=http://staging.nucleus.cantiz.com/


Supported values for target OS are LINUX,MAC and WINDOWS
Supported values for target-browser are Firefox and Chrome across all platforms
Supported values for web-driver-type are local and remote
You need to enter your testing url in the target-url field

Additionaly, Cantiz Automation core project supports functionality for reading an OTP from a mail inbox.
By providing below details in configuration properties file, you can access a 6 digit OTP. Ypu can customise the automation core project to change the length of the OTP.
cantiz.automation.mailer.host=
cantiz.automation.mailer.port=
cantiz.automation.mailer.store-type=imap
cantiz.automation.mailer.username
cantiz.automation.mailer.password=

For gmail the values are as host:imap.gmail.com,port:993,type:imap

Automation core also ebables to pass any tokens that is required if there is any API/backend call associated with the project.
cantiz.automation.environment.token

Add below repository in Repositories

    maven { 
        credentials {
        username artifactory_user
        password artifactory_password}
        
        url 'https://attinadsoftware.jfrog.io/attinadsoftware/Cantiz_Automation_Core'
       }


Add below dependency to download cantiz-automation-core
compile group: 'com.attinad.automation', name: 'cantiz-automation-core', version:'1.0.14'

Add below set of test arguments
test{
    systemProperty 'target-os', System.getProperty('target-os')
    systemProperty 'target-browser', System.getProperty('target-browser')
    systemProperty 'target-url', System.getProperty('target-url')
    systemProperty 'web-driver-type', System.getProperty('web-driver-type')
    systemProperty 'web-driver-url', System.getProperty('web-driver-url')
    systemProperty 'mail-username', System.getProperty('mail-username')
    systemProperty 'mail-password', System.getProperty('mail-password')
}


Create a CommonModuleManager class that extends AbstractModuleManager
Implement constructor in CommonModuleManager as in AbstractModuleManager
Declare and define functionalities that needs to be implemented using front-end automation tool here (Selenium is supported in the  current implementation)
Your corresponding module manager should extends commonModuleManager

Execution Level

In your step defintion file, create an object of the corresponding module manager.
For ex, In SignInStepDefinition file, object of SignInModuleManager should be created.
Each and every selenium related functionalities should be called from SignInSteps by invoking the corresponding methodmanager function.

## Built With

* Java
* Selenium


## Authors

https://github.com/anandxp
https://github.com/shariqmohammed92

