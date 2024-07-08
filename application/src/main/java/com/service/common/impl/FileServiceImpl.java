package com.service.common.impl;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bussiness.Utils;
import com.model.Constants;
import com.model.common.FileVO;
import com.model.common.ImageVO;
import com.service.common.FileService;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
 
@Service("FileService")
public class FileServiceImpl implements FileService {
	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	public static final int BUFF_SIZE = 2048;
	
	@Value("${file.save.abs.path}")
	String fileSaveAbsPath;

	@Value("${file.save.relative.path}")
	String fileSaveRelativePath;

	@Value("${image.resizing.width}")
	int imageResizingWidth;

	@Value("${thumbnail.width}")
	int thumbnailWidth;

	@Value("${thumbnail.height}")
	int thumbnailHeight;

	@Value("${file.upload_max.size}")
	int fileUploadMaxSize;

	@Value("${file.upload.extensions}")
	String fileUploadExtensions;

	@Value("${file.upload.extensions.images}")
	String fileUploadExtensionsImages;

    @Resource(name="FileDAO")
    FileDAO fileDAO;
 
    @Override
	public List<FileVO> getList(int type, int id) throws Exception {
    	return _getList(type, id);
    }
 
    @Override
	public List<FileVO> getList(int type, String id) throws Exception {
    	if (Utils.isNullOrEmpty(id)) {
    		return null;
    	}

    	return _getList(type, Integer.parseInt(id));
    }
 
	public List<FileVO> _getList(int type, int id) throws Exception {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("type", type);
    	paramMap.put("postId", id);
    	
        return fileDAO.getList(paramMap);
    }
 
    @Override
	public FileVO getOne(int type, int id) throws Exception {
    	return _getOne(type, id);
    }
 
    @Override
	public FileVO getOne(int type, String id) throws Exception {
    	if (Utils.isNullOrEmpty(id)) {
    		return null;
    	}
    	
    	return _getOne(type, Integer.parseInt(id));
    }

	/**
	 * 목록의 첫번째 아이템을 반환
	 * @param id
	 * @return map
	 * @exception Exception
	 */
	public FileVO _getOne(int type, int id) throws Exception {
    	List<FileVO> list = _getList(type, id);
    	if (list.size() > 0) {
    		return list.get(0);
    	} else {
    		return null;
    	}
    }
 
    @Override
	public List<FileVO> getListByIdList(List<String> list) throws Exception {
        return fileDAO.getListByIdList(list);
    }

	@Override
	public int add(FileVO item) throws Exception {
        return fileDAO.add(item);
	}

	@Override
	public int addList(List<FileVO> list) throws Exception {
        return fileDAO.addList(list);
	}

	@Override
	public int update(FileVO item) throws Exception {
        return fileDAO.update(item);
	}

	@Override
	public int remove(String id) throws Exception {
        return fileDAO.remove(id);
	}
	
	/**
	 * 파일 추가
	 * @param multiRequest
	 * @param path			경로
	 * @param fileName		파일명
	 * @param fileString	파일 문자열
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> add(final MultipartHttpServletRequest multiRequest, String path, String fileString) throws Exception {
    	Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("added", false);
		returnMap.put("fileName", "");

		// 추가로 업로드한 이미지 확인
		final MultipartFile image = multiRequest.getFile(fileString);
		if (image != null && !image.isEmpty()) { // 첨부파일이 있는 경우
			FileVO file = this.upload(path, image);
			if (file == null) {	// 파일 업로드 실패한 경우
	    		returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_FILE_UPLOAD);
	    		returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_FILE_UPLOAD);
	    		return returnMap;
			}

			returnMap.replace("fileName", file.getName());
			returnMap.replace("added", true);
		}

		returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
		returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
		return returnMap;
	}
	
	/**
	 * 파일 업데이트
	 * @param multiRequest
	 * @param path			경로
	 * @param fileName		파일명
	 * @param fileString	파일 문자열
	 * @param bRemoveOld	기존 파일 삭제 여부
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> update(final MultipartHttpServletRequest multiRequest, String path, String fileName, String fileString, boolean bRemoveOld) throws Exception {
    	Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("changed", false);
		returnMap.put("fileName", fileName);

		if (bRemoveOld) {	// 기존 파일 삭제 여부 확인
			boolean resultRemoveImage = this.delete(path, fileName);
			if (!resultRemoveImage) { // 파일 삭제 실패
	    		returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_DELETE_FILE);
	    		returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_DELETE_FILE);
	    		return returnMap;
			}

			returnMap.replace("fileName", "");
			returnMap.replace("changed", true);
		}

		// 추가로 업로드한 이미지 확인
		final MultipartFile image = multiRequest.getFile(fileString);
		if (image != null && !image.isEmpty()) { // 첨부파일이 있는 경우
			FileVO file = this.upload(path, image);
			if (file == null) {	// 파일 업로드 실패한 경우
	    		returnMap.put("code", Constants.RESULT_CODE_FAIL_TO_FILE_UPLOAD);
	    		returnMap.put("message", Constants.RESULT_MSG_FAIL_TO_FILE_UPLOAD);
	    		return returnMap;
			}

			returnMap.replace("fileName", file.getName());
			returnMap.replace("changed", true);
		}

		returnMap.put("code", Constants.RESULT_CODE_SUCCESS);
		returnMap.put("message", Constants.RESULT_MSG_SUCCESS);
		return returnMap;
	}

	@Override
	public int removeByUsageId(int fileType, String id, String filePath) throws Exception {
		// 파일정보 가져오기
		List<FileVO> fileList = getList(fileType, id);
		if (fileList.size() > 0) {
			for (FileVO item : fileList) {
				// 실제 파일 삭제
				this.delete(filePath, item.getName());
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("type", fileType);
			paramMap.put("postId", id);
			
			return fileDAO.removeByUsageId(paramMap);
		}

		return 0;
	}

	@Override
	public int removeByIdList(List<String> list, String filePath) throws Exception {
		if (list == null || list.size() == 0) {
			return 0;
		}
		
		// 파일정보 가져오기
		List<FileVO> fileList = getListByIdList(list);
		if (fileList.size() > 0) {
			for (FileVO item : fileList) {
				// 실제 파일 삭제
				this.delete(filePath, item.getName());
			}
		}
		
        return fileDAO.removeByIdList(list);
	}

	@Override
	public int uploadList(final MultipartHttpServletRequest multiRequest, String fileStr, String path, int fileType, String postId) throws Exception{
		return uploadList(multiRequest, fileStr, path, fileType, Utils.parseInt(postId, 0));
	}

	/**
	 * 등록된 파일을 서버에 업로드한다.
	 * @param multiRequest
	 * @param fileStr
	 * @param path	저장 경로(절대경로뒤 붙을 상대 경로)
	 * @param type	종류
	 * @param fileType	파일 종류
	 * @param postId
	 * @return int 업로드된 개수
	 * @throws Exception
	 */
	public int uploadList(final MultipartHttpServletRequest multiRequest, String fileStr, String path, int fileType, int postId) throws Exception {
		List<MultipartFile> multipartFileList = multiRequest.getFiles(fileStr);
		List<FileVO> fileList = new ArrayList<FileVO>();
		
		for (MultipartFile multipartFile : multipartFileList) {
			FileVO file = this.upload(path, multipartFile);	// 파일 업로드
			
			if (file != null) {
				file.setType(FileVO.TYPE_POST);
				file.setPostId(String.valueOf(postId));

				fileList.add(file);
			}
		}

		int count = 0;
		if (fileList.size() > 0) {	// 업로드된 이미지이 있으면 DB에 저장
			count = this.addList(fileList);	// DB에 저장
		}
		
		return count;
	}

	/**
	 * 등록된 파일을 서버에 업로드한다.
	 * @param path	저장 경로(절대경로뒤 붙을 상대 경로)
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public FileVO upload(String path, MultipartFile file) throws Exception {

		FileVO fileVO = null;
		try {
			if (file.getSize() / 1024 / 1024 > fileUploadMaxSize) {	// 파일 크기가 큰 경우
				return null;
			}

			String filePath = fileSaveAbsPath + fileSaveRelativePath + path;
			File dir = new File(filePath);
			if (!dir.exists()) {	// 폴더 경로 확인 후 없으면 생성
				if (!dir.mkdirs()) {
					logger.error("failed to directory");
					return null;
				}	
			}
			
			String showingFileName = file.getOriginalFilename();
			String realFileName = file.getOriginalFilename();
			if (realFileName == null || "".equals(realFileName)) {	// MultipartFile이 비정상인 경우
				return null;
			}

			int index = realFileName.lastIndexOf(".");
			String fileName = realFileName.substring(0, index);
			String fileExt = "";
			if (index > 0) {	// 확장자가 없는 경우도 있음.
				fileExt = realFileName.substring(index + 1);
			}
	
			realFileName = fileName + "_" + Utils.getNow("yyyyMMddhhmmssSSS") + "." + fileExt; // 2012.11 KISA 보안조치
			boolean returnValue = write(file, realFileName, filePath);
			if (returnValue) {	// 파일 저장 성공
				fileVO = new FileVO();
				fileVO.setName(realFileName);
				fileVO.setShowName(showingFileName);

				if (this.isImage(realFileName)) {	// 이미지이면 썸네일 생성
					this.createThumbnailImage(filePath + File.separator + realFileName, "thumbnails");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileVO;
	}
	
	/**
	 * 이미지 여부 확인
	 * @param fileName
	 * @return boolean
	 */
	public boolean isImage(String fileName) throws Exception {
		if (Utils.isNullOrEmpty(fileName)) {	// 파일명이 없는 경우
			return false;
		}
	
    	String whiteListFileUploadExtensions = fileUploadExtensionsImages;
		if (Utils.isNullOrEmpty(whiteListFileUploadExtensions)) {	// 허가된 이미지만 판별한다.
			return false;
		}
		
    	String [] allowedExtList = whiteListFileUploadExtensions.split("\\.");
    	if (allowedExtList.length == 0) {
    		return false;
    	}
		
    	String [] fileNameArray = fileName.split("\\.");
    	if (fileNameArray.length <= 1) {	// 파일명에 확장자가 없는 경우
    		return false;
    	}
    	
    	String fileExt = fileNameArray[fileNameArray.length-1].toLowerCase();
    	for (int i = 0; i < allowedExtList.length; i++) {
    		if (Utils.isNullOrEmpty(allowedExtList[i])) {	// 공백 등 잘못된 값.
    			continue;
    		}
    		
    		String allowedExt = allowedExtList[i].toLowerCase();
    		if (fileExt.equals(allowedExt)) {
    			return true;
    		}
    	}
    	
    	return false;
	}

	/**
	 * 썸네일 생성
	 * @param absFilePath	파일 명을 포함한 절대 경로	ex) /usr/local/test.png
	 * @return boolean
	 */
	public boolean createThumbnailImage(String absFilePath, String thumbnailPath) throws Exception {
        boolean isSaved = false;

        String orgFilePath = absFilePath.substring(0, absFilePath.lastIndexOf(File.separator) + 1);
        String orgFileName = absFilePath.substring(absFilePath.lastIndexOf(File.separator) + 1);

        File orgPath = new File(orgFilePath);

        if (orgPath.exists() && !orgPath.isFile()) {
            File orgFile = new File(absFilePath);

            if (orgFile.exists() && !orgFile.isDirectory()) {
                try {
                    String trgFilePath = orgFilePath + thumbnailPath + File.separator;

                    File trgPath = new File(trgFilePath);

                    if (!trgPath.exists() || trgPath.isFile()) {
                        if (!trgPath.mkdirs()) {
                            return false;
                        }
                    }

                    File trgFile = new File(trgFilePath + orgFileName);

                    BufferedImage orgImage = ImageIO.read(orgFile);

                    // Exif 정보 읽기
					Metadata metadata = ImageMetadataReader.readMetadata(orgFile);
					ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
					if (exifIFD0Directory != null) {
						int orientation = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
						switch (orientation) {
							case 3:
								orgImage = rotate(orgImage, 180);
								break;
							case 6:
								orgImage = rotate(orgImage, 90);
								break;
							case 8:
								orgImage = rotate(orgImage, -90);
								break;
						}
					}

                    double ratio = 1.0;
                    double orgWidth = orgImage.getWidth();
                    double orgHeight = orgImage.getHeight();

                    int setWidth = thumbnailWidth;

                    if (orgImage.getWidth() > setWidth) {
                        ratio = orgImage.getWidth() / (double) setWidth;
                    }

                    int width = (int) (orgWidth / ratio);
                    int height = (int) (orgHeight / ratio);

                    BufferedImage trgImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                    Graphics2D graphic = trgImage.createGraphics();
                    Image image = orgImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    graphic.drawImage(image, 0, 0, width, height, null);
                    graphic.dispose();

                    String ext = orgFileName.lastIndexOf(".") != -1 ? orgFileName.substring(orgFileName.lastIndexOf(".") + 1) : "jpg";

                    ImageIO.write(trgImage, ext, trgFile);

                    isSaved = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return isSaved;
    }

	// 이미지 회전
	private BufferedImage rotate(BufferedImage src, int degrees) {
		int width = src.getWidth();
		int height = src.getHeight();
	
		BufferedImage rotated = new BufferedImage(height, width, src.getType());
	
		Graphics2D g2d = rotated.createGraphics();
		g2d.rotate(Math.toRadians(degrees), rotated.getWidth() / 2, rotated.getHeight() / 2);
		g2d.drawImage(src, (rotated.getWidth() - width) / 2, (rotated.getHeight() - height) / 2, null);
		g2d.dispose();
	
		return rotated;
	}
	
	// 파일 복사
	public boolean copy(String sourceFileName, String destFileName) {
		try {
			FileInputStream fis = new FileInputStream(sourceFileName);
			FileOutputStream fos = new FileOutputStream(destFileName);

			int data = 0;
			while((data=fis.read())!=-1) {
				fos.write(data);
			}
			fis.close();
			fos.close();
		   
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * 파일을 실제 물리적인 경로에 생성한다.
	 *
	 * @param file
	 * @param newName
	 * @param stordFilePath
	 * @throws Exception
	 */
	protected boolean write(MultipartFile file, String showingFileName, String filePath) throws Exception {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		boolean returnValue = true;
		try {
			inputStream = file.getInputStream();
			File cFile = new File(filePath);

			if (!cFile.isDirectory())
				cFile.mkdir();

			outputStream = new FileOutputStream(filePath + File.separator + showingFileName);

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = inputStream.read(buffer, 0, BUFF_SIZE)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			returnValue = false;
		}
		finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
		
		return returnValue;
	}
	
	// 실제파일 삭제
	public boolean delete(String path, String fileName) throws Exception {
		if (Utils.isNullOrEmpty(fileName)) {
			return true;
		}
		
		// 저장된 위치 가져오기
		String filePath = fileSaveAbsPath + fileSaveRelativePath + path;
		String fileFullName = filePath + File.separator + fileName;
		
		File file = new File(fileFullName);
		if (file.exists()) {	// 경로에 있는지 확인
            if (file.delete()) {
                logger.debug("파일삭제 성공");
                
                // 썸네일 삭제
                delete(path + File.separator + "thumbnails", fileName);
        		return true;
            } else {
                logger.debug("파일삭제 실패");
        		return false;
            }
        } else {
            logger.debug("파일이 존재하지 않습니다.");
            return true;
        }
	}

	// 실제 이미지파일 삭제
	public boolean deleteImage(ImageVO imageVO, String path) throws Exception {
		// 저장된 위치 가져오기
		String imageSavePath = fileSaveAbsPath + fileSaveRelativePath + path;
		String fileFullName = imageSavePath + File.separator + imageVO.getRealFileName();
		
		File file = new File(fileFullName);
		if (file.exists()) {	// 경로에 있는지 확인
            if (file.delete()) {
                logger.debug("파일삭제 성공");
        		return true;
            }else{
                logger.debug("파일삭제 실패");
        		return false;
            }
        }else{
            logger.debug("파일이 존재하지 않습니다.");
        }

		return false;
	}

	/**
	 * 서버 파일에 대하여 다운로드를 처리한다.
	 *
	 * @param response
	 * @param streFileNm 파일저장 경로가 포함된 형태
	 * @param orignFileNm
	 * @throws Exception
	 */
	public void download(HttpServletResponse response, String streFileNm, String orignFileNm) throws Exception {
		String downFileName = streFileNm;
		String orgFileName = orignFileNm;

		File file = new File(downFileName);
		
		if (!file.exists()) {
			throw new FileNotFoundException(downFileName);
		}

		if (!file.isFile()) {
			throw new FileNotFoundException(downFileName);
		}

		int fSize = (int) file.length();
		if (fSize > 0) {
			BufferedInputStream in = null;

			try {
				in = new BufferedInputStream(new FileInputStream(file));

				String mimetype = "application/x-msdownload";

				//response.setBufferSize(fSize);
				response.setContentType(mimetype);
				response.setHeader("Content-Disposition:", "attachment; filename=" + orgFileName);
				response.setContentLength(fSize);
				//response.setHeader("Content-Transfer-Encoding","binary");
				//response.setHeader("Pragma","no-cache");
				//response.setHeader("Expires","0");
				FileCopyUtils.copy(in, response.getOutputStream());
			} finally {
				//EgovResourceCloseHelper.close(in);
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}

		/*
		String uploadPath = propertiesService.getString("fileDir");

		File uFile = new File(uploadPath, requestedFile);
		int fSize = (int) uFile.length();

		if (fSize > 0) {
		    BufferedInputStream in = new BufferedInputStream(new FileInputStream(uFile));

		    String mimetype = "text/html";

		    //response.setBufferSize(fSize);
		    response.setContentType(mimetype);
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + requestedFile + "\"");
		    response.setContentLength(fSize);

		    FileCopyUtils.copy(in, response.getOutputStream());
		    in.close();
		    response.getOutputStream().flush();
		    response.getOutputStream().close();
		} else {
		    response.setContentType("text/html");
		    PrintWriter printwriter = response.getWriter();
		    printwriter.println("<html>");
		    printwriter.println("<br><br><br><h2>Could not get file name:<br>" + requestedFile + "</h2>");
		    printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
		    printwriter.println("<br><br><br>&copy; webAccess");
		    printwriter.println("</html>");
		    printwriter.flush();
		    printwriter.close();
		}
		//*/

		/*
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition:", "attachment; filename=" + new String(orgFileName.getBytes(),"UTF-8" ));
		response.setHeader("Content-Transfer-Encoding","binary");
		response.setHeader("Pragma","no-cache");
		response.setHeader("Expires","0");

		BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
		int read = 0;

		while ((read = fin.read(b)) != -1) {
		    outs.write(b,0,read);
		}
		log.debug(this.getClass().getName()+" BufferedOutputStream Write Complete!!! ");

		outs.close();
		fin.close();
		//*/
	}

	// by chatgpt4
	@Override
	public String copy(String sourcePath, String sourceFileName, String destPath, String destFileName) throws Exception {
        // 원본 파일의 Path 객체 생성
        Path sourceFilePath = Paths.get(sourcePath, sourceFileName);
        // 목적지 파일의 Path 객체 생성
        Path destFilePath = Paths.get(destPath, destFileName);

		if (Files.exists(destFilePath)) {
            // Append a timestamp and a 6-digit random number to the destFileName
            String fileNameWithoutExtension = destFileName.substring(0, destFileName.lastIndexOf('.'));
            String extension = destFileName.substring(destFileName.lastIndexOf('.'));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timestamp = now.format(formatter);

            // Update destFileName and destFilePath with the new unique name
            destFileName = fileNameWithoutExtension + "_" + timestamp + "_" + Utils.getRandomString(6) + extension;
            destFilePath = Paths.get(destPath, destFileName);
        }

        try {
			Path destDirectory = destFilePath.getParent();
			if (destDirectory != null && !Files.exists(destDirectory)) {	// 폴더가 없으면 생성
				Files.createDirectories(destDirectory);
			}

            // 파일 복사 수행, 이미 존재하는 파일은 대체
            Files.copy(sourceFilePath, destFilePath, StandardCopyOption.REPLACE_EXISTING);

            return destFileName;
        } catch (IOException e) {
			e.printStackTrace();
            return null;
        }
    }
}
