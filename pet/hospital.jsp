<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
%><%@include file="check.jsp"%><%
action.hospital();
PetUserBean petUser = action.getPetUser();
String tip = (String)request.getAttribute("tip");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
String url=("/pet/index.jsp");
int mark = StringUtil.toInt((String)request.getAttribute("loginUser"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="医院">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您真不小心，让你的宠物生病了吗?快来治病吧！<br/>

状态:

<%if (petUser.getHealth() > 60) {%>
健康
<%} else if ((petUser.getHealth() > 30) && (petUser.getHealth() <= 60)) {%>
虚弱
<%} else if (petUser.getHealth() > 0) {%>
疾病
<%}else{%>
死亡
<%}%>
<br/>

<%
if(petUser.getHealth() > 0) {
%>
<a href="/pet/hospitalresult.jsp?hospital=1">吃药(健康+5,3000乐币)</a><br/>
<a href="/pet/hospitalresult.jsp?hospital=2">打针(健康+10,5000乐币)</a><br/>
<a href="/pet/hospitalresult.jsp?hospital=3">住院(健康+30,50000乐币)</a><br/>
<%};%>
<%
if(petUser.getHealth() <= 0) {
%>
<a href="/pet/hospitalresult.jsp?hospital=4">急救(急救,100000乐币)</a><br/>
<%}%>

<%
if(mark > 0) {
%>
<a href="/pet/hospitalresult.jsp?hospital=5">宠物孟婆汤</a><br/>

<%}%>
<%
int temp=action.propPetDistortion();
if(temp>0){
%>
<a href="<%=("/pet/hospitalresult.jsp?hospital=6" + "&amp;"+"petId="+petUser.getId())%>">变属性卡</a><br/>
<%}%>
<%
int tempCard=action.propPetDistortionCard();
if(tempCard>0){
%>
<a href="/pet/changepet.jsp?tempCard=<%=tempCard%>">变形卡</a><br/>
<%}%>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>