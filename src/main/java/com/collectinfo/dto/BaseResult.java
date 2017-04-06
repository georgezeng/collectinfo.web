package com.collectinfo.dto;

import com.collectinfo.constant.SystemConstant;

public class BaseResult<T> {
	public static class ErrorInfo {
		private String id;
		private String msg;

		ErrorInfo() {
		}

		public ErrorInfo(String id, String msg) {
			this.id = id;
			this.msg = msg;
		}

		public String getId() {
			return id;
		}

		public String getMsg() {
			return msg;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	private String status;
	private T data;
	private ErrorInfo error;

	public BaseResult() {
		status = SystemConstant.JSON_STATUS_SUCCESS;
	}

	public BaseResult(T data) {
		status = SystemConstant.JSON_STATUS_SUCCESS;
		this.data = data;
	}

	public BaseResult(String errorId, String errorMsg) {
		this.status = SystemConstant.JSON_STATUS_FAILED;
		this.error = new ErrorInfo(errorId, errorMsg);
	}

	public T getData() {
		return data;
	}

	public String getStatus() {
		return status;
	}

	public ErrorInfo getError() {
		return error;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setError(ErrorInfo error) {
		this.error = error;
	}

}