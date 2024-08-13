package com.model;

public class UserVO {

	public static int USER_ROLE_ADMIN = 9;

    private String id;
    private String username;
    private String password;
    private String newPassword;
    private String role;
    private String status;	// 0 승인 대기, 1 승인
    private String removed;	// 0 탈퇴 안함, 1 탈퇴함
    private String lastLogin;
    private String created;
	private String phone;
	private String fullName;
	private String nickName;
	private String picture;
	private String gender;			// 0 남자, 1 여자
	private String birthd;			// 생년 월일 ex) 1990-01-01
	private boolean removePicture;	// 수정 시, 이미지 삭제 여부
	private boolean fcmNotice;		// 공지 알림 수신 여부
	private String naverId;
	private String kakaoId;
	private String appleId;
	private String agreePrivacy3party;	// 개인정보 제3자 제공 동의 여부 0, 1
	private String agreeMarketing;	// 마케팅 정보 메일, SMS 수신동의 여부 0, 1
	private String address;			// 주소
	private String addressDetail;	// 상세주소

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

	public boolean isRemovePicture() {
		return this.removePicture;
	}

	public void setRemovePicture(boolean removePicture) {
		this.removePicture = removePicture;
	}

	public boolean isFcmNotice() {
		return this.fcmNotice;
	}

	public void setFcmNotice(boolean fcmNotice) {
		this.fcmNotice = fcmNotice;
	}

	public String getNaverId() {
		return this.naverId;
	}

	public void setNaverId(String naverId) {
		this.naverId = naverId;
	}

	public String getKakaoId() {
		return this.kakaoId;
	}

	public void setKakaoId(String kakaoId) {
		this.kakaoId = kakaoId;
	}

	public String getAppleId() {
		return this.appleId;
	}

	public void setAppleId(String appleId) {
		this.appleId = appleId;
	}

	public String getAgreePrivacy3party() {
		return this.agreePrivacy3party;
	}

	public void setAgreePrivacy3party(String agreePrivacy3party) {
		this.agreePrivacy3party = agreePrivacy3party;
	}

	public String getAgreeMarketing() {
		return this.agreeMarketing;
	}

	public void setAgreeMarketing(String agreeMarketing) {
		this.agreeMarketing = agreeMarketing;
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

	public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemoved() {
        return this.removed;
    }

    public void setRemoved(String removed) {
        this.removed = removed;
    }

    public String getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
