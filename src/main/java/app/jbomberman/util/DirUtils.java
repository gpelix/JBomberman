/*
 * Copyright (c) 2023, Gabriele Pelissetto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement:
 *      This product includes software developed by Gabriele Pelissetto
 *
 * 4. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
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
