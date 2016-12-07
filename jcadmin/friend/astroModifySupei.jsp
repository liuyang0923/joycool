<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static int COUNT_PRE_PAGE=30;%>
<%  AstroAction action = new AstroAction(request);
	ArrayList supeiList = null;
	AstroSupei supei = null;
	int modifyId = action.getParameterInt("mid");
	int submit = action.getParameterInt("s");
	int del = action.getParameterInt("d");
	int totalCount = 100000;
	String tip = "";
	if (del > 0){
		SqlUtil.executeUpdate("delete from astro_sp where id=" + del,4);
	}
	if (modifyId > 0){
		supei = action.service.getSupei(" id=" + modifyId);
		if (supei == null){
			tip = "速配信息不存在";
		}
	}
	if (submit > 0){
		AstroSupei supeiBean = action.service.getSupei(" id=" + submit);
		if (supeiBean != null){
			supeiBean = new AstroSupei();
			supeiBean.setId(submit);
			supeiBean.setAstro1(action.getParameterInt("xz1"));
			supeiBean.setAstro2(action.getParameterInt("xz2"));
			int exp = action.getParameterInt("exp");
			String proportion = action.getParameterNoEnter("proportion").trim();
			String content = action.getParameterString("content").trim();
			if(exp < 0){
				tip = "指数错误.";
			}
			if ("".equals(proportion) || proportion.length() > 45){
				tip = "没有输入比重或内容过长.";
			} else if ("".equals(content)){
				tip = "没有输入内容.";
			} else {
				supeiBean.setExp(exp);
				supeiBean.setProportion(proportion);
				supeiBean.setContent(content);
				supeiBean.setFlag(action.getParameterInt("flag"));
				action.service.modifySupei(supeiBean);
			}
		} else {
			tip = "要修改的速配信息不存在.";			
		}
	}
	PagingBean paging = paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();
	supeiList = (ArrayList)action.service.getSupeiList("1 order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>
<html>
	<head>
		<title>修改星座速配</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="astroIndex.jsp">回首页</a><br/>
<% if ("".equals(tip)){
%>说明:flag=0,则说明astro1是男玩家的星座,astro2是女玩家的星座.flag=1则相反.<br/><%
if (modifyId > 0 && supei != null){
%><form action="astroModifySupei.jsp?s=<%=supei.getId()%>" method="post">
星座1:<select name="xz1">
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
	document.forms[0].xz1.value="<%=supei.getAstro1()%>";
</script>
星座2:<select name="xz2">
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
	document.forms[0].xz2.value="<%=supei.getAstro2()%>";
</script>
指数:<input type="text" name="exp" value="<%=supei.getExp()%>"/><br/>
比重:<input type="text" name="proportion" value="<%=supei.getProportion()%>"/>(45字以内)<br/>
内容:<br/><textarea name="content" cols="80" rows="10"><%=supei.getContent()%></textarea><br/>
类型:<select name="flag">
<option value="0">0</option>
<option value="1">1</option>
</select>
<script language="javascript">				
	document.forms[0].flag.value="<%=supei.getFlag()%>";
</script><br/>
<input type="submit" value="更改"/>
</form>
<%	
}
%><%if (supeiList.size() > 0){
%><table border=1 width=100% align=center>
		<tr bgcolor=#C6EAF5>
			<td align=center>
				<font color=#1A4578>ID</font>
			</td>
			<td align=center>
				<font color=#1A4578>星座1</font>
			</td>
			<td align=center>
				<font color=#1A4578>星座2</font>
			</td>
			<td align=center>
				<font color=#1A4578>指数</font>
			</td>
			<td align=center>
				<font color=#1A4578>比重</font>
			</td>
			<td align=center>
				<font color=#1A4578>内容</font>
			</td>
			<td align=center>
				<font color=#1A4578>类型</font>
			</td>
			<td align=center>
				<font color=#1A4578>操作</font>
			</td>
		</tr>
		<%for (int i = 0;i < supeiList.size(); i++){
			supei = (AstroSupei)supeiList.get(i);
			%><tr>
				<td><%=supei.getId()%></td>
				<td><%=action.getAstroNameNoDate(supei.getAstro1())%></td>
				<td><%=action.getAstroNameNoDate(supei.getAstro2())%></td>
				<td><%=supei.getExp()%></td>
				<td><%=supei.getProportion()%></td>
				<td><%=StringUtil.toWml(StringUtil.limitString(supei.getContent(),20))%></td>
				<td><%=supei.getFlag()%></td>
				<td><a href="astroModifySupei.jsp?mid=<%=supei.getId()%>">改</a>|<a href="astroModifySupei.jsp?d=<%=supei.getId()%>" onClick="return confirm('真的要删除吗?')">删</a></td>
	 		</tr><%
		}
		%>
  </table>
<%}%><%=paging.shuzifenye("astroModifySupei.jsp", false, "|", response)%>
<%
} else {
	%><font color="red"><b><%=tip%></b></font><br/><a href="astroModifySupei.jsp">返回</a><br/><%
}
%>
	</body>
</html>