<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; %>
<%  AstroAction action = new AstroAction(request);
	ArrayList cessList = null;
	int astroId = 1;
	int modifyId = action.getParameterInt("mid");
	int submit = action.getParameterInt("s");
	String tip = "";
	if (submit == 1){
		int id = action.getParameterInt("id");
		astroId = action.getParameterInt("xz");
		int all = action.getParameterInt("all");
		int love = action.getParameterInt("love");
		int career = action.getParameterInt("career");
		int fain = action.getParameterInt("fain");
		int health = action.getParameterInt("health");
		int buss = action.getParameterInt("buss");
		String color = action.getParameterString("color");
		int num = action.getParameterInt("num");
		int otherAstro = action.getParameterInt("xz2");
		int year = action.getParameterInt("year");
		int month = action.getParameterInt("month");
		int day = action.getParameterInt("day");
		String content = action.getParameterString("content");
		int flag = action.getParameterInt("flag");
		if ("".equals(color)){
			tip = "没有写幸运颜色.";
		}
		if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31){
			tip = month + "月没有31日.";
		}
		if (month == 2){
			if (year % 4 != 0){
				if (day > 28){
					tip = year + "年的2月没有" + day + "日.";
				}
			} else {
				if (day > 29){
					tip = year + "年的2月没有" + day + "日.";
				}
			}
		}
		if ("".equals(content)){
			tip = "运势内容不可为空.";
		}
		if ("".equals(tip)){
			String dateTmp = "";
			AstroCess cess = new AstroCess();
			cess.setId(id);
			cess.setAstroId(astroId);
			cess.setAll(all);
			cess.setLove(love);
			cess.setCareer(career);
			cess.setFain(fain);
			cess.setHealth(health);
			cess.setBusiness(buss);
			cess.setColor(color);
			cess.setNum(num);
			cess.setOtherAstro(otherAstro);
			dateTmp += year + "-";
			if (month < 10){
				dateTmp += "0" + month + "-";
			} else {
				dateTmp += month + "-";
			}
			if (day < 10){
				dateTmp += "0" + day;
			} else {
				dateTmp += day;
			}
			cess.setDatetimeString(dateTmp);
			cess.setContent(content);
			cess.setFlag(flag);
			action.service.modifyCess(cess);		//======这里写修改运势的方法======
		}
	}
	
	int totalCount = 100000;
	PagingBean paging = paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();
				
	cessList = (ArrayList)action.service.getCessList("1 order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	AstroCess cess = null;
	cess = action.service.getCess(" id = " + modifyId);
	int year = 0;
	int month = 0;
	int day = 0;
	if (modifyId > 0 && cess == null){
		tip ="所选的信息不存在。";
	}
%>
<html>
	<head>
		<title>修改每日运势</title>
<script type="text/javascript">
function changeColor(id){
	color = new Array("","金黄色,宝蓝色","黄色,红色","淡蓝色,天蓝色,白色","深灰色,黑色,淡紫色","白色,黑色","红色,奶油色,金黄色","淡黄,橘橙色","红绿色,黄色,粉红色","深红色,金黄色","粉红色","橘橙,粉红色","紫红色,桔黄色");
	if (id < 1 || id > 12){
		id = 1;
	}
	document.forms[0].color.value=color[id];
}
</script>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body onload="changeColor(<%=astroId %>);">
		<a href="astroIndex.jsp">回首页</a><br/><b>修改每日运势</b><br/>
<% if ("".equals(tip)){
%><%if (modifyId > 0 && cess != null){
			String date = DateUtil.formatDate(new Date(cess.getDatetime()));
			String dateArray[] = date.split("-");
			year = Integer.parseInt(dateArray[0]);
			month = Integer.parseInt(dateArray[1]);
			day = Integer.parseInt(dateArray[2]);
			
			%><form action="astroModifyCess.jsp?s=1" method = "post">
			<input type="hidden" name="id" value=<%=modifyId%>>ID:<%=modifyId%><br/>
			星座:<select name="xz" onChange="changeColor(xz.value)">
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
				document.forms[0].xz.value="<%=cess.getAstroId()%>";
			</script>
			综合运势:<input type="text" name="all" size=20 value=<%=cess.getAll()%>><br/>
			爱情运势:<input type="text" name="love" size=20 value=<%=cess.getLove()%>><br/>
			事业运势:<input type="text" name="career" size=20 value=<%=cess.getCareer()%>><br/>
			理财运势:<input type="text" name="fain" size=20 value=<%=cess.getFain()%>><br/>
			健康运势:<input type="text" name="health" size=20 value=<%=cess.getHealth()%>><br/>
			商业运势:<input type="text" name="buss" size=20 value=<%=cess.getBusiness()%>><br/>
			幸运颜色:<input type="text" name="color" size=20 value=<%=StringUtil.toWml(cess.getColor())%>><br/>
			幸运数字:<input type="text" name="num" size=20 value=<%=cess.getNum()%>><br/>
			速配星座:	<select name="xz2">
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
				document.forms[0].xz2.value="<%=cess.getOtherAstro()%>";
			</script>
			日期：年:<select name="year" >
				<% for (int i=2009;i<=2029;i++){
						%><option value="<%=i%>"><%=i%></option><%
				   }%>
			</select>
			<script language="javascript">				
				document.forms[0].year.value="<%=year%>";
			</script>
			月：<select name="month" >
				<% for (int i=1;i<=12;i++){
						%><option value="<%=i%>"><%=i%></option><%
				   }%>
			</select>
			<script language="javascript">				
				document.forms[0].month.value="<%=month%>";
			</script>
			日:<select name="day" >
				<% for (int i=1;i<=31;i++){
						%><option value="<%=i%>"><%=i%></option><%
				   }%>
			</select><br/>
			<script language="javascript">				
				document.forms[0].day.value="<%=day%>";
			</script>
			运         势：<br/><textarea name="content" cols="80" rows="10"><%=cess.getContent()%></textarea><br/>
			标         志:<input type="text" name="flag" size=20 value=<%=cess.getFlag()%>><br/>
			<input type=submit value="更改"/>
		</form>
		<%}%>
		<%if (cessList.size() > 0){

				%><table border=1 width=100% align=center>
						<tr bgcolor=#C6EAF5>
							<td align=center>
								<font color=#1A4578>ID</font>
							</td>
							<td align=center>
								<font color=#1A4578>星座</font>
							</td>
							<td align=center>
								<font color=#1A4578>综合</font>
							</td>
							<td align=center>
								<font color=#1A4578>爱情</font>
							</td>
							<td align=center>
								<font color=#1A4578>事业</font>
							</td>
							<td align=center>
								<font color=#1A4578>理财</font>
							</td>
							<td align=center>
								<font color=#1A4578>健康</font>
							</td>	
							<td align=center>
								<font color=#1A4578>商业</font>
							</td>	
							<td align=center>
								<font color=#1A4578>幸运色</font>
							</td>	
							<td align=center>
								<font color=#1A4578>幸运数字</font>
							</td>	
							<td align=center>
								<font color=#1A4578>速配星座</font>
							</td>	
							<td align=center>
								<font color=#1A4578>日期</font>
							</td>	
							<td align=center>
								<font color=#1A4578>运势</font>
							</td>	
							<td align=center>
								<font color=#1A4578>标志</font>
							</td>
							<td align=center>
								<font color=#1A4578>操作</font>
							</td>
						</tr>
						<%for (int i = 0;i < cessList.size(); i++){
							cess = (AstroCess)cessList.get(i);
							%><tr>
								<td><%=cess.getId()%></td>
								<td><%=action.getAstroNameNoDate(cess.getAstroId())%></td>
								<td><%=cess.getAll()%></td>
								<td><%=cess.getLove()%></td>
								<td><%=cess.getCareer()%></td>
								<td><%=cess.getFain()%></td>
								<td><%=cess.getHealth()%></td>
								<td><%=cess.getBusiness()%></td>
								<td><%=StringUtil.toWml(cess.getColor())%></td>
								<td><%=cess.getNum()%></td>
								<td><%=action.getAstroNameNoDate(cess.getOtherAstro())%></td>
								<td><%=DateUtil.formatDate(new Date(cess.getDatetime()))%></td>
								<td><%=StringUtil.toWml(StringUtil.limitString(cess.getContent(),20))%></td>
								<td><%=cess.getFlag()%></td>
								<td><a href="astroModifyCess.jsp?mid=<%=cess.getId()%>">改</a></td>
					 		</tr><%
						}
						%>
				  </table>
		<%}%><%=paging.shuzifenye("astroModifyCess.jsp", false, "|", response)%>
<%
} else {
	%><font color="red"><b><%=tip%></b></font><br/><a href="astroModifyCess.jsp">返回</a><br/><%
}
%>
	</body>
</html>