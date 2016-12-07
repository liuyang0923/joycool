<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongCadreJump(request);
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
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
String userId=(String)request.getAttribute("userId");
UserBean user = UserInfoUtil.getUser(StringUtil.toInt(userId));
%>
<card title="任命帮会高层">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您要将<%=StringUtil.toWml(user.getNickName()) %>任命为<br/>
<input name="name"  maxlength="6" value="干事"/>(称号,6个汉字以内)<br/>
<anchor title="确定">确定
  <go href="tongCadreResult.jsp" method="post">
    <postfield name="userId" value="<%=userId%>"/>
    <postfield name="tongId" value="<%=tong.getId()%>"/>
    <postfield name="name" value="$name"/>
  </go>
</anchor><br/>
<br/>
<a href="/tong/nominateAssistant.jsp?tongId=<%=tong.getId()%>">返回任命助手 </a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理帮会 </a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>