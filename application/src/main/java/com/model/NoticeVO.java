package com.model;

// 공지사항(faq)
public class NoticeVO {
	
	private int id;
	private int userId;
	private String title;
	private String contents;
	private int view;
	private String created;
	private String modifyd;

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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getView() {
		return this.view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModifyd() {
		return this.modifyd;
	}

	public void setModifyd(String modifyd) {
		this.modifyd = modifyd;
	}
}
