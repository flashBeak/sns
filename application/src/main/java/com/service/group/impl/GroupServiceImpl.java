package com.service.group.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.model.Constants;
import com.model.group.GroupVO;
import com.service.group.GroupService;

import jakarta.annotation.Resource;

@Service("GroupService")
public class GroupServiceImpl implements GroupService {

	@Resource(name="AdminGroupDAO")
    private GroupDAO dao;

	@Override
	public GroupVO get(int id) throws Exception {
		return dao.get(id);
	}

	@Override
	public List<GroupVO> getList() throws Exception {
		return dao.getList();
	}

	@Override
	public int getListTotalCount() throws Exception {
		return dao.getListTotalCount();
	}

	@Override
	public int add(GroupVO item) throws Exception {
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
	public int update(GroupVO item) throws Exception {
		return dao.update(item);
	}

	@Override
	public int remove(int id) throws Exception {
		return dao.remove(id);
	}
}
