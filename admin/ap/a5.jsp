<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.spec.admin.*"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.framework.*" %><%!
%><%response.setHeader("Cache-Control","no-cache");
ApplyAction action = new ApplyAction(request);
UserBean loginUser = action.getLoginUser();
String tip = null;
if(!action.isMethodGet()) {
	StringBuilder sb = new StringBuilder(256);
	sb.append("举报ID:");
	sb.append(action.getParameterInt("toId"));
	sb.append('\n');
	sb.append("描述:");
	sb.append(action.getParameterString("con1"));
	action.addApply(5, sb.toString());
	tip = "申诉已成功发送,请耐心等待管理员的处理.";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="申诉站">
<p align="left">
<%if(tip!=null){%><%=tip%><br/><%}else{%>
【举报挂机行为/游戏漏洞】<br/>
!!请认真填写以下内容(如果某些项目不需要填写,可以留空)<br/>
输入要举报的ID:
<input name="toId" format="*N" maxlength="9"/><br/>
请填写详细说明:
<input name="con1" maxlength="200"/><br/>
<anchor title="确定">确认无误并提交
<go href="a5.jsp" method="post">
	<postfield name="toId" value="$toId"/>
	<postfield name="con1" value="$con1"/>
</go>
</anchor><br/>
<%}%>
<a href="index.jsp">返回申诉站首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>