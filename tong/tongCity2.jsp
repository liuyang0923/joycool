<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.Tong4Action"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%!
static String[] effectString = {"能力值增加1","效果增加1倍","能力值增加2","效果增加1","效果变为3","效果增加2倍","效果变为1"};
//static String[] choiceString={"(A)","(B)","(C)","(D)"};
//static String[] choiceString2={"A","B","C","D"};
static String[] choiceStringx={"(冬)","(喃)","(夕)","(贝)"};
static String[] choiceString={"(.西)","(北)","(.东)","(南)",};
static String[] choiceString2={"西","北","东","南",};
static String[] sep = {":","：","-","－"};
%><%
response.setHeader("Cache-Control","no-cache");/*
String ip=request.getRemoteAddr();
String ua = request.getHeader("User-Agent");
if(ip.startsWith("211.136.228.24")&&ua!=null&&ua.indexOf("Windows")!=-1){
	response.sendRedirect(("/lswjs/index.jsp"));
	return;
}*/
Tong4Action action = new Tong4Action(request);
action.tongCity(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
Tong4Action.Tong2User t2u = action.getUser2();
int rand = net.joycool.wap.util.RandomUtil.seqInt(4);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("tongList.jsp");%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
long now = System.currentTimeMillis();
TongBean tong = (TongBean)request.getAttribute("tong");
String mark = request.getParameter("mark");
if(mark==null){
	mark = "d";
}
String addurl = "&amp;"+net.joycool.wap.util.RandomUtil.seqInt(10);
%>
<card title="<%=StringUtil.toWml(tong.getTitle())%>" >
<p align="left">
<%=loginUser.showImg("/img/tong/city/"+action.tongCityImage(tong)+".gif")%>
<%=BaseAction.getTop(request, response)%>
<%	String[] choiceS; if(true||net.joycool.wap.util.LogUtil.getLogUser()!=loginUser.getId()) choiceS = choiceString; else choiceS = choiceStringx; 
if(rand%2==0){%><%if(mark.equals("d")){%>破坏<%}else{%>加固<%}%><%}%>城门<%=sep[rand]%><%if(rand%2==0){%><%=choiceS[t2u.choice]%><%}else{%><%=choiceS[t2u.choice]%><%}%><%if(rand/2==0){%><br/><%}%>
[获得:<%=effectString[t2u.effect]%><%if(rand/2!=0){%>]<%}else{%>]<%}%><br/>
　<a href="tongCityResult.jsp?mark=<%=mark%>&amp;c=1&amp;tongId=<%=tong.getId()%><%=addurl%>"><%=choiceString2[1]%>城门</a><br/>
<a href="tongCityResult.jsp?mark=<%=mark%>&amp;c=0&amp;tongId=<%=tong.getId()%><%=addurl%>"><%=choiceString2[0]%>城门</a>|
<a href="tongCityResult.jsp?mark=<%=mark%>&amp;c=2&amp;tongId=<%=tong.getId()%><%=addurl%>"><%=choiceString2[2]%>城门</a><br/>
　<a href="tongCityResult.jsp?mark=<%=mark%>&amp;c=3&amp;tongId=<%=tong.getId()%><%=addurl%>"><%=choiceString2[3]%>城门</a><br/>

<%if(mark.equals("d")){%>
<%if(tong.getTongAssaultTime().getTime()+1000*60*30-now>=0){%>(士气上升)<br/><%}%>
<%}else{%>
<%if(tong.getTongRecoveryTime().getTime()+1000*60*30-now>=0){%>(士气上升)<br/><%}%>
<%}%>
耐久度<%=tong.getNowEndure()%>/<%=tong.getHighEndure()%><br/>
当前能力值:<%=t2u.level%>(能力值越高,加固/破坏的效果越好)<br/>
==========<br/>
<a href="tongCity.jsp?tongId=<%=tong.getId()%>">返回城墙</a><br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
</p>
</card>
<%}%></wml>
