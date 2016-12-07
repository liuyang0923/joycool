<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; %>
<%  AstroAction action = new AstroAction(request);
	int submit = action.getParameterInt("s");
	String tip = "";
	if (submit == 1){
		String title = action.getParameterString("title").trim();
		if ("".equals(title) || title.length() > 255){
			tip ="没添写标题或标题超过255字";
		}
		String content = action.getParameterString("story").trim();
		if ("".equals(content)){
			tip ="内容不可以空";
		}
		int xingzuo = action.getParameterInt("xz");
		int flag = action.getParameterInt("flag");
		if ("".equals(tip)){
			AstroStory story = new AstroStory();
			story.setTitle(title);
			story.setContent(content);
			story.setAstroId(xingzuo);
			story.setFlag(flag);
			action.service.addStory(story);
		}
	}
%>
<html>
	<head>
		<title>输入星座故事、传说</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<font color="red"><b><%=tip%></b></font><br/>
		<a href="astroIndex.jsp">回首页</a><br/>
		<form action="astroInputStory.jsp?s=1" method = "post">
			标题:<input type="text" name="title" size=36%>(限255字)<br/>
			故事:<br/>
			<textarea name="story" cols="80" rows="10"></textarea><br/>
			星座:
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
			类型:
			<select name="flag">
				<option value="0">星座说明</option>
				<option value="1">普通文章</option>
				<option value="2">给男生看的文章</option>
				<option value="3">给女生看的文章</option>
			</select><br/><br/>
			<input type=submit value="提交文章"/>
		</form>
	</body>
</html>