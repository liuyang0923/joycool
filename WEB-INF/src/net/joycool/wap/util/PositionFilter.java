/*
 * MCQ_1_TOTAL 时间:2006-6-6
 */
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

import net.joycool.wap.bean.PositionBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;

public class PositionFilter implements Filter {
	public final static String CAS_FILTER_USER = "edu.yale.its.tp.cas.client.filter.user";

	// ----------------------------------------------------- Instance Variables

	protected FilterConfig filterConfig = null;

	/**
	 * Take this filter out of service.
	 */
	public void destroy() {

		this.filterConfig = null;

	}

	public static IUserService userService = ServiceFactory.createUserService();
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
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);
		if(session == null) {
			chain.doFilter(request, response);
			session = httpRequest.getSession(false);
			return;
		}
		// 从session中取得登陆用户信息
		UserBean loginUser = (UserBean) session
				.getAttribute(Constants.LOGIN_USER_KEY);
		// 从MAP中得到位置信息
		// HashMap map = LoadResource.getPositionMap();
		// 得到请求uri的地址,不包括
		String uri = httpRequest.getRequestURI();
		String uri2 = uri;
		// 判断从第一个字符往后的uri中包含/标志前的字符个数
		int count = uri.indexOf("/", 1);
		// 如果个数等于0,让个数等于1
		if (count == -1) {
			count = 1;
		}
		// 截取字符串
		uri = uri.substring(1, count);
		// 从map中得到对应key的values
		PositionBean positionBean = LoadResource.getPostionByUri(uri);

		// 请求页面的url
		String thisUrl = PageUtil
				.getCurrentPageURL((HttpServletRequest) request);
		// 得到本页面的访问模块
		String thisModule = PositionUtil.getModule(thisUrl);
		// 得到本页面模块的url
		String thisModuleUrl = PositionUtil
				.getModuleReturnUrl(thisUrl, thisUrl);

		// liuyi 2006-09-08 start 返回点击昵称前的页面功能
		String lastUrl = (String) session.getAttribute("lastUrl");
		String returnUrl = (String) session.getAttribute("pagebeforeclick");
		// liuyi 2007-01-25 登录问题修改 start
		if (lastUrl == null) {
			lastUrl = BaseAction.INDEX_URL;
		}
		if (returnUrl == null) {
			returnUrl = BaseAction.INDEX_URL;
		}
		// liuyi 2007-01-25 登录问题修改 end
		// liuyi 2006-12-18 返回跳转修改 start
		if ("post".equalsIgnoreCase(httpRequest.getMethod()) || uri2.indexOf("/entry") != -1) {
			// do nothing
		} else if (PositionUtil.isClickUserName(thisUrl)
				&& lastUrl.indexOf("auto") == -1
				&& lastUrl.indexOf("register") == -1
				&& lastUrl.indexOf("kicked") == -1
				&& lastUrl.indexOf("log") == -1) {
			// session.setAttribute("lastUrl", thisUrl);
			session.setAttribute("pagebeforeclick", lastUrl);
		} else {
			session.setAttribute("lastUrl", thisUrl);
		}
		// liuyi 2006-12-18 返回跳转修改 end
		// liuyi 2006-09-08 end 返回点击昵称前的页面功能

		// 如果本页面模块属于指定模块
		if (!thisModule.equals("") && uri2.indexOf("/entry") == -1) {
			// 出了聊天室模块，则删除保存的roomid
			if (!thisModule.equalsIgnoreCase("chat")) {
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
			// 得到上一个模块的url
			String lastModuleUrl = (String) session
					.getAttribute(Constants.LAST_MODULE_URL);
			if (lastModuleUrl == null) {
				lastModuleUrl = PositionUtil.getModuleReturnUrl(thisUrl,
						thisUrl);
				session.setAttribute(Constants.LAST_MODULE_URL, lastModuleUrl);
			}
			// 得到上一个模块
			String lastModule = (String) session
					.getAttribute(Constants.LAST_MODULE);
			if (lastModule == null) {
				lastModule = PositionUtil.getModule(lastModuleUrl);
				session.setAttribute(Constants.LAST_MODULE, lastModule);
			}

			// 得到当前模块的url
			String currentModuleUrl = (String) session
					.getAttribute(Constants.CURRENT_MODULE_URL);
			if (currentModuleUrl == null) {
				currentModuleUrl = PositionUtil.getModuleReturnUrl(thisUrl,
						thisUrl);
				session.setAttribute(Constants.CURRENT_MODULE_URL,
						currentModuleUrl);
			}
			// 得到当前访问模块
			String currentModule = (String) session
					.getAttribute(Constants.CURRENT_MODULE);
			if (currentModule == null) {
				currentModule = PositionUtil.getModule(currentModuleUrl);
				session.setAttribute(Constants.CURRENT_MODULE, currentModule);
			}

			if (!thisModule.equals(currentModule)) { // 模块间切换
				session.setAttribute(Constants.LAST_MODULE, currentModule);
				session.setAttribute(Constants.LAST_MODULE_URL,
						currentModuleUrl);
				session.setAttribute(Constants.CURRENT_MODULE, thisModule);
				session.setAttribute(Constants.CURRENT_MODULE_URL,
						thisModuleUrl);
			} else { // 模块内切换
				session.setAttribute(Constants.CURRENT_MODULE_URL,
						thisModuleUrl);
			}
		}

		// 判断用户登陆信息是否存在
		if (loginUser != null) {
			// 创建工厂
			
			String strUpdate = null;
			// 判断是否存在values,
			if (positionBean != null) {
				// 判断session中的position和当前位置ID是否一致,如果不一致更新数据库,更新session
				strUpdate = "position_id=" + positionBean.getId();
				if (positionBean.getId() != loginUser.getPositionId()) {
					// 更新数据库position_id字段
					if (positionBean.getId() == Constants.POSITION_CHAT) {
						if (request.getParameter("roomId") != null)
							strUpdate = strUpdate + ",sub_id="
									+ StringUtil.toInt(request.getParameter("roomId"));
						else
							strUpdate = strUpdate + ",sub_id=0";
					}
					boolean flag = true;
					if(positionBean.getId() <= 10)
						flag = userService.updateOnlineUser(strUpdate,
								"user_id=" + loginUser.getId());
					if (flag) {
						// 如果更新成功,更新session中的position为当前位置
						loginUser.setPositionId(positionBean.getId());
						UserBean user = (UserBean) OnlineUtil
								.getOnlineBean(loginUser.getId() + "");
						user.setPositionId(positionBean.getId());
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