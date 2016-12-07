<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static final int COUNT_PRE_PAGE=5; %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	int t = action.getParameterInt("t");
	int count = action.getParameterInt("c");
	boolean result = false;
	if (t <= 0 || t > flowerTypeList.size() || FlowerUtil.getFlower(t).getType()!=1 ){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}						
	if ( count > 0 && count <= 999){
		result = action.buyFlowerSeed(t,count);
	}
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<%  if ( count != 0 ){
		if ( result ){
			%>成功购买了<%=count%>个<%=flowerTypeList.get(t)%>种子,花种已放进你的包裹里面了.<br/><%
		} else {
			%>对不起，你的资金不够!<br/><%
		}
	} else {
%>购买【<%=flowerTypeList.get(t)%>种子】<br/>
价格:<%=FlowerUtil.getFlower(t).getPrice()%><br/>
快捷购买:<br/>
<a href="bflower.jsp?t=<%=t%>&amp;c=1">1个</a>|<a href="bflower.jsp?t=<%=t%>&amp;c=2">2个</a>|<a href="bflower.jsp?t=<%=t%>&amp;c=3">3个</a>|<a href="bflower.jsp?t=<%=t%>&amp;c=4">4个</a>|<a href="bflower.jsp?t=<%=t%>&amp;c=5">5个</a><br/>
输入购买数量:<br/>
<input name="count" value="1" maxlength="3" format="*N"/>
<anchor>购买
	<go href="bflower.jsp?t=<%=t%>" method="post" >
		<postfield name="c" value="$count" />
	</go>
</anchor><br/><%	
	}%>
<a href="shop.jsp">返回商店</a><br/>
<a href="fgarden.jsp">返回我的花园</a><br/>
花市|<a href="ghouse.jsp">花房</a>|<a href="bag.jsp">包裹</a>|商店|<a href="store.jsp">仓库</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>