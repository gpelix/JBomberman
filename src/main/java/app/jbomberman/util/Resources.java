package app.jbomberman.util;

import app.jbomberman.JBomberMan;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

/*
 * This utility class provides uniform access to the resources
 *
 * @author gp
 */
public class Resources
{
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
