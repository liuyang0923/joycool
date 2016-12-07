<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
List list = action.getPkgTypeList();

PagingBean paging = new PagingBean(action, list.size(),10,"p");
int endIndex = paging.getEndIndex();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%for(int i=paging.getStartIndex();i<endIndex;i++){
PkgTypeBean bean = (PkgTypeBean)list.get(i);
%><a href="buy.jsp?id=<%=bean.getId()%>"><%=bean.getName()%></a>(<%=StringUtil.bigNumberFormat2(bean.getPrice())%>)<br/>
<%}%>
<%=paging.shuzifenye("buys.jsp", false, "|", response)%>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>