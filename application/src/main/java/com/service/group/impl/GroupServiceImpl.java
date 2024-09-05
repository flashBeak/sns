package com.service.group.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bussiness.Utils;
import com.model.UserVO;
import com.model.group.GroupManagerVO;
import com.model.group.GroupMemberVO;
import com.model.group.GroupVO;
import com.service.group.GroupService;
import com.service.user.impl.UserDAO;

import jakarta.annotation.Resource;

@Service("GroupService")
public class GroupServiceImpl implements GroupService {

	@Resource(name="GroupDAO")
    private GroupDAO dao;

	@Resource(name="UserDAO")
    private UserDAO userDAO;

	@Override
	public GroupVO get(int id) throws Exception {
		return dao.get(id);
	}

	@Override
	public List<GroupVO> getList(Map<String, Object> map) throws Exception {
		return dao.getList(map);
	}

	@Override
	public int getListTotalCount(Map<String, Object> map) throws Exception {
		return dao.getListTotalCount(map);
	}

	@Override
	public List<GroupVO> getListByUserId(Map<String, Object> map) throws Exception {
		return dao.getList(map);
	}

	@Override
	public int getListTotalCountByUserId(Map<String, Object> map) throws Exception {
		return dao.getListTotalCount(map);
	}
	
	@Override
	public int add(GroupVO item) throws Exception {
		return dao.add(item);
	}

	@Override
	public int update(GroupVO item) throws Exception {
		return dao.update(item);
	}

	@Override
	public int remove(int id) throws Exception {
		
		// 게시판 목록
		// 게시물 목록
		// 댓글 삭제
		// 게시물 삭제
		// 게시판 삭제
		// 카테고리 권한 삭제
		// 카테고리 삭제
		// 그룹 매니져 삭제
		// 그룹 멤버 삭제
		// 그룹 삭제

		return dao.remove(id);
	}

	@Override
	public int addMember(GroupMemberVO item) throws Exception {
		return dao.addMember(item);
	}

	@Override
	public int addManager(GroupManagerVO item) throws Exception {
		return dao.addManager(item);
	}

	@Override
	public boolean getAuth(int id, int userId) throws Exception {
		UserVO userVO = userDAO.get(String.valueOf(userId));	// 사용자 정보 가져오기
		if (userVO == null) {
			return false;
		}

		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("userId", userId);

		GroupMemberVO groupMemberVO = dao.getMemberByUserId(map);	// 그룹 멤버 정보 가져오기
		if (groupMemberVO == null) {
			return false;
		}

		GroupManagerVO groupManagerVO = dao.getManager(groupMemberVO.getId());	// 그룹 매니저 정보 가져오기
		if (groupManagerVO == null) {
			return false;
		}

		if (!Utils.isEqual(GroupManagerVO.TYPE_MASTER, groupManagerVO.getType())) {	// 그룹장이 아닌 경우
			return false;
		}

		return true;
	}
}
