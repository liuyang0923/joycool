<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
int id = action.getParameterInt("id");
UserBean loginUser = action.getLoginUser();
List userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
PagingBean paging = new PagingBean(action, userFriends.size(),15,"p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
把礼包发送给好友:<br/>
<%
int end = paging.getEndIndex();
for(int i=paging.getStartIndex();i<end;i++){
String userIdKey=(String)userFriends.get(i);
UserBean user = UserInfoUtil.getUser(StringUtil.toInt(userIdKey));
if(user==null) continue;
%><a href="send3.jsp?id=<%=id%>&amp;to=<%=user.getId()%>"><%=user.getNickNameWml()%></a><br/>
<%}%>
<%=paging.shuzifenye("send2.jsp?id="+id,true,"|",response)%>
<input name="to" value=""/>
<anchor title="确定">发送给此ID玩家
<go href="send3.jsp?id=<%=id%>">
    <postfield name="to" value="$to"/>
</go></anchor><br/>
<a href="send.jsp?id=<%=id%>">返回</a><br/>
<br/>
<a href="my1.jsp">返回我的礼包列表</a><br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>