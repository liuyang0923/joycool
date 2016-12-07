/*
 * Created on 2005-11-16
 *
 */
package net.joycool.wap.action.user;

import java.net.URLEncoder;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class SearchUserAction extends BaseAction {

	public static int PUBLIC_NUMBER_PER_PAGE = 15;
	static IUserService service = ServiceFactory.createUserService();

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		

		// 取得参数
		String nickName = request.getParameter("nickname");
		if(nickName != null)
			nickName = nickName.trim();
		// 2007.5.30
		int id = StringUtil.toInt(request.getParameter("id"));
		if (nickName != null && nickName.length() != 0) {
			//按照nickname搜索
			String backTo = request.getParameter("backTo");
			if (backTo == null) {
				backTo = BaseAction.INDEX_URL;
			}

			String condition = null;
			if(nickName.length() > 1)
				condition = "nickname like '" + StringUtil.toSqlLike(nickName) + "%'";
			else
				condition = "nickname = '" + StringUtil.toSql(nickName) + "'";

			// 分页相关处理
			int pageIndex = 0;
			if (request.getParameter("pageIndex") == null
					|| "".equals(request.getParameter("pageIndex"))) {
				pageIndex = 0;
			} else {
				pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			}
			String prefixUrl = "SearchUser.do?nickname=" + nickName
					+ "&amp;backTo=" + URLEncoder.encode(backTo, "UTF-8");

			int totalCount = service.getUserCount(condition);
			int totalPageCount = totalCount / PUBLIC_NUMBER_PER_PAGE;
			if (totalCount % PUBLIC_NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			int start = pageIndex * PUBLIC_NUMBER_PER_PAGE;
			int end = PUBLIC_NUMBER_PER_PAGE;

			// 开始查询
			Vector userList = new Vector();
			if (condition != null) {
				condition += " ORDER BY id";
				userList = service.getUserList(condition + " limit " + start
						+ ", " + end);
			}

			request.setAttribute("totalCount", new Integer(totalCount));

			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);

			request.setAttribute("userList", userList);
			request.setAttribute("backTo", backTo);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else {
			//按照id搜索
			String backTo = request.getParameter("backTo");
			if (backTo == null) {
				backTo = BaseAction.INDEX_URL;
			}

//			String condition = null;
//			if (userName != null && !userName.equals("")) {
//				condition = "user_name = '" + userName + "'";
//			}
//			if (nickName != null && !nickName.equals("")) {
//				if (condition != null) {
//					condition += " and ";
//				}
//				condition = "nickname = '" + nickName + "'";
//			}
			if(id<=0){
				id=431;
			}
			String condition = "id = " + id ;
			// 分页相关处理
			int pageIndex = 0;
			if (request.getParameter("pageIndex") == null
					|| "".equals(request.getParameter("pageIndex"))) {
				pageIndex = 0;
			} else {
				pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			}
			String prefixUrl = "SearchUser.do?id=" + id
					+ "&amp;backTo=" + URLEncoder.encode(backTo, "UTF-8");

			int totalCount = service.getUserCount(condition);
			int totalPageCount = totalCount / PUBLIC_NUMBER_PER_PAGE;
			if (totalCount % PUBLIC_NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			int start = pageIndex * PUBLIC_NUMBER_PER_PAGE;
			int end = PUBLIC_NUMBER_PER_PAGE;

			// 开始查询
			Vector userList = new Vector();
			if (condition != null) {
				userList = service.getUserList(condition + " ORDER BY id limit " + start
						+ ", " + end);
			}
			
            	
			request.setAttribute("totalCount", new Integer(totalCount));

			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);

			request.setAttribute("userList", userList);
			request.setAttribute("backTo", backTo);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);

		}
	}
}
