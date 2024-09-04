package com.controller.auth;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Constants;
import com.model.UserVO;
import com.service.user.UserService;

@Controller
@RequestMapping(value = "/auth")
@PropertySource("classpath:/project.properties")
public class AuthController {
	
	//private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Resource(name = "UserService")
    UserService userService;
	
	@Value("${niceid.callback.server}")
	String callbackServer;

	final String SITE_CODE = "BX872";
	final String SITE_PASSWORD = "41HHFR9xEpUu";

	final String PREFIX_PATH = "common/auth";

	public AuthController() {}

	/**
	 * 로그인
	 * @param loginType		0 naver, 1 kakao, 2 apple
	 * @param socialId
	 * @return page
	 */
    @PostMapping("/login")
    public @ResponseBody Map<String, Object> login(@ModelAttribute("UserVO") UserVO userVO, HttpServletRequest request) throws Exception {

		Map<String, Object> returnMap = new HashMap<>();

    	// db에 해당 사용자가 있는지 확인
    	UserVO item = userService.getByLoginTypeSocialId(userVO.getLoginType(), userVO.getSocialId());
    	if (item == null) {
			returnMap.put("code", Constants.RESULT_CODE_NO_ITEM);
			returnMap.put("message", Constants.RESULT_MSG_NO_ITEM);
	        return returnMap;
    	}

    	// session에 사용자 정보저장
		HttpSession session = request.getSession();
		if (session == null) {
			returnMap.put("code", Constants.RESULT_CODE_SYSTEM_ERROR);
			returnMap.put("message", Constants.RESULT_MSG_SYSTEM_ERROR);
	        return returnMap;
		}

		session.setAttribute("userId", item.getId());
		session.setAttribute("username", item.getUsername());
		session.setAttribute("fullName", item.getFullName());
		
		returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
		returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
		
		return returnMap;
    }

	/**
	 * 로그아웃
	 */
	@PostMapping("/logout")
	public @ResponseBody Map<String, Object> logout(HttpServletRequest request) throws Exception {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		HttpSession session = request.getSession();
		session.invalidate();	// 세션 삭제
		
		returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
		returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
		return returnMap;
	}
}
