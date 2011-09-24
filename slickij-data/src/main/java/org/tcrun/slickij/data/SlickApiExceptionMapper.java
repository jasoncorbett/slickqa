package org.tcrun.slickij.data;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author jcorbett
 */
@Provider
public class SlickApiExceptionMapper implements ExceptionMapper<WebApplicationException>
{

	@Override
	public Response toResponse(WebApplicationException e)
	{
		return SlickGenericExceptionMapper.toResponse(e);
	}
}
