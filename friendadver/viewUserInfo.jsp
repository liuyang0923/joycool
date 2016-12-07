<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.user.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
String backTo=request.getParameter("backTo");
if(backTo==null||backTo.equals("")) backTo="/lswjs/index.jsp";
backTo = backTo.replace("&", "&amp;");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="个人资料">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(backTo == null){
	backTo = BaseAction.INDEX_URL;
}
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean user=(UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
%>
<% String tip=(String)request.getAttribute("tip"); if(tip!=null){%><%=tip%><br/><%}%>
交友信息发布成功！您的个人资料如下，建议您修改以增加交友成功率！<a href="friendAdverIndex.jsp" title="进入">返回交友中心</a><br/>
<br/>
年龄:<br/><input format="*N" name="nianling" value="<%=loginUser.getAge()%>"/><br/>
自我介绍：<br/><input name="selfIntroduction"  maxlength="100" value="<%=loginUser.getSelfIntroduction()%>"/>
<br/>

<br/>
    <anchor title="确定">修改
    <go href="/user/UserInfo.do?tag=1&amp;backTo=<%=backTo%>" method="post">
	<postfield name="age" value="$nianling"/>
	<postfield name="selfIntroduction" value="$selfIntroduction"/>
    </go>
    </anchor>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>