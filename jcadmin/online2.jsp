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
		if(user.getId()==0) continue;
		if(user.getIpAddress()==null) continue;

		Long ipl = new Long(net.joycool.wap.util.IP.ipToLong(user.getIpAddress()));
%>
<%if(user.getIpAddress()!=null&&!net.joycool.wap.util.SecurityUtil.permitIpSet.contains(ipl)&&!user.getIpAddress().startsWith("113.107.72")&&
!user.getIpAddress().startsWith("61.156.45.24")&&
!user.getIpAddress().startsWith("123.150.187")&&!user.getIpAddress().startsWith("117.136")){
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
<td><%if(user.getUserAgent()==null||user.getUserAgent().indexOf("Mot-E680G")==-1){%><%=user.getUserAgent()%><%}else{%><font color=red><%=user.getUserAgent()%></font><%}%></td>
<td><%if(user.getLastVisitPage()==null||user.getLastVisitPage().indexOf("/question/")==-1&&user.getLastVisitPage().indexOf("/tong/")==-1){%><%=user.getLastVisitPage()%><%}else{%><font color=red><%=user.getLastVisitPage()%></font><%}%></td>
</tr><%}%>
<%
	    i ++;
	}
}
%>
</table>
<table width="300" border="1">
<tr>
<td width="100"><strong>IP地址</strong></td>
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
<td width="100"><%=ip%></td>
<td><%=count.intValue()%></td>
</tr>
<%
	i ++;
}
%>
</table>