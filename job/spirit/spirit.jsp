<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.job.SpiritBean"%><%@ page import="net.joycool.wap.action.job.SpiritAction"%><%
response.setHeader("Cache-Control","no-cache");
SpiritAction action=new SpiritAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = new UserStatusBean();
if (loginUser != null){
	us=UserInfoUtil.getUserStatus(loginUser.getId());
}
int Point=us.getPoint();
SpiritBean spirit=null;
Vector	vecSpirit=null;
vecSpirit=action.getSpirit();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="静国神社">
<p align="left">
<%=BaseAction.getTop(request, response)%>
这里是静国神社的中文网站，这里供奉着西条英机，山本五十七等。作为一个中国人，请问您想做点什么？<br/>
<%for(int j=0;j<vecSpirit.size();j++){
	spirit=(SpiritBean)vecSpirit.get(j);%>
<a href="/job/spirit/results.jsp?id=<%=spirit.getId()%>">
<%=spirit.getTitle()%>
<%if(spirit.getPrice()/10000==0){%>
(<%=spirit.getPrice()%>乐币)<br/>
<%}else{%>
(<%=spirit.getPrice()/10000%>万乐币)<br/>
<%}%>
</a>
<%}%><br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>