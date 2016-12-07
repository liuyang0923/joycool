/*
 * Created on 2005-5-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.joycool.wap.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.spec.app.AppAction;

/**
 * @author
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SetCharacterEncodingFilter implements Filter {

    // ----------------------------------------------------- Instance Variables

    /**
     * The default character encoding to set for requests that pass through this
     * filter.
     */
    protected String encoding = null;

    /**
     * The filter configuration object we are associated with. If this value is
     * null, this filter instance is not currently configured.
     */
    protected FilterConfig filterConfig = null;

    /**
     * Should a character encoding specified by the client be ignored?
     */
    protected boolean ignore = true;

    // --------------------------------------------------------- Public Methods
    
    LoginFilter loginFilter = new LoginFilter();
    
    // 强制encodeURL，因为手机经常会断开wap连接，一旦断开，cookie丢失
    public static boolean forceEncode = true;
    /**
     * Take this filter out of service.
     */
    public void destroy() {

        this.encoding = null;
        this.filterConfig = null;

    }

    public static HashSet redirectDomain = null;
    public static String redirectToDomain = "http://gd.joycool.net";
    static {
    	redirectDomain = new HashSet();
	    redirectDomain.add("ad.joycool.net");
	    redirectDomain.add("d.joycool.net");
	    redirectDomain.add("gd.joycool.net");
	    redirectDomain.add("qq.joycool.net");
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
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        // Conditionally select and set the character encoding to be used
        if (ignore || (request.getCharacterEncoding() == null)) {
            String encoding = selectEncoding(request);            
            if (encoding != null)
                request.setCharacterEncoding(encoding);
        }
        // HttpServletRequest httpRequest = (HttpServletRequest) request;
        // if (httpRequest.getRequestURL().toString().indexOf("update.jsp") !=
        // -1) {
        // chain.doFilter(request, response);
        // return;
        // }
        // HttpServletResponse httpResponse = (HttpServletResponse) response;
        // httpResponse.sendRedirect("/update.jsp");
        // return;
        // Pass control on to the next filter
        
        //liuyi 2006-10-20 http请求日志 start 
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
		//String requestUrl = PageUtil.getCurrentPageURL(httpRequest);
		//LogUtil.logAccess(requestUrl);
		//liuyi 2006-10-20 http请求日志 end
		String uri = httpRequest.getRequestURI();
//		System.out.println(uri);
        if(uri.endsWith("/") || uri.endsWith(".jsp") || uri.endsWith(".do")) {
    		String ip = LoginFilter.getIp(httpRequest);
    		// 是否允许访问
    		if (!SecurityUtil.allowVisit(ip)) {
    			return;
    		}
    		
    		// 如果是错误的域名立刻跳转（免得产生新的session）
    		String serverName = httpRequest.getServerName();
        	if(!redirectDomain.contains(serverName) && !SqlUtil.isTest) {
        		String query = httpRequest.getQueryString();
        		if(query != null)
        			uri += "?" + query;
        		((HttpServletResponse)response).sendRedirect(redirectToDomain + uri);
        		return;
        	}
        	if(serverName.startsWith("211.157"))
        		return;
        	
    		HttpSession session = httpRequest.getSession(true);
    		boolean wap20 = isWap2(httpRequest, session);
    		
//    		if(wap20) {
//                WapServletResponseWrapper wsrw = new WapServletResponseWrapper(response, request);
//                if(AppAction.process(request, wsrw, uri, true)) {
//                	wsrw.process(false, false, true);
//                	return;
//                }
//    		} else {
//	        	if(AppAction.process(request, response, uri, false))
//	            	return;
//    		}
    		
        	if(AppAction.process(request, response, uri, wap20))
            	return;

    		boolean needEncode = "true".equals(session.getAttribute("allowVisit"));	// 是否需要删除所有空行空格
            WapServletResponseWrapper wsrw = new WapServletResponseWrapper(response, request);
    		//chain.doFilter(request, wsrw);
            loginFilter.doFilter(request, wsrw, chain, session, ip);
            // 如果是代理，都是ie访问的，只要开启了cookie就不再需要encodeURL
            wsrw.process(needEncode, wap20);

        } else {
        	if(AppAction.process(request, response, uri, false))
            	return;
        	((HttpServletResponse)response).setHeader("Cache-Control","max-age=8640000");	// 所有的图片、js, css都缓存100天
        	chain.doFilter(request, response);
        }
    }
    
    public static String wapEd = "wapEd";
    public static String wapEdCookie = "wed";		// 保存到cookie中的名字
    public static boolean  isWap2(HttpServletRequest request, HttpSession session)
	{
//    	if(SqlUtil.isTest) 		return true;
		Integer ii = (Integer)session.getAttribute(wapEd);	// wap版本
		if(ii != null) 
			return ii.intValue() == 2;
		
		CookieUtil cu = new CookieUtil(request);
		String cookie = cu.getCookieValue("wed");
		if(cookie != null && cookie.equals("2")) {
			session.setAttribute(wapEd, Integer.valueOf(2));
			return true;
		}

		String domain = request.getServerName();
		if(domain != null && domain.startsWith("3g.")){
			session.setAttribute(wapEd, Integer.valueOf(2));
			return true;
		}
		String accept = request.getHeader("accept");
		if(accept != null && accept.indexOf("wml") != -1) {
			session.setAttribute(wapEd, Integer.valueOf(1));
			return false;
		}
		String ua = request.getHeader("User-Agent");
		if(ua != null) {
			ua = ua.toLowerCase();
			if (ua.indexOf("safari") != -1 && (ua.indexOf("iphone") != -1 || ua.indexOf("android") != -1 || ua.indexOf("ipad") != -1 || ua.indexOf("ipod") != -1 || ua.indexOf("chrome") != -1) || ua.indexOf("firefox") != -1 || ua.indexOf("msie") != -1 && ua.indexOf("opera") == -1) {
				session.setAttribute(wapEd, Integer.valueOf(2));
				return true;
			}
		}

		session.setAttribute(wapEd, Integer.valueOf(1));
		return false;
	}

    /**
     * Place this filter into service.
     * 
     * @param filterConfig
     *            The filter configuration object
     */
    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");
        if (value == null)
            this.ignore = true;
        else if (value.equalsIgnoreCase("true"))
            this.ignore = true;
        else if (value.equalsIgnoreCase("yes"))
            this.ignore = true;
        else
            this.ignore = false;

        loginFilter.init();
        loadDomainList();
        
        Locale.setDefault(Locale.CHINA);
    }
    public static void loadDomainList() {
    	List list = SqlUtil.getObjectList("select name from domain_list order by id");
    	if(list == null || list.size() == 0)
    		return;
    	HashSet domainSet = new HashSet();
    	for(int i = 0;i < list.size();i++) {
    		Object obj = (Object)list.get(i);
    		domainSet.add(obj.toString());
    	}
    	redirectDomain = domainSet;
    	redirectToDomain = "http://" + list.get(0).toString();
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Select an appropriate character encoding to be used, based on the
     * characteristics of the current request and/or filter initialization
     * parameters. If no character encoding should be set, return
     * <code>null</code>.
     * <p>
     * The default implementation unconditionally returns the value configured
     * by the <strong>encoding </strong> initialization parameter for this
     * filter.
     * 
     * @param request
     *            The servlet request we are processing
     */
    protected String selectEncoding(ServletRequest request) {

        return (this.encoding);

    }

}
