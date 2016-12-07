<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.Date,net.joycool.wap.util.*,jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
int id=action.getParameterInt("id");
int fmId=action.getFmId();
FamilyHomeBean fmHome=action.getFmByID(id);
if(fmHome==null){
response.sendRedirect("/fm/index.jsp");return;
}
VsBean vsBean=VsAction.getFmVsInfo(id);
FamilyUserBean fmUser = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
int[] socre=action.getFmSocre(id);%>
=正在进行的挑战=<br/><%
if(vsBean.getGameid()!=0){
VsGameBean bean=action.getVsGameById(vsBean.getGameid());
if(bean!=null){%><%=bean.getGameIdName()%>(<%=DateUtil.formatTime(new Date(bean.getStartTime()))%>-<%=DateUtil.formatTime(new Date(bean.getEndTime()))%>)<br/>
对战家族:<%=bean.getChallFmidNameWml(id)%><br/>
挑战金:<%=bean.getWager()%>亿<br/><%
if(id==fmId){%><a href="<%=bean.getGameUrl()%>?id=<%=bean.getId()%>&amp;c=1">进入游戏</a>|<%}%><a href="<%=bean.getGameUrl()%>?id=<%=bean.getId()%>">观看比赛</a><br/><%
}
}else{
%>无<br/><%
}%>
=家族挑战积分=<br/><%
for(int i=0;i<VsGameBean.getMaxGameId();i++){
VsConfig vsConfig=VsGameBean.getVsConfig(i);
if(vsConfig!=null&&vsConfig.getHide()==0){
%><a href="gameinfo.jsp?id=<%=i%>&amp;fmId=<%=id%>"><%=VsGameBean.getGameIdName(i)%></a>:<%=socre[i]!=0?socre[i]+"":"未参赛"%><br/><%
}
}
if(fmId>0&&id!=0){
	if(fmId!=id){
		if(FamilyUserBean.isflag_game(fmUser.getFm_flags())){
			%><a href="dealchallenge.jsp?fmId=<%=id%>">向该家族发起挑战!!!</a><br/><%
		}
	}else{
		%><a href="challengeList.jsp">查看家族挑战邀请</a><br/><%
	}
}
%>&gt;&gt;本日已进行的挑战:<%=vsBean.getTime()%><br/>
<%if(fmId==id){%>
=已发起的挑战=<br/><%
if(vsBean.isChallenge()){
%><%=vsBean.getChallGameIdName()%>|<%=vsBean.getChallTimeSfd()%><br/>
对战家族:<%=vsBean.getChallFmidNameWml()%><br/><%
if(FamilyUserBean.isflag_game(fmUser.getFm_flags())&&vsBean.getChallTime()+60*1000<System.currentTimeMillis()){
%><a href="challcancel.jsp">取消挑战</a><br/><%
}else{
%>取消挑战<br/><%
}
}else{
if(FamilyUserBean.isflag_game(fmUser.getFm_flags())){
%><a href="fmhomeList.jsp">发起挑战</a><br/><%}
}%>
<a href="vselites.jsp">&#62;&#62;家族挑战精英</a><br/><%
}%>=挑战历史记录=<a href="exploitsList.jsp?id=<%=id%>">详细</a><br/><%
java.util.List list=action.vsService.getVsExploitDetailList("fm_id="+id+" order by id desc limit 3");
for(int i=0;i<list.size();i++){
VsExploits vsE=(VsExploits)list.get(i);
%><%=i+1%>.<%
VsGameBean bean=action.getVsGameById(vsE.getId());
if(bean!=null){
%><%=vsE.toString2()%>(<%=vsE.getEndTimeStr()%>)<a href="<%=bean.getGameUrl()%>?id=<%=bean.getId()%>">查看</a><br/>
<%}else{%>
<%=vsE.toString2()%>(<%=vsE.getEndTimeStr()%>)<br/><%
}
}%>
<a href="/Column.do?columnId=12222">家族挑战说明</a><br/>
&lt;<a href="/fm/myfamily.jsp?id=<%=id==0?fmId:id%>"><%=fmHome.getFm_nameWml()%></a>&lt;<a href="/fm/index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>