package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import pages.TablePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class DynamicTableTest {

    private WebDriver driver;
    private File tempHtmlFile;

    @BeforeMethod
    public void setUp() throws IOException{
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Dynamic Stock Table</title>
                <style>
                    body { font-family: Arial, sans-serif; padding: 20px; }
                    table { border-collapse: collapse; width: 50%; }
                    th, td { border: 1px solid #999; padding: 8px 12px; text-align: left; }
                    th { background-color: #4CAF50; color: white; }
                    button { margin-bottom: 16px; padding: 8px 16px;
                             background: #2196F3; color: white;
                             border: none; cursor: pointer; font-size: 14px; }
                </style>
            </head>
            <body>

                <button id="refreshBtn" onclick="refreshStock()">Refresh Stock</button>
                <table id="productTable">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Stock</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr><td>Laptop</td>   <td>$999</td>  <td>In Stock</td></tr>
                        <tr><td>Mouse</td>    <td>$25</td>   <td>Out of Stock</td></tr>
                        <tr><td>Keyboard</td> <td>$75</td>   <td>In Stock</td></tr>
                        <tr><td>Monitor</td>  <td>$350</td>  <td>Out of Stock</td></tr>
                        <tr><td>Headset</td>  <td>$120</td>  <td>In Stock</td></tr>
                    </tbody>
                </table>

                <script>
                    function refreshStock() {
                        var rows = document.querySelectorAll('#productTable tbody tr');
                        rows.forEach(function(row) {
                            var stockCell = row.cells[2];
                            stockCell.textContent = Math.random() < 0.5
                                ? 'In Stock'
                                : 'Out of Stock';
                        });
                    }
                </script>

            </body>
            </html>
            """;

        tempHtmlFile = new File(System.getProperty("java.io.tmpdir"),"temp_table.html");
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
    public void testStockChangesOnRefresh() throws InterruptedException{
        TablePage tablePage = new TablePage(driver);

        Set<Integer> uniqueCounts = new HashSet<>();

        for(int click=1;click<=3;click++){
            tablePage.clickRefreshStock();
            int intStockCount = tablePage.countInStockRows();
            uniqueCounts.add(intStockCount);

            //System.out.println("Click"+click+"In Stock ROws: "+ intStockCount);
        }
        Assert.assertTrue(
                uniqueCounts.size()>1,
                "Stock Count was the same after all 3 refreshes."+"Expected dynamic changes, but got: " + uniqueCounts
        );

        //System.out.println("Unique Counts: "+ uniqueCounts);
    }

    @AfterMethod
    public void tearDown(){
        if(driver!=null){
            driver.quit();
            //System.out.println("Browser Closed");
        }

        if(tempHtmlFile!= null && tempHtmlFile.exists()){
            boolean deleted = tempHtmlFile.delete();
            //System.out.println(deleted?"Temp file deleted: "+tempHtmlFile.getAbsolutePath():"Could not delete temp file: "+tempHtmlFile.getAbsolutePath());
        }
    }
}
