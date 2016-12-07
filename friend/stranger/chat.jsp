<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,jc.friend.stranger.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control", "no-cache");
String[] basic = {"正在速配陌生人……","你已和陌生人建立聊天关系，快去打招呼吧！","TA已经离开，<a href=\"chat.jsp?chg=true\">速配陌生人</a>","输入不能超过50个字哦!","输入不能为空哦!","你的发言太快了,休息一下再试吧.","还没找到其他人，不能发言。","酒逢知己,千杯少~~","话不投机可以换个人聊聊哦","陌生的你是不是我今生相伴.","你知道我在等你出现吗?","神秘的缘分,你我的相遇.","每天我都会在陌生人等你.","美女总是以神秘方式出现的","因为陌生,所以坦然.","相遇是缘,你我有缘.","陌生的你我也会产生爱的花","我是不是你梦中的女孩?","在陌生人等待我的英雄出现","爱要我们相遇……","等美女与你交友吧!","帅哥就在你身边."};
ChatAction action = new ChatAction(request,response);
UserBean ub = action.getLoginUser();
int uid = 0;
int strgId = 0;
int pit = 0;
int sign = action.GetSign();
String cont = action.getParameterString("cont");
List list = null;
if(cont != null){
	pit = 6;
}
if(ub != null){
	uid = ub.getId();
	if(action.getParameterString("chg")!=null){
		ChtBean ctb1 = action.service.getChater("user_id="+ uid);
		if(ctb1 != null){
			action.updateChat(ctb1.getRoomId());
			action.deleteCht(uid);
		}
		response.sendRedirect("chat.jsp");//防止匹配后刷新,又重新匹配
		return;
	}
	ChtBean ctb = action.service.getChater("user_id="+ uid);
	if(ctb != null){//是否已经匹配到用户
		if(action.service.getChater("user_id="+ ctb.getStrgid()) != null){//陌生人是否还在
			pit = action.instChats(ctb.getUid(), ctb.getStrgid(), ctb.getRoomId());
			list = action.service.getChatList(" room_id='"+ StringUtil.toSql(ctb.getRoomId())+ "' and isshow=0 order by create_time desc");
			if(pit == 7){
				response.sendRedirect("chat.jsp?pit=7");//防止刷新数据重新提交
				return;
			}
		}else {
			list = action.service.getChatList(" room_id='"+ StringUtil.toSql(ctb.getRoomId())+ "' and isshow<=1 order by create_time desc");
			pit = 2;
			if(cont != null)
				pit = 6;
		}
	}else{
		strgId = action.getStranger(uid);//进入等候
		if(strgId != 0){//匹配陌生人
			action.instChater(strgId);
			pit = 1;
		}
	}
	if(request.getParameter("pit") != null){
		pit = RandomUtil.nextInt(7,21);
	}
}	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wmsl_1.1.xml">
<wml><%
if(uid == 0){%><card title="陌生人聊天"><p><%=BaseAction.getTop(request, response)%><br/>您还没有登录,请<a href="../user/login.jsp">登录</a>后再试!<br/><%=BaseAction.getBottomShort(request, response)%></p></card><%
}else{%><card title="陌生人聊天" ontimer="<%=response.encodeURL("chat.jsp")%>"><timer value="1800"/><p>
<%=BaseAction.getTop(request, response)%><%=basic[pit]%><br/>
<input type="text" name="kkk<%=sign%>" maxlength="50"/><br/>
<anchor>发言<go href="chat.jsp" method="post"><postfield name="cont" value="$kkk<%=sign%>"/><postfield name="sign" value="<%=sign%>"/></go></anchor>|<a href="chat.jsp">刷新</a><br/><%
if(list != null && list.size() > 0){
	PagingBean paging = new PagingBean(action, list.size(), 10, "p");
	for(int i = paging.getStartIndex(); i < paging.getEndIndex(); i++){
		ChatBean cb = (ChatBean)list.get(i);
		if(cb.getFrom() == uid){%>我<%}else{%>Ta<%}%>:<%=StringUtil.toWml(cb.getCont())%><br/><%
	}
	%><%=paging.shuzifenye("chat.jsp", false, "|", response)%><%
}
%><a href="chat.jsp?chg=true">换其他人聊聊</a><br/><a href="index.jsp?exit=true">退出陌生人聊天</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card><%
}%>
</wml>