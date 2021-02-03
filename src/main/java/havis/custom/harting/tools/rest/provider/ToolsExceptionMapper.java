package havis.custom.harting.tools.rest.provider;

import havis.custom.harting.tools.exception.ToolsException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ToolsExceptionMapper implements ExceptionMapper<ToolsException> {

	@Override
	public Response toResponse(ToolsException e) {
		return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}
}
