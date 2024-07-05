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
}

