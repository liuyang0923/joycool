<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
StakeAction action = new StakeAction(request);
action.stakeadd();
String result = (String)request.getAttribute("result");
String url = (String)request.getAttribute("url");
if(url==null)
	url = "/pet/match.jsp";
String tip = (String)request.getAttribute("tip");
url = (url);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赌博投注赛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if("failure".equals(result)) {%>

<%=tip%><br/>


<%}else{
PetUserBean petBean = (PetUserBean)request.getAttribute("petBean");
MatchStakeBean stakeBean = (MatchStakeBean)request.getAttribute("stakeBean");
int order = ((Integer) request.getAttribute("order")).intValue();
long onestake = ((Long) request.getAttribute("onestake")).longValue();
int oneCount = ((Integer) request.getAttribute("oneCount")).intValue();
MatchRunBean matchrunbean = (MatchRunBean)request.getAttribute("matchrunbean");
MatchUserBean matchUserBean = (MatchUserBean)request.getAttribute("matchUserBean");
%>
<a href="/pet/viewpet.jsp?id=<%=petBean.getId()%>">
<%=StringUtil.toWml(petBean.getName())%>
</a>
(<%=action.getPetTypeName(petBean.getType())%>
<%=petBean.getRank()%>级)<br/>
目前赔率:1:<%=StakeAction.getStake(petBean.getId())%><br/>
<%=oneCount%>人共下注<%=onestake%>乐币<br/>
昨日积分:<%=matchUserBean.getYesterday()%><br/>
今日冠军:<%=matchUserBean.getWintime()%>次<br/>
今日获得的总奖金:<%=matchUserBean.getTotalstake()%>乐币<br/>
你对它的赌注
<%if(stakeBean != null) {%>
<%=stakeBean.getStake()%>
<%}else{%>
0
<%}%>
乐币<br/>
您有<%=statusBeans.getGamePoint()%>乐币<br/>
对他下注:
<br/><input name="idd"  maxlength="8" format="*N" value=""/>  
<br/>
    <anchor title="下注">下注
    <go href="/pet/stakeresult.jsp?id=<%=matchrunbean.getId()%>&amp;order=<%=order%>" method="post">
    <postfield name="number" value="$idd"/>
    </go>
    </anchor>
<br/>
<%}%>
<a href="/pet/stakeing.jsp">返回赌博投注赛</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>