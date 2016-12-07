<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="jc.family.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
FightBoxAction action = new FightBoxAction(request,response);
%><%@include file="inc.jsp"%><%
String[] weaps = vsGame.weapons;
BoxBean boxBean = action.getBoxByIJ(vsGame);
List boxUserList = null;
if (boxBean != null) {
	boxUserList = boxBean.getBoxUserList();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="丛林混战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (boxUserList.size() == 0) {%>这个地点空无一人!<br/><%} else {
	List listA = new ArrayList();
	List listB = new ArrayList();
	for (int m=0;m<boxUserList.size();m++){
		BoxUserBean tempBean = (BoxUserBean) boxUserList.get(m);
		if (vsUser != null){
			if(tempBean.getSide() != vsUser.getSide()) {
				listA.add(tempBean);
			} else {
				listB.add(tempBean);
			}
		} else {
			if (tempBean.getSide() == 0) {
				listA.add(tempBean);
			} else {
				listB.add(tempBean);
			}
		}
	}
	if (listA.size() > 0) {
		if (vsUser != null) {%>敌:<%} else {%>攻:<%}%><%=listA.size()%>人<br/><%
		for (int m=0;m<listA.size();m++){
			BoxUserBean tempBean = (BoxUserBean) listA.get(m);
			int weap = tempBean.getWeapon();
			if (vsUser != null && vsUser.getSide() != tempBean.getSide()) {
				weap += 3;
			}
%><%=m+1%>.[<%=weaps[weap]%>]血:<%=tempBean.getBlood()%>(<%=UserInfoUtil.getUser(tempBean.getUserId()).getNickNameWml()%>)<br/><%
		}
	}
	if (listB.size() > 0) {
		if (vsUser != null) {%>友:<%} else {%>守:<%}%><%=listB.size()%>人<br/><%
		for (int m=0;m<listB.size();m++){
			BoxUserBean tempBean = (BoxUserBean) listB.get(m);
			int weap = tempBean.getWeapon();
			if (vsUser != null && vsUser.getSide() != tempBean.getSide()) {
				weap += 3;
			}
%><%=m+1%>.[<%=weaps[weap]%>]血:<%=tempBean.getBlood()%>(<%=UserInfoUtil.getUser(tempBean.getUserId()).getNickNameWml()%>)<br/><%
		}
	}
}
%><a href="move.jsp">返回查看</a><br/>
<a href="check.jsp">返回战场</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>