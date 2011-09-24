package org.tcrun.slickij.data;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author jcorbett
 */
public class NotFoundError extends WebApplicationException
{
	private Class<?> type;
	private String foundby;

	public NotFoundError(Class<?> type)
	{
		this(type, null);
	}

	public NotFoundError(Class<?> type, String foundby)
	{
		super(Status.NOT_FOUND);
		this.type = type;
		this.foundby = foundby;
	}

	@Override
	public String getMessage()
	{
		String message = type.getSimpleName() + " not found";
		if(foundby != null)
			message += " using '" + foundby + "'";
		message += ".";
		return message;
	}
}
