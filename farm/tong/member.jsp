<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
FarmUserBean farmUser = action.getUser();
TongBean tong = null;
if(farmUser.getTongUser()!=null)
	tong = FarmTongWorld.getTong(farmUser.getTongUser().getTongId());

if(tong==null) action.tip("tip","不存在的门派!");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
List member = FarmTongWorld.getTongUserList(tong.getId());
%>
门派--<%=tong.getNameWml()%>(<%=tong.getCount()%>人)<br/>
==门派弟子==<br/>
<%for(int i=0;i<member.size();i++){
Integer iid = (Integer)member.get(i);
FarmUserBean user = FarmWorld.one.getFarmUserCache(iid);
if(user==null || user.getTongUser() == null) continue;
%>
<a href="../user/info.jsp?id=<%=user.getUserId()%>"><%=user.getNameWml()%></a><%=user.getRank()%>级/<%=user.getClass1Name()%>/<%=user.getTongUser().getDutyName()%><br/>
<%}%>
<a href="tong.jsp">返回</a>|
<%}%>
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>