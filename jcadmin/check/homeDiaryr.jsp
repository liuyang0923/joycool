<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.action.home.*"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.FileUtil"%><%@ page import="net.joycool.wap.bean.home.*"%><%
response.setHeader("Cache-Control","no-cache");

HomeAction action= new HomeAction(request);
HomeDiaryReviewBean homeDiary=null;
//删除家园日记
if(request.getParameter("delete")!=null){

	int deleteId =StringUtil.toInt((String)request.getParameter("delete"));
	action.getHomeService().updateHomeDiaryReview("review='(内容被删除)'","id="+deleteId);
}

PagingBean paging = new PagingBean(action,60000,30,"p");
String prefixUrl = "homeDiaryr.jsp";

// 取得要显示的消息列表
int start = paging.getStartIndex();
int end = paging.getEndIndex();
Vector ml = action.getHomeService().getHomeDiaryReviewList("del=0 order by id desc limit "+ start + ",30");
int test=ml.size();
%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<table align="center" border="1" width="100%">
<td>序号</td>
<td>用户</td>
<td>内容</td>
<td>发送时间</td>
<td>操作</td>
<%HomeDiaryReviewBean m = null;
for(int i = 0; i < ml.size(); i ++){
	m = (HomeDiaryReviewBean)ml.get(i);
%>
<tr>
<td width="50"><%=m.getId()%></td>
<td width="60"><a href="../user/queryUserInfo.jsp?id=<%=m.getReviewUserId()%>"><%=m.getReviewUserId()%></a></td>
<td><%=StringUtil.toWml(m.getReview())%></td>
<td width="120"><%=m.getCreateDatetime()%></td>
<td>

<a href="homeDiaryr.jsp?delete=<%=m.getId()%>&p=<%=paging.getCurrentPageIndex()%>" onclick="return confirm('确认删除?')">删</a>

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