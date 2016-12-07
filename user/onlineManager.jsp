<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.beginner.BeginnerQuestionAction,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*,java.util.*,net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
//警察局处理结果代码
ForumAction formaction=new ForumAction(request);
formaction.forum(request,response);
String forumId = (String) request.getAttribute("forumId");
ForumBean forum =(ForumBean)request.getAttribute("forum");
Vector contentList = (Vector)request.getAttribute("contentList");
//警察局处理结果代码

UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
BeginnerQuestionAction action = new BeginnerQuestionAction(request);
action.index(request);
//int totalCount = ((Integer) request.getAttribute("totalCount")).intValue();
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
//取得登陆用户信息
List beginnerHelpOnlineList=(List)request.getAttribute("beginnerHelpOnlineList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷警察局">
<p align="left">
<img src="/img/police.gif" alt="logo"/><br/>
欢迎来到乐酷警察局,有什么问题需要解决吗?<br/>
值班民警：<br/>
<%
List forbidList = ForbidUtil.getForbidList("admin");
if(forbidList != null){
	Iterator itr = forbidList.iterator();
	while(itr.hasNext()){
	ForbidUtil.ForbidBean forbid = (ForbidUtil.ForbidBean)itr.next();

int managerId = forbid.getValue();
if (OnlineUtil.isOnline(String.valueOf(managerId))) {
	UserBean manager = UserInfoUtil.getUser(managerId);
	if(manager!=null){
%><a href="/chat/post.jsp?toUserId=<%=manager.getId()%>"><%= StringUtil.toWml(manager.getNickName()) %></a><br/><%
	}
}
}}
%>
<a href="/admin/list.jsp">*乐酷监察名单*</a><br/>
<a href="/admin/ap/index.jsp">案情申诉请这边走>></a><br/>
<a href="/admin/query.jsp">查询我的封禁信息</a><br/>
治安协管员<br/>
<%
String userId=null;
UserBean user=null;
for(int i=0;i<beginnerHelpOnlineList.size();i++){
    userId=(String)beginnerHelpOnlineList.get(i);
    if(userId==null)continue;
    user=UserInfoUtil.getUser(StringUtil.toInt(userId));
    if(user==null)continue;
    if(loginUser!=null && user.getId()==loginUser.getId()){%>
    <%=i+1%>.您自己<br/>
    <%}else{%>
    <%=i+1%>.<a href="/chat/post.jsp?toUserId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
    <%}
}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, "/user/onlineManager.jsp?forumId=355", true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<% } %>
<a href="/user/prisoner.jsp?roomId=1">查看乐酷拘留所>></a><br/>
==最新处理公示牌==<br/>

  <%if(contentList!=null){
  //警察局处理结果代码
   for(int i=0;i<contentList.size();i++){
   ForumContentBean forum1=(ForumContentBean)contentList.get(i);
   if(forum1!=null){%>
     <%if(forum1.getMark2()==1){%>**<%}else if(forum1.getMark1()==1){%>*<%}else{%><%=i+1%>.<%}%>
      <%=StringUtil.toWml(forum1.getTitle())%><br/>

      <%}
      if(i > 2)
      break;
    }//警察局处理结果代码
   }%>

<a href="/user/policeVies.jsp?forumId=355">查看所有处理结果</a><br/>
<a href="/admin/proto4.jsp">乐酷游戏规则</a><br/>
<a href="/Column.do?columnId=12623">乐酷用户协议和游戏规则总则</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
