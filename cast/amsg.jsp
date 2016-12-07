<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="java.util.*,net.joycool.wap.spec.castle.*,net.joycool.wap.bean.*,net.joycool.wap.framework.*,net.joycool.wap.util.DateUtil"%><%@ page import="java.util.List"%><%!
static CacheService cacheService = CacheService.getInstance();
String[] attackType = {"接近的攻击","接近的支援","发出的攻击","发出的支援"};
%><%
	CastleAction action = new CastleAction(request,response);
	if(action.getCastle()==null){
		response.sendRedirect("s.jsp");
		return;
	}
	int uid = action.getUserBean().getId();
//	int total = action.getCastleService().getCountMessageByUid(uid);
	Object[] counttime = action.getCastleService().getCacheAttackCount(action.getCastle().getId());
	int[] attackCount = (int[])counttime[0];
	long[] attackTime = (long[])counttime[1];
	action.getCastleUser().setUnread(0);
	action.curPage = 4;
	action.setAttribute2("casSwi","amsg.jsp");
	List list = action.getCastleService().getCastleMessageByUid(action.getUserBean().getId(), 0, 6);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="信息" ontimer="<%=action.encodeURL("amsg.jsp")%>"><timer value="300"/><p>
<%@include file="top.jsp"%><a href="res.jsp">资源信息</a>|<a href="army.jsp">军队</a><%if(castleUser.getTong()!=0){%>|<a href="tong/tong.jsp">联盟</a><%
	SimpleChatLog sc = SimpleChatLog.getChatLog("ca"+castleUser.getTong());	// 显示联盟聊天数量
	int count = sc.getUnreadTotal(action.getAttribute2("castle"));
	if(count>0){%>-<a href="tong/tongChat.jsp"><%=count%></a><%}%><%}%><br/>
<%for(int i=0;i<attackCount.length;i++) if(attackCount[i] != 0) {
%><a href="fun.jsp?t=22"><%=attackType[i]%></a>(<%=attackCount[i]%>)剩余<%=DateUtil.formatTimeInterval2(attackTime[i])%><br/><%
}
List cacheList = cacheService.getCacheBuildingByCid(action.getCastle().getId());
if(cacheList == null || cacheList.size() == 0) {
} else {
	Iterator iterator = cacheList.iterator();
	while(iterator.hasNext()){
		BuildingThreadBean cacheBuildingBean = (BuildingThreadBean)iterator.next();
%><%=ResNeed.getTypeName(cacheBuildingBean.getType())%>(等级<%=cacheBuildingBean.getGrade()%>)<%if(cacheBuildingBean.getStartTime()<System.currentTimeMillis()){%>建造中<%}else{%>队列中<%}%><a href="cancel.jsp?id=<%=cacheBuildingBean.getId()%>">取消建造</a><br/>
需时<%=cacheBuildingBean.getTimeLeft()%>结束于<%=DateUtil.formatTime(cacheBuildingBean.getEndTime())%><br/>
<%}}%>【历史记录】<%if(list.size()>5){%><a href="his.jsp">更多</a><%}%><br/><%
	for(int i = 0; i < list.size()&&i<5; i++) {
				CastleMessage message = (CastleMessage)list.get(i);
%><%=DateUtil.converDateToBefore(message.getTime())%>:<%if(!message.hasDetail()){%><%=message.getContent()%><%}else{
%><a href="report.jsp?id=<%=message.getId()%>"><%=message.getContent()%></a><%}%><br/>
<%}%>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>