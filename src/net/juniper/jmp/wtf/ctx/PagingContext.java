package net.juniper.jmp.wtf.ctx;


public class PagingContext {
	private Filterable filter;
	private Pageable pageable;
	
	public Pageable getPageable() {
		return pageable;
	}
	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}
	public Filterable getFilter() {
		return filter;
	}
	public void setFilter(Filterable filter) {
		this.filter = filter;
	}
}
