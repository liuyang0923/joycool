<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.job.HuntUserQuarryBean"%><%@ page import="java.util.HashMap"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.bean.tong.TongHockshopBean"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
session.setAttribute("hockshopCheck","");
TongAction action = new TongAction(request);
action.empolder(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
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
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
TongHockshopBean hockShop = net.joycool.wap.cache.util.TongCacheUtil.getTongHockshop(tong.getId());
%>
<card title="帮会当铺" ontimer="<%=response.encodeURL("/tong/hockshops.jsp?tongId="+tong.getId())%>">
<timer value="12"/>
<p align="left">
<a href="/tong/hockshops.jsp?tongId=<%=tong.getId()%>">开发当铺</a><br/>
掌柜的嘻笑道：这位客官，有什么需要典当的？（本店开发度<%=hockShop.getDevelop()%>）<br/>
<a href="/tong/tongCityHockShopRecord.jsp?tongId=<%=tong.getId()%>">当铺开发记录</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
</p>
</card>
<%}%>
</wml>