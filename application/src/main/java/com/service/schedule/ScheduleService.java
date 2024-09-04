package com.service.schedule;

import java.util.List;

import com.model.schedule.ScheduleVO;

public interface ScheduleService {
	public List<ScheduleVO> getList() throws Exception;
	public int getListTotalCount() throws Exception;
	public ScheduleVO get(int id) throws Exception;
	public int add(ScheduleVO item) throws Exception;
	public int update(ScheduleVO item) throws Exception;
	public int remove(int id) throws Exception;
}