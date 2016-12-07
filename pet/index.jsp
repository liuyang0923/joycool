<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("loginUser")==null){
response.sendRedirect("/user/login.jsp");
return;
}
PetAction action = new PetAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
PetUserBean petUser = (PetUserBean)request.getAttribute("petUser");
if(petUser==null&&ForbidUtil.isForbid("game",loginUser.getId())){
	response.sendRedirect("/enter/not.jsp");
	return;
}
action.index();
String result = (String)request.getAttribute("result");
Vector petList = (Vector)request.getAttribute("petList");
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>

<% if("success".equals(result)) 
{//载入成功%>
<card title="宠物资料">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if (petUser.getHealth() > 0) {%>
<img src="img/<%=action.getImage()%>" alt=""/><br/>
<%}else{%>
<img src="img/dead.gif" alt=""/><br/>
你的宠物死掉了，快去医院吧，也许还有救！！！<br/>
<%}%>

<%if (petUser.getSpot() > 5) {%>
<a href="/pet/addspot.jsp"><%=StringUtil.toWml(petUser.getName())%>还有<%=petUser.getSpot()%>个升级点,快去升级吧</a><br/>
<%}%>
<%=StringUtil.toWml(petUser.getName())%><a href="/pet/rename.jsp">改名</a><br/>
积分：<%=petUser.getIntegral()%><br/>
状态:

<%if (petUser.getHealth() > 60) {%>
<img src="img/health1.gif" alt=""/>健康
<%} else if ((petUser.getHealth() > 30) && (petUser.getHealth() <= 60)) {%>
<img src="img/health2.gif" alt=""/>虚弱
<%} else if (petUser.getHealth() > 0) {%>
<img src="img/health3.gif" alt=""/>疾病
<%}else{%>
<img src="img/health4.gif" alt=""/>死亡
<%}%>

<br/>
年龄：<%=petUser.getAge()%><br/>
等级：<%=petUser.getRank()%><br/>
经验：<%=petUser.getExp()%>/<%=action.involution(petUser.getRank())%><br/>
饥饿：<%=petUser.getHungry()%>/100<br/>
清洁：<%=petUser.getClear()%>/100<br/>
灵活；<%=petUser.getAgile()%><br/>
聪明：<%=petUser.getIntel()%><br/>
强壮：<%=petUser.getStrength()%><br/>
心情：
<%if (petUser.getFriend() <= 50) {%>
郁闷
<%} else if ((petUser.getFriend() > 50) && (petUser.getFriend() <= 80)) {%>
一般
<%} else {%>
高兴
<%}%>
<br/>
		
<br/>

<a href="<%=("/pet/index.jsp")%>">宠物</a>|<a href="/pet/shop.jsp">食堂</a>|<a href="/pet/clear.jsp">清洁</a>|<a href="/pet/work.jsp">打工</a>|<a href="/pet/hospital.jsp">医院</a>
<br/>
<a href="/pet/info.jsp">大厅</a>|<a href="/pet/game.jsp">玩耍</a>|<a href="/pet/match.jsp">比赛</a>|<a href="/pet/stakeing.jsp">赌博</a>|<a href="/Column.do?columnId=9233">帮助</a>
<br/>

<a href="/pet/buypet.jsp">领养</a>|<a href="/pet/kill.jsp">放生</a>|<a href="<%=("/pet/cut.jsp")%>">切换</a>|<a href="<%=("/pet/givepet.jsp" + "?petId=" + petUser.getId())%>">赠送</a>
<br/>
<%=BaseAction.getBottom(request, response)%>

</p>
</card>
<%}else if("nopet".equals(result))
{//没有宠物%>


<card title="宠物资料">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt=""/><br/>
<br/>

您没有宠物，是否要<a href="/pet/buypet.jsp">领养</a>一个？ <br/>
<a href="/Column.do?columnId=9233">查看宠物帮助</a><br/>
<a href="/pet/info.jsp">返回宠物大厅</a><br/>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>


<%}else if("max".equals(result)){
//达到宠物上限%>

<card title="宠物列表">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt=""/><br/>
我的宠物:<br/>
<%
Iterator iter = petList.iterator();
while(iter.hasNext()) {
petUser = (PetUserBean)iter.next();
%>
<a href="/pet/loadpet.jsp?id=<%=petUser.getId()%>"><%=StringUtil.toWml(petUser.getName())%>|<%=petUser.getRank()%>级<%=action.getPetTypeName(petUser.getType())%></a><br/>
<%}%>
<br/>
目前每人可以领养两个宠物，选择之后玩的就是被选中的那个了。<br/>
<a href="/pet/info.jsp">返回宠物大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

<%}else if("some".equals(result)){
//没有达到宠物上限%>

<card title="宠物列表">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt=""/><br/>
我的宠物:<br/>
<%
Iterator iter = petList.iterator();
while(iter.hasNext()) {
petUser = (PetUserBean)iter.next();
%>
<a href="/pet/loadpet.jsp?id=<%=petUser.getId()%>"><%=StringUtil.toWml(petUser.getName())%></a><%=petUser.getRank()%>级<%=action.getPetTypeName(petUser.getType())%><br/>
<%}%>
<br/>
<a href="/pet/info.jsp">返回宠物大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

<%}%>
</wml>