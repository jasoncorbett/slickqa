package org.tcrun.slickij.data;

import java.io.IOException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author jcorbett
 */
public class SlickGenericExceptionMapper
{

	public static Response toResponse(WebApplicationException e)
	{
		String message = e.getMessage();
		String type = e.getClass().getName();
		if (e.getCause() != null)
		{
			message = e.getCause().getMessage();
			type = e.getCause().getClass().getName();
		}
		ApiErrorMessage errorMessage = new ApiErrorMessage(type, message);
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			Response response = Response.status(e.getResponse().getStatus()).entity(mapper.writeValueAsString(errorMessage)).type(MediaType.APPLICATION_JSON_TYPE).build();
			return response;
		} catch (IOException ex)
		{
			return e.getResponse();
		}
	}
}

class ApiErrorMessage
{

	public class ApiError
	{

		private String type;
		private String message;

		public ApiError(String type, String message)
		{
			this.type = type;
			this.message = message;
		}

		public String getType()
		{
			return type;
		}

		public String getMessage()
		{
			return message;
		}
	}
	private ApiError error;

	public ApiErrorMessage(String type, String message)
	{
		error = new ApiError(type, message);
	}

	public ApiError getError()
	{
		return error;
	}
}
