<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,jc.credit.*,java.util.*,jc.search.*,net.joycool.wap.framework.BaseAction"%><%
	response.setHeader("Cache-Control","no-cache");
	SearchAction action = new SearchAction(request);
	UserBean ub = action.getLoginUser();
	if(ub == null){
		response.sendRedirect("/user/login.jsp");
		return;
	}
	String[] returnJsps = {"","searchCenter.jsp","emotionSearch.jsp","homeSearch.jsp","astroSearch.jsp","superSearch.jsp"};
	int province = action.getParameterInt("pro");//展示哪个省份的城市
	int type = action.getParameterInt("type");	//从哪个页面过来的
	int gender = action.getParameterInt("gender");
	int age = action.getParameterInt("age");
	int astro = action.getParameterInt("astro");
	int aim = action.getParameterInt("aim");
	List list = SearchAction.service.getCityList(" hypo = " + province);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="城市"><p><%=BaseAction.getTop(request, response)%><%
if(province > 0 && province<=34){
	if (list!=null && list.size() > 0){
	    CreditProvince pro = (CreditProvince)SearchAction.service.getProvince(" id="+province);
		if(type > 0 && type <= 5){%><%=pro.getProvince()%><br/>==选择城市==<br/><%
			for (int i = 0 ; i < list.size() ; i++){
		    	CreditCity city = (CreditCity)list.get(i);
				%><anchor><%=city.getCity()%>
					<go href="<%=returnJsps[type]%>" method="post"><%
					if(gender > 0){%><postfield name="gender" value="<%=gender%>"/><%}
					if(age > 0){%><postfield name="age" value="<%=age%>"/><%}
					if(astro > 0){%><postfield name="astro" value="<%=astro%>"/><%}
					if(aim > 0){%><postfield name="aim" value="<%=aim%>"/><%}
					%><postfield name="city" value="<%=city.getId()%>"/>
					</go>
				</anchor><%
			}
		%><br/>&gt;&gt;<anchor>返回上级<go href="province.jsp" method="post">
<postfield name="type" value="<%=type%>"/>
<postfield name="gender" value="<%=gender%>"/>
<postfield name="age" value="<%=age%>"/>
<postfield name="astro" value="<%=astro%>"/>
<postfield name="aim" value="<%=aim%>"/>
</go></anchor><br/>&gt;&gt;<a href="searchCenter.jsp">返回搜索首页</a><br/>&gt;&gt;<a href="/friend/friendCenter.jsp">返回交友中心</a><%
		}else{
	%>您的点击过于频繁,为了您的健康,请<a href="searchCenter.jsp">返回</a>后再进行尝试<br/><%
		}
	}else{
	%>您的点击过于频繁,为了您的健康,请<a href="searchCenter.jsp">返回</a>后再进行尝试<br/><%
	}
}else{
	if(type > 0 && type <= 5){
	%>您的点击过于频繁,为了您的健康,请<a href="<%=returnJsps[type]%>">返回</a>后再进行尝试<br/><%
	}else{
	%>您的点击过于频繁,为了您的健康,请<a href="searchCenter.jsp">返回</a>后再进行尝试<br/><%
	}
}
%><br/><%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>