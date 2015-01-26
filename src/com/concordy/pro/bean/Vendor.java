package com.concordy.pro.bean;

public class Vendor extends Entity{
	private String id;
	private String name;
	public Vendor(){}
	public Vendor(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Vendor [id=" + id + ", name=" + name + "]";
	}


}
