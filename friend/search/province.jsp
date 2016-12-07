<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,jc.credit.*,java.util.*,jc.search.*,net.joycool.wap.framework.BaseAction"%><%
	response.setHeader("Cache-Control","no-cache");
	SearchAction action = new SearchAction(request);
	UserBean ub = action.getLoginUser();
	if(ub == null){
		response.sendRedirect("/user/login.jsp");
		return;
	}
	String[] returnJsps = {"","searchCenter.jsp","emotionSearch.jsp","homeSearch.jsp","astroSearch.jsp","superSearch.jsp"};
	int type = action.getParameterInt("type");//从哪个页面过来的
	int gender = action.getParameterInt("gender");
	int age = action.getParameterInt("age");
	int astro = action.getParameterInt("astro");
	int aim = action.getParameterInt("aim");
	List list = SearchAction.service.getProvinceList(" 1");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="省份"><p><%=BaseAction.getTop(request, response)%><%
if (list!=null && list.size() > 0){
	if(type > 0 && type <= 5){%>==选择省份==<br/><%
		for (int i = 0 ; i < list.size() ; i++){
	    CreditProvince province = (CreditProvince)list.get(i);
			%><anchor><%=province.getProvince()%>
				<go href="city.jsp" method="post"><%
					if(gender > 0){%><postfield name="gender" value="<%=gender%>"/><%}
					if(age > 0){%><postfield name="age" value="<%=age%>"/><%}
					if(astro > 0){%><postfield name="astro" value="<%=astro%>"/><%}
					if(aim > 0){%><postfield name="aim" value="<%=aim%>"/><%}
					%><postfield name="type" value="<%=type%>"/>
					<postfield name="pro" value="<%=province.getId()%>"/>
				</go>
			</anchor><%
		}
		%><br/>&gt;&gt;<a href="<%=returnJsps[type]%>">返回上级</a><br/>&gt;&gt;<a href="searchCenter.jsp">返回搜索首页</a><br/>&gt;&gt;<a href="/friend/friendCenter.jsp">返回交友中心</a><%
	}else{
	%>您的点击过于频繁,为了您的健康,请<a href="searchCenter.jsp">返回</a>后再进行尝试<br/><%
	}	
}
%><br/><%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>