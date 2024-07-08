package com.model.common;

public class NotificationReadVO {
	private int id;
	private int userId;
	private String readd;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getReadd() {
		return this.readd;
	}

	public void setReadd(String readd) {
		this.readd = readd;
	}
}