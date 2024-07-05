package com.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.UserVO;
import com.service.user.AdminUserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin/user")
public class AdminUserController {

    @Resource(name = "AdminUserService")
	AdminUserService userService;

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
    	    	
    	returnMap.put("item", item);

		return returnMap;
    }
}
