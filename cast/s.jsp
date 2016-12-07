<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.util.*"%><%!
static CacheService cacheService = CacheService.getInstance();%><%
		
	CustomAction action = new CustomAction(request);
	CastleUserBean user = null;
	if(action.getLoginUser()!=null)
		user = CastleUtil.getCastleUser(action.getLoginUser().getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p>
<%=BaseAction.getTop(request, response)%>
<img src="/cast/img/logo.gif" alt="logo"/><br/>
<a href="index.jsp">++返回城堡大厅</a><br/>
<%if(user == null) {%>一款经营战略游戏，用你的智慧去攻城，掠夺，实现雄霸天下的梦想!<br/><a href="start.jsp">建立自己的城堡</a>
<%} else {
List cacheList = cacheService.getCacheCommonList2(user.getUid(),3);
if(cacheList.size()!=0){
	CommonThreadBean cacheBean = (CommonThreadBean)cacheList.get(0);
%>!!!帐号删除剩余<%=cacheBean.getTimeLeft()%><br/><%}%><%if(!user.isLocked()){%><a href="ad.jsp">我的城堡</a><br/><%}%><a href="account.jsp">我的城堡帐号</a><br/>
++<a href="stat/ww.jsp"><img src="/cast/img/battle.gif" alt="X"/>世界奇迹</a>++<br/>
[<a href="stat/stats.jsp">排名</a>]<a href="stat/stat.jsp">城主</a>|<a href="stat/stat2.jsp">联盟</a>|<a href="stat/stat3.jsp">攻击</a>|<a href="stat/stat4.jsp">防御</a><br/>
<a href="shop.jsp">&gt;&gt;金币商城</a>|<a href="/shop/items.jsp?id=3">购买金币</a>
<%} %><br/>
[必读]<a href="help/help.jsp">详细游戏指南</a><br/>
<a href="/jcforum/forum.jsp?forumId=5545">+玩家交流区+</a><br/>
<a href="help/race.jsp">++种族介绍</a><br/>
<a href="help/soldiers.jsp">++兵种介绍</a><br/>
<a href="help/builds.jsp">++建筑介绍</a><br/>
<a href="help/faq.jsp">++常见问题</a><br/>
<a href="help/faq2.jsp">++城堡进阶说明</a><br/>
~~~~~~~~~~<br/>
<%if(user != null){%><a href="ad.jsp">马上开始游戏</a><%}else{%><a href="start.jsp">马上开始游戏</a><%}%><br/>
<a href="index.jsp">返回城堡战争大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>