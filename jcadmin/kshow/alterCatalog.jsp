<%@ page language="java" pageEncoding="utf-8" import="java.util.*,net.joycool.wap.util.*,jc.show.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	CoolShowAction action = new CoolShowAction(request);
	CatalogBean bean = null;
	int id = action.getParameterInt("id");
	if(id > 0){
		bean = CoolShowAction.service.getCatalogBean(" id="+id);
	}
 %>
<html>
  <head>
    <title>商品分类管理</title>
    <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
    <%
    	if(bean != null){
    	%>
    		<form action="catalog.jsp?alter=1" method="post">
    			<input type="hidden" name="id" value="<%=bean.getId()%>"/>
    			<table>
    			<tr>
    				<td>分类ID</td>
    				<td><%=bean.getId()%></td>
    			</tr>
    			<tr>
    				<td>分类名称:</td>
    				<td><input type="text" name="name" value="<%=StringUtil.toWml(bean.getName())%>"/></td>
    			</tr>
    			<tr>
    				<td>顺序:</td>
    				<td><input type="text" name="seq" value="<%=bean.getSeq()%>"/>(顺序)</td>
    			</tr>
    			<tr>
    				<td>性别:</td>
    				<td><input type="text" name="gender" value="<%=bean.getGender()%>"/></td>
    			</tr>
    			<tr>
    				<td>隐藏:</td>
    				<td><select name="hide"><option value="0"<%if(bean.getHide()==0){%> selected<%}%>>显示</option><option value="1"<%if(bean.getHide()==1){%> selected<%}%>>隐藏</option></select></td>
    			</tr>
    			<tr>
    				<td>分类说明:</td>
    				<td><input type="text" name="bak" value="<%=StringUtil.toWml(bean.getBak())%>"/></td>
    			</tr>
    			<tr>
    				<td align="center"><input type="submit" value="修改"/></td>
    				<td><a href="part.jsp">取消</a></td>
    			</tr>
    			</table>
    		</form>
    	<%
    	}else if(id <= 0){
    	%>
    		<form action="catalog.jsp?add=1" method="post">
    			<table>
    			<tr>
    				<td>分类ID</td>
    				<td><input type="text" name="id"/></td>
    			</tr>
    			<tr>
    				<td>分类名称:</td>
    				<td><input type="text" name="name"/></td>
    			</tr>
    			<tr>
    				<td>顺序:</td>
    				<td><input type="text" name="seq"/></td>
    			</tr>
    			<tr>
    				<td>性别:</td>
    				<td><input type="text" name="gender" value="2"/></td>
    			</tr>
    			<tr>
    				<td>隐藏:</td>
    				<td><select name="hide"><option value="0">显示</option><option value="1" selected>隐藏</option></select></td>
    			</tr>
    			<tr>
    				<td>分类说明:</td>
    				<td><input type="text" name="bak"/></td>
    			</tr>
    			<tr>
    				<td align="center"><input type="submit" value="添加"/></td>
    				<td><a href="part.jsp">取消</a></td>
    			</tr>
    			</table>
    		</form>
    	<%
    	}else{
    	%>
    		没有对应内容!<br/>
    	<%
    	}
     %>
  	 <a href="catalog.jsp">返回商品分类列表</a><br/>
  	 <a href="index.jsp">返回酷秀首页</a>
  </body>
</html>