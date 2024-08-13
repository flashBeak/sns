package com.model.schedule;

// 일정 참여
public class ScheduleJoinVO {
	
	private int id;
	private int scheduleId;
	private int groupMemberId;
	private String created;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public int getGroupMemberId() {
		return this.groupMemberId;
	}

	public void setGroupMemberId(int groupMemberId) {
		this.groupMemberId = groupMemberId;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}