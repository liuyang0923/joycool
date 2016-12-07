<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   int todayAward = 0;
   int today = action.getTodayInt();
   StringBuilder sb = new StringBuilder();
   int award = action.getParameterInt("a");
   GuestUserInfo guestUser = action.getGuestUser();
   if(guestUser == null){
	   %><%@include file="in.jsp"%><%	   
   }
   String news = action.getNews(2,1);
   if (award == 1){
	   if (guestUser == null){
		   response.sendRedirect("login.jsp");
		   return;
	   }
	   if (today == guestUser.getAward()){
		   sb.append("您今天已经领取过奖励了.");
	   } else {
		   int special = 0;		// 特殊奖励
		   todayAward = action.getAward(guestUser);	// 看我今天能加多少钱
		   if (guestUser.getAward() == 0){
				if (guestUser.getBuid() == 0){
					// 游客第一次来领取,特殊奖励1000
					special = 1000;
					GuestHallAction.updateMoney(guestUser.getId(),special);
					sb.append("恭喜你获得奖励游币");
					sb.append(special);
					sb.append(",已领取今日游币");
					sb.append(todayAward);
					sb.append(".<br/>*温馨提示:<a href=\"/register.jsp\">注册</a>乐酷正式用户后会获得10000游币,每天上线还可以多获得200游币哟~");
					SqlUtil.executeUpdate("update guest_user_info set special=1 where id=" + guestUser.getId(),6);
				} else {
					// 正式用户第一次来领取,特殊奖励10000
					special = 10000;
					GuestHallAction.updateMoney(guestUser.getId(),special);
					sb.append("您是乐酷正式用户,获得奖励游币");
					sb.append(special);
					sb.append(",已领取今日游币");
					sb.append(todayAward);
					sb.append(".");
					SqlUtil.executeUpdate("update guest_user_info set special=2 where id=" + guestUser.getId(),6);
				}
		   } else {
			   sb.append("已领取今日游币");
			   sb.append(todayAward);
			   sb.append(".");
		   }
		   action.getAwardToday(guestUser);	// 加钱
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游乐园"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (award == 1){
%><%=sb.toString()%><br/><a href="index.jsp">返回游乐园</a><br/><%
} else {
if (guestUser != null && guestUser.getUserName().length() > 0){
%><%=action.getMsg(guestUser.getId())%>欢迎你,<%=guestUser.getUserNameWml()%><br/>
<%if (today != guestUser.getAward()){
%><a href="index.jsp?a=1">点击获得今日游币</a><br/><%
}
if (guestUser.getMobile() != null && guestUser.getMobile().length() > 0){
%><a href="bm.jsp">保存书签</a><br/><a href="focus.jsp">我的关注</a>|<a href="info.jsp">个人资料</a><br/><%
} else if (guestUser.getFlag() > 0){
%><%if(action.getLoginUser()==null){%><a href="phone.jsp">绑定手机</a>|<a href="bm.jsp">保存书签</a><br/><%}%><a href="focus.jsp">我的关注</a>|<a href="info.jsp">个人资料</a><br/><%
} else {
%><%if(action.getLoginUser()==null){%><a href="pw.jsp">设置密码</a>|<a href="bm.jsp">保存书签</a><br/><%}%><a href="focus.jsp">我的关注</a>|<a href="info.jsp">个人资料</a><br/><%
}
} else {
%><a href="nick.jsp">设置昵称</a>|<a href="login.jsp">登陆</a><br/><%
}%>
[交友]有缘千里来相会<br/>
<a href="/guest/chat.jsp">游客聊天室</a>.<a href="/jcforum/index.jsp">论坛</a>.<a href="/guest/wall/index.jsp">用户墙</a>.<a href="/news/index.jsp">新闻</a><br/>
<%=GuestHallAction.titleDesc[RandomUtil.nextIntNoZero(GuestHallAction.titleDesc.length - 1)]%><br/>
[游戏]哥玩的是寂寞<br/>
<a href="/guest/nb/game.jsp">名字大PK</a>|<a href="/guest/pt/instructions.jsp">智慧拼图</a><br/>
<a href="/guest/farmer/index.jsp">完美农夫</a>|<a href="/guest/sd/index.jsp">数独</a>|<a href="/guest/hrd/hrd.jsp">华容道</a><br/>
<a href="/guest/question/index.jsp">问答接龙</a>|<a href="/guest/fish/index.jsp">欢乐渔场</a><br/>
<a href="/guest/dicedx/index.jsp">骰子</a>|<a href="/guest/showhand/index.jsp">梭哈</a>|<a href="/guest/tiger/index.jsp">老虎机</a><br/>
[排行榜]见证荣耀与辉煌<br/>
<a href="rank.jsp?t=1">经验排行</a>|<a href="rank.jsp">财富排行</a><br/>
<%
}
} else {
	%><%=tip%><br/><a href="/lswjs/index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>