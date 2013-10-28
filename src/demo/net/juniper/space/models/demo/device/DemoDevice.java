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
@Table (name="demo_device")
@XmlRootElement(name = "demodevice")  
@XmlType(propOrder = {"id", "name", "address", "provider", "during", "descs", "state", "url"})  
@XmlAccessorType(XmlAccessType.FIELD)  
public class DemoDevice {
	@Id
	@GeneratedValue
	private Integer id;
	@NotNull
	private String name;
	@NotNull
	private String url;
	@NotNull
	private String address;
	@NotNull
	private String during;
	@NotNull
	private String descs;
	@NotNull
	private String state;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDuring() {
		return during;
	}
	public void setDuring(String during) {
		this.during = during;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String desc) {
		this.descs = desc;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
