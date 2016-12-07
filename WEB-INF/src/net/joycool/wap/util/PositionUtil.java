package net.joycool.wap.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.ModuleBean;
import net.joycool.wap.bean.UserPositionBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.impl.ModuleServiceImpl;

public class PositionUtil {
	
	public static ICacheMap userPositionCache = CacheManage.userPosition;
	
	public static HashMap moduleHash = new HashMap();
	public static List moduleList = new ArrayList();

	public static HashMap clickUserNamePageHash = new HashMap();

	public static int MIN_PRIORITY = 1;

	static {
		loadModuleConf();
		initUserNamePages();
	}

	/**
	 * 从数据库读取模块配置信息
	 */
	public static void loadModuleConf() {
		try {
			HashMap moduleHash2 = new HashMap();
			//读入数据
			ModuleServiceImpl moduleService = new ModuleServiceImpl();
			List moduleList2 = moduleService.getModuleList("1");
			for(int i=0;i<moduleList2.size();i++){
				ModuleBean module = (ModuleBean)moduleList2.get(i);
				if(module!=null){
					moduleHash2.put(new Integer(module.getId()), module);
				}
				if(module.getId() == 0)	{	// 设定默认的module就是5: 闲逛中
					nullModule = module;
					moduleHash2.put(new Integer(5), module);
				}
			}
			if(nullModule == null)
				nullModule = new ModuleBean();
			moduleHash = moduleHash2;
			moduleList = moduleList2;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * 需要记录点击用户昵称或者是登陆的页面
	 */
	public static void initUserNamePages() {
		clickUserNamePageHash.put("/user/ViewUserInfo.do", "");
		clickUserNamePageHash.put("/chat/post.jsp", "");
		clickUserNamePageHash.put("/user/login.jsp", "");
		clickUserNamePageHash.put("/user/logout.jsp", "");
		clickUserNamePageHash.put("/register.jsp", "");
		clickUserNamePageHash.put("/kicked.jsp", "");
		clickUserNamePageHash.put("/jcadmin/autoRegister.jsp", "");
		clickUserNamePageHash.put("/jcadmin/registerView.jsp", "");
	}

	public static boolean isClickUserName(String url) {
		return clickUserNamePageHash.containsKey(url);
//		boolean flag = false;
//
//		if (url == null || url.trim().equals("")) {
//			return false;
//		}
//
//		Enumeration enu = clickUserNamePageHash.keys();
//		while (enu.hasMoreElements()) {
//			String page = (String) enu.nextElement();
//			if (url.startsWith(page)) {
//				flag = true;
//				break;
//			}
//		}
//
//		return flag;
	}

	/**
	 * 根据url获得对应模块的名称
	 * 
	 * @param url
	 * @return
	 */
	public static String getModule(String url) {
		String ret = "";

		if (url == null)
			return "";

		try {
			ModuleBean module = getModuleBean(url);
			if (module != null) {
				ret = module.getImage();
			}
		} catch (Exception e) {
		}

		if (ret == null)
			ret = "";
		return ret;
	}

	/**
	 * 根据url或者对应的模块返回地址
	 * 
	 * @param url
	 * @param defaultValue
	 *            模块返回地址缺省值
	 * @return
	 */
	public static String getModuleReturnUrl(String url, String defaultValue) {
		String ret = defaultValue;

		if (url == null)
			return defaultValue;

		try {		
			ModuleBean module = getModuleBean(url);
			if (module != null) {
				ret = module.getReturnUrl();
			}
			if ("".equals(ret)) {
				ret = defaultValue;
			}
		} catch (Exception e) {
		}

		if (ret == null) {
			ret = defaultValue;
		}
		return ret;
	}
	
	public static ModuleBean getModule(int id) {
		return (ModuleBean) moduleHash.get(new Integer(id));
	}
	
	public static HashMap uriMapModule = new HashMap(512);	// uri map到对应的jc_module
	public static void resetUriMap() {
		synchronized(uriMapModule) {
			uriMapModule.clear();
		}
		uriMapModule.put(null, nullModule);
	}
	public static ModuleBean nullModule;
	static {
		resetUriMap();
	}
	/**
	 * 根据url获取对应的模块
	 * @param url
	 * @return
	 */
	public static ModuleBean getModuleBean(String url) {
		ModuleBean ret = (ModuleBean)uriMapModule.get(url);	// 注意：这里没有synchronized，是否会有问题？
		if(ret != null) {
			if(ret == nullModule)
				return null;
			return ret;
		}

		int currentPriority = MIN_PRIORITY;
		Iterator enu = moduleList.iterator();
		while (enu.hasNext()) {
			ModuleBean module = (ModuleBean)enu.next();
			String moduleUrl = module.getUrlPattern();
			// 包含模块url
			if (url.startsWith(moduleUrl)) {	// 如果是虚拟的模块，无视
				int priority = module.getPriority();
				// 取优先级最高的那个模块url
				if (priority > currentPriority) {
					currentPriority = priority;
					ret = module;
				}
			}
		}
		synchronized(uriMapModule) {
			if(ret == null)
				uriMapModule.put(url, nullModule);
			else
				uriMapModule.put(url, ret);
		}

		return ret;
	}
	
	public static String getPositionName(String key, int positionId) {
		boolean isActive = OnlineUtil.isActive(key);
		if (isActive) {
			return getModule(positionId).getPositionName();
		} else {
			return "发呆";
		}
	}
	
	public static void addModule(ModuleBean module) {
		synchronized(uriMapModule) {
			if(moduleHash == null)
				return;
			moduleHash.put(new Integer(module.getId()), module);
			moduleList.add(module);
			// 添加之后，删除缓存的map
			uriMapModule.remove(module.getUrlPattern());
		}
	}

	/**
	 * 获取返回进入页显示的wml代码
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getLastModuleUrl(HttpServletRequest request,
			HttpServletResponse response) {
		if (request == null)
			return "";

		HttpSession session = request.getSession();

		String lastModuleUrl = StringUtil.convertNull((String) session
				.getAttribute(Constants.LAST_MODULE_URL));
		ModuleBean lastModule = (ModuleBean) session
				.getAttribute(Constants.LAST_MODULE);
		if (lastModule != null) {
			ModuleBean currentModule = (ModuleBean) session
					.getAttribute(Constants.CURRENT_MODULE);
			if (!lastModule.equals(currentModule)) {
				StringBuilder sb = new StringBuilder(32);
				sb.append("<a href=\"");
				sb.append(lastModuleUrl.replace("&", "&amp;"));
				sb.append("\">(回");
				sb.append(lastModule.getName());
				sb.append(")</a>");
				return  sb.toString();
			}
		}

		return "";
	}
	public static String getCurrentModuleUrl(HttpServletRequest request) {
		if (request == null)
			return "";

		HttpSession session = request.getSession();

		String lastModuleUrl = StringUtil.convertNull((String) session
				.getAttribute(Constants.CURRENT_MODULE_URL));
		ModuleBean lastModule = (ModuleBean) session
				.getAttribute(Constants.CURRENT_MODULE);
		if (lastModule != null) {
			StringBuilder sb = new StringBuilder(32);
			sb.append("<a href=\"");
			sb.append(lastModuleUrl.replace("&", "&amp;"));
			sb.append("\">(回");
			sb.append(lastModule.getName());
			sb.append(")</a>");
			return  sb.toString();
		}

		return "";
	}
	// 添加用户访问历史模块
	public static void addUserPositionHistory(int userId, ModuleBean module) {
		UserPositionBean up = getUserPosition(userId);
		up.addHistory(module);
	}
	public static UserPositionBean getUserPosition(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(userPositionCache) {
			UserPositionBean up = (UserPositionBean) userPositionCache.get(key);
			if (up == null) {
				up = new UserPositionBean();

				userPositionCache.put(key, up);
			}
	
			return up;
		}
	}
}
