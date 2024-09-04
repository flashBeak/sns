package com.controller.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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

import com.service.schedule.ScheduleService;
import com.bussiness.TransactionManager;
import com.bussiness.Utils;
import com.model.Constants;
import com.model.common.ParamResult;
import com.model.schedule.ScheduleVO;
import com.service.common.FileService;

@Controller
@RequestMapping(value = "/schedule")
public class ScheduleController {
	
	@Resource(name = "ScheduleService")
	ScheduleService scheduleService;

	@Resource(name = "txManager")
	protected DataSourceTransactionManager txManager;

	@Resource(name="FileService")
	FileService fileService;

	final String FILE_PATH = "/schedule";

    /**
	 * 일정 목록 가져오기
	 * @param search	이름, 카테고리 검색
	 * @param lon		경도 x
	 * @param lat		위도 y
	 * @return map
	 */
    @GetMapping("/list")
	public @ResponseBody Map<String, Object> list(HttpServletRequest request) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		// 목록 가져오기
		List<ScheduleVO> list = scheduleService.getList();
		int totalCount = scheduleService.getListTotalCount();

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
    	
    	ScheduleVO item = scheduleService.get(Utils.parseInt(id, 0));
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
	 * @param name				이름
	 * @param address			주소
	 * @param addressDetail		상세주소
	 * @param introduce			소개
	 * @param etc				기타
	 * @param phone				전화번호
	 * @param addRepresentImage	대표사진
	 * @param addBackgroundmage	배경사진
	 * @param approveYn			승인여부
	 * @return map
	 * @exception Exception
	 */
    @PostMapping("/add")
    public @ResponseBody Map<String, Object> add(final MultipartHttpServletRequest multiRequest, HttpServletRequest request,
    		@ModelAttribute("ScheduleVO") ScheduleVO scheduleVO) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();

		String [] params = {"name", "phone", "address", "introduce", "approveYn"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}
		
		// 트랜잭션 설정
		TransactionStatus txStatus = TransactionManager.start(txManager);

		try {
			// 주소지 정보로 좌표 정보 가져오기
			// 좌표 정보 설정

			// 일정 정보 추가
			int result = scheduleService.add(scheduleVO);
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
	 * @param name					이름
	 * @param address				주소
	 * @param addressDetail			상세주소
	 * @param introduce				소개
	 * @param etc					기타
	 * @param phone					전화번호
	 * @param addRepresentImage		대표사진
	 * @param removeRepresentImage
	 * @param addBackgroundmage		배경사진
	 * @param removeBackgroundImage
	 * @param approveYn				승인여부
	 * @return map
	 * @exception Exception
	 */
    @PostMapping("/update")
    public @ResponseBody Map<String, Object> update(final MultipartHttpServletRequest multiRequest, HttpServletRequest request,
    		@ModelAttribute("ScheduleVO") ScheduleVO scheduleVO) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String [] params = {"id", "name", "phone", "address"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}
		
		// 기존 아이템 정보 가져오기
		ScheduleVO item = scheduleService.get(scheduleVO.getId());
		if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
			return returnMap;
		}

		// 트랜잭션 설정
		TransactionStatus txStatus = TransactionManager.start(txManager);

		try {
			// 정보 수정
			int result = scheduleService.update(scheduleVO);
			if (result <= 0) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백
				
				returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_UPDATE);
				returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_UPDATE);
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
	 * 일정 삭제
	 * @param id
	 * @return map
	 */
    @PostMapping("/remove")
    public @ResponseBody Map<String, Object> remove(HttpServletRequest request,
		@ModelAttribute("ScheduleVO") ScheduleVO scheduleVO) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String [] params = {"id"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}

		// 기존 아이템 정보 가져오기
		ScheduleVO item = scheduleService.get(scheduleVO.getId());
		if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
			return returnMap;
		}

		// 삭제
		int result = scheduleService.remove(scheduleVO.getId());
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
