<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
int id=action.getParameterInt("id");
int fmId=action.getParameterInt("fmId");
int p=action.getParameterInt("p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
VsExploits bean=action.vsService.getVsExploitDetail("b.id="+id+" and fm_id="+fmId);
if(bean==null){
%>挑战信息不存在<br/><%
}else{
%><%=net.joycool.wap.util.DateUtil.formatDate2(bean.getcTime())%><br/>
参与人数:<%=bean.getUserCount()%>人<br/>
挑战游戏:<%=bean.getGameName()%><br/>
对战家族:<a href="/fm/myfamily.jsp?id=<%=fmId==bean.getFmA()?bean.getFmB():bean.getFmA()%>"><%=bean.getFmidNameWml(fmId)%></a><br/>
奖金:<%=action.getDouble(bean.getWagerStr(fmId))%>亿<br/>
积分:<%=bean.getScoreStr(fmId)%><br/><%
if(bean.getUserA()!=0){
%>发起者:<a href="/user/ViewUserInfo.do?userId=<%=bean.getUserA()%>"><%=bean.getUserANickNameWml()%></a><br/>
应战者:<a href="/user/ViewUserInfo.do?userId=<%=bean.getUserB()%>"><%=bean.getUserBNickNameWml()%></a><br/>
<%
}
VsGameBean vsbean=action.getVsGameById(bean.getId());
if(vsbean!=null){
%><a href="<%=vsbean.getGameUrl()%>?id=<%=vsbean.getId()%>">比赛详细信息</a><br/><%
}
}%><a href="exploitsList.jsp?id=<%=fmId%>&amp;p=<%=p%>">返回家族挑战</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>