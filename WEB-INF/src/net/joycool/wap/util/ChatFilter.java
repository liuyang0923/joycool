package net.joycool.wap.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;

public class ChatFilter implements Filter {
	public final static String CAS_FILTER_USER = "edu.yale.its.tp.cas.client.filter.user";

	// ----------------------------------------------------- Instance Variables

	protected FilterConfig filterConfig = null;

	/**
	 * Take this filter out of service.
	 */
	public void destroy() {

		this.filterConfig = null;

	}

	/**
	 * Select and set (if specified) the character encoding to be used to
	 * interpret request parameters for this request.
	 * 
	 * @param request
	 *            The servlet request we are processing
	 * @param result
	 *            The servlet response we are creating
	 * @param chain
	 *            The filter chain we are processing
	 * 
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpSession session = httpRequest.getSession(false);
		if (session != null) {
			UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
			if (loginUser != null) {
				String url = httpRequest.getRequestURI();
				IChatService chatService = ServiceFactory.createChatService();
				boolean flag = chatService.getForBID("user_id=" + loginUser.getId());
				if (url.startsWith("/chat/")) {
					if (flag) {
						chatService.deltetForBID("user_id=" + loginUser.getId());
						BaseAction.sendRedirect("/chat/hall.jsp?roomId=1", httpResponse);
						return;
					}
				}
			}
		}
		chain.doFilter(request, response);
	}
	    
	/**
	 * Place this filter into service.
	 * 
	 * @param filterConfig
	 *            The filter configuration object
	 */
	public void init(FilterConfig filterConfig) throws ServletException {

		this.filterConfig = filterConfig;
	}

	// ------------------------------------------------------ Protected Methods
}