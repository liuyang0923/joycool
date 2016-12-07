<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*,java.util.*,jc.family.game.vs.term.*,jc.family.*,jc.family.game.vs.*"%><%
TermAction action=new TermAction(request,response);
int id = action.getParameterInt("id");
TermBean term = TermAction.termService.getTermBean("id="+id);
if(term == null) {
	response.sendRedirect("term.jsp");
	return;
}
List matchingList = new ArrayList(1);
List glist=VsAction.getVsGameBean();
if(glist!=null&&glist.size()>0){
	for(int i=0;i<glist.size();i++){
		VsGameBean bean=(VsGameBean)glist.get(i);
		if(!bean.isEnd()&&bean.isScore()){
			matchingList.add(bean);
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族争霸赛"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(matchingList.size()>0){
%>【进行中的比赛】<br/><%
	for(int i=0;i<matchingList.size();i++){
		VsGameBean bean=(VsGameBean)matchingList.get(i);
		%>[<%=bean.getFmANameWml()%>]VS[<%=bean.getFmBNameWml()%>]-<a href="<%=bean.getGameUrl()%>?id=<%=bean.getId()%>&amp;c=1">进入比赛</a><br/><%
	}
%><%}%>
【<%=term.getName()%>】<br/>
开始于:<%=net.joycool.wap.util.DateUtil.formatDate1(new Date(term.getCreateTime()))%><br/>
<%=term.getInfo()%><br/>
参与家族:<%
for(int i=0;i<term.getFmList().size();i++){
Integer iid = (Integer)term.getFmList().get(i);
FamilyHomeBean fm = FamilyAction.getFmByID(iid.intValue());
if(i!=0) out.print(',');
%><a href="/fm/myfamily.jsp?id=<%=fm.getId()%>"><%=fm.getFm_nameWml()%></a>
<%}%><br/>
【详细比赛日程安排】<br/>
<%
List list=action.termService.getTermMatchBeanList("term_id="+id+" order by start_time");
if(list!=null&&list.size()>0){
	for(int i=0;i<list.size();i++){
		TermMatchBean termMatch = (TermMatchBean) list.get(i);
		VsGameBean bean=action.getVsGameById(termMatch.getChallengeId());
		%>[<%=DateUtil.sformatTime(termMatch.getStartTime())%>]<%
		if(bean!=null){
			if(bean.getState()!=VsGameBean.gameEnd){
				%><a href="<%=bean.getGameUrl()%>?id=<%=bean.getId()%>&amp;c=1"><%=bean.getFmANameWml()%>VS<%=bean.getFmBNameWml()%></a>(进行中)<br/><%
			}else{
				%><a href="<%=bean.getGameUrl()%>?id=<%=bean.getId()%>"><%=bean.getFmANameWml()%>VS<%=bean.getFmBNameWml()%></a>(已结束)<br/><%
			}
		}else{
			%><%=StringUtil.toWml(action.getFmName(termMatch.getFmidA()))%>VS<%=StringUtil.toWml(action.getFmName(termMatch.getFmidB()))%><br/><%
		}
	}
}else{
%>(暂无)<br/><%
}
%>
<a href="/jcforum/forum.jsp?forumId=11799">+争霸赛讨论区+</a><br/>
<a href="/Column.do?columnId=12711">+家族争霸赛章程+</a><br/>
<a href="term.jsp">&gt;&gt;家族争霸赛历史</a><br/>
&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>