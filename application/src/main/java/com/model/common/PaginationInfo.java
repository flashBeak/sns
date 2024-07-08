/*
 * Copyright 2008-2009 MOPAS(Ministry of Public Administration and Security).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.model.common;

import java.util.HashMap;
import java.util.Map;

import com.bussiness.Utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * PaginationInfo.java
 * <p/><b>NOTE:</b><pre>
 *                페이징 처리를 위한 데이터가 담기는 빈.
 *                페이징 처리에 필요한 데이터를 Required Fields, Not Required Fields 로 나누었다.
 *                
 *                Required Fields
 *                : 사용자가 입력해야 하는 필드값이다.
 *                currentPageNo : 현재 페이지 번호.
 *                recordCountPerPage : 한 페이지당 게시되는 게시물 건 수.
 *                pageSize : 페이지 리스트에 게시되는 페이지 건수.
 *                totalRecordCount : 전체 게시물 건 수.
 *                
 *                Not Required Fields
 *                : 사용자가 입력한 Required Fields 값을 바탕으로 계산하여 정해지는 값이다.
 *                totalPageCount: 페이지 개수.
 *                firstPageNoOnPageList : 페이지 리스트의 첫 페이지 번호.
 *                lastPageNoOnPageList : 페이지 리스트의 마지막 페이지 번호.
 *                firstRecordIndex : 페이징 SQL의 조건절에 사용되는 시작 rownum. 
 *                lastRecordIndex : 페이징 SQL의 조건절에 사용되는 마지막 rownum.
 *                
 *                페이징 Custom 태그인 &lt;ui:pagination&gt; 사용시에 paginationInfo 필드에 PaginationInfo 객체를 값으로 주어야 한다.
 *                </pre>
 *<pre class="code">
 *&lt;ui:pagination paginationInfo = "${paginationInfo}"
 *     type="image"
 *     jsFunction="linkPage"
 *&gt;
 * 
 * @author hslee
 * @since 2017.10.11
 * @version 1.0
 * @see
 *
 */
public class PaginationInfo {

	/**
	 * Required Fields
	 * - 이 필드들은 페이징 계산을 위해 반드시 입력되어야 하는 필드 값들이다.  
	 * 
	 * currentPageNo : 현재 페이지 번호
	 * recordCountPerPage : 한 페이지당 게시되는 게시물 건 수
	 * pageSize : 페이지 리스트에 게시되는 페이지 건수,
	 * totalRecordCount : 전체 게시물 건 수. 
	 */

	private int currentPageNo;
	private int recordCountPerPage;
	private int pageSize;
	private int totalRecordCount;
	
	public String html;
	Map<String, String> pageMap = new HashMap<String, String>();	// pagination 구성 할때, page 번호의 url에 ?search=test와 같이 구성하기 위한 파라미터들

	public Map<String, String> getPageMap() {
		return pageMap;
	}
	public void setPageParam(String key, String value) {
		pageMap.put(key, value);
	}
	
	public void setPageParam(HttpServletRequest request, String [] param) {
		for (int i = 0; i < param.length; i++) {
			String item = request.getParameter(param[i]);
			pageMap.put(param[i], item);
		}
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	} 

	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}

	public void setRecordCountPerPage(String recordCountPerPageStr) {
		this.recordCountPerPage = Utils.parseInt(recordCountPerPageStr, recordCountPerPage);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(String currentPageNoStr) {
		if (currentPageNoStr == null) {
			currentPageNo = 1;
			return;
		}
		
		currentPageNo = Integer.parseInt(currentPageNoStr);
		if (currentPageNo < 1 )	// added by hslee 페이지 번호가 1보다 작을 수 없다.
			currentPageNo = 1;
	}

	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	/**
	 * Not Required Fields
	 * - 이 필드들은 Required Fields 값을 바탕으로 계산해서 정해지는 필드 값이다.
	 * 
	 * totalPageCount: 페이지 개수
	 * firstPageNoOnPageList : 페이지 리스트의 첫 페이지 번호
	 * lastPageNoOnPageList : 페이지 리스트의 마지막 페이지 번호
	 * firstRecordIndex : 페이징 SQL의 조건절에 사용되는 시작 rownum. 
	 * lastRecordIndex : 페이징 SQL의 조건절에 사용되는 마지막 rownum.
	 */

	private int totalPageCount;
	private int firstPageNoOnPageList;
	private int lastPageNoOnPageList;
	private int firstRecordIndex;
	private int lastRecordIndex;

	public PaginationInfo() {
		recordCountPerPage = 15;
    	pageSize = 15;
	}
	
	public int getTotalPageCount() {
		totalPageCount = ((getTotalRecordCount() - 1) / getRecordCountPerPage()) + 1;
		return totalPageCount;
	}

	public int getFirstPageNo() {
		return 1;
	}

	public int getLastPageNo() {
		return getTotalPageCount();
	}

	public int getFirstPageNoOnPageList() {
		firstPageNoOnPageList = ((getCurrentPageNo() - 1) / getPageSize()) * getPageSize() + 1;
		return firstPageNoOnPageList;
	}

	public int getLastPageNoOnPageList() {
		lastPageNoOnPageList = getFirstPageNoOnPageList() + getPageSize() - 1;
		if (lastPageNoOnPageList > getTotalPageCount()) {
			lastPageNoOnPageList = getTotalPageCount();
		}
		return lastPageNoOnPageList;
	}

	public int getFirstRecordIndex() {
		firstRecordIndex = (getCurrentPageNo() - 1) * getRecordCountPerPage();
		return firstRecordIndex;
	}

	public int getLastRecordIndex() {
		lastRecordIndex = getCurrentPageNo() * getRecordCountPerPage();
		return lastRecordIndex;
	}
}
