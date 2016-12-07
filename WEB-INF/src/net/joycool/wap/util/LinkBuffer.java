package net.joycool.wap.util;

import javax.servlet.http.HttpServletResponse;

/**
 * @author bomb
 * 链接矩阵，一行四个？
 */
public class LinkBuffer {
	StringBuilder sb = new StringBuilder();
	int col = 0;
	int maxCol = 4;
	HttpServletResponse response = null;
	String separator = null;
	
	public LinkBuffer(HttpServletResponse response) {
		this.response = response;
		separator = "|";
		maxCol = 4;
	}
	
	public  LinkBuffer(HttpServletResponse response, String separator, int maxCol) {
		this.response = response;
		this.separator = separator;
		this.maxCol = maxCol;
	}
	
	public void appendLink(String url, String name) {
		if(col >= maxCol) {
			sb.append("<br/>");
			col = 0;
		}
		
		if(col != 0)
			sb.append(separator);
		sb.append("<a href=\"");
		sb.append((url));
		sb.append("\">");
		sb.append(name);
		sb.append("</a>");
		
		col++;
	}
	
	public void appendWml(String wml) {
		if(col >= maxCol) {
			sb.append("<br/>");
			col = 0;
		}
		
		if(col != 0)
			sb.append(separator);
		sb.append(wml);
		
		col++;
	}
	
	public String toString() {
		return sb.toString();
	}
}
