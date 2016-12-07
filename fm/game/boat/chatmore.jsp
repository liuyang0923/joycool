<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="jc.util.*,net.joycool.wap.bean.*,jc.family.game.boat.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.*"%><%@ page import="jc.family.*"%><%!
static int NUMBER_OF_PAGE = 10;%><%
BoatAction action=new BoatAction(request,response);
UserBean userBean = action.getLoginUser();
FamilyUserBean fmUser = action.getFmUser();
if(fmUser == null){
	response.sendRedirect("game.jsp");
	return;
}
Integer imid = (Integer) session.getAttribute("mid");
if(imid == null){
	response.sendRedirect("game.jsp");
	return;
}
MatchBean matchBean = (MatchBean) BoatAction.matchCache.get(imid);
if(matchBean == null) {
	response.sendRedirect("game.jsp");
	return;
}
int mid = imid.intValue();
int fid = fmUser.getFm_id();
int uid = action.getLoginUser().getId();
String ct = request.getParameter("ct");
if("a".equals(ct)||"f".equals(ct)){session.setAttribute("ct",ct);}
if(session.getAttribute("ct")!=null){ct=(String)session.getAttribute("ct");}
SimpleChatLog2 scl =null;
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="聊天记录"><p><%
if("a".equals(ct)){
scl = SimpleChatLog2.getChatLog(mid,"match");
%><a href="chatmore.jsp?ct=f&amp;mid=<%=mid%>">家族</a>|所有<br/><%
}else{
StringBuilder fmChatId = new StringBuilder();
fmChatId.append(mid);
fmChatId.append(fid);
scl = SimpleChatLog2.getChatLog(Integer.parseInt(fmChatId.toString()),"match_fm");
%>家族|<a href="chatmore.jsp?ct=a&amp;mid=<%=mid%>">所有</a><br/><%
}
PagingBean paging = new PagingBean(action, scl.size(),NUMBER_OF_PAGE,"p");
String content = action.getParameterNoEnter("content");
	if(content != null&&!"".equals(content)) {		// 发言
		if(content.length() > 100){
			content = content.substring(0,100);
		}
		StringBuilder fmChatId = new StringBuilder();
		fmChatId.append(mid);
		fmChatId.append(fid);
		scl.add(uid,userBean.getNickNameWml(),StringUtil.toWml(content));
	}
%><%=scl.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("chatmore.jsp?mid="+mid+"&amp;ct="+ct,true, "|", response)%>
<a href="say.jsp?f=2">发言</a><br/>
<a href="game.jsp?mid=<%=mid %>">返回龙舟比赛</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>