<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,java.util.regex.Matcher,java.util.regex.Pattern,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<%!public int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   String tip = "";
   int submit = action.getParameterInt("s");
   UserBean user = null;
   UserInfo userInfo = null;
// String frineds = action.List2String(UserInfoUtil.getUserFriends(action.getLoginUser().getId()));
   List friendList =UserInfoUtil.getUserFriends(action.getLoginUser().getId()); // action.service.getUserInfoList(" user_id in (" + frineds + ")");
   PagingBean paging = new PagingBean(action, friendList.size(), COUNT_PRE_PAGE, "p");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request,response)%>
<%if ("".equals(tip)){
if(friendList.size() > 0){
%>我的好友列表:<br/><%
for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
//userInfo = (UserInfo)friendList.get(i);
user = UserInfoUtil.getUser(StringUtil.toInt(friendList.get(i).toString()));
if (user == null) continue;
%><%=i+paging.getStartIndex()+1 %>.<a href="credit.jsp?uid=<%=user.getId()%>"><%if(user.getNickNameWml()==null || "".equals(user.getNickNameWml())){%>游客<%=user.getId()%><%}else{%><%=user.getNickNameWml()%><%}%></a><br/><%
}%><%=paging.shuzifenye("flist.jsp",false,"|",response)%><%	
} else {
%>您还没有好友,不能打分.<br/><%
}
%><a href="credit.jsp">[返回可信度首页]</a><br/><%
} else {
	%><%=tip%><br/><a href="phone.jsp">返回</a><br/><%
}
%><%=BaseAction.getBottomShort(request, response)%></p></card></wml>