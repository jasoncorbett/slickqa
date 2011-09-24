package org.tcrun.slickij.data;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author jcorbett
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundError>
{
	@Override
	public Response toResponse(NotFoundError exception)
	{
		return SlickGenericExceptionMapper.toResponse(exception);
	}
}
