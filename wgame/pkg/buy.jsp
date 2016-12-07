<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
action.buy();
PkgTypeBean bean = (PkgTypeBean)request.getAttribute("type");
PkgBean pkg = (PkgBean)request.getAttribute("pkg");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%if(pkg!=null){%>
<a href="write.jsp?id=<%=pkg.getId()%>">填写礼包内容&gt;&gt;</a><br/>
<%}else if(bean!=null){%><a href="buy.jsp?id=<%=bean.getId()%>">返回</a><br/><%}%>
<%}else{%>

<%=bean.getName()%><br/>
<img src="/rep/img/pkg/<%=bean.getId()%>.gif" alt=""/><br/>
<%=bean.getInfo()%><br/>
<%if(bean.getCount()==0){%>礼包内不能放置礼品<%}else{%>礼包内能放置<%=bean.getCount()%>件礼品<%}%><br/>
价格:<%=StringUtil.bigNumberFormat2(bean.getPrice())%>乐币<br/>
<a href="buy.jsp?c=1&amp;id=<%=bean.getId()%>">确认购买</a><br/>

<%}%><br/>
<a href="buys.jsp">返回礼包列表</a><br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>