<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.service.infc.IChatStatService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="java.util.*" %><%@ page import="java.sql.*" %><%@ page import="net.joycool.wap.bean.chat.ChatStatBean" %><%response.setHeader("Cache-Control","no-cache");%>
<%
ChatStatBean chatStat=null;
IChatStatService chatStatService=ServiceFactory.createChatStatService();
Vector chatList=(Vector)chatStatService.getChatStatList("1=1 order by create_datetime desc");
%>
<html>
<head>
</head>
<body>
每天专门针对聊天的统计（针对2级以上的用户）<BR>
<table width="800" border="1">
<th align="center" width="40">排名</th>
<th align="center">时间</th>
<th align="center">聊天</th>
<th align="center">动作</th>
<th align="center">信件</th>
<th align="center">PK</th>
<th align="center">发主动信息的用户数</th>
<th align="center">平均发送人数</th>
<th align="center">发送好友数量</th>
<th align="center">未回复数量</th>
<th align="center">2级以上用户数量</th>
<th align="center">好友数量</th>
<%
if(chatList!=null)
{
for(int j=0;j<chatList.size();j++){
	chatStat=(ChatStatBean)chatList.get(j);
%>
<tr>
<td><%=(j+1)%></td>
<td><%=chatStat.getCreateDateTime() %></td>
<td><%=chatStat.getChatNum() %></td>
<td><%=chatStat.getActionNum()%></td>
<td><%=chatStat.getMessageNum() %></td>
<td><%=chatStat.getPkNum()%></td>
<td><%=chatStat.getUserCount()%></td>
<td><%=chatStat.getSendToPerson()  %></td>
<td><%=chatStat.getSendFriend()  %></td>
<td><%=chatStat.getNoReply()  %></td>
<td><%=chatStat.getUserTotal()  %></td>
<td><%=chatStat.getFriendNum()  %></td>
<%}
}%>
</table>
<br/>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html>