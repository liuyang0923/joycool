<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
List infoList = vsGame.getInformationList();
int round = 0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="丛林混战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (infoList.size() > 0) {
PagingBean paging = new PagingBean(action,infoList.size(),10,"p");
Iterator iter = infoList.listIterator(paging.getStartIndex());
int co = 0;
for (int i = 0;i<paging.getCountPerPage();i++){
	if(!iter.hasNext())
		continue;
	FightInformationBean info = (FightInformationBean) iter.next();
	if (info.getRound() != round) {round = info.getRound();co=0;%>第<%=round%>回合<br/><%}
	%><%=++co%>.<%=info.getInformation()%><br/>
<%}%>
<%=paging.shuzifenye("fgtinfo.jsp?jcfr=1",true,"|",response)%>
<%}else {%>暂无战斗信息<br/><%}%>
<a href="check.jsp">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>