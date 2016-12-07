<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.wxsj.action.flush.*,java.util.Random"%><?xml version="1.0"?>
<%
String flushWml = FlushAction.getFlushWml(request);
%>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if(!flushWml.equals("")) {
	String [] ss = flushWml.split("\n");
	Random rand = new Random();
	String ddd = ss[rand.nextInt(ss.length)];
	ddd = ddd.replace("\"", "'");
%>
<card id="main" title="读取中..." ontimer="<%=response.encodeURL(ddd)%>">
<timer value="30"/>
<%
}
else {
%>
<card id="main" title="读取中...">
<%
}
%>
<p align="left">
读取中...<br/>
</p>
</card>
</wml>