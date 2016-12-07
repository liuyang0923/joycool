<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session="false"%><%@ page import="net.joycool.wap.util.*"%><%!
final static int interval = 1000;		// 延迟的毫秒
static class PingData {
public void PingData() { }
public long calcDelay() {
	if(count == 0)
		return 0;
	return delay / count;
}
	public long last;		// 上一次时间
	public int total = 10;		// 需要总数
	public int count = 0;		// 计数
	public long delay = 0;		// 总延迟
}
%><%
response.setHeader("Cache-Control","no-cache");
HttpSession session = null;
int type = StringUtil.toId(request.getParameter("type"));
long now = System.currentTimeMillis();
%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><wml>
<%
if(type==0){
session = request.getSession();
PingData ping = null;
Object po = session.getAttribute("ping");
if(po instanceof PingData)
	ping = (PingData)po;
if(ping == null){
	ping = new PingData();
	ping.last = now;
	session.setAttribute("ping",ping);
} else {
	long newDelay = (now - interval - ping.last);
	ping.count++;
	if(newDelay>0)
		ping.delay += newDelay;
	ping.last = now;
}
%>
<card title="ping" ontimer="<%=response.encodeURL("ping.jsp")%>"><timer value="<%=interval/100%>"/>
<%=ping.calcDelay()%>ms(<%=ping.count%>次平均)<br/>

<%}else{
long last = StringUtil.toLong(request.getParameter("last"));
long delay = StringUtil.toLong(request.getParameter("delay"));
if(delay<0) delay=0;
int count = StringUtil.toId(request.getParameter("count"));
if(last<=0) {
	last = now;
} else {
	long newDelay = (now - interval - last);
	count++;
	if(newDelay>0)
		delay += newDelay;
	last = now;
}
int calcDelay = 0;
if(count > 0)
	calcDelay = (int)(delay/count);
%>
<card title="ping" ontimer="<%="ping.jsp?type=1&amp;last="+last+"&amp;count="+count+"&amp;delay="+delay%>"><timer value="<%=interval/100%>"/>
<%=calcDelay%>ms(<%=count%>次平均)<br/>



<%}%>
</card></wml>