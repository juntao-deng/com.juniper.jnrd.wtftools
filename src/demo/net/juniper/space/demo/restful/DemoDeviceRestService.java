package net.juniper.space.demo.restful;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

import net.juniper.space.core.locator.ServiceLocator;
import net.juniper.space.models.demo.device.DemoDevice;
import net.juniper.space.services.demo.DeviceService;
import net.juniper.wtf.core.repository.PageResult;
import net.juniper.wtf.core.util.ParamHelper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Path("/demodevices")
public class DemoDeviceRestService {
	private DeviceService service = ServiceLocator.getService(DeviceService.class);
	
	@GET 
	@Path("/ctx/{seg}") 
	@Produces(MediaType.APPLICATION_JSON)
	public PageResult<DemoDevice> getDevices(@PathParam("seg") PathSegment seg) {
		Pageable pagination = ParamHelper.extractPageableInfo(seg);
		Specification<DemoDevice> spec = ParamHelper.extractSpecification(seg, DemoDevice.class);
		Page<DemoDevice> page = service.getDevices(spec, pagination);
		return new PageResult<DemoDevice>(page);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoDevice getDevice(@PathParam("id") Integer id){
		return service.getDevice(id);
	}
 
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoDevice updateDevice(DemoDevice device){
		return service.updateDevice(device);
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoDevice addDevice(DemoDevice device){
		return service.saveDevice(device);
	}
 
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteDevice(@PathParam("id") Integer id){
		service.deleteDevice(id);
	}
	
	@POST
	@Path("/action/{actionseg}")
	@Produces(MediaType.APPLICATION_JSON)
	public void processAction(@PathParam("actionseg") PathSegment seg){
		for(int i = 0; i < 50; i ++){
			DemoDevice device = new DemoDevice();
			device.setId(i + 1);
			device.setAddress("10.208.11." + (i + 1));
			device.setDuring("10h");
			device.setName("Device " + (i + 1));
			device.setState("Running");
			device.setUrl("");
			device.setDescs("Device ..");
			service.saveDevice(device);
		}
		
	}
}
