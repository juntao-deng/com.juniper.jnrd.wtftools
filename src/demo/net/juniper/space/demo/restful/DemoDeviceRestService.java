package net.juniper.space.demo.restful;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

import net.juniper.space.models.demo.device.DemoDevice;
import net.juniper.wtf.core.repository.PageResult;

@Path("/demodevices")
public interface DemoDeviceRestService {
	
	@GET 
	@Path("/ctx/{seg}") 
	@Produces(MediaType.APPLICATION_JSON)
	public PageResult<DemoDevice> getDevices(@PathParam("seg") PathSegment seg);
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoDevice getDevice(@PathParam("id") Integer id);
 
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoDevice updateDevice(DemoDevice device);
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoDevice addDevice(DemoDevice device);
 
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteDevice(@PathParam("id") Integer id);
	
	@POST
	@Path("/action/{actionseg}")
	@Produces(MediaType.APPLICATION_JSON)
	public void processAction(@PathParam("actionseg") PathSegment seg);
}
