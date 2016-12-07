<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%! static int COUNT_PRE_PAGE = 6;%>
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
Integer count = new Integer(0);
HashMap map = YardAction.yardService.getDeployCount(fmId);
YardBean yard=action.getYardByID(id);
YardRecipeProtoBean recipe = null;
PagingBean paging = null;
List list = null;
if (!action.getFmUser().isflagYard()){
	tip = "只有大厨才能进入中级厨房哦.";
} else {
	list = new ArrayList(yard.getMiddleRecipeSet());
	paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
}
HashSet hs = YardAction.yardService.getFmDeploying(fmId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="中级食谱"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (list != null && list.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		tmp = ((Integer)list.get(i)).intValue();
		recipe = YardAction.getRecipeProto(tmp);
		if (recipe != null){
			// isHave = true;
			count = (Integer)map.get(new Integer(recipe.getId()));
			%><%=i+1%>.<%=recipe.getName()%><%if (count != null && count.intValue() > 0){%>(<%=count - (yard.getDeploying() < 0 ? 0 : yard.getDeploying())%>份)|<%if(hs.contains(new Integer(recipe.getId()))){%>制作中|<%}%><a href="kitchen5.jsp?id=<%=id%>&amp;rid=<%=recipe.getId()%>">烹饪</a><%}%><br/><%
		}
	}%><%=paging.shuzifenye("kitchen7.jsp?id=" + id,true,"|",response)%><%
} else { 
%>(暂无)<br/><%
}
} else {
%><%=tip%><br/><%
}%>
<a href="kitchen4.jsp?id=<%=fmId%>">返回中级厨房</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>