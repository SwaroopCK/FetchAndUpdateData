package Day33;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FixedDepositTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get("https://www.moneycontrol.com/fixed-income/calculator/state-bank-of-india-sbi/fixed-deposit-calculator-SBI-BSB001.html?classic=true");

		
		//To get current directory System.getproperty("user.dir") is used
		String file = System.getProperty("user.dir")+"\\testData\\caldata.xlsx";
		int rows = ExcelUtils.getRowCount(file, "Sheet1");
		
		for(int i=1;i<=rows;i++) {
			//Read data from excel
			String principle = ExcelUtils.getCellData(file, "sheet1", i, 0);
			String rateOfInt = ExcelUtils.getCellData(file, "sheet1", i, 1);
			String period1 = ExcelUtils.getCellData(file, "sheet1", i, 2);
			String period2 = ExcelUtils.getCellData(file, "sheet1", i, 3);
			String freq = ExcelUtils.getCellData(file, "sheet1", i, 4);
			String exp_mvalue = ExcelUtils.getCellData(file, "sheet1", i, 5);
			
			
			//pass data to the app
			driver.findElement(By.id("principal")).sendKeys(principle);
			driver.findElement(By.id("interest")).sendKeys(rateOfInt);
			driver.findElement(By.id("tenure")).sendKeys(period1);
			Select perdmy = new Select(driver.findElement(By.name("tenurePeriod")));
			perdmy.selectByVisibleText(period2);
			
			Select freqdmy = new Select(driver.findElement(By.id(("frequency"))));
			freqdmy.selectByVisibleText(freq);
			
			
					
			driver.findElement(By.xpath("//div[@class=\"cal_div\"]//a[1]")).click();
			
			//validate and update excel
			
			String act_amount = driver.findElement(By.xpath("//span[@class=\"gL_27\"]//strong")).getText();
			
			
			if( Double.parseDouble(act_amount)== Double.parseDouble(exp_mvalue)) {
				System.out.println("Test passed");
				ExcelUtils.setCellData(file, "sheet1", i, 7, "passed");
				ExcelUtils.fillGreenColor(file,"sheet1", i, 7);
			}
			else {
				System.out.println("Test Faied");
				ExcelUtils.setCellData(file, "sheet1", i, 7, "Failed");
				ExcelUtils.fillRedColor(file, "sheet1",i, 7);
			}
			
			Thread.sleep(3000);
			
			//After every result clear textboxes
			driver.findElement(By.xpath("//img[@class='PL5']")).click();
		}
		
	}

}
