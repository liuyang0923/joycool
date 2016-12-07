<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; %>
<%  AstroAction action = new AstroAction(request);
	int submit = action.getParameterInt("s");
	String tip = "";
	if (submit == 1){
		int xz = action.getParameterInt("xz");
		int xz2 = action.getParameterInt("xz2");
		int exp = action.getParameterInt("exp");
		String proportion = action.getParameterString("proportion").trim();
		String content = action.getParameterString("content").trim();
		int flag = action.getParameterInt("flag");
		if ("".equals(proportion)){
			tip = "请输入比重.";
		}
		if ("".equals(content)){
			tip = "请输入内容.";
		}
		if ("".equals(tip)){
			AstroSupei supei = new AstroSupei();
			supei.setAstro1(xz);
			supei.setAstro2(xz2);
			supei.setExp(exp);
			supei.setProportion(proportion);
			supei.setContent(content);
			supei.setFlag(flag);
			action.service.addSupei(supei);
		}
	}
%>
<html>
	<head>
		<title>输入星座速配</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<font color="red"><b><%=tip%></b></font><br/>
		<a href="astroIndex.jsp">回首页</a><br/>
		说明:flag=0,昨说明astro1是男玩家的星座,astro2是女玩家的星座.flag=1则相反.<br/>
		<form action="astroInputSupei.jsp?s=1" method = "post">
			星座1:
			<select name="xz">
				<option value="1">白羊座</option>
				<option value="2">金牛座</option>
				<option value="3">双子座</option>
				<option value="4">巨蟹座</option>
				<option value="5">狮子座</option>
				<option value="6">处女座</option>
				<option value="7">天秤座</option>
				<option value="8">天蝎座</option>
				<option value="9">射手座</option>
				<option value="10">摩羯座</option>
				<option value="11">水瓶座</option>
				<option value="12">双鱼座</option>
			</select><br/>
			星座2:
			<select name="xz2">
				<option value="1">白羊座</option>
				<option value="2">金牛座</option>
				<option value="3">双子座</option>
				<option value="4">巨蟹座</option>
				<option value="5">狮子座</option>
				<option value="6">处女座</option>
				<option value="7">天秤座</option>
				<option value="8">天蝎座</option>
				<option value="9">射手座</option>
				<option value="10">摩羯座</option>
				<option value="11">水瓶座</option>
				<option value="12">双鱼座</option>
			</select><br/>
			指数:<input type="text" name="exp" size=36%><br/>
			比重:<input type="text" name="proportion" size=36%><br/>
			内容:<br/>
			<textarea name="content" cols="80" rows="10"></textarea><br/>
			类型:<select name="flag">
					<option value="0">0</option>
					<option value="1">1</option>
				</select><br/>
			<input type=submit value="提交"/>
		</form>
	</body>
</html>