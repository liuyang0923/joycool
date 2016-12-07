<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
action.index();
HashMap map = action.getPkgTypeMap();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<img src="img/logo.gif" alt="logo"/><br/>
<%=BaseAction.getTop(request, response)%>
<a href="my2.jsp">收到的礼包</a>|<a href="my1.jsp">购买的礼包</a><br/>
==推荐礼包==<br/>
<%{PkgTypeBean bean = (PkgTypeBean)map.get(new Integer(81));%>【<%=bean.getName()%>】(<%=StringUtil.bigNumberFormat2(bean.getPrice())%>)<br/>
--<a href="buy.jsp?id=<%=bean.getId()%>">查看礼包详情</a><br/><%}%>
<%{PkgTypeBean bean = (PkgTypeBean)map.get(new Integer(82));%>【<%=bean.getName()%>】(<%=StringUtil.bigNumberFormat2(bean.getPrice())%>)<br/>
--<a href="buy.jsp?id=<%=bean.getId()%>">查看礼包详情</a><br/><%}%>
<%{PkgTypeBean bean = (PkgTypeBean)map.get(new Integer(60));%>【<%=bean.getName()%>】(<%=StringUtil.bigNumberFormat2(bean.getPrice())%>)<br/>
--<a href="buy.jsp?id=<%=bean.getId()%>">查看礼包详情</a><br/><%}%>
<a href="buys.jsp">更多礼包&gt;&gt;</a><br/>
!!<a href="help.jsp">礼品店介绍</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>