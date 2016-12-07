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
<card title="聊天室" ontimer="<%=response.encodeURL("chat.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip() %>(3秒后返回聊天室)<br/>
<a href="chat.jsp">返回聊天室</a><br/>
<a href="../index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("op")){
%>

<card title="聊天室" ontimer="<%=response.encodeURL("ban.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip() %>(3秒后返回封禁列表)<br/>
<a href="ban.jsp?fid=<%=fid%>">返回封禁列表</a><br/>
<a href="chat.jsp?fid=<%=fid%>">返回聊天室</a><br/>
<a href="../myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
ForbidUtil.ForbidGroup fgroup = (ForbidUtil.ForbidGroup)request.getAttribute("fgroup");
List bUserSet = fgroup.getForbidList();
%>
<card title="聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="bana.jsp?fid=<%=fid%>">添加封禁玩家</a><br/>
===封禁列表===<br/>
<%
if(bUserSet.size()>0){
PagingBean paging = new PagingBean(action,bUserSet.size(),10,"p");
int start = paging.getStartIndex();
int end = paging.getEndIndex();
Iterator it = bUserSet.iterator();
int searchUser = action.getParameterInt("search");
if(searchUser==0){
int i = 0;
long now = System.currentTimeMillis();
while(it.hasNext()&&i<end){
	ForbidUtil.ForbidBean forbid = (ForbidUtil.ForbidBean)it.next();
	int userId=forbid.getValue();
	i++;
	if(i<=start) continue;
	
	UserBean user  = net.joycool.wap.util.UserInfoUtil.getUser(userId);
	if(user==null){continue;}%>
<%=i%><a href="ban.jsp?search=<%=user.getId()%>&amp;fid=<%=fid%>"><%=StringUtil.toWml(user.getNickName())%></a>(<%=DateUtil.formatTimeInterval(forbid.getEndTime()-now)%>)
<a href="ban.jsp?op=del&amp;userId=<%=user.getId()%>&amp;fid=<%=fid%>" >解禁</a><br/>
<%}%>
<%=paging.shuzifenye("ban.jsp?fid="+fid, true, "|", response)%>
<%}else if(fgroup.isForbid(searchUser)){
ForbidUtil.ForbidBean forbid = fgroup.getForbid(searchUser);
UserBean user  = UserInfoUtil.getUser(searchUser);
if(user!=null){%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a><br/>
理由:<%=forbid.getBak()%><br/>
封禁自<%=DateUtil.formatDate2(forbid.getStartTime())%><br/>
至<%=DateUtil.formatDate2(forbid.getEndTime())%><br/>
执行人:(隐藏)<br/>
<a href="ban.jsp?op=del&amp;userId=<%=user.getId()%>&amp;fid=<%=fid%>">确认解禁</a><br/>
<a href="ban.jsp?fid=<%=fid%>">返回列表</a><br/>
<%}}else{%>
(没有查询到封禁用户)<br/>
<a href="ban.jsp?fid=<%=fid%>">返回列表</a><br/>
<%}%>
<input name="userId" format="*N" maxlength="9"/>
<anchor title="确定">查询封禁ID
  <go href="ban.jsp?fid=<%=fid%>" method="post">
    <postfield name="search" value="$userId"/>
  </go>
</anchor><br/>
<%}else{%>封禁列表为空！<br/><%}%>
<br/>
&gt;&gt;<a href="../manage/chat.jsp">置顶信息设置</a><br/>
&lt;<a href="chat.jsp">聊天室</a>&lt;<a href="../myfamily.jsp?id=<%=fid%>">返回家族</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>