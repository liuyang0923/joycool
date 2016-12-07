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
各个pv的手机号数<br/>
<table border="1">
  <tr>
  <td><font color="red">pv数</font></td>
  <td><font color="blue">手机号数</font></td>
  </tr>
<%
tpr.getPvMobiles();
int mpv = tpr.getMaxMpv();
for(int i = 1; i <= mpv; i ++){
    if(tpr.getPvMobiles().containsKey(i + "")){
%>
  <tr>
  <td><font color="red"><%=i%></font></td>
  <td><font color="blue"><%=((Integer) tpr.getPvMobiles().get(i + "")).intValue()%></font></td>
  </tr>
<%
	}
}
%>
</table>
各手机号的详细统计<br/>
<table border="1">
  <tr>
  <td><font color="red">手机号</font></td>
  <td><font color="blue">pv数</font></td>
  </tr>
<%
Enumeration enu0 = tpr.getMobilePvs().keys();
String mobile = null;
int count = 0;
while (enu0.hasMoreElements()) {
    mobile = (String) enu0.nextElement();
    count = ((Integer) tpr.getMobilePvs().get(mobile))
            .intValue();
%>
  <tr>
  <td><font color="red"><%=mobile%></font></td>
  <td><font color="blue"><%=count%></font></td>
  </tr>
<%
}
%>
</table>