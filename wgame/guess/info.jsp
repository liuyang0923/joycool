<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>==我的记录==<br/>
<% String log = action.getGuessUser().getLogString();
if(log.length() == 0){%>
暂无<br/>
<%}else{%>
<%=log%>
<%}%>