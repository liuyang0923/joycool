<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.job.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String luck=(String)session.getAttribute("luck");
String constellation=request.getParameter("constellation");
//LuckAction.clearLuckMap();
if(luck!=null&&constellation!=null)
{
	session.removeAttribute("luck");
	//扣钱,查询相应星座信息
	UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
	int money=100;
	int point=5;
	if(usb.getGamePoint()<money)
	{
		request.setAttribute("tip","对不起!你的乐币已经不足"+money+"!");
		RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
		rd.forward(request,response);
	}else{
		UserInfoUtil.updateUserStatus("game_point=game_point-"+money+", point=point+"+point , "user_id="
				+ loginUser.getId(), loginUser.getId(),UserCashAction.GAME, "用户看运势扣钱"+money);
		//查询今天的运势
		String con=request.getParameter("constellation");
		HashMap luckMap=LuckAction.getLuckMap();
		LuckBean luckBean=(LuckBean)luckMap.get(con);
		session.setAttribute("luckBean",luckBean);

		//response.sendRedirect(("luck.jsp"));
		BaseAction.sendRedirect("/job/luck/luck.jsp", response);
		return;
	}
}else
{
	//response.sendRedirect(("index.jsp"));
	BaseAction.sendRedirect("/job/luck/index.jsp", response);
	return;
}	
%>
