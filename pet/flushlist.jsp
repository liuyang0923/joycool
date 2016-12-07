<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="比赛人员总表">
<p align="left">
<%
int i = 0;
Iterator iter = PetAction.userMap.values().iterator();
while(iter.hasNext()){
PetUserBean pet = (PetUserBean)iter.next();
if(pet.getMatchid() > 0){
MatchRunBean match = (MatchRunBean)PetAction.matchMap[pet.getMatchtype()].get(Integer.valueOf(pet.getMatchid()));
%>
<%=++i%>.<a href="runing.jsp?id=<%=pet.getMatchid()%>&amp;type=<%=pet.getMatchtype()%>"><%=StringUtil.toWml(pet.getName())%>(<%=pet.getUser_id()%>)</a>--<a href="flush.jsp?id=<%=pet.getId()%>">flush</a>--<%=match.getCondition()%><br/>
<%}}%>
</p>
</card>
</wml>