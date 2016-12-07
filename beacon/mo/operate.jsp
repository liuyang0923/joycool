<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page	import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.service.factory.*"%><%!
	MoodService service=new MoodService();
	net.joycool.wap.service.infc.IUserService userService = ServiceFactory.createUserService();
%><%
	response.setHeader("Cache-Control", "no-cache");
	MoodAction action=new MoodAction(request);
	int id = action.getParameterInt("id");
	int type = action.getParameterInt("xq");
	String f = action.getParameterString("f");
	boolean result = false;
	String tip="";
	if("reply".equals(f)){
		//用户是否被加至黑名单
		MoodBean mb = service.getMoodById(id);
		if ( mb != null ){
			boolean isABadGuys = userService.isUserBadGuy(mb.getUserId(),action.getLoginUser().getId());
			if ( isABadGuys ){
				action.sendRedirect("error.jsp?t=1&id=" + mb.getId(),response);
				return;
			}
			//过滤特殊字符串
			String reply = action.getParameterString("reply");
			//字数限制
			if (reply == null){
				response.sendRedirect("mood.jsp?userId=" + action.getLoginUser().getId());
				return;
			} else if (reply.length() > 100 ){
				action.sendRedirect("error.jsp?t=2&id=" + id,response);
				return ;
			}
			result = action.reply();
		}
	}else if("create".equals(f)){
		//字数限制
		String mood=action.getParameterString("mood");
		if (mood == null){
			response.sendRedirect("mood.jsp?userId=" + action.getLoginUser().getId());
			return;
		} else if (mood.length() > 100 || type<1 || type>30){
			action.sendRedirect("error.jsp?t=3&id=" + id,response);
			return ;
		}
		result=action.create(type);
	}else if("rdel".equals(f)){
		result = action.rdel();
	}//else if("mdel".equals(f)){
	 //	//删除一个心情。此功能已被屏蔽
	 //	result=action.mdel();
	 //}
	tip=(String)request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="心情"><p><%
if ("reply".equals(f)){
	//如果输入为空，则返回复页面，否则返回心情列表
	if("回复不能为空".equals(tip)){
		%><%=tip%><br/><a href="view.jsp?id=<%=action.getAttributeInt("moodId")%>">返回上级</a><%
	}else{
		%><%=tip%><br/><a href="mood.jsp?id=<%=action.getAttributeInt("id")%>">返回上级</a><%
	}
}else if ("create".equals(f)){
	%><%=tip%><br/><a href="mood.jsp">返回上级</a><%
}else if ("rdel".equals(f)){
	%><%=tip%><br/><a href="view.jsp?id=<%=action.getAttributeInt("moodId")%>">返回上级</a><%
}else if ("mdel".equals(f)){
	%><%=tip%><br/><a href="mood.jsp">返回上级</a><%
}else if("null".equals(f)){
	//如果心情不存在，则直接返回他自己的个人资料
	%>此心情不存在<br/><a href="../../user/ViewUserInfo.do?userId=<%=action.getLoginUser().getId() %>">返回</a><%
}else if("null2".equals(f)){
	//最新心情不存在
	%>没有与你同心情的玩家.<br/><a href="mood.jsp">返回</a><%
}%></p></card>
</wml>