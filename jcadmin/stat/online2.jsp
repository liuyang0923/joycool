<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.PositionUtil"%><%@ page import="net.joycool.wap.bean.ModuleBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%!
static int MINUTE = 10;
%><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
TD {word-wrap:break-word;}
</style>
<p>详细在线列表</p>
<%
Hashtable ht = OnlineUtil.getOnlineHash();
List list = new ArrayList(ht.keySet());
String[] countName = {"游戏","聊天","帮会","论坛","家园","城堡","打猎","闲逛","其他"};
int[][] count=new int[countName.length][];
int[] sum=new int[MINUTE];
for(int i=0;i<countName.length;i++){
	count[i] = new int[MINUTE];
}
Map onlineMap = new TreeMap();
long now = System.currentTimeMillis();
for(int i=0;i<list.size();i++){
	String key = (String)list.get(i);
	long last = now - OnlineUtil.getLastVisitTime(key);
	int last2 = (int)last / 60 / 1000;
	if(last2 >= MINUTE) continue;
	UserBean user = (UserBean)OnlineUtil.getOnlineBean(key);
	int pos = user.getPositionId();
	Integer posKey = new Integer(pos);
	int[] posCount = (int[])onlineMap.get(posKey);
	if(posCount==null){
		posCount = new int[MINUTE];
		onlineMap.put(posKey, posCount);
	}
	posCount[last2]++;

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
Iterator iter = onlineMap.entrySet().iterator();
while(iter.hasNext()){
Map.Entry me = (Map.Entry)iter.next();
Integer iid = (Integer)me.getKey();
int[] ic = (int[])me.getValue();
ModuleBean module = PositionUtil.getModule(iid.intValue());
if(module==null) module=PositionUtil.nullModule;
%>
<tr>
<td><%=module.getName()%></td><%for(int j=0;j<MINUTE;j++){
sum[j] +=ic[j];
%><td><%=ic[j]%></td><%}%><td><a href="online3.jsp?pos=<%=iid%>">查看详情</a></td>
</tr>
<%}%>
<tr>
<td>总计</td><%for(int j=0;j<MINUTE;j++){
%><td><%=sum[j]%></td><%}%>
</tr>
</table>