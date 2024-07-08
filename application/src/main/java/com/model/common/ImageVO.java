package com.model.common;


public class ImageVO {

	public String id;
	
	public String showingFileName;
	public String realFileName;

	public String extension;	// 파일 확장자	egov와 맞추기 위해 추가함
	public int size;
	
	public ImageVO() {
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShowingFileName() {
		return showingFileName;
	}
	public void setShowingFileName(String showingFileName) {
		this.showingFileName = showingFileName;
	}
	public String getRealFileName() {
		return realFileName;
	}
	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	
}
