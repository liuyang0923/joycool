<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();
	static final int COUNT_PRE_PAGE=5; 
	static int[] fieldPrice1 = {0,20000,40000,50000,800000}; //(第一块地)依次为雪山，山崖，平原，河畔的价格
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action=new FlowerAction(request);
//	int fieldType = action.getParameterInt("b"); 
	//判断非法输入
//	if ( fieldType < 1 || fieldType > 4 ){
//		action.sendRedirect("mess.jsp?e=3",response);
//		return;			
//	}
	FlowerUserBean fub = service.getFieldType(action.getLoginUser().getId());
	//不可重复购买
	if (fub != null){
		action.sendRedirect("mess.jsp?e=2",response);
		return;
	}
	boolean result = action.buyField(fieldPrice1[1],0);
//	String r = (String)request.getAttribute("r");
//	int t = action.getAttributeInt("t");
	List al = FlowerUtil.getFieldTypeList();
%><wml>
<card title="花之境" ontimer="<%=response.encodeURL("back.jsp?b=1")%>"><timer value="30"/>
<p><%=BaseAction.getTop(request, response)%>
<%if ( result ){
	%>你成功的购买了养殖地-<%=al.get(1)%>,赶快开始游戏吧!（5秒后自动<a href="fgarden.jsp">返回</a>）<br/><%
}%><%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>