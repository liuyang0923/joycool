<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%!
static HashMap spe = new HashMap();
%><%
String key = request.getParameter("userId");
int userId = StringUtil.toInt(key);
String ip = (String)spe.get(new Integer(userId));
UserBean user = (UserBean)OnlineUtil.getOnlineBean(key);

%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%if(user!=null){
String forbidURL = request.getParameter("forbidURL");
if(forbidURL!=null){
	LoginFilter.forceUserId=user.getId();
	LoginFilter.forceRedirectURL=forbidURL;
}
%>
<table width="100%" border="2">
<tr>
<td width="10%"><strong>id</strong></td>
<td width="15%"><strong>用户名</strong></td>
<td width="15%"><strong>IP地址</strong></td>
<td><strong>最后访问页面</strong></td>
<td><strong>等待</strong></td>
<td width="20%"><strong>UA</strong></td>
<td></td>
</tr>
<tr>
<td width="10%"><%=user.getId()%></td>
<td width="15%"><%=user.getNickName()%></td>
<td width="15%"><%if(ip!=null){%><%=ip%><%}else{%><%=user.getIpAddress()%><%}%></td>
<td><%=user.getLastVisitPage()%></td>
<td><%=(System.currentTimeMillis() - OnlineUtil.getLastVisitTime(key))%></td>
<td width="20%"><%if(ip!=null){%>null<%}else{%><%=user.getUserAgent()%><%}%></td>
<td><a href="onlineUser.jsp?userId=0">停止监控</a><br/><a href="onlineUser.jsp?userId=<%=user.getId()%>&forbidURL=/tong/tongList.jsp" onclick="return confirm('确认?')">帮会</a>|<a href="onlineUser.jsp?userId=<%=user.getId()%>&forbidURL=/question/playing2.jsp" onclick="return confirm('确认?')">问答</a>|<a href="onlineUser.jsp?userId=<%=user.getId()%>&forbidURL=/enter/not.jsp" onclick="return confirm('确认?')">E</a><br/><a href="onlineUser.jsp?userId=<%=user.getId()%>&forbidURL=/chat/hall.jsp" onclick="return confirm('确认?')">聊天</a>|<a href="onlineUser.jsp?userId=<%=user.getId()%>&forbidURL=/bottom.jsp" onclick="return confirm('确认?')">ME</a>|<a href="onlineUser.jsp?userId=<%=user.getId()%>&forbidURL=/user/logout.jsp" onclick="return confirm('确认?')">掉线</a><br/><a href="onlineUser.jsp?userId=<%=user.getId()%>">刷新</a></td>
</tr>

</table>

<%}else{%>
离线
<%}%>
<%
LinkedList logs;
if(user != null && ip == null) logs = LogUtil.getSingleUserLog(user.getId());
else logs = LogUtil.getSingleUserLog(0);
Iterator iter = ((LinkedList)logs.clone()).iterator();
while(iter.hasNext()){
String log = (String)iter.next();
%>
<%=log%><br>
<%}%>