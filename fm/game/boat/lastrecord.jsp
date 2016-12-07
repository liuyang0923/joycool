<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.bean.*,java.text.*,java.util.*,net.joycool.wap.util.*,jc.family.*,jc.family.game.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction"%><%
BoatAction action=new BoatAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="本轮统计"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean != null){
	MatchBean matchBean = (MatchBean) BoatAction.service.getMatchBean("game_type=1 and state=2 order by id desc limit 1");
	if(matchBean != null){
		int mid = matchBean.getId();
		int fid = fmbean.getFm_id();
		BoatGameBean gameBean = BoatAction.service.getGameBean("m_id="+mid+" and fid1="+fid);
		if(gameBean != null){
			MemberBean self = null;
			MemberBean controller = null;
			List memberList = BoatAction.service.getMemberList("m_id="+mid+" and fid="+fid+" order by total_hit desc");
			int uid = action.getLoginUser().getId();
			if(memberList != null && memberList.size() > 0){
				int count = 0;
				for(int i=0;i<memberList.size();i++){
					if(count >= 2)
						break;
					MemberBean memberBean = (MemberBean) memberList.get(i);
					if(memberBean == null){
						continue;
					}
					if(memberBean.getUid() == uid){
						self = memberBean;
						count++;
					}
					if(memberBean.getSeat() == 10){
						controller = memberBean;
						count++;
					}
				}
			}
			DecimalFormat numFormat = new DecimalFormat("#.#");
%><%=GameAction.timeFormat(matchBean.getCreateTime(),"yyyy-MM-dd")%><br/>
参与人数:<%=gameBean.getNumTotal()%><br/>
排名:第<%=gameBean.getRank()%>名<br/>
&gt;&gt;家族获得:<br/>
排名积分:<%=gameBean.getScore()%><br/>
奖金:<%=gameBean.getPrize()%>乐币<br/>
游戏经验值:<%=gameBean.getGamePoint()%><br/>
&gt;&gt;个人获得:<br/>
个人功勋:<%if(self!=null){%><%=self.getContribution()%><%}else{%>0<%}%><br/>
里程:<%=numFormat.format(gameBean.getDistance())%>米<br/>
用时:<%=GameAction.getFormatDifferTime(gameBean.getSpendTime())%><br/>
最大速度:<%=numFormat.format(gameBean.getMaxSpeed())%>米/分钟<br/>
成员|总操作数<br/><%
			if(controller != null){
%><a href="/user/ViewUserInfo.do?userId=<%=controller.getUid()%>"><%=UserInfoUtil.getUser(controller.getUid()).getNickNameWml()%></a>|<%=controller.getTotalHit()%><br/><%
			}
			PagingBean paging = new PagingBean(action,memberList.size(),11,"p");
			for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
				MemberBean temp = (MemberBean) memberList.get(i);
				if(temp == null){
					continue;
				}
				if(temp.getSeat() == 10){
					continue;
				}
%><a href="/user/ViewUserInfo.do?userId=<%=temp.getUid()%>"><%=UserInfoUtil.getUser(temp.getUid()).getNickNameWml()%></a>|<%=temp.getTotalHit()%><br/><%
			}
		}else{
			%>你的家族没有参加该次家族龙舟赛.<br/><%
		}
%><a href="rank.jsp?mid=<%=mid%>">查看本轮排名</a><br/><%
	}else{
	%>目前没有记录<br/><%
	}
}else{
%>您还没有加入任何家族<br/><%
}%><a href="match.jsp">返回赛龙舟</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>