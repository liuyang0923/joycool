<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.mental.*"%>
<% response.setHeader("Cache-Control","no-cache");
   MentalAction action = new MentalAction(request);
   MentalUser mentalUser = action.getCurrentUser();
   StringBuffer strBuffer = new StringBuffer();
   MentalProperty property = null;
   String tip = "";
//   int mode = action.getParameterInt("m");
//   if (mode < 0 || mode > 2){
//	   mode = 0;
//   }
   if (mentalUser == null || mentalUser.getQueNow() < 8){
	   tip = "用户不存在,或没有答完所有的题.";
   } else {
//	   if (mode == 1){
		   String tmp[] = mentalUser.getAnswer().split("\\|");
		   property = action.service.getProperty(" id=" + tmp[0]);
//	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友心理测试"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
//	if (mode == 0){
//		%>测试已完成.<br/><a href="result.jsp?m=1">提交查看结果</a><br/><%
//	} else if (mode == 1){
		if(property != null){
			%>所属类型:<%=StringUtil.toWml(property.getDescribe())%><br/><%
			List mateList = action.service.getPropertyList(" id in(" + property.getMate() + ")");
			if (mateList != null && mateList.size() > 0){
				%>匹配类型:<%
				for (int i = 0 ; i < mateList.size() ; i++){
					property = (MentalProperty)mateList.get(i);
					strBuffer.append(property.getDescribe() + ",");
				}
			}%><%=strBuffer.substring(0,strBuffer.length()-1)%><br/>匹配指数:★★★★★<br/><a href="more.jsp">更多内容</a><br/><a href="question.jsp">重新测试</a><br/>重测需等到:<%=DateUtil.formatSqlDatetime(mentalUser.getCreateTime()+2592000000l)%><br/><%
		}
//	}
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%	
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>