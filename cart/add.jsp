<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.impl.UserServiceImpl,net.joycool.wap.bean.CartBean"%><%@ page import="net.joycool.wap.framework.CustomAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%!
	static UserServiceImpl service = new UserServiceImpl();
%><%
response.setHeader("Cache-Control","no-cache");

	CustomAction action = new CustomAction(request);
	if(action.getLoginUser()==null){
		response.sendRedirect("/user/login.jsp");
		return;
	}
	String title = action.getParameterNoEnter("title");
	String url = action.getParameterString("url");
	if(title != null && url != null && title.length()>0){
	    CartBean cart = new CartBean();
	    cart.setUserId(action.getLoginUser().getId());
	    cart.setTitle(title);
	    cart.setUrl(url);
	    service.addCart(cart);
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的收藏夹">
<%=BaseAction.getTop(request, response)%>
<p align="left">
我的收藏夹<br/>
-------------------<br/>
收藏成功！<br/>
进入<a href="/cart/mycart.jsp">我的收藏夹</a><br/>
<br/>
<anchor title="back"><prev/>返回上一页</anchor><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>