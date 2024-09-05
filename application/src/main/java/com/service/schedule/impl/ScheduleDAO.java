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
		return sqlSession.selectList("ScheduleDAO.selectList");
	}

	public int getListTotalCount() {
		return sqlSession.selectOne("ScheduleDAO.selectListTotalCount");
	}
    
	public ScheduleVO get(int id) throws Exception {
		return sqlSession.selectOne("ScheduleDAO.select", id);
    }

	public int add(ScheduleVO item) throws Exception {
		return sqlSession.insert("ScheduleDAO.insert", item);
    }

	public int addInfo(ScheduleVO item) throws Exception {
		return sqlSession.insert("ScheduleDAO.insertInfo", item);
    }

	public int update(ScheduleVO item) throws Exception {
		return sqlSession.update("ScheduleDAO.update", item);
    }

	public int remove(int id) throws Exception {
		return sqlSession.delete("ScheduleDAO.delete", id);
    }
}

