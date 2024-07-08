package com.service.auth.impl;

import org.springframework.stereotype.Service;

import com.bussiness.Utils;
import com.model.UserVO;
import com.service.auth.AdminAuthService;
import com.service.user.impl.AdminUserDAO;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Service("AdminUserService")
public class AdminAuthServiceImpl implements AdminAuthService {

	@Resource(name="AdminUserDAO")
	AdminUserDAO userDAO;

	@Override
	public boolean isAdmin(HttpServletRequest request) throws Exception {
		String userId = request.getSession().getId();
		if (Utils.isNullOrEmpty(userId)) {
			return false;
		}

		UserVO userVO = userDAO.get(userId);
		if (userVO == null) {
			return false;
		}

		if (Utils.parseInt(userVO.getRole(), 0) != UserVO.USER_ROLE_ADMIN) {
			return false;
		}

		return true;
	}
}
