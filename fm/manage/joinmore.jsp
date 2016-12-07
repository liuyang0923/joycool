<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
int joinConut=action.service.selectfamilyJoinCount(fmLoginUser.getFm_id());
net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(action, joinConut, 10, "p");
List list=action.service.selectfamilyJoin(fmLoginUser.getFm_id(), paging.getStartIndex(), paging.getCountPerPage());
int p =action.getParameterInt("p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="请求管理"><p align="left">
当前<%=joinConut%>个待处申请<br/><%
for(int i=0;i<list.size();i++){
	FamilyNewManBean fmNew=(FamilyNewManBean)list.get(i);
	%><a href="/user/ViewUserInfo.do?userId=<%=fmNew.getUserid()%>"><%=1+i+p*10%>.<%=StringUtil.toWml(fmNew.getUsername())%></a>|<a href="rerecruit.jsp?id=<%=fmNew.getId()%>&amp;cmd=y" >接受</a>|<a href="rerecruit.jsp?id=<%=fmNew.getId()%>&amp;cmd=n" >拒绝</a><br/><%
}
%><%=paging.shuzifenye("joinmore.jsp", false, "|", response)%>
<a href="recruit.jsp">返回招募新人</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>