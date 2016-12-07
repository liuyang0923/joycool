<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
UserBean loginUser = action.getLoginUser();
ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("tong",loginUser.getId());
if(forbid==null){
	if(loginUser.getUs2().getRank()<5)
		action.doTip("failure", "等级不够5级,无法申请加入帮会");
	else
		action.tongApplyResult(request);
} else {
	action.doTip("failure", "已经被禁止帮会活动 - " + forbid.getBak());
}
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
<%=request.getAttribute("tip") %>（3秒钟跳转到城帮首页）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
TongBean tong = (TongBean)request.getAttribute("tong");
//response.sendRedirect(("/tong/tong.jsp?tongId="+tong.getId()));
BaseAction.sendRedirect("/tong/tong.jsp?tongId="+tong.getId(), response);
return;
}else if(result.equals("contentError")){
TongBean tong = (TongBean)request.getAttribute("tong");
String url=("/tong/tongApply.jsp?tongId="+tong.getId());
%>
<card title="申请入帮" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>（3秒钟跳转到帮会申请首页）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("exist")){
TongBean tong = (TongBean)request.getAttribute("tong");
String url=("/tong/tong.jsp?tongId="+tong.getId());
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml((String)request.getAttribute("tip"))%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong = (TongBean)request.getAttribute("tong");
String url=("/tong/tong.jsp?tongId="+tong.getId());
%>
<card title="<%=StringUtil.toWml(tong.getTitle())%>帮" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
帮会申请已经成功！请安心等待！（3秒钟跳转到帮会首页）<br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>