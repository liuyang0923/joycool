<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static CacheService cacheService = CacheService.getInstance();%><%
	
	BuildingAction action = new BuildingAction(request);
	boolean flag = action.dupgrade();
	List cacheList = cacheService.getCacheCommonList(action.getCastle().getId(),2);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="拆毁建筑"><p><%@include file="top.jsp"%>
<%if(flag){%><%=request.getAttribute("msg")%><br/><%}%>
<%if(cacheList.size()>0){

Iterator iterator = cacheList.iterator();
	while(iterator.hasNext()){
		CommonThreadBean cacheBean = (CommonThreadBean)iterator.next();
		BuildingBean building = action.getCastleService().getBuildingBeanByPos(action.getCastle().getId(),cacheBean.getValue());
%><%=ResNeed.getTypeName(building.getBuildType())%>(等级<%=building.getGrade()%>)拆毁中<a href="dupgrade.jsp?cancel=<%=cacheBean.getId()%>" >取消</a><br/>
所需时间<%=cacheBean.getTimeLeft()%>结束于<%=DateUtil.formatTime(cacheBean.getEndTime())%><br/><%
}
} else{

%>选择以下建筑开始拆毁<br/>
<select name="pos"><%
	int[] buildAdvancePos = action.getCastleService().getAdvanceBuild(action.getCastle().getId());
	int[] buildPos = action.getCastleService().getBuildPos(action.getCastle().getId());
for(int i = 19; i <= 40; i ++){
if(buildPos[i] == 0) continue;
%><option value="<%=i%>"><%=i%>.<%=ResNeed.getTypeName(buildPos[i])%><%=buildAdvancePos[i]%></option><%
}%></select>
<anchor>拆毁<go href="dupgrade.jsp">
<postfield name="pos" value="$pos"/>
</go></anchor><br/>
<%}%>
<a href="fun.jsp?t=4">返回城堡</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>