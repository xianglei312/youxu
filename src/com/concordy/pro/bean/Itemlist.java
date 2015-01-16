package com.concordy.pro.bean;

public class Itemlist{
	private String billId;
	private String id;
	private String name;
	private String pricePerUnit;
	private int quantity;
	private String total;
	public Itemlist(String name,int quantity,String total){
		super();
		this.name = name;
		this.quantity = quantity;
		this.total = total;
	}
	
	public Itemlist(String billId, String id, String name, String pricePerUnit,
			int quantity, String total) {
		super();
		this.billId = billId;
		this.id = id;
		this.name = name;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.total = total;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
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
	public String getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
}
