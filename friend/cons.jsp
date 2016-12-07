<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.shop.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<%! static ShopService shopService = ShopService.getInstance() ; %>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   UserInfo userInfo = action.getUserInfo(action.getLoginUser().getId());
   UserInfoBean userInfoBean = shopService.getUserInfo(userInfo.getUserId());
   if (userInfoBean != null){
	   if (userInfoBean.getConsumeCount() > 50){
	   // 得5分
	   	  userInfo.setConPoint(5);
	      SqlUtil.executeUpdate("update credit_user_info set con_point=5 where user_id=" + userInfo.getUserId(),5);
	   } else if (userInfoBean.getConsumeCount() > 10){
		 // 得2分
		   userInfo.setConPoint(2);
		   SqlUtil.executeUpdate("update credit_user_info set con_point=2 where user_id=" + userInfo.getUserId(),5);
	   } else {
		// 0分
		   userInfo.setConPoint(0);
		   SqlUtil.executeUpdate("update credit_user_info set con_point=0 where user_id=" + userInfo.getUserId(),5);
	   }
   }
   String tip = "";
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>您目前的交友可信度为<%=userInfo.getTotalPoint()%>%<br/>消费累积(5):<%=userInfo.getConPoint()%><br/>
<a href="../shop/index.jsp">乐秀商城初级会员可信度增2.</a><br/>
<a href="../shop/index.jsp">乐秀商城高级会员可信度增3.</a><br/>
<a href="credit.jsp">[返回可信度首页]</a><br/>
<%
} else {
	%><%=tip%><br/><a href="phone.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>