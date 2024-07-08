package com.service.common.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.model.common.NotificationReadVO;
import com.model.common.NotificationVO;

@Repository("NotificationDAO")
public class NotificationDAO {

	@Autowired
	protected SqlSessionTemplate sqlSession;

	public List<NotificationVO> getList(Map<String, Object> map) {
		return sqlSession.selectList("NotificationDAO.selectList", map);
	}

	public int getListTotalCount(Map<String, Object> map) {
		return sqlSession.selectOne("NotificationDAO.selectListTotalCount", map);
	}

	public NotificationVO get(String id) throws Exception {
		return sqlSession.selectOne("NotificationDAO.select", id);
	}

	public int add(NotificationVO item) throws Exception {
		return sqlSession.insert("NotificationDAO.insert", item);
	}

	public int remove(String id) throws Exception {
		return sqlSession.delete("NotificationDAO.delete", id);
	}

	public int removeTimePassed(int day) throws Exception {
		return sqlSession.delete("NotificationDAO.deleteTimePassed", day);
	}

	public NotificationReadVO getNotificationRead(String userId) throws Exception {
		return sqlSession.selectOne("NotificationDAO.selectNotificationReadByUserId", userId);
	}

	public int addNotificationRead(Map<String, Object> map) throws Exception {
		return sqlSession.insert("NotificationDAO.insertNotificationRead", map);
	}

	public int updateNotificationRead(Map<String, Object> map) throws Exception {
		return sqlSession.insert("NotificationDAO.updateNotificationRead", map);
	}

	public int getNotificationUnreadCount(String userId) throws Exception {
		return sqlSession.selectOne("NotificationDAO.selectNotificationUnreadCount", userId);
	}
}
