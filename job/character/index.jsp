<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.money.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
String username=null;
String msg=null;
CustomAction action=new CustomAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//	if(loginUser==null){
//		response.sendRedirect("/user/login.jsp?backTo=/job/character/index.jsp");
//		return;
//	}
boolean validate=false;
/*UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
int gamePoint=us.getGamePoint();
if(gamePoint<1000){
	validate=false;
	msg="您的乐币不足!";
}*/
if(request.getParameter("username")!=null){

	username=request.getParameter("username");
	if(username.trim().equals("")){
		msg="请输入内容";
		validate=false;
	}else{
		request.getSession().setAttribute("characterName",username);
		validate=true;
	}
	if(validate==true){
		int userId=0;
		
//		MoneyAction moenyAction=new MoneyAction();
//		userId=action.getLoginUser().getId();
		//System.out.println("before:gamePoint="+action.getLoginUser().getUs().getGamePoint());
/*		gamePoint=gamePoint-1000;
		
			if(gamePoint<=0){
				//moenyAction.addMoneyFlowRecord(Constants.OTHER,1000+gamePoint,Constants.SUBTRATION,userId);
				gamePoint=0;
			}else
			{
				//moenyAction.addMoneyFlowRecord(Constants.OTHER,1000,Constants.SUBTRATION,userId);
			}
*/	
		//action.getLoginUser().getUs().setGamePoint(gamePoint);
		//IUserService userService=ServiceFactory.createUserService(); 
//		UserInfoUtil.updateUserStatus("game_point="+gamePoint,"user_id="+userId,userId,UserCashAction.GAME,"人品测试扣钱1000");
	}
	//System.out.println("after:gamePoint="+action.getLoginUser().getUs().getGamePoint());	 
}
if(validate==true){%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="main" title="载入中...">
<onevent type="ontimer">
<go href="temp.jsp"/> 
</onevent>
<timer value="1"/>
<p mode="nowrap" align="left">载入中...</p>
</card>
</wml>
<%return ;}%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="诸葛神推">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请输入要推算的事情:<br/>
<%if(msg!=null){%><%=msg%><br/><%}%>
<input name="username" maxlength="15"/><br/>
<anchor>确认提交
<go method="post" href="index.jsp">
<postfield name="username" value="$username" />
</go>
</anchor><br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>