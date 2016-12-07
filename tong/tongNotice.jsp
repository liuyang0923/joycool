<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongNotice(request);
String result =(String)request.getAttribute("result");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong = (TongBean)request.getAttribute("tong");
UserBean user=null;
user=UserInfoUtil.getUser(tong.getUserId());
%>
<card title="<%=StringUtil.toWml(tong.getTitle())%>帮">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%-- liuyi 2007-01-16 帮会修改 start --%>
<%= StringUtil.toWml(tong.getDescription()) %><br/>
<%-- liuyi 2007-01-16 帮会修改 end --%>
<%=StringUtil.toWml(tong.getTitle()) %>帮成立于<%=tong.getCreateDatetime() %>日，现有帮众<%=tong.getUserCount() %>人.
<%if(user!=null){%>
在帮主<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName()) %></a>的带领下，已经成为乐酷第<%=action.getTongCompositor(tong.getUserCount())%>大帮。
<%}%>
<br/>
副帮主：
<%
if(tong.getUserIdA()>0){
user=UserInfoUtil.getUser(tong.getUserIdA());
if(user!=null){%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName()) %></a>
<%}
%><%}
if(tong.getUserIdB()>0){
user=UserInfoUtil.getUser(tong.getUserIdB());
if(user!=null){%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName()) %></a><br/>
<%}}
if(tong.getUserIdA()==-1 && tong.getUserIdB()==-1){%>该职位目前空缺中!<%}%><br/>
<a href="/tong/tongUserList.jsp?tongId=<%=tong.getId()%>">所有帮众</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>