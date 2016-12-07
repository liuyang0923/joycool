<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,com.jspsmart.upload.*,net.joycool.wap.bean.PagingBean,jc.show.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html><%
CoolShowAction action = new CoolShowAction(request);
List list = null;
String tip = "";
String ids = "";
String uids = "";
int type = 0;	// 0:显示列表 1:确认所选组合
Commodity good = null;
GroupBean group = null;
list = CoolShowAction.service.getGroupList(" 1 order by id desc");
int submit = action.getParameterInt("s");
if (submit == 1){
	String idsArray[] = request.getParameterValues("ids");
	uids = action.getParameterString("uids");
	uids = uids.replaceAll("，",",");
	uids = uids.replaceAll(",{2,}", ",");	// 把连续2个以上的","号换成1个
	if (uids.endsWith(","))	// 不可以用","号结尾
		uids = uids.substring(0,uids.length() - 1);
	if (idsArray != null){
		type = 1;
		StringBuilder sb = new StringBuilder(50);
		for (int i = 0 ; i < idsArray.length ; i++){
			sb.append(idsArray[i]);
			sb.append(",");
		}
		ids = sb.toString();
		if (ids.endsWith(",")){
			ids = ids.substring(0,ids.length() - 1);
		}
	}
}
int ok = action.getParameterInt("ok");
if (ok == 1){
	ids = action.getParameterString("ids");
	uids = action.getParameterString("uids");
	action.give2(uids,ids);
	tip = "已赠送.";
}
%>
<head>
<title>赠送组合</title>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  	<%if ("".equals(tip)){
  		if (type == 0){
  			%><form action="give.jsp?s=1" method="post">
  					用户ID(多ID请以英文逗号“,”分割):<br/>
  					<input type="text" name="uids" size="30%"/><br/>
			  		<input type="submit" value="赠送" />
			  		<table border="1" width="100%" align="center">
			  			<tr bgcolor="#C6EAF5">
			  				<td align="center" color="#1A4578">ID</td>
			  				<td align="center" color="#1A4578">物品列表</td>
			  				<td align="center" color="#1A4578">名称</td>
			  				<td align="center" color="#1A4578">UID</td>
			  				<td align="center" color="#1A4578">过期天数</td>
			  				<td align="center" color="#1A4578">操作</td>
			  			</tr>
			  		<%
					if (list != null && list.size() > 0){
						for (int i = 0 ; i < list.size() ; i++){
							group = (GroupBean)list.get(i);
							if (group != null){
								%><tr>
									<td><%=group.getId()%></td>
					  				<td><%=group.getItemIds()%></td>
					  				<td><%=StringUtil.toWml(group.getName())%></td>
					  				<td><%=group.getUid()%></td>
					  				<td><%=group.getDue()%></td>
					  				<td><input type="checkbox" name="ids" value="<%=group.getId()%>"></td>
								</tr><%
							}
						}
					}
					%></table>
				</form><%
  		} else {
  			%>您要赠送给:<br/><%=uids%><br/>下列套装:<br/><%
  			List list2 = CoolShowAction.service.getGroupList(" id in(" + ids + ")");
  			if (list2 != null && list2.size() > 0){
  				for (int i = 0 ; i < list2.size() ; i++){
  					group = (GroupBean)list2.get(i);
  					if (group != null){
  						%><%=StringUtil.toWml(group.getName())%>,<%
  					}
  				}
  			}%><br/>
  			<form action="give.jsp?ok=1" method="post">
  				<input type="hidden" value="<%=ids%>" name="ids" />
  				<input type="hidden" value="<%=uids%>" name ="uids" />
  				<input type="submit" value="赠送">&nbsp;<input id="cmd" type="button" value="重选" onClick="javascript:window.location.href='give.jsp'"><br/>
  			</form><%
  		}
  	} else {
  		%><%=tip%><br/><a href="give.jsp">返回</a><br/><%
  	}
%>
 	<a href="index.jsp">返回酷秀首页</a>
  </body>
</html>
