<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.info();

Vector match = (Vector)request.getAttribute("match");

int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
Vector vector = (Vector) request.getAttribute("vector");
String prefixUrl = (String) request.getAttribute("prefixUrl");
int totalHallPageCount = ((Integer) request.getAttribute("totalHallPageCount")).intValue();
String count = (String) request.getAttribute("count");

PetUserBean petBean;
UserBean user;

UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean statusBeans = null;
if (loginUser != null){
	statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());	
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="宠物大厅">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt=""/><br/>
<a href="/pet/index.jsp">进入我的宠物</a><br/>
<a href="/pet/stakeing.jsp">赌博投注赛下注</a><br/>
<a href="/Column.do?columnId=9233">宠物详细指南</a><br/>
<a href="/pet/changestage.jsp">积分换道具</a><br/>
<a href="/pet/top.jsp">等级排名</a><br/>
<a href="/pet/todaytop.jsp">今日积分排名</a><br/>
==宠物积分排名==<br/>

<%
Iterator iter = vector.iterator();
int number=0;
while(iter.hasNext()) {
number++;
petBean = (PetUserBean)iter.next();
user = UserInfoUtil.getUser(petBean.getUser_id());
%>
<%=pageIndex*5+number%>.
<a href="/pet/viewpet.jsp?id=<%=petBean.getId()%>">
<%=StringUtil.toWml(petBean.getName())%>
</a>
<%=petBean.getIntegral()%>点<br/>
<%}%>
<%
String fenye = PageUtil.shuzifenye(totalHallPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%}%>






==宠物比赛信息==<br/>
<%
			for(int i=1;i<=match.size();i++){
			MatchRunBean matchrunbean = (MatchRunBean)match.get(i-1);
			if(matchrunbean.getCondition() == 1){
%>
游戏中...
共<%=matchrunbean.getPeoplenumber()%>玩家
<%if(matchrunbean.getCondition() != 0) {%>
<a href="/pet/runing.jsp?task=4&amp;type=<%=matchrunbean.getType()%>&amp;id=<%=matchrunbean.getId()%>">观看比赛</a><br/>
<%}%>
<%}else{%>
已结束...
共<%=matchrunbean.getPeoplenumber()%>玩家
<%if(matchrunbean.getCondition() != 0) {%>
<a href="/pet/runing.jsp?task=4&amp;type=<%=matchrunbean.getType()%>&amp;id=<%=matchrunbean.getId()%>">查看结果</a><br/>
<%}}}%>
===========<br/>

领养一个可爱的宠物吧！宠物除了需要主人照料饮食、清洁、打工等日常生活外，还可以跟主人玩耍，跟别的宠物比赛。宠物有经验值(测试期间最高50000)，升级之后主人可自由分配升级点让它更聪明、灵活和强壮，宠物类型不同起始属性也不同，每个宠物都是独一无二的！不管主人是否在线，宠物宝宝都会每48小时长大一岁，好好照顾它吧！<br/>


<%=BaseAction.getBottom(request, response)%>
</p>
</card>

</wml>