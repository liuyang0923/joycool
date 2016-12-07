<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%
if(net.joycool.wap.action.bank.BankAction.chenkUserBankPW(session)){
response.sendRedirect(("http://wap.joycool.net/bank/bankPWCheck.jsp?backTo=" +
	java.net.URLEncoder.encode(net.joycool.wap.util.StringUtil.toWml(net.joycool.wap.util.PageUtil.getCurrentPageURL(request)))));
return;
}
%>