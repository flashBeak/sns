package com.service.schedule.impl;

import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.model.schedule.ScheduleVO;

@Repository("ScheduleDAO")
public class ScheduleDAO {

    @Autowired
	protected SqlSessionTemplate sqlSession;

	public List<ScheduleVO> getList() {
		return sqlSession.selectList("AdminScheduleDAO.selectList");
	}

	public int getListTotalCount() {
		return sqlSession.selectOne("AdminScheduleDAO.selectListTotalCount");
	}
    
	public ScheduleVO get(int id) throws Exception {
		return sqlSession.selectOne("AdminScheduleDAO.select", id);
    }

	public int add(ScheduleVO item) throws Exception {
		return sqlSession.insert("AdminScheduleDAO.insert", item);
    }

	public int addInfo(ScheduleVO item) throws Exception {
		return sqlSession.insert("AdminScheduleDAO.insertInfo", item);
    }

	public int update(ScheduleVO item) throws Exception {
		return sqlSession.update("AdminScheduleDAO.update", item);
    }

	public int remove(int id) throws Exception {
		return sqlSession.delete("AdminScheduleDAO.delete", id);
    }
}

