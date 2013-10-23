package net.juniper.wtf.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageResult<T> {
	private Integer pageSize;
	private Long totalRecords;
	private Integer pageIndex;
	private List<T> records;
	public PageResult(Page<T> page){
		this.records = page.getContent();
		this.pageSize = page.getSize();
		this.totalRecords = page.getTotalElements();
		this.pageIndex = page.getNumber();
	}
	public List<T> getRecords() {
		return records;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public Long getTotalRecords() {
		return totalRecords;
	}
	public Integer getPageIndex() {
		return pageIndex;
	}
}
