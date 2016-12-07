<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="net.joycool.wap.bean.*,java.text.*,jc.family.game.*,net.joycool.wap.util.*,java.util.*,jc.family.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
BoatAction action=new BoatAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="本轮排名"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean != null){
	int mid = action.getParameterInt("mid");
	MatchBean mb = null;
	if(mid > 0){
		mb = action.getAMatch(mid);
	}
	if(mb != null){
		List fmList = mb.getFmList();
		BoatGameBean myGameBean = (BoatGameBean) mb.getGameMap().get(new Integer(fmbean.getFm_id())); 
		if(myGameBean != null){
%><%=GameAction.timeFormat(mb.getCreateTime(),"yyyy-MM-dd")%>|第<%=myGameBean.getRank()%>名<br/>家族排名|航程|用时<br/><%
		}
		if(fmList != null && fmList.size() > 0){
			DecimalFormat numFormat = new DecimalFormat("#.#");
			PagingBean paging = new PagingBean(action,fmList.size(),10,"p");
			for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
				Integer key = (Integer) fmList.get(i);
				if(key != null){
					BoatGameBean temp = (BoatGameBean) mb.getGameMap().get(key);
					FamilyHomeBean fmhome=BoatAction.getFmByID(temp.getFid1());
%><%=i+1%>.<a href="/fm/myfamily.jsp?id=<%=fmhome.getId()%>"><%=StringUtil.toWml(fmhome.getFm_name())%></a>|<%=numFormat.format(temp.getDistance())%>米|<%=GameAction.getFormatDifferTime(temp.getSpendTime())%>|<%=BoatAction.boatType[temp.getBoatType()]%>龙舟<br/><%
				}
			}
  		%><%=paging.shuzifenye("rank.jsp?jcfr=1&amp;mid="+mid,true,"|",response)%><%
			
		}
	}else{
		%>目前还没有排名<br/><%
	}
}else{
	%>您还没有加入任何家族<br/><%
}
%><a href="historyrank.jsp">返回历史排行</a><br/><a href="match.jsp">返回赛龙舟</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>