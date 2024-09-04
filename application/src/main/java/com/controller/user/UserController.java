package com.controller.user;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.model.common.ParamResult;
import com.bussiness.TransactionManager;
import com.bussiness.Utils;
import com.model.Constants;
import com.model.UserVO;
import com.service.common.FileService;
import com.service.user.UserService;

import jakarta.annotation.Resource;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource(name = "UserService")
	UserService userService;

	@Resource(name = "txManager")
	protected DataSourceTransactionManager txManager;

	@Resource(name="FileService")
	FileService fileService;

	final String FILE_PATH = "/user";

	/**
	 * 내 정보 가져오기
	 * @param id
	 * @return map
	 */
	@GetMapping("/get")
    public @ResponseBody Map<String, Object> get(HttpServletRequest request) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();

		String userId = (String) request.getSession().getAttribute("userId");
    	
    	UserVO item = userService.get(userId);
		if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
			return returnMap;
		}
    	    	
    	returnMap.put("item", item);
		returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
		returnMap.put("message", Constants.RESULT_MSG_SUCCESS);

		return returnMap;
    }

	/**
	 * 회원가입
	 * @param naver_id			네이버 아이디
	 * @param kakao_id			카카오 아이디
	 * @param apple_id			애플 아이디
	 * @param gender			0 남자, 1 여자
	 * @param phone				전화번호
	 * @param fullName			이름
	 * @param nickName			별명
	 * @param address			주소
	 * @param address_detail	상세 주소
	 * @param addPicture		프로필 이미지
	 * @param birthd			생년월일
	 * @return map
	 * @exception Exception
	 */
    @PostMapping("/signup")
    public @ResponseBody Map<String, Object> add(final MultipartHttpServletRequest multiRequest, HttpServletRequest request,
    		@ModelAttribute("UserVO") UserVO userVO) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String [] params = {"fullName", "phone", "gender", "address", "birthd"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}

		// 트랜잭션 설정
		TransactionStatus txStatus = TransactionManager.start(txManager);

		try {
			// 사용자 통장사본 파일 업데이트 확인
			Map<String, Object> resultMapPicture = fileService.add(multiRequest, FILE_PATH, "addPicture");
			if (!resultMapPicture.get("code").equals(Constants.RESULT_CODE_SUCCESS)) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백

				return resultMapPicture;
			}
			userVO.setPicture((String)resultMapPicture.get("fileName"));

			// 사용자 정보 추가
			int result = userService.add(userVO);
			if (result <= 0) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백
			
				returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_INSERT);
				returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_INSERT);
				return returnMap;
			}

			TransactionManager.commit(txStatus);	// 트랜잭션 커밋
			
			returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
			returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
			return returnMap;
		} catch (Exception e) {
			TransactionManager.rollback(txStatus);	// 트랜잭션 롤백

			returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_INSERT);
			returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_INSERT);
			return returnMap;
		}
    }

	/**
	 * 정보 변경
	 * @param id
	 * @param phone				전화번호
	 * @param gender			0 남자, 1 여자
	 * @param full_name			이름
	 * @param nick_name			별명
	 * @param address			주소
	 * @param address_detail	상세 주소
	 * @param picture			프로필 이미지
	 * @param birthd			생년월일
	 * @return map
	 * @exception Exception
	 */
    @PostMapping("/update")
    public @ResponseBody Map<String, Object> update(final MultipartHttpServletRequest multiRequest, HttpServletRequest request,
    		@ModelAttribute("UserVO") UserVO userVO) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String [] params = {"id", "fullName", "phone"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}
		
		// 기존 아이템 정보 가져오기
		UserVO item = userService.get(userVO.getId());
		if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
			return returnMap;
		}
		
		String encryptedPassword = Utils.encodeSHA256(userVO.getPassword());
		
		// 비밀번호가 변경 되었는지 확인
		if (!Utils.isNullOrEmpty(userVO.getPassword()) && !encryptedPassword.equals(item.getPassword())) {
			userVO.setNewPassword(encryptedPassword);
		}

		// 트랜잭션 설정
		TransactionStatus txStatus = TransactionManager.start(txManager);

		try {
			// 정보 수정
			int result = userService.update(userVO);
			if (result <= 0) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백
				
				returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_UPDATE);
				returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_UPDATE);
				return returnMap;
			}

			// 추가 정보 수정
			int resultInfo = userService.updateInfo(userVO);
			if (resultInfo <= 0) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백
				
				returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_UPDATE);
				returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_UPDATE);
				return returnMap;
			}

			if (!(result > 0 || resultInfo > 0)) {	// 수정 실패
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백
				
				returnMap.put("code", Constants.RESULT_CODE_NO_CHANGED);
				returnMap.put("message", Constants.RESULT_MSG_NO_CHANGED);
				return returnMap;
			}

			TransactionManager.commit(txStatus);	// 트랜잭션 커밋

			returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
			returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
			return returnMap;
		} catch (Exception e) {
			TransactionManager.rollback(txStatus);	// 트랜잭션 롤백

			returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_UPDATE);
			returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_UPDATE);
			return returnMap;
		}
    }
	
	/**
	 * 회원탈퇴
	 * @return map
	 */
    @PostMapping("/remove")
    public @ResponseBody Map<String, Object> remove(HttpServletRequest request, @ModelAttribute("UserVO") UserVO userVO) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		String userId = (String) request.getSession().getAttribute("userId");

		// 기존 아이템 정보 가져오기
		UserVO item = userService.get(userId);
		if (item == null) {	// 이미 탈퇴된 경우
			returnMap.put("code", Constants.RESULT_CODE_ALREADY_REMOVED_USER);
			returnMap.put("message", Constants.RESULT_MSG_ALREADY_REMOVED_USER);
			return returnMap;
		}

		// 정보 수정
		int result = userService.remove(userId);
		if (result <= 0) {
			returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_DELETE);
			returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_DELETE);
			return returnMap;
		}
		
		returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
		returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
		return returnMap;
    }
}