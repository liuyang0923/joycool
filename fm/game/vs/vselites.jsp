<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
FamilyUserBean fmUser = action.getFmUser();
boolean gameAdmin = fmUser.isflagGame();
int cmd=action.getParameterInt("cmd");
if(cmd==1){
action.dealVsUser();
}%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战精英"><p align="left"><%=BaseAction.getTop(request, response)%>
<a href="addelites.jsp">添加成员</a><br/><%
java.util.List list=action.service.selectUserIdList(fmUser.getFm_id()," and fm_state&" + FamilyUserBean.VS_GAME);
if(list!=null&&list.size()>0){
net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(action,list.size(), 10, "p");
int startIndex=paging.getCurrentPageIndex()*10;
int endIndex=(paging.getCurrentPageIndex() + 1)*10>list.size()?list.size():(paging.getCurrentPageIndex() + 1) * 10;
list=list.subList(startIndex,endIndex);
for(int i=0;i<list.size();i++){
FamilyUserBean bean=null;
Integer userid=(Integer)list.get(i);
bean=action.getFmUserByID(userid.intValue());
%>
<a href="/fm/fmuserinfo.jsp?userid=<%=bean.getId()%>"><%=bean.getNickNameWml()%></a><%
if(gameAdmin){
%>|<a href="vselites.jsp?cmd=1&amp;uid=<%=bean.getId()%>&amp;p=<%=action.getParameterInt("p")%>">踢除</a><%
}%><br/><%
}%><%=paging.shuzifenye("vselites.jsp", false, "|", response)%><%
}%>
&lt;<a href="vsgame.jsp?id=<%=fmUser.getFm_id()%>">家族挑战</a>&lt;<a href="/fm/myfamily.jsp?id=<%=fmUser.getFmId()%>">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>