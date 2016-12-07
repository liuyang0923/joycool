<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
TD {word-wrap:break-word;}
</style>
<link href="farm/common.css" rel="stylesheet" type="text/css">
<p>在线列表</p>
<p>峰值：<%=JoycoolSessionListener.maxSessionCount%></p>
<table width="100%" border="2">
<tr>
<td><strong>用户名</strong></td>
<td><strong>IP地址</strong></td>
<td><strong>UA</strong></td>
<td><strong>最后访问页面</strong></td>
</tr>
<%
Hashtable users = JoycoolSessionListener.getOnlineUsers();
Hashtable hts = new Hashtable();
Integer count = null;
if(users != null){
	Enumeration enu = users.elements();
	int i = 1;
	UserBean user;
	while(enu.hasMoreElements()){
		user = (UserBean) enu.nextElement();
		count = (Integer) hts.get(user.getIpAddress());
		if(count != null){
			hts.put(user.getIpAddress(), new Integer(count.intValue() + 1));
		} else {
			hts.put(user.getIpAddress(), new Integer(1));
		}
%>
<tr>
<td width="100"><%if(user.getId()==0){%><%}else{%><a href="user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getId()%></a><br/><%=user.getNickNameWml()%><%}%></td>
<td width="120"><%=user.getIpAddress()%></td>
<td><%=user.getUserAgent()%></td>
<td><%=user.getLastVisitPage()%></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<table width="100%" border="1">
<tr>
<td><strong>IP地址</strong></td>
<td><strong>在线人数</strong></td>
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
<td width="100"><%if(net.joycool.wap.util.SecurityUtil.isMobileIp(ip)){%><%=ip%><%}else{%><font color=red><%=ip%></font><%}%></td>
<td><%=count.intValue()%></td>
</tr>
<%
	i ++;
}
%>
</table>