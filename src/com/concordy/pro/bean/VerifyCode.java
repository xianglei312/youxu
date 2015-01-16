package com.concordy.pro.bean;

import java.io.Serializable;

public class VerifyCode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean verifyResult;
	private String phone;
	private String sentCode;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSendCode() {
		return sentCode;
	}
	public void setSendCode(String sendCode) {
		this.sentCode = sendCode;
	}
	public boolean isVerifyResult() {
		return verifyResult;
	}
	public void setVerifyResult(boolean verifyResult) {
		this.verifyResult = verifyResult;
	}
}
