<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%TongAction action=new TongAction(request);
//action.tongErect(request);
String result="Error";//(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result!=null){%>
<card title="帮会申请" ontimer="<%=response.encodeURL("/tong/tongList.jsp")%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(result.equals("Error")){%>
帮会数量已满，请过几天再申请<br/>
或者可以选择购买一个无人荒城.<br/>
<%}else{%>
<%=request.getAttribute("tip")%><br/>
<%}%>
（5秒后自动跳转到帮会列表页面）<br/>
<a href="/tong/tongList.jsp">直接返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="帮会申请">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请输入帮会名称（不超过6个字）<br/>
<input name="tongName"  maxlength="6" value="" title="名称"/><br/>
<anchor title="确定">确定
    <go href="/tong/tongLocation.jsp" method="post">
    <postfield name="tongName" value="$tongName"/>
    </go>
</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>