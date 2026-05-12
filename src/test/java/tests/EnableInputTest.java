package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.EnableInputPage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EnableInputTest {
    private WebDriver driver;
    private File tempHtmlFile;

    @BeforeMethod
    public void setUp() throws IOException {
        String htmlContent = """
                       <!DOCTYPE html>
                       <html>
                       <head>
                           <title>Enable Input Test Page</title>
                           <style>
                               body { font-family: Arial, sans-serif; padding: 40px; }
                               input { padding: 8px; font-size: 14px; width: 220px; }
                               button { padding: 8px 16px; margin: 10px 5px;
                                        font-size: 14px; cursor: pointer; }
                               #enableBtn  { background: #4CAF50; color: white; border: none; }
                               #submitBtn  { background: #2196F3; color: white; border: none; }
                               #statusMsg  { margin-top: 12px; color: #555; font-size: 13px; }
                           </style>
                       </head>
                       <body>
           
                           <h2>Task 2 — Enable Input Demo</h2>
           
                           <!-- Input is DISABLED by default -->
                           <input type="text" id="myInput" disabled placeholder="Type here..."/>
           
                           <br/>
           
                           <button id="enableBtn" onclick="enableInput()">Enable Input</button>
                           <button id="submitBtn" onclick="submitValue()">Submit</button>
           
                           <p id="statusMsg">Input is currently <strong>disabled</strong>.</p>
           
                           <script>
                               function enableInput() {
                                   var input = document.getElementById('myInput');
                                   // Remove the disabled attribute after a 1.5 second delay
                                   // This simulates real-world async / loading behaviour
                                   setTimeout(function() {
                                       input.removeAttribute('disabled');
                                       document.getElementById('statusMsg').innerText
                                           = 'Input is now ENABLED. Type something!';
                                   }, 1500);
                               }
           
                               function submitValue() {
                                   var value = document.getElementById('myInput').value;
                                   if (value.trim() === '') {
                                       alert('Please enter some text first!');
                                   } else {
                                       alert('You entered: ' + value);
                                   }
                               }
                           </script>
           
                       </body>
                       </html>
                       """;

        tempHtmlFile = new File(System.getProperty("java.io.tmpdir"),"temp_enable_input.html");
        try(FileWriter writer = new FileWriter(tempHtmlFile)){
            writer.write(htmlContent);
        }
        //System.out.println("Temp File created at: "+tempHtmlFile.getAbsolutePath());

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        String fileUrl = "file:///"+tempHtmlFile.getAbsolutePath().replace("\\","/");
        driver.get(fileUrl);
        //System.out.println("Browser Opened: "+fileUrl);
    }

    @Test
    public void testEnableInputAndSubmit(){
        EnableInputPage page = new EnableInputPage(driver);

        String disabledAttr = page.getInputDisabledAttribute();
        //System.out.println("Disabled Attribute Value: "+ disabledAttr);

        Assert.assertNotNull(
                disabledAttr,"Input should be disabled inititally, but 'disabled' attribute was not found"
        );
        //System.out.println("Input is Disabled at page load");

        page.clickEnableInput();
        //System.out.println("Clicked 'Enable Input' button.");
        page.waitForInputToBeEnabled();
        String afterAttr = page.getInputDisabledAttribute();
        Assert.assertNull(
                afterAttr,"Input should be enabled now, but 'disabled' attribute still exists"
        );

        //System.out.println("Pass - Input is now enabled (disabled attribute removed)");
        String testText = "Automation Test";
        page.typeIntoInput(testText);
        //System.out.println("Typed:"+testText);
        page.clickSubmit();
        //System.out.println("Clicked Submit.");
        String alertText = page.acceptAlertAndGetText();
        Assert.assertTrue(alertText.contains(testText),
                "Alert Text not contain "+testText+" Actual: "+alertText);
        //System.out.println("Pass - alert contains: "+ testText);
    }


    @AfterMethod
    public void tearDown(){
        if(driver!=null){
            driver.quit();
            //System.out.println("Browser Closed");
        }

        if(tempHtmlFile!= null && tempHtmlFile.exists()){
            boolean deleted = tempHtmlFile.delete();
            //System.out.println(deleted?"Temp file deleted. ":"Could Not delete temp file.");
        }
    }
}
