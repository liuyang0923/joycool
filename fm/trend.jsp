<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyHomeBean fm=action.getFamily();
int fmId=action.getFmId();
int id=action.getParameterInt("id");
if(id==0||id!=fmId){
response.sendRedirect("index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
if(fm==null){%><wml><card title="系统提示"><p align="left"><%=BaseAction.getTop(request, response)%>家族已解散<br/>
<a href="index.jsp">返回家族首页</a><br/><%
}else{
%><wml><card title="成员动态"><p align="left"><%=BaseAction.getTop(request, response)%><%
List trendList=fm.getTrendList();
net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(action,trendList.size(), 10, "p");
int startIndex=paging.getCurrentPageIndex()*10;
int endIndex=(paging.getCurrentPageIndex() + 1)*10>trendList.size()?trendList.size():(paging.getCurrentPageIndex() + 1) * 10;
trendList=trendList.subList(startIndex,endIndex);
for(int i = 0;i < trendList.size(); i ++) {
%><%=FamilyAction.trendService.getTrendById(((Integer)trendList.get(i))).getContent(0,response)%><br/><%
}%><%=paging.shuzifenye("trend.jsp?id=" + id, true, "|", response)%>
<a href="myfamily.jsp?id=<%=id%>">返回<%=fm.getFm_nameWml()%>家族</a><br/><%
}%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>