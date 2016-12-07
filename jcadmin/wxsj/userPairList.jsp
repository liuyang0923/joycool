<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.service.infc.*"%><%@ page import="net.wxsj.service.factory.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%
IZippoService zippoService = ServiceFactory.createZippoService(IBaseService.CONN_IN_SERVICE, null);

int userId = StringUtil.toInt(request.getParameter("user_id"));
ArrayList pairList = zippoService.getZippoPairList("user_id = " + userId, 0, -1, "id desc");
int i, count;
ZippoPairBean pair = null;
count = pairList.size();
ZippoStarBean star = null;
ZippoBean zippo = null;
UserBean user = UserInfoUtil.getUser(userId);
%>
<strong>已完成的搭配（<%=pairList.size()%>）</strong><br>
<table border="1" width="100%">
<tr>
<td><strong>序号</strong></td>
<td><strong>用户ID</strong></td>
<td><strong>明星</strong></td>
<td><strong>Zippo</strong></td>
<td><strong>结果</strong></td>
<td><strong>时间</strong></td>
</tr>
<%
for(i = 0; i < count; i ++){
	pair = (ZippoPairBean) pairList.get(i);
	star = ZippoFrk.getStarById(pair.getStarId());
	zippo = ZippoFrk.getZippoById(pair.getZippoId());
%>
<tr>
<td><%=(i + 1)%></td>
<td><%=pair.getUserId()%>(<%=user.getNickName()%>)</td>
<td><%=star.getName()%></td>
<td><%=zippo.getName()%></td>
<td><%if(pair.getResult() == 1){%><font color="red">正确</font><%}else if(pair.getResult() == 0){%>错误<%}else{%><font color="blue">已处理</font><%}%></td>
<td><%=pair.getCreateDatetime()%></td>
</tr>
<%
}
zippoService.releaseAll();
%>
</table>