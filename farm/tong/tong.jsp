<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
FarmUserBean farmUser = action.getUser();
int userTongId = 0;
if(farmUser.getTongUser()!=null)
	userTongId=farmUser.getTongUser().getTongId();
int id = action.getParameterInt("id");
if(id==0)
	id = userTongId;
TongBean tong = FarmTongWorld.getTong(id);
if(tong==null) action.tip("tip","不存在的门派!");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{%>
门派--<%=tong.getNameWml()%>(等级<%=tong.getRank()%>)<br/>
派系:(无)<br/>
总人数:<%=tong.getCount()%><br/>
<%if(userTongId == id){%>
所在地区:(无)<br/>
<a href="member.jsp">本门弟子列表</a><br/>
<a href="chat.jsp">门派闲聊</a><br/>
<%if(farmUser.getTongUser().getDuty()<8){%><a href="leave.jsp">离开<%=tong.getNameWml()%></a><br/><%}%>
<%} else if(userTongId == 0&&farmUser.getInviteTongUser()!=0) {
FarmUserBean user = action.world.getFarmUserCache(farmUser.getInviteTongUser());
if(user!=null&&user.getTongUser()!=null&&user.getTongUser().getTongId()==id){
%>
<a href="join.jsp">应邀加入<%=tong.getNameWml()%></a><br/>
<%}}%>
<%}%>
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>