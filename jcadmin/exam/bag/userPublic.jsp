<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 30;%>
<%ExamAction action = new ExamAction(request);
  int submit = action.getParameterInt("s");
  int userId = action.getParameterInt("uid");
  int type = action.getParameterInt("type");
  int delAll = action.getParameterInt("delAll");
  int totalCount = 0;
  PagingBean paging = null;
  int pageNow = 0;
  List bagList = null;
  ExamBag bag = null;
  if (type < 6 || type > ExamAction.getSubjectTypeMap().size()){
	  type = 10;
  }
  if (delAll == 1){
	  	String[] deleteIds = request.getParameterValues("did");
		String ids = action.array2String(deleteIds, ",");
		if (!"".equals(ids)){
			SqlUtil.executeUpdate("update exam_bag set del=1 where id in(" + ids + ")",0);
		}
  }
  if (submit == 1 && type > 0){
	  totalCount = SqlUtil.getIntResult("select count(id) from exam_bag where que_type=" + type + " and user_id=" + userId, 0);
	  paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
	  pageNow = paging.getCurrentPageIndex();
	  bagList = ExamAction.service.getBagList(" que_type=" + type + " and user_id=" + userId + " order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
  }
%>
<html>
	<head>
		<title>备战考试->用户书包管理</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp"><-返回首页</a><br/>
		<table width="100%" style="border: none;">
			<tr>
				<td style="border: none;">
					<form method="post" action="userPublic.jsp?s=1">
						用户ID：<input type="text" name="uid" value="<%=userId%>"/>
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
						<input type="submit" value="查找">
					</form>
				</td>
			</tr>
		</table>
		<form action="userPublic.jsp?s=1&delAll=1&uid=<%=userId%>&type=<%=type%>" method="post"><input type="submit" value="删除所选" onclick="return confirm('确定删除？')"/>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户</font>
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
					<font color=#1A4578>删除情况</font>
				</td>
				<td align=center>
					<font color=#1A4578>FLAG</font>
				</td>
				<td align=center>
					<font color=#1A4578>删</font>
				</td>
				<td align=center>
					<font color=#1A4578>管理</font>
				</td>
			</tr>
			<%if(bagList != null && bagList.size() > 0){
				ExamType exType = null;
				for (int i = 0; i < bagList.size(); i++) {
					bag = (ExamBag) bagList.get(i);
					%><tr>
						<td><%=bag.getId()%></td>
						<td><a href="/user/ViewUserInfo.do?userId=<%=bag.getUserId()%>"><%=bag.getUserId()%></a></td>
						<td><%=StringUtil.toWml(bag.getTitle()) %></td>
						<td><%=StringUtil.toWml(StringUtil.limitString(bag.getContent(),40))%></td>
						<td><%=DateUtil.formatDate2(bag.getCreateTime()) %></td>
						<td><%if(bag.getDel() == 1){%><font color="red">已删</a><%}else{%>未删<%}%></td>
						<td><%=bag.getFlag()%></td>
						<td><input type=checkbox name="did" value="<%=bag.getId()%>"></td>
						<td><a href="userModify.jsp?bid=<%=bag.getId()%>">管理</a>|<a href="note.jsp?bid=<%=bag.getId()%>&type=<%=type%>&uid=<%=userId %>">留言</a></td>
					</tr>
					<%
				}
			}%>
		</table>
		</form>
		<%if(paging != null){
			%><%=paging.shuzifenye("userPublic.jsp?s=" + submit + "&type=" + type + "&uid=" + userId, true, "|", response)%><%	
		}%>
	</body>
</html>