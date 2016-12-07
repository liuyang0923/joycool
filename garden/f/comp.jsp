<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	String compString = "";
	boolean result = false;
	int flowerId = 0;
	int c = action.getParameterInt("c");
	if ( c == 1 ){
		DishBean dish = FlowerUtil.getUserDish(action.getLoginUserId());
		List compList = action.dishToList(dish);
		if (compList.size()==0){
			action.sendRedirect("mess.jsp?e=10",response);
			return;
		}
		flowerId = action.doCompound2();
	} else {
		result = action.doCompound();
		action.sendRedirect("lab.jsp",response);
		return;
	}
	if ( flowerId == -1 ){
		action.sendRedirect("mess.jsp?e=7",response);
		return;
	}
	List compList = (ArrayList)request.getAttribute("compList");
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	if (compList != null ){
		//合成公式
		for (int i = 0;i < compList.size();i++){
			compString += flowerTypeList.get(StringUtil.toInt(compList.get(i).toString())) + "+";
		}
		compString = compString.substring(0,compString.length()-1);		//将字符串中最后一个“+”号去掉	
	}
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<%	if (flowerId != 0){
		%>合成试验成功!<br/>你用&nbsp;<%=compString%>&nbsp;合成出了[<%=flowerTypeList.get(flowerId)%>],合成出的花种将自动放到你的<%
		if (flowerId < 21){
			%><a href="bag.jsp">包裹</a><%
		} else {
			%><a href="store.jsp">仓库</a><%
		}%>中，获得<%=FlowerUtil.getFlower(flowerId).getSuccessExp()%>点成就值.这个合成公式也将自动记录到你的<a href="result.jsp?t=1">黄金成果</a>中.<br/>
		<a href="lab.jsp">返回实验室</a><br/><a href="fgarden.jsp">返回我的花园</a><br/><%
	}else{
		%>合成试验失败!<br/>对不起!合成实验失败,获得<%=FlowerUtil.COMP_FAIL_EXP%>点成就值,你培养皿中的花已全部消失!不要气馁，继续去尝试吧,成功离你不远了.<br/><a href="lab.jsp">返回实验室</a><br/><%
	}%><%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>