<%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.spec.InviteAction"%><%!

%><%
	String server = request.getServerName();
	String from = request.getParameter("f");
	if("3g".equals(from)){
		session.setAttribute("wapEd",new Integer(2));
		response.sendRedirect("/wapIndex.jsp");
		return;
	}
	if("mm".equals(from)){
		session.setAttribute("red","/Column.do?columnId=11963");
		response.sendRedirect("/Column.do?columnId=5507");
		return;
	}
		
/*	if(server.equals("m.joycool.net")) {
        response.sendRedirect("http://a.xtdsp.com/wapIndex.jsp");
        return;
	}
	String ua = request.getHeader("User-Agent");
	if(ua != null && ua.indexOf("Opera")==-1&& (ua.indexOf("Windows NT 5.1")!=-1||ua.indexOf("iPhone")!=-1||ua.indexOf("MSIE")!=-1 && !SecurityUtil.isMobileFull(request))){
		response.sendRedirect("http://wapx.joycool.net/");
		return;
	}
*/

    int linkId = StringUtil.toId(request.getParameter("linkId"));
	if(linkId == 0){
		linkId = 0;
		InviteAction action = new InviteAction(request, response);
		action.index();
		int userId = action.getUserId();
		if(from!=null){
			int fmId = jc.family.FamilyAction.getFromDomain(from);
			if(fmId!=0){
				action.innerRedirect("/fm/enter.jsp?id=" + fmId);
				return;
			}
		}
		if(from!=null&&from.startsWith("0")){
			int fmId = StringUtil.toId(from);
			if(fmId>0){
				fmId += 20000;
				action.innerRedirect("/fm/enter.jsp?id=" + fmId);
				return;
			}
		}
		if(userId == 0) {
		
			if(request.isRequestedSessionIdFromCookie()&&session.getAttribute("loginUser")!=null) {
				CookieUtil cu = new CookieUtil(request);
				if(cu.getCookieValue("jsid")==null){
					Cookie cookie = new Cookie("jsid", session.getId());	// joycool auto login cookie
					cookie.setMaxAge(90000000);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
		
			action.innerRedirect("Column.do?columnId=" + Constants.INDEX_ID);
			return;
		} else {
			response.sendRedirect("http://gd.joycool.net" + response.encodeURL("/invite.jsp"));		// 如果是不同的域名，encodeURL将会无效
			return;
		}
	} else {
		session.setAttribute("linkId", "" + linkId);
	}
	if(linkId >= 20000 && linkId < 30000){
		response.sendRedirect(("/Column.do?columnId=3480"));
	} else if(linkId >= 30000 && linkId < 40000){
		response.sendRedirect(("http://haha.g3me.cn"));
	} else if(linkId >= 40000 && linkId < 50000){
		response.sendRedirect(("http://gaga.g3me.cn"));
	} else if(linkId >= 50000 && linkId < 60000){
		response.sendRedirect(("http://wawa.g3me.cn"));
	} else {
        request.getRequestDispatcher("Column.do?columnId=" + Constants.INDEX_ID).forward(request,response);
	}
%>