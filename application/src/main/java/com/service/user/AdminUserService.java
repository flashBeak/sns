package com.service.user;

import java.util.List;
import com.model.UserVO;

public interface AdminUserService {
	public List<UserVO> getList() throws Exception;
	public int getListTotalCount() throws Exception;
	public UserVO get(String id) throws Exception;
	public int add(UserVO item) throws Exception;
	public int update(UserVO item) throws Exception;
	public int updateInfo(UserVO item) throws Exception;
	public int remove(String id) throws Exception;
}
