<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.text.DecimalFormat,net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static DecimalFormat numFormat = new DecimalFormat("0.0");%>
<%
MatchAction action = new MatchAction(request);
MatchRes matchRes  = null;
String tip = "";
int modify = action.getParameterInt("m");
if (modify>0){
	modify--;
	matchRes = MatchAction.getRes(modify);
	if (matchRes != null){
		// 修改
		String resName = action.getParameterNoEnter("resname");
		int prices = action.getParameterInt("prices");
		int point = action.getParameterInt("point");
		if (resName == null || "".equals(resName) || resName.length() > 45){
			tip = "没有输入标题或标题长于45字.";
		} else if (prices < 0){
			tip = "价格小于0?";
		} else if (point < 0){
			tip  = "靓点小于0?";
		} else {
			SqlUtil.executeUpdate("update match_res set res_name='" + StringUtil.toSql(resName) + "',prices=" + prices + ",`point`=" + point + " where id=" + modify,5);
			MatchAction.resMap = null;
			MatchAction.getResMap();
			tip = "修改成功.";
		}
	}
}
List resdlist = MatchAction.service.getResList(" 1") ;
%>
<html>
	<head>
		<title>奢侈品管理</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<%=tip %><br/>
	<body>
		<a href="index.jsp">回首页</a><br/>
		如果想修改为10酷币，请输入1000，想修改为8酷币，请输入800。<br/>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>id</font>
				</td>
				<td align=center>
					<font color=#1A4578>名称</font>
				</td>
				<td align=center>
					<font color=#1A4578>图片</font>
				</td>
				<td align=center>
					<font color=#1A4578>价格</font>
				</td>
				<td align=center>
					<font color=#1A4578>靓点</font>
				</td>
				<td align=center>
					<font color=#1A4578>币种</font>
				</td>
				<td align=center>
					<font color=#1A4578>修改</font>
				</td>
			</tr>
			<% for (int i = 0 ; i < resdlist.size() ; i++){
				matchRes = (MatchRes)resdlist.get(i);
				if (matchRes != null){
					%>
					  <form action="res.jsp?m=<%=matchRes.getId()+1%>" method="post">
						  <tr>
								<td><%=matchRes.getId()%></td>
								<td><input type="text" name="resname" value="<%=matchRes.getResNameWml()%>" style="width:100px;"/></td>
								<td align="center"><img src="/friend/match/img/<%=matchRes.getPhoto()%>" alt="无法显示" /></td>
								<td><input type="text" name="prices" value="<%=matchRes.getPrices()%>" style="width:120px;"/>(<%=matchRes.getCur()==0?StringUtil.bigNumberFormat2(matchRes.getPrices()):numFormat.format(matchRes.getPrices() / 100f)%>)</td>
								<td><input type="text" name="point" value="<%=matchRes.getPoint()%>" style="width:80px;"/></td>
								<td align="center"><%=matchRes.getCur()==0?"乐币":"酷币" %></td>
								<td align="center"><input type="submit" value="修改" onclick="return confirm('确定修改？')"/></td>
						  </tr>
					  </form><%
				}
			}%>
		</table>
	</body>
</html>