package com.service.schedule.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.model.Constants;
import com.model.schedule.ScheduleVO;
import com.service.schedule.ScheduleService;

import jakarta.annotation.Resource;

@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Resource(name="AdminScheduleDAO")
    private ScheduleDAO dao;

	@Override
	public ScheduleVO get(int id) throws Exception {
		return dao.get(id);
	}

	@Override
	public List<ScheduleVO> getList() throws Exception {
		return dao.getList();
	}

	@Override
	public int getListTotalCount() throws Exception {
		return dao.getListTotalCount();
	}

	@Override
	public int add(ScheduleVO item) throws Exception {
		int result = dao.add(item);
		if (result <= 0) {
			return result;
		}

		int resultInfo = dao.addInfo(item);
		if (resultInfo <= 0) {
			return resultInfo;
		}

		return Constants.RESULT_CODE_SUCCESS;
	}

	@Override
	public int update(ScheduleVO item) throws Exception {
		return dao.update(item);
	}

	@Override
	public int remove(int id) throws Exception {
		return dao.remove(id);
	}
}
