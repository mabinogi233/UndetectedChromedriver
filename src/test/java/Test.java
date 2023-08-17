import com.frogking.chromedriver.ChromeDriverBuilder;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class Test {

    public static void main(String[] args){

        String driver_home = "your driver home";

        // 1  if use chromeOptions, recommend use this
        // ChromeDriverBuilder could throw RuntimeError, you can catch it, *catch it is unnecessary
        ChromeOptions chrome_options = new ChromeOptions();
        chrome_options.addArguments("--window-size=1920,1080");
        //chrome_options.addArguments("--headless=new"); when chromedriver > 108.x.x.x
        //chrome_options.addArguments("--headless=chrome"); when chromedriver <= 108.x.x.x

        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(driver_home))
                .usingAnyFreePort()
                .build();

        //ChromeDriver chromeDriver1 = new ChromeDriver(service);
        ChromeDriver chromeDriver1 = new ChromeDriverBuilder()
                .build(chrome_options,driver_home);

        // 2  don't use chromeOptions
        //ChromeDriver chromeDriver2 = new ChromeDriverBuilder()
        //        .build("your driver home");

        chromeDriver1.get("your url");
        //chromeDriver2.get("your url");

    }
}
