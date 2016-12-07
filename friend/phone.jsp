<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,java.util.regex.Matcher,java.util.regex.Pattern,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   int submit = action.getParameterInt("s");
   int cancel = action.getParameterInt("c");
   String tip = "";
   String error = "";
   UserInfo userInfo = action.getUserInfo(action.getLoginUser().getId());
   if (submit == 1){
		// 提交
	    String shouji = action.getParameterNoCtrl("sj");
		if (shouji == null){shouji = "";}
		Pattern pattern = Pattern.compile("(1\\d{10})");
		Matcher isPhone = pattern.matcher(shouji);
		if (!isPhone.matches()){
			tip ="手机号格式错误.";
		} else {
			// 与注册时的匹配
			if (action.getLoginUser().getMobile().equals(shouji)){
				SqlUtil.executeUpdate("update credit_user_info set mobile='" + StringUtil.toSql(shouji) + "',phone_point=40 where user_id=" + action.getLoginUser().getId(),5);
				userInfo.setMobile(shouji);
				userInfo.setPhonePoint(40);
				// ****** 这里别忘了加分数 ******
				//SqlUtil.executeUpdate("update credit_user_info set phone_point=40 where phone_point = 0 and user_id=" + action.getLoginUser().getId(),5);
			} else {
				error = "您好,手机号码认证失败,请重新填写.";
			}
		}
   } else if (cancel == 1){
	   // 取消
	   SqlUtil.executeUpdate("update credit_user_info set mobile='',phone_point=0 where user_id=" + action.getLoginUser().getId(),5);
	   userInfo.setMobile("");
	   userInfo.setPhonePoint(0);
	   // ****** 这里别忘了减分数 ******
	   //SqlUtil.executeUpdate("update credit_user_info set phone_point=0 where phone_point != 0 and user_id=" + action.getLoginUser().getId(),5);
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>您目前的交友可信度为<%=userInfo.getTotalPoint()%>%<br/><%
if ("".equals(userInfo.getMobile()) || userInfo.getMobile() == null){
%>手机号码认证(40):0<br/><%if(!"".equals(error)){%><%=error%><br/><%} %>
请输入您的注册ID手机号码:<br/><input name="shouji" value="" maxlength="30" />
<anchor>
	确认
	<go href="phone.jsp?s=1" method="post">
		<postfield name="sj" value="$shouji" />
	</go>
</anchor><br/>
<%	
} else {
%>手机号码认证(40):40<br/>
<%=StringUtil.toWml(userInfo.getMobile())%><a href="phone.jsp?c=1">取消</a><br/>
<%	
}
%><a href="credit.jsp">[返回可信度首页]</a><br/><%
} else {
	%><%=tip%><br/><a href="phone.jsp">返回</a><br/><%
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>