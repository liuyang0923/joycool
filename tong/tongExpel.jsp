<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@include file="../bank/checkpw.jsp"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongExpel(request);
String result =(String)request.getAttribute("result");
String url=("/tong/tongList.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="帮会列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/tong/tongList.jsp">帮会列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("error")){
TongBean tong=(TongBean)request.getAttribute("tong");
url=("/tong/tongUserList.jsp?tongId="+tong.getId());
%>
<card title="帮会列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转回帮会会员)<br/>
<a href="/tong/tongUserList.jsp?tongId=<%=tong.getId()%>">帮会会员</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
url=("/tong/tongUserList.jsp?tongId="+tong.getId());
%>
<card title="帮会会员" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
操作成功!(3秒后跳转回帮会会员)<br/>
<a href="/tong/tongUserList.jsp?tongId=<%=tong.getId()%>">直接返回</a> <a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>