package com.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.model.common.FileVO;
 
public interface FileService {
	public List<FileVO> getList(int type, int id) throws Exception;
	public List<FileVO> getList(int type, String id) throws Exception;
	public FileVO getOne(int type, int id) throws Exception;
	public FileVO getOne(int type, String id) throws Exception;
	public List<FileVO> getListByIdList(List<String> list) throws Exception;
	public int add(FileVO item) throws Exception;
	public int addList(List<FileVO> list) throws Exception;
	public int update(FileVO item) throws Exception;
	public Map<String, Object> add(final MultipartHttpServletRequest multiRequest, String path, String fileString) throws Exception;
	public Map<String, Object> update(final MultipartHttpServletRequest multiRequest, String path, String fileName, String fileString, boolean bRemoveOld) throws Exception;
	public int remove(String id) throws Exception;
	public int removeByIdList(List<String> list, String filePath) throws Exception;
	public int removeByUsageId(int fileType, String id, String filePath) throws Exception;
	public int uploadList(final MultipartHttpServletRequest multiRequest, String fileStr, String path, int fileType, String postId) throws Exception;
	public int uploadList(final MultipartHttpServletRequest multiRequest, String fileStr, String path, int fileType, int postId) throws Exception;
	public FileVO upload(String path, MultipartFile file) throws Exception;
	public boolean isImage(String fileName) throws Exception;
	public boolean createThumbnailImage(String absFilePath, String thumbnailPath) throws Exception;
	public boolean delete(String filePath, String fileName) throws Exception;

	public String copy(String sourcePath, String sourceFileName, String destPath, String destFileName) throws Exception;
}