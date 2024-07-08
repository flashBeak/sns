package com.service.common.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bussiness.Utils;
import com.model.common.NotificationReadVO;
import com.model.common.NotificationVO;
import com.service.common.NotificationService;

import jakarta.annotation.Resource;
 
@Service("NotificationService")
public class NotificationServiceImpl implements NotificationService {
    
   @Resource(name="NotificationDAO")
   private NotificationDAO dao;
 
    @Override
	public List<NotificationVO> getList(Map<String, Object> map) throws Exception {
        return dao.getList(map);
    }

	@Override
	public int getListTotalCount(Map<String, Object> map) throws Exception {
        return dao.getListTotalCount(map);
	}

	@Override
	public NotificationVO get(String id) throws Exception {
        return dao.get(id);
	}

	@Override
	public int add(NotificationVO item) throws Exception {
		return dao.add(item);
	}

	@Override
	public int add(int type, String usageIdStr, String userId, String title, String contents) throws Exception {
		int usageId = Utils.parseInt(usageIdStr, 0);
		NotificationVO item = new NotificationVO();
		item.setType(type);
		item.setUserId(Utils.parseInt(userId, 0));
		item.setTitle(title);
		item.setContents(contents);

		switch (type) {
			case NotificationVO.TYPE_NOTICE : item.setPostId(usageId); break;	// 공지
			default : return 0;
		}

		dao.add(item);	// 정보 추가

		return item.getId();	// id값 반환
	}

	/**
	 * @param type			알림 type
	 * @param usageIdStr
	 * @param userId		
	 * @param title			제목
	 * @param contents		내용
	 * @param optionMap		추가 정보 notification type-value로 구성 ex) map.put("NotificationVO.TYPE_NOTICE", 1)
	 * @return int
	 */
	@Override
	public int add(int type, String usageIdStr, String userId, String title, String contents, Map<String, Object> optionMap) throws Exception {
		int usageId = Utils.parseInt(usageIdStr, 0);
		NotificationVO item = new NotificationVO();
		item.setType(type);
		item.setUserId(Utils.parseInt(userId, 0));
		item.setTitle(title);
		item.setContents(contents);

		switch (type) {
			case NotificationVO.TYPE_NOTICE : item.setPostId(usageId); break;	// 공지
			default : return 0;
		}

		if (optionMap != null && !optionMap.isEmpty()) {	// 추가할 정보가 있으면, DB 정보 추가
			for (Map.Entry<String, Object> entry : optionMap.entrySet()) {
				int key = Utils.parseInt(entry.getKey(), 0);
				int value = Utils.parseInt(entry.getValue().toString(), 0);
				
				switch (key) {	// key 값에 따라서 NotificationVO에 값 설정
					case NotificationVO.TYPE_NOTICE : item.setPostId(value); break;	// 공지
					default : return 0;
				}
			}
		}

		dao.add(item);	// 정보 추가

		return item.getId();	// id값 반환
	}

	@Override
	public int add(int type, String postId) throws Exception {
		NotificationVO item = new NotificationVO();
		item.setType(type);
		item.setPostId(Utils.parseInt(postId, 0));

		return dao.add(item); 
	}

	@Override
	public int remove(String id) throws Exception {
		return dao.remove(id);
	}

	@Override
	public NotificationReadVO getNotificationRead(String userId) throws Exception {
		return dao.getNotificationRead(userId);
	}

	@Override
	public int addNotificationRead(String userId, String readd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("readd", readd);
		
		NotificationReadVO item = dao.getNotificationRead(userId);
		if (item == null) {	// 기존 정보가 없는 경우, 추가
			return dao.addNotificationRead(map);
		} else {	// 기존 정보가 있는 경우, 수정
			return dao.updateNotificationRead(map);
		}
	}

	@Override
	public int getNotificationUnreadCount(String userId) throws Exception {
		return dao.getNotificationUnreadCount(userId);
	}

	@Override
	public int removeTimePassed(int day) throws Exception {
		return dao.removeTimePassed(day);
	}
}
