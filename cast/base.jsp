<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%!
static 	CacheService cacheService = CacheService.getInstance();%><%
	
	
	CastleAction action = new CastleAction(request);
	CastleBean castle = action.getCastle();
	action.curPage = 1;
	action.setAttribute2("casSwi","base.jsp");
	int[] buildPos = action.getCastleService().getBaseBuilding(castle.getId());
	int[] baseBuildPost = ResNeed.baseBuildRes[castle.getType2()];
	int[] buildPos2 = action.getCastleService().getBuildPos(castle.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p><%@include file="top.jsp"%>
<%for(int i = 1; i < 19; i++) {
  if(buildPos2[i] > 0){
%><a href="fun.jsp?type=<%=baseBuildPost[i] %>&amp;pos=<%=i %>"><%= ResNeed.getTypeName(baseBuildPost[i]) %><%=buildPos[i] %></a><%
} else {
%><a href="sbd2.jsp?type=<%=baseBuildPost[i] %>&amp;pos=<%=i %>"><%= ResNeed.getTypeName(baseBuildPost[i]) %></a><%
} 
 if(i % 4 == 0) {%><br/><%}else{%>.<%}%><%}%>
 <br/><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>