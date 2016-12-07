<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static int COUNT_PRE_PAGE=30;%>
<%  AstroAction action = new AstroAction(request);
	int submit = action.getParameterInt("s");
	String tip = "";
	int randomIndex = RandomUtil.nextIntNoZero(12);
	int year = action.getYearNow(System.currentTimeMillis());
	int month = action.getMonthNow(System.currentTimeMillis());
	int day = action.getDayNow(System.currentTimeMillis());
	int astroId = 1;	// 默认为白羊座
	if (submit == 1){
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
		year = action.getParameterInt("year");
		month = action.getParameterInt("month");
		day = action.getParameterInt("day");
		String content = action.getParameterString("content");
		int flag = action.getParameterInt("flag");
		if ("".equals(color)){
			tip = "没有写幸运颜色.";
		}
		if (!action.isDateExist(year,month,day)){
			tip = "日期选择错误.";
		}
		if ("".equals(content)){
			tip = "运势内容不可为空.";
		}
		if ("".equals(tip)){
			String dateTmp = "";
			AstroCess cess = new AstroCess();
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
			action.service.addCess(cess);
		}
	}
%>
<html>
	<head>
		<title>输入每日运势</title>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
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
	<body onload="changeColor(<%=astroId %>);">
		<font color="red"><b><%=tip%></b></font><br/>
		<a href="astroIndex.jsp">回首页</a>|<a href="astroInputCess.jsp">刷新</a><br/>
		<form action="astroInputCess.jsp?s=1" method = "post">
			星        座:
			<select name="xz" onChange="changeColor(xz.value)">
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
			</select>
			<script language="javascript">document.forms[0].xz.value="<%=astroId%>";</script>
			<br/>
			综合运势:<input type="text" name="all" size=20 value=<%=RandomUtil.nextIntNoZero(5)%>><br/>
			爱情运势:<input type="text" name="love" size=20 value=<%=RandomUtil.nextIntNoZero(5)%>><br/>
			事业运势:<input type="text" name="career" size=20 value=<%=RandomUtil.nextIntNoZero(5)%>><br/>
			理财运势:<input type="text" name="fain" size=20 value=<%=RandomUtil.nextIntNoZero(5)%>><br/>
			健康运势:<input type="text" name="health" size=20 value=<%=RandomUtil.nextIntNoZero(5)%>><br/>
			商业运势:<input type="text" name="buss" size=20 value=<%=RandomUtil.nextIntNoZero(5)%>><br/>
			幸运颜色:<input type="text" name="color" size=20 value=""><br/>
			幸运数字:<input type="text" name="num" size=20 value=<%=RandomUtil.nextIntNoZero(100)%>><br/>
			速配星座:<select name="xz2">
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
				document.forms[0].xz2.value="<%=randomIndex%>";
			</script>
			日期：年<select name="year" >
				<% for (int i=2009;i<=2029;i++){
						%><option value="<%=i%>"><%=i%></option><%
				   }%>
			</select>
			<script language="javascript">document.forms[0].year.value="<%=year%>";</script>
			月<select name="month" >
				<% for (int i=1;i<=12;i++){
						%><option value="<%=i%>"><%=i%></option><%
				   }%>
			</select>
			<script language="javascript">document.forms[0].month.value="<%=month%>";</script>
			日<select name="day" >
				<% for (int i=1;i<=31;i++){
						%><option value="<%=i%>"><%=i%></option><%
				   }%>
			</select>
			<script language="javascript">document.forms[0].day.value="<%=day%>";</script>
			<br/>
			运         势：<br/><textarea name="content" cols="80" rows="10"></textarea><br/>
			标         志:<input type="text" name="flag" size=20 value="0"><br/>
			<input type=submit value="提交"/>
		</form>
		
	</body>
</html>