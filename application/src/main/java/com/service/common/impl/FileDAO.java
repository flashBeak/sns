package com.service.common.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.model.common.FileVO;

@Repository("FileDAO")
public class FileDAO {

    @Autowired
	protected SqlSessionTemplate sqlSession;

	public List<FileVO> getList(Map<String, Object> map) {
		return sqlSession.selectList("FileDAO.selectList", map);
	}

	public List<FileVO> getListByIdList(List<String> list) {
		return sqlSession.selectList("FileDAO.selectListByIdList", list);
	}
    
	public int add(FileVO item) throws Exception {
		return sqlSession.insert("FileDAO.insert", item);
    }
    
	public int addList(List<FileVO> list) throws Exception {
		return sqlSession.insert("FileDAO.insertList", list);
    }
    
	public int update(FileVO item) throws Exception {
		return sqlSession.update("FileDAO.update", item);
    }
    
	public int remove(String id) throws Exception {
		return sqlSession.delete("FileDAO.delete", id);
    }
    
	public int removeByUsageId(Map<String, Object> map) throws Exception {
		return sqlSession.delete("FileDAO.deleteByUsageId", map);
    }
    
	public int removeByIdList(List<String> list) throws Exception {
		return sqlSession.delete("FileDAO.deleteByIdList", list);
    }
}
