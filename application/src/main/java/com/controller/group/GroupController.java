package com.controller.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.model.group.GroupManagerVO;
import com.model.group.GroupMemberVO;
import com.model.group.GroupVO;
import com.model.post.BoardVO;
import com.bussiness.NaverMapManager;
import com.bussiness.TransactionManager;
import com.bussiness.Utils;
import com.model.Constants;
import com.model.common.PaginationInfo;
import com.model.common.ParamResult;
import com.service.common.FileService;
import com.service.group.GroupService;
import com.service.post.BoardService;

import jakarta.annotation.Resource;

@Controller
@RequestMapping(value = "/group")
public class GroupController {

    @Resource(name = "GroupService")
	GroupService groupService;

	@Resource(name = "BoardService")
	BoardService boardService;

	@Resource(name = "txManager")
	protected DataSourceTransactionManager txManager;

	@Resource(name="FileService")
	FileService fileService;

	@Value("${file.upload_max.size}")
	int fileUploadMaxSize;
	@Value("${file.save.abs.path}")
	String fileSaveAbsPath;
	@Value("${file.save.relative.path}")
	String fileSaveRelativePath;

	final String FILE_PATH = "/group";

    /**
	 * 그룹 목록 가져오기
	 * @param search	이름, 카테고리 검색
	 * @param lon		경도 x
	 * @param lat		위도 y
	 * @return map
	 */
    @GetMapping("/list")
	public @ResponseBody Map<String, Object> list(HttpServletRequest request) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 페이지 정보
		PaginationInfo paginationInfo = new PaginationInfo();
		
		// 쿼리 검색 관련 변수 설정
		String [] queryParams = {"allItem", "search"};
		
		Map<String, Object> paramMap = ParamResult.putIfExist(request, queryParams);	// 값이 있는 경우 맵에 넣기

		String page = request.getParameter("page");
		paginationInfo.setCurrentPageNo(page);
		
		paramMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
		paramMap.put("page", paginationInfo.getFirstRecordIndex());
		
		// 목록 가져오기
		List<GroupVO> list = groupService.getList(paramMap);
		int totalCount = groupService.getListTotalCount(paramMap);

        returnMap.put("list", list);
        returnMap.put("totalCount", totalCount);
		returnMap.put("filePath", fileSaveAbsPath + fileSaveRelativePath + FILE_PATH);

		return returnMap;
	}

	/**
	 * 내 그룹 목록 가져오기
	 * @param search	이름, 카테고리 검색
	 * @return map
	 */
    @GetMapping("/listByUserId")
	public @ResponseBody Map<String, Object> listByUserId(HttpServletRequest request) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 페이지 정보
		PaginationInfo paginationInfo = new PaginationInfo();
		
		// 쿼리 검색 관련 변수 설정
		String [] queryParams = {"allItem", "search"};
		
		Map<String, Object> paramMap = ParamResult.putIfExist(request, queryParams);	// 값이 있는 경우 맵에 넣기

		String userId = (String) request.getSession().getAttribute("userId");	// 세션에서 정보 가져오기
		paramMap.put("userId", userId);

		String page = request.getParameter("page");
		paginationInfo.setCurrentPageNo(page);
		
		// 목록 가져오기
		List<GroupVO> list = groupService.getListByUserId(paramMap);
		int totalCount = groupService.getListTotalCountByUserId(paramMap);

        returnMap.put("list", list);
        returnMap.put("totalCount", totalCount);
		returnMap.put("filePath", fileSaveAbsPath + fileSaveRelativePath + FILE_PATH);

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
    	
    	GroupVO item = groupService.get(Utils.parseInt(id, 0));
		if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
			return returnMap;
		}
    	    	
    	returnMap.put("item", item);
		returnMap.put("filePath", fileSaveAbsPath + fileSaveRelativePath + FILE_PATH);
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
    public @ResponseBody Map<String, Object> add(final MultipartHttpServletRequest multiRequest,
			HttpServletRequest request, @ModelAttribute("GroupVO") GroupVO groupVO) throws Exception {

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
			if (Utils.isNullOrEmpty(groupVO.getAddress())) {	// 주소지 정보가 있는 경우
				// 주소 정보를 gps로 변환
				Map<String, Object> resultMapSearchAddress = NaverMapManager.searchAddress(groupVO.getAddress());
				if (resultMapSearchAddress != null) { // 주소가 없는 경우는 별도 처리 없음.
					String lon = (String)resultMapSearchAddress.get("x");
					String lat = (String)resultMapSearchAddress.get("y");
					groupVO.setLat(lat);
					groupVO.setLon(lon);
				}
			} else {
				// 주소지 정보가 없는 경우, '주소 없음'으로 추가. 좌표 정보 사용자가 직접 추가 23-11-21 by flash
				groupVO.setAddress("주소 없음");
			}

			// 대표사진 추가
			Map<String, Object> resultMapRepresentImage = fileService.add(multiRequest, FILE_PATH, "addRepresentImage");
			if (!resultMapRepresentImage.get("code").equals(Constants.RESULT_CODE_SUCCESS)) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백

				return resultMapRepresentImage;
			}
			groupVO.setRepresentImage((String)resultMapRepresentImage.get("fileName"));

			// 배경사진 추가
			Map<String, Object> resultMapBackgroundImage = fileService.add(multiRequest, FILE_PATH, "addBackgroundmage");
			if (!resultMapBackgroundImage.get("code").equals(Constants.RESULT_CODE_SUCCESS)) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백

				return resultMapBackgroundImage;
			}
			groupVO.setBackgroundmage((String)resultMapBackgroundImage.get("fileName"));

			// 그룹 정보 추가
			int result = groupService.add(groupVO);
			if (result <= 0) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백
			
				returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_INSERT);
				returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_INSERT);
				return returnMap;
			}

			String userId = (String) request.getSession().getAttribute("userId");	// 세션에서 정보 가져오기

			// 추가할 그룹 멤버 정보 구성
			GroupMemberVO addGroupMemberVO = new GroupMemberVO();
			addGroupMemberVO.setGroupId(groupVO.getId());
			addGroupMemberVO.setUserId(userId);

			int resultAddMember = groupService.addMember(addGroupMemberVO);	// 멤버 정보 추가
			if (resultAddMember <= 0) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백
			
				returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_INSERT);
				returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_INSERT);
				return returnMap;
			}

			// 추가할 그룹 매니져 정보 구성
			GroupManagerVO addGroupManagerVO = new GroupManagerVO();
			addGroupManagerVO.setGroupId(groupVO.getId());
			addGroupManagerVO.setGroupMemberId(addGroupMemberVO.getId());
			addGroupManagerVO.setType(GroupManagerVO.TYPE_MASTER);	// 그룹장

			int resultAddManager = groupService.addManager(addGroupManagerVO);	// 그룹 매니져 정보 추가
			if (resultAddManager <= 0) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백
			
				returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_INSERT);
				returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_INSERT);
				return returnMap;
			}

			// 전체 게시판 추가
			BoardVO addBoardVO = new BoardVO();
			addBoardVO.setGroupId(groupVO.getId());

			int resultAddBoard = boardService.add(addBoardVO);	// 게시판 추가
			if (resultAddBoard <= 0) {
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
    		@ModelAttribute("GroupVO") GroupVO groupVO) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String [] params = {"id", "name", "phone", "address"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}

		String userId = (String) request.getSession().getAttribute("userId");	// 세션에서 정보 가져오기

		boolean bAuth = groupService.getAuth(groupVO.getId(), Utils.parseInt(userId, 0));	// 그룹장 권한 체크
		if (!bAuth) {
			returnMap.put("code", Constants.RESULT_CODE_NO_AUTH);
			returnMap.put("message", Constants.RESULT_MSG_NO_AUTH);
			return returnMap;
		}

		// 기존 아이템 정보 가져오기
		GroupVO item = groupService.get(groupVO.getId());
		if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
			return returnMap;
		}

		// 트랜잭션 설정
		TransactionStatus txStatus = TransactionManager.start(txManager);

		try {
			// 주소지 정보로 좌표 정보가져오기
			if (Utils.isNullOrEmpty(groupVO.getAddress())) {	// 주소지 정보가 있는 경우
				// 주소 정보를 gps로 변환
				Map<String, Object> resultMapSearchAddress = NaverMapManager.searchAddress(groupVO.getAddress());
				if (resultMapSearchAddress != null) { // 주소가 없는 경우는 별도 처리 없음.
					String lon = (String)resultMapSearchAddress.get("x");
					String lat = (String)resultMapSearchAddress.get("y");
					groupVO.setLat(lat);
					groupVO.setLon(lon);
				}
			} else {
				// 주소지 정보가 없는 경우, '주소 없음'으로 추가. 좌표 정보 사용자가 직접 추가 23-11-21 by flash
				groupVO.setAddress("주소 없음");
			}

			// 대표사진 추가
			Map<String, Object> resultMapRepresentImage = fileService.update(multiRequest, FILE_PATH, item.getRepresentImage(), "addRepresentImage", groupVO.isRemoveRepresentImage());
			if (!resultMapRepresentImage.get("code").equals(Constants.RESULT_CODE_SUCCESS)) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백

				return resultMapRepresentImage;
			}
			groupVO.setRepresentImage((String)resultMapRepresentImage.get("fileName"));

			// 배경사진 추가
			Map<String, Object> resultMapBackgroundImage = fileService.update(multiRequest, FILE_PATH, item.getBackgroundmage(), "addBackgroundmage", groupVO.isRemoveBackgroundmage());
			if (!resultMapBackgroundImage.get("code").equals(Constants.RESULT_CODE_SUCCESS)) {
				TransactionManager.rollback(txStatus);	// 트랜잭션 롤백

				return resultMapBackgroundImage;
			}
			groupVO.setBackgroundmage((String)resultMapBackgroundImage.get("fileName"));

			// 정보 수정
			int result = groupService.update(groupVO);
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
	 * 그룹 삭제
	 * @param id
	 * @return map
	 */
    @PostMapping("/remove")
    public @ResponseBody Map<String, Object> remove(HttpServletRequest request,
		@ModelAttribute("GroupVO") GroupVO groupVO) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String [] params = {"id"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}

		String userId = (String) request.getSession().getAttribute("userId");	// 세션에서 정보 가져오기

		boolean bAuth = groupService.getAuth(groupVO.getId(), Utils.parseInt(userId, 0));	// 그룹장 권한 체크
		if (!bAuth) {
			returnMap.put("code", Constants.RESULT_CODE_NO_AUTH);
			returnMap.put("message", Constants.RESULT_MSG_NO_AUTH);
			return returnMap;
		}

		// 기존 아이템 정보 가져오기
		GroupVO item = groupService.get(groupVO.getId());
		if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
			return returnMap;
		}

		// 삭제
		int result = groupService.remove(groupVO.getId());
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
