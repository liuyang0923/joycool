<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! static FlowerService service=new FlowerService();%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int t = action.getParameterInt("t");
	//检查非法参数
	if ( t < 1 || t > 4 ){
		action.sendRedirect("mess.jsp?e=3",response);
		return;
	}
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	String topLink = "";
	if ( t == 1){
		topLink = "精品|<a href=\"result.jsp?t=2\">珍贵</a>|<a href=\"result.jsp?t=3\">稀有</a>|<a href=\"result.jsp?t=4\">特殊</a><br/>";
	} else if ( t == 2){
		topLink = "<a href=\"result.jsp?t=1\">精品</a>|珍贵|<a href=\"result.jsp?t=3\">稀有</a>|<a href=\"result.jsp?t=4\">特殊</a><br/>";
	} else if ( t == 3){
		topLink = "<a href=\"result.jsp?t=1\">精品</a>|<a href=\"result.jsp?t=2\">珍贵</a>|稀有|<a href=\"result.jsp?t=4\">特殊</a><br/>";
	} else if ( t == 4){
		topLink = "<a href=\"result.jsp?t=1\">精品</a>|<a href=\"result.jsp?t=2\">珍贵</a>|<a href=\"result.jsp?t=3\">稀有</a>|特殊<br/>";
	}
	List list = action.flowerCompose(t,action.getLoginUserId());
	List noComposeList = (ArrayList)request.getAttribute("noComposeList");
	int currentIndex = 0;
%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
黄金成果<br/>
<% if (list != null){
	%>通过不断的实验,你已经拥有以下的实验成果.<br/><%=topLink%><%
		for(int i=0;i<list.size();i++){
			currentIndex = i+1;
			%><%=currentIndex%>.[<%=flowerTypeList.get(Integer.parseInt(String.valueOf(list.get(i))))%>]:<%=action.getCompoundExp(FlowerUtil.getFlower(Integer.parseInt(String.valueOf(list.get(i)))).getCompose())%><br/><%
		}
		for(int i=0;i<noComposeList.size();i++){
			currentIndex++;
			%><%=currentIndex%>.[<%=flowerTypeList.get(Integer.parseInt(String.valueOf(noComposeList.get(i))))%>]:未知<br/><%
		}
	%><a href="lab.jsp">返回实验室</a><br/><%
	} else {
		%>你还没有黄金成果,还需继续努力.<br/><%
	}%>
<a href="fgarden.jsp">返回我的花园</a><br/>
<a href="index.jsp">返回花之境首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>