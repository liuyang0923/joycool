<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   int uid = action.getParameterInt("uid");
   int score = action.getParameterInt("s");
   UserInfo userInfo = null;
   UserInfo myInfo = null;
   String tip = "";
   int type = 0;
   if (uid < 0 || UserInfoUtil.getUser(uid) == null){
    	tip = "用户ID:" + uid + "不存在.";
    } else if (uid == action.getLoginUser().getId()){
    	type = 3;
    } else {
    	userInfo = action.getUserInfo(uid);
    	myInfo = action.getUserInfo(action.getLoginUser().getId());
    	if (myInfo.getTotalPoint() < 80){			// 别忘了把设定改回80！
    		type = 1;
    	} else {
    		if (score >= 1 && score <= 5){
    			// 打分
    			int result = action.doPlayerGrade(uid,score);
    			if (result == -1){
    				type = 2;
    			} else {
    				// 打分成功
					response.sendRedirect("credit.jsp?uid=" + uid);
    				return;
    			}
    		}
    	}
    }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
switch(type){
case 0:		// 正常打分
String genderText = UserInfoUtil.getUser(uid).getGenderText();
%>根据您对<%=genderText%>的了解给<%=genderText%>打分吧:<br/>
<a href="grade.jsp?uid=<%=uid%>&amp;s=1">1</a>.<a href="grade.jsp?uid=<%=uid%>&amp;s=2">2</a>.<a href="grade.jsp?uid=<%=uid%>&amp;s=3">3</a>.<a href="grade.jsp?uid=<%=uid%>&amp;s=4">4</a>.<a href="grade.jsp?uid=<%=uid%>&amp;s=5">5</a><br/>
<%
break;
case 1:		// 低于80分
%>您好,您的可靠分数不足80%,还不能为别人打分.赶快<a href="credit.jsp">增加可信度</a>吧.<br/><%
break;
case 2:		// 打了N次分
%>您好,每个高于80%的用户只可给别人打分一次,您已经给他打过分了.<br/><%
break;
case 3:		// 给自己打分
%>只有可信度高于80%的好友才可给您打分.<br/><%
break;
}
%><a href="credit.jsp?uid=<%=uid%>">[返回上一页]</a><br/><%
} else {
%><%=tip%><br/><a href="credit.jsp?uid=<%=uid%>">返回</a><br/><%	
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>