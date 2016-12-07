<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%! static int COUNT_PRE_PAGE = 10;%>
<%YardAction action=new YardAction(request);
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
int tmp = 0;
List list = null;
PagingBean paging = null;
YardItemProtoBean item = null;
YardRecipeProtoBean recipe = null;
YardBean yard=action.getYardByID(id);
// 调料list
list = YardAction.getItemProtoList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="杂货铺"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>调味品|<a href="shop3.jsp?id=<%=fmId%>">食谱</a>|<a href="shop5.jsp?id=<%=fmId%>">中级食谱</a><br/><%
if (list != null && list.size() > 0){
	// 调味料
	for (int i = 0 ; i < list.size() ; i++){
		item = (YardItemProtoBean)list.get(i);
		if (item != null && item.getType() == 2){
			%><%=item.getName()%>(<%=item.getBuyCount()%>)|<%=YardAction.moneyFormat(item.getPrice())%>元<a href="shop2.jsp?id=<%=id%>&amp;bid=<%=item.getId()%>">购买</a><br/><%
		}
	}
}
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>