package org.tcrun.slickij.api.data;

/**
 *
 * @author jcorbett
 */
public class InvalidDataError extends Exception
{
	public InvalidDataError(String type, String property, String error)
	{
		super("Invalid data provided for '" + type + "''s '" + property + "': " + error);
	}
}
