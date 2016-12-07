<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%!
static int MINUTE = 10;
%><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
TD {word-wrap:break-word;}
</style>
<p>在线列表</p>
<%
Hashtable ht = OnlineUtil.getOnlineHash();
List list = new ArrayList(ht.keySet());
String[] countName = {"游戏","聊天","帮会","论坛","家园","城堡","打猎","闲逛","其他"};
int[][] count=new int[countName.length][];
int[] sum=new int[MINUTE];
for(int i=0;i<countName.length;i++){
	count[i] = new int[MINUTE];
}
long now = System.currentTimeMillis();
for(int i=0;i<list.size();i++){
	String key = (String)list.get(i);
	long last = now - OnlineUtil.getLastVisitTime(key);
	int last2 = (int)last / 60 / 1000;
	if(last2 >= MINUTE) continue;
	UserBean user = (UserBean)OnlineUtil.getOnlineBean(key);
	int pos = user.getPositionId();
	if(pos==44)
		count[2][last2]++;
	else if(pos==87||pos==1012||pos==1014)
		count[5][last2]++;
	else if(pos==23)
		count[6][last2]++;
	else if(pos == 11)
		count[1][last2]++;
	else if(pos==46)
		count[3][last2]++;
	else if(pos==41)
		count[4][last2]++;
	else if(pos >20 && pos <95)
		count[0][last2]++;
	else if(pos==5)
		count[7][last2]++;
	else
		count[8][last2]++;

}
%>
<table width="600" border="1">
<tr>
<td><strong>位置</strong></td>
<td><strong>1分钟</strong></td>
<td><strong>2分钟</strong></td>
<td><strong>3分钟</strong></td>
<td><strong>4分钟</strong></td>
<td><strong>5分钟</strong></td>
</tr>
<%
for(int i=0;i<countName.length;i++){	
%>
<tr>
<td><%=countName[i]%></td><%for(int j=0;j<MINUTE;j++){
sum[j] +=count[i][j];
%><td><%=count[i][j]%></td><%}%>
</tr>
<%}%>
<tr>
<td>总计</td><%for(int j=0;j<MINUTE;j++){
%><td><%=sum[j]%></td><%}%>
</tr>
</table>
<a href="online2.jsp">详细数据</a><br/>