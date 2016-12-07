<%@ page language="java" import="jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%YardAction action=new YardAction(request);
String count = "";
int id = action.getParameterInt("id");
int fmId = action.getFmId();
int type = action.getParameterInt("t");
String tip = "";
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
// FamilyHomeBean fm=action.getFmByID(id);
YardBean yard = action.getYardByID(id);
int prodId = action.getParameterInt("pid");
if (prodId < 0 || prodId > 2){
	response.sendRedirect("index.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="生产基地"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (type == 0){

if(fmId<=0){
	%>无法生产,只有家族玩家才能参与!!<br/><%
}else{
// 选择蔬菜
%>当前协作度:<%=yard.getCoopRate()*10%>%<br/>
请选择要生产的类别!<br/>
<%	java.util.List list = YardAction.getSeedList();
	for(int i=0;i<list.size();i++){
		YardItemProtoBean proto = (YardItemProtoBean)list.get(i);
		if(proto.getRank()>yard.getRank()) continue;
%><a href="prod2.jsp?pid=<%=proto.getId()%>&amp;id=<%=id%>"><%=proto.getName()%></a><br/>
<%}%>
<%}%>
【生产动态】<%if(yard.getProductLog().size()>3){%><a href="trends2.jsp?id=<%=id%>">更多</a><%}%><br/>
<%if(yard.getProductLog().size()==0){%>(暂无)<br/><%}else{%><%=yard.getProductLog().getLogString(3)%><%}%>
<a href="stat.jsp?id=<%=id%>">&gt;&gt;生产统计</a><br/>
<%
} else {
// 获取奖励
count = String.valueOf(session.getAttribute("count"));
if (count != null && !"".equals(count)){
%><%
count = "";
} else {
response.sendRedirect("prod.jsp");
return;
}
}
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp?id=<%=id%>">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>