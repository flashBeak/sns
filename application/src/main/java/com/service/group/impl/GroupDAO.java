package com.service.group.impl;

import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.model.group.GroupVO;

@Repository("GroupDAO")
public class GroupDAO {

    @Autowired
	protected SqlSessionTemplate sqlSession;

	public List<GroupVO> getList() {
		return sqlSession.selectList("AdminGroupDAO.selectList");
	}

	public int getListTotalCount() {
		return sqlSession.selectOne("AdminGroupDAO.selectListTotalCount");
	}
    
	public GroupVO get(int id) throws Exception {
		return sqlSession.selectOne("AdminGroupDAO.select", id);
    }

	public int add(GroupVO item) throws Exception {
		return sqlSession.insert("AdminGroupDAO.insert", item);
    }

	public int addInfo(GroupVO item) throws Exception {
		return sqlSession.insert("AdminGroupDAO.insertInfo", item);
    }

	public int update(GroupVO item) throws Exception {
		return sqlSession.update("AdminGroupDAO.update", item);
    }

	public int remove(int id) throws Exception {
		return sqlSession.delete("AdminGroupDAO.delete", id);
    }
}

