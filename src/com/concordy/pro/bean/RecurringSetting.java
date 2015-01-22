package com.concordy.pro.bean;

import java.io.Serializable;

public class RecurringSetting implements Serializable{
	private static final long serialVersionUID = 1L;
	private int RepeatBy;
	public int Interval;
	public int RepeatOn;
	public String StartDate;
	public int EndsBy;
	//end on a particular date
	public String EndDate;
	//end on N instance of recurrence.
	public int EndOnTimes;
	public int getRepeatBy() {
		return RepeatBy;
	}
	public void setRepeatBy(int repeatBy) {
		RepeatBy = repeatBy;
	}
	public int getInterval() {
		return Interval;
	}
	public void setInterval(int interval) {
		Interval = interval;
	}
	public int getRepeatOn() {
		return RepeatOn;
	}
	public void setRepeatOn(int repeatOn) {
		RepeatOn = repeatOn;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public int getEndsBy() {
		return EndsBy;
	}
	public void setEndsBy(int endsBy) {
		EndsBy = endsBy;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public int getEndOnTimes() {
		return EndOnTimes;
	}
	public void setEndOnTimes(int endOnTimes) {
		EndOnTimes = endOnTimes;
	}
	public RecurringSetting(int repeatBy, int interval, int repeatOn,
			String startDate, int endsBy, String endDate, int endOnTimes) {
		super();
		RepeatBy = repeatBy;
		Interval = interval;
		RepeatOn = repeatOn;
		StartDate = startDate;
		EndsBy = endsBy;
		EndDate = endDate;
		EndOnTimes = endOnTimes;
	}


	public RecurringSetting(){}
	@Override
	public String toString() {
		return "RecurringSetting [RepeatBy=" + RepeatBy + ", Interval=" + Interval
				+ ", RepeatOn=" + RepeatOn + ", StartDate=" + StartDate
				+ ", EndsBy=" + EndsBy + ", EndDate=" + EndDate + ", EndOnTimes="
				+ EndOnTimes + "]";
	}  


}


