<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.cache.ICacheMap,net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*" %>
<%
MatchAction action = new MatchAction(request);
String tip = "";
MatchArea matchArea = null;
CreditCity city = null;
CreditProvince province = null;
int id = action.getParameterInt("id");
if (id <= 0){
	tip = "要编辑的信息不存在.";
} else {
	matchArea = MatchAction.getArea(id);
	if (matchArea != null){
		int dpid = action.getParameterInt("dpid");
		// 删除省份
		if (dpid > 0){
			// 先将credit_province表中的相应省份标记还原，再删除match_area表中的字段
			if (!"".equals(matchArea.getProvinces())){
				SqlUtil.executeUpdate("update credit_province set flag=0 where id in(" + matchArea.getProvinces() + ")",5);
				SqlUtil.executeUpdate("update credit_city set flag=0 where hypo in(" + matchArea.getProvinces() + ") and flag=" + matchArea.getId(),5);
			}
			SqlUtil.executeUpdate("update match_area set provinces='' where id=" + matchArea.getId(),5);
			matchArea.setProvinces("");
			tip = "省份已删除.";
		}
		// 删除城市
		int dcid = action.getParameterInt("dcid");
		if (dcid > 0){
			if (!"".equals(matchArea.getCitys())){
				SqlUtil.executeUpdate("update credit_city set flag=0 where id in(" + matchArea.getCitys() + ")",5);
			}
			SqlUtil.executeUpdate("update match_area set citys='' where id=" + matchArea.getId(),5);
			matchArea.setCitys("");
			tip = "城市已删除.";
		}
		// 地区说明
		String describe = action.getParameterString("describe");
		if (describe != null){
			if (describe.length() <= 200){
				SqlUtil.executeUpdate("update match_area set `describe`='" + StringUtil.toSql(describe) + "' where id=" + matchArea.getId(),5);
				matchArea.setDescribe(describe);
				tip = "地区说明已更改.";
			} else {
				tip = "分区说明应在200字以内.";
			}
		}
	} else {
		tip = "要编辑的信息不存在.";
	}
}
%>
<html>
	<head>
		<title>编辑分区</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
	<%if(!"".equals(tip)){%><font color="red"><%=tip%></font><br/><%}%>
	<a href="index.jsp">回首页</a>|<a href="area.jsp">回列表</a><br/>
	<%if("".equals(tip)){
		%><font color="blue">编辑地区</font>:<%=matchArea.getAreaNameWml()%><br/>
		  <font color="blue">省份/直辖市/自治区/特别行政区</font>:<%=action.getCityString(matchArea,1)%><a href="select.jsp?id=<%=matchArea.getId()%>">添加</a>|<a href="area2.jsp?id=<%=matchArea.getId()%>&dpid=1" onClick="return confirm('真的要删除这些省份吗?')">删除</a><br/>
		  <font color="blue">城市/区</font>:<%=action.getCityString(matchArea,0)%><a href="select.jsp?id=<%=matchArea.getId()%>&f=1">添加</a>|<a href="area2.jsp?id=<%=matchArea.getId()%>&dcid=1" onClick="return confirm('真的要删除这些城市吗?')">删除</a><br/>
		  <form action="area2.jsp?id=<%=matchArea.getId()%>" method="post">
		  <font color="blue">大区说明</font>:<br/>
		  <textarea name="describe" rows="5" cols="80" border="1"><%=matchArea.getDescribe()%></textarea><br/><br/>
		  <input type="submit" value="确定"/>
		  </form><%
	}%>
	</body>
</html>