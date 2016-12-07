<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="/checkMobile.jsp"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.action.job.CardAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.job.CardBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil" %><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 1800),
};
%><%
response.setHeader("Cache-Control","no-cache");
Tiny2Action action2 = new Tiny2Action(request, response);
if(action2.checkGame(3)) return;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(action2.getGame() == null){
	int[] count = CardAction.countMap.getCount(loginUser.getId());
	if((count[0] - 20) % 50 == 0){
		action2.startGame(games[0], 3);
		count[0]++;
		return;
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
// 抽卡骤标志
String step = request.getParameter("step");
if(step == null){
   step = "1";
}
if(step.equals("1")){%><%}

CardAction action = new CardAction(request);
if (("2").equals(step)){
	if (loginUser == null){
		out.clearBuffer();
		response.sendRedirect("/user/login.jsp");
		return;
	}
	action.checkUserPoint(request,response);
}
// 后台参数验证提示信息
String message = "";
if (request.getAttribute("msg")!=null){
	message = "您的乐币不足以购买一张机会卡！<br/>";
	step="1";
}
// 开始抽取操作
if (("1").equals(step)){
%>
<card title="购买机会卡">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%session.removeAttribute("randomuserbag");%>
<%=message%>
老巫婆抚摸着水晶球：欢迎来接受神奇法术的洗礼，你愿意花1000乐币，来决定自己的未来吗？<br/>
购买
<a href="buyCard.jsp?step=2">红色卡</a> 
<a href="buyCard.jsp?step=2">黄色卡</a> 
<a href="buyCard.jsp?step=2">蓝色卡</a>
<br/>
<br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("2").equals(step)){
String url =("/job/card/buyCard.jsp?step=3");
%>
<card title="购买机会卡处理" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
老巫婆举起水晶球：天灵灵、地灵灵！众神拯救你的灵魂啊！<br/>
(3秒钟跳转)<br/>
<a href="buyCard.jsp?step=3" title="直接进入">直接进入</a><br/>
<br/>
<a href="buyCard.jsp?step=1" title="返回上一级">返回上一级</a>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if (("3").equals(step)){
	if (loginUser == null){
		response.sendRedirect("../user/login.jsp");
		return;
	}
	if(ForbidUtil.isForbid("game",loginUser.getId())){
		response.sendRedirect("/bottom.jsp");
		return;
	}
	
Long t = (Long)session.getAttribute("buycard");
long t2 = System.currentTimeMillis();
if(t == null || t2 > t.longValue()) {
	session.setAttribute("buycard", Long.valueOf(t2 + 2000));
} else {
    response.sendRedirect("buyCard.jsp");
    return;
}

	if (session.getAttribute("Buying") == null){
		// 不正常进入抽卡页，如果抽过卡显示抽卡结果，否则，跳到error页
		if (session.getAttribute("CardBean") == null){
			BaseAction.sendRedirect("/failure.jsp", response);
			return;
		}
	} else {
		// 抽卡，更改用户状态数据放入session
		CardBean cardBean = action.getRandomCard();

		session.setAttribute("CardBean", cardBean);
		action.dealUserStatusOfBuyCard(cardBean);
	}
	CardBean rodomCard = null;
	if (session.getAttribute("CardBean")!=null){
		// 可能是返回第3个页面
		rodomCard = (CardBean)session.getAttribute("CardBean");
	} else {
			BaseAction.sendRedirect("/failure.jsp", response);
			return;
	}
%>
<card title="购买机会卡结果">
<p align="left">
<%=BaseAction.getTop(request, response)%>
老巫婆：你抽中的是<%=rodomCard.getCardName()%>！<br/>
<%=loginUser.showImg(Constants.JOB_CARD_IMG_PATH+rodomCard.getTypeId()+".gif")%>
<%
String actionDirectionStr = "";
String actionfieldStr = "";
if (rodomCard.getActionDirection() == 0){
	actionDirectionStr = "相应增加";
}else if (rodomCard.getActionDirection() == 1){
	actionDirectionStr = "相应减去";
}
if (rodomCard.getActionfield() == 0){
	actionfieldStr = "乐币";
}else if (rodomCard.getActionfield() == 1){
	actionfieldStr = "经验";
}
%>
<%if(rodomCard.getActionfield()==3){

if( session.getAttribute("randomuserbag")!=null){
DummyProductBean randomuserbag=(DummyProductBean)session.getAttribute("randomuserbag");
session.setAttribute("keepProductCheck", "keepProductCheck");%>
<%=rodomCard.getDescription().replace("card",randomuserbag.getName())%> 
准备<br/>
<%if(randomuserbag.getDummyId()!=2){%>
<a href="/user/fromCardUse.jsp?productId=<%=randomuserbag.getId()%>">使用</a>/
<%}%>
<a href="/user/fromCardPersent.jsp?productId=<%=randomuserbag.getId()%>">赠送</a>/
<a href="/auction/fromCardIndex.jsp?productId=<%=randomuserbag.getId()%>">拍卖</a>/
<a href="buyCard.jsp">丢弃</a>
<%
loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean userStatusBean = UserInfoUtil.getUserStatus(loginUser.getId());
List bagList=UserBagCacheUtil.getUserBagListCacheById(loginUser.getId());
if(bagList!=null&&userStatusBean.getUserBag()>bagList.size()){%>
/<a href="/user/keepProduct.jsp?productId=<%=randomuserbag.getId()%>">存入行囊</a>
<%}%>
 <br/>
<%}}else{%>
<%=rodomCard.getDescription()%> 
<%}
if (rodomCard.getTypeId() <=4){
%>
<%
}
if (rodomCard.getValueType() == 1){
//out.print("%");
}
// 当前用户信息
loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
%>
<br/>你目前乐币<%=us.getGamePoint()%>，经验<%=us.getPoint()%>。<br/>
老巫婆：孩子，还要再来一次吗？<br/>
<a href="buyCard.jsp?step=1">购买机会卡</a>
<br/>
<%
// 机分卡抽取过程跟踪
session.removeAttribute("Buying");
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>