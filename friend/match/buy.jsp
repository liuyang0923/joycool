<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.spec.shop.ShopUtil,net.joycool.wap.spec.shop.UserInfoBean,net.joycool.wap.spec.shop.ShopAction,net.joycool.wap.bean.UserBean,net.joycool.wap.bean.UserStatusBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,java.text.DecimalFormat"%>
<%! static DecimalFormat numFormat = new DecimalFormat("0.0"); %>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
String tip = "";
MatchRes res = null;
boolean buyResult = false;
UserInfoBean bean = null;
UserStatusBean us = null;
String buyResultStr = "";
UserBean loginUser = action.getLoginUser();
int id = action.getParameterInt("id");
int type = action.getParameterInt("t");
int count = action.getParameterInt("c");
//if (loginUser == null){
//	response.sendRedirect("/user/login.jsp");
//	return;
//}
if (loginUser != null){
	bean = ShopAction.shopService.getUserInfo(loginUser.getId());
	us = UserInfoUtil.getUserStatus(loginUser.getId());
} else if (type == 1){
	out.clearBuffer();
	response.sendRedirect("/user/login.jsp");
	return;
}
res = MatchAction.getRes(id);
if (res == null){
	tip = "该物品不存在.";
} else {
	if (type != 0 && (count < 1 || count > 99)){
		tip = "物品数量错误(1-99).";
	}
	if (type == 2){
		// 购买
		buyResult = action.buy(res,count);
		buyResultStr = (String)request.getAttribute("buyResultStr");
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="奢侈品商城"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (type == 0){
// 查看物品信息
int buyCount = 0;
if (res.getCur() == 0){
	buyCount = (us == null ? 0 : us.getGamePoint() / res.getPrices());
} else {
	buyCount = (int)(bean == null ? 0f : bean.getGold() / (res.getPrices() / 100f));
}
%><%=StringUtil.toWml(res.getResName())%><br/><img src="img/<%=res.getPhoto()%>" alt="o" /><br/><%=StringUtil.toWml(res.getResName())%>,销售价格<%if (res.getCur() == 0){%><%=StringUtil.bigNumberFormat2(res.getPrices())%>乐币<%}else{%><%=numFormat.format(res.getPrices() / 100f)%>酷币<%}%>/个,可给乐后增添<%=res.getPoint()%>靓点.您最多可以购买<%=buyCount%>个(单次最多购买99个).<br/>
<input name="count" maxlength="3" format="*N"/><br/>
<anchor>
购买
<go href="buy.jsp?id=<%=res.getId()%>&amp;t=1" method="post">
	<postfield name="c" value="$count" />
</go>
</anchor><br/>
<a href="shop.jsp">返回上一级</a><br/>
<%
} else if (type == 1) {
// 确认购买
%>您要购买<%=count%>个<%=StringUtil.toWml(res.getResName())%>吗?<br/>
<a href="buy.jsp?id=<%=res.getId()%>&amp;t=2&amp;c=<%=count%>">确认购买</a><br/>
<a href="buy.jsp?id=<%=res.getId()%>">返回上一级</a><br/>
<%
} else if (type == 2){
// 购买结果	
%><%=buyResultStr%><br/>
<%if (!buyResult){
%>您目前有酷币您有酷币<img src="img/gold.gif" alt="o" /><%=bean==null?"0":bean.getGoldString() %>[<a href="/pay/pay.jsp">充值</a>]<br/>
身上有<%=us!=null?StringUtil.bigNumberFormat2(us.getGamePoint()):"0"%>乐币[<a href="/lswjs/gameIndex.jsp">赚钱</a>]<br/><%
}
}
} else {
%><%=tip%><br/><%		
}
%><a href="shop.jsp">奢侈品商城</a>|<a href="index.jsp">返回乐后首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>