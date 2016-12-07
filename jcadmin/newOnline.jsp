<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<p>在线列表</p>
<p>峰值：<%=JoycoolSessionListener.maxSessionCount%></p>
<table width="100%" border="2">
<tr>
<td width="10%"><strong>序号</strong></td>
<td width="10%"><strong>key</strong></td>
<td width="15%"><strong>用户名</strong></td>
<td width="15%"><strong>IP地址</strong></td>
<td width="40%"><strong>最后访问页面</strong></td>
<td width="20%"><strong>UA</strong></td>
</tr>
<%
Hashtable users = JoycoolSessionListener.getOnlineUsers();
Hashtable hts = new Hashtable();
Integer count = null;
String key = null;
if(users != null){
	Enumeration enu = users.keys();
	int i = 1;
	UserBean user;
	while(enu.hasMoreElements()){
		key = (String) enu.nextElement();
		user = (UserBean) users.get(key);
		count = (Integer) hts.get(user.getIpAddress());
		if(count != null){
			hts.put(user.getIpAddress(), new Integer(count.intValue() + 1));
		} else {
			hts.put(user.getIpAddress(), new Integer(1));
		}
%>
<tr>
<td width="10%"><%=i%></td>
<td width="10%"><%=key%></td>
<td width="15%"><%=user.getUserName()%>|<%=user.getNickName()%></td>
<td width="15%"><%=user.getIpAddress()%></td>
<td width="40%"><%=user.getLastVisitPage()%></td>
<td width="20%"><%=user.getUserAgent()%></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<table width="100%" border="1">
<tr>
<td width="10%"><strong>序号</strong></td>
<td width="45%"><strong>IP地址</strong></td>
<td width="45%"><strong>在线人数</strong></td>
</tr>
<%
Enumeration enu = hts.keys();
int i = 1;
String ip = null;
while(enu.hasMoreElements()){		
	ip = (String) enu.nextElement();
	count = (Integer) hts.get(ip);
%>
<tr>
<td width="10%"><%=i%></td>
<td width="45%"><%=ip%></td>
<td width="45%"><%=count.intValue()%></td>
</tr>
<%
	i ++;
}
%>
</table>