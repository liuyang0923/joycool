<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.job.HuntUserQuarryBean"%><%@ page import="java.util.HashMap"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.bean.job.HuntQuarryBean"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.hockshopSale(request);
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
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("priceError")){
TongBean tong=(TongBean)request.getAttribute("tong");
String url=("/tong/hockshop.jsp?tongId="+tong.getId());
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
String goodsType=(String)request.getAttribute("goodsTpye");
TongBean tong=(TongBean)request.getAttribute("tong");
String quarryPrice=(String)request.getAttribute("quarryPrice");
%>
<card title="当铺">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(goodsType.equals("1")){
HuntUserQuarryBean huntUserQuarry=(HuntUserQuarryBean)request.getAttribute("huntUserQuarry");
HashMap quarryMap=LoadResource.getQuarryMap();
HuntQuarryBean quarry=(HuntQuarryBean)quarryMap.get(new Integer(huntUserQuarry.getQuarryId()));%>
掌柜的：<%=quarry.getName()%>的原价是:<%=quarry.getPrice()%>回收价是<%=quarryPrice%>每个.
您有<%=huntUserQuarry.getQuarryCount()%>个.当前城市税率为:<%=tong.getRate()%>%<br/>
要卖<input name="goodsCount"  maxlength="10" value="200"/>个
<anchor title="确定">确定 
  <go href="hockshopResult.jsp" method="post">
    <postfield name="goodsCount" value="$goodsCount"/>
    <postfield name="goodsId" value="<%=huntUserQuarry.getQuarryId()%>"/>
    <postfield name="goodsType" value="<%=goodsType%>"/>
    <postfield name="tongId" value="<%=tong.getId() %>"/>
  </go>
</anchor>
<%}%><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回帮会</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>