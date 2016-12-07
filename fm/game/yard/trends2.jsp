<%@ page language="java" import="jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
int id=action.getParameterInt("id");
int fmId=action.getFmId();
if(id==0){
	response.sendRedirect("/fm/index.jsp");
	return;
}
YardBean yard=action.getYardByID(id);
int size=yard.getProductLog().size();
PagingBean paging = new PagingBean(action,size, 10, "p");
int startIndex=paging.getCurrentPageIndex()*10;
int endIndex=startIndex+paging.getCountPerPage()>size?size:startIndex+paging.getCountPerPage();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="基地动态"><p align="left"><%=BaseAction.getTop(request, response)%>
<%=yard.getProductLog().getLogString(startIndex,endIndex)%>
<%=paging.shuzifenye("trends2.jsp?id=" + id,true,"|",response)%>
<a href="index.jsp?id=<%=id%>">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>