<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.wxsj.util.*,java.sql.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%
response.setHeader("Cache-Control","no-cache");
String result = null;
String tip = null;
String backId = request.getParameter("backId");
if(backId == null){
	backId = "6765";
}
String backTo = ("http://wap.joycool.net/Column.do?columnId=" + backId);
//提交
if("post".equalsIgnoreCase(request.getMethod())){
	if(session.getAttribute("caifuOrder") == null){
		response.sendRedirect(backTo);
		return;
	}
	String title = StringUtil.dealParam(request.getParameter("title"));
	String name = StringUtil.dealParam(request.getParameter("name"));
	String mobile = StringUtil.dealParam(request.getParameter("mobile"));
	String address = StringUtil.dealParam(request.getParameter("address"));
	String postcode = StringUtil.dealParam(request.getParameter("postcode"));
	if(StringUtil.isNull(title)){
		tip = "请输入你想要的产品！";
		result = "failure";
	}
	else if(StringUtil.isNull(name)){
		tip = "请输入你的姓名！";
		result = "failure";
	}
	else if(StringUtil.isNull(mobile)){
		tip = "请输入你的手机号！";
		result = "failure";
	}
	else if(!StringUtil.isMobile(mobile)){
		tip = "请输入正确的手机号！";
		result = "failure";
	}
	else if(StringUtil.isNull(address)){
		tip = "请输入你的地址！";
		result = "failure";
	}
	else if(StringUtil.isNull(postcode)){
		tip = "请输入你的邮编！";
		result = "failure";
	}
	else {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		UserBean loginUser = (UserBean) session.getAttribute("loginUser");
		String realMobile = null;
		int userId = 0;
		if(loginUser != null){
			realMobile = loginUser.getMobile();
			userId = loginUser.getId();
		}
		if(realMobile == null){
			realMobile = (String) session.getAttribute("userMobile");
		}
		if(realMobile == null){
			realMobile = "";
		}
		String query = "insert into caifu_order(title, name, mobile, address, postcode, real_mobile, user_id, create_datetime) " + 
			"values('" + title + "', '" + name + "', '" + mobile + "', '" + address + "', '" + postcode + "', '" + realMobile + "', " + userId + ", now())";
		dbOp.executeUpdate(query);
		dbOp.release();

		result = "success";
		session.removeAttribute("caifuOrder");
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if(result == null){
	session.setAttribute("caifuOrder", "true");
%>
<card title="订单">
<p align="left">
<%=BaseAction.getTop(request, response)%>
欢迎您决定订购产品或加盟我们！您的订单如下：<br/>
---------------<br/>
您要的是：<br/>
<input type="text" name="title"  maxlength="20" value=""/><br/>
您的姓名：<br/>
<input type="text" name="name"  maxlength="10" value=""/><br/>
手机：<br/>
<input type="text" name="mobile"  maxlength="20" value=""/><br/>
联系地址：<br/>
<input type="text" name="address"  maxlength="80" value=""/><br/>
邮编：<br/>
<input type="text" name="postcode"  maxlength="10" value=""/><br/>
<anchor>确定
    <go href="http://wap.joycool.net/caifu/order.jsp" method="post">
    <postfield name="title" value="$title"/>
    <postfield name="name" value="$name"/>
	<postfield name="mobile" value="$mobile"/>
	<postfield name="address" value="$address"/>
	<postfield name="postcode" value="$postcode"/>
	<postfield name="backId" value="<%=backId%>"/>
    </go>
</anchor><br/>
<anchor>返回上一页
    <prev/>
</anchor><br/>
<br/>
电话：<a href="wtai://wp/mc;01062056890">010-62056890</a><br/>
短信咨询：<a href="wtai://wp/mc;13718454687">13718454687</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}

//失败
else if("failure".equals(result)){
%>
<card title="订单">
<p align="left">
提交失败<br/>
--------<br/>
<%=tip%><br/>
<anchor>返回
    <prev/>
</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}

//成功
else if("success".equals(result)){
%>
<card title="订单" ontimer="<%=response.encodeURL(backTo)%>">
<timer value="30"/>
<p align="left">
提交成功<br/>
--------<br/>
您的申请已受到！感谢您的信任！我们的客服会尽快跟您联系确认！（3秒钟跳转到首页）<br/>
<a href="<%=backTo%>">直接跳转</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}
%>
</wml>