package org.example.lab2;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EkTest {

  private WebDriver firefoxDriver;
  private static final String baseUrl = "https://ek.ua/ua/";

  @BeforeClass(alwaysRun = true)
  public void setUp() {
    WebDriverManager.firefoxdriver().setup();
    FirefoxOptions firefoxOptions = new FirefoxOptions();
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
  public void testClickOnCommunicationAndGadgetsButton() {
    WebDriverWait webDriverWait = new WebDriverWait(firefoxDriver, Duration.ofSeconds(10));
    WebElement button = firefoxDriver.findElement(By.xpath(
        "/html/body/div[6]/div/table/tbody/tr/td[1]/div[1]/div[2]/a"));
    Assert.assertNotNull(button);
    button.click();
    webDriverWait.until(webDriver -> webDriver.getCurrentUrl().contains("/k"));

    Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseUrl);
  }

  @Test
  public void testSearchFieldOnMainPage() {
    WebDriverWait webDriverWait = new WebDriverWait(firefoxDriver, Duration.ofSeconds(10));
    WebElement searchField = firefoxDriver.findElement(By.tagName("input"));
    String inputValue = "apple iphone";
    searchField.sendKeys(inputValue);
    searchField.sendKeys(Keys.ENTER);
    webDriverWait.until(webDriver -> webDriver.getCurrentUrl().contains("?search_="));
    firefoxDriver.get(firefoxDriver.getCurrentUrl());
    WebElement nextSearchField = firefoxDriver.findElement(By.id("ek-search"));

    Assert.assertNotNull(nextSearchField);
    Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseUrl);
    Assert.assertEquals(nextSearchField.getAttribute("value"), inputValue);
  }

  @Test
  public void testHeaderExistsByClassName() {
    WebElement header = firefoxDriver.findElement(By.className("header"));

    Assert.assertNotNull(header);
  }

  @Test
  public void testSearchFormExistsById() {
    WebElement searchForm = firefoxDriver.findElement(By.id("ek-search-form"));

    Assert.assertNotNull(searchForm);
  }

  @Test
  public void testPopularProductsCarousel() {
    WebElement carousel = firefoxDriver.findElement(By.className("s-goods-carousel-viewport"));
    WebElement products = carousel.findElement(By.className("s-width"));
    List<WebElement> productItems = products.findElements(By.tagName("span"));
    WebElement leftArrow = firefoxDriver.findElement(By.className("s-goods-carousel-arrow-prev"));
    WebElement rightArrow = firefoxDriver.findElement(By.className("s-goods-carousel-arrow-next"));
    Assert.assertNotNull(carousel);
    Assert.assertNotNull(productItems);

    boolean isRightDirection = true;
    for (int i = 0; i < 20; i++) {
      if (productItems.get(0).getAttribute("class").contains("s-goods-carousel-pagination-bullet-active")) {
        Assert.assertFalse(productItems.get(1).getAttribute("class").contains("s-goods-carousel-pagination-bullet-active"));
        Assert.assertFalse(productItems.get(2).getAttribute("class").contains("s-goods-carousel-pagination-bullet-active"));
        Assert.assertFalse(productItems.get(3).getAttribute("class").contains("s-goods-carousel-pagination-bullet-active"));
        isRightDirection = true;
        rightArrow.click();
      } else if (productItems.get(3).getAttribute("class").contains("s-goods-carousel-pagination-bullet-active")) {
        Assert.assertFalse(productItems.get(0).getAttribute("class").contains("s-goods-carousel-pagination-bullet-active"));
        Assert.assertFalse(productItems.get(1).getAttribute("class").contains("s-goods-carousel-pagination-bullet-active"));
        Assert.assertFalse(productItems.get(2).getAttribute("class").contains("s-goods-carousel-pagination-bullet-active"));
        isRightDirection = false;
        leftArrow.click();
      } else if (isRightDirection) {
        rightArrow.click();
      } else {
        leftArrow.click();
      }
    }
  }
}
