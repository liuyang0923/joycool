<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="jc.util.SimpleChatLog2"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*,jc.family.*"%><%!
	static int NUMBER_OF_PAGE = 10;
	static String[] openNames = {"只允许家人进入","允许友联家族进入","允许全部家族玩家进入"};
%><%
FamilyAction action=new FamilyAction(request,response);
int fmId = action.getParameterInt("fid");
FamilyUserBean fmUser=action.getFmUser();
if(fmUser==null||fmUser.getFm_id()==0){
synchronized(FamilyAction.newChatUser){
	FamilyAction.removeNewChatUser(action.getLoginUser().getId());
}
action.redirect("/fm/index.jsp");return;
}
if(fmId==0)
	fmId = fmUser.getFm_id();

FamilyHomeBean fm=action.getFmByID(fmId);

if(fmId != fmUser.getFm_id()){	// 进入别人家族的聊天室，要判断权限
	if(fm.getChatOpen()==0 || fm.getChatOpen()==1 && !fm.isAlly(fmUser.getFm_id())){
		response.sendRedirect("/fm/myfamily.jsp?id="+fmId);
		return;
	}
}

net.joycool.wap.bean.UserBean user = action.getLoginUser();
SimpleChatLog2 sc = SimpleChatLog2.getChatLog(fmId,"fm");
net.joycool.wap.util.ForbidUtil.ForbidBean forbid = net.joycool.wap.util.ForbidUtil.getForbid("c" + fmId, fmUser.getId());
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="家族聊天室"><p>
<%PagingBean paging = new PagingBean(action, sc.size(),NUMBER_OF_PAGE,"p");
	String content = action.getParameterNoEnter("content");
	int uid = action.getParameterInt("uid");
	if(content != null && forbid == null) {		// 发言
		if(uid>0) {
			net.joycool.wap.bean.UserBean toUser = UserInfoUtil.getUser(uid);
			if(toUser!=null)
				content = toUser.getNickName() + "," + content;
		}
		sc.add(user.getId(),user.getNickName(),content);
		if(fmId == fmUser.getFm_id()) {	// 本家族有人说话才弹出提示
			synchronized(FamilyAction.newChatUser) {
				List numlist = FamilyAction.service.selectUserIdList(fmId, "");
				Iterator iter = numlist.iterator();
				while(iter.hasNext()) {
					FamilyAction.newChatUser.add(iter.next());
				}
			}
		}
	}
	if(fmId == fmUser.getFm_id())
		FamilyAction.removeNewChatUser(fmUser.getId());
		
	if(forbid==null||forbid.getFlag()==0){	// 踢出聊天室
%>
<%if(paging.getCurrentPageIndex()==0){%><%=fm.getChatTopWml()%><%}%>
<%if(forbid==null){%>
<input name="gchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="chat.jsp?fid=<%=fmId%>" method="post">
    <postfield name="content" value="$gchat"/>
</go></anchor>|
<%}else{%>x你已被禁止发言x|<%}%>
<a href="chat.jsp?fid=<%=fmId%>">刷新</a><br/>
<%if(!fmUser.isSettingChatLink()){%><%=sc.getChatString2(fmUser.getId(),paging.getStartIndex(), NUMBER_OF_PAGE,"chat2.jsp?fid=" + fmId)%><%}else{%><%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%><%}%>
<%=paging.shuzifenye("chat.jsp?fid="+fmId,true, "|", response)%>
<%}else{%>x你已被踢出聊天室x<br/><%}%>
[状态]<%=openNames[fm.getChatOpen()]%><br/>
<%if(fm.getForumId()!=0){%><a href="/jcforum/forum.jsp?forumId=<%=fm.getForumId()%>">&gt;&gt;家族论坛</a><br/><%}%>
<%if(fmId == fmUser.getFm_id()&&fmUser.isflagChat()){%><a href="ban.jsp">管理聊天室</a><br/><%}%>
<a href="../set.jsp">个人设置</a><br/>
&lt;<a href="../myfamily.jsp?id=<%=fmId%>">返回家族</a>&lt;<a href="../index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>