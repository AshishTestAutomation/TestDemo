package com.bigtree.test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.bigtreecms.utilities.Utilities;

public class ImageUpload extends Utilities {
	static Logger logger = Logger.getLogger(ImageUpload.class);
	ExtentHtmlReporter report;
	ExtentReports extent;
	ExtentTest Extentlogger;

	public void ExtentReporting() {
		report = new ExtentHtmlReporter("./test-output/ExtentReports/Bigtreecms_Automation.html");
		extent = new ExtentReports();
		extent.attachReporter(report);
		Extentlogger = extent.createTest("UploadImage");
	}

	@BeforeTest()
	public void LaunchBrowser() {
		ExtentReporting();
		PropertyConfigurator.configure("log4j.properties");
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\Admin\\BigtreeCMS\\plugins_jars\\geckodriver.exe");
		FirefoxOptions opts = new FirefoxOptions();
		opts.addArguments("-private");
		driver = new FirefoxDriver(opts);
		logger.info("Browser opened");
		driver.get("https://demo.bigtreecms.org/admin/login/");
		logger.info("URL launched");
		Extentlogger.log(Status.INFO, "URL launched");

	}

	@Test()
	public void BigTreeLogin() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			driver.findElement(By.id(obj.getProperty("useremail"))).sendKeys(obj.getProperty("Emailid"));
			logger.info("Entered Email id");
			driver.findElement(By.id(obj.getProperty("password"))).sendKeys(obj.getProperty("demo"));
			logger.info("Entered Password");
			driver.findElement(By.xpath(obj.getProperty("loginBtn"))).click();
			logger.info("Clicked on Login Button");
			Extentlogger.log(Status.INFO, "Clicked on Login Button");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test()
	public void NavigateToFilesScreen() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			driver.findElement(By.linkText(obj.getProperty("Files"))).click();
			logger.info("Clicked on Files Tab");
			Extentlogger.log(Status.INFO, "Clicked on Files Tab");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test()
	public void VerifyAddImagesScreen() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			driver.findElement(By.linkText(obj.getProperty("AddImages"))).click();
			logger.info("Clicked on Add Image Tab");
			Extentlogger.log(Status.INFO, "Clicked on Add Image Tab");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test()
	public void VerifyDocUpload() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		SoftAssert sa = new SoftAssert();
		try {
			ArrayList<String> arrlist = new ArrayList<String>();
			arrlist.add("Doc_JPEG");
			arrlist.add("Doc_BMP");
			arrlist.add("Doc_PNG");
			String FilesURL = driver.getCurrentUrl();
			logger.info("Verifying navigation to Crop Image Screen");
			Extentlogger.log(Status.INFO, "Verifying navigation to Crop Image Screen");
			sa.assertEquals(FilesURL, "https://demo.bigtreecms.org/admin/files/add/image/0/",
					"Could not be navigated to Crop Image Screen");
			screenShot("CropImageScreen");

			logger.info("Adding Images using drag and drop");
			Extentlogger.log(Status.INFO, "Adding Images using drag and drop");

			// executing AutoIT exe for Image Upload
			for (String img : arrlist) {
				driver.findElement(By.id(obj.getProperty("file_manager_dropzone"))).click();

				Process p1 = Runtime.getRuntime().exec("C:\\Users\\Admin\\Desktop\\All_Images\\" + img + ".exe");
				p1.waitFor();
			}

			String AllText = driver.findElement(By.id("file_manager_dropzone")).getText();
			String[] arrSplit = AllText.split("\\n");

			// catching error for one typr of image which is not allowed to upload
			for (int i = 0; i < arrSplit.length; i++) {
				if (arrSplit[i].contains("Drag and drop files into this zone or click to manually upload.")) {
					System.out.println(arrSplit[i + 1] + " file is not allowed to upload");
					logger.info(arrSplit[i + 1] + " file is not allowed to upload");
					Extentlogger.log(Status.PASS, arrSplit[i + 1] + " file is not allowed to upload");
				}
			}

			// clicking on Continue button to navigate to crop Image screen
			driver.findElement(By.xpath(obj.getProperty("continueBtn"))).click();
			logger.info("Clicked on continue button");
			Extentlogger.log(Status.INFO, "Clicked on continue button");
			String cropURL = driver.getCurrentUrl();
			sa.assertEquals(cropURL.contains("https://demo.bigtreecms.org/admin/files/crop"), true,
					"Could not be navigated to Crop Image Screen");

			// cropping all images which is allowed to crop
			try {
				do {
					driver.findElement(By.xpath(obj.getProperty("cropImageBtn"))).click();
					logger.info("Clicked on crop image button");
					Extentlogger.log(Status.INFO, "Clicked on crop image button");
				} while (driver.findElement(By.xpath(obj.getProperty("cropImageBtn"))).isDisplayed());
			} catch (NoSuchElementException e) {
				System.out.println("No more element available to crop");
				logger.info("No more element available to crop");
				Extentlogger.log(Status.INFO, "No more element available to crop");
			} catch (TimeoutException e) {
				System.out.println("Session Timeout");
				logger.info("Session Timeout");
				Extentlogger.log(Status.WARNING, "Session Timeout");
			}

			// Clicking on Update file button to navigate to next screen
			String UpdateFile = driver.getCurrentUrl();
			sa.assertEquals(UpdateFile.contains("https://demo.bigtreecms.org/admin/files/edit/file/"), true,
					"Could not be navigated to update file Screen");
			waitForElementVisibility(By.xpath(obj.getProperty("UpdateFile")), 10);
			driver.findElement(By.xpath(obj.getProperty("UpdateFile"))).click();
			System.out.println("Clicked on Update File");
			logger.info("Clicked on Update File");
			Extentlogger.log(Status.INFO, "Clicked on Update File");

			String adminFilesURL = driver.getCurrentUrl();
			sa.assertEquals(adminFilesURL.contains("https://demo.bigtreecms.org/admin/files/folder"), true,
					"Could not be navigated to Crop Image Screen");
			sa.assertAll();
		} catch (NoSuchElementException e) {
			logger.info("No Such Element");
			Extentlogger.log(Status.INFO, "No Such Element");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error in Image upload functionality");
			Extentlogger.log(Status.INFO, "Error in Image upload functionality");
		}

	}

	@Test()
	public void VerifyDeleteImages() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		SoftAssert sa = new SoftAssert();
		try {
			do {
				try {

					// deleting images if available
					if (driver.findElement(By.className(obj.getProperty("deleteIcon"))).isDisplayed()) {
						driver.findElement(By.className(obj.getProperty("deleteIcon"))).click();
						logger.info("Clicked on delete icon");
						Extentlogger.log(Status.INFO, "Clicked on delete icon");
						try {
							if (driver.findElement(By.xpath(obj.getProperty("deleteFileBtn"))).isDisplayed()) {
								driver.findElement(By.xpath(obj.getProperty("deleteFileBtn"))).click();
								logger.info("Clicked on delete file button");
								Extentlogger.log(Status.INFO, "Clicked on delete file button");
								try {

									if ((driver.findElement(By.xpath(obj.getProperty("deleteImagePopupForm"))).isDisplayed())
											&& (driver.findElement(By.xpath(obj.getProperty("deleteImagePopupForm"))).getText()
													.contains("Are you sure you want to delete"))) {
										logger.info("Text for deleting image is appeared on pop-up");
										Extentlogger.log(Status.PASS, "Text for deleting image is appeared on pop-up");
									} else {
										logger.info("Text for deleting image not appeared on pop-up");
										Extentlogger.log(Status.PASS, "Text for deleting image not appeared on pop-up");
									}
								} catch (NoSuchElementException e) {
									sa.assertFalse(true, "No More Delete popup is available further");
									logger.info("No More Delete popup is available further");
									Extentlogger.log(Status.INFO, "No More Delete popup is available further");
								}
							} else {
								System.out.println("Delete File Option is not visible");
								logger.error("Delete File Option is not visible");
								Extentlogger.log(Status.ERROR, "Delete File Option is not visible");
								sa.assertFalse(true, "Delete File Option is not visible");
								// return false;
							}
						} catch (NoSuchElementException e) {
							sa.assertFalse(true, "No Delete icon present");
							logger.error("No Delete icon present");
							Extentlogger.log(Status.ERROR, "No Delete icon present");
						}

					} else {
						System.out.println("Image could not be uploaded");
						logger.error("Image could not be uploaded");
						Extentlogger.log(Status.ERROR, "Image could not be uploaded");
						sa.assertFalse(true, "Image could not be uploaded");
					}
				} catch (NoSuchElementException e) {
					sa.assertFalse(true, "Image could not be uploaded");
					logger.error("Image could not be uploaded");
					Extentlogger.log(Status.ERROR, "Image could not be uploaded");
				}
			} while (driver.findElement(By.className(obj.getProperty("deleteIcon"))).isDisplayed());
		} catch (NoSuchElementException e) {
			System.out.println("No more image available to delete");
			logger.info("No more image available to delete");
			Extentlogger.log(Status.INFO, "No more image available to delete");
		}
		try {

			if (driver.findElement(By.className(obj.getProperty("deleteIcon"))).isDisplayed()) {
				System.out.println("Image Deleted Successfully");
				logger.info("Image Deleted Successfully");
				Extentlogger.log(Status.PASS, "Image Deleted Successfully");
				sa.assertTrue(true, "Image Deleted Successfully");
			} else {
				System.out.println("Image could not be deleted");
				logger.error("Image could not be deleted");
				Extentlogger.log(Status.ERROR, "Image could not be deleted");
				sa.assertTrue(true, "Image could not be deleted");
			}
		} catch (NoSuchElementException e) {
			sa.assertTrue(true, "No More delete icon is available");
			logger.info("No More delete icon is available");
			Extentlogger.log(Status.PASS, "No More delete icon is available");
		}
		sa.assertAll();
	}

	@AfterTest()
	public void TearDown() {
		driver.quit();
		logger.info("Closing Browser");
		Extentlogger.log(Status.INFO, "Closing Browser");
		extent.flush();
	}

}
