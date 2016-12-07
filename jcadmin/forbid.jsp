<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%

if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}

if(request.getParameter("del") != null){
	List forbidIpList = SecurityUtil.forbidIpList;
	int d = StringUtil.toInt(request.getParameter("del"));
	if(d>=0){
		SqlUtil.executeUpdate("delete from ip_group where `group`='forbid'and ip='"+((IP)forbidIpList.get(d)).toString()+"'");
		forbidIpList.remove(d);
	}
	BaseAction.sendRedirect("/jcadmin/forbid.jsp", response);
	return;
}
if(request.getParameter("del2") != null){
	List mobileIp = SecurityUtil.mobileIp;
	IP ip = new IP(request.getParameter("del2"));
	for(int i = 0;i < mobileIp.size();i++) {
		IP m = (IP)mobileIp.get(i);
		if(m.getIp()==ip.getIp() && ip.getMask()==m.getMask()) {
			int d = i;	// delete it
			SqlUtil.executeUpdate("delete from ip_group where `group`='mobile' and ip='"+ip+"'");
			mobileIp.remove(i);
			break;
		}
	}

	BaseAction.sendRedirect("/jcadmin/forbid.jsp", response);
	return;
}
if(request.getParameter("del3") != null){
	Object[] permitSet = SecurityUtil.permitIpSet.toArray();
	IP ip = new IP(request.getParameter("del3"));
	if(ip.isValid()) {
		SqlUtil.executeUpdate("delete from ip_group where `group`='permit' and ip='"+ip+"'");
		SecurityUtil.permitIpSet.remove(ip.getIp());
	}
	BaseAction.sendRedirect("/jcadmin/forbid.jsp", response);
	return;
}
if(request.getParameter("del4") != null){
	Object[] permitSet = SecurityUtil.cmwapIpSet.toArray();
	IP ip = new IP(request.getParameter("del4"));
	if(ip.isValid()) {
		SqlUtil.executeUpdate("delete from ip_group where `group`='cmwap' and ip='"+ip+"'");
		SecurityUtil.cmwapIpSet.remove(ip.getIp());
	}
	BaseAction.sendRedirect("/jcadmin/forbid.jsp", response);
	return;
}
if(request.getParameter("add") != null && request.getParameter("add").length()>2){
	List forbidIpList = SecurityUtil.forbidIpList;
	IP ip = new IP(request.getParameter("add"));
	if(ip.isValid()) {
		forbidIpList.add(0,ip);
		SqlUtil.executeUpdate("insert into ip_group set `group`='forbid',create_time=now(),ip='"+ip.toString()+"'");
	}
	BaseAction.sendRedirect("/jcadmin/forbid.jsp", response);
	return;
}
if(request.getParameter("add2") != null && request.getParameter("add2").length()>2){
	List forbidIpList = SecurityUtil.mobileIp;
	IP ip = new IP(request.getParameter("add2"));
	if(ip.isValid()) {
		forbidIpList.add(ip);
		SqlUtil.executeUpdate("insert into ip_group set `group`='mobile',create_time=now(),ip='"+ip+"'");
	}
	BaseAction.sendRedirect("/jcadmin/forbid.jsp", response);
	return;
}
if(request.getParameter("add3") != null && request.getParameter("add3").length()>2){
	HashSet ms = SecurityUtil.permitIpSet;
	IP ip = new IP(request.getParameter("add3"));
	if(ip.isValid()) {
		ms.add(new Long(ip.getIp()));
		SqlUtil.executeUpdate("insert into ip_group set `group`='permit',create_time=now(),ip='"+ip+"'");
	}
	BaseAction.sendRedirect("/jcadmin/forbid.jsp", response);
	return;
}
if(request.getParameter("add4") != null && request.getParameter("add4").	length()>2){
	HashSet ms = SecurityUtil.cmwapIpSet;
	String[] s = request.getParameter("add4").split("[,\\r\\n]");
	for(int i=0;i<s.length;i++){
		IP ip = new IP(s[i]);
		Long ipl = new Long(ip.getIp());
		if(ip.isValid() && !ms.contains(ipl)) {
			ms.add(ipl);
		SqlUtil.executeUpdate("insert into ip_group set `group`='cmwap',create_time=now(),ip='"+ip+"'");
	}
	}
	BaseAction.sendRedirect("/jcadmin/forbid.jsp", response);
	return;
}

if(request.getParameter("clear") != null){
	SecurityUtil.forbidIpList=null;
	SecurityUtil.init();

	List forbidUaList = SecurityUtil.forbidUaList;
	if(forbidUaList != null){
		forbidUaList.clear();		
	}
	
	response.sendRedirect("forbid.jsp");
}
%><html>
<head></head>
<link href="farm/common.css" rel="stylesheet" type="text/css">
<body>
<p><a href="forbid.jsp?clear=1">重新设定</a> <form><input type="text" name="query"><input type=submit value="查询"></form></p>
<%if(request.getParameter("query")!=null){
	String ips=request.getParameter("query");
	IP ip = new IP(ips);
	long ipl = ip.getIp();
%>ip - <%=ip%><br/><%
	if(SecurityUtil.permitIpSet.contains(new Long(ipl))){
		%>in mobileIP2<br/><%
	}
	if(SecurityUtil.cmwapIpSet.contains(new Long(ipl))){
		%>in proxy<br/><%
	}
	List mobileIp = SecurityUtil.mobileIp;
	if(IP.maskToLong(32)==ip.getMask()){
		for(int i = 0;i < mobileIp.size();i++) {
			IP m = (IP)mobileIp.get(i);
			if(m.isInScope(ipl)) {
				%>in mobileIP <%=m.getIpRange()%><br/><%
			}
		}
	}else{
		long iplend = ip.getIplEnd();
		for(int i = 0;i < mobileIp.size();i++) {
			IP m = (IP)mobileIp.get(i);
			if(ipl<m.getIplEnd()&&iplend>m.getIp()) {
				%>in mobileIP <%=m.getIpRange()%><br/><%
			}
		}
	}
    for (int i = 0; i < SecurityUtil.forbidIpList.size(); i++) {
    	IP m = (IP) SecurityUtil.forbidIpList.get(i);
        if (m.isInScope(ipl)) {
			%>in forbid <%=m.getIpRange()%><br/><%
        }
    }
%><br/><%
}%>
封禁列表(forbid)--<form action="forbid.jsp"><input type=text name=add value=""><input type=submit value="添加"></form>
<table width="200">
<%
List forbidIpList = SecurityUtil.forbidIpList;
for(int i=0;i<forbidIpList.size();i++){
%>
<tr>
<td width="20"><%=i+1%></td>
<td width="130"><%=(forbidIpList.get(i))%></td>
<td><a href="forbid.jsp?del=<%=i%>">删除</a></td>
</tr>
<%
}
%>
</table>
(代理服务器ip)--<form action="forbid.jsp"><textarea name=add4></textarea><input type=submit value="添加"></form>
<table width="250">
<%
Object[] cmwapIpSet = SecurityUtil.cmwapIpSet.toArray();
Arrays.sort(cmwapIpSet);
for(int i=0;i<cmwapIpSet.length;i++){
IP mi = new IP(IP.longToIp(((Long)cmwapIpSet[i]).longValue()));
%>
<tr>
<td width="20"><%=i+1%></td>
<td width="130"><%=(mi)%></td>
<td width="40"><a href="forbid.jsp?del4=<%=mi%>">删除</a></td>
</tr>
<%
}
%>
</table>
mobileIP--<form action="forbid.jsp"><input type=text name=add2 value=""><input type=submit value="添加"></form>
<table width="450">
<%
List mobileIp = new ArrayList(SecurityUtil.mobileIp);
Collections.sort(mobileIp, new Comparator(){
	public int compare(Object object1, Object object2){
		long ip1 = ((IP)object1).getIp();
		long ip2 = ((IP)object2).getIp();
		return (ip1<ip2 ? -1 : (ip1==ip2 ? 0 : 1));
	}
});
for(int i=0;i<mobileIp.size();i++){
	IP mi = (IP)mobileIp.get(i);
%>
<tr>
<td width="20"><%=i+1%></td>
<td width="130"><%=(mobileIp.get(i))%></td>
<td><%=mi.getIpRange()%></td>
<td width="40"><a href="forbid.jsp?del2=<%=mi%>">删除</a></td>
</tr>
<%
}
%>
</table>
mobileIP2(permit)--<form action="forbid.jsp"><input type=text name=add3 value=""><input type=submit value="添加"></form>
<table width="250">
<%
Object[] permitSet = SecurityUtil.permitIpSet.toArray();
Arrays.sort(permitSet);
for(int i=0;i<permitSet.length;i++){
IP mi = new IP(IP.longToIp(((Long)permitSet[i]).longValue()));
%>
<tr>
<td width="20"><%=i+1%></td>
<td width="130"><%=(mi)%></td>
<td width="40"><a href="forbid.jsp?del3=<%=mi%>">删除</a></td>
</tr>
<%
}
%>
</table>

<p>封禁列表</p>
<table width="100%" border="2">
<%
List forbidUaList = SecurityUtil.forbidUaList;
if(forbidUaList != null){
	Iterator itr = forbidUaList.iterator();
	int i = 1;
	while(itr.hasNext()){
%>
<tr>
<td width="10%"><%=i%></td>
<td width="90%"><%=((String)itr.next())%></td>
</tr>
<%
	    i ++;
	}
}
%>
</table></body></html>