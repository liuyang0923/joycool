<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<%! static int COUNT_PRE_PAGE = 3; %>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   UserFocus focus = null;
   GuestUserInfo focusUser = null;
   GuestUserInfo guestUser = action.getGuestUser();
   if (guestUser == null){
	   response.sendRedirect("login.jsp");
	   return;
   }
   int focusUid = action.getParameterInt("fu");
   int add = action.getParameterInt("a");
   if (add > 0){
	    // 添加关注
	    int result = action.addFocus(guestUser,focusUid);
	    if (add == 1){
	    	// 返回关注列表
			response.sendRedirect("back.jsp?i=" + result);
		    return;
	    } else {
	    	// 返回聊天大厅
	    	if (result == 7)
	    		result = 22;
	    	// 如果成功，提示添加成功并返回聊天室。如果失败，提示失败信息并返回关注列表
			response.sendRedirect("back.jsp?i=" + result);
		    return;
	    }
   }
   int del = action.getParameterInt("d");
   if (del > 0){
	    // 删除关注
	    SqlUtil.executeUpdate("delete from user_focus where left_uid=" + guestUser.getId() + " and right_uid=" + focusUid,6);
	    response.sendRedirect("back.jsp?i=21");
	    return;
   }
   // 取得在线和离线的关注列表。放到request中。
   action.getOnlineAndOffLineList(guestUser.getId());
   List online = (ArrayList)request.getAttribute("online");
   List offline = (ArrayList)request.getAttribute("offline");
   int onlineCount = 0;
   int offlineCount = 0;
   if (online != null && online.size() > 0)
	   onlineCount = online.size();
   if (offline != null && offline.size() > 0)
	   offlineCount = offline.size();   
   PagingBean paging = new PagingBean(action, onlineCount, COUNT_PRE_PAGE, "p");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="关注列表"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>添加关注:(按ID)<br/>
<input name="uid" format="*N"/><br/>
<anchor>
	添加
	<go href="focus.jsp?a=1" method="post">
		<postfield name="fu" value="$uid" />
	</go>
</anchor><br/>
==在线(<%=onlineCount%>)==<br/>
<%if (online != null && online.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		focus = (UserFocus)online.get(i);
		if (focus != null){
			focusUser = GuestHallAction.getGuestUser(focus.getRightUid());
			if (focusUser != null){
				%><%=i+1%>.<a href="info.jsp?uid=<%=focusUser.getId()%>"><%=focusUser.getUserNameWml()%></a><br/><%
			}
		}
	}%><%=paging.shuzifenye("focus.jsp",false,"|",response)%><%
}%>
==离线(<%=offlineCount%>)==<br/>
<%if (offline != null && offline.size() > 0){
	int end = offline.size();
	if (end > 3)
		end = 3;
	for (int i = 0 ; i < end ; i++){
		focus = (UserFocus)offline.get(i);
		if (focus != null){
			focusUser = GuestHallAction.getGuestUser(focus.getRightUid());
			if (focusUser != null){
				%><%=i+1%>.<a href="info.jsp?uid=<%=focusUser.getId()%>"><%=focusUser.getUserNameWml()%></a><br/><%
			}
		}
	}
}%>
<a href="focus2.jsp">更多离线关注</a><br/>
<a href="index.jsp">返回游乐园</a><br/>
<%
} else {
	%><%=tip%><br/><a href="focus.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>