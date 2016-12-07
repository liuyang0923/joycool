<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*,java.util.List,net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%!
static int COUNT_PER_PAGE = 10;	// 一页10个好友
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="申请建立家族"><p align="left">
选择邀请人<br/><%
FamilyAction action=new FamilyAction(request,response);
int applyId=action.getParameterInt("applyId");
List list=action.getLoginUser().getFriendList();
PagingBean paging=new PagingBean(action, list.size(), COUNT_PER_PAGE, "p");
int startindex=0,endindex=0;
if(paging.getStartIndex()>list.size()){
startindex=0;
}else{
startindex=paging.getStartIndex();
}
if(startindex+paging.getCountPerPage()>list.size()){
endindex=list.size();
}else{
endindex=startindex+paging.getCountPerPage();
}
list=list.subList(startindex,endindex);
for(int i=0;i<list.size();i++){
String userId = (String) list.get(i);
UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
if(user==null)continue;
%><%=(i+1+COUNT_PER_PAGE*paging.getCurrentPageIndex())%>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>&#160;<a href="fmbuildapply.jsp?userId=<%=user.getId()%>&#38;applyId=<%=applyId%>">选择</a><br/><%
}%><%=paging.shuzifenye("friendlist.jsp?applyId="+applyId, true, "|", response)%>
<a href="buildfail.jsp?applyId=<%=applyId%>">返回我的家族创建</a><br/>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>