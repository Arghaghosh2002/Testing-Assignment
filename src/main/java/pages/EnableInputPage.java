package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EnableInputPage {
    public WebDriver driver;
    public WebDriverWait wait;

    public By enableButton = By.id("enableBtn");
    public By textInput = By.id("myInput");
    public By submitButton = By.id("submitBtn");

    public EnableInputPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getInputDisabledAttribute(){
        WebElement input = driver.findElement(textInput);
        return input.getAttribute("disabled");
    }

    public void clickEnableInput(){
        driver.findElement(enableButton).click();
    }

    public void waitForInputToBeEnabled(){
        wait.until(ExpectedConditions.elementToBeClickable(textInput));
        //System.out.println("Input is enable and clickable");
    }
    public void typeIntoInput(String text){
        driver.findElement(textInput).sendKeys(text);
    }

    public void  clickSubmit(){
        driver.findElement(submitButton).click();
    }
    public String acceptAlertAndGetText(){
        wait.until(ExpectedConditions.alertIsPresent());

        String alertText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        //System.out.println("Alert Accepted, text was: "+ alertText);
        return alertText;
    }
}