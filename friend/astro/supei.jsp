<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.spec.friend.*,net.joycool.wap.service.impl.FriendServiceImpl,net.joycool.wap.bean.friend.*,net.joycool.wap.service.factory.ServiceFactory,jc.credit.UserBase,jc.credit.CreditAction"%>
<% response.setHeader("Cache-Control","no-cache");
   AstroAction action = new AstroAction(request);
   CreditAction action2 = new CreditAction(request);
   int uid = action.getParameterInt("uid");		// uid与uid2配对。如果uid2不存在，就与我配对
   int uid2 = action.getParameterInt("uid2");
  
   boolean isMyAstro = false;
   AstroSupei mySupei = null;
   UserBean user = null;
   UserBean user2 = null;
   String tip = "";
   int astroId1 = 0;
   int astroId2 = 0;
   
   user = UserInfoUtil.getUser(uid);
   if (uid2 != 0){
   		user2 = UserInfoUtil.getUser(uid2);
   } else {
   		user2 = action.getLoginUser();
   		isMyAstro = true;
   }
   if (user == null || user2 == null){
   		tip = "用户不存在.";
   } else if (user.getId() == user2.getId()){
   		tip = "不能与自己配对.";
   } else if (user.getGender() == user2.getGender()){
   		tip = "同性间不能配对.";
   } else {
   		int supei = action.getParameterInt("sp");
//   		// 取得2人的星座
//   	    FriendServiceImpl service2 = (FriendServiceImpl)ServiceFactory.createFriendService();
//   	    FriendBean friendBean = service2.getFriend(uid);
//   	    if (friendBean != null){
//   		   // 交友中心的星座是从0开始，而这里是以1开始的
//   		   astroId2 = friendBean.getConstellation() + 1;
//   	    }
//   	 	friendBean = service2.getFriend(isMyAstro?action.getLoginUser().getId():uid2);
//   	    if (friendBean != null){
//    	   // 交友中心的星座是从0开始，而这里是以1开始的
//    	   astroId1 = friendBean.getConstellation() + 1;
//    	}
   		UserBase userBase = CreditAction.getUserBaseBean(uid) ;
   		if (userBase != null && userBase.getBirMonth() != 0 && userBase.getBirDay() != 0){
   			astroId1 = AstroAction.getAstroIdByDate(userBase.getBirMonth(),userBase.getBirDay());
  		}
   		userBase = CreditAction.getUserBaseBean(isMyAstro?action.getLoginUser().getId():uid2) ;
   		if (userBase != null && userBase.getBirMonth() != 0 && userBase.getBirDay() != 0){
  			astroId2 = AstroAction.getAstroIdByDate(userBase.getBirMonth(),userBase.getBirDay());
   		}
   		// 从别人的星座首页处点了“与TA速配”
   		if (supei == 1){
	   		// 写入数据库
	   		AstroUserSupei userSupei = new AstroUserSupei();
	   		// from_uid 对 to_uid 进行了速配
	   		userSupei.setFromUid(action.getLoginUser().getId());
	   		userSupei.setToUid(uid);
	   		userSupei.setFlag(0);
	   		boolean result = AstroAction.service.newUserSupei(userSupei);
	   		// 如果“我”是男的...
	   		if (user2.getGender() == 1){
	   			// astro1 男生的星座 astro2 女生的星座
	   			mySupei = AstroAction.service.getSupei(" astro1 = " + astroId2 + " and astro2 = " + astroId1 + " and flag = 0");
	   		} else {
	   			mySupei = AstroAction.service.getSupei(" astro1 = " + astroId1 + " and astro2 = " + astroId2 + " and flag = 1");
	   		}
   		} else {
   			// 只是查看记录
	   		if (user.getGender() == 1){
	   			// astro1 男生的星座 astro2 女生的星座
	   			mySupei = AstroAction.service.getSupei(" astro1 = " + astroId2 + " and astro2 = " + astroId1 + " and flag = 0");
	   		} else {
	   			mySupei = AstroAction.service.getSupei(" astro1 = " + astroId1 + " and astro2 = " + astroId2 + " and flag = 1");
	   		}
   		}
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="星座"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
	%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>星座:<%if(astroId1<=0){%>TA还没有完善星座信息<%}else{%><a href="intro.jsp?id=<%=astroId1%>"><%=action.getAstroNameNoDate(astroId1) %></a><%}%><br/>
<% if (isMyAstro){
		%>我的星座:<%if(astroId2<=0){%>我还没有完善星座信息<%}else{%><a href="intro.jsp?id=<%=astroId2%>"><%=action.getAstroNameNoDate(astroId2) %></a><%}%><br/><%
} else {
		%><a href="/user/ViewUserInfo.do?userId=<%=user2.getId()%>"><%=user2.getNickNameWml()%></a>星座:<%if(astroId2<=0){%>TA还没有完善星座信息<%}else{%><a href="intro.jsp?id=<%=astroId2 %>"><%=action.getAstroNameNoDate(astroId2) %></a><%}%><br/><%
}%>
<% if (mySupei != null){
%>星座配对指数:<%=mySupei.getExp()%><br/>
星座配对比重:<%=StringUtil.toWml(mySupei.getProportion())%><br/>
星座配对点评:<br/>
<%=StringUtil.toWml(mySupei.getContent())%><br/>
<%}%>
<%if (isMyAstro){
	%><a href="index.jsp">返回星座</a><br/><%
} else {
	%><a href="index.jsp?uid=<%=uid%>">返回星座</a><br/><%
}%><a href="/home/home.jsp">返回家园</a><br/>
<%} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}%><%=BaseAction.getBottomShort(request, response)%></p></card></wml>