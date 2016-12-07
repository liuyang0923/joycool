<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
String tip = "";
int result = 0;
int id = action.getFmId();
FamilyUserBean fmUser = action.getFmUser();
int fmId = fmUser.getFmId();
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard = action.getYardByID(id);
YardLandBean land = null;
YardItemProtoBean item = null;
int li = action.getParameterIntS("li");
if (li >= 0){
	result = yard.reap(li,request);
	if (result == 0){
		tip = request.getAttribute("tip").toString();
	} else if (result == -1){
		tip = "参数错误.";
	} else if (result == -2){
		tip = "不是本家族的土地.";
	} else if (result == -3){
		tip = "仍未成熟.";
	} else if (result == -4){
		tip  = "这块地已经空了.";
	}
}
List list = yard.getLandList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="培育中心"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (list != null && list.size() > 0){
	long now = System.currentTimeMillis();
	for (int i = 0 ; i < list.size() ; i++){
		land = (YardLandBean)list.get(i);
		if (land != null){
			item = land.getItemOnLand(); // 取得这块地上正在种着的物品
			if (item != null){
				int timeLeft = land.getTimeLeft(now);
				%><%=i+1%>.<a href="landview.jsp?li=<%=i%>"><%=item.getNameWml()%></a>/<%=land.getCount()%>/<%if(timeLeft<=0){%>已成熟<%}else{%><%=DateUtil.formatTimeInterval(timeLeft)%><%}%><br/><%
			} else {
				%><%=i+1%>.<a href="breed.jsp?li=<%=i%>">空地</a><br/><%
			}
		}
	}
}
if (yard.getLandRank() < YardAction.LAND_MAX_RANK){
	%>+<a href="land3.jsp">购买土地</a>+<br/><%
}
%>+<a href="land4.jsp">升级土地</a>+<br/><%
} else {
%><%=tip%><br/><a href="land2.jsp?id=<%=id%>">返回培育中心</a><br/><%
}
%>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>