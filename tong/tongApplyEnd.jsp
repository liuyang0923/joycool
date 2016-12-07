<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action= new TongAction(request);
action.tongApplyEnd(request);
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="帮会列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
TongBean tong=(TongBean)request.getAttribute("tong");
//response.sendRedirect(("/tong/tongApplyList.jsp?tongId="+tong.getId()));
BaseAction.sendRedirect("/tong/tongApplyList.jsp?tongId="+tong.getId(), response);
return;
}else{
TongBean tong=(TongBean)request.getAttribute("tong");
String url=("/tong/tongApplyList.jsp?tongId="+tong.getId());
%>
<card title="帮会管理" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>