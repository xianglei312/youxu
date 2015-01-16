package com.concordy.pro.bean;

public class HttpError {
	private  String errorCode;
	private  String errorMsg;
	private CustomException exception; 
	public  String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String error_code) {
		this.errorCode = error_code;
	}
	public  String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String error_msg) {
		this.errorMsg = error_msg;
	}
	public CustomException getException() {
		return exception;
	}
	public void setException(CustomException exception) {
		this.exception = exception;
	}
	public class CustomException{
		private String message;
		private String exceptionMessage;
		private String exceptionType;
		private String stackTrace;
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getExceptionMessage() {
			return exceptionMessage;
		}
		public void setExceptionMessage(String exceptionMessage) {
			this.exceptionMessage = exceptionMessage;
		}
		public String getExceptionType() {
			return exceptionType;
		}
		public void setExceptionType(String exceptionType) {
			this.exceptionType = exceptionType;
		}
		public String getStackTrace() {
			return stackTrace;
		}
		public void setStackTrace(String stackTrace) {
			this.stackTrace = stackTrace;
		}
	}
}
