<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.bank();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花钱庄">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/><br/>
<a href="bank.jsp">返回钱庄</a><br/>

<%}else{
long saving = BankCacheUtil.getStoreMoney(action.getLoginUser().getId());
int money = action.getUser().getMoney();
%>
===桃花钱庄<a href="help/bank.jsp">??</a>===<br/>
您现有存款<%=StringUtil.bigNumberFormat(saving)%>乐币，携带<%=FarmWorld.formatMoney(money)%>。<br/>
一个铜板等于一千乐币<br/>
最多只能取<%=FarmAction.highMoney%>铜板<br/>
<input type="text" name="mn2" format="*N" emptyok="false" value="50" maxlength="5"></input>铜板<br/>
<anchor title="ok">确认取款
<go href="bank.jsp" method="post">
<postfield name="money" value="$mn2"/>
<postfield name="a" value="1"/>
</go>
</anchor>|
<anchor title="ok">确认存款
<go href="bank.jsp" method="post">
<postfield name="money" value="$mn2"/>
<postfield name="a" value="2"/>
</go>
</anchor>
<br/>单次存款不得超过9个金块，不得少于1个金块
<%}%><br/>
<a href="map.jsp">返回场景</a>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>