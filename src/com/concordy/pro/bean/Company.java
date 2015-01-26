package com.concordy.pro.bean;


public class Company extends Entity{


	private int id;
	private String name;
	private String displayName;
	private String logoUrl;
	private String website;
	private String createdOn;
	private int createdBy;
	private String lastUpdatedOn;
	private int lastUpdatedBy;
	private Address address;
	protected int getId() {
		return id;
	}
	protected void setId(int id) {
		this.id = id;
	}


	protected String getName() {
		return name;
	}


	protected void setName(String name) {
		this.name = name;
	}


	protected String getDisplayName() {
		return displayName;
	}


	protected void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	protected String getLogoUrl() {
		return logoUrl;
	}


	protected void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}


	protected String getWebsite() {
		return website;
	}


	protected void setWebsite(String website) {
		this.website = website;
	}


	protected String getCreatedOn() {
		return createdOn;
	}


	protected void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}


	protected int getCreatedBy() {
		return createdBy;
	}


	protected void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}


	protected String getLastUpdatedOn() {
		return lastUpdatedOn;
	}


	protected void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}


	protected int getLastUpdatedBy() {
		return lastUpdatedBy;
	}


	protected void setLastUpdatedBy(int lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}


	protected Address getAddress() {
		return address;
	}


	protected void setAddress(Address address) {
		this.address = address;
	}


	public Company(){};


	public Company(int id, String name, String displayName, String logoUrl,
			String website, String createdOn, int createdBy,
			String lastUpdatedOn, int lastUpdatedBy, Address address) {
		super();
	}


}
