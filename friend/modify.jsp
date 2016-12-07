<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,java.util.regex.Matcher,java.util.regex.Pattern,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
// UserBase userBase = action.service.getUserBase(" user_id=" + action.getLoginUser().getId());
   Property property = null;
   int type = action.getParameterInt("t");
   int submit = action.getParameterInt("s");
   UserBase userBase = null;
   UserWork userWork = null;
   UserLive userLive = null;
   UserLooks userLooks = null;
   UserContacts userContacts = null;
   List list = null;
   String tip = "";
   int tmp = 0;
   if (type < 1){
	   tip = "类别编号错误.";
   } else {
	   if (submit == 1){
		    // 修改
			int backId = action.modifyIn(type);
		    if (backId == -1){
		    	tip = (String)request.getAttribute("tip");
		    } else {
		    	response.sendRedirect("navi.jsp?t=" + backId);	
		    	return;
		    }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
switch (type){
case 1:			// 喜欢的影星
// 用户数据不存在，返回
if(!SqlUtil.exist("select user_id from credit_user_live where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=2");
	return;
}%>喜欢的影星(可输入30字):<br/><input name="yingxing" value="" maxlength="30" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="yx" value="$yingxing" />
	</go>
</anchor><br/><%
	break;
case 2:			// 喜欢的歌手
if(!SqlUtil.exist("select user_id from credit_user_live where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=2");
	return;
}%>喜欢的歌手(可输入30字):<br/><input name="geshou" value="" maxlength="30" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="gs" value="$geshou" />
	</go>
</anchor><br/><%
	break;
case 3:			// 喜欢的食物
if(!SqlUtil.exist("select user_id from credit_user_live where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=2");
	return;
}%>喜欢的食物(可输入30字):<br/><input name="shiwu" value="" maxlength="30" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="sw" value="$shiwu" />
	</go>
</anchor><br/><%
	break;
case 4:			// 喜欢的运动
if(!SqlUtil.exist("select user_id from credit_user_live where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=2");
	return;
}%>喜欢的运动(可输入30字):<br/><input name="yundong" value="" maxlength="30" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="yd" value="$yundong" />
	</go>
</anchor><br/><%
	break;
case 5:			// 我的关注
if(!SqlUtil.exist("select user_id from credit_user_live where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=2");
	return;
}%>最近关注(可输入30字):<br/><input name="guanzhu" value="" maxlength="30" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="gz" value="$guanzhu" />
	</go>
</anchor><br/><%
	break;
case 6:			// 我擅长
if(!SqlUtil.exist("select user_id from credit_user_live where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=2");
	return;
}%>擅长(可输入30字):<br/><input name="shanchang" value="" maxlength="30" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="sc" value="$shanchang" />
	</go>
</anchor><br/><%
	break;
case 7:			// 真实姓名
if(!SqlUtil.exist("select user_id from credit_user_contacts where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=4");
	return;
}%>请填写真实姓名(可输入4位字):<br/><input name="xingming" value="" maxlength="30" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="xm" value="$xingming" />
	</go>
</anchor><br/><%
	break;
case 8:			// 交友联系方式
if(!SqlUtil.exist("select user_id from credit_user_contacts where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=4");
	return;
}%>交友联系方式(可输入30位数):<br/><input name="lianxi" value="" maxlength="30" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="lx" value="$lianxi" />
	</go>
</anchor><br/><%
	break;
case 9:			// 身份证号
if(!SqlUtil.exist("select user_id from credit_user_contacts where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=4");
	return;
}%>请填写身份证号(可输入18位数):<br/><input name="shenfenzheng" value="" maxlength="30" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="sfz" value="$shenfenzheng" />
	</go>
</anchor><br/><%
	break;
case 10:		// 奖品邮寄地址
if(!SqlUtil.exist("select user_id from credit_user_contacts where user_id=" + action.getLoginUser().getId(),5)){
	response.sendRedirect("navi.jsp?t=4");
	return;
}%>奖品邮寄地址(可输入200字):<br/><input name="dizhi" value="" maxlength="200" /><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="dz" value="$dizhi" />
	</go>
</anchor><br/><%
	break;
case 11:		// 内容公开设置
%>请选择内容公开设置:<br/>
<select name="baomi" value="1">
<option value="1">不公开</option>
<option value="2">公开</option>
<option value="3">只对好友公开</option>
</select><br/>
<anchor>
	确认
	<go href="modify.jsp?t=<%=type%>&amp;s=1" method="post">
		<postfield name="bm" value="$baomi" />
	</go>
</anchor><br/><%
	break;
}
} else {
%><%=tip%><br/>
<%if (type <= 6){
%><a href="navi.jsp?t=2">返回</a><%
} else{
%><a href="navi.jsp?t=4">返回</a><%
}
}
%><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>