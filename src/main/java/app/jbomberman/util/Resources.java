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

import app.jbomberman.JBomberMan;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * Resource is a utility class providing uniform access to the resources
 *
 * @author gp
 */
public class Resources
{
	/**
	 * relativeClass is the class to which the Resources are attributed
	 */
	private static final Class<?> relativeClass = JBomberMan.class;

	/**
	 * Utility function to of an InputStream out of a URI
	 *
	 * @param URI file location
	 * @return InputStream associated with the URI
	 */
	public static InputStream getResourceAsStream(String URI)
	{
		InputStream ret;
		try {
			ret = new BufferedInputStream(Objects.requireNonNull(relativeClass.getResourceAsStream(URI)));
		} catch (NullPointerException e) {
			throw new RuntimeException("Failed to acquire resource " + URI);
		}
		return ret;
	}

	/**
	 * Utility function that opens the URI and read its content assuming
	 * it's not a binary file
	 *
	 * @param URI file location
	 * @return String content of the file
	 */
	public static String readResource(String URI)
	{
		StringBuilder stringBuilder = new StringBuilder();
		try (InputStream inputStream = getResourceAsStream(URI);
				 Scanner scanner = new Scanner(inputStream))
		{
			while (scanner.hasNext()) {
				stringBuilder.append(scanner.next());
			}
			return stringBuilder.toString();
		} catch (IOException e) {
			throw new RuntimeException("Unable to read resource " + URI + " due to error: " + e.getMessage());
		}
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
