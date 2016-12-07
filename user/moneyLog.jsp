<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.user.UserMoneyLogAction" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.bank.MoneyLogBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%
response.setHeader("Cache-Control","no-cache");
UserMoneyLogAction action  = new UserMoneyLogAction(request);
action.getMoneyLogList2();
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
List MoneyLogList=(List)request.getAttribute("MoneyLogList");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="转帐交易记录">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(MoneyLogList!=null){
for(int i=0;i<MoneyLogList.size();i++){
MoneyLogBean moneyLog=(MoneyLogBean)MoneyLogList.get(i);
UserBean toUser = UserInfoUtil.getUser(moneyLog.getRUserId());
if(toUser==null)continue;
if(moneyLog.getMoney()<0){
%>
<%=moneyLog.getTime().substring(5,16)%>
转给<a href="/chat/post.jsp?toUserId=<%=toUser.getId()%>"><%=StringUtil.toWml(toUser.getNickName())%></a>
<%=StringUtil.bigNumberFormat(-moneyLog.getMoney())%>乐币<br/>
<%}else{%>
<%=moneyLog.getTime().substring(5,16)%>
收到<a href="/chat/post.jsp?toUserId=<%=toUser.getId()%>"><%=StringUtil.toWml(toUser.getNickName())%></a>
<%=StringUtil.bigNumberFormat(moneyLog.getMoney())%>乐币<br/>
<%}}}%>
<%=PageUtil.shuzifenye(pagingBean, "moneyLog.jsp", false, "|", response)%>
注意:转帐记录有时候会有延迟,最新的转帐记录如果未显示,请等待几分钟再试.<br/>
<a href="/bank/bank.jsp">返回银行</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>