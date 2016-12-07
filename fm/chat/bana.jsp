<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="jc.family.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.*" %><%
response.setHeader("Cache-Control","no-cache");
FamilyAction action=new FamilyAction(request);
FamilyUserBean fmUser=action.getFmUser();
if(fmUser==null){
	response.sendRedirect("/fm/index.jsp");
	return;
}
action.ban();
int fid = fmUser.getFmId();
String result =action.getResult();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip() %><br/>
<a href="chat.jsp">返回聊天室</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("op")){
%>

<card title="聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="chat.jsp">返回聊天室</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
ForbidUtil.ForbidGroup fgroup = (ForbidUtil.ForbidGroup)request.getAttribute("fgroup");
List bUserSet = fgroup.getForbidList();
int uid = action.getParameterInt("uid");
%>
<card title="聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
用户ID:<%if(uid!=0){%><%=uid%><%}else{%><input name="userId2" format="*N" maxlength="9"/><%}%><br/>
理由:<input name="bak" maxlength="100"/><br/>
期限:<select name="per">
<option value="5">5分钟</option>
<option value="20">20分钟</option>
<option value="60">1小时</option>
<option value="240">4小时</option>
<option value="720">12小时</option>
<option value="1440">1天</option>
<option value="4320">3天</option>
<option value="10080">7天</option>
<option value="21600">15天</option>
<option value="43200">30天</option>
</select><br/>
<anchor title="确定">封禁发言
  <go href="ban.jsp?op=add&amp;fid=<%=fid%>" method="post">
    <postfield name="userId" value="<%if(uid!=0){%><%=uid%><%}else{%>$userId2<%}%>"/>
    <postfield name="bak" value="$bak"/>
    <postfield name="per" value="$per"/>
  </go>
</anchor>|<anchor title="确定">踢出聊天室
  <go href="ban.jsp?op=add&amp;fid=<%=fid%>&amp;k=1" method="post">
    <postfield name="userId" value="<%if(uid!=0){%><%=uid%><%}else{%>$userId2<%}%>"/>
    <postfield name="bak" value="$bak"/>
    <postfield name="per" value="$per"/>
  </go>
</anchor><br/>
----------<br/>
<input name="userId" format="*N" maxlength="9"/>
<anchor title="确定">查询封禁ID
  <go href="ban.jsp?fid=<%=fid%>" method="post">
    <postfield name="search" value="$userId"/>
  </go>
</anchor><br/>
<br/>
<a href="ban.jsp">返回封禁列表</a><br/>
&lt;<a href="chat.jsp">聊天室</a>&lt;<a href="../myfamily.jsp?id=<%=fid%>">返回家族</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>