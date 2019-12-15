package com.bigtreecms.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


	
public class Utilities {
    public static WebDriver driver;
    static Logger logger = Logger.getLogger(Utilities.class);
    protected Properties obj;
    FileInputStream objfile;
    
	public static void waitForElementVisibility(By by, int t) {
		WebDriverWait wait = new WebDriverWait(driver, t);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='Update File']")));

	}
	
	public Utilities()
    {
        //PropertiesConfigurator is used to configure logger from properties file
        PropertyConfigurator.configure("log4j.properties");
        //Log in console in and log file
        logger.debug("Log4j appender configuration is successful !!");
        
        try {
        	obj = new Properties(); 
			objfile = new FileInputStream(System.getProperty("user.dir")+"\\or.properties");
			obj.load(objfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }
	
	public void screenShot(String screenname) throws IOException, InterruptedException
	{
	    File scr=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    File dest= new File("./test-output/screenshots/screenshot_"+screenname+"_"+timestamp()+".png");
	    FileUtils.copyFile(scr, dest);
	    Thread.sleep(3000);
	}

	public String timestamp() {
	        return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
	}

}
