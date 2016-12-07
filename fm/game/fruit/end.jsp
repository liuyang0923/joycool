<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.fruit.*"%><%@ page import="jc.family.FamilyUserBean"%>
<%! static String str[] = {"获胜","失败","平局"}; 
%>
<%FruitAction action=new FruitAction(request);%>
<%@include file="inc.jsp"%>
<%
String tip = "";
List list = null;
String pageStr = "";
FruitFamilyBean ffb = null;
int winSide = vsGame.getGameResult();	// 赢的side
int side = 0;
int type = 1;

if(winSide == 2){
	type = 2;
}

if(vsUser!=null && !action.hasParam("t")){
	side = vsUser.getSide();
}else if(action.hasParam("t")){
	int t=action.getParameterInt("t");
	if(t==0){
		side = 0;
	}else if(t == 1){
		side = 1;
	}
}

ffb = vsGame.getFruitFamilyBean(side);
if (ffb == null){
	tip = "无法找到相应信息.";
} else {
		list = action.getSubList(ffb.getUserList(),10,"pageStr","end.jsp?t=" + type+"&amp;t="+side,true);
		pageStr = (String)request.getAttribute("pageStr");
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族水果战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>挑战统计|<a href="game.jsp">查看果园</a><br/>
<%
int index = 1; 
if(winSide ==2) index =2;
if(winSide == side) index = 0;
%>
<%=str[index]%>:<%=vsGame.getFmNameBySide(side)%>家族<br/>
参与人数:<%=side==0?vsGame.getFmCountA():vsGame.getFmCountB()%><br/>
用时:<%=vsGame.getGameSpendTime()%><br/>
>>家族果园数量:<%=vsGame.getOrchardCountBySide(side)%><br/>
阳光总量:<%=ffb.getSunTotalCount()%>|采集率:<%=ffb.getSunTotalCaptureRate()%><br/>
家族水果:<%=ffb.getFruitTotalcount()%><br/>
尖刺果皮:<%=ffb.getFruitATKGrade()%>级<br/>
加厚果皮:<%=ffb.getFruitHP()%>级<br/>
果影分身:<%=ffb.getFruitYield()%>级<br/>
喷气水果:<%=ffb.getFruitSpeed()%>级<br/>
已消灭水果:<%=ffb.getFruitBeatted()%><br/>
家族水果烈士:<%=ffb.getFruitSacrificed()%><br/><br/>
成员|总操作数<br/>
<%if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		FruitUserBean fub = (FruitUserBean)list.get(i);
		if (fub != null){
			%><a href="userOpt.jsp?uid=<%=fub.getUserId()%>&amp;t=<%=side%>"><%=fub.getNickNameWml()%></a>|<%=fub.getOperateCount()%><br/><%
		}
	}%><%=pageStr==null || "".equals(pageStr)?"":pageStr%><%
} else {
%>暂无<br/><%
}%>
<%if(type==1){
	if(winSide == side){%>
		<a href="end.jsp?t=<%=side==1?0:1 %>">查看挑战失败家族</a>	
	<%}else{%>
		<a href="end.jsp?t=<%=side==1?0:1 %>">查看挑战胜利家族</a>
	<%} %>
<%}else if (type == 2){
	%><a href="end.jsp?t=<%=side==1?0:1 %>">查看<%=vsGame.getFmNameBySide(side==1?0:1)%>家族</a><%
}%><br/>
<a href="aMore.jsp">查看水果动态</a><br/><br/>
<%	
} else {
%><%=tip%><br/><%
}
%>
<a href="/fm/game/vs/vsgame.jsp?id=<%=side==0?vsGame.getFmIdA():vsGame.getFmIdB() %>">返回家族挑战</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>