package com.service.group.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.model.group.GroupManagerVO;
import com.model.group.GroupMemberVO;
import com.model.group.GroupVO;

@Repository("GroupDAO")
public class GroupDAO {

    @Autowired
	protected SqlSessionTemplate sqlSession;

	public List<GroupVO> getList(Map<String, Object> map) {
		return sqlSession.selectList("AdminGroupDAO.selectList", map);
	}

	public int getListTotalCount(Map<String, Object> map) {
		return sqlSession.selectOne("AdminGroupDAO.selectListTotalCount", map);
	}

	public List<GroupVO> getListByUserId(Map<String, Object> map) {
		return sqlSession.selectList("AdminGroupDAO.selectList", map);
	}

	public int getListTotalCountByUserId(Map<String, Object> map) {
		return sqlSession.selectOne("AdminGroupDAO.selectListTotalCount", map);
	}
    
	public GroupVO get(int id) throws Exception {
		return sqlSession.selectOne("AdminGroupDAO.select", id);
    }

	public int add(GroupVO item) throws Exception {
		return sqlSession.insert("AdminGroupDAO.insert", item);
    }

	public int update(GroupVO item) throws Exception {
		return sqlSession.update("AdminGroupDAO.update", item);
    }

	public int remove(int id) throws Exception {
		return sqlSession.delete("AdminGroupDAO.delete", id);
    }

	public GroupMemberVO getMemberByUserId(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("AdminGroupDAO.selectMemberByUserId", map);
    }

	public int addMember(GroupMemberVO item) throws Exception {
		return sqlSession.insert("AdminGroupDAO.insertMember", item);
    }

	public GroupManagerVO getManager(int groupMemberId) throws Exception {
		return sqlSession.selectOne("AdminGroupDAO.selectManager", groupMemberId);
    }

	public int addManager(GroupManagerVO item) throws Exception {
		return sqlSession.insert("AdminGroupDAO.insertManager", item);
    }
}

