<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%! static int NUMBER_OF_PAGE = 10;%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);

UserBean loginUser = action.getLoginUser();
if(ForbidUtil.isForbid("team",loginUser.getId())){
response.sendRedirect(("index.jsp"));
return;
}
action.removeNewChatUser();
action.chat();
String url = ("chat.jsp");
TeamBean team = (TeamBean)request.getAttribute("team");
boolean auto = false;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>

<%if(action.isResult("tip")){%>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<%if(team!=null){%><a href="chat.jsp">返回<%=team.getName()%></a><br/><%}%>
<%}else{%>
<%if((team.getFlag()&4)!=0){
out.clearBuffer();
response.sendRedirect(("index.jsp"));
return;
}
List chatList = team.getChatList();
PagingBean paging = new PagingBean(action, chatList.size(),NUMBER_OF_PAGE,"p");
int id = action.getParameterInt("to");		// 对某用户发言
TeamUserBean tu = team.getUser(loginUser.getId());
UserBean to = UserInfoUtil.getUser(id);
if(to == null)
	id = 0;
if(id==0&&tu!=null && tu.isFlag(1)&&paging.getCurrentPageIndex()==0)
	auto=true;
%>
<%if(auto){%>
<card id="c1" title="<%=StringUtil.toWml(team.getName())%>" ontimer="<%=response.encodeURL("chat.jsp")%>">
<timer value="300"/>
<%}else{%>
<card title="<%=StringUtil.toWml(team.getName())%>">
<%}%>
<%if(request.getAttribute("setvar")!=null){%><onevent type="onenterforward"><refresh><setvar name="tchat" value=""/></refresh></onevent><%}%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if((tu!=null&&tu.getDuty()==10)&&team.getApplySet().size()>0){%><a href="applys.jsp">入圈申请列表(<%=team.getApplySet().size()%>)</a><br/><%}%>
<%if(id!=0){%>to:<%=to.getNickNameWml()%>(<%=LoadResource.getPostionNameByUserId(to.getId())%>)<br/><%}%>
<%if(tu!=null||team.isFlagOutChat()){%>
<%if(auto){%><a href="#c2">发言</a><%}else{%>
<input name="tchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="<%if(id==0){%>chat.jsp<%}else{%>chat.jsp?tid=<%=id%><%}%>" method="post">
    <postfield name="content" value="$tchat"/>
</go></anchor><%}%>|<a href="<%if(id==0){%>act.jsp<%}else{%>act.jsp?to=<%=id%><%}%>">动作</a>|
<%}%><a href="chat.jsp">刷新</a><br/>
<%if(id==0){%>
<%=action.getChatString(chatList, paging.getStartIndex(), NUMBER_OF_PAGE, response)%>
<%=paging.shuzifenye("chat.jsp", false, "|", response)%>
<%}else{%>
<a href="/chat/post.jsp?toUserId=<%=id%>">与他/她私聊</a><br/>
<a href="teams.jsp?id=<%=id%>">查看他所在的圈子</a><br/>
<%}%>
<%if(tu!=null){%><a href="member.jsp">成员(<%=team.getCount()%>)</a>|<%}%>
<a href="info.jsp">圈子信息</a><br/>
<%}%>

<a href="index.jsp">返回圈子首页</a><br/>
</p>
</card><%if(auto){		// 如果自动刷新，发言页面放到card2
%><card id="c2" title="发言"><p align="left">
<input name="tchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="chat.jsp" method="post">
    <postfield name="content" value="$tchat"/>
</go></anchor>|<a href="#c1">返回</a></p></card><%}%>
</wml>