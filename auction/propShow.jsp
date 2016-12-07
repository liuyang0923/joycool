<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.action.auction.PropAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%@ page import="java.util.Vector"%><%
response.setHeader("Cache-Control","no-cache");
PropAction action = new PropAction(request);
if(action.isCooldown("propshow",1000))
	action.propShow(request);
else{
	response.sendRedirect("propShop.jsp");
	return;
}
String result =(String)request.getAttribute("result");
String url = ("propShop.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="神秘商店" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转神秘商店!)<br/>
<a href="propShop.jsp">神秘商店</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
	
DummyProductBean dummyProduct=(DummyProductBean)request.getAttribute("dummyProduct");
UserHonorBean honor = action.getLoginUser().getUserHonor();
int count = UserBagCacheUtil.getUserBagItemCount(dummyProduct.getId(), action.getLoginUser().getId());
float discount = honor.getDiscount();
%>
<card title="神秘商店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(count>0){%>我的行囊现有<%=dummyProduct.getName()%><%=count%>个<br/><%}%>
<%=dummyProduct.getName()%><%=dummyProduct.getTimeString()%><br/>
<%
long time = action.checkProductView(dummyProduct);
//判断道具出现时间
if(time==0 && honor.getRank() >= dummyProduct.getRank()){%>
<a href="propBuy.jsp?dummyId=<%=dummyProduct.getId()%>&amp;t=<%=dummyProduct.getStartTime().getTime()/60000%10000%>">购买一个</a><br/>
<%}else if(honor.getRank() < dummyProduct.getRank()){%>
这个物品需要<%=UserHonorBean.getRankName(dummyProduct.getRank())%>勋章以上才能购买<br/>
<%}%>
<%=(int)(dummyProduct.getBuyPrice() * discount)%>乐币<br/>
<%=dummyProduct.getDescription()%><br/><br/><br/>
<%if(dummyProduct.isBind()){%>
此物品无法赠送或拍卖<br/>
<%}%>
<a href="propShop.jsp">返回神秘商店</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

<%}%>
</wml>