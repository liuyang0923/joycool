<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
action.jyList(request);
List jvList = (List)request.getAttribute("jvList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的金兰">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(jvList.size()>0){
int k=0;
for(int i=0;i<jvList.size();i++){
    Integer userId=(Integer)jvList.get(i);
    if(userId==null)continue;
%>
<%=k+1%>.<a href="/user/ViewUserInfo.do?userId=<%=userId%>"><%=StringUtil.toWml(UserInfoUtil.getUser(userId.intValue()).getNickName())%></a><br/>
<%k++;}
String fenye = action.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", "pageIndex" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}}else{%>对不起,没有查询到您所结交的金兰朋友!<br/><%}%>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>