<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.shop();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInShop()){	 //离开商店
response.sendRedirect(("go.jsp"));
return; }
int act = action.getParameterInt("a");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(act==0){	//
 %>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
拥有点券:<%=richUser.getMoney2()%><br/>
请选择需要购买的卡片:<br/>
<%for(int i = 0;i < RichShop.MAX_ITEM;i++){
RichItemBean item = action.world.shop.items[i];
if(item!=null){%>
<a href="shop.jsp?a=1&amp;s=<%=i%>&amp;i=<%=item.getId()%>"><%=item.getName()%>(<%=item.getPrice()%>)</a><br/>
<%}}%>
<br/><a href="shop2.jsp">卖出卡片</a><br/>
<a href="shop.jsp?a=2">离开商店</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else{%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="shop.jsp">继续购买</a><br/>
<a href="shop2.jsp">卖出卡片</a><br/>
<a href="shop.jsp?a=2">离开商店</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>