<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.*,jc.family.game.vs.term.*,jc.family.*,jc.family.game.vs.*"%><%
TermAction action=new TermAction(request,response);
int id = action.getParameterInt("id");
List termList = TermAction.termService.getTermBeanList("1 order by create_time desc");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族争霸赛"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(termList!=null && termList.size()>0){
	for(int i=0;i<termList.size();i++){
		TermBean term = (TermBean) termList.get(i);
		%><a href="match.jsp?id=<%=term.getId()%>"><%=term.getName()%></a>(<%=net.joycool.wap.util.DateUtil.formatDate1(new Date(term.getCreateTime()))%>)<br/><%
	}
} else {
%>(暂无)<br/><%
}
%>
<a href="/Column.do?columnId=12711">+家族争霸赛章程+</a><br/>
<a href="/fm/index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>