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
import net.juniper.space.models.demo.device.DemoSecurityStore;
import net.juniper.space.services.demo.SecurityStoreService;
import net.juniper.wtf.core.repository.PageResult;
import net.juniper.wtf.core.util.ParamHelper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Path("/demosecuritystores")
public class DemoSecurityStoreRestService {
	private SecurityStoreService service = ServiceLocator.getService(SecurityStoreService.class);
	
	@GET 
	@Path("/ctx/{seg}") 
	@Produces(MediaType.APPLICATION_JSON)
	public PageResult<DemoSecurityStore> getSecurityStores(@PathParam("seg") PathSegment seg) {
		Pageable pagination = ParamHelper.extractPageableInfo(seg);
		Specification<DemoSecurityStore> spec = ParamHelper.extractSpecification(seg, DemoSecurityStore.class);
		Page<DemoSecurityStore> page = service.getSecurityStores(spec, pagination);
		PageResult<DemoSecurityStore> ps = new PageResult<DemoSecurityStore>(page);
		if(page.getTotalElements() == 0){
			DemoSecurityStore store1 = new DemoSecurityStore();
			store1.setId(1);
			store1.setPassword("root");
			store1.setUser("root");
			ps.getRecords().add(store1);
			
			DemoSecurityStore store2 = new DemoSecurityStore();
			store2.setId(1);
			store2.setPassword("admin");
			store2.setUser("admin");
			ps.getRecords().add(store2);
		}
		return ps;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoSecurityStore getSecurityStore(@PathParam("id") Integer id){
		return service.getSecurityStore(id);
	}
 
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoSecurityStore updateSecurityStore(DemoSecurityStore device){
		return service.updateSecurityStore(device);
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoSecurityStore addSecurityStore(DemoSecurityStore device){
		return service.saveSecurityStore(device);
	}
 
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteSecurityStore(@PathParam("id") Integer id){
		service.deleteSecurityStore(id);
	}
}
