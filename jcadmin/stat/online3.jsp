<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.PositionUtil"%><%@ page import="net.joycool.wap.bean.ModuleBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%!
static int MINUTE = 10;
%><%

CustomAction action = new CustomAction(request);
int pos=action.getParameterInt("pos");
ModuleBean module = PositionUtil.getModule(pos);
if(module==null) module=PositionUtil.nullModule;
%><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
TD {word-wrap:break-word;}
</style>
<p>【<%=module.getName()%>】详细在线列表</p>
<%

Hashtable ht = OnlineUtil.getOnlineHash();
List list = new ArrayList(ht.keySet());

long now = System.currentTimeMillis();
List onlinelist = new ArrayList();
for(int i=0;i<list.size();i++){
	String key = (String)list.get(i);
	long last = now - OnlineUtil.getLastVisitTime(key);
	int last2 = (int)last / 60 / 1000;
	if(last2 >= MINUTE) continue;
	UserBean user = (UserBean)OnlineUtil.getOnlineBean(key);
	if(pos==user.getPositionId())
		onlinelist.add(user);
}
%>
<table border="1">
<%
for(int i=0;i<onlinelist.size();i++){
UserBean user = (UserBean)onlinelist.get(i);
long last = now - OnlineUtil.getLastVisitTime(String.valueOf(user.getId()));
int last2 = (int)last / 60 / 1000;
%>
<tr>
<td width="100"><%if(user.getId()==0){%><%}else{%><a href="/jcadmin/user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getId()%></a><%=user.getNickNameWml()%><%}%></td>
<td><%=last2%>分钟</td>
<td width="120"><%=user.getIpAddress()%></td>
<td><%=user.getUserAgent()%></td>
<td><%=user.getLastVisitPage()%></td>
</tr>
<%}%>
</table>