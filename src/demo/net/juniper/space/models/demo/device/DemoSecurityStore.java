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
@Table (name="demo_securitystore")
@XmlRootElement(name = "demosecuritystore")  
@XmlType(propOrder = {"id", "user", "password"})  
@XmlAccessorType(XmlAccessType.FIELD)  
public class DemoSecurityStore {
	@Id
	@GeneratedValue
	private Integer id;
	@NotNull
	private String user;
	@NotNull
	private String password;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
