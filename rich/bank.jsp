<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.bank();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInBank()){	 //离开银行
response.sendRedirect(("go.jsp"));
return; }
int act = action.getParameterInt("a");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(act==0){	//
 %>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
金币:<%=richUser.getMoney()%><br/>
存款:<%=richUser.getSaving()%><br/>
<input name="save" format="*N" maxlength="9" value=""/>
<anchor title="确定">存
  <go href="bank.jsp?a=1" method="post">
    <postfield name="save" value="$save"/>
  </go>
</anchor><br/>
<input name="load" format="*N" maxlength="9" value=""/>
<anchor title="确定">取
  <go href="bank.jsp?a=1" method="post">
    <postfield name="load" value="$load"/>
  </go>
</anchor><br/>
<a href="bank.jsp?a=2">离开银行</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else{%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="bank.jsp">继续存款</a><br/>
<a href="bank.jsp?a=2">离开银行</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>