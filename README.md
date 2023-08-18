# undetected-chromedriver for java

https://github.com/mabinogi233/UndetectedChromedriver
___

**now the driver is recommended chromedriver version > 108, if you wang to
use <108, you should make --headless=chrome not --headless=new**

Custom Selenium Chromedriver for Java can pass almost all selenium check. It's the Java version for undetected-chromedriver.

This tool is refer to **ultrafunkamsterdam/undetected-chromedriver** and **fysh711426
/UndetectedChromeDriver**

https://github.com/ultrafunkamsterdam/undetected-chromedriver

https://github.com/fysh711426/UndetectedChromeDriver

---

## · how to use it

1. java version >= 11 (recommend)
2. selenium version > 4.0.0 (must, because when version < 4 selenium can't excute cdp script)
3. com.alibaba.fastjson (if you wang to use other tool to parse json, you can edit source code)

If you satisfied these requriement, you can copy the source code in a package of your project to use it.

It doesn't use by Maven because when I use maven to import selenium version 4, Maven also import some selenium package jar of version 3.

---

## · example


#### if use chromeOptions, recommend use this

```        
String driver_home = "YOUR DRIVER HOME";
ChromeOptions chrome_options = new ChromeOptions();
chrome_options.addArguments("--window-size=1920,1080");

//when chrome version > 108, use "--headless=new"
chrome_options.addArguments("--headless=new");

//when chrome version <= 108, use "--headless=chrome"
//chrome_options.addArguments("--headless=chrome");

//ChromeDriverBuilder could throw RuntimeError, you can catch it, *catch it is unnecessary
ChromeDriver chromeDriver1 = new ChromeDriverBuilder()
                .build(chrome_options,driver_home);
				
chromeDriver1.get("https://bot.sannysoft.com");

```

#### don't use chromeOptions

```
ChromeDriver chromeDriver2 = new ChromeDriverBuilder()
                .build(driver_home);

chromeDriver2.get("https://bot.sannysoft.com");
```

---

## · v1.0.1 version log

This is the second version. fix headless mode can't use problem.

Use ChromeDriverBuilder to get a UndetectedChromeDriver

param

+ ChromeOptions options **default null**
  + same as selenium chrome options
+ String driverExecutablePath **notnull**
  + chromeDriver absolute path         
+ String binaryLocation  **default null**
  + excutable chrome path
+ boolean headless  **default false**
  + headless or not
+ boolean suppressWelcome  **default true**
  + if this is true, chrome won't show welcome bar 
+ boolean needPrintChromeInfo  **default false**
  + if this is true, System.out will print chrome process's error and other mssage
+ Map <String, Object> prefs  **default null** 
  + when prefs not null, you could make sure options hasn't experimentalOptions params prefs 


