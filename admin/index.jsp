<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*" %><%!
static String[] adminType={"chat","forum","mail","home","tong","team","info","fan","sell"};
static String[] adminName={"聊天","论坛总","信件","家园","帮会功能","圈子","个人资料","防沉迷","交易"};
%><%response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
net.joycool.wap.bean.UserBean loginUser = action.getLoginUser();
if(loginUser==null){
	response.sendRedirect("/user/login.jsp");
	return;
}
int userId = (loginUser==null?0:loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷监察员">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%for(int i=0;i<adminType.length;i++){%>
<%if(ForbidUtil.isForbid(adminType[i]+"a",loginUser.getId())){%>*<%}%><%=i+1%>.<a href="<%=adminType[i]%>f.jsp"><%=adminName[i]%>监察</a><br/>
<%}%><br/>
<a href="list.jsp">乐酷监察名单</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>