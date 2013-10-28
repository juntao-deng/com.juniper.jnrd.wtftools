package net.juniper.space.models.demo.device;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table (name="demo_iprange")
@XmlRootElement(name = "demoiprange")  
@XmlType(propOrder = {"id", "startip", "endip"})  
@XmlAccessorType(XmlAccessType.FIELD)  
public class DemoIpRange {
	@Id
	@GeneratedValue
	private Integer id;
	@NotNull
	private String startip;
	@NotNull
	private String endip;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStartip() {
		return startip;
	}
	public void setStartip(String startip) {
		this.startip = startip;
	}
	public String getEndip() {
		return endip;
	}
	public void setEndip(String endip) {
		this.endip = endip;
	}
}
