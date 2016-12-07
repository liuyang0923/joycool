<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
action.send();
PkgBean pkg = (PkgBean)request.getAttribute("pkg");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

UserBean to = (UserBean)request.getAttribute("to");
PkgTypeBean type = (PkgTypeBean)action.getPkgType(pkg.getType());
%>
<%=type.getName()%><br/>
<%=StringUtil.toWml(pkg.getTitle())%><br/>
发送给:<a href="/user/ViewUserInfo.do?userId=<%=to.getId()%>"><%=to.getNickNameWml()%></a>(<%=to.getId()%>)<br/>
<%if(pkg.getSendTime()>System.currentTimeMillis()){%>
<a href="send3.jsp?c=1&amp;id=<%=pkg.getId()%>&amp;to=<%=to.getId()%>">确认并定时于<%=DateUtil.sformatTime(pkg.getSendTime())%>发送礼包</a><br/>
<br/>
&gt;&gt;<a href="set.jsp?id=<%=pkg.getId()%>">重新设定发送时间</a>
<%}else{%>
<a href="send3.jsp?c=1&amp;id=<%=pkg.getId()%>&amp;to=<%=to.getId()%>">确认并立即发送礼包</a><br/>
<%}%>
<%}%><br/>
<%if(pkg!=null){%><a href="send2.jsp?id=<%=pkg.getId()%>">返回</a><br/><%}%>
<a href="my1.jsp">返回我的礼包列表</a><br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>