<%@ page language="java" pageEncoding="utf-8" import="java.util.*,net.joycool.wap.util.*,jc.show.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	CoolShowAction action = new CoolShowAction(request);
	if("1".equals(action.getParameterString("alter"))){
		action.alterCatalog();
	}
	if("1".equals(action.getParameterString("add"))){
		action.insertCatalog();
	}
	List list = CoolShowAction.service.getCatalogList(" 1 order by seq");
 %>
<html>
  <head>
    <title>商品分类列表</title>
    <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <p style="color:red;"><%=request.getAttribute("tip") != null?request.getAttribute("tip"):""%></p>
  	<table width="60%">
		<tr bgcolor=#C6EAF5>
  			<td align="center"><font color=#1A4578>部位ID</font></td>
  			<td align="center"><font color=#1A4578>名称</font></td>
  			<td align="center"><font color=#1A4578>顺序</font></td>
  			<td align="center"><font color=#1A4578>性别</font></td>
  			<td align="center"><font color=#1A4578>说明</font></td>
  			<td align="center"><font color=#1A4578>隐藏</font></td>
  			<td align="center"><font color=#1A4578>操作</font></td>
  		</tr>
  	<%
  		if(list != null && list.size() > 0){
  			for(int i=0;i<list.size();i++){
  				CatalogBean bp = (CatalogBean)list.get(i);
  				%>
  				<tr>
  					<td align="center"><%=bp.getId()%></td>
  					<td align="center"><%=StringUtil.toWml(bp.getName())%></td>
  					<td align="center"><%=bp.getSeq()%></td>
  					<td align="center"><%=bp.getGender()%></td>
  					<td align="center"><%=StringUtil.toWml(bp.getBak())%></td>
  					<td align="center"><%=bp.getHide()==1?"<font color=red>隐藏</font>":"显示"%></td>
  					<td align="center"><a href="alterCatalog.jsp?id=<%=bp.getId()%>">修改</a></td>
  				</tr>
  				<%
  			}
  		}
  	 %>
  	 </table>
  	 <a href="alterCatalog.jsp">添加分类</a><br/>
  	 <a href="index.jsp">返回酷秀首页</a>
  </body>
</html>