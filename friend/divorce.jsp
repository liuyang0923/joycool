<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="net.joycool.wap.bean.friend.FriendRingBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.NoticeUtil"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
String reply=request.getParameter("reply");
int userId=StringUtil.toInt(request.getParameter("userId"));
UserBean loginUser=(UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
UserBean user=(UserBean)UserInfoUtil.getUser(userId);
FriendAction action=new FriendAction(request);
if("1".equals(reply))
{
 if(request.getParameter("sure")==null){
	 session.setAttribute("divorce","true");
%>
<card title="协议离婚" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
您将要与<%=StringUtil.toWml(user.getNickName())%>协议离婚，<%=user.getGender()==1?"他":"她"%>选择同意之后你们就解除婚姻关系了。你们之间的友好度将变成零，同时要各自负担5万乐币的离婚手续费。<br/>
<a href="/friend/divorce.jsp?reply=1&amp;sure=1&amp;userId=<%=userId%>">确定</a><br/>
<a href="/friend/divorce.jsp?reply=1&amp;userId=<%=userId%>">再考虑考虑</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}
else
{  
	if(session.getAttribute("divorce")!=null){
		session.removeAttribute("divorce");
	    NoticeBean notice = new NoticeBean();
		notice.setUserId(user.getId());
	    notice.setTitle(loginUser.getNickName()+"与你协议离婚了！");
		notice.setContent("");
	    notice.setHideUrl("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setLink("/friend/replyDivorce.jsp?user="+loginUser.getId());
		NoticeUtil.getNoticeService().addNotice(notice);
	}
	out.clearBuffer();
	response.sendRedirect("friendCenter.jsp");
	return;
}
}
else if ("2".equals(reply)){
	int count=action.divorce(loginUser.getId(),user.getId());
    if(count==0){%>
<card title="不符合请求宣告离婚条件" ontimer="<%=response.encodeURL("/friend/divorce.jsp?userId="+userId)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
对不起，您的配偶在10天之内仍然与您有交流，您不能抛弃<%=user.getGender()==1?"他":"她"%>。<br/>
5秒跳转<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
    }
    if(request.getParameter("sure")==null){%>
<card title="请求宣告离婚">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(user.getNickName())%>10天之内没有理您，你们的夫妻关系有名无实，您可以请求乐酷管理员宣告与<%=user.getGender()==1?"他":"她"%>离婚，你们之间的友好度将变成零，并且不用支付手续费<br/>
<a href="/friend/divorce.jsp?reply=2&amp;sure=1&amp;userId=<%=userId%>">确定</a><br/>
<a href="/friend/divorce.jsp?reply=2&amp;userId=<%=userId%>">再考虑考虑</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%    
    }
    else{
	    action.divorce(2,loginUser.getId(),user.getId());
	    out.clearBuffer();
        response.sendRedirect("friendCenter.jsp");
        return;
    }
}
else if ("3".equals(reply)){
	 if(request.getParameter("sure")==null){
%>
<card title="强行离婚">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您选择抛弃<%=StringUtil.toWml(user.getNickName())%>，你们之间的友好度将变成零，同时您要赔偿<%=user.getGender()==1?"他":"她"%>当初你们定情时购买信物的乐币。<br/>
<a href="/friend/divorce.jsp?reply=3&amp;sure=1&amp;userId=<%=userId%>">确定</a><br/>
<a href="/friend/divorce.jsp?reply=3&amp;userId=<%=userId%>">再考虑考虑</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
    }
    else {	
        action.divorce(3,loginUser.getId(),user.getId());
        out.clearBuffer();
        response.sendRedirect("friendCenter.jsp");
        return;
    }
}
else{%>
<card title="离婚">
<p align="left">
<%=BaseAction.getTop(request, response)%>
你的<%=user.getGender() == 1? "老公" : "老婆"%><br/>
<%=StringUtil.toWml(user.getNickName())%><br/>
<a href="/friend/divorce.jsp?reply=1&amp;userId=<%=userId%>">协议离婚（需要对方同意）</a><br/>
<a href="/friend/divorce.jsp?reply=2&amp;userId=<%=userId%>">请求宣告离婚(<%=user.getGender() == 1? "他" : "她"%>老不理你)</a><br/>
<a href="/friend/divorce.jsp?reply=3&amp;userId=<%=userId%>">强行离婚（要赔偿青春损失哦)</a><br/>
<a href="/friend/friendCenter.jsp">一日夫妻百日恩，再考虑考虑</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>