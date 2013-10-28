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
import net.juniper.space.models.demo.device.DemoIpRange;
import net.juniper.space.services.demo.IpRangeService;
import net.juniper.wtf.core.repository.PageResult;
import net.juniper.wtf.core.util.ParamHelper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Path("/demoipranges")
public class DemoIpRangeRestService {
	private IpRangeService service = ServiceLocator.getService(IpRangeService.class);
	
	@GET 
	@Path("/ctx/{seg}") 
	@Produces(MediaType.APPLICATION_JSON)
	public PageResult<DemoIpRange> getIpRanges(@PathParam("seg") PathSegment seg) {
		Pageable pagination = ParamHelper.extractPageableInfo(seg);
		Specification<DemoIpRange> spec = ParamHelper.extractSpecification(seg, DemoIpRange.class);
		Page<DemoIpRange> page = service.getIpRanges(spec, pagination);
		PageResult<DemoIpRange> ps = new PageResult<DemoIpRange>(page);
		if(page.getTotalElements() == 0){
			DemoIpRange demo1 = new DemoIpRange();
			demo1.setId(1);
			demo1.setStartip("10.208.11.1");
			demo1.setEndip("10.208.11.255");
			ps.getRecords().add(demo1);
			
			DemoIpRange demo2 = new DemoIpRange();
			demo2.setId(2);
			demo2.setStartip("20.111.1.1");
			demo2.setEndip("20.111.1.255");
			ps.getRecords().add(demo2);
		}
		return ps;
		
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoIpRange getIpRange(@PathParam("id") Integer id){
		return service.getIpRange(id);
	}
 
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoIpRange updateIpRange(DemoIpRange device){
		return service.updateIpRange(device);
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public DemoIpRange addIpRange(DemoIpRange device){
		return service.saveIpRange(device);
	}
 
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteIpRange(@PathParam("id") Integer id){
		service.deleteIpRange(id);
	}
}
