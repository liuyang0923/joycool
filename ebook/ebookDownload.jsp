<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.money.MoneyAction"%><%@ page import="net.joycool.wap.bean.friendlink.RandomLinkBean"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.ebook.FileNameBean"%><%@ page import="net.joycool.wap.bean.ebook.EBookBean"%><%@ page import="net.joycool.wap.bean.CatalogBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.action.wgame.WGameAction"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction,net.joycool.wap.service.factory.*"%><%
    String address = request.getParameter("address");
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="下载">
<p align="left">
<%=BaseAction.getTop(request, response)%>
下载电子书需要等级3<br/>
<%
if(loginUser == null){
%>
您未登录，请先<a href="/user/login.jsp?backTo=<%=PageUtil.getBackTo(request)%>">登录</a><br/>
<%
} else {
	//WGameAction action = new WGameAction();
	UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
	//UserStatusBean us = action.getUserStatus(loginUser.getId());
	//if(us.getGamePoint() < 5000){
	//Liq 2007.3.26下载电子书需要等级3
	if(us.getRank() < 3){
%>
您目前的等级是：<%=us.getRank()%>级,等级太低了!<br/>
<a href="/lswjs/index.jsp">进入赌城赚乐币</a><br/>
<%
	} else {
	    //IUserService service = ServiceFactory.createUserService();
	    //Liq 2007.3.26下载电子书需要等级3
		//UserInfoUtil.updateUserStatus("game_point = game_point - 5000", "user_id = " + loginUser.getId(),loginUser.getId(),UserCashAction.OTHERS,"看ebook扣5000乐币");
		//Liq 2007.3.26下载电子书需要等级3
		// add by zhangyi 2006-07-24 for stat user money history start
		//	MoneyAction.addMoneyFlowRecord(Constants.OTHER,5000,
		//			Constants.SUBTRATION,loginUser.getId());
		// add by zhangyi 2006-07-24  for stat user money history end
%>
<a href="<%=(address.replace("&","&amp;"))%>">保存到手机阅读</a><br/>
<%
    }
}
%>
<anchor title="back"><prev/>返回上一页</anchor><br/>
<br/>
	<%=BaseAction.getBottom(request, response)%><br/>
<%
//<!--add friend link start-->
RandomLinkBean randomLinkBean = new RandomLinkBean(); %>
<img src="<%=( randomLinkBean.getRandomLink(request) ) %>" alt="loading..." />
<%//<!--add friend link end--> %>
</p>
</card>
</wml>
