package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Lab {

  private WebDriver firefoxDriver;
  private static final String baseUrl = "https://www.nmu.org.ua/ua/";

  @BeforeClass(alwaysRun = true)
  public void setUp() {
    WebDriverManager.firefoxdriver().setup();
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.addArguments("--start-fullscreen");
    firefoxOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
    this.firefoxDriver = new FirefoxDriver(firefoxOptions);
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    firefoxDriver.quit();
  }

  @BeforeMethod
  public void preconditions() {
    firefoxDriver.get(baseUrl);
  }

  @Test
  public void testHeaderExists() {
    WebElement header = firefoxDriver.findElement(By.id("heder"));

    Assert.assertNotNull(header);
  }

  @Test
  public void testClickOnForStudent() {
    WebElement forStudentButton = firefoxDriver.findElement(
        By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[4]/a"));
    forStudentButton.click();
    Assert.assertNotNull(forStudentButton);
    Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseUrl);
  }

  @Test
  public void testSearchFieldOnForStudentPage() {
    String studentPageUrl = "content/student_life/students/";
    firefoxDriver.get(baseUrl + studentPageUrl);
    WebElement searchField = firefoxDriver.findElement(By.tagName("input"));
    Assert.assertNotNull(searchField);
    System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name")) +
        String.format("\nID attribute: %s", searchField.getAttribute("id")) +
        String.format("\nType attribute: %s", searchField.getAttribute("type")) +
        String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
        String.format("\nPosition: (%d;%d)", searchField.getLocation().x,
            searchField.getLocation().y) +
        String.format("\nSize: %dx%d", searchField.getSize().height, searchField.getSize().width));
    String inputValue = "I need info";
    searchField.sendKeys(inputValue);
    Assert.assertEquals(searchField.getText(), inputValue);
    searchField.sendKeys(Keys.ENTER);
    Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), studentPageUrl);
  }

  @Test
  public void testSlider() {
    WebElement nextButton = firefoxDriver.findElement(By.className("next"));
    WebElement nextButtonByCss = firefoxDriver.findElement(By.cssSelector("a.next"));
    Assert.assertEquals(nextButton, nextButtonByCss);

    WebElement previousButton = firefoxDriver.findElement(By.className("prev"));

    for (int i = 0; i < 20; i++) {
      if (nextButton.getAttribute("class").contains("disabled")) {
        for (int j = 0; j < 5; j++) {
          previousButton.click();
        }
        Assert.assertTrue(previousButton.getAttribute("class").contains("disabled"));
        Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
      } else {
        for (int j = 0; j < 5; j++) {
          nextButton.click();
        }
        Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
        Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
      }
    }
  }

}
