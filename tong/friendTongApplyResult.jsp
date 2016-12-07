<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.DateUtil,net.joycool.wap.cache.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
TongBean tong = action.getTong(StringUtil.toInt(request.getParameter("tongId")));
if(tong==null){
	BaseAction.sendRedirect("/tong/tongList.jsp", response);
	return;
}
action.friendTongApplyResult(request);
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if("failure".equals(result)){
String url=("/tong/tongList.jsp");
%>
<card title="城帮结盟" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>（3秒钟跳转到城帮首页）<br/>
<br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%= StringUtil.toWml(tong.getTitle()) %></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{ 
TongBean myTong = (TongBean)request.getAttribute("myTong");
%>
<card title="城帮结盟" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
您已代表<%= StringUtil.toWml(myTong.getTitle()) %>向<%= StringUtil.toWml(tong.getTitle()) %>提出结盟，请等待答复（3秒跳转）<br/>
<a href="/tong/friendTongs.jsp?tongId=<%=myTong.getId()%>">马上返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%} %>
</wml>