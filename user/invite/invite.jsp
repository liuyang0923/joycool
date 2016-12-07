<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.MessageBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.InviteAction"%>
<%
response.setHeader("Cache-Control","no-cache");
InviteAction action = new InviteAction(request,response);
UserBean loginUser = action.getLoginUser();
String tip = "";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>您有2种方式邀请您的朋友:<br/>
<!-- 1.您让朋友发送短信<%=loginUser==null?"m":""+loginUser.getId()%>,到号码13718998855,他就会收到登陆的网址和账号密码.智能机点击<a href="sms:13718998855?body=我最近在混乐酷社区,能偷菜聊天玩网游,感觉不错.你发<%=loginUser==null?"m":""+loginUser.getId()%>到13718998855就可以免费注册并得到网址,在好友列表能找到我,强力推荐啊!">这里</a>编辑短信.<br/>  -->
1.您也可以让他直接登录: http://<%=loginUser==null?"m":""+loginUser.getId()%>.joycool.net,免费注册.智能机点击<a href="sms:13718998855?body=我最近在混乐酷社区,能偷菜聊天玩网游,感觉不错.你登陆http://<%=loginUser==null?"m":""+loginUser.getId()%>.joycool.net免费注册之后,在好友列表能找到我,强力推荐啊!">这里</a>开始编短信.<br/>
2.在您的朋友登陆后,在"ME"中可以设置他是您邀请来的.<br/>
<%
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回邀请好友页</a><br/><a href="/chat/lastRank.jsp">返回邀请榜</a><br/><a href="/bottom.jsp">返回ME</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>