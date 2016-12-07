<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.mall.*,net.wxsj.action.mall.*,java.util.*,net.wxsj.util.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");

MallAction action = new MallAction();
action.postReply(request, response);

String result = (String) request.getAttribute("result");

int parentId = StringUtil.toInt(request.getParameter("parentId"));
//进入页面
if(result == null){
%>
<wml>
<card title="点评信息">
<p align="left">
点评信息<br/>
--------<br/>
点评内容：<br/>
<input type="text" name="content" maxlength="500"/><br/>
<anchor>发表点评
  <go href="postReply.jsp?zzz=1" method="post">    
	<postfield name="content" value="$content"/>
	<postfield name="parentId" value="<%=parentId%>"/>
  </go>
</anchor><br/>
<a href="info.jsp?id=<%=parentId%>">返回主贴</a><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>
<%
	return;
}
//失败
else if("failure".equals(result)){
	String tip = (String) request.getAttribute("tip");
%>
<wml>
<card title="点评信息">
<p align="left">
点评信息<br/>
--------------<br/>
<%=StringUtil.toWml(tip)%><br/>
<anchor>返回
<prev/>
</anchor><br/>
<a href="info.jsp?id=<%=parentId%>">返回主贴</a><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>
<%
	return;
}
//成功
else if("success".equals(result)){	
%>
<wml>
<card title="点评信息" ontimer="<%=response.encodeURL("info.jsp?id="+parentId)%>">
<timer value="30"/>
<p align="left">
点评信息<br/>
--------------<br/>
发表成功<br/>
<a href="info.jsp?id=<%=parentId%>">返回主贴</a><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>
<%
	return;
}
%>