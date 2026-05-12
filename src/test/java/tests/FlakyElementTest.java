package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.FlakyPage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FlakyElementTest {
    private WebDriver driver;
    private File tempHtmlFile;

    @BeforeMethod
    public void setUp() throws IOException{
        String html = """
                <!DOCTYPE html>
                                <html>
                                <head>
                                    <title>Flaky Element Test</title>
                                </head>
                                <body>
                               \s
                                    <h2>Flaky Element Page</h2>
                                    <p>Button will appear after a random delay...</p>
                               \s
                                    <div id="buttonContainer"></div>
                               \s
                                    <script>
                                        var delay    = Math.floor(Math.random() * 3000) + 1000;
                                        var randomId = 'btn_' + Math.floor(Math.random() * 9000 + 1000);
                               \s
                                        setTimeout(function () {
                                            var btn         = document.createElement('button');
                                            btn.id          = randomId;
                                            btn.className   = 'click-me-btn';
                                            btn.textContent = 'Click Me Now';
                               \s
                                            btn.addEventListener('click', function () {
                                                btn.textContent = 'Clicked!';
                                                btn.className   = 'clicked-btn';
                                            });
                               \s
                                            document.getElementById('buttonContainer').appendChild(btn);
                                        }, delay);
                                    </script>
                               \s
                                </body>
                                </html>
                """;

        tempHtmlFile = new File(System.getProperty("java.io.tmpdir"),"flaky.html");
        try(FileWriter writer = new FileWriter(tempHtmlFile)){
            writer.write(html);
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
    public void testFlakyButtonClick() {

        FlakyPage page = new FlakyPage(driver);
        page.waitForButtonToAppear();
        String initialText = page.getButtonText();

        Assert.assertEquals(
                initialText,
                "Click Me Now",
                "Button text before click should be 'Click Me Now'"
        );
       //System.out.println("PASS — Initial button text : " + initialText);

        page.clickButton();
        page.waitForButtonToShowClicked();
        String afterText = page.getClickedButtonText();

        Assert.assertEquals(
                afterText,
                "Clicked!",
                "Button text after click should be 'Clicked!' but got: " + afterText
        );
        //System.out.println("PASS — Button text after click: " + afterText);
    }
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            //System.out.println("Browser closed.");
        }

        if (tempHtmlFile != null && tempHtmlFile.exists()) {
            boolean deleted = tempHtmlFile.delete();
            //System.out.println(deleted ? "Temp file deleted." : "Could not delete temp file.");
        }
    }
}
