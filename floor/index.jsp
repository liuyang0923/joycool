<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.floor.FloorAction" %><%@ page import="net.joycool.wap.bean.FloorBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.floor.FloorTopBean" %><%@ page import="java.util.Vector" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
FloorAction action = new FloorAction(request);
action.index(request);
String result =(String)request.getAttribute("result");
String url=("/floor/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="乐酷">
<p align="left">
<%=request.getAttribute("tip") %><br/>
<a href="/floor/addPrize.jsp">添加奖金做楼主</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%FloorBean floorBean=(FloorBean)request.getAttribute("FloorBean");
Vector top =(Vector)request.getAttribute("Toplist");
int id = floorBean.getId();
int userId = floorBean.getUserId();
UserBean user = (UserBean)UserInfoUtil.getUser(userId);
String name = user.getNickName();
long prize = floorBean.getPrize();
int number = floorBean.getNumber();
long num = prize*number;
%>
<%=StringUtil.toWml(floorBean.getContent())%><br/>
当前楼主：<br/>
<%=StringUtil.toWml(name)%><br/>
当前中奖尾数：
<%=floorBean.getFloor()%><br/>
当前奖金：
<%=floorBean.getPrize()%><br/>
总奖金：
<%=StringUtil.bigNumberFormat(num)%>乐币<br/>
共有<%=floorBean.getNumber()%>次中奖，已中奖<br/>
<%=floorBean.getNowPrize()%>次<br/>
目前楼数:<%=floorBean.getCount()%><br/>
<%if(loginUser!=null){%><a href="/floor/tread.jsp?id=<%=id%>&amp;userId=<%=loginUser.getId()%>">【我踩!】</a><br/><%}%>
*踩楼实况*<br/>
<%=
action.getLogString()
%>
*今日踩踩王*<br/>
<%if(top!=null){
for(int i=0;i<top.size();i++)
{
FloorTopBean topBean =(FloorTopBean) top.get(i);
UserBean user1 = (UserBean)UserInfoUtil.getUser(topBean.getUserId());
if(user1==null){continue;}
String name1 = user1.getNickName();
%>
<%=StringUtil.toWml(name1)%><%=topBean.getCount()%>楼<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
<%}%>
</wml>
