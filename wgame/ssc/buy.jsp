<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.spec.ssc.*" %><%
response.setHeader("Cache-Control","no-cache");
%><%@include file="../../bank/checkpw.jsp"%><%
//登录用户
//UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
LhcAction action = new LhcAction(request);
int type=StringUtil.toInt((String)request.getParameter("type"));
action.buy();
Date date = new Date();
LhcResultBean lhcResult =LhcWorld.bean;
int hour = date.getHours();
int termNow=lhcResult.getTerm()+1;
if (termNow > LhcWorld.dayTerm){
	if (hour < 9){
		response.sendRedirect("index.jsp");
		return;
	} else {
		termNow = 1;
	}
}
int maxBuy = LhcAction.MAX_COUNT;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%long money=action.getUserStore(); %><%if(hour < 9 || hour > 24) {%>
<card title="六时彩">
<p>您的存款:<%=StringUtil.bigNumberFormat(money)%>乐币<br/>
<%=BaseAction.getTop(request, response)%>
==第<%=termNow %>期==<br/>
每天上午9点开始到晚上9点才可以购买<br/>	
<a href="index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%} else {

%><card title="六时彩">
<p align="left"><%=BaseAction.getTop(request, response)%>
您的存款:<%=StringUtil.bigNumberFormat(money)%>乐币<br/>
==第<%=termNow%>期投注==<br/><%

if(type==LhcWorld.PING_MA){

%>

购买平码（1:7）<br/>
号码：<input name="num" maxlength="2" value="1" format="*N" /><br/>
数量(注)：<input name="moneyx" maxlength="3" value="1" format="*N" /><br/>
每注价格1亿乐币,一次最多购买<%=maxBuy%>注<br/>
<anchor title="确定">购买 
  <go href="buyResult.jsp?term=<%=termNow %>" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$moneyx"/>
    <postfield name="type" value="<%=LhcWorld.PING_MA%>"/>
  </go>
</anchor>
<%}else if(type==LhcWorld.TE_MA){%>
购买特码赔率（1:40）<br/> 
号码：<input name="num" maxlength="2" value="1" format="*N" /><br/>
数量(注)：<input name="moneyx" maxlength="3" value="1" format="*N" /><br/>
每注价格1亿乐币,一次最多购买<%=maxBuy%>注<br/>
<anchor title="确定">购买
  <go href="buyResult.jsp?term=<%=termNow %>" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$moneyx"/>
    <postfield name="type" value="<%=LhcWorld.TE_MA%>"/>
  </go>
</anchor>
<%}
//大小
else if(type==LhcWorld.DA_XIAO){%>
购买大小赔率（1:1.6）<br/>
选择：<select name="num" value="1">
    <option value="0">小</option>
     <option value="1">大</option>
</select><br/>
数量(注)：<input name="moneyx" maxlength="3" value="1" format="*N" /><br/>
每注价格1亿乐币,一次最多购买<%=maxBuy%>注<br/>
<anchor title="确定">购买
  <go href="buyResult.jsp?term=<%=termNow %>" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$moneyx"/>
    <postfield name="type" value="<%=LhcWorld.DA_XIAO%>"/>
  </go>
</anchor>
<%}
//生肖
else if(type==LhcWorld.LIU_XIAO){%>

购买生肖赔率（1:10）(牛1:9)<br/>
选择：
<select name="num" value="0">
	<%for(int i=0;i<LhcWorld.LHC_SHENG_XIAO_NAME.length;i++){%>
	    <option value="<%=i%>"><%=LhcWorld.LHC_SHENG_XIAO_NAME[i]%></option>
	<%}%>
</select><br/>
数量(注)：<input name="moneyx" maxlength="3" value="1" format="*N" /><br/>
每注价格1亿乐币,一次最多购买<%=maxBuy%>注<br/>
<anchor title="确定">购买
  <go href="buyResult.jsp?term=<%=termNow %>" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$moneyx"/>
    <postfield name="type" value="<%=LhcWorld.LIU_XIAO%>"/>
  </go>
</anchor>
<%}
//家禽野兽
else if(type==LhcWorld.JIA_QIN_YE_SHOU){%>
购买家禽野兽赔率（1:1.6）<br/>
选择：
<select name="num" value="0">
   <option value="0">家禽</option>
   <option value="1">野兽</option>
</select><br/>
数量(注)：<input name="moneyx" maxlength="3" value="1" format="*N" /><br/>
每注价格1亿乐币,一次最多购买<%=maxBuy%>注<br/>
<anchor title="确定">购买
  <go href="buyResult.jsp?term=<%=termNow %>" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$moneyx"/>
    <postfield name="type" value="<%=LhcWorld.JIA_QIN_YE_SHOU%>"/>
  </go>
</anchor>
<%}
//前后
else if(type==LhcWorld.QIAN_HOU){%>
购买前后赔率（1:1.6）<br/>
选择：
<select name="num" value="0">
   <option value="0">前</option>
   <option value="1">后</option>n>
</select><br/>
数量(注)：<input name="moneyx" maxlength="3" value="1" format="*N" /><br/>
每注价格1亿乐币,一次最多购买<%=maxBuy%>注<br/>
<anchor title="确定">购买
  <go href="buyResult.jsp?term=<%=termNow %>" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$moneyx"/>
    <postfield name="type" value="<%=LhcWorld.QIAN_HOU%>"/>
  </go>
</anchor>
<%}
//波色
else if(type==LhcWorld.BO_SE){%>
购买波色赔率（1:2.5）<br/>
选择：
<select name="num" value="0">
	<%for(int i=0;i<LhcWorld.LHC_BO_SE_NAME.length;i++){%>
	    <option value="<%=i%>"><%=LhcWorld.LHC_BO_SE_NAME[i]%></option>
	<%}%>
</select><br/>
数量(注)：<input name="moneyx" maxlength="3" value="1" format="*N" /><br/>
每注价格1亿乐币,一次最多购买<%=maxBuy%>注<br/>
<anchor title="确定">购买
  <go href="buyResult.jsp?term=<%=termNow %>" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$moneyx"/>
    <postfield name="type" value="<%=LhcWorld.BO_SE%>"/>
  </go>
</anchor>
<%}
//五行
else if(type==LhcWorld.WU_XING){%>
购买五行赔率（1:4）<br/>
选择：
<select name="num" value="0">
	<%for(int i=0;i<LhcWorld.LHC_WU_XING_NAME.length;i++){%>
	    <option value="<%=i%>"><%=LhcWorld.LHC_WU_XING_NAME[i]%></option>
	<%}%>
</select><br/>
数量(注)：<input name="moneyx" maxlength="3" value="1" format="*N" /><br/>
每注价格1亿乐币,一次最多购买<%=maxBuy%>注<br/>
<anchor title="确定">购买
  <go href="buyResult.jsp?term=<%=termNow %>" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$moneyx"/>
    <postfield name="type" value="<%=LhcWorld.WU_XING%>"/>
  </go>
</anchor>
<%}
//单双
else if(type==LhcWorld.DAN_SHUANG){%>
购买单双赔率（1:1.6）<br/>
选择：
<select name="num" value="1">
   <option value="1">单</option>
   <option value="0">双</option>
</select><br/>
数量(注)：<input name="moneyx" maxlength="3" value="1" format="*N" /><br/>
每注价格1亿乐币,一次最多购买<%=maxBuy%>注<br/>
<anchor title="确定">购买
  <go href="buyResult.jsp?term=<%=termNow %>" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$moneyx"/>
    <postfield name="type" value="<%=LhcWorld.DAN_SHUANG%>"/>
  </go>
</anchor>

<%}else{
out.clearBuffer();
response.sendRedirect("index.jsp");
return;
}
%><a href="index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p></card><%
}%>
</wml>