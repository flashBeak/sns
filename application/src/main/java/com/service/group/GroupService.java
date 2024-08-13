package com.service.group;

import java.util.List;

import com.model.group.GroupVO;

public interface GroupService {
	public List<GroupVO> getList() throws Exception;
	public int getListTotalCount() throws Exception;
	public GroupVO get(int id) throws Exception;
	public int add(GroupVO item) throws Exception;
	public int update(GroupVO item) throws Exception;
	public int remove(int id) throws Exception;
}