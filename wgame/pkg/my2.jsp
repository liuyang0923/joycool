<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
UserBean loginUser = action.getLoginUser();
List list = action.getReceiveList(loginUser.getId());
PagingBean paging = new PagingBean(action,list.size(),10,"p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
==收到的礼包==<br/>
<%for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
PkgBean bean = action.getPkg((Integer)list.get(i));
PkgTypeBean type = (PkgTypeBean)action.getPkgType(bean.getType());
UserBean user = UserInfoUtil.getUser(bean.getUserId());
%><%=i+1%>.<a href="view.jsp?id=<%=bean.getId()%>"><%=type.getName()%></a>(<%=user.getNickNameWml()%>)
<%if(bean.getStatus()==2){%>(新)<%}%><br/>
<%}%>
<%=paging.shuzifenye("my2.jsp",false,"|",response)%>
<br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>