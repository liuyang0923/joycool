/*
 * Created on 2005-11-24
 *
 */
package net.joycool.wap.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.PagingBean;

/**
 * @author lbj
 * 
 */
public class PageUtil {
	public static int PAGE_COUNT = 5;

	public static String shuzifenye(int totalPageCount, int currentPageIndex,
			String prefixUrl, boolean addAnd, String separator,
			HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;pageIndex=";
		} else {
			prefixUrl += "?pageIndex=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb.append("<a href=\""
					+ (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;&lt;</a>");
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
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}
	
	public static String shuzifenye(PagingBean page,
			String prefixUrl, boolean addAnd, String separator,
			HttpServletResponse response) {
		int totalPageCount = page.getTotalPageCount();
		
		if (totalPageCount <= 1)
			return "";

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;";
		} else {
			prefixUrl += "?";
		}
		prefixUrl += page.getParam() + "=";
		
		int currentPageIndex = page.getCurrentPageIndex();
		
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			sb.append("<a href=\"" + (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;&lt;</a>");
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
			sb.append("\">&gt;&gt;</a>");
		}
		sb.append("<br/>");

		return sb.toString();
	}

	public static String shuzifenye1(int totalPageCount, int currentPageIndex,
			String prefixUrl, boolean addAnd, String separator,
			HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;pageIndex1=";
		} else {
			prefixUrl += "?pageIndex1=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb.append("<a href=\""
					+ (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;&lt;</a>");
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
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}

	public static String shangxiafenye(int totalPageCount,
			int currentPageIndex, String prefixUrl, boolean addAnd,
			String separator, HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;pageIndex=";
		} else {
			prefixUrl += "?pageIndex=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;

		if (currentPageIndex > 0) {
			hasPrevPage = 1;
		}
		if (currentPageIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasNextPage == 1) {
			sb.append("<a href=\""
					+ (prefixUrl + (currentPageIndex + 1)));
			sb.append("\">下一页</a>");
		}

		if (hasPrevPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (currentPageIndex - 1)));
			sb.append("\">上一页</a>");
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		String s = PageUtil.shuzifenye(100, 10, "test.jsp", true, "|", null);
	}

	public static String getCurrentPageURL(HttpServletRequest request) {
		String queryString = request.getQueryString();
		if (queryString != null) {
			return request.getRequestURI() + "?" + queryString;
		} else {
			return request.getRequestURI();
		}
	}

	public static String getBackTo(HttpServletRequest request) {
		try {
			return URLEncoder.encode(StringUtil.getCurrentPageURL(request),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertBackTo(String backTo) {
		backTo = backTo.replace("&", "&amp;");
		return backTo;
	}

	public static String encodeBackTo(String backTo) {
		try {
			backTo = URLEncoder.encode(backTo, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return backTo;
	}

	/**
	 * fanys 2006-09-19 得到当前总的页数
	 * 
	 * @param pageSize
	 *            一页显示多少条
	 * @param itemCount
	 *            总的条数
	 * @return
	 */
	public static int getPageCount(int pageSize, int itemCount) {
		int pageCount = 0;
		pageCount = itemCount / pageSize;
		if (itemCount % pageSize > 0)
			pageCount++;
		return pageCount;
	}

	/**
	 * fanys 2006-09-19 得到当前页号
	 * 
	 * @param pageIndex
	 *            页号
	 * @param pageCount
	 *            总的页数
	 * @return
	 */
	public static int getPageIndex(int pageIndex, int pageCount) {
		pageIndex = Math.max(0, pageIndex);
		pageIndex = Math.min(pageIndex, pageCount - 1);
		return pageIndex;

	}

	/**
	 * fanys 2006-09-19 该分页的页面参数是通过postfield来传递的
	 * 
	 * @param totalPageCount
	 *            总页数
	 * @param currentPageIndex
	 *            当前页号 0对应第1页
	 * @param prefixUrl
	 *            jsp页面地址，例如roomList.jsp
	 * @param pageParamName
	 *            就是网页之间传递的参数名
	 * @param addAnd
	 *            参数方式，是第一个参数呢？还是以后的参数，第一个参数应该以？开头，后面的参数以&开头
	 * @param
	 *            endUrl在参数pageParamName后面的其它参数，如传&amp;backTo="/chat/hall.jsp?roomId=85"
	 * @param separator
	 *            页之间分隔符号,如以”|“为分隔符号，”1|2|3|4|5“
	 * @param response
	 *            页面的相应，传递当前jsp的response即可
	 * 
	 * @return
	 */
	public String pagination(int totalPageCount, int currentPageIndex,
			String prefixUrl, String pageParamName, boolean addAnd,
			String separator, String endUrl, HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;" + pageParamName + "=";
		} else {
			prefixUrl += "?" + pageParamName + "=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb
					.append("<a href=\""
							+ (prefixUrl + (startIndex - 1))
							+ endUrl);
			sb.append("\">&lt;&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i)
						+ endUrl);
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}

}
