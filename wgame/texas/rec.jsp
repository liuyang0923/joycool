<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="net.joycool.wap.framework.CustomAction"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
int id = action.getParameterInt("id");
TexasGame game = TexasGame.service.getTexasRecord("id=" + id);
TexasGame game2 = TexasAction.boards[game.getBoardId()];
TexasGame.service.getTexasRecordUsers(game);
if(game==null){
	response.sendRedirect("recs.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="德州扑克">
<p align="left">
<%=BaseAction.getTop(request, response)%>
历史编号<%=game.getCode()%><br/>
<%=game.getBoardId() + 1%>号<%=game2.getGameTypeName()%>桌(<%=game.getGameUserCount()%>/<%=game.getMaxUser()%>人)<br/>
台面:<%=TexasGame.toString(game.getCards())%><br/>
总下注:<%=game.getWager()%><br/>
===本局详情===<br/><%
TexasUser[] users = game.getUsers();
for(int i=0;i<users.length;i++){
	TexasUser tu = users[i];
	if(tu==null || tu.getStatus()==0) continue;
%><%=i+1%>座下注<%=tu.getWager()%>赢回<%=tu.getWinWager()%><%if(tu.getStatus()!=3){%>[<%=tu.getTypeName()%>]<%=TexasGame.toString(tu.getUserCards())%><%if(tu.getType()!=0){%><br/>-&gt;<%=TexasGame.toString(tu.getFinalCards())%><%}%><%}else{%>[放弃]<%}%><br/>
<%
}%>
<a href="recs.jsp">返回列表</a><br/>
<a href="index.jsp">返回德州扑克大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>