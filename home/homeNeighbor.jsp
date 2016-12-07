<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page errorPage=""%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.action.home.*"%><%!
static IHomeService homeService =ServiceFactory.createHomeService();
static IUserService userService=ServiceFactory.createUserService();%><%
response.setHeader("Cache-Control","no-cache");
//所要查看的其邻居的用户ID，如果没有些参数则查看本人邻居
String userId=request.getParameter("userId");
//DAO、ACTION

HomeAction home=new HomeAction(request);      
//如有以下参数为删除用户邻居操作
int neighborId=StringUtil.toInt(request.getParameter("neighborId"));
String delete=request.getParameter("delete");
if(delete!=null&&neighborId>0){
home.deleteNeighbor(neighborId);
}
//获取用户邻居
home.getNeighborsList(request);
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
Vector neighbors=(Vector)request.getAttribute("neighbors");   
//如果NEIGHBORS为空，则本人家园未建，引导到注册提示页面
//if(neighbors==null)
//{
//response.sendRedirect(("/home/inputRegisterInfo.jsp"));return;
//} 
//所要查看的用户的信息       
UserBean ownerUser=UserInfoUtil.getUser(StringUtil.toInt(userId));      
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="家园邻居">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=userId==null?"我":StringUtil.toWml(ownerUser.getNickName())%>的邻居<br/>
<%
int count = neighbors.size();
if(count==0){%>(暂无)<br/><%}else{
for(int i = 0; i < count; i ++){
	HomeUserBean neighbor = (HomeUserBean) neighbors.get(i);
	int diary=neighbor.getDiaryCount();
	if(diary<0){
	diary=0;
	}
	int photo=neighbor.getPhotoCount();
	if(photo<0){
	photo=0;
	}
//	UserBean user=userService.getUser("id="+neighbor.getUserId());
	//zhul 2006-10-12_优化用户信息查询
	UserBean user = UserInfoUtil.getUser(neighbor.getUserId());
//	String hits=(String)home.getHitsOrder().get(neighbor.getUserId()+"");
if(user!=null)
{
%>
<%=(Integer.parseInt(perPage)*pageIndex + i+1)%>.<a href="/home/home.jsp?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>(日记<%=diary%> 相册<%=photo%>)
<%}
if(userId==null){%>&nbsp;&nbsp;
<a href="homeNeighbor.jsp?delete=1&amp;neighborId=<%=neighbor.getUserId()%>" >删除</a><%}%><br/>
<%
}
%>
<%if(userId!=null){%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "homeNeighbor.jsp?userId="+userId, true, " ", response)%><%if(totalPage>1){%><br/><%}%>
<%}else{%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "homeNeighbor.jsp", false, " ", response)%><%if(totalPage>1){%><br/><%}%>
<%}}%>
<%if(userId==null){%><a href="fillSearch.jsp">添加邻居</a><br/><%}%>
<%if(userId==null){
	UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<a href="home.jsp?userId=<%= loginUser.getId()%>" >返回<%= StringUtil.toWml(loginUser.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%>
<%}else{%>
<%-- liuyi 2006-12-26 返回家园首页修改 start --%> 
<%
UserBean user = UserInfoUtil.getUser(StringUtil.toInt(userId));
%>
<a href="home.jsp?userId=<%=userId%>" >返回<%= StringUtil.toWml(user.getNickName()) %>的家园</a><br/>
<%-- liuyi 2006-12-26 返回家园首页修改 end --%> 
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>