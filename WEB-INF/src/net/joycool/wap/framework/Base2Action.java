package net.joycool.wap.framework;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhouj
 * wap20专用的头尾等
 */
public class Base2Action {
	public static void getPageTop(Writer out, String title) throws IOException {
		out.write(topStart);
		out.write(title);
		out.write(topSecond);
	}
	
	public static void getPageBottom(Writer out) throws IOException {
		out.write(bottomEnd);
	}
	
	public static void getTop(HttpServletRequest request, HttpServletResponse response, Writer out) throws IOException {
		out.write(BaseAction.getTop(request, response));
	}
	
	public static void getBottom(HttpServletRequest request, HttpServletResponse response, Writer out) throws IOException {
		out.write(BaseAction.getBottom(request, response));
	}
	
	public static void getBottomShort(HttpServletRequest request, HttpServletResponse response, Writer out) throws IOException {
		out.write(BaseAction.getBottomShort(request, response));
	}
	
	public static String topStart = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"HTML\" \"-//W3C//DTD HTML 4.0 Transitional//EN\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta name=\"viewport\" content=\"width=240,initial-scale=1.29,user-scalable=0\"/><meta name=\"MobileOptimized\" content=\"236\"/><title>乐酷游戏社区</title><link href=\"/proxy.css\" rel=\"stylesheet\" type=\"text/css\"/></head><body><script src=\"/proxy.js\">;</script><div align=\"center\" class=\"div1\"><font color=\"yellow\">";
	public static String topSecond = "</font></div><div class=\"div2\">";
	public static String bottomEnd = "</div></body></html>";
}
