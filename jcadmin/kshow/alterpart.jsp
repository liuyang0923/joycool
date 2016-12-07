<%@ page language="java" pageEncoding="utf-8" import="java.util.*,net.joycool.wap.util.*,jc.show.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	CoolShowAction action = new CoolShowAction(request);
	PartBean bean = null;
	int id = action.getParameterInt("id");
	if(id > 0){
		bean = CoolShowAction.service.getPartBean(" id="+id);
	}
 %>
<html>
  <head>
    <title>部位管理</title>
    <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
    <%
    	if(bean != null){
    	%>
    		<form action="part.jsp?alter=1" method="post">
    			<input type="hidden" name="id" value="<%=bean.getId()%>"/>
    			<table>
    			<tr>
    				<td>部位ID</td>
    				<td><%=bean.getId()%></td>
    			</tr>
    			<tr>
    				<td>部位名称:</td>
    				<td><input type="text" name="name" value="<%=StringUtil.toWml(bean.getName())%>"/></td>
    			</tr>
    			<tr>
    				<td>画图顺序:</td>
    				<td><input type="text" name="lvllayer" value="<%=bean.getLvlLayer()%>"/>(顺序)</td>
    			</tr>
    			<tr>
    				<td>显示顺序:</td>
    				<td><input type="text" name="lvlshow" value="<%=bean.getLvlShow()%>"/>(0表示不显示)</td>
    			</tr>
    			<tr>
    				<td>部位说明:</td>
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
    		<form action="part.jsp?add=1" method="post">
    			<table>
    			<tr>
    				<td>部位ID</td>
    				<td><input type="text" name="id"/></td>
    			</tr>
    			<tr>
    				<td>部位名称:</td>
    				<td><input type="text" name="name"/></td>
    			</tr>
    			<tr>
    				<td>画图顺序:</td>
    				<td><input type="text" name="lvllayer"/>(顺序)</td>
    			</tr>
    			<tr>
    				<td>显示顺序:</td>
    				<td><input type="text" name="lvlshow"/>(0表示不显示)</td>
    			</tr>
    			<tr>
    				<td>部位说明:</td>
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
  	 <a href="part.jsp">返回部位列表</a><br/>
  	 <a href="index.jsp">返回酷秀首页</a>
  </body>
</html>