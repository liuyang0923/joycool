<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
StakeAction action = new StakeAction(request);
action.stakeing();
String tip = (String)request.getAttribute("tip");
String result = (String)request.getAttribute("result");
PetUserBean petUser = (PetUserBean)request.getAttribute("petUser");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
MatchRunBean matchrunbean = (MatchRunBean)request.getAttribute("matchrunbean");
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
Vector stakeList = (Vector) request.getAttribute("stakeList");
long totalstake = 0;
int totalCount = 0;
String url=("/pet/index.jsp");


int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
Vector vector = (Vector) request.getAttribute("vector");
int totalHallPageCount = ((Integer) request.getAttribute("totalHallPageCount")).intValue();

String refreshUrl;

//刷新
if(matchrunbean != null){
refreshUrl = "/pet/stakeing.jsp?id="+matchrunbean.getId()+"&amp;type="+matchrunbean.getType();
totalstake = ((Long) request.getAttribute("totalstake")).longValue();
totalCount = ((Integer) request.getAttribute("totalCount")).intValue();
}else{
refreshUrl = "/pet/stakeing.jsp?type=3";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>



<% if("wait".equals(result)) {%>
<% if(matchrunbean.getCondition() == 0) {%>
<card title="赌博投注赛" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
==目前的比赛==<br/>
<%=matchrunbean.getMessage()%><br/>
已有<%=totalCount%>人总赌注<%=totalstake%>乐币<br/>
<%if(stakeList != null) {%>
您已下注:<br/>
			<%for(int i=0;i<stakeList.size();i++){
			MatchStakeBean petStake = (MatchStakeBean)stakeList.get(i);
			%>
<%=petStake.getStake()%>乐币

<%=StringUtil.toWml(petStake.getPet_name())%><br/>
			<%}%>
<%}%>
您有<%=statusBeans.getGamePoint()%>乐币<br/>
==运动员名单==<br/>
运动员|目前赔率<br/>
<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.STAKE_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<a href="/pet/viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>|1:<%=StakeAction.getStake(playbean[i].getPetid())%>|
<a href="/pet/stakeadd.jsp?order=<%=i%>&amp;id=<%=matchrunbean.getId()%>">下注</a>
<br/>
<%}}}%>
<br/>

==已结束的比赛==<br/>
<%
			for(int i=vector.size()-1;i>=0;i--){
			MatchRunBean matchrunbeanTemp = (MatchRunBean)vector.get(i);
if(matchrunbeanTemp.getCondition() != 0) {%>
<%=matchrunbeanTemp.getMessage()%>
总赌注<%=matchrunbeanTemp.getTotalstake()%>乐币
<a href="/pet/stakeing.jsp?type=<%=matchrunbeanTemp.getType()%>&amp;id=<%=matchrunbeanTemp.getId()%>">查看结果</a><br/>
<%}%>
<%}%>
<%
String fenye = PageUtil.shuzifenye(totalHallPageCount, pageIndex, refreshUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
}
%>
==投注赛规则==<br/>
1、参赛选手: 每轮比赛的前3名选手的主人依次获得总赌注5％、3％、2％的奖金。 <br/>
2、下注人: 按照自己下注金额占对该选手下注总金额的比例，分得90％的总赌注（结果精确到1乐币）。
 比如你下注“飞天龟“1万乐币,飞天龟的总注为10万，8位选手加起来的赌注有100万。那么当飞天龟跑了第一名你就能获得（1/10)x(100x90%)=9万；如果飞天龟不跑第一就没得赚啦。<br/>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>






<%} else if(matchrunbean.getCondition() == 1) {%>

<card title="赌博投注赛" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.STAKE_PLAYNUMBER;i++){
			if(i > 2)//只显示前3名
		     	break;
			if(playbean[i] != null)
			{
%>
第<%=i+1%>名:<a href="/pet/viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>
到达<%if(playbean[i].getPosition() >= action.TOTAL_LONG){%>终点<%}else{%><%=playbean[i].getPosition()%>米
<%}%>
<br/>
<%}}}%>
==比赛花絮==<br/>
<%=matchrunbean.toString()%>
==实时成绩==<br/>
<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.STAKE_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<%=i+1%>.
<a href="/pet/viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>
到达<%if(playbean[i].getPosition() >= action.TOTAL_LONG){%>终点.<%}else{%><%=playbean[i].getPosition()%>米
<%}%>
<br/>
<%}}}%>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>






<%} else if(matchrunbean.getCondition() == 2) {%>

<card title="赌博投注赛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(matchrunbean != null){
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.STAKE_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<%if((petUser != null)&&(petUser.getId() == playbean[i].getPetid())){%>
<%=StringUtil.toWml(playbean[i].getName())%>最终排名第:<%=i+1%><br/>
<%}}}}%>
<%=matchrunbean.getMessage()%><br/>
<%=matchrunbean.getTotalpeople()%>人总赌注<%=matchrunbean.getTotalstake()%>乐币<br/>
冠军赔率1:<%=matchrunbean.getWinrate()%><br/>
<%if(matchrunbean.getNoticMap().get(new Integer(loginUser.getId())) != null){%>
您的总赌注是:<%=(((Long)matchrunbean.getNoticMap().get(new Integer(loginUser.getId()))).longValue())%><br/>
您的奖金是:
<%if(matchrunbean.getWinMap().get(new Integer(loginUser.getId())) != null){%>
<%=(((Long)matchrunbean.getWinMap().get(new Integer(loginUser.getId()))).longValue())%>
<%}else{%>
0
<%}%>
乐币<br/>
<%}%>
==比赛成绩==<br/>
<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.STAKE_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
第<%=i+1%>名:<a href="/pet/viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>
<%if(i == 0){%>
<br/>奖金:<%=matchrunbean.getWin1()%>乐币
<%}else if(i == 1){%>
<br/>奖金:<%=matchrunbean.getWin2()%>乐币
<%} else if(i == 2){%>
<br/>奖金:<%=matchrunbean.getWin3()%>乐币
<%}%>

<br/>
<%}}}%>
<br/>
下注盈利前5名:<br/>
<%=matchrunbean.getWinner()%>
<a href="/pet/stakeing.jsp">返回赌博投注赛</a><br/>
</p>
</card>
<%}%>




<%}else {%>
<card title="赌博投注赛" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>

==已结束的比赛==<br/>
<%
			for(int i=vector.size()-1;i>=0;i--){
			MatchRunBean matchrunbeanTemp = (MatchRunBean)vector.get(i);
if(matchrunbeanTemp.getCondition() != 0) {%>
<%=matchrunbeanTemp.getMessage()%>
总赌注<%=matchrunbeanTemp.getTotalstake()%>乐币
<a href="/pet/stakeing.jsp?type=<%=matchrunbeanTemp.getType()%>&amp;id=<%=matchrunbeanTemp.getId()%>">查看结果</a>
<%}%>
<br/>
<%}%>
<%
String fenye = PageUtil.shuzifenye(totalHallPageCount, pageIndex, refreshUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%}%>
<%if(vector.size() <= 0){%>
尚无比赛<br/>
<%}%>
==投注赛规则==<br/>
1、参赛选手: 每轮比赛的前3名选手的主人依次获得总赌注5％、3％、2％的奖金。 <br/>
2、下注人: 按照自己下注金额占对该选手下注总金额的比例，分得90％的总赌注（结果精确到1乐币）。
 比如你下注“飞天龟“1万乐币,飞天龟的总注为10万，8位选手加起来的赌注有100万。那么当飞天龟跑了第一名你就能获得（1/10)x(100x90%)=9万；如果飞天龟不跑第一就没得赚啦。<br/>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>