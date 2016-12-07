<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%
List cacheList = cacheService.getCacheCommonList(action.getCastle().getId(),1);
%>小型活动(500文明度)<%if(cacheList.size()!=0){%>举行中<%}else{%><a href="cele.jsp?type=1">举办</a><%}%><br/><%ResTBean res = ResNeed.celeRes;%>
木<%=res.getWood()%>|石<%=res.getStone()%>|铁<%=res.getFe()%>|粮<%=res.getGrain()%><br/>
时间:<%=DateUtil.formatTimeInterval2((int)(res.getTime()*ResNeed.getGradeTime(building.getGrade())))%><br/>
<%if(building.getGrade()>=10){%>
大型活动(2000文明度)<%if(cacheList.size()!=0){%>举行中<%}else{%><a href="cele.jsp?type=2">举办</a><%}%><br/><%res = ResNeed.cele2Res;%>
木<%=res.getWood()%>|石<%=res.getStone()%>|铁<%=res.getFe()%>|粮<%=res.getGrain()%><br/>
时间:<%=DateUtil.formatTimeInterval2((int)(res.getTime()*ResNeed.getGradeTime(building.getGrade())))%><br/>
<%}else{%>
大型活动(2000文明度)举办<br/>需要广场等级10<br/>
<%} if(cacheList.size()>0){%>--------<br/><%
for(int i=0;i<cacheList.size();i++){
CommonThreadBean cacheBean = (CommonThreadBean)cacheList.get(i);
%><%if(cacheBean.getValue()<1000){%>小型活动<%}else{%>大型活动<%}%>举行中<br/>
剩余<%=cacheBean.getTimeLeft()%>结束于<%=DateUtil.formatTime(cacheBean.getEndTime())%><br/><%}}%>