package com.model.common;

public class FileVO {

	public static final int TYPE_POST = 0;	// 게시판
	public static final int TYPE_QNA = 1;	// Q&A
	
	public String id;
	
	public int type;	// 0 게시판, 1 qna
	public String postId;	// 사용처의 아이디
	public String showName;
	public String name;
	public int order;	// 표시 순서

	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
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
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
