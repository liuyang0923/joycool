<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 30;%>
<%ExamAction action = new ExamAction(request);
  int type = action.getParameterInt("type");	//类别
  int flag = action.getParameterInt("flag");	//学年	1:初中 2:高中
  if (type < 6){type = 10;}
  if (flag <= 0){flag = 1;}
  int submit = action.getParameterInt("s");
  String tip = "";
  int totalCount = 0;
  PagingBean paging = null;
  int pageNow = 0;
  List libList = null;
  ExamLib lib = null;
  if (submit == 1 && type > 0 && flag > 0){
	  if (type > ExamAction.getSubjectTypeMap().size()){
		  tip = "科目类别错误";
	  }else if (flag > 2){
		  tip = "学年选择错误";
	  } else {
		  totalCount = SqlUtil.getIntResult("select count(id) from exam_lib where type=" + type + " and flag=" + flag, 5);
		  paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
		  pageNow = paging.getCurrentPageIndex();
		  libList = ExamAction.service.getLibList(" type=" + type + " and flag=" + flag + " order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	  }
  }
%>
<html>
	<head>
		<title>备战考试->题库管理</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<font color="red"><%=tip%></font><br/>
		<a href="index.jsp"><-返回首页</a><br/>
		<form action="modify.jsp?s=1" method = "post">
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
			</select>
			<script language="javascript">				
				document.forms[0].type.value="<%=type%>";
			</script>
			学年:<select name="flag">
				<option value="1">初中</option>
				<option value="2">高中</option>
			</select>
			<script language="javascript">				
				document.forms[0].flag.value="<%=flag%>";
			</script>
			<input type=submit value="选择"/>
		</form>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>标题</font>
				</td>
				<td align=center>
					<font color=#1A4578>内容</font>
				</td>
				<td align=center>
					<font color=#1A4578>创建时间</font>
				</td>
				<td align=center>
					<font color=#1A4578>类型</font>
				</td>
				<td align=center>
					<font color=#1A4578>删除情况</font>
				</td>
				<td align=center>
					<font color=#1A4578>学年</font>
				</td>
				<td align=center>
					<font color=#1A4578>管理</font>
				</td>
			</tr>
			<%if(libList != null){
				for (int i = 0; i < libList.size(); i++) {
					lib = (ExamLib) libList.get(i);
					%><tr>
						<td><%=lib.getId()%></td>
						<td><%=StringUtil.toWml(lib.getTitle()) %></td>
						<td><%=StringUtil.toWml(StringUtil.limitString(lib.getContent(),40))%></td>
						<td><%=DateUtil.formatDate2(lib.getCreateTime()) %></td>
						<td><%=lib.getType() %></td>
						<td><%if(lib.getDel() == 1){%><font color="red">已删</a><%}else{%>未删<%}%></td>
						<td><%=lib.getFlag()%></td>
						<td><a href="modify2.jsp?lid=<%=lib.getId() %>">管理</a></td>
					</tr>
					<%
				}
			}
			%>
		</table>
		<%if (paging != null){
			%><%=paging.shuzifenye("modify.jsp?s=" + submit + "&type=" + type + "&flag=" + flag, true, "|", response)%><%	
		}%>
	</body>
</html>