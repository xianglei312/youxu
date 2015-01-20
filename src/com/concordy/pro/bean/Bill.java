package com.concordy.pro.bean;

import java.io.Serializable;
import java.util.List;
/*****
 * @author Scleo
 */
public class Bill implements Serializable {
	private String id ;
	private	String billDate;
	private Category category;
	private String createOn;
	private String description;
	private String dueDate;
	private float amount;
	private List<Item> items;
	private String lastUpdatedOn;
	private int status;
	private Vendor vendor;
	private RecurringSetting recurringSetting;
	
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public static class Item implements Serializable{
		private String billId;
		private String id;
		private String name;
		private String pricePerUnit;
		private int quantity;
		private float total;
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
		public float getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}

		public Item(){}
		public Item(String billId, String id, String name, String pricePerUnit,
				int quantity, int total) {
			super();
			this.billId = billId;
			this.id = id;
			this.name = name;
			this.pricePerUnit = pricePerUnit;
			this.quantity = quantity;
			this.total = total;
		}
		public Item(String name, int number, String price) {
			this.name = name;
			this.pricePerUnit = price;
			this.quantity  = number;
		}
		@Override
		public String toString() {
			return "Item [billId=" + billId + ", id=" + id + ", name=" + name
					+ ", pricePerUnit=" + pricePerUnit + ", quantity="
					+ quantity + ", total=" + total + "]";
		}



	}

	public Bill(){};
	public Bill(String billDate, int amount,String dueDate) {
		super();
		this.billDate = billDate;
		this.dueDate = dueDate;
		this.amount = amount;
	}




	public RecurringSetting getRecurringSetting() {
		return recurringSetting;
	}


	public void setRecurringSetting(RecurringSetting recurringSetting) {
		this.recurringSetting = recurringSetting;
	}


	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getCreateOn() {
		return createOn;
	}
	public void setCreateOn(String createOn) {
		this.createOn = createOn;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	@Override
	public String toString() {
		return "Bill [id=" + id + ", billDate=" + billDate + ", category="
				+ category + ", createOn=" + createOn + ", description="
				+ description + ", dueDate=" + dueDate + ", items=" + items + ", lastUpdatedOn="
				+ lastUpdatedOn + ", status=" + status + ", vendor=" + vendor
				+ "]";
	}
}
