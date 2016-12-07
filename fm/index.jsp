<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.*,net.joycool.wap.util.StringUtil,java.util.List,net.joycool.wap.bean.PagingBean,jc.family.game.MatchBean,jc.family.game.GameAction,net.joycool.wap.util.DateUtil,jc.family.game.vs.*,java.util.Date,jc.family.*,jc.family.game.vs.term.*"%><%!
	static int[] offer = {20249,20108,20111,20188,20041,20334,20373,20369,20356};
	static String[] weekGame = {"","赛龙舟","雪仗","问答"};
%><%
FamilyAction action=new FamilyAction(request,response);
UserBean userbean=action.getLoginUser();
int fmId=action.getFmId();
FmApply fmApply=null;
java.util.List list=null;
if(userbean!=null){
fmApply=action.service.selectFmApplybyId(userbean.getId());
list=action.service.selectFamilyApplyUserList(userbean.getId());
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmId>0){%>
<a href="myfamily.jsp?id=<%=fmId%>">&lt;&lt;我的家族</a><br/><%
}else if(fmApply!=null&&fmApply.getIsok()==0&&fmId==0){%>
<a href="buildfail.jsp?applyId=<%=fmApply.getId()%>">我的家族创建</a><br/><%
}else if(list!=null&&list.size()>0){
for(int i=0;i<list.size();i++ ){
FmApplyUser fmApplyUser=(FmApplyUser)list.get(i);
if(fmApplyUser.getId()!=userbean.getId()&&fmApplyUser.getTongId()==0){%><a href="buildfail.jsp?applyId=<%=fmApplyUser.getFm_apply_id()%>">我参与的家族创建</a><br/><%}
}
}
%>
【推荐家族】<a href="topmore.jsp">更多</a><br/>
<%
int[] offer2 = new int[offer.length*2];
for(int i=0;i<offer.length;i++){
	offer2[i] = offer[i];
	offer2[i+offer.length] = offer[i];
}
int rnd = net.joycool.wap.util.RandomUtil.nextInt(offer.length);
for(int i=0;i<5;i++){
int fid = offer2[rnd+i];
FamilyHomeBean fmrnd = FamilyAction.getFmByID(fid);
if(fmrnd==null) continue;
%><%if(i!=0){%>,<%}
%><a href="myfamily.jsp?id=<%=fid%>"><%=fmrnd.getFm_nameWml()%></a><%
}%><br/><%
TermBean term = TermAction.term;
if(term!=null){
%>【<a href="game/vs/match.jsp?id=<%=term.getId()%>"><%=term.getName()%></a>】<br/>
<%
}
%>
<a href="game/vs/nowvs.jsp">+查看挑战赛实况+</a><br/><%
int nowvs=0;
list=VsAction.getVsGameBean();
if(list!=null&&list.size()>0){
	for(int i=0;i<list.size();i++){
		VsGameBean bean=(VsGameBean)list.get(i);
		if(bean.getState()!=bean.gameEnd){
			nowvs++;
			%><%=nowvs%>.<a href="<%=bean.getGameUrl()%>?id=<%=bean.getId()%>"><%=bean.getFmANameWml()%>VS<%=bean.getFmBNameWml()%></a><br/><%
		}
		if(nowvs==2){
			continue;
		}
	}
}
%>
<a href="/jcforum/forum.jsp?forumId=11799">+家族玩家交流区+</a><br/>
今日活动:<%
List mlist = GameAction.getCurrentMatchList();
if(mlist != null && mlist.size() > 0){
	for(int i=0;i<mlist.size();i++){
		Integer key = (Integer) mlist.get(i);
		MatchBean matchBean = (MatchBean) GameAction.matchCache.get(key);
		if(matchBean != null && matchBean.getState2() == 0 && matchBean.getState() < 2){
			%><%=weekGame[matchBean.getType()]%>(<%=DateUtil.formatTime(matchBean.getStarttime())%>-<%=DateUtil.formatTime(matchBean.getEndtime())%>)<%
		}
	}
}else{
	%>无<%
}%><a href="/Column.do?columnId=12502 ">[活动介绍]</a><br/>
<a href="topmore.jsp">+家族排行榜+</a><br/>
<a href="topgame.jsp?a=2">+家族游戏排行榜+</a><br/>
☆家族动态☆&#160;<a href="activity.jsp">更多</a><br/><%
mlist=action.service.selectFmActivities(0,5);
if(mlist!=null&&mlist.size()>0){
	for(int i=0;i<mlist.size();i++){
		FamilyActivityBean activity=(FamilyActivityBean)mlist.get(i);
		%><%=i+1%>.<%
		if(activity.getFm_state()==0){
			%><a href="/user/ViewUserInfo.do?userId=<%=activity.getUserid()%>"><%=StringUtil.toWml(activity.getUsername())%></a>建立<a href="myfamily.jsp?id=<%=activity.getFm_id()%>"><%=StringUtil.toWml(activity.getFm_name())%></a>家族<%
		}else if(activity.getFm_state()==1){
			%><a href="/user/ViewUserInfo.do?userId=<%=activity.getUserid()%>"><%=StringUtil.toWml(activity.getUsername())%></a>解散<%=StringUtil.toWml(activity.getFm_name())%>家族<%
		}else if(activity.getFm_state()==2){
			FamilyHomeBean fmhome=action.getFmByID(activity.getFm_id());
			%><a href="myfamily.jsp?id=<%=activity.getFm_id()%>"><%=StringUtil.toWml(fmhome==null?"":fmhome.getFm_name())%></a>家族获得<a href="<%=activity.getFm_url()%>"><%=StringUtil.toWml(activity.getMovement())%></a>的第一名<%
		}else if(activity.getFm_state()==3){
			FamilyHomeBean fmhome=action.getFmByID(activity.getFm_id());
			%><%=StringUtil.toWml(activity.getFm_name())%>家族更名为<a href="myfamily.jsp?id=<%=activity.getFm_id()%>"><%=StringUtil.toWml(fmhome.getFm_name())%></a>家族<%
		}else if(activity.getFm_state()==4){
			FamilyHomeBean fmhome=action.getFmByID(activity.getFm_id());
			%><%=activity.getMovement()%><a href="myfamily.jsp?id=<%=activity.getFm_id()%>"><%=StringUtil.toWml(fmhome==null?"":fmhome.getFm_name())%></a><%
		}%><br/><%
	}
}%>
家族名称:
<input name="familyname" maxlength="30" />
<anchor title="OK">搜索
  <go href="searchfamily.jsp" method="post">
    <postfield name="familyname" value="$(familyname)"/>
  </go>
</anchor><br/>
<a href="game/vs/term.jsp">+家族争霸赛历史+</a><br/>
<%
if(fmId<1){
%><a href="/jcforum/viewContent.jsp?contentId=2329943">申请建立家族</a><br/><%}%><a href="fmknows.jsp">家族须知</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>