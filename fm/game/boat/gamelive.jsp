<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="java.text.*,net.joycool.wap.bean.*,jc.family.game.*,net.joycool.wap.util.*,java.util.*,jc.family.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
BoatAction action=new BoatAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="比赛实况"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean != null){
	int mid = action.getParameterInt("mid");
	MatchBean matchBean = (MatchBean) GameAction.matchCache.get(new Integer(mid));
	if(matchBean != null){
		List fmList2 = matchBean.getFmList();
		if(fmList2 != null && fmList2.size() > 0){
			List fmGameList = new ArrayList();
			for(int i=0;i<fmList2.size();i++){
				FmApplyBean fmApplyBean = (FmApplyBean) fmList2.get(i); 
				if(fmApplyBean != null){
					BoatGameBean gameBean = (BoatGameBean) matchBean.getGameMap().get(new Integer(fmApplyBean.getFid()));
					if(gameBean != null){
						fmGameList.add(gameBean);
					}
				}
			}
			fmGameList = BoatAction.sortByDistance(fmGameList);
			PagingBean paging = new PagingBean(action,fmGameList.size(),10,"p");
			for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
				BoatGameBean gameBean = (BoatGameBean) fmGameList.get(i);
				if(gameBean != null){
					FamilyHomeBean fhb = FamilyAction.getFmByID(gameBean.getFid1());
					if(fhb != null){
						DecimalFormat numFormat = new DecimalFormat("#.#");
					%><%=i+1%>.<a href="game.jsp?mid=<%=matchBean.getId()%>&amp;fid=<%=gameBean.getFid1()%>"><%=StringUtil.toWml(fhb.getFm_name())%>家族</a>|<%=numFormat.format(gameBean.getDistance())%>米<br/><%
					}
				}
			}
  		%><%=paging.shuzifenye("gamelive.jsp?mid="+matchBean.getId(),true,"|",response)%><%
		}
	} else {
	%>没有这场赛事!<br/><%
	}
}else{
	%>您还没有加入任何家族!<br/><%
}
%><a href="game.jsp">返回龙舟比赛</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>