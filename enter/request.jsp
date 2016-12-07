<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%
String ip = request.getRemoteAddr();
CustomAction action = new CustomAction(request);
int type = action.getParameterInt("type");
if(!action.isMethodGet()){
	String con1 = action.getParameterNoEnter("con1");
	String con4 = action.getParameterNoEnter("con4");
	if(con1 == null)	con1 = "";
	if(con4 == null)	con4 = "";
	int con2 = action.getParameterInt("con2");
	int con3 = action.getParameterInt("con3");
	int userId = 0;
	if(action.getLoginUser() != null){
		userId = action.getLoginUser().getId();
	}
	SqlUtil.executeUpdate("insert into ip_apply (ip,addr,card,browser,info,create_time,user_id) values('"+ip+"','"+StringUtil.toSql(con1)+"',"+con2+","+con3+",'"+StringUtil.toSql(con4)+"',now(),"+userId+")", 5);
	action.tip("ok", "申请已经提交成功!<br/>请耐心等待处理.");
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="乐酷社区"><p align="left">
<%if(action.isMethodGet()){%>
你现在的IP:<%=ip%><br/>
==请填写申请详单==<br/>
所在地(省市):<br/><input name="con1"  maxlength="30"/><br/>
手机卡属于:<select name="con2">
<option value="0">未选择</option>
<option value="1">移动</option>
<option value="2">联通</option>
<option value="3">电信</option>
<option value="4">其他</option>
</select><br/>
浏览器:<select name="con3">
<option value="0">未选择</option>
<option value="1">手机自带</option>
<option value="2">UCweb</option>
<option value="3">航海家</option>
<option value="4">Operamini</option>
<option value="5">其他</option>
</select><br/>
其他说明(可不填):<input name="con4"  maxlength="50"/><br/>
<anchor title="确定">提交申请开通访问
<go href="request.jsp?type=<%=type%>" method="post">
	<postfield name="con1" value="$con1"/>
	<postfield name="con2" value="$con2"/>
	<postfield name="con3" value="$con3"/>
	<postfield name="con4" value="$con4"/>
</go></anchor><br/>
<%}else{%>
<%=action.getTip()%><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>