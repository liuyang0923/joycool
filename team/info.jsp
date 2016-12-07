<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
UserBean loginUser = action.getLoginUser();
action.info();
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

TeamBean team = (TeamBean)request.getAttribute("team");
TeamUserBean tu = team.getUser(loginUser.getId());
int duty = (tu == null?-1:tu.getDuty());
%>
<%=StringUtil.toWml(team.getName())%>(<%=team.getCount()%>人)<br/>
(id:<%=team.getId()%>)<%=StringUtil.toWml(team.getInfo())%><br/>
分类：<%=TeamAction.getTeamClassName(team.getClass1())%><br/>
<%if(duty>=0||team.isFlagOutBrowse()){%><a href="chat.jsp">聊天</a><%}%>
<%if(duty>=0){%>|<a href="set2.jsp">修改设置</a><%}%>
<%if(duty>=8){%>|<a href="member.jsp">成员明细</a><%}%>
<%if(duty>=8){%>|<a href="set.jsp">修改圈子信息</a><%}%>
<br/>
<%if(duty>=8){%><a href="applys.jsp">入圈申请列表(<%=team.getApplySet().size()%>)</a><br/><%}%>
<%if(duty>=0){%>
---<a href="leave.jsp">退出这个圈子</a><br/>
<%if(duty==10){%><a href="invites.jsp">邀请好友加入</a><br/><%}%>
<%}else{%>
<%if(!team.isApply(loginUser.getId())){%>
<%if(team.isInvite(loginUser.getId())){%>
<a href="apply.jsp?g=<%=team.getId()%>">应邀加入</a><br/>
<%}else{%>
<a href="apply.jsp?g=<%=team.getId()%>">申请加入</a>|<a href="/chat/post.jsp?toUserId=<%=team.getDuty0()%>">与圈主联系</a><br/>
<%}%>
<%}%>
<%}%>

<%}%>
<a href="index.jsp">返回圈子首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>