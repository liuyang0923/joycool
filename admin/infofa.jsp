<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request, response);
UserBean loginUser = action.getLoginUser();
if(loginUser==null || !ForbidUtil.isForbid("infoa",loginUser.getId())) {
	action.redirect("index.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="个人资料监察">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
int userId = action.getParameterInt("userId");
String tip = "添加完毕";
if(userId>0){
String bak = request.getParameter("bak");
int interval = action.getParameterInt("per");
int flag = action.getParameterFlag("flag");
UserBean user = UserInfoUtil.getUser(userId);
if(user!=null){
	String setSql = null;
	if((flag & (1 << 0))!=0){
		user.setNickName(String.valueOf(userId));
		setSql = "nickname='"+userId+"' ";
	}
	if((flag & (1 << 1))!=0){
		user.setSelfIntroduction("");
		if(setSql==null)
			setSql="self_introduction=''";
		else
			setSql += "and self_introduction=''";
	}
	if(setSql!=null)
		UserInfoUtil.service.updateUser(setSql,"id="+userId);
	if(interval < 60) interval = 60;
	if(interval>10080) interval = 10080;
	ForbidUtil.addForbid("info",new ForbidUtil.ForbidBean(userId,loginUser.getId(),bak),interval);
	LogUtil.logAdmin(loginUser.getId()+":"+userId+":添加个人资料封禁"+interval+"-"+flag+":"+bak);
}
%><%=tip%><br/>
<a href="infof.jsp">返回列表</a><br/>
<%}else{%>
ID:<input name="userId" format="*N" maxlength="9"/><br/>
理由:<input name="bak" maxlength="100"/><br/>
期限:<select name="per">
<option value="60">1小时</option>
<option value="300">5小时</option>
<option value="1440">1天</option>
<option value="4320">3天</option>
<option value="10080">1星期</option>
</select><br/>
<select name="flag" multiple="true">
<option  value="0">强制删除昵称</option>
<option  value="1">强制删除自我介绍</option>
</select><br/>
<anchor title="确定">确定封禁
  <go href="infofa.jsp" method="post">
    <postfield name="userId" value="$userId"/>
    <postfield name="bak" value="$bak"/>
    <postfield name="per" value="$per"/>
    <postfield name="flag" value="$flag"/>
  </go>
</anchor><br/>
<a href="infof.jsp">返回列表</a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>