package havis.custom.harting.tools.rest;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import havis.custom.harting.tools.ToolsManager;
import havis.custom.harting.tools.exception.ToolsException;
import havis.net.rest.shared.Resource;

@Path("tools")
public class ToolsService extends Resource {
	private ToolsManager toolsManager;
	
	public ToolsService() {
		toolsManager = new ToolsManager();
	}
	
	@GET
	@Path("taglist/file/{epc}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getItemsCSV(@PathParam("epc") String epc) throws ToolsException {
		StringWriter writer = new StringWriter();
		toolsManager.exportCsv(epc, writer);
		String filename = String.format("TagList_%s.csv", new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
		byte[] data = writer.toString().getBytes(StandardCharsets.UTF_8);
		return Response.ok(data, MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
				.header("Content-Type", "text/plain; charset=utf-8").header("Content-Length", data.length).build();
	}
}
