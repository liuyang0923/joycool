<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.List,net.joycool.wap.bean.PagingBean,jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
int fmId = action.getFmId();
if(fmId<0){
response.sendRedirect("/fm/index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
List list=action.vsService.getChallengeList("fm_b="+fmId+" order by id desc");
if(list!=null&&list.size()>0){
PagingBean paging = new PagingBean(action,list.size(), 10, "p");
int startIndex=paging.getCurrentPageIndex()*10;
int endIndex=(paging.getCurrentPageIndex() + 1)*10>list.size()?list.size():(paging.getCurrentPageIndex() + 1) * 10;
list=list.subList(startIndex,endIndex);
int p=action.getParameterInt("p");
int index=0;
for(int i=0;i<list.size();i++){
Challenge bean=(Challenge)list.get(i);
if(bean.getCTime()+Challenge.dealTime>System.currentTimeMillis()&&bean.getStatus()==Challenge.undisposed){
index++;
%><%=index+10*p%>.<a href="challengedo.jsp?chall=<%=bean.getId()%>&amp;a=1"><%=bean.getFmANameWml()%></a>|<%=net.joycool.wap.util.DateUtil.formatTime(new java.util.Date(bean.getCTime()))%><br/><%
}
}if(index==10){%><%=paging.shuzifenye("challengeList.jsp", false, "|", response)%><%}
}%><a href="vsgame.jsp?id=<%=fmId%>">返回家族挑战</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>