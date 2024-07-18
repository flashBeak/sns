package com.model.group;

// 그룹 공지
public class GroupNoticeVO {
	
	private int id;
	private int groupId;
	private int userId;
	private String title;		// 제목
	private String contents;	// 내용
	private int view;			// 조회수
	private String modifyd;
	private String created;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
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

	public String getModifyd() {
		return this.modifyd;
	}

	public void setModifyd(String modifyd) {
		this.modifyd = modifyd;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}
