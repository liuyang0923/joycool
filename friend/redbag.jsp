<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%int marriageId = StringUtil.toInt(request.getParameter("marriageId"));
FriendAction action=new FriendAction(request);%>
<card title="送红包">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%FriendMarriageBean friendMarriage=action.getOnesMarriage(marriageId);
UserBean loginUser= (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
	if(friendMarriage!=null ){
	if(action.isMarriageNow(marriageId)!=0){
	int fromId=friendMarriage.getFromId();
	int toId=friendMarriage.getToId();
	UserBean from=(UserBean)UserInfoUtil.getUser(fromId);
	UserBean to=(UserBean)UserInfoUtil.getUser(toId);
	if(loginUser.getId()==toId ||loginUser.getId()==fromId){
	out.clearBuffer();
	response.sendRedirect("review.jsp?marriageId="+marriageId);
	return;
	}%>
<%=StringUtil.toWml(from.getNickName())%>和<%=StringUtil.toWml(to.getNickName())%>婚礼：<br/>
设定送<input type="text" name="money" value=""/>乐币<br/>
 <anchor title="决定">决定
    <go href="/friend/review.jsp?marriageId=<%=marriageId%>" method="post">
      <postfield name="money" value="$money"/>
    </go>
    </anchor><br/>
<a href="/friend/review.jsp?marriageId=<%=marriageId%>">直接进入</a><br/>
<%
}else{
out.clearBuffer();
response.sendRedirect("marriage.jsp");
return;}
}else{%>
该婚礼不存在<br/>
<a href="/friend/marriage.jsp">返回结婚礼堂</a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>