<%@ page language="java" import="net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%! static int COUNT_PRE_PAGE = 5;%>
<%YardAction action=new YardAction(request);
String tip = "";
// boolean isHave = false;
boolean isThisFamily = false;
int id = action.getFmId();
int fmId = action.getFmId();
if(fmId > 0){
	if (id == fmId)
		isThisFamily = true;
}else{
	response.sendRedirect("/fm/index.jsp");
	return;
}
YardBean yard = action.getYardByID(id);
YardDeployingBean deploying = null;
YardRecipeProtoBean recipe = null;
PagingBean paging = null;
List list = null;
if (!action.getFmUser().isflagYard()){
	tip = "只有大厨才能进入中级厨房哦.";
} else {
	list = YardAction.yardService.getYardDeployingBeanList(" fmid=" + fmId + " order by id desc");
	paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
}
HashSet hs = YardAction.yardService.getFmDeploying(fmId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="中级厨房"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><a href="kitchen7.jsp?id=<%=fmId%>">>>中级食谱</a><br/>
<%
if (list != null && list.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		deploying = (YardDeployingBean)list.get(i);
		if (deploying != null){
			recipe = YardAction.getRecipeProto(deploying.getRecipeId());
			if (recipe != null)
				%><%=i+1%>.<a href="kitchen5.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>&amp;did=<%=deploying.getDeployId()%>"><%=recipe.getName()%></a>,制作中(用时:<%=action.getCookingTimeStr(System.currentTimeMillis() - deploying.getCreateTime())%>)<br/><%
		}
	}%><%=paging.shuzifenye("kitchen4.jsp?id=" + id,true,"|",response)%><%
} else { 
%>(暂无)<br/><%
}
%><a href="kitchen.jsp">返回厨房</a><br/>
<a href="kitchenrank3.jsp?id=<%=fmId%>">升级中级厨房</a><br/><%
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>