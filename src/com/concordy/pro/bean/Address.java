package com.concordy.pro.bean;

public class Address {
	private int Id;
	private boolean IsDefault;
	private int AddressType;
	private String  Line1;
	private String  Line2;
	private String  Line3;
	private String  Line4;
	private String City;
	private String Province; 
	private String Country;
	private String Description;
	private String CreatedOn;
	private int CreatedBy;
	private String LastUpdatedOn;
	private int LastUpdatedBy;

	public Address(){};

	public Address(int id, boolean isDefault, int addressType, String line1,
			String line2, String line3, String line4, String city,
			String province, String country, String description,
			String createdOn, int createdBy, String lastUpdatedOn,
			int lastUpdatedBy) {
		super();
		Id = id;
		IsDefault = isDefault;
		AddressType = addressType;
		Line1 = line1;
		Line2 = line2;
		Line3 = line3;
		Line4 = line4;
		City = city;
		Province = province;
		Country = country;
		Description = description;
		CreatedOn = createdOn;
		CreatedBy = createdBy;
		LastUpdatedOn = lastUpdatedOn;
		LastUpdatedBy = lastUpdatedBy;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public boolean isIsDefault() {
		return IsDefault;
	}
	public void setIsDefault(boolean isDefault) {
		IsDefault = isDefault;
	}
	public int getAddressType() {
		return AddressType;
	}
	public void setAddressType(int addressType) {
		AddressType = addressType;
	}
	public String getLine1() {
		return Line1;
	}
	public void setLine1(String line1) {
		Line1 = line1;
	}
	public String getLine2() {
		return Line2;
	}
	public void setLine2(String line2) {
		Line2 = line2;
	}
	public String getLine3() {
		return Line3;
	}
	public void setLine3(String line3) {
		Line3 = line3;
	}
	public String getLine4() {
		return Line4;
	}
	public void setLine4(String line4) {
		Line4 = line4;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getProvince() {
		return Province;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
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




}
