<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.JaLineBean,net.wxsj.framework.log.*,java.util.*,net.wxsj.util.StringUtil"%><title>详细统计</title>
<%
String date = request.getParameter("date");
String columnId = request.getParameter("columnId");
Hashtable ht = TempLogParser.getDateResult(date);
if(ht == null || ht.get(columnId) == null){
%>
本日的统计还没有出来哦，再等等吧！<br>
<%
	return;
}
TempParseResultBean tpr = (TempParseResultBean) ht.get("" + columnId);
%>
各个pv的用户数<br/>
<table border="1">
  <tr>
  <td><font color="red">pv数</font></td>
  <td><font color="blue">用户数</font></td>
  </tr>
<%
tpr.getPvUsers();
int mpv = tpr.getMaxUpv();
for(int i = 1; i <= mpv; i ++){
    if(tpr.getPvUsers().containsKey(i + "")){
%>
  <tr>
  <td><font color="red"><%=i%></font></td>
  <td><font color="blue"><%=((Integer) tpr.getPvUsers().get(i + "")).intValue()%></font></td>
  </tr>
<%
	}
}
%>
</table>
各用户的详细统计<br/>
<table border="1">
  <tr>
  <td><font color="red">用户ID</font></td>
  <td><font color="blue">pv数</font></td>
  </tr>
<%
Enumeration enu0 = tpr.getUserPvs().keys();
String userId = null;
int count = 0;
while (enu0.hasMoreElements()) {
    userId = (String) enu0.nextElement();
    count = ((Integer) tpr.getUserPvs().get(userId))
            .intValue();
%>
  <tr>
  <td><font color="red"><%=userId%></font></td>
  <td><font color="blue"><%=count%></font></td>
  </tr>
<%
}
%>
</table>