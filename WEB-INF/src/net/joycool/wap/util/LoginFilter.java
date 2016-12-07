/*
 * Created on 2005-5-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.joycool.wap.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.AreaBean;
import net.joycool.wap.bean.OnlineBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.admin.AdminAction;
import net.joycool.wap.spec.admin.AdminUserBean;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class LoginFilter implements Filter {
	// ----------------------------------------------------- Instance Variables
	WGameFilter wgameFilter = new WGameFilter();
	
	protected FilterConfig filterConfig = null;

	public static boolean isRedirect = false;
	
	static IUserService userService = ServiceFactory.createUserService();

	static HashSet cutPv = new HashSet();
	static {
		cutPv.add("/tong/tongEmpolder.jsp");
		cutPv.add("/tong/shopEmpolder.jsp");
		cutPv.add("/tong/tongCityResult.jsp");
		cutPv.add("/tong/shop.jsp");
		cutPv.add("/tong/shop2.jsp");
		cutPv.add("/tong/hockshop.jsp");
		cutPv.add("/tong/tongCity.jsp");
		cutPv.add("/tong/tongCity2.jsp");
		cutPv.add("/job/hunt/huntArea.jsp");
		cutPv.add("/chat/hall.jsp");
		cutPv.add("/lswjs/index.jsp");
		cutPv.add("/cast/amsg.jsp");
		cutPv.add("/farm/map.jsp");
		cutPv.add("/bottom.jsp");
		cutPv.add("/idle.jsp");
		cutPv.add("/ping.jsp");
	}
	public static int forceUserId = 0;		// 强制跳转的user id
	public static String forceRedirectURL = null;		// 强制该session跳转到某个页面
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
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}
	
	// 这个方法是给自己用的
	public void doFilter(ServletRequest request, WapServletResponseWrapper response,
			FilterChain chain, HttpSession session, String ip) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String uri = httpRequest.getRequestURI();
		String url = uri;
//		if(url.indexOf("fpic") != -1 || url.indexOf("isNotMobile") != -1 || url.indexOf("fromlszx") != -1) {
//			CountUtil.countSpecial();
//			chain.doFilter(request, response);
//			return;
//		}
		boolean isAdmin = uri.startsWith("/jcadmin");
		String qs = httpRequest.getQueryString();
		if (qs != null) {
			url += "?" + qs;
		}
		boolean ismobile = SecurityUtil.isMobile(httpRequest);
		
		long executeStartTime = System.currentTimeMillis();
		
		// 还没取过手机号，试图取得手机号
		// liuyi 2006-12-20 登录注册修改 start
		
		String sessionId = "";
		UserBean loginUser = null;
		String mobile = null;
		
		if(session != null) {
			loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
			sessionId = session.getId();
			mobile = (String) session.getAttribute("userMobile");
		}
		if (loginUser == null && SecurityUtil.getMobile != 0 &&
				session.getAttribute("fetchMobile") == null && mobile == null) {
			// 一些特殊页面
			if (isAdmin
					|| url.indexOf("enter") != -1
					|| url.indexOf("friendlink") != -1
					|| url.startsWith("/wapIndex.jsp")
					|| url.startsWith("/Column.do?columnId=" + Constants.INDEX_ID)
					|| url.equals("/")
					|| url.equals("")
					|| url.startsWith("/entry")
					|| url.startsWith("/friend")
					|| url.startsWith("/ebook/index.jsp")
					|| url.startsWith("/game/index.jsp")
					|| url.startsWith("/image/index.jsp")
					|| url.startsWith("/Column.do?columnId=3480")
					|| url.startsWith("/Column.do?columnId=2705")
					|| url.startsWith("/Column.do?columnId=3925")
					|| url.startsWith("/huangye/")) {
				// do noting
			}
			// 取手机号返回
			else if (SecurityUtil.getPhone(httpRequest) != null) {
				// mobile = "13811885620";
				// mobile = "";
				mobile = SecurityUtil.getPhone(httpRequest);
				
				int linkId = StringUtil.toInt(request.getParameter("linkId"));
				if (linkId == -1) {
					linkId = 0;
				}
				// liuyi 2006-11-07 体坛周报wap版返回 start
				int unionId = StringUtil.toInt(request.getParameter("unionId"));
				if (unionId == -1) {
					unionId = 0;
				}
				//session.setAttribute("unionId", new Integer(unionId));
				// liuyi 2006-11-07 体坛周报wap版返回 end
				// 保存手机号
				if (mobile != null && !mobile.equals("")) {
					if (mobile.startsWith("86")) {
						mobile = mobile.substring(2);
					}
					// liuyi 2006-11-07 体坛周报wap版返回 start
					LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(httpRequest)
							+ ":" + ip + ":" + linkId
							+ ":" + url + ":" + unionId);
					// liuyi 2006-11-07 体坛周报wap版返回 end
					// liuyi 2006-09-19 书签功能 start
					session.setAttribute("UA", SecurityUtil.getUA(httpRequest));
					// liuyi 2006-09-19 书签功能 end
					// 自动注册，对于登陆的处理按四步走方案做
					// liuyi 2006-12-20 自动注册功能屏蔽 start
					if (mobile != null) {
						session.setAttribute("userMobile", mobile);
					}
					// liuyi 2006-12-26 注册流程修改 start
					if (mobile != null
							&& (mobile.startsWith("13") || mobile
									.startsWith("15"))) {
//						String sql = "select id from user_info where mobile='"
//								+ mobile + "'";
//						int id = SqlUtil.getIntResult(sql,
//								Constants.DBShortName);
//						if (id < 1) {
//							Util.updatePhoneNumber(httpRequest, mobile);
//						} else {// macq_2007-01-11_增加判断对可以获取到手机号的用户判断是否一小时内登录过,如果满足条件帮助用户自动登录_start
//							UserStatusBean bean = UserInfoUtil
//									.getUserStatus(id);
//							if (bean != null
//									&& httpRequest.getRequestURL().indexOf(
//											"/enter/enter.jsp") == -1) {
//								Date lastDatatime = DateUtil.parseDate(bean
//										.getLastLoginTime(),
//										DateUtil.normalTimeFormat);
//								Date currentDatatime = new Date();
//								long l1 = lastDatatime.getTime();
//								long l2 = currentDatatime.getTime();
//								long difference = l2 - l1;
//								if (difference < 60 * 60 * 1000) {
//									Util.updatePhoneNumber(httpRequest, mobile);
//								}
//							}
//						}// macq_2007-01-11_增加判断对可以获取到手机号的用户判断是否一小时内登录过,如果满足条件帮助用户自动登录_end
						//macq_2007-3-22_更改登录方式为自动登录（排除/enter/enter.jsp）_start
						if (httpRequest.getRequestURL().indexOf(
								"/enter/enter.jsp") == -1) {
							Util.updatePhoneNumber(httpRequest, mobile);
						}
						//macq_2007-3-22_更改登录方式为自动登录（排除/enter/enter.jsp）_end
					}
					// liuyi 2006-12-26 注册流程修改 end
					// liuyi 2006-12-20 自动注册功能屏蔽 end
				}
				session.setAttribute("fetchMobile", "true");
			}
			// 方法为post的页面
			else if ("post".equalsIgnoreCase(httpRequest.getMethod())) {
				// do nothing
			}
			// 取手机号
			else if (ismobile) {
				int linkId = StringUtil.toInt((String) session
						.getAttribute("linkId"));
				if (linkId == -1) {
					linkId = 0;
				}
				String backUrl = null;
				if (url.indexOf("?") == -1) {
					backUrl = url + "?linkId=" + linkId;
				} else {
					backUrl = url + "&linkId=" + linkId;
				}
				SecurityUtil.redirectGetMobile(httpResponse, backUrl);
				return;
			}
		}

		// 封禁手机
		String allowVisit = null;
		if(session != null)
			allowVisit = (String)session.getAttribute("allowVisit");
		if (allowVisit == null
				&& (mobile == null || mobile.equals(""))) {
			if (loginUser != null
					|| isAdmin
					|| uri.startsWith("/enter")
					|| url.equals("/")
					|| url.equals("")
					|| url.startsWith("/wapIndex.jsp")
					|| url.startsWith("/Column.do?columnId=" + Constants.INDEX_ID)) {
				// do nothing
			} else if(session != null){
				if (ismobile) {
					// 是手机访问
					session.setAttribute("allowVisit", "true");
				}
				// 提示用手机访问
				else if(SecurityUtil.isInnerIp(ip)) {	// 允许op访问的ip
					session.setAttribute("allowVisit", "op");
				} else {
					// liuyi 2007-01-25 登录问题修改 start
	//				request.getRequestDispatcher("/isNotMobile.jsp").forward(request, response);
					session.setAttribute("allowVisit", "false");
					// liuyi 2007-01-25 登录问题修改 end
//					return;
				}
			}
		}
		// liuyi 2006-12-20 登录注册修改 end

		/**
		 * 给北京WAP业务推广
		 */
		// System.out.println("mobile " + mobile + " start bjwap.");
		if (isRedirect) {
			if (mobile != null && session.getAttribute("bjWap") == null) {
				session.setAttribute("bjWap", "1");
				AreaBean area = AreaUtil.getAreaByMobile(mobile);
				// 是北京的手机号码
				if (area != null && "北京".equals(area.getCityname())) {
					boolean hasVisit = false;
					DbOperation dbOp = new DbOperation();
					dbOp.init();
					String query = "select id from bj_mobile_visit where mobile = '"
							+ mobile + "'";
					ResultSet rs = dbOp.executeQuery(query);
					try {
						// 已经访问过
						if (rs.next()) {
							hasVisit = true;
						}
						// 还没有访问过
						else {
							query = "insert into bj_mobile_visit set mobile = '"
									+ mobile + "', visit_datetime = now()";
							dbOp.executeUpdate(query);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					dbOp.release();
					// System.out.println("mobile " + mobile + " end bjwap.");
					if (!hasVisit) {
						httpResponse
								.sendRedirect(httpResponse
										.encodeRedirectURL("/bjWap.jsp"));
						return;
					}
				}
			}
		}
		StringBuilder logsb = null;
		// lbj_输出log_start
		if(isAdmin) {
			CountUtil.countSpecial();

			AdminUserBean adminUser = null;
			if (session != null)
				adminUser = (AdminUserBean) session.getAttribute("adminLogin");
			if (adminUser == null) {

				CookieUtil ck = new CookieUtil(httpRequest, httpResponse); // 根据cookie登陆
				String username = ck.getCookieValue("jopau");
				String password = ck.getCookieValue("jopap");

				if (password != null) {
					adminUser = AdminAction.getAdminUser("name='"
							+ StringUtil.toSql(username) + "'");
					if (adminUser != null && adminUser.getPassword().equals(password)) {
						session = httpRequest.getSession();
						session.setAttribute("adminLogin", adminUser);
					} else {
						httpResponse.sendRedirect("/enter/jcadmin/logout.jsp");
						return;
					}
				} else {
					httpResponse.sendRedirect("/enter/jcadmin/logout.jsp");
					return;
				}
			}
			LogUtil.logOperation(adminUser.getName() + ":" + ip + ":" + url);

		} else if (allowVisit != null && allowVisit.equals("op") || url.indexOf("entry") != -1) {
			CountUtil.countSpecial();
			
			// do nothing
		} else if(!cutPv.contains(uri)){
			// 登录且手机号不为空
			// liuyi 2006-11-07 体坛周报wap版返回 start
//			int unionId = 0;
//			Integer unionIdObject = (Integer) session.getAttribute("unionId");
//			if (unionIdObject != null) {
//				unionId = unionIdObject.intValue();
//			}
			// liuyi 2006-12-22 友链pv统计 start
			int linkId = 0;
			String logSession;
			if(session != null) {
				linkId = StringUtil.toId((String) session.getAttribute("linkId"));
				logSession = SecurityUtil.getSessionId(session);
			} else {
				logSession = "";
			}
			// liuyi 2006-12-11 统计后台修改 start
			logsb = new StringBuilder(128);
			if (loginUser != null && loginUser.getMobile() != null) {
				logsb.append(loginUser.getMobile());
				logsb.append(':');
				logsb.append(loginUser.getId());
				logsb.append(':');
				logsb.append(loginUser.getNickName().replace(':', '_'));
				logsb.append(':');
				logsb.append(ip);
				logsb.append(':');
				logsb.append(url);
				logsb.append(":0:");
				logsb.append(logSession);
				logsb.append(':');
				logsb.append(linkId);
			} else {
				logsb.append("null:0:null:");
				logsb.append(ip);
				logsb.append(':');
				logsb.append(url);
				logsb.append(":0:");
				logsb.append(logSession);
				logsb.append(':');
				logsb.append(linkId);
			}
			// liuyi 2006-12-22 友链pv统计 end
			// liuyi 2006-12-11 统计后台修改 end
			// liuyi 2006-11-07 体坛周报wap版返回 end
			// 访问统计
			CountUtil.count();
		} else {
			CountUtil.countOther();
		}

		// lbj_输出log_end
		if(session != null && !isAdmin) {
			// 已经登录
			if (loginUser != null) {
				// 判断如果ip段换了，且跳转到登陆页面
				String oldIp = (String)session.getAttribute("oldIp");
				if(oldIp == null) {
					// 初始化，
					if(httpRequest.isRequestedSessionIdFromCookie()) {
						session.setAttribute("oldIp", "");	// 如果sessionid是从cookie获得则以后无需判断了
					} else {
						String ipc = ip.substring(0, ip.lastIndexOf('.') + 1);
						session.setAttribute("oldIp", ipc);
					}
				} else if(oldIp.length() > 0){
					if(!ip.startsWith(oldIp)) {
						if(!uri.startsWith("/user/login.jsp")) {
							httpResponse.sendRedirect("/user/login.jsp;12345678");
							return;
						}
					}
				}
				if (loginUser.getId() == LogUtil.getLogUser()) {
					String logs = DateUtil.formatSqlDatetime(new Date()) + "-" + loginUser.getMobile() + "-" + loginUser.getId() + "-"
					+ loginUser.getNickName() + "-" + ip + "-" + url + "-" + sessionId;
					LogUtil.logSingleUser(logs);
				}
	
				String onlineKey = "" + loginUser.getId();
				UserBean onlineUser = (UserBean) JoycoolSessionListener.getOnlineUser(onlineKey);
				
				if(loginUser.getId() == forceUserId && forceUserId != 0) {
					forceUserId = 0;
//					httpResponse.sendRedirect((forceRedirectURL));
					httpRequest.getRequestDispatcher(forceRedirectURL).forward(request, response);
					return;
				}
				
				if(onlineUser != null && OnlineUtil.isUserKicked(onlineKey, sessionId) && !"op".equals(allowVisit)) {		// 判断是否被人踢下线
					JoycoolSessionListener.kickout(session);
					httpResponse.sendRedirect(("/kicked.jsp"));
					return;
				} else {
					// 如果onlineUser里没有该注册用户，则再次把它加入到onlineUser和jc_online_user表里，以得到准确的在线用户数；
					if (onlineUser == null) {
						onlineUser = loginUser;
						JoycoolSessionListener
								.updateOnlineStatus(onlineKey, onlineUser);
						JoycoolSessionListener.removeOnlineUser(sessionId);
						// liuyi 2006-12-01 程序优化 start
						// synchronized (onlineUser)
						{
							int onlineCount = userService.getOnlineUserCount(
											"user_id = " + onlineUser.getId());
							if (onlineCount < 1) {
								OnlineBean online = new OnlineBean();
								online.setUserId(onlineUser.getId());
								online.setSessionId(sessionId);
								// macq_2006-12-25_澧炲姞鍦ㄧ嚎鐢ㄦ埛鎵�灞炲府浼歩d_start(0涓烘病鏈夊府浼�,-1涓虹敵璇烽樁娈�)
								UserStatusBean us = UserInfoUtil
										.getUserStatus(loginUser.getId());
								online.setTongId(us.getTong());
								// macq_2006-12-25_澧炲姞鍦ㄧ嚎鐢ㄦ埛鎵�灞炲府浼歩d_end(0涓烘病鏈夊府浼�,-1涓虹敵璇烽樁娈�)
								userService.addOnlineUser(online);
							}
						}
						// liuyi 2006-12-01 程序优化 end
					} else {
						JoycoolSessionListener
								.updateOnlineStatus(onlineKey, onlineUser);
	//					if(!"op".equals(allowVisit))
	//						OnlineUtil.setOnlineSessionId(onlineKey, sessionId);
					}
					onlineUser.setLastVisitPage(httpRequest.getRequestURL() + "?"
							+ httpRequest.getQueryString());
				}
			}
			// 未登录、未注册
			else if (session.getAttribute(Constants.NOT_REGISTER_KEY) != null) {
				UserBean onlineUser = (UserBean) JoycoolSessionListener
						.getOnlineUser(sessionId);
				// 如果onlineUser里面没有该session的用户，就再次创建一个非注册用户加入到onlineUser里，以得到准确的在线用户数；
				if (onlineUser == null) {
					UserBean nullUser = createNullUser(httpRequest);
					JoycoolSessionListener.updateOnlineStatus(sessionId,
							nullUser);
					onlineUser = nullUser;
				} else {
					JoycoolSessionListener.updateOnlineStatus(sessionId,
							onlineUser);
				}
				onlineUser.setLastVisitPage(url);
			}
			// 根据手机号码/Cookie判断
			else {
				UserBean user = getUserByMobile(httpRequest);
	
				// 没登录或没注册
				if (user == null) {
					session
							.setAttribute(Constants.NOT_REGISTER_KEY,
									new Integer(1));
					UserBean nullUser = createNullUser(httpRequest);
					// ly 20060830 更新用户在线数据，以得到准确的在线用户数；
					JoycoolSessionListener.updateOnlineUser(httpRequest, nullUser);
					// end ly 20060830
				}
				// 已经注册或已经登录过
				else {
					user.setLastVisitPage(url);
					user.setIpAddress(ip);
					// ly 20060830 教育用户五步走第(零\一\二步)
					if (Util.getFlag().equals("zero")
							|| Util.getFlag().equals("first")
							|| (Util.getFlag().equals("second") && Util
									.getDayLoginUser(httpRequest, user) == 0)) {
						// 自动登陆
						JoycoolSessionListener.updateOnlineUser(httpRequest, user);
					} else {
						session.setAttribute(Constants.NOT_REGISTER_KEY,
								new Integer(1));
						UserBean nullUser = createNullUser(httpRequest);
						// ly 20060830 更新用户在线数据，以得到准确的在线用户数；
						JoycoolSessionListener.updateOnlineUser(httpRequest,
								nullUser);
					}
					// end ly 20060830 教育用户五步走第(零\一\二步)
	
				}
			}
			
			// 判断cookie是否存在，如果被移动网关删了，就加入jsessionid恢复
			Long ce = (Long)session.getAttribute("CookieEnable");
			
			if(ce == null) {
				if(httpRequest.isRequestedSessionIdFromCookie()) {	// 支持，cookie 启用
//					Cookie cookie = new Cookie("SID", sessionId);
//					cookie.setMaxAge(3600);
//					cookie.setPath("/");
//					if(!SqlUtil.isTest)
//						cookie.setDomain("joycool.net");
//					httpResponse.addCookie(cookie);
					session.setAttribute("CookieEnable", Long.valueOf(executeStartTime));
				}
			} else {
				if(executeStartTime - ce.longValue() > 900 * 1000) {	// cookie已经启用，但被移动删除
					Cookie cookie = new Cookie("SID", sessionId);
					cookie.setMaxAge(3600);
					cookie.setPath("/");
//					if(!SqlUtil.isTest)
//						cookie.setDomain("joycool.net");
					httpResponse.addCookie(cookie);
					session.setAttribute("CookieEnable", Long.valueOf(executeStartTime));
				}
			}
		}
		
		try {
			wgameFilter.doFilter(request, response, chain, session);
		} finally {
			if(logsb != null) {
				logsb.append(':');
				logsb.append(System.currentTimeMillis() - executeStartTime);
				LogUtil.logPv(logsb.toString());
			}
		}
	}

	public UserBean getUserByMobile(HttpServletRequest request) {
		String mobile = Util.getPhoneNumber(request);
		// 取不到手机号码
		if (mobile == null) {
			return null;
		}
		// 查询是否已注册
		else {
			String condition = "mobile = '" + mobile + "'";
			UserBean user = userService.getUser(condition);
			return user;
		}
	}

	public UserBean getUserByCookie(HttpServletRequest request) {
		// liuyi 2006-10-11 封禁从cookie获取用户信息 start
		Cookie cookie = getCookieByName(request, Constants.USER_COOKIE_NAME);
		if (cookie == null) {
			return null;
		}

		String userName = cookie.getValue();
		String condition = "user_name = '" + userName + "'";
		UserBean user = userService.getUser(condition);
		return user;

		// return null;
		// liuyi 2006-10-11 封禁从cookie获取用户信息 end
	}

	public Cookie getCookieByName(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (int i = 0; i < cookies.length; i++) {
			// System.out.println(cookies[i].getName() + ":" +
			// cookies[i].getValue());
			if (cookies[i].getName().equals(cookieName)) {
				return cookies[i];
			}
		}
		return null;
	}

	/**
	 * 作者：刘翊 创建日期：2006-08-21 根据request传进来的参数，创建一个代表非注册用户的空UserBean 参数列表：
	 * request：用户的http request 对应的jsp：无
	 */
	public UserBean createNullUser(HttpServletRequest httpRequest) {
		UserBean nullUser = new UserBean();
		nullUser.setIpAddress(httpRequest.getRemoteAddr());
		nullUser.setUserAgent(httpRequest.getHeader("User-Agent"));
		nullUser.setLastVisitPage(httpRequest.getRequestURI() + "?"
				+ httpRequest.getQueryString());
		return nullUser;
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
	
	public void init() throws ServletException {
		wgameFilter.init();
	}
	
	public static String getIp(HttpServletRequest httpRequest) {
		
		String realIp = httpRequest.getHeader("jcproxy");
		if(realIp != null)
			return realIp;
		return httpRequest.getRemoteAddr();
	}

	// ------------------------------------------------------ Protected Methods

	// 单个session的点击计数 1-5
	public static class SessionClick {
		public long time;
		public int count;
		public SessionClick() {
			time = System.currentTimeMillis();
		}
	};
	// 单个ip的点击计数
	public static class IpClick {
		long time;
		int count;
		int maxCount;
		public IpClick(int maxCount) {
			time = System.currentTimeMillis();
			this.maxCount = maxCount;
		}
	};
}
