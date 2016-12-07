<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.user.UserMoneyLogAction" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserMoneyLogBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
UserMoneyLogAction action  = new UserMoneyLogAction(request);
action.getUserMoneyReceiveList();
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List userMoneyLogList=(List)request.getAttribute("userMoneyLogList");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="接受乐币记录">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
int j = 1;
for(int i=0;i<userMoneyLogList.size();i++){
UserMoneyLogBean userMoneyLog=(UserMoneyLogBean)userMoneyLogList.get(i);
UserBean fromUser = UserInfoUtil.getUser(userMoneyLog.getFromId());
if(fromUser==null)continue;
%>
<%=userMoneyLog.getCreateDatetime().substring(5,16)%>
收到<a href="/chat/post.jsp?toUserId=<%=fromUser.getId()%>"><%= StringUtil.toWml(fromUser.getNickName()) %></a>
<%=StringUtil.bigNumberFormat(userMoneyLog.getMoney())%>乐币<br/>
<%j++;}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|",response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/bank/bank.jsp">返回银行</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>