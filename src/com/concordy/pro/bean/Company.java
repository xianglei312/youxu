package com.concordy.pro.bean;

public class Company {


	private int Id;
	private String Name;
	private String DisplayName;
	private String LogoUrl;
	private String Website;
	private String CreatedOn;
	private int CreatedBy;
	private String LastUpdatedOn;
	private int LastUpdatedBy;
	private Address address;

	public Company(){};


	public Company(int id, String name, String displayName, String logoUrl,
			String website, String createdOn, int createdBy,
			String lastUpdatedOn, int lastUpdatedBy, Address address) {
		super();
		Id = id;
		Name = name;
		DisplayName = displayName;
		LogoUrl = logoUrl;
		Website = website;
		CreatedOn = createdOn;
		CreatedBy = createdBy;
		LastUpdatedOn = lastUpdatedOn;
		LastUpdatedBy = lastUpdatedBy;
		this.address = address;
	}


	public int getId() {
		return Id;
	}


	public void setId(int id) {
		Id = id;
	}


	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getDisplayName() {
		return DisplayName;
	}


	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}


	public String getLogoUrl() {
		return LogoUrl;
	}


	public void setLogoUrl(String logoUrl) {
		LogoUrl = logoUrl;
	}


	public String getWebsite() {
		return Website;
	}


	public void setWebsite(String website) {
		Website = website;
	}


	public String getCreatedOn() {
		return CreatedOn;
	}


	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}


	public int getCreatedBy() {
		return CreatedBy;
	}


	public void setCreatedBy(int createdBy) {
		CreatedBy = createdBy;
	}


	public String getLastUpdatedOn() {
		return LastUpdatedOn;
	}


	public void setLastUpdatedOn(String lastUpdatedOn) {
		LastUpdatedOn = lastUpdatedOn;
	}


	public int getLastUpdatedBy() {
		return LastUpdatedBy;
	}


	public void setLastUpdatedBy(int lastUpdatedBy) {
		LastUpdatedBy = lastUpdatedBy;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}





}
