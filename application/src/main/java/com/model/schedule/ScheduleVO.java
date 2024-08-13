package com.model.schedule;

// 일정
public class ScheduleVO {
	
	private int id;
	private int groupId;		// 그룹
	private int category;		// 카테고리 아이디. 없으면 전체
	private String status;		// 0 모집중, 1 마감(조기 마감이 필요한 경우 사용)
	private String name;		// 이름
	private String contents;	// 내용
	private String startd;		// 시작 시간
	private String endd;		// 종료 시간
	private String repeatYn;	// 반복 여부
	private String address;
	private String address_detail;
	private int count;			// 최대 인원수. 기본 그룹 멤버 수
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

	public int getCategory() {
		return this.category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getStartd() {
		return this.startd;
	}

	public void setStartd(String startd) {
		this.startd = startd;
	}

	public String getEndd() {
		return this.endd;
	}

	public void setEndd(String endd) {
		this.endd = endd;
	}

	public String getRepeatYn() {
		return this.repeatYn;
	}

	public void setRepeatYn(String repeatYn) {
		this.repeatYn = repeatYn;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress_detail() {
		return this.address_detail;
	}

	public void setAddress_detail(String address_detail) {
		this.address_detail = address_detail;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

}
