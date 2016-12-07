<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.*"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
AntiAction action = new AntiAction(request);
int rand = 0;
int id = action.getParameterInt("id");

AntiAction.AntiUser user = action.getAntiUser();
if(user!=null) {
	if(user.rand==id) {
		AntiAction.unCheckUser(user.userId);
		if(user.url!=null){
			response.sendRedirect((user.url));
			return;
		}
	} else {
		rand=user.rand;
	}
}
if(rand==0){
	response.sendRedirect(("/lswjs/index.jsp"));
	return;
}
int rand2 = RandomUtil.nextInt(100);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="系统异常">
<p align="left">
===系统异常===<br/>
<%if(rand2<50){%>
<a href="/user/logout.jsp">尝试注销并重新登陆</a><br/>
<%}else{%>
<a href="/user/logout.jsp">尝试注销</a><a href="/user/logout.jsp">并</a><a href="/user/logout.jsp">重新登陆</a><br/>
<%}%>
异常级别：C<br/>
异常原因：系统繁忙资源不足。<br/>
系统检测到潜在的异常而中止，造成的不便请谅解。<br/>
<%if(rand%23==0){%><br/><%}%>
请选择下面的链接提交错误报告：<br/>
<a href="/farm/aAU.jsp?id=<%=rand%>">提交</a><br/>
<%if(rand%2==0){%><br/><%}%>
或者点击以下链接直接返回：<br/>
<a href="/farm/aAU.jsp?send=false&amp;id=<%=rand%>">返回</a><br/>
<%if(rand%5==0){%><br/><%}%>
如果异常无法消除请联系管理员：<br/>
<a href="/user/onlineManager.jsp?forumId=355">警察局</a><br/>
<%if(rand2<50){%>
<br/>
<a href="/user/logout.jsp">尝试注销</a><a href="/user/logout.jsp">并重新登陆</a><br/>
<%}%>
</p>
</card>
</wml>