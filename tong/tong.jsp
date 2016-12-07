<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.bean.stock2.StockBean" %><%@ page import="net.joycool.wap.bean.tong.TongApplyBean" %><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tong(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
String url=null;
if(result.equals("failure")){
response.sendRedirect(("/tong/tongCenter.jsp"));
return;}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){

}else{
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List onlineTongUserId=(List)request.getAttribute("onlineTongUserId");
TongBean tong =(TongBean)request.getAttribute("tong");
String tongOnlineUserCount=(String)request.getAttribute("tongOnlineUserCount");
StockBean stock  = (StockBean)request.getAttribute("stock");
%>
<card title="乐酷帮会">
<p align="left">
<%=loginUser.showImg("/img/tong/tong/"+action.tongCityImage(tong)+".gif")%>
<%=BaseAction.getTop(request, response)%>
<%if(tong.getMark()==0){%>
<a href="/tong/tongNotice.jsp?tongId=<%=tong.getId()%>"><%=StringUtil.toWml(tong.getTitle()) %></a>主城<br/>
<%}else{%>
荒城<br/>
<%}%>
<%if(loginUser.getId()==tong.getUserId()){%>
帮主：<%=StringUtil.toWml(loginUser.getNickName()) %><br/>
<%}else{
UserBean user = UserInfoUtil.getUser(tong.getUserId());
	if(user!=null){%>
	帮主：<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName()) %></a><br/>
	<%}
} 		
if (us.getTong()==0 && tong.getMark()==0) {%>
<a href="/tong/tongApply.jsp?tongId=<%=tong.getId()%>">申请加入</a><br/>
<% }
TongApplyBean tongApply = action.getTongApply(loginUser.getId(),tong.getId());
//判断是否申请入帮
 if(tongApply!=null) {%>
<a href="/tong/tongApplyCancel.jsp?tongId=<%=tong.getId()%>">撤销入帮申请</a><br/>
<%}%>
<%// 判断是否为帮主是否为副帮主
if (tong.getUserId() == loginUser.getId() || tong.getUserIdA() == loginUser.getId() || tong.getUserIdB() == loginUser.getId()){%>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">管理帮会</a><br/>
<%}// 判断是非会员%>
<% ForumBean forum=null;try{forum=ForumCacheUtil.getForumCacheBean(tong.getId());}catch(Exception e){}
if(forum!=null){%><a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>">帮会论坛</a><%}else{%>论坛故障<%}%><br/> 
<a href="/tong/shop.jsp?tongId=<%=tong.getId()%>">商店</a>|<a href="/tong/hockshop.jsp?tongId=<%=tong.getId()%>">当铺</a>|<a href="/tong/tongFund.jsp?tongId=<%=tong.getId()%>">基金</a>|<a href="/tong/depot.jsp?tongId=<%=tong.getId()%>">仓库</a><br/>
<a href="/tong/tongHonorList.jsp?tongId=<%=tong.getId()%>">旗帜</a>: <%=tong.getHonor()%>
<%if(tong.getHonorDrop()>0){%>
<a href="/tong/tongHonorDrop.jsp?tongId=<%=tong.getId()%>">抢劫旗帜</a>
<%}%><br/>
<a href="/tong/tongCity.jsp?tongId=<%=tong.getId()%>">城墙</a>:<%=tong.getNowEndure() %>/<%=tong.getHighEndure() %><br/>
<%if(stock!=null){%>
<img src="../img/tong/tongStock.gif" alt=""/>目前<a href="/stock2/stockInfo.jsp?stockId=<%=tong.getStockId()%>">股价</a>：<%=stock.getPrice()%><br/>
<%}%>
<a href="/tong/tongUserList.jsp?tongId=<%=tong.getId()%>">所有帮众</a>:(<%=tongOnlineUserCount%>人在线)<br/>
<%
Integer userId=null;
UserBean user=null;
TongUserBean tongUser=null;
int count = 0;
for(int i=0;i<onlineTongUserId.size();i++){
userId=(Integer)onlineTongUserId.get(i);
tongUser=action.getTongUser(tong.getId(),userId.intValue());
if(tongUser==null){continue;}
user=UserInfoUtil.getUser(userId.intValue());%>
<%=++count%>.<%if(user.getId()!=loginUser.getId()){%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><%}else{%><%=StringUtil.toWml(user.getNickName())%><%}%>(<%=LoadResource.getPostionNameByUserId(user.getId())%>)
<% if(tong.getUserId()==user.getId()){%>帮主
<%}else if(tong.getUserIdA()==user.getId() || tong.getUserIdB()==user.getId()){%>副帮主
<%}else{%><%=StringUtil.toWml(action.getTongTitle(tongUser))%>
<%}%><br/>
<%} String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/tong/friendTongs.jsp?tongId=<%=tong.getId()%>">本帮盟友</a><br/>
<a href="/tong/tongCenter.jsp">所有帮会城市</a><br/><br/>
<% if(tong.getUserId() == loginUser.getId()){%>
<a href="change.jsp?tongId=<%=tong.getId()%>">申请转为家族</a><br/>
<%}%>
<%if(us!=null && us.getTong()==tong.getId()){%>
<a href="/tong/tongExitConfirm.jsp?tongId=<%=tong.getId()%>">退出帮会</a><br/>
<%}%>
<%if(tong.getMark()==1){%>
<a href="/tong/buyBarren.jsp?tongId=<%=tong.getId()%>">申请购买</a><br/>
<%}%>
<a href="/Column.do?columnId=10011">帮会须知</a><br/> 
<%--=BaseAction.getAdver(16,response)--%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>