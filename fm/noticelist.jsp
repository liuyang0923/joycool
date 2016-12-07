<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%@page import="java.util.List"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmUser =action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族通知"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if(fmUser==null){%>
您未加入该家族,不能查看通知!<br/>
<a href="index.jsp">返回家族首页</a><br/>
<%}else{
int c = action.service.selectIntResult("select count(id) from fm_notice where fm_id=" + fmUser.getFm_id());
net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(action, c, 10, "p");
List list =action.service.selectNotice(fmUser.getFm_id(),paging.getStartIndex(),paging.getCountPerPage());
if(list!=null&&list.size()>0){
for(int i=0;i<list.size();i++){
FamilyNoticeBean bean=(FamilyNoticeBean)list.get(i);%>
<%=FamilyAction.sd2.format(bean.getNoticetime())%><br/><a href="/user/ViewUserInfo.do?userId=<%=bean.getUserid()%>"><%=net.joycool.wap.util.StringUtil.toWml(bean.getUsername())%></a>:&#160;<%=net.joycool.wap.util.StringUtil.toWml(bean.getContent())%><br/><%
}%>
<%=paging.shuzifenye("noticelist.jsp", false, "|", response)%>
<%}else{
%>暂无通知记录!<br/>
<%}%>
<a href="myfamily.jsp">返回我的家族</a><br/>
<%}%>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>