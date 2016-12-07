<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.charitarian.CharitarianAction" %><%@ page  import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%
response.setHeader("Cache-Control","no-cache");
CharitarianAction action = new CharitarianAction(request);
//if(action.getLoginUser()==null){
//	response.sendRedirect("/user/login.jsp?backTo=/charitarian/index.jsp");
//	return;
//}
action.index(request);
String money=(String)request.getAttribute("money");
String count=(String)request.getAttribute("count");
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="慈善基金">
<p align="left">
<img src="../img/charitarian.gif" alt="慈善基金"/><br/>
<%=BaseAction.getTop(request, response)%>
<%if (loginUser != null){%>乐酷市长向您鞠躬道：尊敬的<%=StringUtil.toWml(loginUser.getNickName())%><%=loginUser.getGender() == 1? "先生":"女士"%>，乐酷慈善基金，是为了帮助没有钱的乐酷新手而设置的。您只要捐献一点点乐币，就可以让很多新手留在乐酷，成为您的朋友哦！<%	}%>
您曾经捐献过<%=(money==null)?0:money%>乐币，救济过<%=(count==null)?0:count%>个新用户。<br/>
您这次要捐献:
<input name="count"  maxlength="11" value="1"/>份
<anchor title="确定">确定
<go href="result.jsp" method="post">
<postfield name="count" value="$count"/>
</go>
</anchor><br/>
(注:一份<%= Constants.CHARITARIAN_USER_MONEY %>乐币,一次最少捐助1份,最多捐助20万份)<br/>
<a href="/charitarian/history.jsp">查看您的慈善账户</a><br/>
<a href="/charitarian/rule.jsp">慈善基金使用规则</a><br/>
<a href="/top/charitarianTop.jsp">查看慈善排行榜</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>