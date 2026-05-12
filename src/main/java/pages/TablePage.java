package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TablePage {
    public WebDriver driver;

    public By refreshButton = By.id("refreshBtn");
    public By tableRows = By.cssSelector("#productTable tbody tr");
    public By stockCell = By.xpath(".//td[3]");

    public TablePage(WebDriver driver){
        this.driver = driver;
    }

    public  void clickRefreshStock(){
        driver.findElement(refreshButton).click();
    }

    public int countInStockRows(){
        List<WebElement> rows = driver.findElements(tableRows);
        int count = 0;
        for(WebElement row:rows){
            String stockValue = row.findElement(stockCell).getText().trim();
            if(stockValue.equalsIgnoreCase("In Stock")){
                count++;
            }
        }
        return count;
    }
}
