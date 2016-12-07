<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.buyBarren(request);
String result=(String)request.getAttribute("result");
String tongId=(String)request.getAttribute("tongId");
TongBean tong=(TongBean)request.getAttribute("tong");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result!=null){%>
<card title="买荒城信息" ontimer="<%=response.encodeURL("/tong/tong.jsp?tongId="+tongId)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/tong/tong.jsp?tongId=<%=tongId%>">直接跳转 </a> <br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(tong!=null){%>
<card title="买荒城信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
城市管理员:想购买这座空城吗?缴纳1亿乐币即可.你现有乐币<%=request.getAttribute("money")%>,存款<%=request.getAttribute("bank")%><br/>
<a href="/tong/buyBarrenResult.jsp?tongId=<%=tong.getId()%>">确定购买</a><br/>
<a href="/bank/bank.jsp">取存款</a><br/><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="买荒城信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您要购买的荒城不存在！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>