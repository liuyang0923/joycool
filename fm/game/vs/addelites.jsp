<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="添加成员"><p align="left"><%=BaseAction.getTop(request, response)%>
<a href="addelites.jsp"></a>
按ID:<input name="userid" maxlength="8" format="*N"/>
<anchor title="添加">添加
<go href="dealelites.jsp" method="post">
<postfield name="uid" value="$(userid)"/>
</go></anchor><br/><%
java.util.List list=action.getFmUserIdList(action.getFmId());
if(list!=null&&list.size()>0){
net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(action,list.size(), 10, "p");
int startIndex=paging.getCurrentPageIndex()*10;
int endIndex=(paging.getCurrentPageIndex() + 1)*10>list.size()?list.size():(paging.getCurrentPageIndex() + 1) * 10;
list=list.subList(startIndex,endIndex);
for(int i=0;i<list.size();i++){
FamilyUserBean bean=null;
Integer userid=(Integer)list.get(i);
bean=action.getFmUserByID(userid.intValue());
%><a href="/user/ViewUserInfo.do?userId=<%=bean.getId()%>"><%=bean.getNickNameWml()%></a>|<a href="dealelites.jsp?uid=<%=bean.getId()%>"><%=bean.isVsGame(bean.getFm_state())?"踢除":"添加"%></a><br/><%
}%><%=paging.shuzifenye("addelites.jsp", false, "|", response)%><%
}%>
<a href="vselites.jsp">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>