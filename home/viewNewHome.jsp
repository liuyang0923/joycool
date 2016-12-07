<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
//dao,action
IHomeService homeService =ServiceFactory.createHomeService();
IUserService userService =ServiceFactory.createUserService();
HomeAction home=new HomeAction(request);      
home.viewNewHome(request);
//新成立家园
int perPage=StringUtil.toInt((String)request.getAttribute("NUM_PER_PAGE"));
int totalCount=StringUtil.toInt((String)request.getAttribute("totalCount"));
int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
Vector newHome=(Vector)request.getAttribute("newHome");

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="新成立家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
通吃岛新成立家园(共<%=totalCount%>个)<br/>
------------------<br/>

<%
int count = newHome.size();
for(int i = 0; i < count; i ++){
	HomeUserBean homeUser=(HomeUserBean)newHome.get(i);
	UserBean user=UserInfoUtil.getUser(homeUser.getUserId());
%>
<%=perPage*pageIndex + i+1%>.
<a href="home.jsp?userId=<%=homeUser.getUserId()%>" ><%=(loginUser!=null&&loginUser.getId()==user.getId())?"我的家园":StringUtil.toWml(user.getNickName())%></a>(人气<%=homeUser.getTotalHits()%>)<br/>
<%
}
%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "viewNewHome.jsp", false, " ", response)%><%if(totalPage>1){%><br/><%}%>
搜索家园<br/>
用户ID：<input name="userId" format="*N"  maxlength="10" value=""/> <anchor title ="search Image">Go
     <go href="searchHome.jsp" method="post">
         <postfield name="userId" value="$userId"/>
     </go>
     </anchor><br/>
<a href="diaryTop.jsp">乐客日记</a><br/>
<a href="photoTop.jsp">乐客相册</a><br/>
<a href="viewAllHome.jsp">*家园之星*</a><br/>        
<%if(loginUser!=null && loginUser.getHome()==1){%>
<a href="home.jsp">返回我的家园</a><br/>
<%}else{%>
<a href="inputRegisterInfo.jsp">我也要建立家园</a><br/> 
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>