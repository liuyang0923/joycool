<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static int COUNT_PRE_PAGE=30; 
	static String[] type = {"星座说明","普通文章","给男生看的文章","给女生看的文章"};%>
<%  AstroAction action = new AstroAction(request);
	ArrayList storyList = null;
	int astroId = 1;
	AstroStory astroStory = null;
	int modifyId = action.getParameterInt("mid");
	int submit = action.getParameterInt("s");
	int del = action.getParameterInt("d");
	String tip = "";
	if (del > 0){
		SqlUtil.executeUpdate("delete from astro_story where id=" + del,4);
	}
	if (submit == 1){
		int id = action.getParameterInt("id");
		astroId = action.getParameterInt("xz");
		String title = action.getParameterNoEnter("title").trim();
		String story = action.getParameterString("story").trim();
		int flag = action.getParameterInt("flag");
		if ("".equals(title) || title.length() > 255){
			tip = "没有输入标题或标题太长.";
		}
		if ("".equals(story)){
			tip = "没有输入文章.";
		}
		if ("".equals(tip)){
			AstroStory storyBean = new AstroStory();
			storyBean.setId(id);
			storyBean.setAstroId(astroId);
			storyBean.setTitle(title);
			storyBean.setContent(story);
			storyBean.setFlag(flag);
			action.service.modifyStory(storyBean);
		}
	}
	
	// 选择了要修改的文章
	if (modifyId > 0){
		astroStory = action.service.getStory(" id=" + modifyId);
		if (astroStory == null){
			tip = "所选的文章不存在.";
		}
	}
	
	int totalCount = 100000;
	PagingBean paging = paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();
				
	storyList = (ArrayList)action.service.getStoryList("1 order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	astroStory = action.service.getStory(" id = " + modifyId);
	if (modifyId > 0 && astroStory == null){
		tip ="所选的信息不存在。";
	}
%>
<html>
	<head>
		<title>修改星座文章</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body onload="changeColor(<%=astroId %>);">
		<a href="astroIndex.jsp">回首页</a><br/><b>修改星座文章</b><br/>
<% if ("".equals(tip)){
%><%if (modifyId > 0 && astroStory != null){
			%><form action="astroModifyStory.jsp?s=1" method = "post">
			<input type="hidden" name="id" value=<%=modifyId%>>ID:<%=astroStory.getId()%><br/>
			<input type="text" name="title" size="36%" value="<%=astroStory.getTitle()%>" />(255字内)<br/>
			故事:<br/>
			<textarea name="story" cols="80" rows="10"><%=astroStory.getContent()%></textarea><br/>
			星座:<select name="xz">
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
			<script language="javascript">				
				document.forms[0].xz.value="<%=astroStory.getAstroId()%>";
			</script>
			类型:<select name="flag">
				<option value="0">星座说明</option>
				<option value="1">普通文章</option>
				<option value="2">给男生看的文章</option>
				<option value="3">给女生看的文章</option>
			</select>
			<script language="javascript">				
				document.forms[0].flag.value="<%=astroStory.getFlag()%>";
			</script><br/><br/>
			<input type=submit value="更改"/>
		</form>
		<%}%>
		<%if (storyList.size() > 0){
				%><table border=1 width=100% align=center>
						<tr bgcolor=#C6EAF5>
							<td align=center>
								<font color=#1A4578>ID</font>
							</td>
							<td align=center>
								<font color=#1A4578>星座</font>
							</td>
							<td align=center>
								<font color=#1A4578>标题</font>
							</td>
							<td align=center>
								<font color=#1A4578>内容</font>
							</td>
							<td align=center>
								<font color=#1A4578>类型</font>
							</td>
							<td align=center>
								<font color=#1A4578>修改</font>
							</td>
						</tr>
						<%for (int i = 0;i < storyList.size(); i++){
							astroStory = (AstroStory)storyList.get(i);
							%><tr>
								<td><%=astroStory.getId()%></td>
								<td><%=action.getAstroNameNoDate(astroStory.getAstroId())%></td>
								<td><%=StringUtil.toWml(StringUtil.limitString(astroStory.getTitle(),20))%></td>
								<td><%=StringUtil.toWml(StringUtil.limitString(astroStory.getContent(),20))%></td>
								<td><%=type[astroStory.getFlag()]%></td>
								<td><a href="astroModifyStory.jsp?mid=<%=astroStory.getId()%>">改</a>|<a href="astroModifyStory.jsp?d=<%=astroStory.getId()%>" onClick="return confirm('真的要删除这篇文章吗?')">删</a></td>
					 		</tr><%
						}
						%>
				  </table>
		<%}%><%=paging.shuzifenye("astroModifyStory.jsp", false, "|", response)%>
<%
} else {
	%><font color="red"><b><%=tip%></b></font><br/><a href="astroModifyStory.jsp">返回</a><br/><%
}
%>
	</body>
</html>