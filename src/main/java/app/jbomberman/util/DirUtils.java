package app.jbomberman.util;

import java.io.File;

/**
 * DirUtils is a utility class that contains OS spacific functions to work
 * with directories
 *
 * @author gp
 */
public class DirUtils
{
	/**
	 * getAppData() is a function that gets the String representing the path to
	 * the directory where apps can store their data
	 * On Windows it is %APPDATA%, on MACOSX is $HOME/Library/Application Support
	 * while on the other *NIX's ~/.config/
	 *
	 * @return the appdata directory
	 * TODO: 5/24/23 It's untested on MAC
	 */
	public static String getAppData()
	{
		String ret;
		String OS = (System.getProperty("os.name")).toUpperCase();
		if (OS.contains("WIN"))
			ret = System.getenv("APPDATA");
		else if (OS.contains("LINUX") || OS.contains("SOLARIS") || OS.contains("BSD"))
			ret = System.getProperty("user.home") + File.separator + ".config";
		else if (OS.contains("MAC"))
			ret = System.getProperty("user.home") + "/Library/Application Support";
		else
			throw new RuntimeException("Unrecognized OS " + OS);
		return ret;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
