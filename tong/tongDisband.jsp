<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongDisband(request);
String result =(String)request.getAttribute("result");
//UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/tong/tongList.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="帮会同盟" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/tong/tongList.jsp">帮会列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong =(TongBean)request.getAttribute("tong");
TongBean fTong =(TongBean)request.getAttribute("fTong");
%>
<card title="帮会同盟">
<p align="left">
<%=BaseAction.getTop(request, response)%>
解除同<%=StringUtil.toWml((String)fTong.getTitle()) %>的盟约<br/>
毁约金10亿乐币（从帮会基金中扣除）<br/>
<anchor title="确定">确定
  <go href="/tong/tongDisbandResult.jsp" method="post">
    <postfield name="tongId" value="<%=tong.getId()%>"/>
    <postfield name="fTongId" value="<%=fTong.getId()%>"/>
  </go>
</anchor>|
<anchor title="确定">取消
  <go href="/tong/tong.jsp" method="post">
  	<postfield name="tongId" value="<%=tong.getId()%>"/>
  </go>
</anchor><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle()) %></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>