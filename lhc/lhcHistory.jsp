<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.lhc.LhcAction" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.action.lhc.LhcResultBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.action.lhc.LhcWagerRecordBean" %><%@ page import="net.joycool.wap.action.lhc.LhcWorld" %><%
response.setHeader("Cache-Control","no-cache");
LhcAction action = new LhcAction(request);
action.lhcHistory();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List lhcResultList=(List)request.getAttribute("lhcResultList");
%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(lhcResultList.size()>0){
for(int i=0;i<lhcResultList.size();i++){
LhcResultBean lhcResult=(LhcResultBean)lhcResultList.get(i);
%>
第<%=lhcResult.getId()%>期中奖号码：<br/>
<%=lhcResult.getNum1()%>
<%=lhcResult.getNum2()%>
<%=lhcResult.getNum3()%>
<%=lhcResult.getNum4()%>
<%=lhcResult.getNum5()%>
<%=lhcResult.getNum6()%>
(<%=lhcResult.getNum7()%>)<br/>
中奖名称：<br/>
<%=action.countName(lhcResult.getId())%><br/>
=============<br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|",response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}
}else{%>没有查询到开奖记录!<br/><%}%>
<a href="/lhc/index.jsp">返回六合彩</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>