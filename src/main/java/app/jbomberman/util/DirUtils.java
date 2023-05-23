
package app.jbomberman.util;

import java.io.File;

public class DirUtils
{
    public static String getAppData()
    {
        String ret;
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) {
            ret = System.getenv("APPDATA");
        } else if (OS.contains("LINUX")) {
            ret = System.getProperty("user.home") + File.separator + ".config";
        } else if (OS.contains("MAC")) {
            ret = System.getProperty("user.home") + "/Library/Application Support";
        } else {
            throw new RuntimeException("Unrecognized OS " + OS);
        }
        return ret;
    }
}
