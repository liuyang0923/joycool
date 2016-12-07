<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.LoadResource" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.home.HomeUserBean" %><%response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.homeManage(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
HomeUserBean homeUser=(HomeUserBean)request.getAttribute("homeUser");
String userImageUrl = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="管理家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/img/home/house/<%=homeUser.getTypeId()%>.gif" alt="家的图片"/><br/>
<a href="/home/buyHome.jsp">买新房子</a>
<a href="/home/adornmentRoom.jsp">装修房间</a><br/>
<a href="/home/myStore.jsp">购买家具</a>
<a href="/home/changeGoods.jsp">更换家具</a><br/>
<a href="/home/myStore.jsp">我的仓库</a>
<a href="/user/userBag.jsp">我的行囊</a><br/>
<a href="/home/homeNeighbor.jsp">我的邻居</a>
<a href="/home/homeDiaryList.jsp">日记</a>|<a href="/home/homePhoto.jsp">相册</a><br/>
<a href="/auction/myBuy.jsp">竞价物品</a>
<a href="/auction/myAuction.jsp">拍卖物品</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>