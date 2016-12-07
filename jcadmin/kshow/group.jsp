<%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,com.jspsmart.upload.*,net.joycool.wap.bean.PagingBean,jc.show.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html><%
CoolShowAction action = new CoolShowAction(request);
List list = null;
List list2 = null;
String tip = "";
Commodity good = null;
GroupBean group = null;
int did = action.getParameterInt("did");
if (did > 0){
	// 删除
	SqlUtil.executeUpdate("delete from show_group where id=" + did,5);
}
int select = action.getParameterInt("s");
String ids= action.getParameterString("ids");
if (select == 1){
	// 已选了几个套装
	if (ids != null && !"".equals(ids)){
		ids = ids.replaceAll("，",",");
		ids = ids.replaceAll(",{2,}", ",");	// 把连续2个以上的","号换成1个
		if (ids.endsWith(","))	// 不可以用","号结尾
			ids = ids.substring(0,ids.length() - 1);
		list = CoolShowAction.service.getCommodityList(" id in(" + ids + ")");
	}
}
int ok = action.getParameterInt("ok");
if (ok == 1 && ids != null && !"".equals(ids)){
	String name = action.getParameterString("name");
	int due = action.getParameterInt("due");
	if (name == null || "".equals(name) || name.length() > 45){
		tip = "套装名称错误.";
	} else if (due <= 0){
		tip = "过期天数错误.";	
	} else {
		SqlUtil.executeUpdate("insert into show_group (item_ids,name,due) value ('" + ids + "','" + StringUtil.toSql(name) + "'," + due + ")",5);
	}
}
 %>
  <head>
<title>增加组合</title>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  	<%if ("".equals(tip)){
	  	if (list == null){
	  		%><form action="group.jsp?s=1" method="post">
		  		请输入物品的ID,以英文逗号分割:<br/>
		  		<input type="text" size="50%" name="ids"/><br/>
		  		<input type="submit" value="增加组合" />
	  		</form>
	  		<%list2 = CoolShowAction.service.getGroupList(" 1 order by id desc");
	  		if (list2 != null && list2.size() > 0){
	  			%><table border="1" width="100%" align="center">
			  			<tr bgcolor="#C6EAF5">
			  				<td align="center" color="#1A4578">ID</td>
			  				<td align="center" color="#1A4578">物品列表</td>
			  				<td align="center" color="#1A4578">名称</td>
			  				<td align="center" color="#1A4578">UID</td>
			  				<td align="center" color="#1A4578">过期天数</td>
			  				<td align="center" color="#1A4578">操作</td>
			  			</tr><%
	  			for (int i = 0 ; i < list2.size() ; i++){
	  				group = (GroupBean)list2.get(i);
	  				if (group != null){
	  					%><tr>
							<td><%=group.getId()%></td>
			  				<td><%=group.getItemIds()%></td>
			  				<td><%=StringUtil.toWml(group.getName())%></td>
			  				<td><%=group.getUid()%></td>
			  				<td><%=group.getDue()%></td>
			  				<td><a href="group.jsp?did=<%=group.getId()%>" onClick="return confirm('真的要删除这套组合?')">删除</a></td>
						  </tr><%
	  				}
	  			}%></table><%
	  		}
	  		
	  		
	  		
	  		%>
	  		<%
	  	} else {
	  		%>您选择了以下物品.真的要将它们做为组合吗?那就给他们起个名子吧:<br/>
	  		  <form action="group.jsp?ok=1" method="post">
	  		  	名称:<input type="text" name="name"><br/>
	  		  	过期时间(单位:天):<input type="text" name="due"><br/>
	  		  	<input type="hidden" name="ids" value="<%=ids%>">
	  		  	<input type="submit" value="提交" />
	  		  	<input id="cmd" type="button" value="重选" onClick="javascript:window.location.href='group.jsp'"><br/>
	  		  </form>
			  <table border="1" width="100%" align="center">
			  	<tr bgcolor="#C6EAF5">
			  		<td align="center" color="#1A4578">ID</td>
			  		<td align="center" color="#1A4578">名称</td>
			  		<td align="center" color="#1A4578">图片</td>
			  	</tr><%
	  		if (list.size() > 0){
	  			for (int i = 0 ; i < list.size() ; i++){
	  				good = (Commodity)list.get(i);
	  				if (good != null){
	  					%><tr>
	  						<td><%=good.getId()%></td>
	  						<td><%=StringUtil.toWml(good.getName())%></td>
	  						<td><img alt="x" src="/rep/show/comm/<%=good.getBigImg()%>"/></td>
	  					</tr><%
	  				}
	  			}
	  		}
	  		%></table><%
	  	}
  	} else {
  		%><%=tip%><br/><a href="group.jsp">返回</a><br/>
  	<%}%>
 	<a href="index.jsp">返回酷秀首页</a>
  </body>
</html>
