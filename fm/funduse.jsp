<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*,java.util.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null){
response.sendRedirect("index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="基金使用记录"><p align="left"><%=BaseAction.getTop(request, response)%><%
int c=action.service.selectIntResult("select count(id) from fm_fund_history where fm_id="+fmLoginUser.getFm_id());
net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(action, c, 10, "p");
List list =action.service.selectFundUseList(fmLoginUser.getFm_id(), paging.getStartIndex(), paging.getCountPerPage());
if(list!=null&&list.size()>0){
for(int i=0;i<list.size();i++){
FamilyFundBean fund=(FamilyFundBean)list.get(i);
%><%=i+1%>.<%=FamilyAction.sd.format(fund.getEvent_time())%>|<%
if(fund.getFm_State()==0){
%><a href="/user/ViewUserInfo.do?userId=<%=fund.getUserid()%>"><%=net.joycool.wap.util.StringUtil.toWml(fund.getUsername())%></a><%=net.joycool.wap.util.StringUtil.toWml(fund.getEvent())%><%
}else{
%><%=net.joycool.wap.util.StringUtil.toWml(fund.getEvent())%><%}%><br/>
<%}%>
<%=paging.shuzifenye("funduse.jsp", false, "|", response)%>
<%}else{%>
暂无家族基金使用记录!<br/>
<%}%>
<a href="fundmgt.jsp">返回家族基金</a><br/>
<a href="myfamily.jsp">返回我的家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>