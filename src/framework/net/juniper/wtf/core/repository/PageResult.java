package net.juniper.wtf.core.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageResult<T> {
	private Integer pageSize;
	private Integer totalRecords;
	private Integer pageIndex;
	private List<T> records;
	public PageResult(Page<T> page){
		this.records = new ArrayList<T>();
		this.records.addAll(page.getContent());
		this.pageSize = page.getSize();
		this.totalRecords = (int) page.getTotalElements();
		this.pageIndex = page.getNumber();
	}
	
	public PageResult(List<T> records){
		this.records = records;
		this.pageSize = -1;
		this.totalRecords = this.records.size();
		this.pageIndex = 0;
	}

	public List<T> getRecords() {
		return records;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public Integer getPageIndex() {
		return pageIndex;
	}
}
