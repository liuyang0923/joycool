<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*,net.joycool.wap.spec.buyfriends.BeanTrend"%><%
FamilyAction action=new FamilyAction(request,response);
int fmId=action.getFmId();
FamilyUserBean fmUser = action.getFmUser();
int id=action.getParameterInt("id");
if(id==0){
	if(fmId>0){
		id=fmId;
	}else{
		response.sendRedirect("index.jsp");
		return;
	}
}
FamilyHomeBean fm=action.getFmByID(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
if(fm==null){%><wml><card title="系统提示"><p align="left"><%=BaseAction.getTop(request, response)%>该家族不存在<br/>
<a href="index.jsp">返回家族首页</a><br/><%
}else{
%><wml><card title="家族"><p align="left"><%=BaseAction.getTop(request, response)%>
<%=fm.getFm_nameWml()%>家族(<%=jc.family.Constants.FM_LEVEL_NAME[fm.getFm_level()]%>)<br/><%if(!fm.getLogoUrl().equals("")){
%><img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%>/<%=fm.getLogoUrlForImg()%>"/><br/><%

}%>＝家族公告＝<br/><%
action.uvStatistics(id);
if(fm.getBulletin()!=null&&!fm.getBulletin().equals("")){
%><%=net.joycool.wap.util.StringUtil.toWml(fm.getBulletin())%><br/><%
}else{
%>该家族暂无公告!<br/><%
}
List medalList = fm.getMedalList();
if(medalList.size()>0){
%>{<%for(int i = 0;i < medalList.size();i++){
FamilyMedalBean medal = (FamilyMedalBean)medalList.get(i);
%><img src="/rep/family/medal/<%=medal.getImg()%>" alt="<%=medal.getName()%>"/><%
}%>}<a href="medal.jsp?id=<%=id%>">&gt;&gt;</a><br/><%}%>

<%if(fmId==id){
%><a href="chat/chat.jsp?fid=<%=id%>">家族聊天</a>|<a href="game/fmgame.jsp">家族活动</a><br/><%
}else{%><%if(fmId>19999&&(fm.getChatOpen()==2||fm.getChatOpen()==1&&fm.isAlly(fmId))){%><a href="chat/chat.jsp?fid=<%=id%>">家族聊天</a><%}else{%>家族聊天<%}%>|家族活动<br/><%}%>
<%=fm.getForumId()==0?"家族论坛":"<a href=\"/jcforum/forum.jsp?forumId="+fm.getForumId()+"\">家族论坛</a>"%>|<a href="game/vs/vsgame.jsp?id=<%=id%>">家族挑战</a>|<a href="pho/phocat.jsp?fid=<%=id%>">相册</a><br/>
<%
if(fmId<1){
%><a href="applyjoin.jsp?id=<%=fm.getId()%>">申请加入家族</a><br/><%
}%>＝家人动态＝<%
List trendList=fm.getTrendList();
int size=trendList.size();
if(size > 3&&fmId==id){%><a href="trend.jsp?id=<%=id%>">全部</a><%}%><br/><%
if(size>3){size = 3;}
for(int i = 0;i < size; i ++) {
	BeanTrend trend = FamilyAction.trendService.getTrendById(((Integer)trendList.get(i)));
	if(trend==null) continue;
%><%=trend.getContent(0,response)%><br/><%
}
%>
<a href="manage/familydes.jsp?id=<%=fm.getId()%>&amp;cmd=see">家族简介</a>|<a href="familyinfo.jsp?id=<%=fm.getId()%>">家族资料</a><br/>
<a href="history.jsp?id=<%=fm.getId()%>">家族历史</a>|<a href="position.jsp?id=<%=fm.getId()%>">家族官员</a><br/><%
if(fm.isEatery()){
	%><a href="game/yard/index.jsp?id=<%=fm.getId()%>">家族餐厅</a><%
}else{%>家族餐厅<%}%>|<a href="ally.jsp?id=<%=fm.getId()%>">友联家族</a><br/>
族长:<a href="/user/ViewUserInfo.do?userId=<%=fm.getLeader_id()%>"><%=fm.getLeaderNickNameWml()%></a><br/>
<%List list=action.getOnlineFmUserIdList(id);
if(list!=null){%>
家族成员:(<%=list.size()%>/<%=fm.getFm_member_num()%>)<a href="memberlist.jsp?id=<%=id%>">更多</a><br/><%
for(int i=0;i<list.size()&&i<4;i++){
Integer userid=(Integer)list.get(i);
FamilyUserBean bean=action.getFmUserByID(userid.intValue());
%><a href="/user/ViewUserInfo.do?userId=<%=bean.getId()%>"><%=bean.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId(bean.getId())%>)<%=net.joycool.wap.util.StringUtil.toWml(bean.getFm_name())%><br/>
<%}
}%>
<%if(fmId>19999&&fmId==id){
%><a href="manage/management.jsp">管理家族</a>|<a href="fundmgt.jsp">基金</a>|<a href="exploituser.jsp">家人排名</a>|<a href="set.jsp">其他</a><br/><%
}%>
<a href="index.jsp">返回家族首页</a><br/>
<%
}%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>