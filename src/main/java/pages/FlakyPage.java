package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FlakyPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By buttonBeforeClick = By.cssSelector("button.click-me-btn");
    private By buttonAfterClick = By.cssSelector("button.clicked-btn");

    public FlakyPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForButtonToAppear(){
        wait.until(ExpectedConditions.presenceOfElementLocated(buttonBeforeClick));
    }

    public String getButtonText(){
        return driver.findElement(buttonBeforeClick).getText();
    }

    public void clickButton(){
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(buttonBeforeClick));
        button.click();
    }

    public void waitForButtonToShowClicked(){
        wait.until(ExpectedConditions.presenceOfElementLocated(buttonAfterClick));
    }

    public String getClickedButtonText(){
        return driver.findElement(buttonAfterClick).getText();
    }
}
