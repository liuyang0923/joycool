<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,java.util.regex.Matcher,java.util.regex.Pattern,net.joycool.wap.util.*,net.joycool.wap.cache.util.ForumCacheUtil,net.joycool.wap.cache.util.TongCacheUtil,net.joycool.wap.bean.jcforum.ForumUserBean,net.joycool.wap.action.tong.TongAction,net.joycool.wap.bean.tong.TongBean,net.joycool.wap.bean.tong.TongUserBean,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   int explain = action.getParameterInt("e");
   TongUserBean tongUser = null;
   TongBean tong = null;
   TongAction taction = null;
   UserInfo userInfo = action.getUserInfo(action.getLoginUser().getId());
   UserStatusBean userStatus=(UserStatusBean)UserInfoUtil.getUserStatus(action.getLoginUser().getId());
   ForumUserBean forumUser = ForumCacheUtil.getForumUser(action.getLoginUser().getId());
   if(userStatus.getTong()>0&&userStatus.getTong()<20000){
	   tongUser = TongCacheUtil.getTongUser(userStatus.getTong(),action.getLoginUser().getId());
	   tong = TongCacheUtil.getTong(userStatus.getTong());
	   taction=new TongAction(request);
   }
   String tip = "";
   int totalPoint = action.getNetPoint(userStatus,forumUser,tongUser);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (explain==1){
%>网站信用担保分数:<br/>
在线时长超过168小时的用户可信度增加3分;<br/>
等级高于15级用户信任度增加3分;<br/>
帮会帮主可信度增加3分;<br/>
论坛经验高于100可信度增1分.<br/>
<a href="net.jsp">[返回上一页]</a><br/><%
} else {
%>您目前的交友可信度为<%=userInfo.getTotalPoint()%>%<br/>
<a href="net.jsp?e=1">网站信用担保(10)</a>:<%=totalPoint%><br/>
在线时长:<%=userStatus.getOnlineTime()%>小时<br/>
您的等级:<%=userStatus.getRank()%><br/>
帮会:<%if (tongUser != null){if(tongUser.getMark()==2){%>帮主<%}else if(tongUser.getMark()==1){%>副帮主<%}else{%><%=StringUtil.toWml(taction.getTongTitle(tongUser))%><%}}else{%>无<%}%><br/>
论坛经验:<%if(forumUser != null){%><%=forumUser.getExp()%><%}else{%>0<%}%><br/>
<a href="credit.jsp">[返回可信度首页]</a><br/>
<%	
}
} else {
	%><%=tip%><br/><a href="net.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>