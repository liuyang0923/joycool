<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser != null){
	String prefix;
	String fm = request.getParameter("fm");
	if(fm!=null)
		prefix = "/enter/enterf.jsp?fm="+fm+"&amp;";
	else
		prefix = "/enter/enter.jsp?";
	//response.sendRedirect(("/enter/enter.jsp?code=" + Encoder.encrypt("" + loginUser.getId())  + "AAAA" + loginUser.getPassword()));
	//return;
	%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="card1" title="乐酷游戏社区" ontimer="<%=response.encodeURL(prefix+"code=" + Encoder.encrypt("" + loginUser.getId())  + "AAAA" + loginUser.getPassword())%>">
<timer value="1"/>
<p align="left">
页面跳转中...
</p>
</card>
</wml>
	<%
}
else {
	//response.sendRedirect((BaseAction.INDEX_URL));
	BaseAction.sendRedirect(null, response);
	return;
}
%>