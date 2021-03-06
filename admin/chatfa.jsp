<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request, response);
UserBean loginUser = action.getLoginUser();
if(loginUser==null || !ForbidUtil.isForbid("chata",loginUser.getId())) {
	action.redirect("index.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷聊天室监察">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
int userId = action.getParameterInt("userId");
String tip = "添加完毕";
if(userId>0){
String bak = request.getParameter("bak");
int interval = action.getParameterInt("per");
if(interval < 15) interval = 15;
if(interval>1440) interval = 1440;
ForbidUtil.addForbid("chat",new ForbidUtil.ForbidBean(userId,loginUser.getId(),bak),interval);
LogUtil.logAdmin(loginUser.getId()+":"+userId+":添加聊天封禁"+interval+":"+bak);
%>
<%=tip%><br/>
<a href="chatf.jsp">返回列表</a><br/>
<%}else{%>
ID:<input name="userId" format="*N" maxlength="9"/><br/>
理由:<input name="bak" maxlength="100"/><br/>
期限:<select name="per">
<option  value="15">15分钟</option>
<option  value="60">1小时</option>
<option  value="360">6小时</option>
<option  value="1440">1天</option>
</select><br/>
<anchor title="确定">确定封禁
  <go href="chatfa.jsp" method="post">
    <postfield name="userId" value="$userId"/>
    <postfield name="bak" value="$bak"/>
    <postfield name="per" value="$per"/>
  </go>
</anchor><br/>
<a href="chatf.jsp">返回列表</a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>