<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*"%><%@ page import="java.util.*"%><%!
static CacheService cacheService = CacheService.getInstance();%><%
	
	
	CastleBaseAction action = new CastleBaseAction(request);
	List list = action.getCastleService().getOasisList("cid="+action.getCastle().getId());
	int id = action.getParameterInt("del");
	CastleBean castle = action.getCastle();
	if(id>0&&!cacheService.containCommon(castle.getId(), 6)){
		
		OasisBean oasis = CastleUtil.getOasisById(id);
		if(oasis != null && oasis.getCid() == castle.getId()) {
			int time = 6 * 24 * 3600;	// 放弃绿洲，需要6个小时
			CommonThreadBean commonThreadBean = new CommonThreadBean(castle.getUid(), castle.getId(), 6, time, id);
			cacheService.addCacheCommon(commonThreadBean);
		}
	}
	int cancel = action.getParameterInt("cancel");
	if(cancel > 0){
		CommonThreadBean bean = cacheService.getCacheCommon(cancel);
		if(bean!=null&&bean.getUid()==castle.getUid()){
			cacheService.deleteCacheCommon(cancel);
		}
	}
	
	List cacheList = cacheService.getCacheCommonList(castle.getId(),6);
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="占领的绿洲"><p><%@include file="top.jsp"%><%
if(cacheList.size()!=0){
Iterator iterator = cacheList.iterator();
	while(iterator.hasNext()){
		CommonThreadBean cacheBean = (CommonThreadBean)iterator.next();
		OasisBean oasis = CastleUtil.getOasisById(cacheBean.getValue());
%>正在放弃绿洲(<%=oasis.getX()%>|<%=oasis.getY()%>)<a href="oasis.jsp?cancel=<%=cacheBean.getId()%>">取消</a><br/>
剩余时间<%=cacheBean.getTimeLeft()%>结束于<%=DateUtil.formatTime(cacheBean.getEndTime())%><br/><%
}
}%><%
		for(int i = 0; i < list.size(); i++) {
			OasisBean bean = (OasisBean)list.get(i);
			
%><%=i+1%>.<a href="search.jsp?pos=<%=bean.getId()%>">绿洲</a>(<%=bean.getX()%>|<%=bean.getY()%>)<%if(cacheList.size()==0){%><a href="oasis.jsp?del=<%=bean.getId()%>">放弃占领</a><%}%>:<%=ResNeed.oasisInfo2[bean.getType()]%><br/><%
			}%>
<a href="fun.jsp?t=37">返回开采所</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>