package com.service.post.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.model.post.BoardVO;
import com.service.post.BoardService;
import jakarta.annotation.Resource;

@Service("BoardService")
public class BoardServiceImpl implements BoardService {

	@Resource(name="BoardDAO")
    private BoardDAO dao;

	@Override
	public List<BoardVO> getList(Map<String, Object> map) throws Exception {
		return dao.getList(map);
	}

	@Override
	public int getListTotalCount(Map<String, Object> map) throws Exception {
		return dao.getListTotalCount(map);
	}

	@Override
	public BoardVO get(int id) throws Exception {
		return dao.get(id);
	}

	@Override
	public BoardVO getByCategoryId(int categoryId) throws Exception {
		return dao.getByCategoryId(categoryId);
	}

	@Override
	public int add(BoardVO item) throws Exception {
		return dao.add(item);
	}

	@Override
	public int update(BoardVO item) throws Exception {
		return dao.update(item);
	}

	@Override
	public int remove(int id) throws Exception {
		return dao.remove(id);
	}
}
