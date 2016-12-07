<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page  import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongFundHelp(request);
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");%>
<card title="乐酷" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转城帮列表)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong = (TongBean)request.getAttribute("tong");
%>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
帮会基金规则：<br/>
帮会会员捐款，以5份为单位，增加1贡献度。非帮会会员捐款不加贡献度。<br/>
帮会基金只有帮主和管理员有权调用。<br/>
要捐款:(单位:份)<br/>
<input name="count"  maxlength="9" value="100"/><br/>
<anchor title="确定">确定
  <go href="tongFundResult.jsp" method="post">
    <postfield name="count" value="$count"/>
    <postfield name="tongId" value="<%=tong.getId()%>"/>
  </go>
</anchor><br/>
(注:一份<%= Constants.TONG_FUND_MONEY %>乐币,一次最少捐助1份,最多捐助40万份)<br/>
<a href="/tong/tongFundHelp.jsp?tongId=<%=tong.getId()%>">管理规则</a>
<a href="/tong/tongFundUse.jsp?tongId=<%=tong.getId()%>">使用明细</a><br/>
<a href="/tong/tongFundTop.jsp?tongId=<%=tong.getId()%>">帮会贡献度排行榜</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>