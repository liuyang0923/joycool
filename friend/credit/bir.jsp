<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   List errorList = new ArrayList();
   int in = action.getParameterInt("in");
   int modify = action.getParameterInt("m");	//是否修改
   int aim = action.getParameterInt("aim");
   UserBase userBase = CreditAction.getUserBaseBean(action.getLoginUser().getId());
   if (aim > 4 || aim < 1){
	   aim = 4;
   }
   if (in == 1){
	   int year = action.getParameterInt("year");
	   if (year < 1940 || year > 2000){
		   errorList.add("年份格式不对.(1940-2000)");
	   }
	   int month = action.getParameterInt("month");
	   if (month < 1 || month > 12){
		   errorList.add("月份格式不对.(1-12)");
	   }
	   int day = action.getParameterInt("day");
	   if (day < 1 || day > 31){
		   errorList.add("日期格式不对.(1-31)");
	   }
	   if (!action.isDateExist(year,month,day)){
		   errorList.add("您所输入的日期不存在.");
	   }
	   int hour = action.getParameterInt("hour");
	   if (hour < 0 || hour > 23){
		   errorList.add("时间格式不对.(0-23)");
	   }
	   int stature = action.getParameterInt("st");
	   if (stature < 100 || stature > 220){
		   errorList.add("身高范围不对.(100-220)");
	   }
	   if (errorList.size() == 0){
		   Lunar lunar = new Lunar(year,month,day);
		   if (userBase == null){
			   userBase = new UserBase();
			   userBase.setUserId(action.getLoginUser().getId());
			   userBase.setBirYear(year);
			   userBase.setBirMonth(month);
			   userBase.setBirDay(day);
			   userBase.setBirHour(hour);
			   userBase.setStature(stature);
			   userBase.setAnimals(lunar.animalsYear());
			   userBase.setAstro(CreditAction.getAstroIdByDate(month,day));
			   userBase.setAim(aim);
			   userBase.setGender(UserInfoUtil.getUser(action.getLoginUser().getId()).getGender());
			   // 放入数据库中
			   CreditAction.service.createUserBase(userBase);
			   // 放入缓存中
			   CreditAction.userBaseCache.put(new Integer(userBase.getUserId()),userBase);
//			   SqlUtil.executeUpdate("insert into credit_user_base (user_id,bir_year,bir_month,bir_day,bir_hour,stature,animals,astro,aim,gender) values (" + action.getLoginUser().getId() + "," + year + "," + month + "," + day + "," + hour + "," + stature + ",'" + StringUtil.toSql(lunar.animalsYear()) + "'," + action.getAstroIdByDate(month,day) + "," + aim + "," + UserInfoUtil.getUser(action.getLoginUser().getId()).getGender() + ")",5);
		   } else {
			   SqlUtil.executeUpdate("update credit_user_base set bir_year=" + year + ",bir_month=" + month + ",bir_day=" + day + ",bir_hour=" + hour + ",stature=" + stature + ",animals='" + StringUtil.toSql(lunar.animalsYear()) + "',astro=" + CreditAction.getAstroIdByDate(month,day) + ",aim=" + aim + " where user_id=" + action.getLoginUser().getId(),5);
		   	   userBase.setBirYear(year);
		   	   userBase.setBirMonth(month);
		   	   userBase.setBirDay(day);
		   	   userBase.setBirHour(hour);
		   	   userBase.setStature(stature);
		   	   userBase.setAnimals(lunar.animalsYear());
		   	   userBase.setAstro(CreditAction.getAstroIdByDate(month,day));
		   	   userBase.setAim(aim);
		   	   CreditAction.userBaseCache.put(new Integer(userBase.getUserId()),userBase);
		   }
		   action.updateModifyTime(1);
		   if (modify==1){
			    out.clearBuffer();
		   		response.sendRedirect("navi.jsp");
		   } else {
			   	out.clearBuffer();
			    response.sendRedirect("select.jsp?t=1");
		   }
		   return;
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<% if (errorList.size() == 0){
%>请填写的您出生时间:<br/>
<input name="year" value="" maxlength="4" format="*N"/>年<input name="month" value="" maxlength="2" format="*N"/>月<br/>
<input name="day" value="" maxlength="2" format="*N"/>日<input name="hour" value="" maxlength="4" format="*N"/>时<br/>
请填写您的身高:<br/>
<input name="st" value="" maxlength="3" />cm<br/>
交友目的:<select name="aim" value="<%if(userBase==null||userBase.getAim()==0){%>4<%}else{%><%=userBase.getAim()%><%}%>">
<option value="1">恋人</option>
<option value="2">知已</option>
<option value="3">玩伴</option>
<option value="4">其它</option>
</select><br/>
<anchor>
	确认
	<go href="bir.jsp?in=1&amp;m=<%=modify%>" method="post">
		<postfield name="year" value="$year"/>
		<postfield name="month" value="$month"/>
		<postfield name="day" value="$day"/>
		<postfield name="hour" value="$hour"/>
		<postfield name="st" value="$st"/>
		<postfield name="aim" value="$aim"/>
	</go>
</anchor><br/>
<%	
} else {
	for(int i = 0 ; i < errorList.size() ; i++){
		%><%=errorList.get(i)%><br/><%
	}
%>[<%=action.getParameterNoCtrl("year")%>]年[<%=action.getParameterNoCtrl("month")%>]月<br/>
[<%=action.getParameterNoCtrl("day")%>]日[<%=action.getParameterNoCtrl("hour")%>]时<br/><br/>
[<%=action.getParameterNoCtrl("st")%>]cm<br/><a href="bir.jsp?m=<%=modify %>">确认</a><br/>
<%
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>