<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.charitarian.CharitarianAction" %><%@ page import="net.joycool.wap.bean.charitarian.CharitarianHistoryBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
CharitarianAction action = new CharitarianAction(request);
action.notice(request);
//取得登陆用户信息
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
CharitarianHistoryBean charitarianHistory =(CharitarianHistoryBean)request.getAttribute("charitarianHistory");
String url = "/charitarian/index.jsp";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="慈善基金"  ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(charitarianHistory!=null){
UserBean user = UserInfoUtil.getUser(charitarianHistory.getCharitarianId());
	if(user!=null){
	%>
	乐酷慈善天使微笑道：亲爱的<%=StringUtil.toWml(loginUser.getNickName())%>，感谢您来到乐酷，乐币没有了吗？乐酷慈善家<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><%=user.getGender() == 1? "先生" : "女士"%>，捐献给您<%= Constants.CHARITARIAN_USER_MONEY %>乐币。祝您玩的开心！也希望您有钱之后，去慈善基金多捐献些爱心：）所有捐赠都在那里有记录的：）（5秒钟跳转）<br/>
	<%}
}else{%>
参数错误<br/>
<%}%>
<a href="/charitarian/index.jsp">直接返回</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>