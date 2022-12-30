import com.frogking.chromedriver.ChromeDriverBuilder;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Test {

    public static void main(String[] args){

        String driver_home = "YOUR DRIVER HOME";

        // 1  if use chromeOptions, recommend use this
        // ChromeDriverBuilder could throw RuntimeError, you can catch it, *catch it is unnecessary
        ChromeOptions chrome_options = new ChromeOptions();
        chrome_options.addArguments("--window-size=1920,1080");

        ChromeDriver chromeDriver1 = new ChromeDriverBuilder()
                .build(chrome_options,driver_home);

        // 2  don't use chromeOptions
        ChromeDriver chromeDriver2 = new ChromeDriverBuilder()
                .build(driver_home);

        chromeDriver1.get("https://bot.sannysoft.com");

    }
}
