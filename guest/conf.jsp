<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   FocusMsg msg = null;
   UserFocus focus = null;
   GuestUserInfo focusUser = null;
   GuestUserInfo leftUser = null;
   GuestUserInfo guestUser = action.getGuestUser();
   if (guestUser == null){
	   response.sendRedirect("login.jsp");
	   return;
   }
   int result = action.getParameterInt("r");
   int id = action.getParameterInt("id");
   if (result > 0 && result < 3 && id > 0){
	   msg = GuestHallAction.service.getFocusMsg(" id=" + id);
	   if (msg != null && msg.getRightUid() == guestUser.getId()){
		   // 设置标记为已处理
		   // SqlUtil.executeUpdate("update focus_msg set readed=1 where id=" + id,6);
		   // 接受。如果选择了“忽略”，则什么都不做。
		   if (result == 1){
			   // 接受
				focus = new UserFocus(msg.getLeftUid(),msg.getRightUid());
				GuestHallAction.service.addFocus(focus);
		   }
		   SqlUtil.executeUpdate("delete from focus_msg where id=" + msg.getId(),6);
	   }
   }
   // 尚未处理的验证列表
   List list = GuestHallAction.service.getFocusMsgList(" right_uid=" + guestUser.getId() + " and readed=0 order by id asc");
   int residual = list.size() - 1;
   if (residual < 0) residual=0;
   if (list == null || list.size() == 0){
	   response.sendRedirect("index.jsp");
	   return;
   } else {
	   msg = (FocusMsg)list.get(0);
	   if (msg != null){
		   leftUser = GuestHallAction.getGuestUser(msg.getLeftUid());
		   if (leftUser == null){
			   tip = "没有找到相关信息.";
		   }
	   } else {
		   tip = "没有找到相关信息.";
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="添加关注"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (leftUser != null){
%><%=leftUser.getUserNameWml()%>(<%=leftUser.getGenderStr()%>/<%=leftUser.getAge()%>岁)希望将您加为关注.<br/>
<a href="conf.jsp?r=1&amp;id=<%=msg.getId()%>">接受</a>|<a href="conf.jsp?r=2&amp;id=<%=msg.getId()%>">忽略</a><br/>剩余<%=residual%>条未处理.<br/><%
}
} else {
%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>