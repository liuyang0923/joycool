<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
BoatAction action = new BoatAction(request,response);
String[] distance = {"无","前进","后退"}; 
String[] speed = {"无","提高","降低"}; 
List list = BoatAction.service.getAccidentList("1 order by id desc");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="龙舟随机事件"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(list != null && list.size() > 0){
	PagingBean paging = new PagingBean(action,list.size(),6,"p");
	for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
		AccidentBean bean = (AccidentBean) list.get(i);
		if(bean != null){
			%><%=StringUtil.toWml(bean.getName())%>事件<br/>
			描述:<%=StringUtil.toWml(bean.getBak())%>
			事件效果:<%
			if(bean.getDistanceType()>0){%>龙舟位置<%=distance[bean.getDistanceType()]%><%=bean.getDistance1()%>m-<%=bean.getDistance2()%>m.<%}
			if(bean.getSpeedType()>0){%>龙舟速度变化(<%=speed[bean.getSpeedType()]%><%=bean.getSpeed1()%>%-<%=bean.getSpeed2()%>%).<%}
			if(bean.getAngleType()>0){%>龙舟角度变化(<%=bean.getAngle1()%>°-<%=bean.getAngle2()%>°).<%}
			if(i != paging.getEndIndex() - 1){
			%><br/>==========<br/><%
			} else {
			%><br/><%
			}
		}
	}
  	%><%=paging.shuzifenye("accident.jsp?jcfr=1",true,"|",response)%><%
} else {
%>暂无随机事件<br/><%
}
%><a href="rule.jsp">游戏规则</a><br/><a href="tpboat.jsp">龙舟类型</a><br/><a href="match.jsp">返回赛龙舟</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>