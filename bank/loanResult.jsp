<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.bank.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.bank.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("success")==null){
//response.sendRedirect("/bank/loan.jsp");
BaseAction.sendRedirect("/bank/loan.jsp", response);
return;
}
BankAction bankAction=new BankAction(request);
bankAction.loanResult(request);
String result=(String)request.getAttribute("result");
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
if("saving".equals(result)){
//response.sendRedirect("/bank/loan.jsp");
BaseAction.sendRedirect("/bank/loan.jsp", response);
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%//判断用户有存款
if("overstep".equals(result)){
//存款金额
String money=(String)request.getAttribute("money");
%> 
<card title="用户有存款">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：对不起,您当钱的乐币<%=us.getGamePoint()%>大于100000000,不能贷款,您还想要什么服务？<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%//判断用户有贷款
}else if("failure".equals(result)){
String money=(String)request.getAttribute("money");
String hour=(String)request.getAttribute("hour");
String mark=(String)request.getAttribute("mark");
%>
<card title="贷款失败">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(mark.equals("1")){
String isCanLoan=(String)request.getAttribute("isCanLoan");
%>
银行经理：贷款失败！输入数目过大！根据您的等级，您最多还能贷款<%=isCanLoan%>元。贷款时间最多72小时。<br/>
<%}else{
String canLoadMoney=(String)request.getAttribute("canLoadMoney");
long canLoadMoney1=Long.parseLong(canLoadMoney);
canLoadMoney1=(long)(canLoadMoney1*0.1);
%>
银行经理：贷款失败！输入数目过小！根据您的等级，您每次贷款的最小金额为<%=canLoadMoney1%>元，最小时间为1小时。<br/>
<%}%>
重新填写贷款？<br/>
<input type="text" name="money" format="*N" maxlength="10" value="<%=money%>"/>乐币<br/>                 
时间（不能超过72小时）<br/>
<input type="text" name="hour" format="*N" maxlength="2" value="<%=hour%>"/>小时<br/>
<anchor title="post">确定贷款
  <go href="loanResult.jsp" method="post">
    <postfield name="money" value="$(money)"/>
    <postfield name="hour" value="$(hour)"/>
  </go>
</anchor><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%//判断用户输入是否有效
}else if("input".equals(result)){
String money=(String)request.getAttribute("money");
String hour=(String)request.getAttribute("hour");
%>
<card title="输入无效">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：贷款失败！输入字符无效！<br/>
所填写的存款数目应是1—210 000 000之间的自然数。<br/>
贷款时间为1—72之间的自然数<br/>
重新填写贷款？<br/>
<input type="text" name="money" format="*N" maxlength="10" value="<%=money%>"/>乐币<br/>                 
时间（不能超过72小时）<br/>
<input type="text" name="hour" format="*N" maxlength="2" value="<%=hour%>"/>小时<br/>
<anchor title="post">确定贷款
  <go href="loanResult.jsp" method="post">
    <postfield name="money" value="$(money)"/>
    <postfield name="hour" value="$(hour)"/>
  </go>
</anchor><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%
}else{
session.removeAttribute("success");
Vector loadMoneyList=(Vector)request.getAttribute("loadMoneyList");
%>
<card title="贷款成功">
<p align="left">
<%=BaseAction.getTop(request, response)%>
银行经理：贷款成功！您现在乐币<%=us.getGamePoint()%>,当前贷款项目如下：<br/>
(归还倒计时)  (款额)<br/>
<%
LoadBean load=null;
for(int i=0;i<loadMoneyList.size();i++){
   load=(LoadBean)loadMoneyList.get(i);%>
<%=bankAction.getLeaveTime(load.getCurrentTime(),load.getExpireTime())+" "%><%=load.getMoney()+"乐币"%><br/>
<%}%><br/>
您还要什么服务？<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>