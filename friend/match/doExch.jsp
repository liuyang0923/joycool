<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.UserBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
String tip = "";
int type = action.getParameterInt("t");
MatchGood matchGood = null;
MatchUser matchUser = MatchAction.getMatchUser(loginUser.getId());
String phone = "";
String name = "";
String address = "";
int goodId = action.getParameterInt("gid");
if (goodId <= 0){
	tip = "没有找到相关物品.";
} else {
	matchGood = MatchAction.getGood(goodId);
	if (matchGood == null){
		tip = "没有找到相关物品.";
	} else {
		if (!action.confirmExch(matchUser,matchGood.getId())){
			tip = (String)request.getAttribute("tip");
		} else {
			phone = action.getParameterNoEnter("p");
			name = action.getParameterNoEnter("n");
			address = action.getParameterNoEnter("a");
		}
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="商品兑换"><p>
<%=BaseAction.getTop(request, response)%>
<%if("".equals(tip)){
%>您靓点兑换的<%=matchGood.getGoodNameWml()%>兑换成功,共消耗靓点<%=matchGood.getPrice2()%>点.<br/>
您的联系电话是:<%=phone%><br/>
收货人姓名:<%=StringUtil.toWml(name)%><br/>
收货地址:<%=StringUtil.toWml(address)%><br/>
您选定的商品将在我们与您核实信息后发货,请注意保持手机畅通!<br/>
<%
} else {
%><%=tip%><br/><a href="exch.jsp?t=1&amp;gid=<%=goodId%>">返回上一页</a><br/><%
}
%><a href="goods.jsp">返回商品兑换商城</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>