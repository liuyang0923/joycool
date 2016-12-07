<%@include file="../checkMobile.jsp"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.Tong2Action"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.job.HuntUserQuarryBean"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.bean.tong.TongHockshopBean"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%!
public static TinyGame[] tinyGames = {new TinyGame1(2, 3), new TinyGame2(3, 3), new TinyGame3(3)};
%><% response.setHeader("Cache-Control","no-cache");
Tong2Action action = new Tong2Action(request);
action.shop(request);
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
TongBean tong=(TongBean)request.getAttribute("tong");
TongCacheUtil.updateTongDepot(tong.getId(),1);
%>
<card title="帮会仓库" ontimer="<%=response.encodeURL("depotsR.jsp?tongId="+tong.getId())%>">
<timer value="300"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="depotE.jsp?tongId=<%=tong.getId()%>">开发仓库</a><br/>
本周开发度:<%=tong.getDepotWeek()%><br/>
历史开发度:<%=tong.getDepot()%><br/>
帮会仓库测试阶段,暂时不提供其他功能,请稍后再来.<br/>
帮会仓库一周开发度排名前三的上市帮会,该帮股票将进行分红.具体的细则近期内将会发布.<br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
</p>
</card>
<%}%>
</wml>