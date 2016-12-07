<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="java.text.*,net.joycool.wap.bean.*,jc.family.game.*,net.joycool.wap.util.*,java.util.*,jc.family.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
GameAction action=new GameAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="赛龙舟"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean != null){
	int mid = action.getParameterInt("mid");
	UserBean userbean = action.getLoginUser();
	int uid = userbean.getId();
	int fid = fmbean.getFm_id();
	MatchBean mb = null;
	if(mid > 0){
		mb = GameAction.service.getMatchBean(" id=" + mid);
	}
	if(mb != null){
		GameHistoryBean myGameBean = GameAction.service.getHistoryGameBean("fid1="+fid+" and m_id="+mid);
		if(myGameBean != null){
			List memberList = null;
			if(myGameBean.getType() == 1){// 龙舟
				memberList = BoatAction.service.getMemberList("m_id="+mid+" and fid="+fid+" order by total_hit desc");
				MemberBean self = BoatAction.service.getMemberBean("m_id="+mid+" and fid="+fid+" and uid="+uid);
				DecimalFormat numFormat = new DecimalFormat("#.#");
%><%=GameAction.timeFormat(mb.getCreateTime(),"yyyy-MM-dd")%><br/>
参与人数:<%=myGameBean.getNumTotal()%><br/>
排名:第<%=myGameBean.getRank()%>名<br/>
&gt;&gt;家族获得:<br/>
排名积分:<%=myGameBean.getScore()%><br/>
奖金:<%=myGameBean.getPrize()%>乐币<br/>
游戏经验值:<%=myGameBean.getGamePoint()%><br/>
&gt;&gt;个人获得:<br/>
个人功勋:<%if(self!=null){%><%=self.getContribution()%><%}else{%>0<%}%><br/>
里程:<%=numFormat.format(myGameBean.getDistance())%>米<br/>
用时:<%=GameAction.getFormatDifferTime(myGameBean.getSpendTime())%><br/>
最大速度:<%=numFormat.format(myGameBean.getMaxSpeed())%>米/分钟<br/>
成员|总操作数<br/><%
				if(memberList != null && memberList.size() > 0){
					MemberBean controller = BoatAction.service.getMemberBean("seat=10 and m_id="+mid+" and fid="+fid);
					if(controller != null){
%><a href="/user/ViewUserInfo.do?userId=<%=controller.getUid()%>"><%=UserInfoUtil.getUser(controller.getUid()).getNickNameWml()%></a>|<%=controller.getTotalHit()%><br/><%
					}
					PagingBean paging = new PagingBean(action,memberList.size(),11,"p");
					for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
						MemberBean memberBean = (MemberBean) memberList.get(i);
						if(memberBean == null){
							continue;
						}
						if(memberBean.getSeat() == 10){
							continue;
						}
	%><a href="/user/ViewUserInfo.do?userId=<%=memberBean.getUid()%>"><%=UserInfoUtil.getUser(memberBean.getUid()).getNickNameWml()%></a>|<%=memberBean.getTotalHit()%><br/><%
					}
		  			%><%=paging.shuzifenye("fmscore.jsp",false,"|",response)%><%
				} else {
			%>无家族成员操作!<br/><%
				}
			} 
		} else {
		%>没有该赛事记录!<br/><%
		}
	}else{
		%>还没有该赛事!<br/><%
	}
}else{
	%>您还没有加入任何家族<br/><%
}
%><a href="/fm/game/historygame.jsp">返回家族活动记录</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>