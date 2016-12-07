<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   String str = "";
   GuestUserInfo guestUser = action.getGuestUser();
   if (guestUser == null){
	   response.sendRedirect("login.jsp");
	   return;
   }
   int[] ihave = action.getMyTitle(guestUser);
   int getTitle = action.getParameterInt("gt");		// 想要获得的称号ID
   PagingBean paging = new PagingBean(action, ihave.length, COUNT_PRE_PAGE, "p");
   // 注意:因为我要对数组分页，而数第的第0个元素不需要显示，所以我要对起止的ID做些处理。
   int startPage = paging.getStartIndex() + 1;
   int endPage = paging.getEndIndex();
   if (endPage + 1 <= ihave.length){
	   endPage++;
   }
   if (getTitle < 0 || getTitle > GuestHallAction.title.length - 1){
	   getTitle = 0;
   }
   if (getTitle > 0){
	   // 看有没有资格获得称号
	   if (ihave[getTitle] == 1){
		   str = "获得称号失败,您已经有此称号了!<br/>";
	   } else {
		   // 开始设置称号
		   boolean result = action.addTitle2(guestUser,getTitle);
		   if (result){
			   str = "恭喜你获得称号\"" + GuestHallAction.title[getTitle] + "\",喜欢就选择它作为你的展示称号吧!<br/><a href=\"title.jsp\">继续</a><br/>";
		   } else {
			   str = "获得称号失败,请查看此称号的必备条件,努力吧!<br/><a href=\"title.jsp\">尝试其它称号</a><br/>";
		   }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="获得称号"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (getTitle > 0){
	%><%=str%><%	
} else {
	if (ihave != null){
		if (ihave.length == 0){
			%>您已经获得了所有称号.<br/><%
		} else {
			for (int i = startPage ; i <  endPage; i++){
				%><%=i%>.<%
				if (ihave[i] == 0){
					%><a href="title.jsp?gt=<%=i%>"><%=GuestHallAction.title[i]%></a>:<%
				} else {
					%><%=GuestHallAction.title[i]%>:<%
				}
				%><%=GuestHallAction.titleDesc[i]%><br/><%
			}%><%=paging.shuzifenye("title.jsp",false,"|",response)%><%
		}
	}
}
%>
<a href="info.jsp">返回个人资料</a><br/>
<%
} else {
%><%=tip%><br/><a href="index.jsp">返回游乐园</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>