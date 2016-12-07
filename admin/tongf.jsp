<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
UserBean loginUser = action.getLoginUser();
boolean isAdmin = loginUser!=null&&ForbidUtil.isForbid("tonga",loginUser.getId());
int id = action.getParameterInt("id");
if(isAdmin&&request.getParameter("del") != null){
	int deleteId = action.getParameterInt("del");
    ForbidUtil.deleteForbid("tong",deleteId,loginUser.getId());
    LogUtil.logAdmin(loginUser.getId()+":"+deleteId+":删除帮会封禁");
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷帮会监察">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="tongfa.jsp">添加封禁</a><br/>
<%if(id>0){
ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("tong",id);
if(forbid!=null){
UserBean user = UserInfoUtil.getUser(forbid.getValue());
if(user!=null){
UserBean operator = UserInfoUtil.getUser(forbid.getOperator());
%>
<a href="/chat/post.jsp?toUserId=<%=user.getId()%>" ><%=user.getNickNameWml()%></a>(<%=user.getId()%>)<br/>
理由:<%=forbid.getBak()%><br/>
封禁自<%=DateUtil.formatDate2(forbid.getStartTime())%><br/>
至<%=DateUtil.formatDate2(forbid.getEndTime())%><br/>
执行人:<%if(operator==null){%>管理员<%}else{%><%=operator.getNickNameWml()%><%}%><br/>
<%if(isAdmin){%><a href="tongf.jsp?del=<%=forbid.getValue()%>">立即解除</a><%}%>
<%}}%>
<a href="tongf.jsp">返回列表</a><br/>
<%}else{%>
<%
List forbidList = ForbidUtil.getForbidList("tong");
if(forbidList != null){
	Iterator itr = forbidList.iterator();
	int i = 1;
	while(itr.hasNext()){
	ForbidUtil.ForbidBean forbid = (ForbidUtil.ForbidBean)itr.next();
	UserBean user = UserInfoUtil.getUser(forbid.getValue());
	if(user==null)continue;
%>
<a href="tongf.jsp?id=<%=forbid.getValue()%>"><%=user.getNickNameWml()%></a>
<%if(isAdmin){%>-<a href="tongf.jsp?del=<%=forbid.getValue()%>">解除</a><%}%>
<br/>
<%
	    i ++;
	}
}
%>
<%}%>
<input name="search" format="*N" maxlength="9"/><a href="tongf.jsp?id=$search">按ID搜索</a><br/>
<a href="index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>