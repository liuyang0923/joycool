<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
TorchAction action = new TorchAction(request);
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="奥运火炬手">
<p align="left"><%!static String torchUserKey = "torchUser";%>
<%=BaseAction.getTop(request, response)%>
==上期火炬手排行榜==<br/>
<%
List list = null;
synchronized(CacheManage.stuff) {
list = (List)CacheManage.stuff.get(torchUserKey);
if(list == null){
	list = SqlUtil.getObjectsList("select user_id,point from torch_user order by point desc limit 100",2);
}
CacheManage.stuff.put(torchUserKey, list);
}
if(list!=null){
PagingBean paging = new PagingBean(action,list.size(),20,"p");
int end = paging.getEndIndex();
for(int i = paging.getStartIndex();i < end;i++){
Object[] res = (Object[])list.get(i);
Number u = (Number)res[0];
Number p = (Number)res[1];
UserBean user = UserInfoUtil.getUser(u.intValue());
%>
<%=i+1%>.<%if(loginUser!=null&&loginUser.getId()==u.intValue()){
%><%=user.getNickNameWml()%><%}else{
%><a href="/user/ViewUserInfo.do?userId=<%=u.intValue()%>"><%=user.getNickNameWml()%></a><%}%>
(<%=StringUtil.numberFormat(p.floatValue())%>)<br/>
<%}%>
<%=paging.shuzifenye("top.jsp",false,"|",response)%>
<%}%>
<a href="index.jsp">返回奥运火炬首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>