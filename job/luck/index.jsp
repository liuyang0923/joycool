<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
Calendar cal=Calendar.getInstance();
session.setAttribute("luck","go");
String tip=(String)request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="今日运势">
<p align="left"><%=BaseAction.getTop(request, response)%>
<%if(tip!=null){%><%=tip%><br/><%}%>
今日运程<br/>
<%if(loginUser!=null){%><%=StringUtil.toWml(loginUser.getNickName())%>,<%}%>今天是<br/>
<%=cal.get(Calendar.YEAR)%>年<%=cal.get(Calendar.MONTH)+1%>月<%=cal.get(Calendar.DAY_OF_MONTH)%>日<br/>
运势如何？<br/>
老天保佑你吧!<br/><br/>
选择我的星座:<br/>
<select name="yunshi" >
<option value="1">白羊座</option>
<option value="2">金牛座</option>
<option value="3">双子座</option>
<option value="4">巨蟹座</option>
<option value="5">狮子座</option>
<option value="6">处女座</option>
<option value="7">天秤座</option>
<option value="8">天蝎座</option>
<option value="9">射手座</option>
<option value="10">摩羯座</option>
<option value="11">水瓶座</option>
<option value="12">双鱼座</option>
</select>&nbsp;&nbsp;
<anchor >看今天运势
<go href="divining.jsp" method="post">
<postfield name="constellation" value="$yunshi"/>
</go>
</anchor><br/><br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="http://wap.joycool.net">乐酷免费门户首页</a><br/>
</p>
</card>
</wml>