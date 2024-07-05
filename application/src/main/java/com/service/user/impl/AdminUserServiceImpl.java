package com.service.user.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.model.UserVO;
import com.service.user.AdminUserService;

import jakarta.annotation.Resource;

@Service("AdminUserService")
public class AdminUserServiceImpl implements AdminUserService {

	@Resource(name="AdminUserDAO")
    private AdminUserDAO dao;

	@Override
	public UserVO get(String id) throws Exception {
		return dao.get(id);
	}

	@Override
	public List<UserVO> getList() throws Exception {
		return dao.getList();
	}

	@Override
	public int getListTotalCount() throws Exception {
		return dao.getListTotalCount();
	}
}
