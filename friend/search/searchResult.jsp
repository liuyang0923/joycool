<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,jc.credit.*,java.util.*,jc.search.*,net.joycool.wap.framework.BaseAction"%><%
	response.setHeader("Cache-Control","no-cache");
	SearchAction action = new SearchAction(request);
	UserBean ub = action.getLoginUser();
	if(ub == null){
		response.sendRedirect("/user/login.jsp");
		return;
	}
	CreditCity creditCity = null;
	UserBase base = null;
	String[] guides = {"","综合搜索","交友搜索","老乡搜索","星座搜索","高级搜索","同城异性","所有异性","同生缘","同城缘"};
	String[] returnJsps = {"","searchCenter.jsp","emotionSearch.jsp","homeSearch.jsp","astroSearch.jsp","superSearch.jsp","searchCenter.jsp","searchCenter.jsp","searchCenter.jsp","searchCenter.jsp"};
	List list = action.search();
	List randomList = null;
	if(list == null || list.size() < 5)
		randomList = action.randomSearch(list,0,0);
	String strType = String.valueOf(request.getAttribute("type"));
	int type = 0;
	if(strType != null && !"null".equals(strType))
		type = Integer.parseInt(strType);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%if(type > 0 && type<=9){%><%=guides[type]%><%}else{%>搜索结果<%}%>"><p><%=BaseAction.getTop(request, response)%><%
if(list != null){
	%><a href="/friend/friendCenter.jsp">交友中心</a>&gt;<a href="searchCenter.jsp">搜索中心</a><%if(type > 0 && type<=9){
	%>&gt;<%=guides[type]%><%}%><br/><%if(list != null && list.size() > 0){%>符合您搜索条件的用户<%=list.size()%>个，如下：<br/><%
		PagingBean paging = new PagingBean(action, list.size(), 5, "p");
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			base = CreditAction.getUserBaseBean(Integer.parseInt(String.valueOf(list.get(i))));
			if(base != null){
				creditCity = SearchAction.service.getCity(" id="+base.getCity());
			%><a href="/user/ViewUserInfo.do?userId=<%=base.getUserId()%>"><%=UserInfoUtil.getUser(base.getUserId()).getNickNameWml()%></a><br/><%
				if(!"".equals(base.getPhoto())){
				%><img src="<%= CreditAction.ATTACH_URL_ROOT+"/"+base.getPhoto()%>" alt="o"/><%
				}else{
				%>该用户暂未上传照片<%
				}
			%><br/><%=base.getAge()%>岁,<%if(base.getGender()==0){%>女<%}else{%>男<%}if(creditCity!=null){%>&#160;<%=creditCity.getCity()%><%}%><br/><a href="/user/ViewUserInfo.do?userId=<%=base.getUserId()%>">看TA资料</a>|<a href="/chat/post.jsp?toUserId=<%=base.getUserId()%>">与TA聊天</a>|<a href="/home/home.jsp?userId=<%=base.getUserId()%>">看TA家园</a><br/>————————<br/><%
			}
		}
		%><%=paging.shuzifenye("searchResult.jsp?jcfr=1&amp;from=1&amp;type="+type, true, "|", response)%><%
		if(list.size()>=30){
			int pageindex = action.getParameterInt("p");
			if(pageindex == 5){
				base = CreditAction.getUserBaseBean(Integer.parseInt(String.valueOf(list.get(list.size() - 1))));
			%><anchor>更多<go href="searchResult.jsp" method="post"><postfield name="from" value="1"/><postfield name="startId" value="<%=base.getUserId()%>"/><postfield name="type" value="<%=type%>"/></go></anchor><br/><%
			}
		}
		if(list.size() < 5 && randomList != null && randomList.size() > 0){%>下面可能是您感兴趣的用户<br/><%
			for(int i=0;i<randomList.size();i++){
				base = CreditAction.getUserBaseBean(Integer.parseInt(String.valueOf(randomList.get(i))));
				if(base != null){
					creditCity = SearchAction.service.getCity(" id="+base.getCity());
				%><a href="/user/ViewUserInfo.do?userId=<%=base.getUserId()%>"><%=UserInfoUtil.getUser(base.getUserId()).getNickNameWml()%></a><br/><%
					if(!"".equals(base.getPhoto())){
					%><img src="<%= CreditAction.ATTACH_URL_ROOT+"/"+base.getPhoto()%>" alt="o"/><%
					}else{
					%>该用户暂未上传照片<%
					}
				%><br/><%=base.getAge()%>岁,<%if(base.getGender()==0){%>女<%}else{%>男<%}if(creditCity!=null){%>&#160;<%=creditCity.getCity()%><%}%><br/><a href="/user/ViewUserInfo.do?userId=<%=base.getUserId()%>">看TA资料</a>|<a href="/chat/post.jsp?toUserId=<%=base.getUserId()%>">与TA聊天</a>|<a href="/home/home.jsp?userId=<%=base.getUserId()%>">看TA家园</a><br/>————————<br/><%
				}
			}
		}
	}else if(randomList != null && randomList.size() > 0){%>没有符合要求的用户,下面可能是您感兴趣的用户<br/><%
			for(int i=0;i<randomList.size();i++){
				base = CreditAction.getUserBaseBean(Integer.parseInt(String.valueOf(randomList.get(i))));
				if(base != null){
					creditCity = SearchAction.service.getCity(" id="+base.getCity());
				%><a href="/user/ViewUserInfo.do?userId=<%=base.getUserId()%>"><%=UserInfoUtil.getUser(base.getUserId()).getNickNameWml()%></a><br/><%
					if(!"".equals(base.getPhoto())){
					%><img src="<%= CreditAction.ATTACH_URL_ROOT+"/"+base.getPhoto()%>" alt="o"/><%
					}else{
					%>该用户暂未上传照片<%
					}
				%><br/><%=base.getAge()%>岁,<%if(base.getGender()==0){%>女<%}else{%>男<%}if(creditCity!=null){%>&#160;<%=creditCity.getCity()%><%}%><br/><a href="/user/ViewUserInfo.do?userId=<%=base.getUserId()%>">看TA资料</a>|<a href="/chat/post.jsp?toUserId=<%=base.getUserId()%>">与TA聊天</a>|<a href="/home/home.jsp?userId=<%=base.getUserId()%>">看TA家园</a><br/>————————<br/><%
				}
			}
	}else{%>没有符合要求的用户<br/><%}if(type > 1 && type < 6){%>[<a href="<%=returnJsps[type]%>">重新搜索</a>]<br/><%}
}else{
%>您的可信度值过低,请到可信度中<a href="/friend/credit/credit.jsp">填写信息</a>后再进行尝试!<br/><%
}%>[<a href="searchCenter.jsp">搜索中心</a>]<br/>
[<a href="/friend/friendCenter.jsp">交友中心</a>]<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>