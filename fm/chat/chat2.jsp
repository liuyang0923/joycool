<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="jc.util.SimpleChatLog2"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*,jc.family.*"%><%!
static int NUMBER_OF_PAGE = 10;%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmUser=action.getFmUser();
int uid = action.getParameterInt("uid");
int fmId = action.getParameterInt("fid");

if(fmUser==null||fmUser.getFm_id()==0||uid<=0){
	action.redirect("chat.jsp?fid=" + fmId);
	return;
}
FamilyUserBean toFmUser = FamilyAction.getFmUserByID(uid);
net.joycool.wap.bean.UserBean toUser = UserInfoUtil.getUser(uid);
if(toFmUser==null || toUser == null){
	action.redirect("chat.jsp?fid=" + fmId);
	return;
}
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="家族聊天室"><p>
<%if(toFmUser.getFmId()==fmUser.getFmId()){%><%if(!"".equals(toFmUser.getFm_name())){%><%=StringUtil.toWml(toFmUser.getFm_name())%><%}else{%>家人<%}%><%}else{%>外人<%}%>:<%=toUser.getNickNameWml()%><br/>
<input name="gchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="chat.jsp?uid=<%=uid%>&amp;fid=<%=fmId%>" method="post">
    <postfield name="content" value="$gchat"/>
</go></anchor><br/>
<a href="../fmuserinfo.jsp?userid=<%=uid%>">&gt;&gt;查看功勋</a><%if(fmId == fmUser.getFm_id()&&fmUser.isflagChat()){%>|<a href="bana.jsp?uid=<%=uid%>">x封禁x</a><%}%><br/>
<a href="chat.jsp?fid=<%=fmId%>">返回聊天室</a><br/>
&lt;<a href="../myfamily.jsp?id=<%=fmId%>">返回家族</a>&lt;<a href="../index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>