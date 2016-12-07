package net.joycool.wap.util;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class TimeFilter implements Filter {
	private FilterConfig config = null;

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	public void destroy() {
		config = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		long startTime = System.currentTimeMillis();
		chain.doFilter(request, response);
		long endTime = System.currentTimeMillis();
		
		String url = "null";
		if (request instanceof HttpServletRequest) {
			url = PageUtil.getCurrentPageURL((HttpServletRequest) request);
		}
//		LogUtil.logTime(url + ":" + (endTime - startTime) + "ms");
	}
}
