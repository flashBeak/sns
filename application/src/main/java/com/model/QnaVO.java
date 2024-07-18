package com.model;

// QnA
public class QnaVO {
	
	private int id;
	private int userId;
	private int answerId;
	private String contents;	// 질문
	private String answer;		// 답변
	private String created;
	private String answerd;		// 답변 일시

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAnswerId() {
		return this.answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getAnswerd() {
		return this.answerd;
	}

	public void setAnswerd(String answerd) {
		this.answerd = answerd;
	}
}