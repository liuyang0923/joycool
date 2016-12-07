<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static String[] sortNames = FlowerUtil.getSortNames();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int lowPrice = 0;
	int heightPrice = 0;
	int state = action.getParameterInt("s");	//0:全部 1:普通 2:精品 3:珍贵 4:稀有 5:特殊
	if ( state < 0 || state > sortNames.length - 1 ){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
花市<br/>
在花市中可以购买到自己所需的鲜花,也可以卖掉仓库中多余的鲜花.<br/>
【购买鲜花】<br/>
鲜花:<a href="ghouse.jsp?t=3">选择鲜花</a><br/>
价格:<input name="BuyPrice" value="" maxlength="100" format="*N"/><br/>
<anchor>确定
<go href="#" method="post">
	<postfield name="bp" value="$BuyPrice" />
</go>
</anchor><br/>
【出售鲜花】<br/>
鲜花:<a href="ghouse.jsp?t=3">选择鲜花</a><br/>
价格:<input name="SellPrice" value="" maxlength="100" format="*N"/><br/>
<anchor>确定
<go href="#" method="post">
	<postfield name="sp" value="$SellPrice" />
</go>
</anchor><br/>
<% 

%><a href="#">我的交易记录</a><br/>
花市|<a href="ghouse.jsp">花房</a>|<a href="bag.jsp">包裹</a>|<a href="shop.jsp">商店</a>|<a href="store.jsp">仓库</a>|成就<br/>
<a href="lab.jsp">实验室</a>|<a href="result.jsp?t=1">黄金成果</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>