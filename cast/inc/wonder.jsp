<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><br/><%
WWBean ww = castleService.getWW("cid="+action.getCastle().getId());
%>【<%=StringUtil.toWml(ww.getName())%>】<br/>
<%if(building.getGrade()!=0){%><a href="wwR.jsp">&gt;&gt;修改奇迹名称</a><br/><%}%>

<br/>