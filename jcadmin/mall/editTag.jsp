<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.action.mall.*,net.wxsj.bean.mall.*"%><%
MallAdminAction action = new MallAdminAction();
action.editTag(request, response);

String result = (String) request.getAttribute("result");
//失败
if("failure".equals(result)){
	String tip = (String) request.getAttribute("tip"); 
%>
<script>
alert("<%=tip%>");
history.back(-1);
</script>
<%
	return;
}
//成功
else if("success".equals(result)){
%>
<script>
alert("修改成功！");
document.location = "tagList.jsp?zzz=1";
</script>
<%
	return;
}

TagBean tag = (TagBean) request.getAttribute("tag");
%>
<form action="editTag.jsp" method="post">
名称：<input type="text" name="name" size="20" value="<%=tag.getName()%>"/><br/>
类型：<select name="mark"><option value="0" <%if(tag.getMark() == 0){%>selected="selected"<%}%>>普通标签</option><option value="1" <%if(tag.getMark() == 1){%>selected="selected"<%}%>>热门标签</option></select><br/>
顺序：<input type="text" name="displayOrder" size="20" value="<%=tag.getDisplayOrder()%>"/><br/>
<input type="hidden" name="id" value="<%=tag.getId()%>"/>
<input type="submit" name="B" size="20" value="修改"/>
</form>