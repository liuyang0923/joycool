<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.snow.*"%><%@ page import="jc.family.*"%><%
SnowGameAction snowAction=new SnowGameAction(request,response);
int tid=snowAction.getParameterInt("tid");
int mid=snowAction.getParameterInt("mid");
request.setAttribute("mid",Integer.valueOf(mid));
int clear=snowAction.getOneTypeTool();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="清雪"><p align="left"><%=BaseAction.getTop(request, response)%>
<%
	if (clear == 0 || clear == 1) {
%>
该道具不存在!<br/>
<%
	} else if (clear == 2) {
		if (request.getAttribute("oneTool") != null) {
			SnowGameToolTypeBean bean = (SnowGameToolTypeBean) request
					.getAttribute("oneTool");
%>
<% String ct="/fm/game/img/sb.gif"; if(bean.getId()==6){ct="/fm/game/img/tq.gif";}else if(bean.getId()==7){ct="/fm/game/img/tuixj.gif";}%>
<img alt="clear" src="<%=ct%>" /><br/>
清扫费用:<%=bean.getSpendMoney()%>雪币<br/>
清扫时间:<%=bean.getSpendTime()%>秒<br/>
清扫效果:减少己方积雪<%=bean.getSnowEffect()%><br/>
<a href="reClear.jsp?tid=<%=tid%>&amp;mid=<%=mid %>">确定清扫</a><br/>
<%
	}
	}
%>
<a href="fight.jsp?mid=<%=mid %>">返回游戏</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>