<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%! static int COUNT_PRE_PAGE = 10;%>
<%YardAction action=new YardAction(request);
String tip = "";
String tip2 = "";
int result = 0;
int id = action.getParameterInt("id");
FamilyUserBean fu = action.getFmUser();
int fmId = 0;
if(fu!=null)
	fmId=fu.getFm_id();
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
boolean my = (fmId == id && fu.isflagYard());
int tmp = 0;
YardItemBean item = null;
YardRecipeProtoBean recipe = null;
YardBean yard=action.getYardByID(id);
List list = yard.getItemListByType(3);
int sellId = action.getParameterInt("sid");
if (sellId > 0){
	if(!my){
		response.sendRedirect("index.jsp");
		return;
	}
	item = yard.getItem(sellId);
	if (item == null || item.getNumber() == 0){	// 您要卖的物品不存在,或数量=0
		sellId  = 0;
	} else {
		int count = action.getParameterInt("c");
		if (count > 0){
			result = yard.sell(item.getItemId(),count);
			if (result == -1){
				tip2 = "抱歉!你没有那么多菜肴.<br/>";
			} else {
				tip = "卖出" + count +"份"+ item.getItemNameWml() + "!获得资金" + YardAction.moneyFormat(result) + "元!<br/><a href=\"hall.jsp?id=" + fmId + "\">返回餐厅大堂</a>";
			}
		}
	}
}
PagingBean paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="餐厅大堂"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%
if (sellId <= 0){
%>冰箱容量:<%=yard.getTotalDishCount()/10%>/<%=yard.getKitchen2Count()%><br/>卖出菜肴:<br/><%
// 列表
if (list != null && list.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		item = (YardItemBean)list.get(i);
		if (item != null){
			%><%=item.getItemNameWml()%>共<%=(float)item.getNumber()/10%>份<br/>单价:<%=YardAction.moneyFormat(item.getItemPrice())%>元/份<%if(my){%>|<a href="hall.jsp?sid=<%=item.getItemId()%>">卖出</a><%}%><br/>==========<br/><%
		}
	}%><%=paging.shuzifenye("hall.jsp?id=" + id,true,"|",response)%><%
} else {
	%>暂时没有菜肴.<br/><%
}
if(fmId == id){%><a href="kitchenrank2.jsp">升级冰箱</a><br/><%}
} else {
	int count2 = item.getNumber()/10;
// 卖
%><%=tip2%><%=item.getItemNameWml()%>(现有<%=(float)item.getNumber()/10%>份)<br/>输入您要卖出的数量(单价:<%=YardAction.moneyFormat(item.getItemPrice())%>元/份):<br/>
<input name="c" format="*N" maxlength="3"/><br/>
<anchor>确定卖出
	<go href="hall.jsp?id=<%=id%>&amp;sid=<%=sellId%>" method="post">
		<postfield name="c" value="$c" />
	</go>
</anchor><%if(count2>0){%>|<a href="hall.jsp?id=<%=id%>&amp;sid=<%=sellId%>&amp;c=<%=count2%>">卖出<%=count2%>份</a><%}%><br/>
<a href="hall.jsp">返回大堂</a><br/>
<%
}
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp?id=<%=id%>">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>