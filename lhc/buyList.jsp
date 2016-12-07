<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.lhc.LhcAction" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.action.lhc.LhcResultBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.action.lhc.LhcWagerRecordBean" %><%@ page import="net.joycool.wap.action.lhc.LhcWorld" %><%
response.setHeader("Cache-Control","no-cache");
LhcAction action = new LhcAction(request);
action.buyList();
String result =(String)request.getAttribute("result");
String url=("/lhc/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=LhcAction.LHC_NAME%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后返回六合彩首页)<br/>
<a href="/lhc/index.jsp">返回六合彩</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{ 
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List lhcWagerRecordList=(List)request.getAttribute("lhcWagerRecordList");
%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(lhcWagerRecordList.size()>0){
for(int i=0;i<lhcWagerRecordList.size();i++){
LhcWagerRecordBean lhcWagerRecord=(LhcWagerRecordBean)lhcWagerRecordList.get(i);
%>
<%=i+1%>
第<%=lhcWagerRecord.getLhcId()%>期
<%if(lhcWagerRecord.getType()<=7){%>
<%=LhcWorld.LHC_NAME_ARRAY[lhcWagerRecord.getType()][lhcWagerRecord.getNum()]%>
<%}else if(lhcWagerRecord.getType()==8){%>
平码
<%=lhcWagerRecord.getNum()%>
<%}else if(lhcWagerRecord.getType()==9){%>
特码
<%=lhcWagerRecord.getNum()%>
<%}%>
<%=lhcWagerRecord.getMoney()%>乐币
<%if(lhcWagerRecord.getMark()==0){%>
<a href="/lhc/exchange.jsp?lhcId=<%=lhcWagerRecord.getLhcId()%>">兑奖</a>
<%}else if(lhcWagerRecord.getMark()==1){%>已兑中奖<%}else{%>已兑未中奖<%}%>
<%--<%=lhcWagerRecord.getCreateDatetime().toString().substring(5,16)%>--%><br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|",response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}
}else{%>没有查询到您的下注记录!<br/><%}%>
<a href="/lhc/index.jsp">返回六合彩</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>