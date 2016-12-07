<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
// UserBase userBase = action.service.getUserBase(" user_id=" + action.getLoginUser().getId());
   Property property = null;
   int type = action.getParameterInt("t");
   int modify = action.getParameterInt("m");
   String pid = action.getParameterNoCtrl("pid");
   int shenfen = action.getParameterInt("sf");
   int hangye = action.getParameterInt("hy");
   int shouru = action.getParameterInt("sr");
   int mengxiang = action.getParameterInt("mx");
   int lianai = action.getParameterInt("la");
   int maidan = action.getParameterInt("md");
   int chouyan = action.getParameterInt("cy");
   int hunyin = action.getParameterInt("hy");
   int haizi = action.getParameterInt("hz");
   int tixing = action.getParameterInt("tx");
   int waimao = action.getParameterInt("wm");
   int meili = action.getParameterInt("ml");
   int submit = action.getParameterInt("s");
   UserBase userBase = null;
   UserWork userWork = null;
   UserLive userLive = null;
   UserLooks userLooks = null;
   UserContacts userContacts = null;
   List list = null;
   String tip = "";
   int tmp = 0;
   if (type < 1){
	   tip = "类别编号错误.";
   } else {
	   if (submit == 1){
		   action.validateSelect(type,modify,response);
		   tip = (String)request.getAttribute("tip");
		   if (tip == null){
			   return;
		   }
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
switch (type){
case 3:			//选择你的学历
	%>请选择您的学历:<br/><%
	list = CreditAction.service.getPropertyList(" flag=0");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;s=1&amp;m=<%=modify%>&amp;pid=<%=property.getId()%>"><%=property.getContent()%></a>.<%
	}%><br/><%
	break;
case 4:			//选择个性类型
	%>个性类型:<br/><%
	list = CreditAction.service.getPropertyList(" flag=1");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;s=1&amp;m=<%=modify%>&amp;pid=<%=property.getId()%>"><%=property.getContent()%></a>.<%
	}%><br/><%
	break;
case 5:			//选择血型
	%>血型:<br/><%
	list = CreditAction.service.getPropertyList(" flag=2");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;s=1&amp;m=<%=modify%>&amp;pid=<%=property.getId()%>"><%=property.getContent()%></a>.<%
	}%><br/><%
	break;
case 6:			//选择身份与行业
	userWork = CreditAction.service.getUserWork(" user_id=" + action.getLoginUser().getId());
	if(userWork==null){userWork = new UserWork();}
	shenfen = shenfen==0?userWork.getIdentity():shenfen;
	hangye = hangye==0?userWork.getTrade():hangye;
	%>请选择您的身份:<%if(shenfen!=0){%><%=CreditAction.getPropertyString(shenfen)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=3");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;m=<%=modify%>&amp;sf=<%=property.getId()%>&amp;hy=<%=hangye%>"><%=property.getContent()%></a>.<%
	}%><br/><%
	%>请选择您的行业:<%if(hangye!=0){%><%=CreditAction.getPropertyString(hangye)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=4");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;m=<%=modify%>&amp;hy=<%=property.getId()%>&amp;sf=<%=shenfen%>"><%=property.getContent()%></a>.<%
	}%><br/><br/><a href="select2.jsp?s=1&amp;m=<%=modify%>&amp;t=<%=type%>&amp;hy=<%=hangye%>&amp;sf=<%=shenfen%>">确认</a><br/><%
	break;
case 7:			//选择收入与梦想
	userWork = CreditAction.service.getUserWork(" user_id=" + action.getLoginUser().getId());
	if(userWork==null){response.sendRedirect("select2.jsp?t=6");return;}	// 没有记录，让用户从头开始填
	shouru = shouru==0?userWork.getEarning():shouru;
	mengxiang = mengxiang==0?userWork.getDream():mengxiang;
	%>请选择您的收入:<%if(shouru!=0){%><%=CreditAction.getPropertyString(shouru)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=5");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;m=<%=modify%>&amp;sr=<%=property.getId()%>&amp;mx=<%=mengxiang%>"><%=property.getContent()%></a>.<%
	}%><br/><%
	%>请选择您的职位梦想:<%if(mengxiang!=0){%><%=CreditAction.getPropertyString(mengxiang)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=6");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?&amp;m=<%=modify%>&amp;t=<%=type%>&amp;mx=<%=property.getId()%>&amp;sr=<%=shouru%>"><%=property.getContent()%></a>.<%
	}%><br/><br/><a href="select2.jsp?s=1&amp;m=<%=modify%>&amp;t=<%=type%>&amp;mx=<%=mengxiang%>&amp;sr=<%=shouru%>">确认</a><br/><%
	break;
case 8:			//选择最长恋爱时间、埋单、抽烟
	userLive = CreditAction.service.getUserLive(" user_id=" + action.getLoginUser().getId());
	if(userLive==null){userLive=new UserLive();}
	lianai = lianai==0?userLive.getLoveTime():lianai;
	maidan = maidan==0?userLive.getBill():maidan;
	chouyan = chouyan==0?userLive.getSmoke():chouyan;
	%>请选择最长恋爱时间:<%if(lianai!=0){%><%=CreditAction.getPropertyString(lianai)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=7");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;m=<%=modify%>&amp;la=<%=property.getId()%>&amp;md=<%=maidan%>&amp;cy=<%=chouyan %>"><%=property.getContent()%></a>.<%
	}%><br/><%
	%>请选择恋爱谁买单:<%if(maidan!=0){%><%=CreditAction.getPropertyString(maidan)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=8");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;m=<%=modify%>&amp;md=<%=property.getId()%>&amp;la=<%=lianai%>&amp;cy=<%=chouyan%>"><%=property.getContent()%></a>.<%
	}%><br/>
	请选择是否抽烟:<%if(chouyan!=0){%><%=CreditAction.getPropertyString(chouyan)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=9");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;m=<%=modify%>&amp;cy=<%=property.getId()%>&amp;la=<%=lianai%>&amp;md=<%=maidan%>"><%=property.getContent()%></a>.<%
	}%><br/><br/><a href="select2.jsp?s=1&amp;m=<%=modify%>&amp;t=<%=type%>&amp;cy=<%=chouyan%>&amp;la=<%=lianai%>&amp;md=<%=maidan%>">确认</a><br/><%
	break;
case 9:			//选择婚姻状况、有无小孩
	userLive = CreditAction.service.getUserLive(" user_id=" + action.getLoginUser().getId());
	if(userLive==null){response.sendRedirect("select2.jsp?t=8");return;}
	hunyin = hunyin==0?userLive.getMarrage():hunyin;
	haizi = haizi==0?userLive.getChild():haizi;
	%>请选择婚姻状况:<%if(hunyin!=0){%><%=CreditAction.getPropertyString(hunyin)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=10");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;m=<%=modify%>&amp;hy=<%=property.getId()%>&amp;hz=<%=haizi%>"><%=property.getContent()%></a>.<%
	}%><br/><%
	%>请选择有无小孩:<%if(haizi!=0){%><%=CreditAction.getPropertyString(haizi)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=11");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?&amp;m=<%=modify%>&amp;t=<%=type%>&amp;hz=<%=property.getId()%>&amp;hy=<%=hunyin%>"><%=property.getContent()%></a>.<%
	}%><br/><br/><a href="select2.jsp?s=1&amp;m=<%=modify%>&amp;t=<%=type%>&amp;hz=<%=haizi%>&amp;hy=<%=hunyin%>">确认</a><br/><%
	break;
case 10:			//选择体形、外貌、魅力部位
	userLooks = CreditAction.service.getUserLooks(" user_id=" + action.getLoginUser().getId());
	if(userLooks==null){userLooks=new UserLooks();}
	tixing = tixing==0?userLooks.getBodyType():tixing;
	waimao = waimao==0?userLooks.getLooks():waimao;
	meili = meili==0?userLooks.getCharm():meili;
	%>请选择您的体型范围:<%if(tixing!=0){%><%=CreditAction.getPropertyString(tixing)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=12");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;m=<%=modify%>&amp;tx=<%=property.getId()%>&amp;wm=<%=waimao%>&amp;ml=<%=meili%>"><%=property.getContent()%></a>.<%
	}%><br/><%
	%>请选择外貌自评:<%if(waimao!=0){%><%=CreditAction.getPropertyString(waimao)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=13");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?t=<%=type%>&amp;m=<%=modify%>&amp;wm=<%=property.getId()%>&amp;tx=<%=tixing%>&amp;ml=<%=meili%>"><%=property.getContent()%></a>.<%
	}%><br/>请选择魅力部位:<%if(meili!=0){%><%=CreditAction.getPropertyString(meili)%><%}%><br/><%
	list = CreditAction.service.getPropertyList(" flag=14");
	for (int i=0;i<list.size();i++){
		property = (Property)list.get(i);
		%><a href="select2.jsp?&amp;m=<%=modify%>&amp;t=<%=type%>&amp;ml=<%=property.getId()%>&amp;tx=<%=tixing%>&amp;wm=<%=waimao%>"><%=property.getContent()%></a>.<%
	}%><br/><br/><a href="select2.jsp?s=1&amp;m=<%=modify%>&amp;t=<%=type%>&amp;ml=<%=meili%>&amp;tx=<%=tixing%>&amp;wm=<%=waimao%>">确认</a><br/><%
	break;
default:
	%>分类不存在.<br/><a href="navi.jsp">返回</a><br/><%
}
} else {
%><%=tip%><br/>
<%if (type >=1 && type <=3){
%><a href="navi.jsp">返回</a><%
} else if (type == 6 || type == 7){
%><a href="navi.jsp?t=2">返回</a><%
} else if (type == 8 || type == 9){
%><a href="navi.jsp?t=2">返回</a><%	
} else if (type == 10){
%><a href="navi.jsp?t=3">返回</a><%	
}
}
%><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>