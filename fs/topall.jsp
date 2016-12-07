<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="java.util.List" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.topall();
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/fs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=FSAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回城市)<br/>
<a href="/fs/index.jsp">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
FSUserBean fsUser = action.getFsUser();
int highscore = action.getAttributeInt("highscore");
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
List fsUserList = (List)request.getAttribute("fsUserList");
int type = action.getAttributeInt("type");
%>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
城市浮生总排行榜<br/>
----------------<br/>
<%if(fsUser!=null){%>
您最高分为：<%=highscore%>元，排名第<%=action.getAttributeInt("count")%>。<br/>
<%}%>
<%
FSTopBean fsUser1 = null;
UserBean user = null;
for(int i =0 ;i<fsUserList.size();i++){
	fsUser1 = (FSTopBean)fsUserList.get(i);
	user=UserInfoUtil.getUser(fsUser1.getUserId());
	if(user==null)continue;%>
	<%=i+1+pagingBean.getCurrentPageIndex()*10%>.
	<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>
	最高分:<%=StringUtil.bigNumberFormat(fsUser1.getHighScore())%>元<br/>
<%}%>
<%=PageUtil.shuzifenye(pagingBean, "topall.jsp?type=" + type, true, "|", response)%>
<%if(!fsUser.isGameOver()){%>
<a href="/fs/index.jsp">返回游戏</a><br/>
<%}%>
<a href="/fs/top.jsp?type=<%=type%>">查看最近排行榜</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>