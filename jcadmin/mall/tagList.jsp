<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.bean.mall.*,net.wxsj.action.mall.*,java.util.*"%><%
MallAdminAction action = new MallAdminAction();
action.tagList(request, response);

ArrayList tagList = (ArrayList) request.getAttribute("tagList");

int i, count;
TagBean tag = null;
%>
分类标签列表<br/>
<table width="100%" border="1">
    <tr><td><strong>ID</strong></td><td><strong>名称</strong></td><td><strong>显示顺序</strong></td><td><strong>类型</strong></td><td><strong>操作</strong></td></tr>
<%
count = tagList.size();
for(i = 0; i < count; i ++){
	tag = (TagBean) tagList.get(i);
	
%>
    <tr><td><%=tag.getId()%></td><td><%=tag.getName()%></td><td><%=tag.getDisplayOrder()%></td><td><%=tag.getMark() == 0 ? "普通标签" : "<font color=red>热门标签</font>"%></td><td><a href="editTag.jsp?id=<%=tag.getId()%>">编辑</a> <a href="deleteTag.jsp?id=<%=tag.getId()%>" onclick="return confirm('确认删除？')">删除</a></td></tr>
<%
}
%>
</table>
<br/>
添加标签：<br/>
<form action="addTag.jsp" method="post">
名称：<input type="text" name="name" size="20" value=""/><br/>
类型：<select name="mark"><option value="0">普通标签</option><option value="1">热门标签</option></select><br/>
<input type="submit" name="B" size="20" value="添加"/>
</form>