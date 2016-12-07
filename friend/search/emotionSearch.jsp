<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,java.util.*,net.joycool.wap.bean.*,jc.credit.*,jc.search.*,net.joycool.wap.framework.BaseAction"%><%
	response.setHeader("Cache-Control","no-cache");
	SearchAction action = new SearchAction(request);
	UserBean ub = action.getLoginUser();
	if(ub == null){
		response.sendRedirect("/user/login.jsp");
		return;
	}
	List cityList = SearchAction.service.getCityList(" 1");
	UserBase base = null;
	CreditCity creditCity = SearchAction.service.getCity(" id="+296);
	if(ub != null){
		base = SearchAction.service.getUserBase(" user_id=" + ub.getId());
		if(base != null){
			creditCity.setId(base.getCity());
			CreditCity creditCity1 = SearchAction.service.getCity(" id="+base.getCity());
			if(creditCity1!= null)
				creditCity.setCity(creditCity1.getCity());	
		}
	}
	int city = action.getParameterInt("city");
	int gender = action.getParameterInt("gender");
	int age = action.getParameterInt("age");
	int aim = action.getParameterInt("aim");
	if(city > 0)
		creditCity = SearchAction.service.getCity(" id="+city);
	else
		city = 296;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="交友搜索"><p><%=BaseAction.getTop(request, response)%><%
%><a href="/friend/friendCenter.jsp">交友中心</a>&gt;<a href="searchCenter.jsp">搜索中心</a>&gt;交友搜索<br/>
==交友搜索==<br/>城市:<%=creditCity.getCity()%>[<anchor>修改<go href="province.jsp" method="post"><postfield name="type" value="2"/><postfield name="gender" value="$gender"/><postfield name="age" value="$age"/><postfield name="aim" value="$aim"/></go></anchor>]<br/>
性别:<select name="gender" value="<%=gender%>">
<option value="0">不限</option>
<option value="1">女</option>
<option value="2">男</option>
</select><br/>  
年龄:<select name="age" value="<%=age%>">
<option value="0">不限</option>
<option value="1">10-20</option>
<option value="2">20-30</option>
<option value="3">30-50</option>
<option value="4">50以上</option>
</select><br/>  
交友目的:<select name="aim" value="<%=aim%>">
<option value="1">恋人</option>
<option value="2">知已</option>
<option value="3">玩伴</option>
<option value="4">其它</option>
</select><br/>
<anchor>搜索<go href="searchResult.jsp" method="post"><postfield name="type" value="2"/><postfield name="gender" value="$gender"/><postfield name="city" value="<%=creditCity.getId()%>"/><postfield name="age" value="$age"/><postfield name="aim" value="$aim"/></go></anchor>
<br/>&gt;&gt;更多<a href="homeSearch.jsp">城市</a>.<a href="astroSearch.jsp">星座</a>.<a href="superSearch.jsp">高级</a>搜索&lt;&lt;<br/>
==热门搜索==<br/><%
int hotSize = SearchAction.hot.size();
if(hotSize > 0){
	if(hotSize > 2){
		int temp1 = RandomUtil.nextInt(hotSize);
		int temp2 = RandomUtil.nextInt(hotSize);
		while(temp1 == temp2){
			temp2 = RandomUtil.nextInt(hotSize);
		}
		BeanSearch bean = (BeanSearch)SearchAction.hot.get(temp1);
		String show = "";
		int cc = bean.getCity();
		if(cc > 0){
			CreditCity ccc = (CreditCity)cityList.get(cc-1);
			show = show+"/"+ccc.getCity();
		}
		show = action.getShow(bean,show);
		%><%=show%><anchor>搜索<go href="searchResult.jsp" method="post"><postfield name="type" value="1"/><%
		if(bean.getCity()>0){
		%><postfield name="city" value="<%=bean.getCity()%>"/><%
		}
		if(bean.getAstro()>0){
		%><postfield name="astro" value="<%=bean.getAstro()%>"/><%
		}
		if(bean.getGender()>0){
		%><postfield name="gender" value="<%=bean.getGender()%>"/><%
		}
		if(bean.getAim()>0){
		%><postfield name="aim" value="<%=bean.getAim()%>"/><%
		}
	 	%></go></anchor><br/><%
		bean = (BeanSearch)SearchAction.hot.get(temp2);
		show = "";
		cc = bean.getCity();
		if(cc > 0){
			CreditCity ccc = (CreditCity)cityList.get(cc-1);
			show = show+"/"+ccc.getCity();
		}
		show = action.getShow(bean,show);
		%><%=show%><anchor>搜索<go href="searchResult.jsp" method="post"><postfield name="type" value="1"/><%
		if(bean.getCity()>0){
		%><postfield name="city" value="<%=bean.getCity()%>"/><%
		}
		if(bean.getAstro()>0){
		%><postfield name="astro" value="<%=bean.getAstro()%>"/><%
		}
		if(bean.getGender()>0){
		%><postfield name="gender" value="<%=bean.getGender()%>"/><%
		}
		if(bean.getAim()>0){
		%><postfield name="aim" value="<%=bean.getAim()%>"/><%
		}
	 	%></go></anchor><br/><%
	}else{
		for(int i=0;i<hotSize;i++){
			BeanSearch bean = (BeanSearch)SearchAction.hot.get(i);
			String show = "";
			int cc = bean.getCity();
			if(cc > 0){
				CreditCity ccc = (CreditCity)cityList.get(cc-1);
				show = show+"/"+ccc.getCity();
			}
			show = action.getShow(bean,show);
			%><%=show%><anchor>搜索<go href="searchResult.jsp" method="post"><postfield name="type" value="1"/><%
			if(bean.getCity()>0){
			%><postfield name="city" value="<%=bean.getCity()%>"/><%
			}
			if(bean.getAstro()>0){
			%><postfield name="astro" value="<%=bean.getAstro()%>"/><%
			}
			if(bean.getGender()>0){
			%><postfield name="gender" value="<%=bean.getGender()%>"/><%
			}
			if(bean.getAim()>0){
			%><postfield name="aim" value="<%=bean.getAim()%>"/><%
			}
		 	%></go></anchor><br/><%
		}
	}
}
%>————————<br/>
搜索乐酷友友<br/>
<input name="id"  maxlength="10" format="*N" value=""/><br/>
<anchor title="确定">ID搜索<go href="../../user/searchUserResult.jsp" method="post"><postfield name="id" value="$id"/></go></anchor><br/>
<input name="nickname" maxlength="20" value=""/><br/>
<anchor title="确定">昵称搜索<go href="../../user/searchUserResult.jsp" method="post"><postfield name="nickname" value="$nickname"/></go></anchor><br/>
[<a href="searchCenter.jsp">搜索中心</a>]<br/>
[<a href="../friendCenter.jsp">交友中心</a>]<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>