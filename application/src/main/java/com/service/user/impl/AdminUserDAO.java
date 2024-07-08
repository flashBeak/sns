package com.service.user.impl;

import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.model.UserVO;

@Repository("AdminUserDAO")
public class AdminUserDAO {

    @Autowired
	protected SqlSessionTemplate sqlSession;

	public List<UserVO> getList() {
		return sqlSession.selectList("AdminUserDAO.selectList");
	}

	public int getListTotalCount() {
		return sqlSession.selectOne("AdminUserDAO.selectListTotalCount");
	}
    
	public UserVO get(String id) throws Exception {
		return sqlSession.selectOne("AdminUserDAO.select", id);
    }

	public int add(UserVO item) throws Exception {
		return sqlSession.insert("AdminUserDAO.insert", item);
    }

	public int addInfo(UserVO item) throws Exception {
		return sqlSession.insert("AdminUserDAO.insertInfo", item);
    }

	public int update(UserVO item) throws Exception {
		return sqlSession.update("AdminUserDAO.update", item);
    }

	public int updateInfo(UserVO item) throws Exception {
		return sqlSession.update("AdminUserDAO.updateInfo", item);
    }

	public int remove(String id) throws Exception {
		return sqlSession.delete("AdminUserDAO.delete", id);
    }
}

