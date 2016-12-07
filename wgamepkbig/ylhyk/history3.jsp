<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.big.HistoryBigBean" %><%@ page import="net.joycool.wap.action.wgamepk.big.YlhykAction" %><%@ page import="net.joycool.wap.bean.wgame.big.WGamePKBigBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.*" %><%!
static net.joycool.wap.service.infc.IWGamePKService pkService = net.joycool.wap.service.factory.ServiceFactory.createWGamePKService();
%><%
response.setHeader("Cache-Control","no-cache");
YlhykAction action = new YlhykAction(request);
UserBean loginUser = (UserBean) request.getSession().getAttribute("loginUser");
List list = pkService.getWGamePKBigList("right_user_id="+loginUser.getId()+" and game_id=1 and mark=3 order by id desc limit 20");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富豪战绩">
<p align="left">
<%=BaseAction.getTop(request, response)%>
最近挑庄结果<br/>
-------------------<br/>
<%
for(int i = 0;i < list.size();i++){
WGamePKBigBean bean = (WGamePKBigBean)list.get(i);
UserBean user = UserInfoUtil.getUser(bean.getLeftUserId());
if(user==null) continue;
%><%if(bean.getWinUserId() == loginUser.getId()){%>赢了<%}else{%>输给<%}%><a href="/user/ViewUserInfo.do?userId=<%=user.getId() %>"><%=user.getNickNameWml()%></a>
<%=action.bigNumberFormat(bean.getWager())%>(<%=bean.getEndDatetime().substring(5,16)%>)<br/>
<%}%>
<br/>
<a href="history.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>