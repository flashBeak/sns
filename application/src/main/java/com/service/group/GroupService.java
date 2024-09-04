package com.service.group;

import java.util.List;
import java.util.Map;

import com.model.group.GroupManagerVO;
import com.model.group.GroupMemberVO;
import com.model.group.GroupVO;

public interface GroupService {
	public List<GroupVO> getList(Map<String, Object> map) throws Exception;
	public int getListTotalCount(Map<String, Object> map) throws Exception;
	public List<GroupVO> getListByUserId(Map<String, Object> map) throws Exception;
	public int getListTotalCountByUserId(Map<String, Object> map) throws Exception;
	public GroupVO get(int id) throws Exception;
	public int add(GroupVO item) throws Exception;
	public int update(GroupVO item) throws Exception;
	public int remove(int id) throws Exception;

	public int addMember(GroupMemberVO item) throws Exception;
	public int addManager(GroupManagerVO item) throws Exception;

	public boolean getAuth(int id, int userId) throws Exception;
}