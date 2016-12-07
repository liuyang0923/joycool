<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static final int COUNT_PRE_PAGE=5;%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int loginUid = action.getLoginUserId();
	int from = action.getParameterInt("f");			//f=1表示从“种植”连接过来的
	int fieldOrder = action.getParameterInt("o");	//地几块地？
	int totalCount = 0;
	PagingBean paging = null;
	int pageNow = 0;
	String cond = "";
	if (from == 1){			//只显示种子
		totalCount = SqlUtil.getIntResult("select count(flower_id) from flower_store where user_id=" + loginUid + " and count > 0 and type =1", 5);
		paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
		pageNow = paging.getCurrentPageIndex();
		cond = " user_id=" + loginUid + " and count > 0 and type = 1 order by id limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE;
	} else if (from == 2){	//只显示催化剂
		totalCount = SqlUtil.getIntResult("select count(flower_id) from flower_store where user_id=" + loginUid + " and count > 0 and type =3", 5);
		paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
		pageNow = paging.getCurrentPageIndex();
		cond = " user_id=" + loginUid + " and count > 0 and type = 3 order by id limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE;
	} else {				//全部显示
		totalCount = SqlUtil.getIntResult("select count(flower_id) from flower_store where user_id=" + loginUid + " and count > 0 and (type =1 or type = 3)", 5);
		paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
		pageNow = paging.getCurrentPageIndex();
		cond = " user_id=" + loginUid + " and count > 0 and (type = 1 or type = 3) order by id limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE;
	}
	List seedList = service.getSeeds(cond);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
我的包裹<br/>
<%if(seedList.size() == 0){
	%>我的包裹中没有任何物品,快去购买吧.<br/><%
} else {
	%>包裹中存放着各种鲜花的花种和特殊物品.<br/><%
	int fid = 0;
	FlowerStoreBean fsb = null;
	for(int i = 0;i < seedList.size();i++){
		fsb=(FlowerStoreBean)seedList.get(i);
		fid = fsb.getFlowerId();
		//只有用户的土地为空时才可以选择种子
		if (from == 1 ){
			if (fsb.getType() == 1){
				%><a href="pro.jsp?p=<%=fid%>&amp;f=1"><%=flowerTypeList.get(fid)%></a>种子 &nbsp;<%=fsb.getCount() %> 个 &nbsp;<a href="fgarden.jsp?s=<%=fid%>&amp;o=<%=fieldOrder%>">选择</a><br/><%
			}
		} else if ( from == 2){
			if (fsb.getType() == 3){
				%><a href="pro.jsp?p=<%=fid%>&amp;t=1"><%=FlowerUtil.getEspecial(fid).getName()%></a> &nbsp;<%=fsb.getCount() %>个<a href="use.jsp?id=<%=FlowerUtil.getEspecial(fid).getId()%>">选择</a><br/><%
			}
		} else {
			if (fsb.getType() == 1){
				%><a href="pro.jsp?p=<%=fid%>"><%=flowerTypeList.get(fid)%></a>种子 &nbsp;<%=fsb.getCount()%> 个 &nbsp;<br/><%
			} else {
				%><a href="pro.jsp?p=<%=fid%>&amp;t=1"><%=FlowerUtil.getEspecial(fid).getName() %></a> &nbsp;<%=fsb.getCount()%>个<br/><%
			}
		}
	}
}%><%=paging.shuzifenye("bag.jsp?f=" + from + "&amp;o=" + fieldOrder, true, "|", response)%>
花市|<a href="ghouse.jsp">花房</a>|包裹|<a href="shop.jsp">商店</a>|<a href="store.jsp">仓库</a><br/>
<a href="shop.jsp">购买花种</a>|<a href="shop2.jsp">购买催化剂</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>