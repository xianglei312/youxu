package com.concordy.pro.bean;


public class RecurringSetting  extends Entity{
	private int repeatBy;
	private int interval;
	private int repeatOn;
	private String startDate;
	private int endsBy;
	//end on a particular date
	private String endDate;
	//end on N instance of recurrence.
	private int endOnTimes;
	public int getRepeatBy() {
		return repeatBy;
	}
	public void setRepeatBy(int repeatBy) {
		this.repeatBy = repeatBy;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getRepeatOn() {
		return repeatOn;
	}
	public void setRepeatOn(int repeatOn) {
		this.repeatOn = repeatOn;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public int getEndsBy() {
		return endsBy;
	}
	public void setEndsBy(int endsBy) {
		this.endsBy = endsBy;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getEndOnTimes() {
		return endOnTimes;
	}
	public void setEndOnTimes(int endOnTimes) {
		this.endOnTimes = endOnTimes;
	}
}


