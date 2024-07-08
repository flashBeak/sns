package com.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.service.auth.AdminAuthService;
import com.service.common.FileService;
import com.service.user.AdminUserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin/user")
public class AdminUserController {

    @Resource(name = "AdminUserService")
	AdminUserService userService;

	@Resource(name = "AdminAuthService")
	AdminAuthService authService;

	@Resource(name = "txManager")
	protected DataSourceTransactionManager txManager;

	@Resource(name="FileService")
	FileService fileService;

	final String FILE_PATH = "/user";

    /**
	 * 사용자 목록 가져오기
	 * @return map
	 */
    @GetMapping("/list")
	public @ResponseBody Map<String, Object> list(HttpServletRequest request) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 목록 가져오기
		List<UserVO> list = userService.getList();
		int totalCount = userService.getListTotalCount();

        returnMap.put("list", list);
        returnMap.put("totalCount", totalCount);

		return returnMap;
	}

    /**
	 * 정보 가져오기
	 * @param id
	 * @return map
	 */
	@GetMapping("/get")
    public @ResponseBody Map<String, Object> get(String id) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();
    	
    	UserVO item = userService.get(id);
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
	 *  추가
	 * @param phone				전화번호
	 * @param naver_id			네이버 아이디
	 * @param kakao_id			카카오 아이디
	 * @param apple_id			애플 아이디
	 * @param password			비밀번호
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
    @PostMapping("/add")
    public @ResponseBody Map<String, Object> add(final MultipartHttpServletRequest multiRequest, HttpServletRequest request,
    		@ModelAttribute("UserVO") UserVO userVO) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();

		if (!authService.isAdmin(request)) {	// 시스템 관리자가 아니면
    		returnMap.put("code", Constants.RESULT_CODE_NO_AUTH);
    		returnMap.put("message", Constants.RESULT_MSG_NO_AUTH);
        	return returnMap;
		}
		
		String [] params = {"fullName", "phone", "gender", "address", "birthd", "password", "role"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}

		// 비밀번호 암호화
		String encryptedPassword = Utils.encodeSHA256(userVO.getPassword());
		userVO.setPassword(encryptedPassword);
		
		// 트랜잭션 설정
		TransactionStatus txStatus = TransactionManager.start(txManager);

		try {
			// 사용자 통장사본 파일 업데이트 확인
			Map<String, Object> resultMapPicture = fileService.add(multiRequest, FILE_PATH, "picture");
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

		if (!authService.isAdmin(request)) {	// 시스템 관리자가 아니면
    		returnMap.put("code", Constants.RESULT_CODE_NO_AUTH);
    		returnMap.put("message", Constants.RESULT_MSG_NO_AUTH);
        	return returnMap;
		}
		
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
	 * 사용자 삭제
	 * @param id
	 * @return map
	 */
    @PostMapping("/remove")
    public @ResponseBody Map<String, Object> remove(HttpServletRequest request,
		@ModelAttribute("UserVO") UserVO userVO) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		if (!authService.isAdmin(request)) {	// 권한 확인
			returnMap.put("code", Constants.RESULT_CODE_NO_AUTH);
			returnMap.put("message", Constants.RESULT_MSG_NO_AUTH);
			return returnMap;
		}
		
		String [] params = {"id"};
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

		// 정보 수정
		int result = userService.remove(userVO.getId());
		if (result > 0) {
			returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
			returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
		} else {
			returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_DELETE);
			returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_DELETE);
		}

		return returnMap;
    }
}
