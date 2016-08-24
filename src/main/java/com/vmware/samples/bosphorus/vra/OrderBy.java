package com.vmware.samples.bosphorus.vra;

public class OrderBy {
	public enum Order { UP, DOWN };
	
	private final Order order;
	
	private final String column;

	public OrderBy(String column, Order order) {
		super();
		this.order = order;
		this.column = column;
	}

	public Order getOrder() {
		return order;
	}

	public String getColumn() {
		return column;
	}
	
	public String toOdata() {
		return "$orderby=" + column + "+" + ((order == Order.UP) ? "asc" : "desc");
	}
}
