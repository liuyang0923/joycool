<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%
	
	
	CastleAction action = new CastleAction(request);
	int id = action.getParameterInt("id");
	if(id == 0)
		id = action.getCastleUser().getTong();
	TongBean tong = CastleUtil.getTong(id);
	SimpleChatLog sc = SimpleChatLog.getChatLog("ca"+id);
	int count = sc.getUnreadTotal(action.getAttribute2("castle"));
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟资料"><p><%@include file="top.jsp"%>
<%if(tong != null) {
boolean myTong = (action.getCastleUser().getTong() == id);
List list = action.getService().getTongUser(id, 0, 100);

%>
【<%=StringUtil.toWml(tong.getName())%>】
<br/>
成员:<%=tong.getCount()%>|总人口:<%=tong.getPeople()%><br/>
描述:<%=StringUtil.toWml(tong.getInfo())%><br/>
<%
boolean power = false;
if(myTong){
TongPowerBean myPower = action.getService().getTongPowerByUid(action.getCastleUser().getUid());
if(myPower != null)
	power = (myPower.getPower()!=0);
%><a href="tongChat.jsp">联盟讨论<%if(count>0){%><%=count%><%}%></a>|<a href="tongReport.jsp">联盟战报</a><%if(power){%>|<a href="tongM.jsp">联盟管理</a><%}%><br/><%}%>
<a href="dip.jsp?id=<%=id%>">联盟外交</a>|<a href="tongPower.jsp?id=<%=id%>">联盟高层</a><br/>
=成员/人口/城堡数=<br/>
<%
int sum = 0;
for(int i=0;i<list.size();i++){
Integer iid = (Integer)list.get(i);
CastleUserBean user = CastleUtil.getCastleUser(iid.intValue());
if(user==null) continue;
%><%=i+1%>.<a href="../user.jsp?uid=<%=user.getUid()%>"><%=user.getNameWml()%></a>/<%=user.getPeople()%>/<%=user.getCastleCount()%><br/>
<%
sum += user.getPeople();
}
// 更新联盟人口数据
if(sum!=tong.getPeople()){
	CastleUtil.updateTongPeople(tong, sum);
}
} %>

<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>