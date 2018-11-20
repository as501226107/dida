/**
 * 
 */
package com.dream.utils;

import java.util.List;

public class PageHelper<T> {

	private int pageIndex;
	private int pageSize; 
	private int totalCount; 
	private int totalPage; 
	private List<T> list; 
	private Object bean; 

	private int startNum; 
	private int endNum; 

	public PageHelper(int pageIndex, int pageSize, int totalCount, List<T> list, Object bean) {
		super();
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.list = list;
		this.bean = bean;
		this.totalPage = (totalCount % pageSize == 0) ? (totalCount / pageSize) : (totalCount / pageSize + 1);

		//校验pageIndex是否有效

		if(pageIndex>totalPage) {
			this.pageIndex=totalPage;
		}
		else if(pageIndex<1) {
			this.pageIndex=1;
		}else {
			this.pageIndex=pageIndex;
		}
		if (totalPage <= 10) {
			this.startNum = 1;
			this.endNum = totalPage;
		} else {
			this.startNum = pageIndex - 4;
			this.endNum = pageIndex + 5;
			if (this.startNum < 1) {
				this.startNum = 1;
				this.endNum = 10;
			}
			else if (this.endNum > totalPage) {
				this.endNum = totalPage;
				this.startNum = totalPage - 9;
			}
			if (this.endNum >= totalPage) {
				this.endNum = totalPage;
			}
		}

	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public int getEndNum() {
		return endNum;
	}

	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}
}
