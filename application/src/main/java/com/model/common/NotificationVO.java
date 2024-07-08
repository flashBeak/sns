package com.model.common;

import java.util.List;

public class NotificationVO {
	public static final int TYPE_NOTICE = 0;	// 공지사항

	public static final int SUB_STRING_LENGTH = 50;	// 알림 줄임 글자수

	// 삭제 기준일, 현 시점 기준 removeDay만큼 이전. ex) 현재 날짜 2023-08-21 -> 2023-08-06 이전 데이터 삭제
	public static final int REMOVE_DAY = 15;

	private int id;
	private int type;		// 0 시스템 공지사항, 1 계약서
	private int userId;
	private int postId;
	private String fullName;
	private String title;
	private String contents;
	private String created;
	private List<FileVO> fileList;

	public List<FileVO> getFileList() {
		return this.fileList;
	}

	public void setFileList(List<FileVO> fileList) {
		this.fileList = fileList;
	}

	public int getPostId() {
		return this.postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}