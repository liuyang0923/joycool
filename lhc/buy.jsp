<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.lhc.LhcAction" %><%@ page import="net.joycool.wap.action.lhc.LhcWorld" %><%
response.setHeader("Cache-Control","no-cache");
//登录用户
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
LhcAction action = new LhcAction(request);
action.buy();
int type=StringUtil.toInt((String)request.getAttribute("type"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%--
<%//平码
if(type==LhcWorld.PING_MA){%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买平码（1:7）<br/>
号码：<input name="num"  maxlength="2" value="1" format="*N" /><br/>
金额：<input name="money"  maxlength="9" value="10000" format="*N" /><br/>
<anchor title="确定">购买 
  <go href="buyResult.jsp" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$money"/>
    <postfield name="type" value="<%=LhcWorld.PING_MA%>"/>
  </go>
</anchor>
<a href="/lhc/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}//特码
else --%>
<%if(type==LhcWorld.TE_MA){%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买特码赔率（1:40）<br/> 
号码：<input name="num"  maxlength="2" value="1" format="*N" /><br/>
金额：<input name="money"  maxlength="10" value="10000" format="*N" /><br/>
<anchor title="确定">购买
  <go href="buyResult.jsp" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$money"/>
    <postfield name="type" value="<%=LhcWorld.TE_MA%>"/>
  </go>
</anchor>
<a href="/lhc/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
//大小
else if(type==LhcWorld.DA_XIAO){%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买大小赔率（1:1.6）<br/>
选择：<select name="num" value="1">
    <option value="0">小</option>
     <option value="1">大</option>
</select><br/>
金额：<input name="money"  maxlength="10" value="10000" format="*N" /><br/>
<anchor title="确定">购买
  <go href="buyResult.jsp" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$money"/>
    <postfield name="type" value="<%=LhcWorld.DA_XIAO%>"/>
  </go>
</anchor>
<a href="/lhc/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
//六肖
else if(type==LhcWorld.LIU_XIAO){%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买生肖赔率（1:10）<br/>
选择：
<select name="num" value="0">
	<%for(int i=0;i<LhcWorld.LHC_SHENG_XIAO_NAME.length;i++){%>
	    <option value="<%=i%>"><%=LhcWorld.LHC_SHENG_XIAO_NAME[i]%></option>
	<%}%>
</select><br/>
金额：<input name="money"  maxlength="10" value="10000" format="*N" /><br/>
<anchor title="确定">购买
  <go href="buyResult.jsp" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$money"/>
    <postfield name="type" value="<%=LhcWorld.LIU_XIAO%>"/>
  </go>
</anchor>
<a href="/lhc/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
//家禽野兽
else if(type==LhcWorld.JIA_QIN_YE_SHOU){%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买家禽野兽赔率（1:1.6）<br/>
选择：
<select name="num" value="0">
   <option value="0">家禽</option>
   <option value="1">野兽</option>
</select><br/>
金额：<input name="money"  maxlength="10" value="10000" format="*N" /><br/>
<anchor title="确定">购买
  <go href="buyResult.jsp" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$money"/>
    <postfield name="type" value="<%=LhcWorld.JIA_QIN_YE_SHOU%>"/>
  </go>
</anchor>
<a href="/lhc/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
//前后
else if(type==LhcWorld.QIAN_HOU){%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买前后赔率（1:1.6）<br/>
选择：
<select name="num" value="0">
   <option value="0">前</option>
   <option value="1">后</option>n>
</select><br/>
金额：<input name="money"  maxlength="10" value="10000" format="*N" /><br/>
<anchor title="确定">购买
  <go href="buyResult.jsp" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$money"/>
    <postfield name="type" value="<%=LhcWorld.QIAN_HOU%>"/>
  </go>
</anchor>
<a href="/lhc/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
//波色
else if(type==LhcWorld.BO_SE){%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买波色赔率（1:2.5）<br/>
选择：
<select name="num" value="0">
	<%for(int i=0;i<LhcWorld.LHC_BO_SE_NAME.length;i++){%>
	    <option value="<%=i%>"><%=LhcWorld.LHC_BO_SE_NAME[i]%></option>
	<%}%>
</select><br/>
金额：<input name="money"  maxlength="10" value="10000" format="*N" /><br/>
<anchor title="确定">购买
  <go href="buyResult.jsp" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$money"/>
    <postfield name="type" value="<%=LhcWorld.BO_SE%>"/>
  </go>
</anchor>
<a href="/lhc/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
//五行
else if(type==LhcWorld.WU_XING){%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买五行赔率（1:4）<br/>
选择：
<select name="num" value="0">
	<%for(int i=0;i<LhcWorld.LHC_WU_XING_NAME.length;i++){%>
	    <option value="<%=i%>"><%=LhcWorld.LHC_WU_XING_NAME[i]%></option>
	<%}%>
</select><br/>
金额：<input name="money"  maxlength="10" value="10000" format="*N" /><br/>
<anchor title="确定">购买
  <go href="buyResult.jsp" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$money"/>
    <postfield name="type" value="<%=LhcWorld.WU_XING%>"/>
  </go>
</anchor>
<a href="/lhc/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
//单双
else if(type==LhcWorld.DAN_SHUANG){%>
<card title="<%=LhcAction.LHC_NAME%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
购买单双赔率（1:1.6）<br/>
选择：
<select name="num" value="1">
   <option value="1">单</option>
   <option value="0">双</option>
</select><br/>
金额：<input name="money"  maxlength="10" value="10000" format="*N" /><br/>
<anchor title="确定">购买
  <go href="buyResult.jsp" method="post">
    <postfield name="num" value="$num"/>
    <postfield name="money" value="$money"/>
    <postfield name="type" value="<%=LhcWorld.DAN_SHUANG%>"/>
  </go>
</anchor>
<a href="/lhc/index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
out.clearBuffer();
BaseAction.sendRedirect("/lhc/index.jsp",response);
return;
}%>
</wml>