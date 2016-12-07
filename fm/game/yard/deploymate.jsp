<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%!
static int COUNT_PRE_PAGE = 10;
%><%
YardAction action=new YardAction(request);
int fmId = action.getFmId();
String tip = "";
if(fmId < 0){
	response.sendRedirect("/fm/index.jsp");
	return;
}
YardBean yard = action.getYardByID(fmId);
int a=action.getParameterInt("a");
if(a==0){
	a=1;
}
int rid = 0;
PagingBean paging = null;
YardRecipeProtoBean recipe = null;
YardDeployBean deploy = null;
if (!action.getFmUser().isflagYard()){
	tip = "只有大厨才可以配菜.";
} else {
	rid=action.getParameterInt("rid");
	recipe=action.getRecipeProto(rid);
	deploy=(YardDeployBean)action.getAttribute2("rid");
	if(action.hasParam("reset")){
		deploy=new YardDeployBean();
		deploy.setProtoId(rid);
		deploy.setFmid(fmId);
		action.setAttribute2("rid",deploy);
	}
	if(deploy==null&&recipe!=null){
		deploy=new YardDeployBean();
		deploy.setProtoId(rid);
		deploy.setFmid(fmId);
		action.setAttribute2("rid",deploy);
	}
	if(action.hasParam("item")){
		int itemid=action.getParameterInt("item");
		YardItemBean itemBean=yard.getItem(itemid);
		if(itemBean!=null&&itemBean.getNumber()>0){
			int s=recipe.isinside(itemid);
			deploy.addItemToList(itemid,s);
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="配菜间"><p align="left"><%=BaseAction.getTop(request, response)%><%
if ("".equals(tip)){
	if(recipe!=null){
		%><%=recipe.getName()%>:<%=recipe.getStuff()%><br/>
		<%=deploy.getProtoName()%>:<%=action.getItemListString(deploy.getMaterialList())%>|<a href="finishdeploy.jsp?rid=<%=rid%>">完成配菜</a>|<a href="deploymate.jsp?rid=<%=rid%>&amp;reset=1">重置</a><br/><%
	}
	if(a==1){
		%>食材|<%
	}else{
		%><a href="deploymate.jsp?a=1&amp;rid=<%=rid%>">食材</a>|<%
	}
	if(a==2){
		%>配料<br/><%
	}else{
		%><a href="deploymate.jsp?a=2&amp;rid=<%=rid%>">配料</a><br/><%
	}
	List list=yard.getItemList();
	if(list!=null){
		int cap = yard.getMateCount();// 库房升级
		for(int i=0;i<list.size();i++){
			YardItemBean item=(YardItemBean)list.get(i);
			YardItemProtoBean itemProto = YardAction.getItmeProto(item.getItemId());
			if(item.getItemType()==a){
				%><%=itemProto.getName()%>:<%=item.getNumber()%>/<%=itemProto.getCapacity()*cap/100%>|<a href="deploymate.jsp?a=<%=a%>&amp;rid=<%=rid%>&amp;item=<%=item.getItemId()%>">选择</a><br/><%
			}
		}
	}
} else {
	%><%=tip%><br/><%
}
%><a href="deploy.jsp">返回配菜间</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>