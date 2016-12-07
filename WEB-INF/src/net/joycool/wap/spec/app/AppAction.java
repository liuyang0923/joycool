package net.joycool.wap.spec.app;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.bean.ModuleBean;
import net.joycool.wap.bean.OnlineBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.BaseAction2;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.JaLineServiceImpl;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.CountUtil;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.PositionUtil;
import net.joycool.wap.util.SecurityUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.WapServletResponseWrapper;
import net.joycool.wap.util.LoginFilter.SessionClick;

public class AppAction extends CustomAction {

	static IUserService userService = ServiceFactory.createUserService();
	static IJaLineService lineService = new JaLineServiceImpl();
	
	public static ICacheMap appUserCache = CacheManage.appUser;
	public static AppService service = new AppService();

	public static Map appMap;	//	dir -> app bean
	public static Map appMap2;	//	id -> app bean
	
	public static int MAX_REPLY_COUNT = 100;
	public static int MAX_TYPE_COUNT = 100;	// 分类的id最大是99
	public static AppTypeBean[] types;
	public static List typeList;
	
	public static byte[] lock = new byte[0];
	UserBean loginUser;

	public AppAction() {
	}

	public AppAction(HttpServletRequest request) {
		super(request);

	}

	public static void initAppMap() {
		if(appMap != null)
			return;
		synchronized(lock) {
			if(appMap != null)
				return;
			LoadResource.loadPosition();
			List list = service.getAppList("1");
			appMap = new HashMap(list.size());
			appMap2 = new HashMap(list.size());
			for(int i = 0;i < list.size();i++) {
				AppBean ab = (AppBean)list.get(i);
				appMap.put(ab.getDir(), ab);
				appMap2.put(new Integer(ab.getId()), ab);
				// 添加到position里，id是appId + 1000
				synchronized(LoadResource.class) {
					LoadResource.addPosition(ab.getId() + 1000, ab.getName2(), ab.getDirFull(), null);
				}
			}
			// 初始化类型
			typeList = service.getTypeList("1 order by seq");
			types = new AppTypeBean[MAX_TYPE_COUNT];
			for(int i = 0;i < typeList.size();i++) {
				AppTypeBean at = (AppTypeBean)typeList.get(i);
				types[at.getId()] = at;
			}
			types[0] = new AppTypeBean();
			types[0].setName("(无)");
			
		}
	}

	public static AppTypeBean getType(int type) {
		return types[type];
	}
	public static List getTypeList() {
		initAppMap();
		return typeList;
	}
	public static Map getAppMap() {
		initAppMap();
		return appMap;
	}
	
	public static Map getAppMap2() {
		initAppMap();
		return appMap2;
	}
	public static AppBean getApp(int id) {
		return (AppBean)getAppMap2().get(new Integer(id));
	}
	public static void addApp(AppBean ab) {
		initAppMap();
		if(service.addApp(ab)) {
			appMap.put(ab.getDir(), ab);
			appMap2.put(new Integer(ab.getId()), ab);
			LoadResource.addPosition(ab.getId() + 1000, ab.getName2(), ab.getDirFull(), null);
		}
	}
	public static void updateApp(AppBean ab) {
		initAppMap();
		service.updateApp(ab);
		LoadResource.addPosition(ab.getId() + 1000, ab.getName2(), ab.getDirFull(), null);
	}
	// 重新载入app数据
	public static void reload() {
		appMap = null;
		initAppMap();
	}
	
	static Pattern jcPattern = Pattern.compile("<jc:([0-9a-z]+)/>");
	// 如果被重定向则返回true
	public static boolean process(ServletRequest req, ServletResponse res, String uri, boolean wap20) throws IOException {
		Map map = getAppMap();
		if(map.size() == 0)
			return false;
		
		HttpServletRequest request = (HttpServletRequest)req;
		
		int pos = uri.indexOf('/', 1);
		if(pos == -1)
			return false;
		
		String dir = uri.substring(1, pos);
		
		AppBean ab = (AppBean)map.get(dir);
		if(ab == null || ab.isFlagLocal())	// 没有找到对应的插件，或者是本地插件
			return false;
		
		String append = uri.substring(pos + 1);
		String url = ab.getUrl() + append;
		
		String query = request.getQueryString();
		String uri2 = uri;
		if(query != null) {
			url += "?" + query;
			uri += "?" + query;
		}
			
		
		HttpServletResponse response = (HttpServletResponse)res;
		HttpSession session = request.getSession(false);
		if(session == null) {
			response.sendRedirect(response.encodeURL("/user/login.jsp"));
			return true;
		}
		UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
		if(loginUser == null) {
			response.sendRedirect(response.encodeURL("/user/login.jsp"));
			return true;
		}

		if(!ab.isFlagDirect()) {
			Map appUserMap = getAppUserMap(loginUser.getId());
			if(!appUserMap.containsKey(new Integer(ab.getId()))) {
				// 没有安装该组件
				response.sendRedirect(response.encodeURL("/apps/appi.jsp?id=" + ab.getId()));
				return true;
			}
		}
		
		if(ab.isFlagOffline()) {
			if(ab.isFlagDirect()) {
				response.setHeader("Cache-Control","no-cache");
				if(wap20) {
					response.setContentType("text/html;charset=utf-8");
					PrintWriter writer = response.getWriter();
					BaseAction2.getPageTop(writer, "无法访问");
					writer.append("本功能暂停服务,请稍候再试.<br/>————————<br/>");
					writer.append("<a href=\"/bottom.jsp\">ME</a>|");
					writer.append("<a href=\"/lswjs/index.jsp\">导航</a>|");
					writer.append("<a href=\"/wapIndex.jsp\">乐酷首页</a><br/>");
					writer.append(DateUtil.getCurrentDatetimeAsStr());
					BaseAction2.getPageBottom(writer);
				} else {
					response.setContentType("text/vnd.wap.wml;charset=utf-8");
					PrintWriter writer = response.getWriter();
					writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\" \"http://www.wapforum.org/DTD/wml_1.1.xml\"><wml><card title=\"无法访问\"><p>");
					writer.append("本功能暂停服务,请稍候再试.<br/>wap.joycool.net<br/>");
					writer.append("<a href=\"").append(response.encodeURL("/bottom.jsp")).append("\">ME</a>|");
					writer.append("<a href=\"").append(response.encodeURL("/lswjs/index.jsp")).append("\">导航</a>|");
					writer.append("<a href=\"").append(response.encodeURL("/wapIndex.jsp")).append("\">乐酷首页</a><br/>");
					writer.append(DateUtil.getCurrentDatetimeAsStr());
					writer.append("</p></card></wml>");
				}
				CountUtil.countOther();
				return true;
			}
			response.sendRedirect(response.encodeURL("/apps/app.jsp?id=" + ab.getId()));
			return true;
		}
		// 开始访问
		long executeStartTime = System.currentTimeMillis();
		
		boolean ismobile = SecurityUtil.isMobile(request);
		if(!ismobile) {
			if(!ab.isFlagAllow()) {	// 该插件不允许电脑访问
				response.sendRedirect(response.encodeURL("/apps/app.jsp?id=" + ab.getId()));
				return true;
			}
		}
		// 根据插件，设置user position
		UserBean ou = (UserBean) OnlineUtil.getOnlineBean(String.valueOf(loginUser.getId()));
		if(ou != null)
			ou.setPositionId(ab.getId() + 1000);
		
		// 判断jc_module所属模块
		ModuleBean thisModule = PositionUtil.getModuleBean(ab.getDirFull());
		if(thisModule != null) {
			ModuleBean module = (ModuleBean) session.getAttribute(Constants.CURRENT_MODULE);
			if(module == null || !module.equals(thisModule)) {
				session.setAttribute(Constants.CURRENT_MODULE, thisModule);
				session.setAttribute(Constants.CURRENT_MODULE_URL, ab.getDirFull());
				PositionUtil.addUserPositionHistory(loginUser.getId(), thisModule);
			}
		} else {	// 如果没有该模块，添加一个
			thisModule = new ModuleBean();
			thisModule.setId(ab.getId() + 1000);
			thisModule.setImage(ab.getDirFull());
			thisModule.setName(ab.getName());
			thisModule.setReturnUrl(ab.getDirFull());
			thisModule.setUrlPattern(ab.getDirFull());
			thisModule.setEntryUrl(ab.getDirFull());
			thisModule.setPosName(ab.getName2());
			thisModule.setShortName(ab.getShortName());
			PositionUtil.addModule(thisModule);
			session.setAttribute(Constants.CURRENT_MODULE, thisModule);
			session.setAttribute(Constants.CURRENT_MODULE_URL, ab.getDirFull());
		}
		
		
		// 记录pv，从loginfilter拷贝来的
		String logSession = SecurityUtil.getSessionId(session);
		
		String ip = request.getRemoteAddr();
		String sessionId = session.getId();

		CountUtil.count();
		
		// 从loginfilter里复制的关于onlineutil方面的功能
		String onlineKey = String.valueOf(loginUser.getId());
		UserBean onlineUser = (UserBean) JoycoolSessionListener.getOnlineUser(onlineKey);
		if(onlineUser != null && OnlineUtil.isUserKicked(onlineKey, sessionId)) {		// 判断是否被人踢下线
			JoycoolSessionListener.kickout(session);
			response.sendRedirect(response.encodeURL("/kicked.jsp"));
			return true;
		} else {
			// 如果onlineUser里没有该注册用户，则再次把它加入到onlineUser和jc_online_user表里，以得到准确的在线用户数；
			if (onlineUser == null) {
				onlineUser = loginUser;
				JoycoolSessionListener
						.updateOnlineStatus(onlineKey, onlineUser);
				JoycoolSessionListener.removeOnlineUser(sessionId);

				{
					int onlineCount = userService.getOnlineUserCount(
									"user_id = " + onlineUser.getId());
					if (onlineCount < 1) {
						OnlineBean online = new OnlineBean();
						online.setUserId(onlineUser.getId());
						online.setSessionId(sessionId);

						UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
						online.setTongId(us.getTong());

						userService.addOnlineUser(online);
					}
				}

			} else {
				JoycoolSessionListener
						.updateOnlineStatus(onlineKey, onlineUser);
			}
			onlineUser.setLastVisitPage(request.getRequestURL() + "?"
					+ request.getQueryString());
		}
		
		
//		System.out.println(new String(baos.toByteArray(),"utf-8"));
		
		String method = request.getMethod();

		byte[] param = null;// 页面参数
		request.setCharacterEncoding("utf-8");
		if (method.equalsIgnoreCase("POST")) {
//			StringBuilder sb = new StringBuilder(32);
//			Enumeration er = request.getParameterNames();
//			while (er.hasMoreElements()) {
//				String paraName = (String) er.nextElement();
//				sb.append(paraName);
//				sb.append('=');
//				sb.append(URLEncoder.encode(request.getParameter(paraName), "utf-8"));
//				sb.append('&');
//			}
//			param = sb.toString();
			int iLength = request.getContentLength();
			if(iLength > 0) {
				ServletInputStream is = request.getInputStream();
				
				int bytesRead = 0;
				
				param = new byte[iLength];
				int i = 0;
				for (; i < iLength; i += bytesRead) {
					bytesRead = is.read(param, i, iLength - i);
					if (bytesRead < 1)
						break;
				}
			}
		}
		// 处理隐藏url的情况
		HideUrlBean hub = null;	// 用于hide url情况下区分每次点击，记录一些信息
		if(ab.isFlagHideUrl()) {
			// 初始化hub数据
			hub = (HideUrlBean)session.getAttribute("hub");
			if(hub == null) {
				hub = new HideUrlBean();
				session.setAttribute("hub", hub);
				url = ab.getUrl();
				uri = "/";
			} else {
				boolean notOk = true;
				if(append.length() == 0) {
					url = ab.getUrl();
					uri = "/";
					notOk = false;
				} else if(append.charAt(0) == hub.visitC) {
					int g = StringUtil.toInt(append.substring(1));
					List list = hub.apphul;
					if(g >= 0 && list != null && list.size() > g) {
						String ap = (String)list.get(g);
						String lastURL = hub.lastURL;
						ap = ap.replace("&amp;", "&");
						if(lastURL == null) {
							url = ab.getUrl() + ap;
							uri = ap;
						} else {
							URL u = new URL(lastURL);
							url = new URL(u, ap).toString();
							int prefixLength = ab.getUrl().length();
							if(url.length() >= prefixLength)
								uri = url.substring(prefixLength);
						}
						notOk = false;	// 正常访问
					}
				}
				if(notOk) {	// 非正常访问
		        	String c2 = hub.lastP;
		        	if(c2 != null) {
		        		String ct2 = hub.lastCT;
		        		boolean isWml = ct2.startsWith("text/vnd");
				        if(wap20 && isWml) {	// 如果是wml页面，而且需要转换成2.0
				        	response.setContentType("text/html;charset=utf-8");
							c2 = WapServletResponseWrapper.toWap20(c2);
				        } else {
				        	response.setContentType(ct2);
				        }
				        response.getWriter().print(c2);
				        return true;
		        	} else {
		        		url = ab.getUrl();	// 没有找到上一次访问的页面，访问根目录
		        		uri = "/";
		        	}
				}
			}
		}
		
		// 记录pv
		StringBuilder logsb = new StringBuilder(128);;
		if (loginUser.getMobile() != null) {
			logsb.append(loginUser.getMobile());
			logsb.append(':');
			logsb.append(loginUser.getId());
			logsb.append(':');
			logsb.append(loginUser.getNickName().replace(':', '_'));
			logsb.append(':');
			logsb.append(ip);
			logsb.append(':');
			logsb.append(uri);
			logsb.append(":0:");
			logsb.append(logSession);
			logsb.append(":0");
		} else {
			logsb.append(':');
			logsb.append(uri);
			logsb.append(":0:");
			logsb.append(logSession);
			logsb.append(":0");
		}
		if (loginUser.getId() == LogUtil.getLogUser()) {
			String logs = DateUtil.formatSqlDatetime(new Date()) + "-" + loginUser.getMobile() + "-" + loginUser.getId() + "-"
			+ loginUser.getNickName() + "-" + ip + "-" + uri + "-" + sessionId;
			LogUtil.logSingleUser(logs);
		}
		
		//System.out.println("param----"+param);
		Map header = new HashMap();
		header.put("jcuser", String.valueOf(loginUser.getId()));
		header.put("jcip", ip);
		if(wap20)
			header.put("jcw2", "2");
//		header.put("jcnick", loginUser.getNickName());
		if(request.getParameter("jcfr") != null)
			header.put("jcfr", loginUser.getFriendString());
		if(request.getParameter("jcofr") != null)
			header.put("jcofr", loginUser.getOnlineFriendString());
		String requestType = request.getContentType();
		if(requestType != null) {
			header.put("Content-Type", requestType);
		}
		HttpResponse hr = AppConnector.toRequest(url, method, param, header); // 访问requestURL
		if (hr == null) {
			logsb.append(":e");
			LogUtil.logPv(logsb.toString());
			if(ab.isFlagDirect()) {
				response.setHeader("Cache-Control","no-cache");
				if(wap20) {
					response.setContentType("text/html;charset=utf-8");
					PrintWriter writer = response.getWriter();
					BaseAction2.getPageTop(writer, "无法访问");
					writer.append("访问页面出错啦-_-!.<br/>");
					if(!uri2.equals(ab.getDirFull()))
						writer.append("<a href=\"").append(ab.getDirFull()).append("\">返回").append(ab.getName()).append("</a><br/>");
					writer.append("————————<br/>");
					writer.append("<a href=\"/bottom.jsp\">ME</a>|");
					writer.append("<a href=\"/lswjs/index.jsp\">导航</a>|");
					writer.append("<a href=\"/wapIndex.jsp\">乐酷首页</a><br/>");
					writer.append(DateUtil.getCurrentDatetimeAsStr());
					BaseAction2.getPageBottom(writer);
				} else {
					response.setContentType("text/vnd.wap.wml;charset=utf-8");
					PrintWriter writer = response.getWriter();
					writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\" \"http://www.wapforum.org/DTD/wml_1.1.xml\"><wml><card title=\"访问出错\"><p>");
					writer.append("访问页面出错啦-_-!.<br/>");
					if(!uri2.equals(ab.getDirFull()))
						writer.append("<a href=\"").append(response.encodeURL(ab.getDirFull())).append("\">返回").append(ab.getName()).append("</a><br/>");
					writer.append("————————<br/>");
					writer.append("<a href=\"").append(response.encodeURL("/bottom.jsp")).append("\">ME</a>|");
					writer.append("<a href=\"").append(response.encodeURL("/lswjs/index.jsp")).append("\">导航</a>|");
					writer.append("<a href=\"").append(response.encodeURL("/wapIndex.jsp")).append("\">乐酷首页</a><br/>");
					writer.append(DateUtil.getCurrentDatetimeAsStr());
					writer.append("</p></card></wml>");
				}
				CountUtil.countOther();
				return true;
			}
			response.sendRedirect(response.encodeURL("/apps/app.jsp?id=" + ab.getId()));
			return true;
		}
		if(hr.getResponseCode() == 302) {
			String url2 = (String)((List)hr.getHeaders().get("Location")).get(0);
			if(url2.startsWith(ab.getUrl())) {
				url2 = ab.getDirFull() + url2.substring(ab.getUrl().length());
				if(ab.isFlagHideUrl()) {
					List list = new ArrayList(0);
					list.add(url2.replace(ab.getDirFull(), ""));
					hub.apphul = list;
					hub.addVisitC();
					hub.lastURL = ab.getUrl();
					url2 = hub.visitC + "0";
				}
			} else {
				url2 = url2.replace("http://dummy", "");
			}

			LogUtil.logPv(logsb.toString());	// 跳转也要记录pv!
			if(wap20)
				response.sendRedirect(url2);
			else
				response.sendRedirect(response.encodeURL(url2));
			return true;
		}
		
//		response.setHeader("Cache-Control", "max-age=0");
		List ctlist = (List) hr.getHeaders().get("Content-Type");
		String contentType;
		if(ctlist == null)
			contentType = "";
		else
			contentType = (String) (ctlist.get(0));
		response.setContentType(contentType);
		
		boolean isWml = contentType.startsWith("text/vnd");
		if (isWml || contentType.startsWith("text/html")) {
			String content = new String(hr.getContent(), "utf-8");
			
	    	Matcher m = jcPattern.matcher(content);
	        StringBuilder sb = new StringBuilder((int) (content.length() + 300));
	        pos = 0;
	        while (m.find()) {
	            sb.append(content.substring(pos, m.start(0)));
	            String label = m.group(1);
	            if(label.equals("bottom")) {
	            	sb.append(BaseAction.getBottom(request, response));
	            } else if(label.equals("top")) {
	            	sb.append(BaseAction.getTop(request, response));
	            } else if(label.equals("bottoms")) {
	            	sb.append(BaseAction.getBottomShort(request, response));
	            } else if (label.startsWith("inc")){
	            	int id = StringUtil.toInt(label.substring(3));
	            	if(id > 0) {
	                	Vector lineList = lineService.getLineList("parent_id=" + id + " and if_display = 1 ORDER BY line_index");
	                    for (int i = 0;i < lineList.size();i++) {
	                    	JaLineBean line = (JaLineBean) lineList.get(i);
	                        sb.append(StringUtil.getLineWml(line, request, response));
	                    }
	            	}
	            } else if(label.equals("sid")) {
	            	sb.append(response.encodeURL("/").substring(2));
	            } else if(label.equals("path")) {
	            	sb.append(ab.getDirFull());
	            } else if(label.startsWith("nick")) {
	            	UserBean user = null;
	            	int id = StringUtil.toInt(label.substring(4));
	            	if(id > 0) {
	            		user = UserInfoUtil.getUser(id);
	            		if(user != null)
	            			sb.append(user.getNickNameWml());
	            		else
	            			sb.append("(未知用户)");
	            	} else
	            		sb.append(loginUser.getNickNameWml());
	            } else if(label.startsWith("chat")) {
	            	UserBean user = null;
	            	int id = StringUtil.toInt(label.substring(4));
	            	if(id > 0) {
	            		user = UserInfoUtil.getUser(id);
	            		if(user != null) {
	            			sb.append("<a href=\"");
	            			sb.append(response.encodeURL("/chat/post.jsp?toUserId="));
	            			sb.append(id);
	            			sb.append("\">");
	            			sb.append(user.getNickNameWml());
	            			sb.append("</a>");
	            		}
	            	}
	            } else if(label.startsWith("info")) {
	            	UserBean user = null;
	            	int id = StringUtil.toInt(label.substring(4));
	            	if(id > 0) {
	            		user = UserInfoUtil.getUser(id);
	            		if(user != null) {
	            			sb.append("<a href=\"");
	            			sb.append(response.encodeURL("/user/ViewUserInfo.do?userId="));
	            			sb.append(id);
	            			sb.append("\">");
	            			sb.append(user.getNickNameWml());
	            			sb.append("</a>");
	            		}
	            	}
	            } else if(label.startsWith("home")) {
	            	UserBean user = null;
	            	int id = StringUtil.toInt(label.substring(4));
	            	if(id > 0) {
	            		user = UserInfoUtil.getUser(id);
	            		if(user != null) {
	            			sb.append("<a href=\"");
	            			sb.append(response.encodeURL("/home/home.jsp?userId="));
	            			sb.append(id);
	            			sb.append("\">");
	            			sb.append(user.getNickNameWml());
	            			sb.append("</a>");
	            		}
	            	}
	            } else {
	            	sb.append("(未知标签)");
	            }
	            pos = m.end(0);
	        }
	        sb.append(content.substring(pos));
	        if(ab.isFlagNoSid())
	        	content = replaceAllUrl(ab, sb.toString());
	        else if(ab.isFlagHideUrl()) {
	        	if(hr.getResponseCode() == 200) {		// 如果是页面报错，不需要处理
		        	hub.addVisitC();
		        	content = encodeHideAll(ab, sb.toString(), response, hub, wap20);
		        	hub.lastP = content;
		        	hub.lastCT = contentType;
		        	hub.lastURL = url;
	        	}
	        } else if(wap20)
	        	content = replaceAllUrl(ab, sb.toString());
	        else
	        	content = encodeAll(ab, sb.toString(), response);
	        if(wap20 && isWml) {	// 如果是wml页面，而且需要转换成2.0
	        	response.setContentType("text/html");
				content = WapServletResponseWrapper.toWap20(content);
	        }
			response.getWriter().print(content);
		} else {
			// 可能是文件下载
			List disp = (List) hr.getHeaders().get("Content-disposition");
			if(disp != null) {
				response.setHeader("Content-disposition", (String)disp.get(0));
			}
			
			OutputStream os = response.getOutputStream();
			os.write(hr.getContent());
			os.flush();
			os.close();
		}
		logsb.append(':');
		logsb.append(System.currentTimeMillis() - executeStartTime);
		LogUtil.logPv(logsb.toString());
//		OutputStream os = response.getOutputStream();
//		os.write(hr.getContent());
//		os.flush();
//		os.close();
		
		return true;
	}
	
	public static HashMap getAppUserMap(int userId) {
		Integer key = new Integer(userId);
		synchronized(appUserCache) {
			HashMap map = (HashMap)appUserCache.get(key);
			if(map != null)
				return map;
			
			List list = service.getAppUserList("user_id=" + userId);
			map = new HashMap(list.size());
			for(int i = 0;i < list.size();i++) {
				AppAddBean au = (AppAddBean)list.get(i);
				map.put(new Integer(au.getAppId()), au);
			}
			
			appUserCache.put(key, map);
			return map;
		}
	}
	public HashMap getAppUserMap() {
		loginUser = getLoginUser();
		if(loginUser == null)
			return null;
		return getAppUserMap(loginUser.getId());
	}
	
	// 根据id查看插件
	public void view() {
		int id = getParameterInt("id");
		AppBean ab = null;
		if(id > 0)
			ab = service.getApp("id=" + id);
		
		if(ab != null && !ab.isFlagHide()) {
			setAttribute("appBean", ab);
			return;
		}
		
		tip("tip", "没有找到该组件!");
	}
	
	
	// 安装插件
	public void install() {
		int id = getParameterInt("id");
		AppBean ab = null;
		if(id > 0)
			ab = service.getApp("id=" + id);
		
		if(ab == null || ab.isFlagClose() && ab.isFlagHide()) {
			tip("tip", "无法安装该组件!");
			return;
		}
		
		Map map = getAppUserMap();
		if(map.containsKey(new Integer(ab.getId()))) {
			tip("tip", "组件已经安装成功!");
			return;
		}
		AppAddBean au = new AppAddBean();
		au.setUserId(loginUser.getId());
		au.setAppId(id);
		au.setFlag(0);
		service.addAppUser(au);
		map.put(new Integer(id), au);
		tip("tip", "组件已经安装成功!");
	}
	
	// 卸载插件
	public void uninstall() {
		int id = getParameterInt("id");
		AppBean ab = null;
		if(id > 0)
			ab = service.getApp("id=" + id);
		
		if(ab.isFlagClose())
			setAttribute("appBean", ab);
	}
	
	static Pattern urlPattern = Pattern.compile("href=\"[^\"]+\"");
    public static String encodeAll(AppBean ab, String content, HttpServletResponse response) {
    	Matcher m = urlPattern.matcher(content);
        StringBuilder sb = new StringBuilder((int) (content.length() * 1.1));
        int pos = 0;
        while (m.find()) {
            sb.append(content.substring(pos, m.start(0)));
            String href = m.group(0);
            String url = href.substring(6, href.length() - 1);
            if(url.charAt(0) == '@') {	// @1表示ontimer，后面跟随url
            	switch(url.charAt(1)) {
            	case '1':	// ontimer
            		sb.append("ontimer=\"");
            		break;
            	case '2':	// src
            		sb.append("src=\"");
            		break;
            	case '3':	// action
            		sb.append("action=\"");
            		break;
            	}
	            sb.append(response.encodeURL(url.substring(2)));
	            sb.append('\"');
            } else if(url.startsWith("//")) {
	            sb.append("href=\"");
	            sb.append(ab.getDirFull());
	            sb.append(response.encodeURL(url.substring(2)));
	            sb.append('\"');
            } else {
	            sb.append("href=\"");
	            sb.append(response.encodeURL(url));
	            sb.append('\"');
            }
            pos = m.end(0);
        }
        sb.append(content.substring(pos));
        return sb.toString();
    }
    // 隐藏所有的url到session
    public static String encodeHideAll(AppBean ab, String content, HttpServletResponse response, HideUrlBean hub, boolean wap20) {
//    	int ic = hub.apphuc;
    	List list = new ArrayList(32);
    	int count;
    	String vc = String.valueOf(hub.visitC);
//    	if(ic == null)
    		count = 0;
//    	else
//    		count = ic.intValue();
    	Matcher m = urlPattern.matcher(content);
        StringBuilder sb = new StringBuilder((int) (content.length()));
        int pos = 0;
        if(wap20) {
	        while (m.find()) {
	            sb.append(content.substring(pos, m.start(0)));
	            String href = m.group(0);
	            String url = href.substring(6, href.length() - 1);
	            
	            if(url.startsWith("/")) {
	            	if(url.startsWith("//")) {
		            	sb.append("href=\"");
		            	sb.append(ab.getDirFull());
			            sb.append(vc.substring(2));
			            sb.append(count);
			            sb.append('\"');
			            count++;
			            list.add(url);
	            	} else {
	            		sb.append(href);
	            	}
	            } else if(url.charAt(0) == '@') {	// @1表示ontimer，后面跟随url
	            	switch(url.charAt(1)) {
	            	case '1':	// ontimer
	            		sb.append("ontimer=\"");
	            		break;
	            	case '2':	// src
	            		sb.append("src=\"");
	            		break;
	            	case '3':	// action
	            		sb.append("action=\"");
	            		break;
	            	}
		            sb.append(vc);
		            sb.append(count);
		            sb.append('\"');
		            count++;
		            list.add(url.substring(2));
	            } else {
	            	sb.append("href=\"");
		            sb.append(vc);
		            sb.append(count);
		            sb.append('\"');
		            count++;
		            list.add(url);
	            }
	
	            pos = m.end(0);
	        }
        } else {
	        while (m.find()) {
	            sb.append(content.substring(pos, m.start(0)));
	            String href = m.group(0);
	            String url = href.substring(6, href.length() - 1);
	            
	            if(url.startsWith("/")) {
	            	if(url.startsWith("//")) {
		            	sb.append("href=\"");
		            	sb.append(ab.getDirFull());
			            sb.append(response.encodeURL(vc.substring(2) + count));
			            sb.append('\"');
			            count++;
			            list.add(url);
	            	} else {
		            	sb.append("href=\"");
		            	sb.append(response.encodeURL(url));
		            	sb.append('\"');
	            	}
	            } else if(url.charAt(0) == '@') {	// @1表示ontimer，后面跟随url
	            	switch(url.charAt(1)) {
	            	case '1':	// ontimer
	            		sb.append("ontimer=\"");
	            		break;
	            	case '2':	// src
	            		sb.append("src=\"");
	            		break;
	            	case '3':	// action
	            		sb.append("action=\"");
	            		break;
	            	}
	            	sb.append(response.encodeURL(vc + count));
		            sb.append('\"');
		            count++;
		            list.add(url.substring(2));
	            } else {
	            	sb.append("href=\"");
		            sb.append(response.encodeURL(vc + count));
		            sb.append('\"');
		            count++;
		            list.add(url);
	            }
	            
	            pos = m.end(0);
	        }
        }
        sb.append(content.substring(pos));
        hub.apphuc = count;
        hub.apphul = list;
        return sb.toString();
    }
    static Pattern urlPattern2 = Pattern.compile("href=\"@[^\"]+\"");
    public static String replaceAllUrl(AppBean ab, String content) {
    	Matcher m = urlPattern.matcher(content);
        StringBuilder sb = new StringBuilder((int) (content.length() * 1.1));
        int pos = 0;
        while (m.find()) {
            sb.append(content.substring(pos, m.start(0)));
            String href = m.group(0);
            String url = href.substring(6, href.length() - 1);
            if(url.startsWith("//")) {
            	sb.append("href=\"");
            	sb.append(ab.getDirFull());
            	sb.append(url.substring(2));
            	sb.append('\"');
            } else if(url.startsWith("@")) {
	        	switch(url.charAt(1)) {
	        	case '1':	// ontimer
	        		sb.append("ontimer=\"");
	        		break;
	        	case '2':	// src
	        		sb.append("src=\"");
	        		break;
	        	case '3':	// action
	        		sb.append("action=\"");
	        		break;
	        	}
	            sb.append(url.substring(2));
	            sb.append('\"');
            } else {
            	sb.append(href);
            }
            
            pos = m.end(0);
        }
        sb.append(content.substring(pos));
        return sb.toString();
    }
    
    // 返回评论列表[分页]
    public List getReplyList(AppBean app,int pageNow,int pageInfoCount){
    	List list = new ArrayList();
    	if (app != null){
    		list = service.getAppReplyList(" app_id = " + app.getId() + " order by id desc limit " + (pageNow * pageInfoCount) + "," + pageInfoCount);
    	}
    	return list;
    }
    
    // 评论
    public boolean reply(String content,AppBean app){
    	boolean result = false;
		// 检查输入
		content = StringUtil.removeSpecialAsc(content);
    	if ("".equals(content)){
    		this.setAttribute("tip","评论不可为空.");
    		return result;
    	}
    	if (content.length() > MAX_REPLY_COUNT){
    		this.setAttribute("tip","字数太多.");
    		return result;    		
    	}
	    if(this.isCooldown("chat",15000)){
	    	AppReplyBean replyBean = new AppReplyBean();
	    	replyBean.setContent(content);
	    	replyBean.setUserId(this.getLoginUser().getId());
	    	replyBean.setAppId(app.getId());
	    	result = service.addReply(replyBean);
    	} else {
    		this.setAttribute("tip","你评论的太快了.");
    		return result;  
    	}
    	return result;
    }
    
    // 返回评分列表[分页]
    public List getScoreList(AppBean app,int pageNow,int pageInfoCount){
    	List list = new ArrayList();
    	if (app != null){
    		list = service.getAppScoreList(" app_id = " + app.getId() + " order by id desc limit " + (pageNow * pageInfoCount) + "," + pageInfoCount);
    	}
    	return list;
    }
    
    // 评分
    public boolean score(String content,int score,AppBean app){
    	boolean result = false;
    	Map map = this.getAppUserMap();
    	AppAddBean appAdd = (AppAddBean)map.get(new Integer(app.getId()));
    	// 检查用户是否已添加了该组件
    	if (appAdd == null){
    		this.setAttribute("tip","你没有添加此组件,不可评分.");
    		return result;
    	}
		// 检查输入
		content = StringUtil.removeSpecialAsc(content);
    	if ("".equals(content)){
    		this.setAttribute("tip","评论不可为空.");
    		return result;
    	}
    	if (content.length() > MAX_REPLY_COUNT){
    		this.setAttribute("tip","字数太多.");
    		return result;    		
    	}
    	if (score <= 0 || score > 5){
    		this.setAttribute("tip","输入的分数错误.");
    		return result;  
    	}
	    if(this.isCooldown("chat",15000)){
	    	AppScoreBean scoreBean = new AppScoreBean();
	    	scoreBean.setContent(content);
	    	scoreBean.setUserId(this.getLoginUser().getId());
	    	scoreBean.setAppId(app.getId());
	    	scoreBean.setScore(score);
	    	result = service.addScore(scoreBean);
	    	app.setScore(score);
    	} else {
    		this.setAttribute("tip","你评分的太快了.");
    		return result;  
    	}
    	return result;
    }
    
    public static void addTypeCount(int type, int add) {
    	if(type == 0)
    		return;
    	AppTypeBean t = AppAction.getType(type);
    	if(t == null)
    		return;
		t.addCount(add);
		SqlUtil.executeUpdate("update app_type set `count`=" + t.getCount() + " where id=" + type, 5);
    }
}