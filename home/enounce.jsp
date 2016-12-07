<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="net.joycool.wap.spec.garden.*,java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.action.user.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendBean,net.joycool.wap.action.home.*"%><%@ page import="net.joycool.wap.spec.friend.*"%><%!
static IHomeService homeService =ServiceFactory.createHomeService();%><%
	response.setHeader("Cache-Control","no-cache");
	HomeAction action = new HomeAction(request);
	
	UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	String tip = "";
	
	if(user == null) {
		response.sendRedirect("/");
		return;
	}
	int backId = action.getParameterInt("b");
	int view = action.getParameterInt("v");
	
	int userId = action.getParameterInt("userId");
	
	if (backId != 2){
		// backId == 2，说明是来查看别人的宣言，不用检查此项。
		if (user.getGender() == 1){
			// 男的？转回男性家园
			response.sendRedirect("home.jsp");
			return;
		}
	}

	int uid = user.getId();//LoginUser.getId();
	

	if (userId != 0){
		uid = userId;
	}
	
	HomePlayer player = homeService.getPlayer(" user_id = " + uid);
	int create = action.getParameterInt("c");
	if (create == 1){
		HomeEnounce enounce = new HomeEnounce();
		enounce.setUserId(uid);
		enounce.setContent(action.getParameterNoEnter("content"));
		enounce.setDel(0);
		enounce.setFlag(0);
		enounce.setPic(action.getParameterInt("pic"));
		boolean result = action.addNewEnounce(enounce);
		if (!result){
			tip = (String)request.getAttribute("tip");
		}
	}
	List enounceList = homeService.getEnounceList(" user_id =" + uid + " order by create_time desc limit 5");
	HomeEnounce enounce = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="家园">
<p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%>==本日宣言==<br/>
<%if (enounceList.size()>0){
	enounce = (HomeEnounce)enounceList.get(0);
	%><img src="img/<%=enounce.getPic()%>.gif" alt="<%=action.getPicList().get(enounce.getPic())%>" /><%=StringUtil.toWml(enounce.getContent())%><br/><%
}%>
<%if (backId != 2 && userId == 0){
%>宣言(限20字):<input name="content" value="" maxlength="20" />
<select name="pic" value="<%=RandomUtil.nextInt(30) + 1%>"><%
		   for(int i=1;i<=30;i++){
				%><option value="<%=i%>"><%=action.getPicList().get(i)%></option><%
		   }
%></select><br/>
<anchor>
	>>添加
	<go href="enounce.jsp?c=1" method="post">
		<postfield name="content" value="$content" />
		<postfield name="pic" value="$pic"/>
	</go>
</anchor><br/>
<%}%>
<%if (backId == 1){
	%><a href="couple.jsp">返回上一页</a><%
}else if (backId == 2){
	%><a href="couple2.jsp?userId=<%=uid%>">返回上一页</a><%
} else {
	%><a href="fehome.jsp">返回上一页</a><%
}%><br/>
--------------------<br/>
最近宣言:<br/>
<% if (enounceList.size() != 0){
	for (int i = 0; i < enounceList.size();i++){
		enounce = (HomeEnounce)enounceList.get(i);
		%><%=i + 1%>.<img src="img/<%=enounce.getPic()%>.gif" alt="<%=action.getPicList().get(enounce.getPic())%>" /><%=StringUtil.toWml(enounce.getContent())%><br/><%
	}
} else {
	%>您还没写宣言呢~快快编写吧.<br/><%
}	
} else {
	%><%=tip%><br/><a href="enounce.jsp">返回</a><br/><%
}%>
<a href="fehome.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>