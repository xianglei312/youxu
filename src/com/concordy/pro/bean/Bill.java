package com.concordy.pro.bean;

import java.util.List;
/*****
 * @author Scleo
 */
public class Bill {
	private String id ;
	private	String billDate;
	private Category category;
	private String createOn;
	private String description;
	private String dueDate;
	private int amount;
	private List<Itemlist> items;
	private String lastUpdatedOn;
	private int status;
	private Vendor vendor;
	private RecurringSetting recurringSetting;

	public class Item{
		private String billId;
		private String id;
		private String name;
		private int pricePerUnit;
		private int quantity;
		private int total;
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
		public int getPricePerUnit() {
			return pricePerUnit;
		}
		public void setPricePerUnit(int pricePerUnit) {
			this.pricePerUnit = pricePerUnit;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}

		public Item(){}
		public Item(String billId, String id, String name, int pricePerUnit,
				int quantity, int total) {
			super();
			this.billId = billId;
			this.id = id;
			this.name = name;
			this.pricePerUnit = pricePerUnit;
			this.quantity = quantity;
			this.total = total;
		}
		@Override
		public String toString() {
			return "Item [billId=" + billId + ", id=" + id + ", name=" + name
					+ ", pricePerUnit=" + pricePerUnit + ", quantity="
					+ quantity + ", total=" + total + "]";
		}



	}

	public Bill(){};


	public Bill(String id, String billDate, Category category,
			String createOn, String description, String dueDate,
			boolean isRecurring, List<Itemlist> items, String lastUpdatedOn,
			int status, Vendor vendor,int amount,RecurringSetting recurringSetting) {
		super();
		this.id = id;
		this.billDate = billDate;
		this.category = category;
		this.createOn = createOn;
		this.description = description;
		this.dueDate = dueDate;
		this.items = items;
		this.lastUpdatedOn = lastUpdatedOn;
		this.status = status;
		this.vendor = vendor;
		this.amount = amount;
		this.recurringSetting = recurringSetting;
	}
	public Bill(String billDate, Category category,
			String createOn, String description, String dueDate,
			boolean isRecurring, List<Itemlist> items, String lastUpdatedOn,
			int status, Vendor vendor,int amount,RecurringSetting recurringSetting) {
		super();
		this.billDate = billDate;
		this.category = category;
		this.createOn = createOn;
		this.description = description;
		this.dueDate = dueDate;
		this.items = items;
		this.lastUpdatedOn = lastUpdatedOn;
		this.status = status;
		this.vendor = vendor;
		this.amount = amount;
		this.recurringSetting = recurringSetting;
	}
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


	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
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


	public List<Itemlist> getItems() {
		return items;
	}
	public void setItems(List<Itemlist> items) {
		this.items = items;
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
