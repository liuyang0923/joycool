<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action=new FlowerAction(request);
	FlowerUserBean fub = FlowerUtil.getUserBean(action.getLoginUserId());
	HashMap especialMap = FlowerUtil.getEspecialMap();
	EspecialBean espBean = null;
	if ( fub == null ){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
商店<br/>
欢迎光临!本商店所售商品包括各种花种和催化剂,请您随意挑选.<br/>
分类:<a href="shop.jsp">花种</a>|催化剂<br/>
您拥有现金:<%=action.getGold()%>|成就值:<%=fub.getExp() - fub.getUsedExp() %><br/>
<% if (especialMap.size() != 0){
		for (int i = 1;i <= especialMap.size();i++){
			espBean = (EspecialBean)especialMap.get(new Integer(i));
			%><a href="pro.jsp?p=<%=espBean.getId()%>&amp;b=2"><%=espBean.getName()%></a>|<a href="besp.jsp?esp=<%=espBean.getId()%>">购买</a><br/>价格:<%=espBean.getMoney()%>;扣除成就值:<%=espBean.getExp()%><br/><%
		}
}%>
花市|<a href="ghouse.jsp">花房</a>|<a href="bag.jsp">包裹</a>|商店|<a href="store.jsp">仓库</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<a href="../island.jsp">返回采集岛</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>