<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.util.*"%><%
AppAction action = new AppAction(request);
AppBean ab = null;
int id = action.getParameterInt("id");
if(id>0)
	ab = AppAction.getApp(id);
	
if(!action.isMethodGet()){
	String oldDir = null;
	if(ab == null)
		ab = new AppBean();
	else
		oldDir = ab.getDir();
	ab.setName(request.getParameter("name"));
	ab.setName2(request.getParameter("name2"));
	ab.setShortName(request.getParameter("shortName"));
	ab.setInfo(request.getParameter("info"));
	ab.setDir(request.getParameter("dir"));
	ab.setUrl(request.getParameter("url"));
	ab.setOffline(request.getParameter("offline"));
	ab.setAuthor(request.getParameter("author"));
	ab.setContact(request.getParameter("contact"));
	ab.setEmail(request.getParameter("email"));	
	
	ab.setApiKey(request.getParameter("apiKey"));
	ab.setSecretKey(request.getParameter("secretKey"));	
	ab.setFlag(action.getParameterFlag("flag"));
	
	ab.setIndex("");
	if(ab.getId()==0) {
		ab.setCreateTime(System.currentTimeMillis());
		AppAction.addApp(ab);
	} else {
		int type = action.getParameterInt("type");
		if(type != ab.getType()) {
			AppAction.addTypeCount(ab.getType(), -1);
			AppAction.addTypeCount(type, 1);
		}
		ab.setType(type);
		AppAction.updateApp(ab);
		if(oldDir != null && !oldDir.equals(ab.getDir())) {
			AppAction.appMap.put(ab.getDir(), ab);
			AppAction.appMap.remove(oldDir);
		}
	}
	response.sendRedirect("index.jsp");
}
List list2 = AppAction.getTypeList();
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
	插件列表
	<br />
	<br />
<form action="edit.jsp?id=<%=id%>" method=post>
	<table width="600">
	<tr>
		<td>名称<%if(ab!=null){%>(id:<%=ab.getId()%>)<%}%></td>
		<td><input type=text name="name" size=50<%if(ab!=null){%> value="<%=ab.getName()%>"<%}%>></td>
	</tr>
	<tr>
		<td>短名称</td>
		<td><input type=text name="shortName" size=50<%if(ab!=null){%> value="<%=ab.getShortName()%>"<%}%>></td>
	</tr>
	<tr>
		<td>状态名称</td>
		<td><input type=text name="name2" size=50<%if(ab!=null){%> value="<%=ab.getName2()%>"<%}%>></td>
	</tr>
<%if(ab!=null){%>
	<tr>
		<td>分类</td>
		<td><select name="type"><%
for(int i=0;i<list2.size();i++){
AppTypeBean at = (AppTypeBean)list2.get(i);
%>		<option value="<%=at.getId()%>" <%if(at.getId()==ab.getType()){%>selected<%}%> ><%=at.getName()%></option><%}%>
		</select></td>
	</tr>
<%}%>
	<tr>
		<td>描述</td>
		<td><textarea name="info" cols="60" rows="2"><%if(ab!=null){%><%=ab.getInfo()%><%}%></textarea></td>
	</tr>
	<tr>
		<td>作者</td>
		<td><input type=text name="author" size=50<%if(ab!=null){%> value="<%=ab.getAuthor()%>"<%}%>></td>
	</tr>
	<tr>
		<td>组件目录</td>
		<td><input type=text name="dir" size=50<%if(ab!=null){%> value="<%=ab.getDir()%>"<%}%>></td>
	</tr>
	<tr>
		<td>组件服务器地址</td>
		<td><input type=text name="url" size=50 value="<%if(ab!=null){%><%=ab.getUrl()%><%}else{%>http://<%}%>"></td>
	</tr>
	<tr>
		<td>api密钥</td>
		<td><input type=text name="apiKey" size=50<%if(ab!=null){%> value="<%=ab.getApiKey()%>"<%}%>></td>
	</tr>
	<tr>
		<td>访问密钥</td>
		<td><input type=text name="secretKey" size=50<%if(ab!=null){%> value="<%=ab.getSecretKey()%>"<%}%>></td>
	</tr>
	<tr>
		<td>离线内容</td>
		<td><textarea name="offline" cols="60" rows="2"><%if(ab!=null){%><%=ab.getOffline()%><%}%></textarea></td>
	</tr>
	<tr>
		<td>联系email</td>
		<td><textarea name="email" cols="60" rows="2"><%if(ab!=null){%><%=ab.getEmail()%><%}%></textarea></td>
	</tr>
	<tr>
		<td>其他联系方式</td>
		<td><textarea name="contact" cols="60" rows="2"><%if(ab!=null){%><%=ab.getContact()%><%}%></textarea></td>
	</tr>
<%if(ab!=null){%>
	<tr>
		<td>安装人数</td>
		<td><input type=text readonly name="count" value="<%=ab.getCount()%>"></td>
	</tr>
	<tr>
		<td>收藏人数</td>
		<td><input type=text readonly name="count" value="<%=ab.getFavorCount()%>"></td>
	</tr>
	<tr>
		<td>评分人数</td>
		<td><input type=text readonly name="count" value="<%=ab.getScoreCount()%>"></td>
	</tr>
	<tr>
		<td>平均分</td>
		<td><input type=text readonly name="count" value="<%=ab.getAveScoreString()%>"></td>
	</tr>
	<tr>
		<td>评论人数</td>
		<td><input type=text readonly name="count" value="<%=ab.getReplyCount()%>"></td>
	</tr>
	<tr>
		<td>活跃人数</td>
		<td><input type=text readonly name="count" value="<%=ab.getActiveCount()%>"></td>
	</tr>
<%}%>
	<tr>
		<td>选项</td>
		<td>
	<table cellpadding=3 valign=top><tr><td>
		<input type=checkbox name="flag" value="3" <%if(ab!=null&&ab.isFlagIcon()){%>checked<%}%>>带图标<br/>
		<input type=checkbox name="flag" value="10" <%if(ab!=null&&ab.isFlagLogo()){%>checked<%}%>>带大图标<br/>
		<input type=checkbox name="flag" value="4" <%if(ab!=null&&ab.isFlagLocal()){%>checked<%}%>>本地组件<br/>
		<input type=checkbox name="flag" value="14" <%if(ab!=null&&ab.isFlagNoSid()){%>checked<%}%>>不编码url<br/>
		<input type=checkbox name="flag" value="17" <%if(ab!=null&&((ab.getFlag()&(1 << 17))!=0)){%>checked<%}%>>链接管理<br/>
	</td><td>
		<input type=checkbox name="flag" value="0" <%if(ab!=null&&ab.isFlagClose()){%>checked<%}%>>无法安装<br/>
		<input type=checkbox name="flag" value="1" <%if(ab!=null&&ab.isFlagHide()){%>checked<%}%>>列表中隐藏<br/>
		<input type=checkbox name="flag" value="9" <%if(ab!=null&&ab.isFlagTest()){%>checked<%}%>>处于测试期<br/>
		<input type=checkbox name="flag" value="2" <%if(ab!=null&&ab.isFlagOffline()){%>checked<%}%>>服务器离线<br/>
		<input type=checkbox name="flag" value="16" <%if(ab!=null&&ab.isFlagHideUrl()){%>checked<%}%>>隐藏url<br/>
	</td><td>
		<input type=checkbox name="flag" value="5" <%if(ab!=null&&ab.isFlagUid()){%>checked<%}%>>传递userId<br/>
		<input type=checkbox name="flag" value="7" <%if(ab!=null&&ab.isFlagAllow()){%>checked<%}%>>允许电脑访问<br/>
		<input type=checkbox name="flag" value="8" <%if(ab!=null&&ab.isFlagLimit()){%>checked<%}%>>限制访问频率<br/>
		<input type=checkbox name="flag" value="13" <%if(ab!=null&&ab.isFlagDirect()){%>checked<%}%>>不需要添加<br/>
	</td><td>
		<input type=checkbox name="flag" value="6" <%if(ab!=null&&ab.isFlagPay()){%>checked<%}%>>开通支付(酷币)<br/>
		<input type=checkbox name="flag" value="11" <%if(ab!=null&&ab.isFlagPayItem()){%>checked<%}%>>开通支付(物品)<br/>
		<input type=checkbox name="flag" value="12" <%if(ab!=null&&ab.isFlagPayLb()){%>checked<%}%>>开通支付(乐币存款)<br/>
		<input type=checkbox name="flag" value="15" <%if(ab!=null&&ab.isFlagPresent()){%>checked<%}%>>允许送乐币/物品<br/>
	</td></tr></table>
		</td>
	</tr>
	<tr>
		<td><input type=submit value="确认提交"></td>
	</tr>
	</table></form>
	<br /><a href="index.jsp">返回组件列表</a><br/>
</body>
</html>
