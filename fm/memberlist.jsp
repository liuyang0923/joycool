<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%@ page import="java.util.List" %><%
FamilyAction action=new FamilyAction(request,response);
String cmd=action.getParameterString("cmd");
if(cmd==null)cmd="online";
int id=action.getParameterInt("id");
if(id==0){
response.sendRedirect("/fm/index.jsp");return;
}
FamilyHomeBean fmhome=action.getFmByID(id);
if(fmhome==null){
response.sendRedirect("/fm/index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="成员列表"><p align="left">
=成员列表=<br/><%
List list=null;
if(cmd.equals("all")){
list=action.service.selectFmOnLineUserList(id);
%>按全部|<a href="memberlist.jsp?id=<%=id%>&amp;cmd=online">在线</a><br/><%
}else{
list=action.getOnlineFmUserIdList(id);
%>按<a href="memberlist.jsp?id=<%=id%>&amp;cmd=all">全部</a>|在线<br/><%
}if(list!=null&&list.size()>0){
net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(action,list.size(), 10, "p");
int startIndex=paging.getCurrentPageIndex()*10;
int endIndex=(paging.getCurrentPageIndex() + 1)*10>list.size()?list.size():(paging.getCurrentPageIndex() + 1) * 10;
list=list.subList(startIndex,endIndex);
for(int i=0;i<list.size();i++){
FamilyUserBean bean=null;
if(list.get(i) instanceof Integer){
Integer userid=(Integer)list.get(i);
bean=action.getFmUserByID(userid.intValue());
}else{
bean=(FamilyUserBean)list.get(i);
}
%>
<a href="/user/ViewUserInfo.do?userId=<%=bean.getId() %>"><%=bean.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId(bean.getId())%>)<%=StringUtil.toWml(bean.getFm_name())%><br/><%
}%><%=paging.shuzifenye("memberlist.jsp?id=" + id + "&#38;cmd=" + cmd, true, "|", response)%><%
}%><a href="/fm/myfamily.jsp?id=<%=id%>">返回<%=fmhome.getFm_nameWml()%>家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>