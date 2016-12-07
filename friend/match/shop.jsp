<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.spec.shop.ShopUtil,net.joycool.wap.spec.shop.UserInfoBean,net.joycool.wap.spec.shop.ShopAction,net.joycool.wap.bean.UserBean,net.joycool.wap.bean.UserStatusBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,java.text.DecimalFormat"%>
<%! static DecimalFormat numFormat = new DecimalFormat("0.0"); %>
<%response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
String tip = "";
String tmp = "";
MatchRes res = null;
MatchTrends trend = null;
UserBean loginUser = action.getLoginUser();
//if (loginUser == null){
//	response.sendRedirect("/user/login.jsp");
//	return;
//}
List resList = MatchAction.getResList();
UserInfoBean bean = null;
UserStatusBean us = null;
List trendList = null;
if (loginUser != null){
	bean = ShopAction.shopService.getUserInfo(loginUser.getId());
	us = UserInfoUtil.getUserStatus(loginUser.getId());
	trendList = MatchAction.service.getTrendsList(" left_uid!=" + loginUser.getId() + " and flag=1 order by id desc limit 5");
}
float money = 0f;
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买选票"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=action.getTopTrendsByRandom()%>购买奢侈品投给中意靓女,她的排名就会上升哦！<br/>
您目前有酷币<img src="img/gold.gif" alt="o" /><%=bean==null?"0":bean.getGoldString() %>[<a href="/pay/pay.jsp">充值</a>]<br/>
身上有<%=us!=null?StringUtil.bigNumberFormat2(us.getGamePoint()):"0"%>乐币[<a href="/lswjs/gameIndex.jsp">赚钱</a>]<br/>
<a href="box.jsp">我的奢侈品</a><br/>
==酷币奢侈品区==<br/>
<%if (resList != null && resList.size() > 0){
for (int i = 0 ; i < resList.size() ; i++){
res = MatchAction.getRes(StringUtil.toInt(resList.get(i).toString()));
if (res != null && res.getCur() == 1){
money = res.getPrices() / 100.0f;
%><a href="buy.jsp?id=<%=res.getId()%>"><%=StringUtil.toWml(res.getResName())%></a><%=numFormat.format(money)%>酷币/个[<a href="buy.jsp?id=<%=res.getId()%>">购买</a>]<br/><%
}
}	
}
%>
==乐币奢侈品区==<br/>
<%if (resList != null && resList.size() > 0){
for (int i = 0 ; i < resList.size() ; i++){
res = MatchAction.getRes(StringUtil.toInt(resList.get(i).toString()));
if (res != null && res.getCur() == 0){
%><a href="buy.jsp?id=<%=res.getId()%>"><%=StringUtil.toWml(res.getResName())%></a><%=StringUtil.bigNumberFormat2(res.getPrices())%>乐币/个[<a href="buy.jsp?id=<%=res.getId()%>">购买</a>]<br/><%
}
}	
}
%>
<a href="buyLog.jsp">我的购买记录</a><br/>
==其他用户购买实录==<br/>
<%if (trendList != null && trendList.size() > 0){
for (int i = 0 ; i < trendList.size() ; i++){
	trend = (MatchTrends)trendList.get(i);
	if (trend != null){
		tmp = StringUtil.toWml(trend.getContent());
		tmp = tmp.replace("%l","<a href=\"/user/ViewUserInfo.do?userId=" + trend.getLeftUid() + "\">" + UserInfoUtil.getUser(trend.getLeftUid()).getNickNameWml() + "</a>");
		%><%=tmp%><br/><%
	}
}
}
%>
为<input name="uid" format="*N"/>
<anchor>
	投票
	<go href="vote.jsp" method="post">
		<postfield name="uid" value="$uid" />
	</go>
</anchor><br/>
<%	
} else {
%><%=tip%><br/><%		
}
%><a href="index.jsp">返回乐后首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>