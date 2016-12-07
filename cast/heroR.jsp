<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%
	
	SoldierAction action = new SoldierAction(request);
	String tip = null;
	HeroBean hero = null;
	boolean isTip = action.isResult("tip");
	if(!isTip){
		CastleUserBean user = action.getCastleUser();
		hero = user.getHero();
		if(hero == null||!hero.isAlive()){
			response.sendRedirect("ad.jsp");
			return;
		}
		String name = action.getParameterNoEnter("name");
		if(name!=null){
			if(StringUtil.getCLength(name)>10||name.length()==0){
				tip="名称最少一个字最多五个字";
			}else{
				hero.setName(name);
				SqlUtil.executeUpdate("update castle_hero set name='"+StringUtil.toSql(name)+"' where id="+hero.getId(),5);
				tip="指挥官改名成功";
			}
		}
	}
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="指挥官"><p>
<%if(isTip){%><%=action.getTip()%><br/><%}else{

%><%if(tip!=null){%><%=tip%><br/><%}
%>【<%=StringUtil.toWml(hero.getName())%>】(<%=hero.getHeroSoldier().getSoldierName()%>)等级<%=hero.getRank()%><br/>
指挥官改名为:<input name="heron" value=""/><br/>
<anchor>确认修改<go href="heroR.jsp" method="post">
<postfield name="name" value="$heron"/></go></anchor><br/>
<br/>
<%}%>
<a href="hero.jsp">查看指挥官数据</a><br/>
<a href="fun.jsp?t=37">返回开采所</a><br/><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>