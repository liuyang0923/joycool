<%@ page language="java" pageEncoding="utf-8" import="java.util.*,net.joycool.wap.util.*,jc.show.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	CoolShowAction action = new CoolShowAction(request);
	if("1".equals(action.getParameterString("alter"))){
		action.alterPart();
	}
	if("1".equals(action.getParameterString("add"))){
		action.insertPart();
	}
	List list = CoolShowAction.service.getPartList(" 1 order by level_layer");
 %>
<html>
  <head>
    <title>部位列表</title>
    <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  <p style="color:red;"><%=request.getAttribute("tip") != null?request.getAttribute("tip"):""%></p>
  	<table width="60%">
		<tr bgcolor=#C6EAF5>
  			<td align="center"><font color=#1A4578>部位ID</font></td>
  			<td align="center"><font color=#1A4578>部位名称</font></td>
  			<td align="center"><font color=#1A4578>画图顺序</font></td>
  			<td align="center"><font color=#1A4578>罗列顺序</font></td>
  			<td align="center"><font color=#1A4578>部位说明</font></td>
  			<td align="center"><font color=#1A4578>部位操作</font></td>
  		</tr>
  	<%
  		if(list != null && list.size() > 0){
  			for(int i=0;i<list.size();i++){
  				PartBean bp = (PartBean)list.get(i);
  				%>
  				<tr>
  					<td align="center"><%=bp.getId()%></td>
  					<td align="center"><%=StringUtil.toWml(bp.getName())%></td>
  					<td align="center"><%=bp.getLvlLayer()%></td>
  					<td align="center"><%=bp.getLvlShow()%></td>
  					<td align="center"><%=StringUtil.toWml(bp.getBak())%></td>
  					<td align="center"><a href="alterpart.jsp?id=<%=bp.getId()%>">修改</a></td>
  				</tr>
  				<%
  			}
  		}
  	 %>
  	 </table>
  	 <a href="alterpart.jsp">添加部位</a><br/>
  	 <a href="index.jsp">返回酷秀首页</a>
  </body>
</html>