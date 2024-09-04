package com.model.post;

public class PostVO {

	private int id;
	private int groupMemberId;
	private int groupId;
	private int boardId;
	private String title;		// 제목
	private String contents;	// 내용
	private String viewType;	// 게시물 표시 유형. 0 일반 조회, 1 카테고리 소속만 조회
	private int view;			// 조회수
	private int order;			// 정렬 순서. 추후 개발 예정
	private String created;
	private String modifyd;
	
	public String getViewType() {
		return this.viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupMemberId() {
		return this.groupMemberId;
	}

	public void setGroupMemberId(int groupMemberId) {
		this.groupMemberId = groupMemberId;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getBoardId() {
		return this.boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
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

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
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