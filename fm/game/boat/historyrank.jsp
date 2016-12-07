<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="net.joycool.wap.bean.*,jc.family.game.*,java.util.*,jc.family.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
BoatAction action=new BoatAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="历史排行"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean != null){
	List matchList = GameAction.service.getMatchList("state=2 and game_type=1 order by id desc limit 100");
	if(matchList != null && matchList.size() > 0){
		BoatRecordBean recordBean = BoatAction.service.getFmRecord("complete=0 and fm_id="+fmbean.getFm_id()+" order by use_time");
		%>本家族最好成绩：<br/><%
		if (recordBean != null) {
		%><%=FamilyAction.sd.format(new Date(recordBean.getCreateTime()))%>|<%=GameAction.getFormatDifferTime(recordBean.getUseTime())%>|<%=BoatAction.boatType[recordBean.getBoatType()]%>龙舟<br/><%
		} else {
		%>还没有记录<br/><%
		}
		%>赛龙舟历史排行<br/><%
		PagingBean paging = new PagingBean(action,matchList.size(),10,"p");
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			MatchBean matchBean = (MatchBean) matchList.get(i);
			if(matchBean != null){
				int mid = matchBean.getId();
				BoatGameBean gameBean = BoatAction.service.getGameBean(" m_id=" + mid + " and fid1="+fmbean.getFm_id());
				if(gameBean != null){
				%><a href="rank.jsp?mid=<%=mid%>"><%=i+1%>.<%=GameAction.timeFormat(matchBean.getCreateTime(),"yyyy-MM-dd")%>|第<%=gameBean.getRank()%>名</a>|<%=BoatAction.boatType[gameBean.getBoatType()]%>龙舟<br/><%
				} else {
				%><a href="rank.jsp?mid=<%=mid%>"><%=i+1%>.<%=GameAction.timeFormat(matchBean.getCreateTime(),"yyyy-MM-dd")%>|未参赛</a><br/><%
				}
			}
		}
	  	%><%=paging.shuzifenye("historyrank.jsp",false,"|",response)%><%
	}else{
		%>目前还没有排名<br/><%
	}
}else{
	%>您还没有加入任何家族<br/><%
}
%><a href="match.jsp">返回赛龙舟</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>