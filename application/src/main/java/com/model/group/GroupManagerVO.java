package com.model.group;

// 그룹 멤버
public class GroupManagerVO {

	public static final String TYPE_MASTER = "0";	// 그룹장
	public static final String TYPE_MANAGER = "1";	// 운영진

	private int id;
	private int groupId;
	private int groupMemberId;
	private String type;		// 0 그룹장, 1 운영진
	private String phone;		// 전화번호
	private String fullName;	// 이름
	private String nickName;	// 닉네임
	private String picture;		// 프로필
	private String gender;		// 성별
	private String birthd;		// 생년월일
	private String updateYn;	// 게시물 수정 가능 여부
	private String removeYn;	// 게시물 삭제 가능 여부
	private String created;

	public String getUpdateYn() {
		return this.updateYn;
	}

	public void setUpdateYn(String updateYn) {
		this.updateYn = updateYn;
	}

	public String getRemoveYn() {
		return this.removeYn;
	}

	public void setRemoveYn(String removeYn) {
		this.removeYn = removeYn;
	}

	public int getGroupMemberId() {
		return this.groupMemberId;
	}

	public void setGroupMemberId(int groupMemberId) {
		this.groupMemberId = groupMemberId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthd() {
		return this.birthd;
	}

	public void setBirthd(String birthd) {
		this.birthd = birthd;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

}