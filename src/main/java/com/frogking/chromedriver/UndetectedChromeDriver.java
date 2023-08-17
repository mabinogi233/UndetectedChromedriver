package com.frogking.chromedriver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.*;

public class UndetectedChromeDriver extends ChromeDriver{

    private boolean _headless;

    private Process _browser;

    private boolean _keepUserDataDir;

    private String _userDataDir;

    private ChromeOptions chromeOptions;


    public void get(String url) {
        if (_headless) {
            _headless();
        }
        _cdcProps();
        super.get(url);
    }


    public void quit(){
        super.quit();
        // kill process
        _browser.destroyForcibly();
        //delete temp user dir
        if(_keepUserDataDir){
            for (int i = 0;i<5;i++) {
                try {
                    File file =  new File(_userDataDir);
                    if(!file.exists()){
                        break;
                    }
                    boolean f =file.delete();
                    if(f){
                        break;
                    }
                }
                catch (Exception e) {
                    try {
                        Thread.sleep(300);
                    }catch (Exception ignored){ }
                }
            }
        }

    }

    public UndetectedChromeDriver(ChromeOptions chromeOptions,
                                  boolean headless,
                                  boolean keepUserDataDir,
                                  String userDataDir,
                                  Process browser){

        super(chromeOptions);
        this.chromeOptions = chromeOptions;
        _browser = browser;
        _headless = headless;
        _keepUserDataDir = keepUserDataDir;
        _userDataDir = userDataDir;
    }

    /**
     * configure headless
     */
    private void _headless(){
        //set navigator.webdriver
        Object f = this.executeScript("return navigator.webdriver");
        if(f==null){
            return;
        }

        Map<String,Object> params1 = new HashMap<>();
        params1.put("source","Object.defineProperty(window, 'navigator', {\n" +
                "    value: new Proxy(navigator, {\n" +
                "        has: (target, key) => (key === 'webdriver' ? false : key in target),\n" +
                "        get: (target, key) =>\n" +
                "            key === 'webdriver' ?\n" +
                "            false :\n" +
                "            typeof target[key] === 'function' ?\n" +
                "            target[key].bind(target) :\n" +
                "            target[key]\n" +
                "        })\n" +
                "});");

        this.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument",params1);

        //set ua
        Map<String,Object> params2 = new HashMap<>();
        params2.put("userAgent",((String)this.executeScript("return navigator.userAgent")).replace("Headless",""));
        this.executeCdpCommand("Network.setUserAgentOverride",params2);

        Map<String,Object> params3 = new HashMap<>();
        params3.put("source","Object.defineProperty(navigator, 'maxTouchPoints', {get: () => 1});");
        this.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument",params3);

        Map<String,Object> params4 = new HashMap<>();
        params4.put("source","" +
                "Object.defineProperty(navigator.connection, 'rtt', {get: () => 100});\n" +
                "// https://github.com/microlinkhq/browserless/blob/master/packages/goto/src/evasions/chrome-runtime.js\n" +
                "window.chrome = {\n" +
                "        app: {\n" +
                "            isInstalled: false,\n" +
                "            InstallState: {\n" +
                "                DISABLED: 'disabled',\n" +
                "                INSTALLED: 'installed',\n" +
                "                NOT_INSTALLED: 'not_installed'\n" +
                "            },\n" +
                "            RunningState: {\n" +
                "                CANNOT_RUN: 'cannot_run',\n" +
                "                READY_TO_RUN: 'ready_to_run',\n" +
                "                RUNNING: 'running'\n" +
                "            }\n" +
                "        },\n" +
                "        runtime: {\n" +
                "            OnInstalledReason: {\n" +
                "                CHROME_UPDATE: 'chrome_update',\n" +
                "                INSTALL: 'install',\n" +
                "                SHARED_MODULE_UPDATE: 'shared_module_update',\n" +
                "                UPDATE: 'update'\n" +
                "            },\n" +
                "            OnRestartRequiredReason: {\n" +
                "                APP_UPDATE: 'app_update',\n" +
                "                OS_UPDATE: 'os_update',\n" +
                "                PERIODIC: 'periodic'\n" +
                "            },\n" +
                "            PlatformArch: {\n" +
                "                ARM: 'arm',\n" +
                "                ARM64: 'arm64',\n" +
                "                MIPS: 'mips',\n" +
                "                MIPS64: 'mips64',\n" +
                "                X86_32: 'x86-32',\n" +
                "                X86_64: 'x86-64'\n" +
                "            },\n" +
                "            PlatformNaclArch: {\n" +
                "                ARM: 'arm',\n" +
                "                MIPS: 'mips',\n" +
                "                MIPS64: 'mips64',\n" +
                "                X86_32: 'x86-32',\n" +
                "                X86_64: 'x86-64'\n" +
                "            },\n" +
                "            PlatformOs: {\n" +
                "                ANDROID: 'android',\n" +
                "                CROS: 'cros',\n" +
                "                LINUX: 'linux',\n" +
                "                MAC: 'mac',\n" +
                "                OPENBSD: 'openbsd',\n" +
                "                WIN: 'win'\n" +
                "            },\n" +
                "            RequestUpdateCheckStatus: {\n" +
                "                NO_UPDATE: 'no_update',\n" +
                "                THROTTLED: 'throttled',\n" +
                "                UPDATE_AVAILABLE: 'update_available'\n" +
                "            }\n" +
                "        }\n" +
                "}\n" +
                "\n" +
                "// https://github.com/microlinkhq/browserless/blob/master/packages/goto/src/evasions/navigator-permissions.js\n" +
                "if (!window.Notification) {\n" +
                "        window.Notification = {\n" +
                "            permission: 'denied'\n" +
                "        }\n" +
                "}\n" +
                "\n" +
                "const originalQuery = window.navigator.permissions.query\n" +
                "window.navigator.permissions.__proto__.query = parameters =>\n" +
                "        parameters.name === 'notifications'\n" +
                "            ? Promise.resolve({ state: window.Notification.permission })\n" +
                "            : originalQuery(parameters)\n" +
                "        \n" +
                "const oldCall = Function.prototype.call \n" +
                "function call() {\n" +
                "        return oldCall.apply(this, arguments)\n" +
                "}\n" +
                "Function.prototype.call = call\n" +
                "\n" +
                "const nativeToStringFunctionString = Error.toString().replace(/Error/g, 'toString')\n" +
                "const oldToString = Function.prototype.toString\n" +
                "\n" +
                "function functionToString() {\n" +
                "        if (this === window.navigator.permissions.query) {\n" +
                "            return 'function query() { [native code] }'\n" +
                "        }\n" +
                "        if (this === functionToString) {\n" +
                "            return nativeToStringFunctionString\n" +
                "        }\n" +
                "        return oldCall.call(oldToString, this)\n" +
                "}\n" +
                "// eslint-disable-next-line\n" +
                "Function.prototype.toString = functionToString");
        this.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument",params4);


    }

    /**
     * remove cdc
     */
    private void _cdcProps(){
        List<String> f = (List<String>)this.executeScript("let objectToInspect = window,\n" +
                "    result = [];\n" +
                "while(objectToInspect !== null)\n" +
                "{ result = result.concat(Object.getOwnPropertyNames(objectToInspect));\n" +
                "  objectToInspect = Object.getPrototypeOf(objectToInspect); }\n" +
                "return result.filter(i => i.match(/.+_.+_(Array|Promise|Symbol)/ig))");

        if(f!=null && f.size()>0) {
            Map<String, Object> param = new HashMap<>();
            param.put("source", "let objectToInspect = window,\n" +
                    "    result = [];\n" +
                    "while(objectToInspect !== null)\n" +
                    "{ result = result.concat(Object.getOwnPropertyNames(objectToInspect));\n" +
                    "  objectToInspect = Object.getPrototypeOf(objectToInspect); }\n" +
                    "result.forEach(p => p.match(/.+_.+_(Array|Promise|Symbol)/ig)\n" +
                    "                    &&delete window[p]&&console.log('removed',p))");
            this.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", param);
        }

    }

    /**
     *  set stealth
     */
    private void _stealth() {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream in = this.getClass().getResourceAsStream("/static/js/stealth.min.js");
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuffer.append(str);
                stringBuffer.append("\n");
            }
            in.close();
            bufferedReader.close();
        } catch (Exception ignored) { }
        Map<String, Object> params = new HashMap<>();
        params.put("source", stringBuffer.toString());
        this.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
    }

    @Override
    public void startSession(Capabilities capabilities) {
        if (capabilities==null){
            capabilities = this.chromeOptions;
        }
        super.startSession(capabilities);
    }

}



