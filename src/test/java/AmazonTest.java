
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;

public class AmazonTest implements AmazonInterfaceSetUp {

	WebDriver driver;
	private String nameJson;

	@BeforeMethod
	public void setUp() throws IOException, ParseException {

		WebDriverManager.chromedriver().browserVersion("77.0.3865.40").setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.addArguments("enable-automation");
		driver = new ChromeDriver(options);
		JSONParser parser = new JSONParser();
		try {
			Reader reader = new FileReader("Data.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            nameJson = (String) jsonObject.get("busqueda");
            System.out.println(nameJson);
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}
		// step number 1
		driver.get("https://www.amazon.com/");

	}

	@SuppressWarnings("deprecation")
	@Test
	public void AmazonSearch() throws InterruptedException {

		// step number 2
		List<WebElement> dropdowns = driver.findElements(By.xpath("//*[@id='nav-xshop']/a"));
		Assert.assertEquals(dropdowns.size(), 6);
		System.out.println(dropdowns.size());

		// Step number 3
		WebElement menu;
		menu = driver.findElement(By.xpath("//*[@id=\"nav-hamburger-menu\"]"));
		menu.click();
		Thread.sleep(3000);
		// espera explicita
		WebDriverWait wait = new WebDriverWait(driver, 5);
		//wait.until(ExpectedConditions.presenceOfElementLocated((By) menu));

		// Step number 4
		WebElement submenuElement;
		submenuElement = driver.findElement(
				By.xpath("/html/body/div[3]/div[2]/div/ul[1]/li[3]/a/div[contains(.,'Kindle E-readers & Books')]"));
		submenuElement.click();
		Thread.sleep(3000);
		// Step number 5
		List<WebElement> leftMenu = driver
				.findElements(By.xpath("//*[@id='hmenu-content']/ul[3]/li[*][not(self::div)]/a[1]"));
		String getText;
		System.out.println("The elements in the left menu are: ");
		for (int i = 0; i < leftMenu.size(); i++) {

			getText = leftMenu.get(i).getText();
			System.out.println(i + 1 + " " + getText);
		}

		// Step number 6
		WebElement close = driver.findElement(By.xpath("//*[@id='hmenu-container']/div[1]"));
		close.click();
		Thread.sleep(2000);

		// Step number 7
		WebElement searchBar = driver.findElement(By.xpath("//input[@aria-label=\"Search\"]"));
		searchBar.clear();
		searchBar.sendKeys(nameJson);
		searchBar.submit();
		Thread.sleep(3000);

		// Step number 8 and 9
		String getText2;
		WebElement price = driver.findElement(By.xpath(
				"//span[3]/div[2]/div[1]/div[1]/span[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/a[1]/span[1]/span[2]"));
		getText2 = price.getText();
		System.out.println(getText2);
		Thread.sleep(3000);

		// Step number 10
		WebElement firstProduct = driver.findElement(By.xpath(
				"//span[3]/div[2]/div[1]/div[1]/span[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/h2[1]/a[1]/span[1]"));
		firstProduct.click();
		Thread.sleep(5000);

		// Step number 11
		WebElement firstPrice = driver.findElement(By.xpath("//tr[1]/td[2]/span[1]/span[2]"));
		Assert.assertEquals(price, firstPrice);
		Thread.sleep(3000);

		// Step number 12
		WebElement addToCart = driver.findElement(By.xpath("//*[@id='add-to-cart-button']"));
		addToCart.click();
		Thread.sleep(3000);
		// Step number 13
		WebElement goToCart = driver.findElement(
				By.xpath("//*[contains(concat( \" \", @class, \" \" ), concat( \" \", \"a-button-input\", \" \" ))]"));
		goToCart.click();
		Thread.sleep(3000);

		// Step number 14
		WebElement dropDownList = driver.findElement(By.xpath("//*[@id='a-autoid-0-announce']"));
		dropDownList.click();
		Thread.sleep(3000);
		WebElement moreThanTen = driver.findElement(By.xpath("//*[@id='quantity_10']"));
		moreThanTen.click();
		Thread.sleep(3000);
		WebElement clearBar = driver.findElement(By.xpath("//div[2]/div[1]/span[1]/span[1]/input[1]"));

		clearBar.clear();
		clearBar.sendKeys("20");
		clearBar.submit();
		Thread.sleep(2000);
		WebElement noStock = driver.findElement(By.xpath("//li[2]/span[1]/span[1]/span[1]"));
		Thread.sleep(2000);
		if (noStock.isDisplayed()) {
			System.out.println("There's not product enough in stock");
		} else {
			WebElement subTotalA = driver.findElement(By.xpath("//div[3]/span[2]/span[1]"));
			String total = subTotalA.getText();
			int priceTimes20 = (Integer.parseInt(getText2)) * 20;
			if (total.equals(priceTimes20)) {
				System.out.println("The price is correct");
			}

		}

	}

	@AfterTest
	public void afterTest() {
		driver.quit();
	}

}
