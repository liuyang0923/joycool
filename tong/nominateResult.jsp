<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.nominateResult(request);
String result=(String)request.getAttribute("result");
String tongId=(String)request.getAttribute("tongId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="副帮主任命结果" ontimer="<%=response.encodeURL("/tong/nominateAssistant.jsp?tongId="+tongId)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(result!=null&&result.equals("failure")){%>
<%=request.getAttribute("tip")%><br/>
<a href="/tong/nominateAssistant.jsp?tongId=<%=tongId%>">直接返回 </a><br/>
<%}else if(result!=null&&result.equals("success")){%>
<%=request.getAttribute("tip")%><br/>
<a href="/tong/nominateAssistant.jsp?tongId=<%=tongId%>">直接返回 </a><br/>
<%}else {%>
参数错误！<br/>
<a href="/tong/nominateAssistant.jsp?tongId=<%=tongId%>">直接返回 </a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>