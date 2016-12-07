<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="net.joycool.wap.bean.*,jc.family.game.*,java.util.*,jc.family.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
BoatAction action=new BoatAction(request,response);
String[] gameType = {"","赛龙舟","雪仗","问答"};
FamilyUserBean fmbean = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="本家族历史活动记录"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean != null){
	List gameList = GameAction.service.getHistoryGameList("fid1=" + fmbean.getFm_id() + " order by id desc limit 100");
	if(gameList != null && gameList.size() > 0){
		PagingBean paging = new PagingBean(action,gameList.size(),10,"p");
		%>活动时间|类别|名次/输赢<br/><%
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			GameHistoryBean gameBean = (GameHistoryBean) gameList.get(i);
			if(gameBean != null){
				MatchBean mb = BoatAction.service.getMatchBean(" id="+gameBean.getMid());
				if(mb != null){
					if(mb.getType()==1){
					%><%=i+1%>.<a href="fmscore.jsp?mid=<%=mb.getId()%>"><%=GameAction.timeFormat(mb.getCreateTime(),"yyyy-MM-dd")%>|<%=gameType[mb.getType()]%>|第<%=gameBean.getRank()%>名</a><br/><%
					}
					if(mb.getType()==2){
					%><%=i+1%>.<a href="snow/activityDetails.jsp?mid=<%=mb.getId()%>"><%=GameAction.timeFormat(mb.getCreateTime(),"yyyy-MM-dd")%>|<%=gameType[mb.getType()]%>|<%if(gameBean.getRank()==1){%>赢<%}else if(gameBean.getRank()==2){ %>输<%}else if(gameBean.getRank()==3||gameBean.getRank()==4){%>平<%}%></a><br/><%
					}
					if(mb.getType()==3){
					%><%=i+1%>.<a href="ask/record.jsp?backto=/fm/game/historygame.jsp&#38;mid=<%=mb.getId()%>"><%=GameAction.timeFormat(mb.getCreateTime(),"yyyy-MM-dd")%>|<%=gameType[mb.getType()]%>|第<%=gameBean.getRank()%>名</a><br/><%
					}
				}
			}
		}
	  	%><%=paging.shuzifenye("historygame.jsp",false,"|",response)%><%
	}else{
		%>目前还没有排名<br/><%
	}
}else{
	%>您还没有加入任何家族<br/><%
}
%><a href="fmgame.jsp">返回家族活动</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>