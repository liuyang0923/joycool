/*
 * Created on 2006-8-30
 *
 */
package net.joycool.wap.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.framework.CustomAction;

/**
 * 作者：bomb
 * 创建日期：2007-5-8
 * 说明：分页传递的参数。
 */
public class PagingBean {
	
    int totalCount;
    int totalPageCount;
    int currentPageIndex;
    int countPerPage;

    String prefixUrl;
    String param = null;
    
    public PagingBean(int currentPageIndex, int totalCount, int countPerPage) {
    	
    	init(currentPageIndex, totalCount, countPerPage);
    	this.param = "pageIndex";
    }

    // 支持go跳转的分页，go跳转的1表示第一页，当前页面的1表示第二页，是有差别的
    public PagingBean(CustomAction action, int totalCount, int countPerPage, String param, String go) {
    	int currentPageIndex = action.getParameterInt(param);
    	if(currentPageIndex == 0) {
    		int goPage = action.getParameterInt(go);
    		if(goPage > 0) {
    			currentPageIndex = goPage - 1;;
    		}
    	}
    	
    	init(currentPageIndex, totalCount, countPerPage);
    	this.param = param;
    }
    
    public PagingBean(CustomAction action, int totalCount, int countPerPage, String param) {
    	int currentPageIndex = action.getParameterInt(param);
    	
    	init(currentPageIndex, totalCount, countPerPage);
    	this.param = param;
    }
    
    public PagingBean() {
    	
    }
    
    public void init(int currentPageIndex, int totalCount, int countPerPage) {
        totalPageCount = (totalCount - 1) / countPerPage + 1;
    	
        if (currentPageIndex >= totalPageCount) {
        	currentPageIndex = totalPageCount - 1;
        }
        if (currentPageIndex < 0) {
        	currentPageIndex = 0;
        }
    	
    	this.currentPageIndex = currentPageIndex; 
    	this.totalCount = totalCount;
    	this.countPerPage = countPerPage;
    }
    
    /**
     * @return Returns the currentPageIndex.
     */
    public int getCurrentPageIndex() {
        return currentPageIndex;
    }
    /**
     * @param currentPageIndex The currentPageIndex to set.
     */
    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }
    /**
     * @return Returns the prefixUrl.
     */
    public String getPrefixUrl() {
        return prefixUrl;
    }
    /**
     * @param prefixUrl The prefixUrl to set.
     */
    public void setPrefixUrl(String prefixUrl) {
        this.prefixUrl = prefixUrl;
    }
    /**
     * @return Returns the totalCount.
     */
    public int getTotalCount() {
        return totalCount;
    }
    /**
     * @param totalCount The totalCount to set.
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    /**
     * @return Returns the totalPageCount.
     */
    public int getTotalPageCount() {
        return totalPageCount;
    }
    /**
     * @param totalPageCount The totalPageCount to set.
     */
    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
    
    public int getStartIndex() {
    	return countPerPage * currentPageIndex;
    }
    
    public int getEndIndex() {
    	return Math.min(countPerPage * currentPageIndex + countPerPage, totalCount);
    }
    // 逆向分页
    public int getStartIndexR() {
    	return totalCount - countPerPage * currentPageIndex - 1;
    }
    
    public int getEndIndexR() {
    	return totalCount - Math.min(countPerPage * currentPageIndex + countPerPage, totalCount) - 1;
    }

	/**
	 * @return Returns the param.
	 */
	public String getParam() {
		return param;
	}

	/**
	 * @param param The param to set.
	 */
	public void setParam(String param) {
		this.param = param;
	}
	
	public String shuzifenye(String prefixUrl, boolean addAnd, HttpServletResponse response) {
		return shuzifenye(prefixUrl, addAnd, "|", response);
	}
	
	public static int PAGE_COUNT = 5;
	public String shuzifenye(String prefixUrl, boolean addAnd, String separator,
			HttpServletResponse response) {
		int totalPageCount = getTotalPageCount();
		
		if (totalPageCount <= 1)
			return "";

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;";
		} else {
			prefixUrl += "?";
		}
		prefixUrl += getParam() + "=";
		
		int currentPageIndex = getCurrentPageIndex();
		
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			sb.append("<a href=\"" + (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i));
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (endIndex < totalPageCount - 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\"" + (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;</a>");
		}
		sb.append("<br/>");

		return sb.toString();
	}
	public String shuzifenye(String prefixUrl, boolean addAnd, String separator,
			HttpServletResponse response, int pagePer) {
		int totalPageCount = getTotalPageCount();
		
		if (totalPageCount <= 1)
			return "";

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;";
		} else {
			prefixUrl += "?";
		}
		prefixUrl += getParam() + "=";
		
		int currentPageIndex = getCurrentPageIndex();
		
		int startIndex = (currentPageIndex / pagePer) * pagePer;
		int endIndex = (currentPageIndex / pagePer + 1) * pagePer - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			sb.append("<a href=\"" + (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i));
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (endIndex < totalPageCount - 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\"" + (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;</a>");
		}
		sb.append("<br/>");

		return sb.toString();
	}

	public int getCountPerPage() {
		return countPerPage;
	}
}
