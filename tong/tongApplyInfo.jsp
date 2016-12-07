<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongApplyBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action= new TongAction(request);
action.tongApplyInfo(request);
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="帮会列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
UserBean user=(UserBean)request.getAttribute("user");
UserStatusBean userStatus=(UserStatusBean)request.getAttribute("userStatus");
TongApplyBean tongApply=(TongApplyBean)request.getAttribute("tongApply");
TongBean tong=(TongBean)request.getAttribute("tong");
String url=("/tong/memberManage.jsp?tongId="+tong.getId());
%>
<card title="帮会管理">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName()) %></a>[<%=user.getId()%>]
<%=user.getGender()==0?"女":"男"%>
<%=user.getAge()%>岁
<%if(user.getCityname()==null || user.getCityname().replace(" ","").equals("")){}else{%>
<%=user.getCityname()%>
<%}%><br/>
<%=userStatus.getRank()%>级
经验<%=userStatus.getPoint()%>
乐币<%=userStatus.getGamePoint()%><br/>
申请书:<br/>
<%=StringUtil.toWml(tongApply.getContent())%><br/>
<anchor title="确定">批准
  <go href="tongApplyEnd.jsp" method="post">
    <postfield name="applyId" value="<%=tongApply.getId()%>"/>
    <postfield name="operation" value="<%=1%>"/>
  </go>
</anchor>
<anchor title="确定">拒绝
  <go href="tongApplyEnd.jsp" method="post">
    <postfield name="applyId" value="<%=tongApply.getId()%>"/>
    <postfield name="operation" value="<%=0%>"/>
  </go>
</anchor><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>