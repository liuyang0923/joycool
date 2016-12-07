<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="用户特殊荣誉">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
int userId = StringUtil.toInt(request.getParameter("userId"));
UserBean user = UserInfoUtil.getUser(userId);

%>

<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><a href="/user/ViewUserInfo.do?userId=<%=userId%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
<%if(loginUser.getId() == userId){ %>
<a href="editExtraInfo.jsp">编辑显示顺序</a><br/>
<%} %>
<% // 显示用户信息图片，录入新人卡、旗帜卡等等
String extraInfo = user.getExtraInfoFull(-1);
if(extraInfo!=null){%>
<%=extraInfo%>
<%}else{%>
该用户没有特殊信息<br/>
<%}%>
<a href="/user/ViewUserInfo.do?userId=<%=userId%>">返回用户信息</a><br/> 
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/> 
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
</p>
</card>
</wml>