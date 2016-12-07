<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static int[] fieldPrice1 = {0,20000,40000,50000,800000}; //(第一块地)依次为雪山，山崖，平原，河畔的价格
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action=new FlowerAction(request);
	int userMoney = action.getGold(20000);
	//根据钱数来判断是否可以进入商店.没钱不许进.
	if ( userMoney ==  -1){
		action.sendRedirect("mess.jsp?e=5",response);
		return;
	}
	//检查用户是否已经有地了
	if (SqlUtil.getIntResult("select id from flower_field where user_id = " + action.getLoginUserId(),5) > 0){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
	%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
您拥有现金:<%=userMoney%><br/>
【雪山】价格:<%=fieldPrice1[1] %>&nbsp;<a href="bfield.jsp">购买</a><br/>
常年积雪覆盖,脆弱的植物都被这厚厚的积雪所覆盖,只有顽强的植物才能屹立在白雪皑皑的雪山中.植物成长速度缓慢.<br/>
<a href="index.jsp">返回花之境首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>