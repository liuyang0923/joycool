<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.UserBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%! static String rankStr[] = {"","一","二","三","四-六","七-十"}; %>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
//if (loginUser == null){
//	response.sendRedirect("/user/login.jsp");
//	return;
//}
String tip = "";
int type = action.getParameterInt("t");
MatchGood matchGood = null;
MatchUser matchUser = null;
if (loginUser != null){
	matchUser = MatchAction.getMatchUser(loginUser.getId());
}
int goodId = action.getParameterInt("gid");
if (goodId <= 0){
	tip = "没有找到相关物品.";
} else {
	matchGood = MatchAction.getGood(goodId);
	if (matchGood == null || matchGood.getHide() == 1){
		tip = "没有找到相关物品.";
	}
}
String title = "奖品介绍";
if (matchGood != null && matchGood.getFlag() == 0){
	title="商品兑换";
}
if (type == 1){
	if (loginUser == null){
		out.clearBuffer();
		response.sendRedirect("/user/login.jsp");
		return;
	}
	if (matchGood != null && matchGood.getCountNow() <= 0){
		tip  = "此物品已没有库存.";
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if("".equals(tip)){
if (type == 0){
// session中写入要购买的标记
session.setAttribute("gid",matchGood.getId() + "");
// 奖品说明
%><%if (matchGood.getFlag() > 0){%>第<%=rankStr[matchGood.getFlag()]%>名奖品-<%}%><%=matchGood.getGoodNameWml()%><br/>
专柜价格<%=matchGood.getPrice()%>元<%if (matchGood.getFlag() == 0){%>,靓点兑换<%=matchGood.getPrice2()%><%}%><br/>
<img src="<%=MatchAction.ATTACH_GOOD_URL_ROOT + matchGood.getPhoto()%>" alt="o" /><br/>
=商品规格&amp;产品点评=<br/>
<%=matchGood.getDescribeWml()%><br/>
<%if (matchGood.getFlag()==0){
%><a href="exch.jsp?t=1&amp;gid=<%=matchGood.getId()%>">靓点兑换</a><br/><%	
}
} else if (type == 1){
// 兑换
%>您准备购买的商品:<%=matchGood.getGoodNameWml()%><br/>
靓点兑换:<%=matchGood.getPrice2()%>靓点<br/>
您的靓点数:<%=matchUser != null?matchUser.getVoteCount()-matchUser.getConsume():0%>靓点<br/>
请填写:<br/>
您的联系电话:<br/>
<input name="phone" value="" maxlength="20" format="*N"/><br/>
*请仅填数字号码,勿带符号<br/>
收货人姓名:<br/>
<input name="name" maxlength="10" /><br/>
收货地址:<br/>
<input name="address" maxlength="100" /><br/>
<anchor>
	确认兑换
	<go href="doExch.jsp?gid=<%=goodId%>" method="post">
		<postfield name="p" value="$phone" />
		<postfield name="n" value="$name" />
		<postfield name="a" value="$address" />
	</go>
</anchor><br/>
提示:请认真填写地址及姓名,方便发货确认!!<br/>
**每周周三统一发货,咨询电话400-779-0940<br/>
<a href="exch.jsp?gid=<%=goodId%>">返回商品页</a><br/>
<%
}
} else {
%><%=tip%><br/><%
}
%><a href="goods.jsp">返回商品兑换商城</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>