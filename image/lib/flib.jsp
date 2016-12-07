<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.imglib.*,net.joycool.wap.util.db.DbOperation"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
   ImgLibAction action = new ImgLibAction(request);
   if (action.getLoginUser() == null){
	   // 没登陆,返回登陆页面
	   response.sendRedirect("/user/login.jsp");
	   return;
   }
   String tip = "";
   List list = null;
   LibUser libUser = null;
   PagingBean paging = null;
   String friends = action.getLoginUser().getFriendString();
   if ("".equals(friends)){
	   tip = "您还没有好友。";
   } else {
	   list = ImgLibAction.service.getLibUserList(" user_id in (" + friends + ") and `count` > 0");
	   paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="好友的图库"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (list != null && list.size() > 0){
for (int i = paging.getStartIndex() ; i < paging.getEndIndex();i++){
libUser = (LibUser)list.get(i);
if (libUser != null){
	%><%=i+1%>.<a href="lib.jsp?uid=<%=libUser.getUserId()%>"><%=UserInfoUtil.getUser(libUser.getUserId()).getNickNameWml()%></a>(<%=libUser.getCount() %>)<br/><%
}
}%><%=paging.shuzifenye("flib.jsp",false,"|",response)%><%
} else {
%>您的好友还没有使用图库.<br/><%	
}
} else {
%><%=tip%><br/><%	
}
%><a href="lib.jsp">返回我的图库</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>