<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; %>
<%  ExamAction action = new ExamAction(request);
	int submit = action.getParameterInt("s");
	String tip = "";
	String title = "";
	String content = "";
	int type = action.getParameterInt("type");
	if (type==0){type=10;}
	int flag = action.getParameterInt("flag");
	if (flag==0){flag=1;}
	if (submit == 1){
		title = action.getParameterString("title");
		content = action.getParameterString("content");
		if ("".equals(title) || title.length() > 20 ){
			tip ="没有输入标题，或标题超过20字。";	
		}
		if ("".equals(content)){
			tip ="没有输入内容。";
		}
		if ("".equals(tip)){
			ExamLib lib = new ExamLib();
			lib.setTitle(title);
			lib.setContent(content);
			lib.setType(action.getParameterInt("type"));
			lib.setDel(0);
			lib.setFlag(action.getParameterInt("flag"));
			boolean result = action.addLib(lib);
			if (result){
				tip = "已存入题库。";
				title="";
				content="";
			} else {
				tip = (String)request.getAttribute("tip");
			}
		}
	}
%>
<html>
	<head>
		<title>备战考试->题库加题</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<font color="red"><b><%=tip%></b></font><br/>
		<a href="index.jsp"><-返回首页</a><br/>
		<form action="add.jsp?s=1" method = "post">
			标题:<input type="text" name="title" size=36 value="<%=title %>"/><br/>
			内容:<br/><textarea name="content" cols="80" rows="10"><%=content %></textarea><br/>
			科目:<select name="type">
				<option value="10">数学->公式大全</option>
				<option value="11">数学->基本定理</option>
				<option value="12">数学->经典例题</option>
				<option value="13">语文->背诵默写</option>
				<option value="14">语文->文学常识</option>
				<option value="15">语文->范文例句</option>
				<option value="16">英语->词汇表</option>
				<option value="17">英语->语法大全</option>
				<option value="18">英语->范文例句</option>
				<option value="19">物理->常量</option>
				<option value="20">物理->公式大全</option>
				<option value="21">物理->基本定理</option>
				<option value="22">物理->经典例题</option>
				<option value="23">化学->公式大全</option>
				<option value="24">化学->基本定理</option>
				<option value="25">化学->常量</option>
				<option value="26">化学->经典实验</option>
				<option value="6">生物</option>
				<option value="7">历史</option>
				<option value="8">政治</option>
				<option value="9">地理</option>
			</select><br/>
			<script language="javascript">				
				document.forms[0].type.value="<%=type%>";
			</script>
			学年:<select name="flag">
				<option value="1">初中</option>
				<option value="2">高中</option>
			</select><br/>
			<script language="javascript">				
				document.forms[0].flag.value="<%=flag%>";
			</script>
			<input type=submit value="提交"/>
		</form>
	</body>
</html>