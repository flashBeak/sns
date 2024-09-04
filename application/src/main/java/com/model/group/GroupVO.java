package com.model.group;

// 그룹
public class GroupVO {

	private int id;
	private String name;			// 이름
	private String address;			// 지번, 도로명 주소
	private String addressDetail;	// 상세주소
	private String introduce;		// 소개
	private String etc;				// 기타
	private String lon;				// 경도 x
	private String lat;				// 위도 y
	private String phone;			// 전화번호
	private String representImage;	// 대표사진
	private String approveYn;		// 승인여부
	private boolean removeRepresentImage;
	private String backgroundmage;	// 배경사진
	private boolean removeBackgroundmage;
	
	public String getApproveYn() {
		return this.approveYn;
	}

	public void setApproveYn(String approveYn) {
		this.approveYn = approveYn;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressDetail() {
		return this.addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getIntroduce() {
		return this.introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getEtc() {
		return this.etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getLon() {
		return this.lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return this.lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRepresentImage() {
		return this.representImage;
	}

	public void setRepresentImage(String representImage) {
		this.representImage = representImage;
	}

	public boolean isRemoveRepresentImage() {
		return this.removeRepresentImage;
	}

	public void setRemoveRepresentImage(boolean removeRepresentImage) {
		this.removeRepresentImage = removeRepresentImage;
	}

	public String getBackgroundmage() {
		return this.backgroundmage;
	}

	public void setBackgroundmage(String backgroundmage) {
		this.backgroundmage = backgroundmage;
	}

	public boolean isRemoveBackgroundmage() {
		return this.removeBackgroundmage;
	}

	public void setRemoveBackgroundmage(boolean removeBackgroundmage) {
		this.removeBackgroundmage = removeBackgroundmage;
	}

}