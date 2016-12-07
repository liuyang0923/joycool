<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%
//获取手机号和UA
String mobile = (String) session.getAttribute("userMobile");
if(mobile==null){
	mobile = "";
}
String ua = (String)session.getAttribute("UA");
if(ua==null){
	ua = "";  
}
ua = ua.toLowerCase();
//nokia或者索爱的手机
if(mobile.length()>=11 && (ua.indexOf("sonyericsson")!=-1 || ua.indexOf("nokia")!=-1)){
	//liuyi_2006-11-23_修改push发送方法 start
    String content = "欢迎来到乐酷游戏社区！";
    String link = "wap.joycool.net";
    String msg = content;   
    SmsUtil.sendPush(msg, mobile, link);
    //liuyi_2006-11-23_修改push发送方法 end
}
else{ //其它手机类型
	//response.sendRedirect(("/favorite/mobileType.jsp"));
	BaseAction.sendRedirect("/favorite/mobileType.jsp", response);
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="书签" ontimer="<%=response.encodeURL(BaseAction.INDEX_URL)%>">
<timer value="30"/>
<p align="left">
添加书签成功！<br/>
3秒钟后自动跳转～<br/>
<br/>
<a href="<%=(BaseAction.INDEX_URL)%>">返回上一级</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</card>
</wml>