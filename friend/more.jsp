<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   int uid = action.getParameterInt("uid");
   int userId = 0;
   String tip = "";
   List list = null;
   UserBean user = null;
   PagingBean paging = null;
   if (uid == 0){
	   uid = action.getLoginUser().getId();
   }
   user = UserInfoUtil.getUser(uid);
   if (user == null){
	   tip = "用户不存在";
   } else {
	   list = action.getMoreGradeUserList(uid,-1);
	   paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
if (list.size() == 0){

%>还没有可信度≥80%的用户给<%=user.getId() == action.getLoginUser().getId()?"您":user.getGenderText()%>打过分哦.<br/><%
} else {
for (int i=paging.getStartIndex();i<paging.getEndIndex();i++){
	userId = StringUtil.toInt(list.get(i).toString());
	if (userId > 0){
		user = UserInfoUtil.getUser(userId);
		if (user != null){
			%><%=i+1 %>.<a href="../chat/post.jsp?toUserId=<%=user.getId()%>"><%=user.getNickNameWml()%></a><br/><%
		}
	}
}
}
if (uid == action.getLoginUser().getId()){
%><%=paging.shuzifenye("more.jsp",false,"|",response)%><a href="credit.jsp">[返回可信度首页]</a><br/><%
} else {
%><%=paging.shuzifenye("more.jsp?uid=" + uid,true,"|",response)%><a href="credit.jsp?uid=<%=uid %>">[返回可信度首页]</a><br/><%	
}%><%
} else {
%><%=tip%><br/><a href="flist.jsp">返回</a><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>