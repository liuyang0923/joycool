<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.runIndex();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
PetUserBean petUser = (PetUserBean)request.getAttribute("petUser");
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
String url=("/pet/index.jsp");

int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
Vector vector = (Vector) request.getAttribute("vector");
String prefixUrl = (String) request.getAttribute("prefixUrl");
int totalHallPageCount = ((Integer) request.getAttribute("totalHallPageCount")).intValue();

//刷新
String refreshUrl = StringUtil.getUniqueURL(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="趣味赛跑"  ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(petUser != null){
if(petUser.getMatchid() == 0) {%>
<a href="/pet/matchact.jsp?task=1&amp;type=1">新开一局比赛</a><br/>
<%}else{%>
<a href="/pet/mymatch.jsp">我的比赛</a><br/>
<%}}%>
==目前的比赛==<br/>
<%
			for(int i=vector.size()-1;i>=0;i--){
			MatchRunBean matchrunbean = (MatchRunBean)vector.get(i);
			if(matchrunbean.getCondition() == 0){
%>
等待中...
共<%=matchrunbean.getPeoplenumber()%>玩家
<%if((petUser != null)&&(petUser.getMatchid() == 0)){%>
<a href="/pet/matchact.jsp?task=2&amp;type=<%=matchrunbean.getType()%>&amp;id=<%=matchrunbean.getId()%>">加入比赛</a>
<%}%>
<br/>
<%}else if(matchrunbean.getCondition() == 1){
%>
游戏中...
共<%=matchrunbean.getPeoplenumber()%>玩家
<%if(matchrunbean.getCondition() != 0) {%>
<a href="/pet/runing.jsp?task=4&amp;type=<%=matchrunbean.getType()%>&amp;id=<%=matchrunbean.getId()%>">观看比赛</a>
<%}%>
<br/>
<%
}else{%>
已结束...
共<%=matchrunbean.getPeoplenumber()%>玩家
<%if(matchrunbean.getCondition() != 0) {%>
<a href="/pet/runing.jsp?task=4&amp;type=<%=matchrunbean.getType()%>&amp;id=<%=matchrunbean.getId()%>">查看结果</a>
<%}%>
<br/>
<%}}%>
<%
String fenye = PageUtil.shuzifenye(totalHallPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%
}
%>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>

</wml>