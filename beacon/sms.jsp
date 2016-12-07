<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.*,java.io.*,java.util.*"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.*"%><%

String tip="";

if(request.getParameter("content")!=null) {
	String c = "http://218.241.67.234:9000/QxtSms/QxtFirewall?OperID=ydsj&OperPass=123456&SendTime=&ValidTime=&AppendID=&DesMobile="+
		request.getParameter("mobile")+"&Content="+URLEncoder.encode(request.getParameter("content"),"gbk")+"&ContentType=8";
		System.out.println(c);
	URL url = new URL(c);
	InputStream in = url.openStream();
	in.read();
	in.close();
	tip = new String(baos.toByteArray(),"utf-8");

}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷门户">
<p>
<%=StringUtil.toWml(tip)%><br/>

手机号<input name="mobile"/><br/>
内容<input name="content"/><br/>
<anchor title="确定">确定
  <go href="sms2.jsp" method="post">
    <postfield name="mobile" value="$mobile"/>
    <postfield name="content" value="$content"/>
  </go>
</anchor><br/>

</p>
</card>
</wml>