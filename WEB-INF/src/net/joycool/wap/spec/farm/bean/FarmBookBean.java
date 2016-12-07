package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 大地图书籍
 * @datetime:1007-10-24
 */
public class FarmBookBean extends MapDataBean{
	int id;
	String name;		// 书名
	String content;		// 书内容（用||间隔）
	String[] pages;

	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
		pages = content.split("\\|\\|");
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	public int getPageCount() {
		return pages.length;
	}
		
	public String getPage(int page) {
		if(pages == null)
			return "";
		if(page < 0 || page >= pages.length)
			return pages[0];
		return pages[page];
	}

	public String getLink(HttpServletResponse response) {
		return "《" + "<a href=\"" + ("book.jsp?id=" + id) + "\">" + name + "</a>" + "》";
	}
	public String getEditLink(HttpServletResponse response) {
		return "《" + "<a href=\"" + ("editBook.jsp?id=" + id) + "\">" + name + "</a>" + "》";
	}
}
