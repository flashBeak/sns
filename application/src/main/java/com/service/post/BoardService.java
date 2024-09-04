package com.service.post;

import java.util.List;
import java.util.Map;

import com.model.post.BoardVO;

public interface BoardService {
	public List<BoardVO> getList(Map<String, Object> map) throws Exception;
	public int getListTotalCount(Map<String, Object> map) throws Exception;
	public BoardVO get(int id) throws Exception;
	public BoardVO getByCategoryId(int categoryId) throws Exception;
	public int add(BoardVO item) throws Exception;
	public int update(BoardVO item) throws Exception;
	public int remove(int id) throws Exception;
}