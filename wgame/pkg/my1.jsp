<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
UserBean loginUser = action.getLoginUser();
List list = action.getPkgList(loginUser.getId());
PagingBean paging = new PagingBean(action,list.size(),10,"p");
long now = System.currentTimeMillis();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
==我的礼包==<br/>
<%for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
PkgBean bean = action.getPkg((Integer)list.get(i));
PkgTypeBean type = (PkgTypeBean)action.getPkgType(bean.getType());
%><%=i+1%>.<a href="send.jsp?id=<%=bean.getId()%>"><%=type.getName()%></a><%if(bean.getStatus()>=2){%>(已送出)<%}if(bean.getStatus()==1){%>(定时)<%}%><br/>
<%}%>
<%=paging.shuzifenye("my1.jsp",false,"|",response)%>
<br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>