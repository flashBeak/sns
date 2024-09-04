package com.service.post.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.model.post.BoardVO;

@Repository("BoardDAO")
public class BoardDAO {

    @Autowired
	protected SqlSessionTemplate sqlSession;

	public List<BoardVO> getList(Map<String, Object> map) {
		return sqlSession.selectList("BoardDAO.selectList", map);
	}

	public int getListTotalCount(Map<String, Object> map) {
		return sqlSession.selectOne("BoardDAO.selectListTotalCount", map);
	}
    
	public BoardVO get(int id) throws Exception {
		return sqlSession.selectOne("BoardDAO.select", id);
    }

	public BoardVO getByCategoryId(int categoryId) throws Exception {
		return sqlSession.selectOne("BoardDAO.selectByCategoryId", categoryId);
    }

	public int add(BoardVO item) throws Exception {
		return sqlSession.insert("BoardDAO.insert", item);
    }

	public int update(BoardVO item) throws Exception {
		return sqlSession.update("BoardDAO.update", item);
    }

	public int remove(int id) throws Exception {
		return sqlSession.delete("BoardDAO.delete", id);
    }
}