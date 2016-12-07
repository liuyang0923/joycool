<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.util.Constants"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongFund(request);
String result =(String)request.getAttribute("result");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>（3秒钟跳转到城帮首页）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong = (TongBean)request.getAttribute("tong");
TongUserBean tongUser = (TongUserBean)request.getAttribute("tongUser");
%>
<card title="<%=StringUtil.toWml(tong.getTitle())%>" >
<p align="left">
<img src="../img/tong/fund.gif" alt="帮会基金"/><br/>
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(tong.getTitle())%>帮基金是为发展帮会而建。你只要捐献一点点乐币，就可以为帮会发展贡献力量哦。<br/>
基金现有<%=tong.getFund()%>乐币<br/>
历史累计:<%=tong.getFundTotal()%>乐币<br/>
<%=request.getAttribute("tip") %><br/>
要捐款:(单位:份)<br/>
<input name="count"  maxlength="9" value="100"/><br/>
<anchor title="确定">确定
  <go href="tongFundResult.jsp" method="post">
    <postfield name="count" value="$count"/>
    <postfield name="tongId" value="<%=tong.getId()%>"/>
  </go>
</anchor><br/>
(注:一份<%= Constants.TONG_FUND_MONEY %>乐币,一次最少捐助1份,最多捐助10000份)<br/>
<a href="/tong/tongFundHelp.jsp?tongId=<%=tong.getId()%>">管理规则</a>
<a href="/tong/tongFundUse.jsp?tongId=<%=tong.getId()%>">使用明细</a><br/>
<a href="/tong/tongFundTop.jsp?tongId=<%=tong.getId()%>">帮会基金贡献榜</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>