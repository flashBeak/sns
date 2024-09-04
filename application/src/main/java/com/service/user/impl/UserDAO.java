package com.service.user.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.model.UserVO;

@Repository("UserDAO")
public class UserDAO {

    @Autowired
	protected SqlSessionTemplate sqlSession;

	public List<UserVO> getList() {
		return sqlSession.selectList("UserDAO.selectList");
	}

	public int getListTotalCount() {
		return sqlSession.selectOne("UserDAO.selectListTotalCount");
	}
    
	public UserVO get(String id) throws Exception {
		return sqlSession.selectOne("UserDAO.select", id);
    }

	public UserVO getByLoginTypeSocialId(Map<String, Object> map) throws Exception {
		return sqlSession.selectOne("UserDAO.selectByLoginTypeSocialId", map);
    }

	public int add(UserVO item) throws Exception {
		return sqlSession.insert("UserDAO.insert", item);
    }

	public int addInfo(UserVO item) throws Exception {
		return sqlSession.insert("UserDAO.insertInfo", item);
    }

	public int update(UserVO item) throws Exception {
		return sqlSession.update("UserDAO.update", item);
    }

	public int updateInfo(UserVO item) throws Exception {
		return sqlSession.update("UserDAO.updateInfo", item);
    }

	public int remove(String id) throws Exception {
		return sqlSession.delete("UserDAO.delete", id);
    }
}

