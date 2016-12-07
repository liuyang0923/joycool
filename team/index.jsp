<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
int userId = 0;
if(action.getLoginUser() != null) {
	action.removeNewChatUser();
	userId = action.getLoginUser().getId();
	action.index();
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%
if(userId!=0){
if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

List teamList = (List)request.getAttribute("teamList");
for(int i = 0;i < teamList.size();i++){
TeamBean team = action.getTeam(((Integer)teamList.get(i)).intValue());
if(team==null||(team.getFlag()&4)!=0) continue;
%><%=i+1%>.<a href="chat.jsp?ti=<%=team.getId()%>"><%=StringUtil.toWml(team.getName())%></a>
<%if(team.isNewChat(userId)){%>(未读)<%}%>
-<a href="info.jsp?ti=<%=team.getId()%>">查看</a><br/>
<%}%>

<%}}%>
圈子id:<input name="ti" format="*N" maxlength="5"/>
<anchor title="确定">进入
  <go href="info.jsp">
    <postfield name="ti" value="$ti"/>
  </go>
</anchor><br/>
<a href="all.jsp">查看所有圈子</a><br/>
<a href="create.jsp">创建我的圈子</a><br/>
<a href="question/index.jsp">缘分测试</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>