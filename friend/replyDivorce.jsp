<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="net.joycool.wap.bean.friend.FriendRingBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.NoticeUtil"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
String reply=request.getParameter("reply");
int fromId=StringUtil.toInt(request.getParameter("user"));
UserBean loginUser=(UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
UserBean fromUser=(UserBean)UserInfoUtil.getUser(fromId);
if("1".equals(reply)){%>
<card title="协议离婚结果" ontimer="<%=response.encodeURL("/friend/marriage.jsp")%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您答应了<%=StringUtil.toWml(fromUser.getNickName())%>的离婚！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
FriendAction action=new FriendAction(request);
action.divorce(1,fromUser.getId(),loginUser.getId());
}
else if ("0".equals(reply)){%>
<card title="协议离婚结果" ontimer="<%=response.encodeURL("/friend/marriage.jsp")%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您拒绝了<%=StringUtil.toWml(fromUser.getNickName())%>的离婚<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%		NoticeBean notice = new NoticeBean();
		notice.setUserId(fromId);
		notice.setTitle("遗憾，"+loginUser.getNickName()+"拒绝了您的离婚！");
		notice.setContent("");
	    notice.setHideUrl("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setLink("/friend/marriage.jsp");
		NoticeUtil.getNoticeService().addNotice(notice);
}
else
{
FriendAction action=new FriendAction(request);
%>
<card title="离婚">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(fromUser.getNickName())%>向您协议离婚了！你选择同意之后你们就解除婚姻关系了。你们之间的友好度将变成零，同时要各自负担5万乐币的离婚手续费。<br/>
<a href="/friend/replyDivorce.jsp?reply=1&amp;user=<%=fromId%>">答应</a><br/>
<a href="/friend/replyDivorce.jsp?reply=0&amp;user=<%=fromId%>">拒绝</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>