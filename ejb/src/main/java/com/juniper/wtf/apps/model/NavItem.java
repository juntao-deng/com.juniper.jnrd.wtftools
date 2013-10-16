package com.juniper.wtf.apps.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table (name="wtf_navitem")
@XmlRootElement(name = "navitem")  
@XmlType(propOrder = {"id", "name", "url", "groupId", "target"})  
@XmlAccessorType(XmlAccessType.FIELD)  
public class NavItem extends NavType {
	@Id
    @GeneratedValue
	private String id;
	@NotNull
	private String name;
	@NotNull
	private String url;
	@NotNull
	private String groupId;
	@NotNull
	private String target;
	
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
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
}
