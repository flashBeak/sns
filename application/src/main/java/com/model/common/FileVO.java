package com.model.common;

public class FileVO {

	public static final int TYPE_POST = 0;	// 게시판
	public static final int TYPE_QNA = 1;	// Q&A
	
	public String id;
	
	public int type;	// 0 게시판, 1 qna
	public String postId;	// 사용처의 아이디
	public String name;
	public String groupId;
	public String groupNoticeId;
	public String noticeId;
	public String created;

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupNoticeId() {
		return this.groupNoticeId;
	}

	public void setGroupNoticeId(String groupNoticeId) {
		this.groupNoticeId = groupNoticeId;
	}

	public String getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
