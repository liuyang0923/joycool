<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HandbookingerAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.LoadResource" %><%
response.setHeader("Cache-Control","no-cache");
HandbookingerAction action=new HandbookingerAction(request);%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="下注页面">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%int horseId=StringUtil.toInt(request.getParameter("horseName"));
  String horseName=(String)LoadResource.getHorse().get(request.getParameter("horseName"));
  if(horseName==null){
  out.clearBuffer();
  response.sendRedirect("index.jsp");
   return;
  }
  int compensateName=StringUtil.toInt(request.getParameter("compensateName"));
  String id=request.getParameter("id");
  UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
  UserStatusBean status=(UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
  session.setAttribute("chipIn","true");%>
您选择了<%=horseName%>！<a href="/job/handbookinger/index.jsp">重选</a><br/>
您现有乐币<%=status.getGamePoint()%>，<br/>
请下注<br/>
<input name="chipIn"  maxlength="100" value="100"/><br/>
<anchor title="确定">确定
    <go href="/job/handbookinger/jump.jsp" method="post">
      <postfield name="money" value="$chipIn"/>
      <postfield name="horseId" value="<%=horseId%>"/>
      <postfield name="compensateName" value="<%=compensateName%>"/>
      <postfield name="id" value="<%=id%>"/>
     </go>
</anchor><br/>
<a href="/job/handbookinger/index.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>