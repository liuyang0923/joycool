<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action=new FlowerAction(request);
	FlowerUserBean fub = FlowerUtil.getUserBean(action.getLoginUserId());
	HashMap especialMap = FlowerUtil.getEspecialMap();
	int esp = action.getParameterInt("esp");
	int count = action.getParameterInt("c");
	int buy = action.getParameterInt("b");
	boolean result = false;
	String tip = "";
	if (esp <= 0 || esp > especialMap.size()){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
	if (buy == 1){
		if (count <= 0 || count > 999){
			result = false;
			tip = "你输入的个数超出范围.";
		} else {
			result = action.buySpecialGoods(count,esp);
		}
	}
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<% if (buy == 1){
	 	if (result){
	 		%>购买成功.<br/><%
	 	} else {
	 		if ("".equals(tip)){
	 			tip = (String)request.getAttribute("tip");
	 		}
	 		%><%=tip%><br/><%
	 	}
   } else {
   %>购买【<%=FlowerUtil.getEspecial(esp).getName()%>】<br/>
      价格:<%=FlowerUtil.getEspecial(esp).getMoney()%>|成就值:<%=FlowerUtil.getEspecial(esp).getExp()%><br/>
	 您拥有现金:<%=action.getGold()%>|成就值:<%=fub.getExp() - fub.getUsedExp() %><br/>
	 快捷购买:<br/>
	 <a href="besp.jsp?c=1&amp;esp=<%=esp%>&amp;b=1">1个</a>|<a href="besp.jsp?c=2&amp;esp=<%=esp%>&amp;b=1">2个</a>|<a href="besp.jsp?c=3&amp;esp=<%=esp%>&amp;b=1">3个</a>|<a href="besp.jsp?c=4&amp;esp=<%=esp%>&amp;b=1">4个</a>|<a href="besp.jsp?c=5&amp;esp=<%=esp%>&amp;b=1">5个</a><br/>
	 输入购买数量:<br/>
	 <input name="count" value="1" maxlength="3" format="*N"/>
	 <anchor>购买
	 	<go href="besp.jsp?esp=<%=esp%>&amp;b=1" method="post" >
			<postfield name="c" value="$count" />
		</go>
	 </anchor><br/><%
   }%>
<a href="shop2.jsp">返回</a><br/>
<a href="shop.jsp">返回商店</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>