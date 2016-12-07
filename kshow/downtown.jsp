<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.UserBean,net.joycool.wap.spec.shop.*,net.joycool.wap.framework.BaseAction,java.util.List,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
if("2".equals(request.getParameter("gend"))) {	// 女装
	request.getRequestDispatcher("/Column.do?columnId=11070").forward(request, response);
	return;
} else if(true){
	request.getRequestDispatcher("/Column.do?columnId=11101").forward(request, response);
	return;
}
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
int uid = ub.getId();
UserInfoBean bean = ShopAction.shopService.getUserInfo(uid);
if(bean == null)
	bean = ShopAction.shopService.addUserInfo(uid);
String[] gender = {"","女","男"};
int part = action.getParameterIntS("p");
int gend = action.getParameterInt("gend");
int st = action.getParameterInt("st");
CatalogBean catalogBean = CoolShowAction.getCatalog(part);
if(catalogBean==null||catalogBean.isHide())
	part = -1;
String cond = "del=0 and type<>1 and type<>2";	
if(gend == 1){
	cond += " and gender<>1";
}else{
	cond += " and gender<>0";
}
if (part!=-1){
	cond+= " and catalog="+part;
}
if(st == 1) {
	cond += " order by price desc";
} else if(st == 2) {
	cond += " order by price asc";
} else if(st == 3) {
	cond += " order by count desc";
} else if(st == 4) {
	cond += " order by count asc";
} else if(st == 5) {
	cond += " order by id desc";
}else{
	cond += " order by id desc";
}
List advlist = CoolShowAction.service.getAdv(" place=0");
if(advlist!=null && advlist.size() > 3)
	advlist = action.getRandList(advlist,3);
List list = CoolShowAction.service.getCommodityList(cond);
PagingBean paging = new PagingBean(action,list.size(),5,"pg");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="酷秀商城<%=gender[gend]%>装区">
<p><%=BaseAction.getTop(request, response)%><%
if(part < 0){%><img src="img/logo.gif" alt="logo"/><br/><%}
if(part >= 0){
%><a href="downtown.jsp?gend=<%=gend%>&amp;st=<%=st%>">全部</a><%
}else{
%>全部<%
}
  	List catalogList = CoolShowAction.getCatalogList();
	for(int i=0;i<catalogList.size();i++){
		CatalogBean catalog = (CatalogBean)catalogList.get(i);
		if(catalog.isHide()) continue;
		if(catalog !=null){
			if(catalog.getId()!=part){
		%>|<a href="downtown.jsp?gend=<%=gend%>&amp;p=<%=catalog.getId()%>&amp;st=<%=st%>"><%=catalog.getName()%></a><%
			}else{
		%>|<%=catalog.getName()%><%
			}
		}
	}%><br/><%
if(list != null && list.size() > 0){
	for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
	Commodity com = (Commodity)list.get(i);
	%><%=i+1%>.<a href="consult.jsp?from=1&amp;Iid=<%=com.getIid()%>"><%=StringUtil.toWml(com.getName())%></a>(<%=com.getPrice()==0?"免费":com.getPrice()+"酷币"%>)<br/><%
	%><img src="/rep/show/comm/<%=com.getBigImg()%>" alt="o"/><br/><%
	%><a href="room.jsp?from=2&amp;Iid=<%=com.getIid()%>">试穿</a>|<a href="sureBuy.jsp?Iid=<%=com.getIid()%>">购买</a><br/><%
	}
  	%><%=paging.shuzifenye("downtown.jsp?gend="+gend+"&amp;p="+part+"&amp;st="+st,true,"|",response)%><%
}else{
%>暂无对应酷秀!<br/><%
}%>
<select name="st" value="<%=st%>">
<option value="2">价格从低到高</option>
<option value="1">价格从高到低</option>
<option value="3">人气从高到低</option>
<option value="4">人气从低到高</option>
<option value="5">按上架时间</option>
</select><a href="l.jsp?gend=<%=gend%>&amp;p=<%=part%>&amp;st=$st">排序</a><br/>==推荐商品==<br/><%
if(advlist!=null&&advlist.size()>0){
	for(int i=0;i<advlist.size();i++){
		BeanAdv adv = (BeanAdv)advlist.get(i);
		Commodity com = CoolShowAction.getCommodity(adv.getCommid());
		if(com != null){
		%><a href="consult.jsp?from=1&amp;Iid=<%=com.getIid()%>"><%=StringUtil.toWml(com.getName())%></a>|<%=com.getPrice()==0?"免费":com.getPrice()+"酷币"%><br/><%
		}
	}
}
%>——————<br/><a href="index.jsp">&gt;我的酷秀</a><br/><%
if(gend == 1){
%><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><%
}else{
%><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><%
}
%><br/><%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>