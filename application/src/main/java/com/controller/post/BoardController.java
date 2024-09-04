package com.controller.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.model.post.BoardVO;
import com.bussiness.Utils;
import com.model.Constants;
import com.model.common.PaginationInfo;
import com.model.common.ParamResult;
import com.service.common.FileService;
import com.service.group.GroupService;
import com.service.post.BoardService;

import jakarta.annotation.Resource;

@Controller
@RequestMapping(value = "/board")
public class BoardController {

    @Resource(name = "BoardService")
	BoardService boardService;

	@Resource(name = "GroupService")
	GroupService groupService;

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

    /**
	 * 게시판 목록 가져오기
	 * @param search		검색어
	 * @param categoryId
	 * @param groupId
	 * @return map
	 */
    @GetMapping("/list")
	public @ResponseBody Map<String, Object> list(HttpServletRequest request) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		String [] params = {"groupId"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}

		// 페이지 정보
		PaginationInfo paginationInfo = new PaginationInfo();
		
		// 쿼리 검색 관련 변수 설정
		String [] queryParams = {"allItem", "search", "groupId", "categoryId"};
		
		Map<String, Object> paramMap = ParamResult.putIfExist(request, queryParams);	// 값이 있는 경우 맵에 넣기

		String page = request.getParameter("page");
		paginationInfo.setCurrentPageNo(page);
		
		paramMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
		paramMap.put("page", paginationInfo.getFirstRecordIndex());
		
		// 목록 가져오기
		List<BoardVO> list = boardService.getList(paramMap);
		int totalCount = boardService.getListTotalCount(paramMap);

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
    public @ResponseBody Map<String, Object> get(HttpServletRequest request, @ModelAttribute("BoardVO") BoardVO boardVO) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();
    	
    	BoardVO item = boardService.get(boardVO.getId());
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
	 * @param groupId
	 * @param type				0 전체 게시판, 1 게시판 게시판
	 * @param categoryId		게시판 게시판일때만 사용
	 * @return map
	 * @exception Exception
	 */
    @PostMapping("/add")
    public @ResponseBody Map<String, Object> add(HttpServletRequest request, @ModelAttribute("BoardVO") BoardVO boardVO) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();

		String [] params = {"groupId", "type"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}

		String userId = (String) request.getSession().getAttribute("userId");	// 세션에서 정보 가져오기

		boolean bAuth = groupService.getAuth(boardVO.getGroupId(), Utils.parseInt(userId, 0));	// 그룹장 권한 체크
		if (!bAuth) {
			returnMap.put("code", Constants.RESULT_CODE_NO_AUTH);
			returnMap.put("message", Constants.RESULT_MSG_NO_AUTH);
			return returnMap;
		}

		// 카테고리 설정이 있는 경우, 중복체크. 동일한 카테고리에 중복된 게시판 존재 불가
	
		// 게시판 정보 추가
		int result = boardService.add(boardVO);
		if (result <= 0) {
			returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_INSERT);
			returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_INSERT);
			return returnMap;
		}
		
		returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
		returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
		return returnMap;
    }

	/**
	 * 정보 변경
	 * @param id
	 * @param type				0 전체 게시판, 1 게시판 게시판
	 * @param categoryId		게시판 게시판일때만 사용
	 * @return map
	 * @exception Exception
	 */
    @PostMapping("/update")
    public @ResponseBody Map<String, Object> update(HttpServletRequest request, @ModelAttribute("BoardVO") BoardVO boardVO) throws Exception {

    	Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String [] params = {"id", "type"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}

		// 기존 아이템 정보 가져오기
		BoardVO item = boardService.get(boardVO.getId());
		if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
			return returnMap;
		}

		String userId = (String) request.getSession().getAttribute("userId");	// 세션에서 정보 가져오기

		boolean bAuth = groupService.getAuth(item.getGroupId(), Utils.parseInt(userId, 0));	// 그룹장 권한 체크
		if (!bAuth) {
			returnMap.put("code", Constants.RESULT_CODE_NO_AUTH);
			returnMap.put("message", Constants.RESULT_MSG_NO_AUTH);
			return returnMap;
		}

		// 카테고리 설정이 있는 경우, 중복체크. 동일한 카테고리에 중복된 게시판 존재 불가

		// 정보 수정
		int result = boardService.update(boardVO);
		if (result <= 0) {				
			returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_UPDATE);
			returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_UPDATE);
			return returnMap;
		}

		returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
		returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
		return returnMap;
    }
	
	/**
	 * 게시판 삭제
	 * @param id
	 * @return map
	 */
    @PostMapping("/remove")
    public @ResponseBody Map<String, Object> remove(HttpServletRequest request, @ModelAttribute("BoardVO") BoardVO boardVO) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String [] params = {"id"};
		ParamResult paramResult = ParamResult.valid(request, params);	// 필수인자 확인
		if (paramResult.code != Constants.RESULT_CODE_SUCCESS) {
			returnMap.put("code", paramResult.code);
			returnMap.put("message", paramResult.message);
			return returnMap;
		}

		// 기존 아이템 정보 가져오기
		BoardVO item = boardService.get(boardVO.getId());
		if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
			return returnMap;
		}

		String userId = (String) request.getSession().getAttribute("userId");	// 세션에서 정보 가져오기

		boolean bAuth = groupService.getAuth(item.getGroupId(), Utils.parseInt(userId, 0));	// 그룹장 권한 체크
		if (!bAuth) {
			returnMap.put("code", Constants.RESULT_CODE_NO_AUTH);
			returnMap.put("message", Constants.RESULT_MSG_NO_AUTH);
			return returnMap;
		}

		// 삭제
		int result = boardService.remove(boardVO.getId());
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
