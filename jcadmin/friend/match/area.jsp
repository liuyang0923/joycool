<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.cache.ICacheMap,net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%
MatchAction action = new MatchAction(request);
String tip = "";
MatchArea matchArea = null;
int deleteId = action.getParameterInt("did");
if (deleteId > 0){
	// 删除一个分区
	matchArea = MatchAction.service.getArea(" id=" + deleteId);
	if (matchArea != null){
		// 把这个分区下的所有省(市)的flag置为0
		if (!"".equals(matchArea.getProvinces())){
			SqlUtil.executeUpdate("update credit_province set flag=0 where id in (" + matchArea.getProvinces() + ")",5);
			SqlUtil.executeUpdate("update credit_city set flag=0 where hypo in(" + matchArea.getProvinces() + ")",5);
		}
		if (!"".equals(matchArea.getCitys())){
			SqlUtil.executeUpdate("update credit_city set flag=0 where id in (" + matchArea.getCitys() + ")",5);	
		}
		// 把这个分区的信息从数据库中删除
		SqlUtil.executeUpdate("delete from match_area where id=" + deleteId,5);
		MatchAction.areaMap.remove(new Integer(matchArea.getId()));
	} else {
		tip  = "要删除的分区信息不存在。";
	}
}
int add = action.getParameterInt("add");
if (add == 1){
	// 添加一个分区
	int confirm = action.getParameterInt("c");
	if (confirm == 1){
		String areaName = action.getParameterNoEnter("areaName");
		if (areaName == null || "".equals(areaName) || areaName.length() < 4 || areaName.length() > 6){
			tip = "没有输入地区名称，或名称字数没有介于4至6字之间。";	
		} else {
			matchArea = new MatchArea();
			matchArea.setAreaName(areaName);
			matchArea.setCitys("");
			matchArea.setDescribe("");
			matchArea.setProvinces("");
			matchArea.setCount(0);
			int id = MatchAction.service.addArea(matchArea);
			if (id > 0){
				matchArea.setId(id);
				MatchAction.areaMap.put(new Integer(id),matchArea);
			}
			response.sendRedirect("area2.jsp?id=" + id);
			return;
		}
	}
}
List list = MatchAction.getAreaList();
%>
<html>
	<head>
		<title>比赛分区</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
	<%if(!"".equals(tip)){%><font color="red"><%=tip%></font><br/><%}%>
	<%if(add == 1){
		%><a href="index.jsp">回首页</a>|<a href="area.jsp">回列表</a>|<a href="area.jsp?add=1">刷新</a><br/>
		  添加地区排名:<br/>
		  <form action="area.jsp?add=1&c=1" method="post">
		  		添加地区:<input type="text" name="areaName"/><br/>
		  		<input type="submit" value="创建"><br/>
		  </form><%
	} else {%>
	<a href="index.jsp">回首页</a>|<a href="area.jsp">刷新</a><br/>
	目前共<%=MatchAction.areaMap.size()%>个分区<br/>
		<table border="1" width="100%" align="center">
			<tr bgcolor=#C6EAF5>
				<td align=center><font color=#1A4578>ID</font></td>
				<td align=center><font color=#1A4578>地区名</font></td>
				<td align=center><font color=#1A4578>人数</font></td>
				<td align=center><font color=#1A4578>包含省市</font></td>
				<td align=center><font color=#1A4578>地区说明</font></td>
				<td align=center><font color=#1A4578>编辑</font></td>
				<td align=center><font color=#1A4578>删除</font></td>
			</tr>
			<%if(list != null && list.size() > 0){
					for (int i = 0 ; i < list.size() ; i ++){
						matchArea = MatchAction.getArea(StringUtil.toInt("" + list.get(i)));
						if (matchArea != null){
							%><tr>
								<td><%=matchArea.getId()%></td>
								<td><%=matchArea.getAreaNameWml()%></td>
								<td align="center"><%=matchArea.getCount()%></td>
								<td>市:<%=action.getCityString(matchArea,0)%><br/>省:<%=action.getCityString(matchArea,1)%></td>
								<td><%=matchArea.getDescribeWml()%></td>
								<td><a href="area2.jsp?id=<%=matchArea.getId()%>">编辑</a></td>
								<td><a href="area.jsp?did=<%=matchArea.getId()%>" onClick="return confirm('真的要删除该分区吗?')">删除</a></td>
							</tr><%
						}
					}
			  }%>
		</table><br/>
		<input type="button" value="创建分区"  onClick="javascript:window.location.href='area.jsp?add=1'"/><br/><%	
	}%>
	</body>
</html>