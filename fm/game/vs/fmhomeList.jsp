<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.*"%><%@ page import="jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
int fmId=action.getFmId();
List list=action.service.selectFamilyIdList("id!="+fmId);
%>请选择要挑战的家族:<br/><%
if(list!=null&&list.size()>0){
net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(action,list.size(), 10, "p");
int startIndex=paging.getCurrentPageIndex()*10;
int endIndex=(paging.getCurrentPageIndex() + 1)*10>list.size()?list.size():(paging.getCurrentPageIndex() + 1) * 10;
list=list.subList(startIndex,endIndex);
for(int i=0;i<list.size();i++){
FamilyHomeBean bean=null;
Integer id=(Integer)list.get(i);
bean=action.getFmByID(id.intValue());
%><a href="dealchallenge.jsp?fmId=<%=bean.getId()%>"><%=bean.getFm_nameWml()%></a><br/><%
}%><%=paging.shuzifenye("fmhomeList.jsp", false, "|", response)%><%
}%>
<a href="vsgame.jsp?id=<%=fmId%>">返回家族挑战</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>