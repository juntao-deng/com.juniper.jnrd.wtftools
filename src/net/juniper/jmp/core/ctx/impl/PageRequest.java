package net.juniper.jmp.core.ctx.impl;

import java.io.Serializable;

import net.juniper.jmp.wtf.ctx.Pageable;

public class PageRequest implements Pageable, Serializable {
	private static final long serialVersionUID = -4153345207626971352L;
	private int pageIndex;
	private int pageSize;
	
	public PageRequest(int pageIndex, int pageSize){
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}
	
	@Override
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	@Override
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	
}