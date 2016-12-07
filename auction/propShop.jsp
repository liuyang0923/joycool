<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.PropAction"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%@ page import="java.util.Vector"%><%@ page import="java.sql.Timestamp"%><%response.setHeader("Cache-Control","no-cache");
PropAction action = new PropAction(request);
action.propShop(request);
String result =(String)request.getAttribute("result");
String url=("propShop.jsp");
net.joycool.wap.service.infc.IDummyService dService = action.getDummyService();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="神秘商店" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转神秘商店!)<br/>
<a href="/auction/propShop.jsp">神秘商店</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
Vector gameProduct=(Vector)request.getAttribute("gameProduct");
Vector zhengProduct=(Vector)request.getAttribute("zhengProduct");
Vector composeProduct=(Vector)request.getAttribute("composeProduct");
int type = action.getParameterInt("type");
%>
<card title="神秘商店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="/Column.do?columnId=6909">神秘商店说明</a><br/>
<%if(type==0){%>限量<%}else{%><a href="propShop.jsp?type=0">限量</a><%}%>|<%if(type==1){%>不限量<%}else{%><a href="propShop.jsp?type=1">不限量</a><%}%>|<%if(type==2){%>勋章<%}else{%><a href="propShop.jsp?type=2">勋章</a><%}%><%
if(action.getLoginUser() != null && action.getLoginUser().getUs().getRank()>=3){
%>|<a href="/shop/index.jsp">商城</a><%
}%><br/>
<%if(gameProduct.size()>0){%>**特殊道具**<br/>
<%for(int i=0;i<gameProduct.size();i++)
	{
	DummyProductBean dpBean =(DummyProductBean)gameProduct.get(i);
	dpBean = dService.getDummyProducts(dpBean.getId());
	//判断道具出现时间
	long time = action.checkProductView(dpBean);
	if(time==0){%>
	<a href="propShow.jsp?dummyId=<%=dpBean.getId()%>"><%=dpBean.getName()%></a>|
	<a href="propShow.jsp?dummyId=<%=dpBean.getId()%>">购买</a><br/>
	<%}else{%>
    <a href="propShow.jsp?dummyId=<%=dpBean.getId()%>"><%=dpBean.getName()%></a>
    <%if(time>10){%><%=(time/60)+1%>小时<%}else{%><%=time+1%>分钟<%}%><br/>
	<%}%>
<%}}%>
<%if(composeProduct.size()>0){%>**合成道具**<br/>
<%for(int i=0;i<composeProduct.size();i++){
	DummyProductBean dpBean =(DummyProductBean)composeProduct.get(i);
	dpBean = dService.getDummyProducts(dpBean.getId());
	//判断道具出现时间
	long time = action.checkProductView(dpBean);
	if(time==0){%>
	<a href="propShow.jsp?dummyId=<%=dpBean.getId()%>"><%=dpBean.getName()%></a>|
    <a href="propShow.jsp?dummyId=<%=dpBean.getId()%>">购买</a><br/>
    <%}else{%>
    <a href="propShow.jsp?dummyId=<%=dpBean.getId()%>"><%=dpBean.getName()%></a>
	<%if(time>10){%><%=(time/60)+1%>小时<%}else{%><%=time+1%>分钟<%}%><br/>
	<%}%>
<%}}%>
<%if(zhengProduct.size()>0){%>**整人道具**<br/>
<%for(int i=0;i<zhengProduct.size();i++){
	DummyProductBean dpBean =(DummyProductBean)zhengProduct.get(i);
	dpBean = dService.getDummyProducts(dpBean.getId());
	//判断道具出现时间
	long time = action.checkProductView(dpBean);%>
	<a href="propShow.jsp?dummyId=<%=dpBean.getId()%>"><%=dpBean.getName()%></a>
	<%if(time==0){%>
	|<a href="propShow.jsp?dummyId=<%=dpBean.getId()%>">购买</a><br/>
    <%}else{%>
	<%if(time>10){%><%=(time/60)+1%>小时<%}else{%><%=time+1%>分钟<%}%><br/>
	<%}%>
<%}}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>