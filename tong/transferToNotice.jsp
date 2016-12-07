<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.transferToNotice(request);
String result=(String)request.getAttribute("result");
String tongId=(String)request.getAttribute("tongId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if("userError".equals(result)){%>
<card title="帮主转让" ontimer="<%=response.encodeURL("/tong/tong.jsp?tongId="+tongId)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=(String)request.getAttribute("tip")%>(3秒后跳转到帮会管理页面）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="帮主转让" ontimer="<%=response.encodeURL("/tong/tongManage.jsp?tongId="+tongId)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=(String)request.getAttribute("tip")%>(3秒后跳转到帮会管理页面）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>