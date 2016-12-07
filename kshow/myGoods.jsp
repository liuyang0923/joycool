<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean,net.joycool.wap.bean.PagingBean,net.joycool.wap.framework.BaseAction,jc.show.*,java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
int pt = action.getParameterInt("pt");
CatalogBean curCat = CoolShowAction.getCatalog(pt);
if(curCat!=null&&curCat.isHide()) curCat = null;
if(curCat==null) pt=-1;
String cond = "del=0 and user_id="+ub.getId();
if (curCat!=null){
	cond +=" and type="+pt;
}
List list = action.getMyGoods(cond);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的物品">
<p><%=BaseAction.getTop(request, response)%><%
%><%
if(pt!=-1){
%><a href="myGoods.jsp">我的物品</a>|<%
}else{
%>我的物品|<%
}/*
List catalogList = CoolShowAction.getCatalogList();
for(int i=0;i<catalogList.size();i++){
	CatalogBean catalog = (CatalogBean)catalogList.get(i);
	if(catalog.isHide()) continue;
		if(catalog.getId() == pt){
	%><%=catalog.getName()%>|<%
		}else{
	%><a href="myGoods.jsp?pt=<%=catalog.getId()%>"><%=catalog.getName()%></a>|<%
		}
	
}*/
%><a href="dustbin.jsp">废衣篓</a><br/><%
if(list != null && list.size() > 0){
	PagingBean paging = new PagingBean(action, list.size(), CoolShowAction.COUNT_PRE_PAGE, "p");
	for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
	Pocket po = (Pocket)list.get(i);
	Commodity comm = CoolShowAction.getCommodity(po.getIid());
	%><%=i+1%>.<a href="consult.jsp?Iid=<%=po.getIid()%>"><%=comm.getName()%></a><%if(comm.getGender()==0){%>(女)<%}else if(comm.getGender()==1){%>(男)<%}%><%
	%>|<a href="room.jsp?Iid=<%=po.getIid()%>">试穿</a><br/><%
	}
	 %><%=paging.shuzifenye("myGoods.jsp?pt="+pt, true, "|", response)%><%
}else{
%>无<br/><%
}%>--------<br/><a href="index.jsp">&gt;我的酷秀</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>