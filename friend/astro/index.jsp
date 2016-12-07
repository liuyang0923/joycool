<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.friend.*,net.joycool.wap.bean.*,net.joycool.wap.spec.friend.*,net.joycool.wap.service.impl.FriendServiceImpl,net.joycool.wap.service.factory.ServiceFactory,jc.credit.UserBase,jc.credit.CreditAction"%>
<% response.setHeader("Cache-Control","no-cache");
   AstroAction action = new AstroAction(request);
   if (action.getLoginUser() == null){
	   // 没登陆,返回登陆页面
	   response.sendRedirect("/user/login.jsp");
	   return;
   }
   CreditAction action2 = new CreditAction(request);
   boolean isMyAstro = false;
   AstroCess cess = null;
   UserBean user = null;
   String tip = "";
   int astroId = 0;
   int uid = action.getParameterInt("uid");
   // 传来的UID错误，则让UID等于自己
   if (uid <= 0){
	   uid = action.getLoginUser().getId();
	   isMyAstro = true;
   }
   int today = action.getParameterInt("td");
   if (today < 0 || today > 1) today = 0;
   // 取得UID的星座
   UserBase userBase = CreditAction.getUserBaseBean(uid);
   if (userBase != null && userBase.getBirMonth() != 0 && userBase.getBirDay() != 0){
	   astroId = userBase.getAstro();
   }
//   FriendServiceImpl service2 = (FriendServiceImpl)ServiceFactory.createFriendService();
//   FriendBean friendBean = service2.getFriend(uid>0?uid:action.getLoginUser().getId());
//   if (friendBean != null){
//	   // 交友中心的星座是从0开始，而这里是以1开始的
//	   astroId = friendBean.getConstellation() + 1;
//   }
   if (astroId <= 0){
	   if (uid == action.getLoginUser().getId()){
		   tip = "您还没有填写生日,无法查看星座信息,请先去<a href=\"../credit/credit.jsp\">填写</a>.";
	   } else {
		   tip = "TA还没有填写生日,无法和TA配对.";
	   }
   } else {
//	   if (uid == 0 && action.getLoginUser() != null){
//	   		uid = action.getLoginUser().getId();
//	   		isMyAstro = true;
//	   }
	   user = UserInfoUtil.getUser(uid);
	   if (user == null){
	   		tip = "用户不存在.";
	   } else {
	   		if (today == 1){
	   			// 明日星运
	   			cess = AstroAction.service.getCess(" astro_id = " + astroId +" and substr(date_time,1,10) = substr(DATE_ADD(now(),INTERVAL 1 DAY),1,10)");  // 最后别忘了加时间条件
	   		} else {
	   			// 今日星运
	   			cess = AstroAction.service.getCess(" astro_id=" + astroId + " and substr(date_time,1,10) = substr(now(),1,10)");  // 最后别忘了加时间条件
	   		}
	   		if (cess == null){
	   			tip ="对不起,您查询的星座信息不存在.";
	   		}
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="星座"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
if (isMyAstro){
	%>我的星座<br/>
<%=action.getAstroList().get(astroId)%><br/><%
	if (today == 0){
		%>今日运势|<a href="index.jsp?td=1">明日运势</a><br/><%
	} else {
		%><a href="index.jsp">今日运势</a>|明日运势<br/><%
	}
} else {
	%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>的星座<br/>
	<a href="supei.jsp?uid=<%=uid%>&amp;sp=1">*与TA星座速配</a><br/>
<%=action.getAstroList().get(astroId)%><br/><%
}%>综合运势：<%=action.showStar(cess.getAll())%><br/>
爱情运势：<%=action.showStar(cess.getLove())%><br/>
事业运势：<%=action.showStar(cess.getCareer())%><br/>
理财运势：<%=action.showStar(cess.getFain())%><br/>
健康指数：<%=cess.getHealth()%>%<br/>
商业指数：<%=cess.getBusiness()%>%<br/>
幸运颜色：<%=cess.getColor()%><br/>
幸运数字：<%=cess.getNum()%><br/>
速配星座：<%=action.getAstroNameNoDate(cess.getOtherAstro())%><br/>
综合评价：<br/><%=StringUtil.toWml(cess.getContent()) %><br/>
<% if (!isMyAstro){
	%><a href="supei.jsp?uid=<%=uid%>&amp;sp=1">*与TA星座速配</a><br/><%
}%><a href="intro.jsp?id=<%=astroId%>">星座解说</a>|<a href="/jcforum/forum.jsp?forumId=1181">星座奇缘</a><br/>
<% if (isMyAstro){
		ArrayList supeiList = (ArrayList)AstroAction.service.getUserSupeiList(" to_uid = " + action.getLoginUser().getId() + " order by create_time desc limit 5");
		if (supeiList.size() > 0){
			AstroUserSupei usp = null;
			for (int i = 0; i < supeiList.size();i++){
				usp = (AstroUserSupei)supeiList.get(i);
				user = UserInfoUtil.getUser(usp.getFromUid());
				%>*<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a><%=DateUtil.converDateToBefore(new Date(usp.getCreateTime()))%>对你进行<a href="supei.jsp?uid=<%=user.getId()%>">星座速配</a><br/><%
			}
		} else {
			%>暂时还没有用户与你速配.<br/><%
		}
} else {
		ArrayList supeiList = (ArrayList)AstroAction.service.getUserSupeiList(" to_uid = " + uid + " order by create_time desc limit 5");
		if (supeiList.size() > 0){
			AstroUserSupei usp = null;
			for (int i = 0; i < supeiList.size();i++){
				usp = (AstroUserSupei)supeiList.get(i);
				user = UserInfoUtil.getUser(usp.getFromUid());
				%>*<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a><%=DateUtil.converDateToBefore(new Date(usp.getCreateTime()))%>对<%=UserInfoUtil.getUser(uid).getGenderText()%>进行<a href="supei.jsp?uid=<%=user.getId()%>&amp;uid2=<%=uid %>">星座速配</a><br/><%
			}
		} else {
			%>暂时还没有用户与<%=UserInfoUtil.getUser(uid).getGenderText()%>速配.<br/><%
		}
}
} else {
	%><%=tip%><br/><a href="/lswjs/index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>