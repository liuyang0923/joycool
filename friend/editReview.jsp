<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
int marriageId = StringUtil.toInt(request.getParameter("marriageId"));
FriendAction action=new FriendAction(request);
if(action.isMarriageNow(marriageId)!=0)
{
%>
<card title="祝福的话">
<p align="left">
<%=BaseAction.getTop(request, response)%>
内容:<input name="review"  maxlength="100" value="v"/><br/>
 <anchor title="确定">发表祝福
    <go href="review.jsp?marriageId=<%=marriageId%>" method="post">
    <postfield name="review" value="$review"/>
    <postfield name="reviewUser" value="<%=loginUser.getId()%>"/>
    </go>
    </anchor><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
else{%>
<card title="祝福的话">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择婚礼<br/>
<a href="/friend/marriage.jsp">返回结婚礼堂</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>