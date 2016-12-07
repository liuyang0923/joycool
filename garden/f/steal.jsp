<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int fieldId = action.getParameterInt("id");
	int friendUserUid = action.getParameterInt("fuid");
	FieldBean fb = service.getFieldById(fieldId);
	boolean result;
	String tip = "";
	if (fb != null && fb.getField() != 0){
		int growTime = action.abloomTime(fb.getCreateTime() / 1000,fb.getField(),fb.getType());
		if ( growTime <= 0 ){
			tip = "你来晚了,现在已经不能这么做了.";
			result = false;
		} else {
			result = action.stealLeaf(fieldId,friendUserUid,10,10,1);	//(土地的ID,好友的ID,最大偷取次数,增加的时间（分）,获得的成就值)
			tip = (String)request.getAttribute("tip");
		}
	} else {
		tip = "土地上没有任何鲜花.";
		result = false;
	}
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<% if (result){
		%>踩踏成功,你获得1点成就值.<br/><%
   } else {
   		%><%=tip%><br/><%
   }%><a href="friend.jsp">返回好友列表</a><br/>
<a href="fgarden2.jsp?fuid=<%=friendUserUid %>">返回好友的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>