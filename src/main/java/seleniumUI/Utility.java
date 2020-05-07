package seleniumUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utility {
	
	static WebDriver driver;
	static String Path = System.getProperty("user.dir");

	public static void gameflow() throws InterruptedException, IOException {
		
		System.setProperty("webdriver.chrome.driver", Path +"//TestResource//chromedriver.exe");
		WebDriver driver= new ChromeDriver();
		
		driver.get("https://www.game.tv");
		driver.manage().window().maximize();
		String CurrentWindow= driver.getWindowHandle();
		
		//Code to Scroll the page to specific Element
		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//section[@class='available-games']")));	
	
    	WebElement Ul=driver.findElement(By.xpath("//ul[@class='games-list']"));
		List<WebElement> li= Ul.findElements(By.tagName("li"));
		
		//File is Created for of xlsx type
		File file = new File(Path +"\\GamesInfo.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sh = wb.createSheet("Game Info");
        
        //Add the Column name in the Excel Sheet
        sh.createRow(0).createCell(0).setCellValue("Game name");
        sh.getRow(0).createCell(1).setCellValue("Page URL");
        sh.getRow(0).createCell(2).setCellValue("Page Status");	
        sh.getRow(0).createCell(3).setCellValue("Tournament Count");	

        //Using traditional for loop as the counter is of use
		for(int i=0;i<li.size();i++) {
			
			String Gamename=li.get(i).getText();
            String Gameurl=li.get(i).findElement(By.tagName("a")).getAttribute("href");
            
            //RestAssured API status code
            RequestSpecification httpRequest = RestAssured.given();
      		Response response = httpRequest.request(Method.GET, Gameurl);
      		
      	    //Get the Status Code of the URL
      		int Statuscode=response.getStatusCode();
      		
      		//Opening new tab to get the Number of Tournaments---
      		//Even though it is quite lengthy process to get the attribute value----
      		//Hard to get from the response body of the tournament API-----
            ((JavascriptExecutor) driver).executeScript("window.open()");
    		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
    		//Switching the control to new tab
    		driver.switchTo().window(tabs.get(1)); 
    		//Opening the Game URL in new tab
    		driver.get(Gameurl);
            Thread.sleep(1000);
            //Getting the Tournament Counts
            String TourNumber= driver.findElement(By.xpath("//h2[@class='heading section-heading']/span")).getText();            
            //Closing the opened tab
            driver.close();
            driver.switchTo().window(CurrentWindow); 

      		//j is the local variable used to increment the value of i 
      		int j=i+2;
      		//Write data in the excel
            sh.createRow(j).createCell(0).setCellValue(Gamename);
            sh.getRow(j).createCell(1).setCellValue(Gameurl);
            sh.getRow(j).createCell(2).setCellValue(Statuscode);
            sh.getRow(j).createCell(3).setCellValue(TourNumber+" Tournaments");

            //write data in the excel file
            FileOutputStream fos = new FileOutputStream(file);
            //Initiate Write in WorkBook
            wb.write(fos);
            //close output stream
            fos.close();	     
		}
        wb.close();
        driver.quit();
	}
	
	public static void web() {
		
	
	}
	}
