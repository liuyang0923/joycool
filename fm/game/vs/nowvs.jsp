<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.Date,net.joycool.wap.util.*,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
int a=action.getParameterInt("a");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(a==0){
%>进行中|<a href="nowvs.jsp?a=1">已完赛</a><br/><%
int nowvs=0;
List list=action.getVsGameBean();
int p=action.getParameterInt("p");
if(list!=null&&list.size()>0){
for(int i=0;i<list.size();i++){
VsGameBean bean=(VsGameBean)list.get(i);
if(bean.getState()!=bean.gameEnd){
nowvs++;
%><%=nowvs+10*p%>.<%=bean.getGameIdName()%>
[<%=bean.getFmANameWml()%>]VS[<%=bean.getFmBNameWml()%>](挑战金<%=bean.getWager()%>亿)<a href="<%=bean.getGameUrl()%>?id=<%=bean.getId()%>">观看比赛</a><br/><%
}
}
}
if(nowvs==0){%>(暂无)<br/><%}
}else{
int p=action.getParameterInt("p");
int count=action.service.selectIntResult("select count(id) from fm_vs_exploits");
PagingBean paging = new PagingBean(action,count, 10, "p");
List list=action.vsService.getVsExploitsList("1 order by id desc limit "+paging.getStartIndex()+","+paging.getCountPerPage());
%><a href="nowvs.jsp">进行中</a>|已完赛<br/><%
for(int i=0;i<list.size();i++){
VsExploits vsE=(VsExploits)list.get(i);
%><%=i+1+p*10%>.<%
VsGameBean bean=action.getVsGameById(vsE.getId());
if(bean!=null){
%><%=vsE.toString2()%>(<%=vsE.getEndTimeStr()%>)<a href="<%=bean.getGameUrl()%>?id=<%=bean.getId()%>">观看</a><br/>
<%}else{%>
<%=vsE.toString2()%>(<%=vsE.getEndTimeStr()%>)<a href="exdetail2.jsp?id=<%=vsE.getId()%>">查看</a><br/><%
}
}%><%=paging.shuzifenye("nowvs.jsp?a=1", true, "|", response)%>
<%}%><a href="/Column.do?columnId=12222">+挑战赛规则和介绍+</a><br/>
<a href="/fm/index.jsp">&lt;&lt;返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>