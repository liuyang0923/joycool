<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.action.home.*"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.FileUtil"%><%@ page import="net.joycool.wap.bean.home.*"%><%
response.setHeader("Cache-Control","no-cache");

HomeAction action= new HomeAction(request);
HomeDiaryBean homeDiary=null;
//删除家园日记
if(request.getParameter("delete")!=null){

	int deleteId =StringUtil.toInt((String)request.getParameter("delete"));
	homeDiary=action.getHomeService().getHomeDiary("id="+deleteId);
	if(homeDiary != null)
		action.deleteHomeDiary(deleteId,homeDiary.getUserId());
}
// 编辑家园日记
if(request.getParameter("id")!=null){

	int id =StringUtil.toInt((String)request.getParameter("id"));
	homeDiary=action.getHomeService().getHomeDiary("id="+id);
	if(homeDiary != null){
		action.getHomeService().updateHomeDiary("title='"+StringUtil.toSql(request.getParameter("title"))+"',content='"+StringUtil.toSql(request.getParameter("content"))+"'",
					"id=" + id);
	}
}

PagingBean paging = new PagingBean(action,10000,20,"p");
String prefixUrl = "homeDiary.jsp";

// 取得要显示的消息列表
int start = paging.getStartIndex();
int end = paging.getEndIndex();
Vector ml = action.getHomeService().getHomeDiaryList("del=0 order by id desc limit "+ start + ",20");
int test=ml.size();
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<table align="center" border="1" width="100%">
<td>序号</td>
<td>用户</td>
<td>标题</td>
<td>内容</td>
<td>发送时间</td>
<td>操作</td>
<%HomeDiaryBean m = null;
for(int i = 0; i < ml.size(); i ++){
	m = (HomeDiaryBean)ml.get(i);
%>
<tr>
<td width="40"><%=m.getId()%></td>
<td width="60"><a href="../user/home.jsp?id=<%=m.getUserId()%>"><%=m.getUserId()%></a></td>
<td><%=StringUtil.toWml(m.getTitel())%></td>
<td><a href="homeDiary2.jsp?id=<%=m.getId()%>&p=<%=paging.getCurrentPageIndex()%>"><%=StringUtil.toWml(m.getContent())%></a></td>
<td width="80"><%=m.getCreateDatetime()%></td>
<td>

<a href="homeDiary.jsp?delete=<%=m.getId()%>&p=<%=paging.getCurrentPageIndex()%>" onclick="return confirm('确认删除?')">删</a>

</td>
</tr>
<%
}
%>
</table>
<p align="center">
<%=paging.shuzifenye(prefixUrl,false,"|",response)%><br/>
</p>
<!-- fanys 2006-06-28 start -->
<p align="center"><a href="../chatmanage.jsp">返回</a></p>
<!-- fanys 2006-06-28 end -->
</body>
</html>