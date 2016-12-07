<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.spec.admin.*"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.framework.*" %><%!
%><%response.setHeader("Cache-Control","no-cache");
ApplyAction action = new ApplyAction(request);
UserBean loginUser = action.getLoginUser();
String tip = null;
if(!action.isMethodGet()) {
	StringBuilder sb = new StringBuilder(256);
	sb.append("被盗ID:");
	sb.append(action.getParameterInt("toId"));
	sb.append('\n');
	sb.append("银行密码:");
	sb.append(action.getParameterString("bank"));
	sb.append('\n');
	sb.append("被盗物品:");
	sb.append(action.getParameterString("con1"));
	sb.append('\n');
	sb.append("描述:");
	sb.append(action.getParameterString("con2"));
	action.addApply(3, sb.toString());
	tip = "申诉已成功发送,请耐心等待管理员的处理.";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="申诉站">
<p align="left">
<%if(tip!=null){%><%=tip%><br/><%}else{%>
【被盗号要求取回】<br/>
!!请认真填写以下内容(如果某些项目不需要填写,可以留空)<br/>
输入被盗的ID:
<input name="toId" format="*N" maxlength="9"/><br/>
如果记得银行密码(或者其中部分),请填写:
<input name="bank" maxlength="20"/><br/>
如果是被盗了乐币和道具,请填写被盗内容:
<input name="con2" maxlength="200"/><br/>
请说出该帐户的一些信息以证明(例如银行密码,行囊赠送和最近转帐赠送情况等):
<input name="con1" maxlength="200"/><br/>
<anchor title="确定">确认无误并提交
<go href="a3.jsp" method="post">
	<postfield name="toId" value="$toId"/>
	<postfield name="bank" value="$bank"/>
	<postfield name="con1" value="$con1"/>
	<postfield name="con2" value="$con2"/>
</go>
</anchor><br/>
<%}%>
<a href="index.jsp">返回申诉站首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>