<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.Set" %><%@ page import="java.util.Iterator" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
if(!action.isLogin()) {
	action.innerRedirect("needlogin.jsp", response);
	return;
}
action.viewAdmin(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = action.getLoginUser();
String url=("index.jsp");
 %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="论坛" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回乐酷论坛)<br/>
<a href="index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
ForumBean forum =(ForumBean)request.getAttribute("forum");
Set userSet = forum.getUserIdSet();
%>
<card title="<%=StringUtil.toWml(forum.getTitle())%>版主">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(userSet.size()>0){
Iterator it = userSet.iterator();
int i = 1;
while(it.hasNext()){
	int userId=((Integer)it.next()).intValue();
	UserBean user  = UserInfoUtil.getUser(userId);
	if(user==null){continue;}%>
	<%=i%>.
	<%if(loginUser!=null && user.getId()==loginUser.getId()){%>
  	 	<%=StringUtil.toWml(user.getNickName())%><br/>
     <%}else{%>
      	<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>" ><%=StringUtil.toWml(user.getNickName())%></a>(<%=LoadResource.getPostionNameByUserId(user.getId())%>)<br/>
<%}i++;
}
	boolean superAdmin = ForumAction.isSuperAdmin(loginUser.getId());
	if(forum.getPrimeCat()!=0&&(superAdmin||forum.getUserIdSet().contains(new Integer(loginUser.getId())))){
%>

<a href="primeAdmin.jsp?forumId=<%=forum.getId()%>">管理分类精华区</a><br/>

<%}%>

<%if(ForbidUtil.isForbid("system", loginUser.getId())){%>
<%if(forum.getPrimeCat()==0){%>
<a href="primeAdmin.jsp?a=1&amp;forumId=<%=forum.getId()%>">创建分类精华区</a><br/>
<%}else{%>
<a href="primeAdmin.jsp?a=2&amp;forumId=<%=forum.getId()%>">关闭分类精华区</a><br/>
<%}%>
<%}%>


<%
}else{%>该板块没有版主！<br/><%}%>
<br/>
<a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="index.jsp">返回乐酷论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>