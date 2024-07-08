package com.service.user.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.model.Constants;
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

	@Override
	public int add(UserVO item) throws Exception {
		int result = dao.add(item);
		if (result <= 0) {
			return result;
		}

		int resultInfo = dao.addInfo(item);
		if (resultInfo <= 0) {
			return resultInfo;
		}

		return Constants.RESULT_CODE_SUCCESS;
	}

	@Override
	public int update(UserVO item) throws Exception {
		return dao.update(item);
	}

	@Override
	public int updateInfo(UserVO item) throws Exception {
		return dao.updateInfo(item);
	}

	@Override
	public int remove(String id) throws Exception {
		return dao.remove(id);
	}
}
