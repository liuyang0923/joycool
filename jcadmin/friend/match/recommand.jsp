<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=10; %>
<%
MatchAction action = new MatchAction(request);
int add = action.getParameterInt("add");
int del = action.getParameterInt("del");
String tip = "";
MatchUser matchUser = null;
if (add > 0){
	matchUser = MatchAction.getMatchUser(add);
	if (matchUser == null){
		tip = "用户不存在.";
	} else if(matchUser.getChecked() == 3){
		tip = "该用户已被取消资格.";
	} else {
		if (!MatchAction.recoList.contains(new Integer(matchUser.getUserId()))){
			MatchAction.recoList.add(new Integer(matchUser.getUserId()));
			SqlUtil.executeUpdate("insert into match_reco (user_id) values (" + matchUser.getUserId() + ")",5);
		}
	}
}
if (del > 0){
	MatchAction.recoList.remove(new Integer(del));
	SqlUtil.executeUpdate("delete from match_reco where user_id=" + del,5);
}
int size = MatchAction.recoList.size();
String photoAddress = "";
%>
<html>
	<head>
		<title>审核参赛用户</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a><br/>目前总数:<%=size %><br/>
		<%if(!"".equals(tip)){%><%=tip%><br/><%tip="";}%>
		<form action="recommand.jsp" method="post">
			请输入UID:<input type="text" name="add" />
			<input type="submit" value="提交" />
		</form>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>昵称</font>
				</td>
				<td align=center>
					<font color=#1A4578>照片</font>
				</td>
				<td align=center>
					<font color=#1A4578>操作</font>
				</td>
			</tr>
			<% for (int i = 0 ; i < size ; i++){
				matchUser = MatchAction.getMatchUser(((Integer)MatchAction.recoList.get(i)).intValue());
				if (matchUser != null){
					%><tr>
					<td><%=UserInfoUtil.getUser(matchUser.getUserId()).getNickNameWml()%></td>
					<td><img src="<%=action.getCurrentPhoto(matchUser,false)%>" alt="无法显示" /></td>
					<td><a href="recommand.jsp?del=<%=matchUser.getUserId()%>" onclick="return confirm('确认删除？')">删除</a></td>
			  </tr><%
				}
			}
			%>
		</table>
	</body>
</html>