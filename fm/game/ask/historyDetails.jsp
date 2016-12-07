<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.*,jc.family.game.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.ask.*"%><%@ page import="jc.family.*"%><%
AskAction action=new AskAction(request,response);
List list=action.getFmAskHistory();
GameHistoryBean myGameBean=(GameHistoryBean)request.getAttribute("myGameBean");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="历史排行"><p align="left"><%if(myGameBean!=null){%>
<%=myGameBean.getHoldTimeToString() %>|第<%=myGameBean.getRank()%>名<br/><%
}else{%>
本家族未参加此场赛事<br/><%}%>
家族|人数|答题积分<br/><% 
if(list!=null&&list.size()>0){
int p=action.getParameterInt("p");
for(int i=0;i<list.size();i++){
GameHistoryBean bean=(GameHistoryBean)list.get(i);
FamilyHomeBean fmHome=action.getFmByID(bean.getFid1());
%>
<a href="/fm/myfamily.jsp?id=<%=bean.getFid1()%>"><%=(i+1)+p*10%>.<%=fmHome==null?"":StringUtil.toWml(fmHome.getFm_name())%>家族</a>|<%=bean.getNumTotal()%>|<%=bean.getShipId()%><br/><%
}%><%=request.getAttribute("paging")%><%
}else{%>
暂无家族记录<br/><%
}%><a href="history.jsp">返回历史排行</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>