<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%
response.setHeader("Cache-Control","no-cache");
String[] illegalStrs=new String[]{"http","法轮","江泽民","胡锦涛","温家宝","我靠","我操","fuck","bitch","他妈的","鸡巴","falun","共产党","操你妈","你妈逼","月赚","操你大爷","你妈的","www.","wap",".com",".cn",".net"};
JCRoomChatAction action=new JCRoomChatAction(request);
action.getLastInviteMessage(request);
String name="";
String message="有空吗？来和我一起聊聊天吧！";
if(request.getAttribute("name")!=null){
	name=(String)request.getAttribute("name");
	message=(String)request.getAttribute("message");
}
boolean validate=true;
String info=null;
String redirectUrl=("inviteSend.jsp");
//手机号码为空或者用户通过opera登录(通过opera登录的用户手机号为一个字符串"fttodeath")
if(action.getLoginUser().getMobile().trim().equals("fttodeath")){
	redirectUrl=("inviteSend.jsp?flag=5");
	%><%=JCRoomChatAction.getTransferPage(redirectUrl)%><%return ;
}
else if(action.getLoginUser().getMobile().trim().equals("")){
	redirectUrl=("inviteSend.jsp?flag=3");
	%><%=JCRoomChatAction.getTransferPage(redirectUrl)%><%return ;
}
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
int rank=us.getRank();
if(rank<3){
	redirectUrl=("inviteSend.jsp?flag=4");
response.sendRedirect(redirectUrl);return ;}
if(request.getParameter("mobile")!=null){
    	validate=action.invite(request,illegalStrs);//验证输入合法性
    	info=(String)request.getAttribute("info");
		if(info!=null)
		{
			if(info.equals("北京"))
			{
				validate=true;
				info=null;
				redirectUrl=("inviteSend.jsp?flag=3");
			}else if(info.equals("未到发送时间")){
				validate=true;
				info=null;
				redirectUrl=("inviteSend.jsp?flag=2");
			}
		}
	%><%if(info==null&&validate==true){%><%=JCRoomChatAction.getTransferPage(redirectUrl)%><%return ;}%>
<%}%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请好友来乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(null!=info){ %><%=info %><br/>
<%}else{%><!-- 
介绍好友来乐酷,将有机会获得独一无二的乐酷王冠哦:)<br/>-->
<%}%> 
【邀请的好处】<br/>
拥有一顶乐酷王冠后就会在昵称增加炫酷的王冠图片（所有人都可见）从而成为其他玩家羡慕妒嫉的焦点，成为争先恐后结交的对象。<br/>
【如何邀请好友】<br/>
1.记住属于你个人的域名/地址:<%=loginUser.getId()%>.joycool.net<br/>
2.把这个域名/地址通过短信发给你的朋友，并提示让他按照这个地址访问"你的网站"<br/>
3.当他访问了你的专属域名，并注册成功后，邀请成功，你会得到积分奖励！<br/>
提示：可以通过通讯录对好友进行群发提高效率。发给他们的短信中最好有说服他们访问的理由，如"来看看我的最新照片"之类的话<br/>

奖励说明:我们将对邀请上线好友（须为乐酷新用户）最多的用户进行排名，每天前7位用户将每人获得一顶独一无二的乐酷王冠!<br/>
<a href="inviteRank.jsp">查看今天的邀请排名</a><br/>
<a href="lastRank.jsp">返回邀请榜</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>