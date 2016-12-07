<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.equip();
FarmUserBean user = action.getUser();
BattleStatus bs = user.getCurStat();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(user != null){%>

<%=user.getNameWml()%>(<%=user.getRank()%>级)<br/>
战斗等级:<%=user.getProRank(FarmProBean.PRO_BATTLE)%><br/>
==人物基本属性==<br/>
<%if(user.getPro(FarmProBean.PRO_BATTLE)!=null){
int rate = user.getPro(FarmProBean.PRO_BATTLE).getRank() * 40 + 200;
%>
血:<%=user.hp%>/<%=bs.hp%><br/>
气力:<%=user.mp%>/<%=bs.mp%><br/>
体力:<%=user.sp%>/<%=bs.sp%><br/>
攻击:<%=bs.attack1%>-<%=bs.attack1+bs.attack1Float%><br/>
攻击速度:<%=FarmWorld.formatNumber((float)bs.attackInterval/1000)%>s<br/>
防御:<%=bs.defense1%>(<%=bs.defense1*100/(rate+bs.defense1)%>%)<br/>
致命:<%=bs.ds%>%|
爆发:<%=(int)(bs.cb*100)%>%<br/>
<%=FarmUserBean.attrName[0]%>:<%=bs.attr1%><br/>
<%=FarmUserBean.attrName[1]%>:<%=bs.attr2%><br/>
<%=FarmUserBean.attrName[2]%>:<%=bs.attr3%><br/>
<%=FarmUserBean.attrName[3]%>:<%=bs.attr4%><br/>
<%=FarmUserBean.attrName[4]%>:<%=bs.attr5%><br/>
<%if(bs.element1>0){%><%=FarmUserBean.elementName[1]%>元素+<%=bs.element1%><br/><%}%>
<%if(bs.element2>0){%><%=FarmUserBean.elementName[2]%>元素+<%=bs.element2%><br/><%}%>
<%if(bs.element3>0){%><%=FarmUserBean.elementName[3]%>元素+<%=bs.element3%><br/><%}%>
<%if(bs.element4>0){%><%=FarmUserBean.elementName[4]%>元素+<%=bs.element4%><br/><%}%>
<%if(bs.element5>0){%><%=FarmUserBean.elementName[5]%>元素+<%=bs.element5%><br/><%}%>
<%}%>
<%}%>

<a href="info.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>