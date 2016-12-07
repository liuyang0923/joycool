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
YardBean yard=action.getYardByID(id);
int tmp = 0;
List list = null;
PagingBean paging = null;
YardItemProtoBean item = null;
YardRecipeProtoBean recipe = null;
list = YardAction.yardService.getYardRecipeProtoBeanList("`type`=1 and rank<=" + (yard.getRank()+1)+" order by rank,id");
paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="杂货铺"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><a href="shop.jsp?id=<%=fmId%>">调味品</a>|<a href="shop3.jsp?id=<%=fmId%>">食谱</a>|中级食谱<br/><%
int iterator = 1;
if (list != null && list.size() > 0){
		// 中级食谱
		for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
			recipe = (YardRecipeProtoBean)list.get(i);
			if (recipe != null){
				if (yard.isRecipeSet(recipe.getId()) || yard.isMiddleRecipeSet(recipe.getId())){
					%><%=iterator%>.[<%=recipe.getName()%>]已购买<%				
				} else {
					%><%=iterator%>.<a href="shop4.jsp?id=<%=id%>&amp;b=1&amp;bid=<%=recipe.getId()%>&amp;bk=1">[<%=recipe.getName()%>]</a><%if(recipe.getRank()>yard.getRank()){%>等级不足<%}else{%><%=recipe.getRank()+1%>级<%}%><%
				}
				++iterator;
				%><br/><%
			}
		}%><%=paging.shuzifenye("shop5.jsp?id=" + id + "&amp;t=1",true,"|",response)%><%
}
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>