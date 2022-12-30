package com.frogking.chromedriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SysUtil {

    /**
     * judge os is Windows
     * @return true：is windows  false：another
     */
    public static boolean isWindows() {
        String osName = getOsName();
        return osName != null && osName.startsWith("Windows");
    }

    /**
     * judge os is mac
     * @return true：is mac  false：another
     */
    public static boolean isMacOs() {
        String osName = getOsName();

        return osName != null && osName.startsWith("Mac");
    }

    /**
     * judge os is Linux
     * @return true：is linux  false：another
     */
    public static boolean isLinux() {
        String osName = getOsName();

        return (osName != null && osName.startsWith("Linux")) || (!isWindows() && !isMacOs());
    }

    /**
     * get os name
     * @return os.name
     */
    public static String getOsName() {
        return System.getProperty("os.name");
    }

    /**
     * get env PATH
     * @return PATHs
     */
    public static List<String> getPath(){
        String sep = System.getProperty("path.separator");
        String paths = System.getenv("PATH");
        return new ArrayList<>(Arrays.asList(paths.split(sep)));
    }

    /**
     * get one PATH by key
     * @param key PATH key
     * @return PATH value
     */
    public static String getString(String key){
        return System.getenv(key);
    }

}
