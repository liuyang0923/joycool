<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBagBean" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
%><%@include file="../bank/checkpw.jsp"%><%
UserBagAction action = new UserBagAction(request);
action.userBag(request);
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List userBagList=(List)request.getAttribute("userBagList");
String userBagCount=(String)request.getAttribute("userBagCount");
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title=" 我的行囊">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=loginUser.showImg("/img/user/userBag.gif")%>
<%
if(userBagList.size()>0){%>
我的行囊(<%=userBagCount%>/<%=loginUser.getUs().getUserBag()%>)<a href="userBagCat.jsp">分类</a><br/>
<%
Integer userBagId = null;
UserBagBean userBag = null;
DummyProductBean dummyProduct = null;
for(int i=0;i<userBagList.size();i++){
    userBagId=(Integer)userBagList.get(i);
    userBag=action.getUserBag(userBagId.intValue());
    if(userBag==null){System.out.println(loginUser.getId()+" not exists "+userBagId);continue;}
    dummyProduct=action.getDummyProduct(userBag.getProductId());
    if(dummyProduct == null) continue;%>
<%=i+1%>.<a href="userBagUse.jsp?userBagId=<%=userBag.getId()%>"><%=dummyProduct.getName()%></a>
<%=userBag.getTimeString(dummyProduct)%><br/>
<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/>
<%}}else{%>您的行囊中没有物品!<br/><%}%>
<a href="/auction/myBuy.jsp">我竞价的物品</a><br/>
<a href="/auction/myAuction.jsp">我拍卖的物品</a><br/>
<a href="/auction/auctionHall.jsp">拍卖大厅</a><br/>
<a href="/home/home.jsp">家园首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>