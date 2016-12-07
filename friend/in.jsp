<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
// UserBase userBase = action.service.getUserBase(" user_id=" + action.getLoginUser().getId());
   UserLive userLive = null;
   int type = action.getParameterInt("t");
   int modify = action.getParameterInt("m");
   int submit = action.getParameterInt("s");
   boolean result = false;
   String tip = "";
//   tip = (String)request.getAttribute("tip");
//   tip = tip != null ? tip : "";
   int tmp = 0;
   if (type < 1){
	   tip = "类别编号错误.";
   } else {
	   if (submit == 1){
		  result = action.validateInput(type,modify,response);
		  if (!result){
			  tip = (String)request.getAttribute("tip");
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
case 1:	
userLive = CreditAction.service.getUserLive(" user_id=" + action.getLoginUser().getId());
if(userLive==null){response.sendRedirect("select2.jsp?t=8");return;}
%>喜欢的影星(可输入30字):<br/><input name="yingxing" value="" maxlength="30" /><br/>
喜欢的歌手(可输入30字):<br/><input name="geshou" value="" maxlength="30" /><br/><br/>
<anchor>
	确认
	<go href="in.jsp?t=<%=type%>&amp;s=1&amp;m=<%=modify %>" method="post">
		<postfield name="yx" value="$yingxing" />
		<postfield name="gs" value="$geshou"/>
	</go>
</anchor><br/><%
	break;
case 2:	
userLive = CreditAction.service.getUserLive(" user_id=" + action.getLoginUser().getId());
if(userLive==null){response.sendRedirect("select2.jsp?t=8");return;}
%>喜欢的食物(可输入30字):<br/><input name="shiwu" value="" maxlength="30" /><br/>
喜欢的运动(可输入30字):<br/><input name="yundong" value="" maxlength="30" /><br/>
最近关注(可输入30字):<br/><input name="guanzhu" value="" maxlength="30" /><br/>
擅长(可输入30字):<br/><input name="shanchang" value="" maxlength="30" /><br/><br/>
	<anchor>
		确认
		<go href="in.jsp?t=<%=type%>&amp;s=1&amp;m=<%=modify %>" method="post">
			<postfield name="sw" value="$shiwu" />
			<postfield name="yd" value="$yundong"/>
			<postfield name="gz" value="$guanzhu"/>
			<postfield name="sc" value="$shanchang"/>
		</go>
	</anchor><br/><%
	break;
case 3:	
%>请填写真实姓名(可输入4位字):<br/><input name="xingming" value="" maxlength="4" /><br/>
交友联系方式(可输入30位数):<br/><input name="lianxi" value="" maxlength="30" /><br/>
请填写身份证号(可输入18位数):<br/><input name="shenfenzheng" value="" maxlength="18" /><br/>
奖品邮寄地址(可输入200字):<br/><input name="dizhi" value="" maxlength="200" /><br/>
请选择内容公开设置:<br/>
<select name="baomi" value="1">
<option value="1">不公开</option>
<option value="2">公开</option>
<option value="3">只对好友公开</option>
</select><br/><br/>
		<anchor>
			确认
			<go href="in.jsp?t=<%=type%>&amp;s=1&amp;m=<%=modify %>" method="post">
				<postfield name="xm" value="$xingming" />
				<postfield name="lx" value="$lianxi"/>
				<postfield name="sfz" value="$shenfenzheng"/>
				<postfield name="dz" value="$dizhi"/>
				<postfield name="bm" value="$baomi"/>
			</go>
		</anchor><br/><%
	break;
}
} else {
%><%=tip%><br/><%
if (type ==1 || type ==2){
%><a href="<%=result?"navi.jsp?t=" + type:"in.jsp?t=" + type%>">返回</a><br/><%
} else if (type == 3){
%><a href="<%=result?"navi.jsp?t=4":"in.jsp?t=3"%>">返回</a><br/><%	
}
}%><%=BaseAction.getBottomShort(request, response)%></p></card></wml>