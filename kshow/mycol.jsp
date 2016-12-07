<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,java.util.*,net.joycool.wap.bean.*,net.joycool.wap.framework.BaseAction,jc.show.*"%>
<%@page import="jc.show.Collection"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
List list = CoolShowAction.service.getCollection(" user_id="+ub.getId());
List advlist = CoolShowAction.service.getAdv(" place=1");
PagingBean paging = new PagingBean(action,list.size(),5,"p");
if(advlist!=null && advlist.size() > 3)
	advlist = action.getRandList(advlist,3);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的收藏">
<p><%=BaseAction.getTop(request, response)%>==我的收藏==<br/><%
if(list != null && list.size() > 0){
	%>您一共收藏了<%=list.size()%>件商品<br/><%
	for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
		Collection col = (Collection)list.get(i);
		Commodity com = CoolShowAction.getCommodity(col.getIid());
		if(com != null){
		%><%=i+1%>.<a href="consult.jsp?from=1&amp;Iid=<%=com.getIid()%>"><%=StringUtil.toWml(com.getName())%></a>|<%=com.getPrice()==0?"免费":com.getPrice()+"酷币"%>|<a href="delcol.jsp?del=<%=com.getId()%>">删除</a><br/><%
		}
	}
  	%><%=paging.shuzifenye("mycol.jsp?jcfr=1",true,"|",response)%><%
}else{
	%>暂无收藏<br/><%
}
if(advlist!=null&&advlist.size()>0){
	%>==热点推荐==<br/><%
	for(int i=0;i<advlist.size();i++){
		BeanAdv adv = (BeanAdv)advlist.get(i);
		Commodity comm = CoolShowAction.getCommodity(adv.getCommid());
		if(comm != null){
		%><a href="consult.jsp?from=1&amp;Iid=<%=comm.getIid()%>"><%=StringUtil.toWml(comm.getName())%></a><%=comm.getPrice()==0?"免费":comm.getPrice()+"酷币"%><br/><%
		}
	}
}else{
	%>==暂无推荐==<br/><%
}
%><a href="index.jsp">&gt;我的酷秀</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>