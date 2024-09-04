package com.model;

public class Constants {
	public static final int RESULT_CODE_NOMAL_ERROR = 0;
	public static final int RESULT_CODE_SUCCESS = 1;
	public static final int RESULT_CODE_NEED_REQUIRE_PARAM = 2;
	public static final int RESULT_CODE_NO_AUTH = 3;
	public static final int RESULT_CODE_NO_ITEM = 4;
	public static final int RESULT_CODE_FAIL_TO_INSERT = 5;
	public static final int RESULT_CODE_FAIL_TO_UPDATE = 6;
	public static final int RESULT_CODE_FAIL_TO_DELETE = 7;
	public static final int RESULT_CODE_FAIL_TO_DELETE_FILE = 8;
	public static final int RESULT_CODE_FAIL_TO_FILE_UPLOAD = 9;
	public static final int RESULT_CODE_NO_CHANGED = 10;
	public static final int RESULT_CODE_SYSTEM_ERROR = 11;
	public static final int RESULT_CODE_ALREADY_REMOVED_USER = 12;

	public static final String RESULT_MSG_SUCCESS = "성공";
	public static final String RESULT_MSG_NEED_REQUIRE_PARAM = "필수인자 없음";
	public static final String RESULT_MSG_NO_AUTH = "권한 없음";
	public static final String RESULT_MSG_NO_ITEM = "정보 없음";
	public static final String RESULT_MSG_FAIL_TO_INSERT = "추가 실패";
	public static final String RESULT_MSG_FAIL_TO_UPDATE = "수정 실패";
	public static final String RESULT_MSG_FAIL_TO_DELETE = "삭제 실패";
	public static final String RESULT_MSG_FAIL_TO_DELETE_FILE = "파일 삭제 실패";
	public static final String RESULT_MSG_FAIL_TO_FILE_UPLOAD = "파일 업로드 실패";
	public static final String RESULT_MSG_NO_CHANGED = "변경 사항이 없습니다.";
	public static final String RESULT_MSG_SYSTEM_ERROR = "시스템 오류입니다. 관리자에게 문의해 주세요";
	public static final String RESULT_MSG_ALREADY_REMOVED_USER = "이미 탈퇴되었습니다.";
}
