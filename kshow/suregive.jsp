<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*,net.joycool.wap.bean.*,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
String gender[] = {"限女性","限男性","通用"};
int Iid = action.getParameterInt("Iid");
int touid = action.getParameterInt("touid");
UserBean toub = UserInfoUtil.getUser(touid);
Commodity com=Iid>0?CoolShowAction.getCommodity(Iid):null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="酷秀赠送">
<p><%=BaseAction.getTop(request, response)%><%

if(com == null||com.getDel()!=0){
	%>没有该酷秀信息!<br/><%
}else{

if(toub != null){
	int due = action.getParameterInt("due");
	if(due != 1 && due != 3 && due != 6 && due != 12)
		due = 1;
		session.setAttribute("ses","give");
	%>确定赠送给用户:<br/>ID:<%=toub.getId()%><br/>昵称:<%=toub.getNickNameWml()%><br/>酷秀:【<a href="consult.jsp?from=1&amp;Iid=<%=Iid%>"><%=StringUtil.toWml(com.getName())%></a>】<br/>类型:<%=com.getPartName()%><br/><%
		if(com.getGender()>=0&&com.getGender()<3){
		%>性别:<%=gender[com.getGender()]%><br/><%
		}else{
		%>无<br/><%
		}
	%>价格:<%if(com.getPrice()>0){ %><%if(due==1){%><%=com.getPrice()%>酷币/月<%}else if(due==3){%><%=com.getPrice3()%>酷币/3个月<%}else if(due==6){%><%=com.getPrice6()%>酷币/6个月<%}else if(due==12){%><%=com.getPrice12()%>酷币/年<%}%><%}else{ %>免费<%} %>
	<br/><a href="give.jsp?Iid=<%=Iid%>&amp;touid=<%=touid%>&amp;due=<%=due %>">确认赠送</a><br/><%

}else{
	if(touid > 0){
	%>查不到该用户,请再次确认对方ID:<%
	}else{
	%>请输入对方ID:<%
	}
%><input type="text" format="*N" name="to"/><br/>
酷秀:【<a href="consult.jsp?from=1&amp;Iid=<%=Iid%>"><%=StringUtil.toWml(com.getName())%></a>】<br/>
价格:<%if(com.getPrice()>0){ %><select name="due"><option value="1"><%=com.getPrice()%>酷币/月</option><option value="3"><%=com.getPrice3()%>酷币/3个月</option><option value="6"><%=com.getPrice6()%>酷币/6个月</option><option value="12"><%=com.getPrice12()%>酷币/年</option></select><%}else{ %>免费<%} %><br/>
<anchor>赠送<go href="suregive.jsp" method="post"><postfield name="due" value="$due"/><postfield name="touid" value="$to"/><postfield name="Iid" value="<%=Iid%>"/></go></anchor>|<a href="consult.jsp?from=1&amp;Iid=<%=Iid%>">返回</a><br/><%
}
}
%><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>