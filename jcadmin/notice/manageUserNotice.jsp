<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%
int userId = StringUtil.toInt(request.getParameter("userId"));

Vector noticeList = null;
if(userId>0){
	noticeList = new Vector();
	
	UserBean userBean = UserInfoUtil.getUser(userId);
	Vector noticeList1 = NewNoticeCacheUtil.getSystemNoticeReadedCountMap(userBean);
	Vector noticeList2 = NewNoticeCacheUtil.getUserGeneralNoticeCountMap(userId);
	if(noticeList1!=null && noticeList1.size()>0){
		noticeList.addAll(noticeList1);
	}
	if(noticeList2!=null && noticeList2.size()>0){
		noticeList.addAll(noticeList2);
	}
}
%>
<table border=1 width=80%>
  <form method=post>
  <tr>
    <td>用户ID</td>
    <td><input type=text name="userId" value="">&nbsp;<input type=submit value="查询"></td>
  </tr>
  </form>
</table>
 
<% if(noticeList!=null){ %>  
<table border=1  width=80%> 
  <tr>
    <td width=10%>消息ID</td>
    <td width=70%>消息标题</td>
    <td width=10%>操作</td>
  </tr>
  <%
      for(int i=0;i<noticeList.size();i++){
    	  Integer noticeId = (Integer) noticeList.get(i);
    	  if(noticeId==null)continue;
    	  
    	  NoticeBean notice = NewNoticeCacheUtil.getNotice(noticeId
					.intValue());
    	  if(notice==null)continue;
    	  
    	  String content = notice.getTitle();
		  if (content != null && content.length() > 18){
			  content = content.substring(0, 18) + "...";
		  }	
		  %>
  <form action="/jcadmin/notice/updateUserNotice.jsp" method=post>
  <input type=hidden name="userId" value="<%= userId %>">	
  <input type=hidden name="noticeId" value="<%= notice.getId() %>">	  
  <tr>
    <td width=10%><%= notice.getId() %></td>
    <td width=70%><input name="title" size=80 value="<%= StringUtil.toWml(content) %>" ></td>
    <td width=10%><input type=submit value="修改"></td>
  </tr>
  </form>	  
		  <%
      }
  %>
</table> 
<%} %>  