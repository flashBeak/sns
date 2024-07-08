package com.model.common;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.bussiness.Utils;
import com.model.Constants;


public class ParamResult {
	public int code = Constants.RESULT_CODE_SUCCESS;
	public String message = "success";

	public ParamResult() {
	}

	// 결과 메세지 수정 by flash 24-03-04
	// 필수 전달인자가 넘어왔는지 확인.
	public static ParamResult valid(HttpServletRequest request, String [] param) {
		ParamResult result = new ParamResult();
		if (param == null) {
			result.code = Constants.RESULT_CODE_NEED_REQUIRE_PARAM;
			result.message = Constants.RESULT_MSG_NEED_REQUIRE_PARAM;
			return result;
		}
		
		if (param.length == 0) {
			result.code = Constants.RESULT_CODE_NEED_REQUIRE_PARAM;
			result.message = Constants.RESULT_MSG_NEED_REQUIRE_PARAM;
			return result;
		}
		
		for (int i = 0; i < param.length; i++) {
			String item = request.getParameter(param[i]);
			if (Utils.isNullOrEmpty(item)) {
				result.code = Constants.RESULT_CODE_NEED_REQUIRE_PARAM;
				result.message = Constants.RESULT_MSG_NEED_REQUIRE_PARAM;
				// result.message = param[i] + " is null";
				return result;
			}
		}

		result.code = Constants.RESULT_CODE_SUCCESS;
		result.message = Constants.RESULT_MSG_SUCCESS;

		return result;
	}

	// 전달인지가 있으면 맵에 넣음
	// 보통쿼리 검색용으로 사용
	public static Map<String, Object> putIfExist(Map<String, Object> paramMap, HttpServletRequest request, String [] param) {
		if (paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}
    	
		for (int i = 0; i < param.length; i++) {
			String item = request.getParameter(param[i]);
			if (!Utils.isNullOrEmpty(item)) {
				paramMap.put(param[i], item);
			}
		}

		return paramMap;
	}

	// 전달인지가 있으면 넣음
	public static Map<String, Object> putIfExist(HttpServletRequest request, String [] param) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
    	
		for (int i = 0; i < param.length; i++) {
			String item = request.getParameter(param[i]);
			if (!Utils.isNullOrEmpty(item)) {
				paramMap.put(param[i], item);
			}
		}

		return paramMap;
	}
}
