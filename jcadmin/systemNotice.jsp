<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.INoticeService"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
if(true){
// 系统通知暂时废弃
	response.sendRedirect("manage.jsp");
	return;
}
INoticeService noticeService = ServiceFactory.createNoticeService();
NoticeBean notice = null;
if(request.getParameter("delete") != null){
    String delete=request.getParameter("delete");
    int number=StringUtil.toInt(delete);
    if(number>=0){
		boolean flag=noticeService.deleteNotice("id = " + number);
		//mcq_2006-8-1_增加删除jc_notice_history方法,删除系统消息的同时,删除系统消息history_start
		if(flag){
		DbOperation dbOp = new DbOperation();
	    dbOp.init();
	    dbOp.executeUpdate("DELETE FROM jc_notice_history WHERE notice_id=" + number);
	    dbOp.release();
	    //删除对应系统消息id在缓存中的数据
	    NewNoticeCacheUtil.flushSystemNotice(number);
	    }
    }
    //mcq_2006-8-1_增加删除jc_notice_history方法,删除系统消息的同时,删除系统消息history_end
	//response.sendRedirect("systemNotice.jsp");
	BaseAction.sendRedirect("/jcadmin/systemNotice.jsp", response);
	return;
}
if(request.getMethod().equalsIgnoreCase("post")){
	String title = request.getParameter("title");
	String content = request.getParameter("content");
	String link = request.getParameter("link");	
	if(title == null || title.equals("") || ((content == null || content.equals("")) && (link == null || link.equals("")))){
%>
<script>
alert("请填写正确各项参数！");
history.back(-1);
</script>
<%
	    return;
	} else {
		notice = new NoticeBean();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setLink(link);
		notice.setType(NoticeBean.SYSTEM_NOTICE);
		noticeService.addNotice(notice);
		NewNoticeCacheUtil.addSystemNoticeById();
%>
<script>
alert("添加成功！");
window.navigate("systemNotice.jsp");
</script>
<%
	    return;
	}
}

String condition = "type = " + NoticeBean.SYSTEM_NOTICE + " and tong_id=0 order by id desc limit 0, 100";
Vector noticeList = noticeService.getNoticeList(condition);
int count = noticeList.size();
int i;
%>
<p>系统公告列表</p>
<table width="100%" border="2">
<tr>
<td width="10%">序号</td>
<td width="20%">标题</td>
<td width="20%">内容</td>
<td width="25%">链接</td>
<td width="20%">添加时间</td>
<td width="5%">操作</td>
</tr>
<%
for(i = 0; i < count; i ++){
	notice = (NoticeBean) noticeList.get(i);
%>
<tr>
<td width="10%"><%=(i + 1)%></td>
<td width="20%"><%=notice.getTitle()%></td>
<td width="20%"><%if(notice.getContent() == null || notice.getContent().equals("")){%>空<%}else{%><%=notice.getContent()%><%}%></td>
<td width="25%"><%if(notice.getLink() == null || notice.getLink().equals("")){%>空<%}else{%><%=notice.getLink()%><%}%></td>
<td width="20%"><%=notice.getCreateDatetime()%></td>
<td width="5%"><a href="systemNotice.jsp?delete=<%=notice.getId()%>">删除</a></td>
</tr>
<%
}
%>
</table>
<p>增加系统公告</p>
<form method="post" action="systemNotice.jsp">
标题：<input type="text" name="title" size="30">必填<br>
内容：<input type="text" name="content" size="30"><br>
链接：<input type="text" name="link" size="30"><br>
说明：内容和链接必填其一<br>
<input type="submit" value="增加">
</form>