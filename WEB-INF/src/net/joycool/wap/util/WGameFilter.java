/*
 * Created on 2005-5-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.joycool.wap.util;

import java.io.IOException;
import java.util.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.bank.BankAction;
import net.joycool.wap.bean.ModuleBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;

/**
 * @author
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class WGameFilter implements Filter {
	public final static String CAS_FILTER_USER = "edu.yale.its.tp.cas.client.filter.user";

	public static String URL = "/user/login.jsp";

	// ----------------------------------------------------- Instance Variables

	protected FilterConfig filterConfig = null;
	public static IUserService userService = ServiceFactory.createUserService();
	
	/**
	 * Take this filter out of service.
	 */
	public void destroy() {

		this.filterConfig = null;

	}

	public void login(HttpServletResponse response, String url) throws IOException {
		response.sendRedirect((URL + "?backTo=" + url));
	}
	
	public static HashMap loginMap;	// 不同路径对应登录后返回的地址
		
	public static HashSet noLoginSet;	// 不需要的登陆的地址
	
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
	
	// 这个方法是自己调用，不给系统用
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain, HttpSession session) throws IOException, ServletException {
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// copy from positionfilter
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String uri = httpRequest.getRequestURI();
		String url = uri;
		UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
		if(loginUser != null){
			
			// 从MAP中得到位置信息
			// HashMap map = LoadResource.getPositionMap();
			// 得到请求uri的地址,不包括

			// 判断从第一个字符往后的uri中包含/标志前的字符个数
			int count = uri.indexOf("/", 1);
			// 如果个数等于0,让个数等于1
			if (count == -1) {
				count = 1;
			}
			// 截取字符串
			uri = uri.substring(1, count);
			// 从map中得到对应key的values
//			PositionBean positionBean = LoadResource.getPostionByUri(uri);
	
			// 请求页面的url
			String queryString = httpRequest.getQueryString();
			String thisUrl;
			if (queryString != null) {
				thisUrl = url + "?" + queryString;
			} else {
				thisUrl = url;
			}

			// 得到本页面的访问模块
			ModuleBean thisModule = PositionUtil.getModuleBean(url);

			// liuyi 2006-09-08 start 返回点击昵称前的页面功能
			String lastUrl = (String) session.getAttribute("lastUrl");

			// liuyi 2007-01-25 登录问题修改 start
			if (lastUrl == null) {
				lastUrl = BaseAction.INDEX_URL;
			}
			// liuyi 2007-01-25 登录问题修改 end
			// liuyi 2006-12-18 返回跳转修改 start
			if ("post".equalsIgnoreCase(httpRequest.getMethod()) || url.indexOf("/entry") != -1) {
				// do nothing
			} else if (PositionUtil.isClickUserName(url)) {
				session.setAttribute("pagebeforeclick", lastUrl);
			} else {
				session.setAttribute("lastUrl", thisUrl);
			}
			// liuyi 2006-12-18 返回跳转修改 end
			// liuyi 2006-09-08 end 返回点击昵称前的页面功能
	
			// 如果本页面模块属于指定模块
			if (thisModule != null) {
				// 得到本页面模块的url
				String thisModuleUrl;
				if(thisModule.getReturnUrl().length() == 0)
					thisModuleUrl = thisUrl;
				else
					thisModuleUrl = thisModule.getReturnUrl();
				
				String thisModuleImage = thisModule.toString();
				// 出了聊天室模块，则删除保存的roomid
				if (!thisModuleImage.equals("chat")) {
					session.removeAttribute("lastroomid");
					session.removeAttribute("currentroomid");
				} else { // 进入聊天室模块内，则更新roomid
					String currentRoomId = StringUtil.convertNull((String) session
							.getAttribute("currentroomid"));
					int thisRoomId = StringUtil.toInt(request
							.getParameter("roomId"));
					// 聊天大厅
					if (thisRoomId < 0) {
						thisRoomId = 0;
					}
					String rid = String.valueOf(thisRoomId);
					if (!rid.equals(currentRoomId)) {
						session.setAttribute("lastroomid", currentRoomId);
						session.setAttribute("currentroomid", rid);
						// liuyi 2006-09-07 start “返回聊天大厅”返回最近的那个聊天室
						session.setAttribute("chatroomid", rid);
						// liuyi 2006-09-07 end
					}
				}
//				// 得到上一个模块的url
//				String lastModuleUrl = (String) session
//						.getAttribute(Constants.LAST_MODULE_URL);
//				if (lastModuleUrl == null) {
//					lastModuleUrl = PositionUtil.getModuleReturnUrl(thisUrl,
//							thisUrl);
//					session.setAttribute(Constants.LAST_MODULE_URL, lastModuleUrl);
//				}
//				// 得到上一个模块
//				String lastModule = (String) session
//						.getAttribute(Constants.LAST_MODULE);
//				if (lastModule == null) {
//					lastModule = PositionUtil.getModule(lastModuleUrl);
//					session.setAttribute(Constants.LAST_MODULE, lastModule);
//				}
	
				// 得到当前模块的url
				String currentModuleUrl = (String) session
						.getAttribute(Constants.CURRENT_MODULE_URL);
				// 得到当前访问模块
				ModuleBean currentModule = (ModuleBean) session.getAttribute(Constants.CURRENT_MODULE);
				if (currentModule == null || currentModuleUrl == null) {
					session.setAttribute(Constants.CURRENT_MODULE, thisModule);
					session.setAttribute(Constants.CURRENT_MODULE_URL, thisModuleUrl);
					
//					 切换模块后加入访问历史
					if(thisModule.isFlagRecent())
						PositionUtil.addUserPositionHistory(loginUser.getId(), thisModule);
				} else {
	
					if (!thisModuleImage.equals(currentModule.toString())) { // 模块间切换
						session.setAttribute(Constants.LAST_MODULE, currentModule);
						session.setAttribute(Constants.LAST_MODULE_URL,
								currentModuleUrl);
						session.setAttribute(Constants.CURRENT_MODULE, thisModule);
						session.setAttribute(Constants.CURRENT_MODULE_URL,
								thisModuleUrl);
						
						// 切换模块后加入访问历史
						if(thisModule.isFlagRecent())
							PositionUtil.addUserPositionHistory(loginUser.getId(), thisModule);
					} else { // 模块内切换
						session.setAttribute(Constants.CURRENT_MODULE_URL,
								thisModuleUrl);
					}
				}
			}
	
			// 判断用户登陆信息是否存在
			if (loginUser != null) {
				
				String strUpdate = null;
				// 判断是否存在values,
				if (thisModule != null) {
					// 判断session中的position和当前位置ID是否一致,如果不一致更新数据库,更新session
					strUpdate = "position_id=" + thisModule.getId();
					if (thisModule.getId() != loginUser.getPositionId()) {
//						// 更新数据库position_id字段
//						if (thisModule.getId() == Constants.POSITION_CHAT) {
//							if (request.getParameter("roomId") != null)
//								strUpdate = strUpdate + ",sub_id="
//										+ StringUtil.toInt(request.getParameter("roomId"));
//							else
//								strUpdate = strUpdate + ",sub_id=0";
//						}
						boolean flag = true;
						if(thisModule.getId() == 11)
							flag = userService.updateOnlineUser(strUpdate,
									"user_id=" + loginUser.getId());
						if (flag) {
							// 如果更新成功,更新session中的position为当前位置
							loginUser.setPositionId(thisModule.getId());
							UserBean user = (UserBean) OnlineUtil
									.getOnlineBean(loginUser.getId() + "");
							user.setPositionId(thisModule.getId());
						}
					} else {// 如果不存在对应的values,更新用户位置为0,
	//					if (positionBean.getId() == Constants.POSITION_CHAT) {
	//						if (request.getParameter("roomId") != null) {
	//							int roomId = StringUtil.toId(request.getParameter("roomId"));
	//							strUpdate = strUpdate + ",sub_id=" + roomId;
	//							boolean flag = userService.updateOnlineUser(strUpdate,
	//									"user_id=" + loginUser.getId());
	//						}
	//					}
	
					}
				}// 如果不存在对应的values,更新用户位置为0,
				else {
					loginUser.setPositionId(0);
					// 判断session中的位置信息是否等于0,如果不等于0更新为0,
	//				if (loginUser.getPositionId() != 0) {
	//					// 更新数据库position_id字段
	//					boolean flag = userService.updateOnlineUser("position_id="
	//							+ 0 + ",sub_id=-1", "user_id=" + loginUser.getId());
	//					if (flag) {
	//						// 如果更新成功,更新session中的值为0
	//						loginUser.setPositionId(0);
	//						UserBean user = (UserBean) OnlineUtil
	//								.getOnlineBean(loginUser.getId() + "");
	//						user.setPositionId(0);
	//					}
	//				}
				}
			}
		}
		// copy from positionfilter end
		
//		String url = httpRequest.getRequestURI();

//		HttpSession session = httpRequest.getSession(false);
		// liuyi 2007-01-30 图片session问题 start

//		if (url.endsWith(".jsp") || url.endsWith("/") || url.endsWith(".do")) 
		{
//			UserBean loginUser = null;
//			if(session != null)
//				loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
			
			// 未登陆情况下，根据loginMap进行跳转到login.jsp。如果在nologinset里则不需要登陆
			if(loginUser == null) {
				int pathIndex = url.indexOf("/", 2);
				if(pathIndex != -1) {
					String path = url.substring(1, pathIndex);
					String to = (String)loginMap.get(path);
					if(to != null && !noLoginSet.contains(url) && url.indexOf("/index.") == -1) {
						if(to.length() == 0) {
							httpResponse.sendRedirect(URL);
							return;	
						}
						login(httpResponse, to);
						return;
					}
				}
			} else {
			
				if (url.startsWith("/stock2/")) {
					if (chenkUserBankPW(session)) {
						httpResponse.sendRedirect("/bank/bankPWCheck.jsp?backTo=/stock2/index.jsp");
						return;
					}
				} else if (url.startsWith("/auction/")) {		// 进入拍卖系统
					// 判断是否有银行密码
					if (chenkUserBankPW(session) && url.indexOf("propShop.jsp") < 0
							&& url.indexOf("propShow.jsp") < 0 && url.indexOf("propBuy.jsp") < 0) {
						httpResponse
								.sendRedirect("/bank/bankPWCheck.jsp?backTo=/auction/auctionHall.jsp");
						return;
					}
	
				} else if (url.startsWith("/bank/")) {
					if (url.indexOf("bankPW.jsp") < 0 && url.indexOf("bankPWHelp.jsp") < 0
							&& url.indexOf("bankPWRs.jsp") < 0 && url.indexOf("bankPWCheck.jsp") < 0
							&& url.indexOf("bankLogin.jsp") < 0 && chenkUserBankPW(session)) {
						httpResponse.sendRedirect("/bank/bankPWCheck.jsp?backTo=/bank/bank.jsp");
						return;
					}
				} else if (url.startsWith("/wgamepkbig/")) {
					if (chenkUserBankPW(session)) {
						httpResponse
								.sendRedirect("/bank/bankPWCheck.jsp?backTo=/wgamepkbig/ylhyk/index.jsp");
						return;
					}
				} 
			}
			if (url.startsWith("/guest/")){
				jc.guest.GuestUserInfo guestUser = (jc.guest.GuestUserInfo)session.getAttribute(jc.guest.GuestHallAction.GUEST_KEY);
				if (guestUser != null){
					jc.guest.OnlineGuest onlineGuest = (jc.guest.OnlineGuest)jc.guest.GuestHallAction.onlineGuestCache.sgt(new Integer(guestUser.getId()));
					if (onlineGuest == null){
						// 在线游客列表中没有，添加之。
						onlineGuest = new jc.guest.OnlineGuest(guestUser,System.currentTimeMillis(),url);
						jc.guest.GuestHallAction.onlineGuestCache.spt(new Integer(guestUser.getId()), onlineGuest);
					} else {
						// 在线游客列表中存在，更新之
						onlineGuest.setLastVisitTime(System.currentTimeMillis());
						onlineGuest.setLastVisitUrl(url);
					}
				} 
			}

//			if (loginUser != null) {
//				RequestDispatcher rd = request.getRequestDispatcher(uri + "2");
//				if (rd != null) {
//					rd.forward(request, response);
//					return;
//				}
//			}
		}
		boolean wap20 = SetCharacterEncodingFilter.isWap2(httpRequest, session);
		if(wap20) {		// 已经做了2.0版本的页面，自动转向
			String newURI = (String)wap20Map.get(url);	// 这里没有同步，所以其他地方都不能使用wap20Map，赋值除外
			if(newURI != null) {
				request.getRequestDispatcher(newURI).forward(request, response);
				return;
			}
		} else {	// 1.0页面自动转向，例如.jsp映射到Column.do
			String newURI = (String)wap10Map.get(url);	// 这里没有同步，所以其他地方都不能使用wap10Map，赋值除外
			if(newURI != null) {
				request.getRequestDispatcher(newURI).forward(request, response);
				return;
			}
		}
		chain.doFilter(request, response);
	}

    public static HashMap wap20Map = new HashMap(16);
    // 从数据库加载，每当有修改都要调用（效率无视）
    public static void loadWap20Map() {
    	HashMap map = new HashMap(16);
    	List objs = SqlUtil.getObjectsList("select url,to_url from url_redirect2", 5);
    	if(objs == null) {
    		if(wap20Map == null)
    			wap20Map = map;
    		return;
    	}
    	for(int i = 0;i < objs.size();i++) {
    		Object[] obj = (Object[])objs.get(i);
    		map.put(obj[0].toString(), obj[1].toString());
    	}
    	
    	wap20Map = map;
    }
    
    // wap10也提供强制跳转功能
    public static HashMap wap10Map = new HashMap(16);
    // 从数据库加载，每当有修改都要调用（效率无视）
    public static void loadWap10Map() {
    	HashMap map = new HashMap(16);
    	List objs = SqlUtil.getObjectsList("select url,to_url from url_redirect", 5);
    	if(objs == null) {
    		if(wap10Map == null)
    			wap10Map = map;
    		return;
    	}
    	for(int i = 0;i < objs.size();i++) {
    		Object[] obj = (Object[])objs.get(i);
    		map.put(obj[0].toString(), obj[1].toString());
    	}
    	
    	wap10Map = map;
    }
    
    public static void loadLoginMap() {
    	List objs = SqlUtil.getObjectsList("select url,entry from login_map", 0);
    	if(objs == null)
    		return;
    	HashMap map = new HashMap(objs.size());
    	for(int i = 0;i < objs.size();i++) {
    		Object[] obj = (Object[])objs.get(i);
    		map.put(obj[0].toString(), obj[1].toString());
    	}
    	
    	loginMap = map;
    }
    public static void loadNoLoginSet() {
    	List objs = SqlUtil.getObjectsList("select url from no_login", 0);
    	if(objs == null)
    		return;
    	HashSet set = new HashSet(objs.size());
    	for(int i = 0;i < objs.size();i++) {
    		Object[] obj = (Object[])objs.get(i);
    		set.add(obj[0].toString());
    	}
    	
    	noLoginSet = set;
    }
	
	public boolean chenkUserBankPW(HttpSession session) {
		return BankAction.chenkUserBankPW(session);
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
	
	public void init() {
		loadWap10Map();
		loadWap20Map();
		loadLoginMap();
		loadNoLoginSet();
	}

	// ------------------------------------------------------ Protected Methods

}
