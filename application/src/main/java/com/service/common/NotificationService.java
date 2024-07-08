package com.service.common;

import java.util.List;
import java.util.Map;

import com.model.common.NotificationReadVO;
import com.model.common.NotificationVO;
 
public interface NotificationService {
	public List<NotificationVO> getList(Map<String, Object> map) throws Exception;
	public int getListTotalCount(Map<String, Object> map) throws Exception;
	public NotificationVO get(String id) throws Exception;
	public int add(NotificationVO item) throws Exception;
	public int add(int type, String usageIdStr, String userId, String title, String contents) throws Exception;
	public int add(int type, String usageIdStr, String userId, String title, String contents, Map<String, Object> optionMap) throws Exception;
	public int add(int type, String postId) throws Exception;
	public NotificationReadVO getNotificationRead(String userId) throws Exception;
	public int addNotificationRead(String userId, String readd) throws Exception;
	public int remove(String id) throws Exception;
	public int removeTimePassed(int day) throws Exception;	// 일정 시간 지난 알림 데이터 삭제

	public int getNotificationUnreadCount(String userId) throws Exception;
}