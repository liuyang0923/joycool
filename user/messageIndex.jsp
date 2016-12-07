<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(net.joycool.wap.util.Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的信箱">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=loginUser.showImg("img/message.gif")%>
---我的信箱---<br/>
1.<a href="ViewMessages.do">我收到的信件</a><br/>
2.<a href="ViewSendMessages.do">我发送的信件</a><br/>
3.<a href="../user/ViewFriends.do">给好友发送信件</a><br/>
<br/>    
<anchor title="back"><prev/>返回上页</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>