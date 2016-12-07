<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
String tip = "";
int id = action.getFmId();
int fmId = action.getFmId();
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard = action.getYardByID(id);
YardItemProtoBean item = null;
YardItemBean item2 = null;
int itemId = action.getParameterInt("itemid");
if (itemId <= 0){
	tip = "该物品不存在.";
} else {
	item = YardAction.getItmeProto(itemId);
	item2 = yard.getItem(item.getId());
	if (item == null){
		tip = "该物品不存在.";
	} else if (item.getType() != 0){
		tip = "该物品无法培育.";
	}
}
int li = action.getParameterInt("li");
YardLandBean land = yard.getLand(li);
if(land==null){
	response.sendRedirect("land2.jsp");
	return;
}
int count = action.getParameterInt("c");
if (count > 0){
	if (count > land.getMaxCount()){
		tip = "输入的数量过多.";
	} else {
		if (item2 == null || item2.getNumber() == 0){
			tip = "您没有此物品.";
		} else if(count > item2.getNumber()) {
			tip  = "您没有这么多" + item2.getItemNameWml();
		} else if(land.getSeedId()!=0) {
			tip  = "这块地不是空的";
		} else {
			int result = yard.plant(item2.getItemId(),count,action.getLoginUser().getId(),land);
			if (result == -1){
				tip = "参数错误.";
			} else if (result == -2){
				tip = "您没有空地了.";
			} else {
				response.sendRedirect("land2.jsp");
				return;
			}
		}
	}
}
int maxCount = land.getMaxCount();
int autoCount = item2.getNumber()>maxCount?maxCount:item2.getNumber();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="培育中心"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>培育[<%=item.getNameWml()%>](现有<%=item2.getNumber()%>个)<br/>
这块土地容量:<%=maxCount%><br/>
请选择数量:<br/>
<input name="c" format="*N"/><br/>
<anchor>
	确定培育
	<go href="land.jsp?li=<%=li%>&amp;itemid=<%=itemId%>" method="post">
		<postfield name="c" value="$c" />
	</go>
</anchor><%if(item2.getNumber()>0){%>|<a href="land.jsp?li=<%=li%>&amp;itemid=<%=itemId%>&amp;c=<%=autoCount%>">培育<%=autoCount%>个</a><%}%><br/>
<%
} else {
%><%=tip%><br/><%
}%>
<a href="land2.jsp?id=<%=id%>">返回培育中心</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>