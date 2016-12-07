<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	FlowerUserBean fub = FlowerUtil.getUserBean(action.getLoginUserId());
	
	if ( fub == null ){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
商店<br/>
欢迎光临!本商店所售商品包括各种花种和催化剂,请您随意挑选.<br/>
分类:花种|<a href="shop2.jsp">催化剂</a><br/>
您有现金:<%=action.getGold()%>|成就值:<%=fub.getExp() - fub.getUsedExp() %><br/>
<% for (int i = 1;i < FlowerUtil.getFlowerMap().size() + 1;i++){
	//只显示基本的，没有被隐藏的花。
	if (FlowerUtil.getFlower(i).getType()==1 && FlowerUtil.getFlower(i).getState() == 1){
		%><a href="pro.jsp?p=<%=i%>&amp;b=1"><%=FlowerUtil.getFlower(i).getName()%></a>种子价格:<%=FlowerUtil.getFlower(i).getPrice()%>&nbsp;<a href="bflower.jsp?t=<%=i%>">购买</a><br/><%
	}
}%>
花市|<a href="ghouse.jsp">花房</a>|<a href="bag.jsp">包裹</a>|商店|<a href="store.jsp">仓库</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<a href="../island.jsp">返回采集岛</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>