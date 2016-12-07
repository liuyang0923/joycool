<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="net.joycool.wap.framework.CustomAction"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
PagingBean paging = new PagingBean(action, 1000, 10, "p");
List list = TexasGame.service.getTexasRecordList(" 1 order by id desc limit " + paging.getStartIndex()+",10");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="德州扑克">
<p align="left">
<%=BaseAction.getTop(request, response)%>
==历史战局==<br/><%
for(int i = 0;i < list.size();i++){
	TexasGame game = (TexasGame)list.get(i);
%><a href="rec.jsp?id=<%=game.getId()%>"><%=game.getGameUserCount()%>人总下注<%=game.getWager()%></a>(<%=DateUtil.sformatTime(game.getCreateTime())%>)<br/><%
}
%><%=paging.shuzifenye("recs.jsp",false,"|",response)%>
<a href="index.jsp">返回德州扑克大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>