<%@include file="../checkMobile.jsp"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.job.HuntUserQuarryBean"%><%@ page import="java.util.HashMap"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.bean.job.HuntQuarryBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.bean.tong.TongHockshopBean"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><% response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.hockshop(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("tongList.jsp");
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
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector quarryList=(Vector)request.getAttribute("quarryList");
TongHockshopBean hockShop=(TongHockshopBean)request.getAttribute("hockShop");
TongBean tong=(TongBean)request.getAttribute("tong");
int rand = net.joycool.wap.util.RandomUtil.seqInt(100);
session.setAttribute("hockshopCheck", new Integer(rand));
Integer sid = (Integer)session.getAttribute("hockshopCheck2");
if (sid != null && sid.intValue() != action.getParameterIntS("s")) // 防止刷
	rand=0;
%>
<card title="帮会当铺">
<p align="left">
<%=loginUser.showImg("/img/tong/hockshop.gif")%><%=BaseAction.getTop(request, response)%>
<a href="tongEmpolder.jsp?tongId=<%=tong.getId()%>&amp;s=<%=rand%>">开发当铺</a><br/>
掌柜的嘻笑道：这位客官，有什么需要典当的？（本店开发度<%=hockShop.getDevelop()>10000000?10000000:hockShop.getDevelop()%>）<br/>
<%
if(quarryList!=null){
HuntUserQuarryBean userQuarry=null;
HuntQuarryBean quarry=null;
for(int i = 0; i < quarryList.size(); i ++){
	userQuarry = (HuntUserQuarryBean) quarryList.get(i);
	HashMap quarryMap=LoadResource.getQuarryMap();
	quarry=(HuntQuarryBean)quarryMap.get(new Integer(userQuarry.getQuarryId()));
	if(quarry==null)continue;
%>
<%=i+1%>.<%=StringUtil.toWml(quarry.getName())%>
<%=userQuarry.getQuarryCount()%>
<anchor title="卖">卖
  <go href="hockshopSale.jsp" method="post">
    <postfield name="goodsId" value="<%=userQuarry.getQuarryId()%>"/>
    <postfield name="goodsTpye" value="<%=userQuarry.getGoodsTpye()%>"/>
    <postfield name="tongId" value="<%=tong.getId()%>"/>
  </go>
</anchor><br/>
<%
}}else{%>您没有可以典当的物品<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%><br/>
<a href="tongCityHockShopRecord.jsp?tongId=<%=tong.getId()%>">当铺开发记录</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
</p>
</card>
<%}%>
</wml>