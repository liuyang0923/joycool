<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.JaLineBean,net.wxsj.framework.log.*,java.util.*,net.wxsj.util.StringUtil"%><title>每日统计</title>
<%
String date = request.getParameter("date");
Hashtable ht = TempLogParser.getDateResult(date);
if(ht == null){
%>
本日的统计还没有出来哦，再等等吧！<br>
<%
	return;
}
%>
本日统计<br/>
<strong>以下列出了树状页面每个根页面（及其子页面）的统计，请自己寻找财富项目。</strong><br>
<table border="1">
  <tr>
  <td><font color="red">根页面标题</font></td>
  <td><font color="blue">总pv</font></td>
  <td><font color="green">总session</font></td>
  <td><font color="brown">pv/session</font></td>
  <td><font color="green">总用户数</font></td>
  <td><font color="brown">pv/用户</font></td>
  <td><font color="green">总手机号</font></td>
  <td><font color="brown">pv/手机号</font></td>
  <td>详细记录</td>
  </tr>
<%
        JaLineBean line = null;
        Enumeration enu = ht.keys();
        int columnId = -1;        
        TempParseResultBean tpr = null;
		int apv = 0;
        while (enu.hasMoreElements()) {
%>
  <tr>
<%
            columnId = StringUtil.toInt((String) enu.nextElement());
            line = TempLogParser.getLine(columnId);
            if (line != null) {
%>
    <td><font color="red"><%=line.getName()%></font></td>
<%
                tpr = (TempParseResultBean) ht.get("" + columnId);
%>
    <td><font color="blue"><%=tpr.getTotalPv()%></font></td>
	<td><font color="green"><%=tpr.getSessionPvs().size()%></font></td>
<%
                if (tpr.getSessionPvs().size() > 0) {
                    apv = tpr.getTotalPv() / tpr.getSessionPvs().size();
                }
				else {
					apv = 0;
				}
%>
<td><font color="brown"><%=apv%></font></td>
	<td><font color="green"><%=tpr.getUserPvs().size()%></font></td>
<%
                if (tpr.getMobilePvs().size() > 0) {
                    apv = tpr.getTotalPv() / tpr.getUserPvs().size();
                }
				else {
					apv = 0;
				}
%>
<td><font color="brown"><%=apv%></font></td>
	<td><font color="green"><%=tpr.getMobilePvs().size()%></font></td>
<%
                if (tpr.getMobilePvs().size() > 0) {
                    apv = tpr.getTotalPv() / tpr.getMobilePvs().size();
                }
				else {
					apv = 0;
				}
%>
<td><font color="brown"><%=apv%></font></td>
<td>
<a href="tempLogDateDetail.jsp?date=<%=date%>&columnId=<%=line.getId()%>" target="_blank">session详细记录</a>|
<a href="tempLogDateDetailUser.jsp?date=<%=date%>&columnId=<%=line.getId()%>" target="_blank">用户详细记录</a>|
<a href="tempLogDateDetailMobile.jsp?date=<%=date%>&columnId=<%=line.getId()%>" target="_blank">手机号详细记录</a><br>
</td>
<%                
            }
%>
</tr>
<%
        }
%>
</table>