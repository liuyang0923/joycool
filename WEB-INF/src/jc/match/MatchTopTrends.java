package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 * 置顶动态
 */
public class MatchTopTrends {
	int id;
	String content;
	String links;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public String getContentWml() {
		return StringUtil.toWml(content);
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}
}
