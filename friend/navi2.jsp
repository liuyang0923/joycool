<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*,net.joycool.wap.service.factory.ServiceFactory"%>
<%!net.joycool.wap.service.infc.IUserService userService = ServiceFactory.createUserService();
   static String aim[] = {"未填写","恋人","知已","玩伴","其它"};%>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   int type = action.getParameterInt("t");
   int uid = action.getParameterInt("uid");
   int tmp = 0;
   String topLink = "";
   String tip = "";
   List friendList = null;
   UserInfo userInfo = null;
   UserBase userBase = null;
   UserWork userWork = null;
   UserLive userLive = null;
   UserLooks userLooks = null;
   UserContacts userContacts = null;
   if (UserInfoUtil.getUser(uid) == null){
   	tip = "用户ID:" + uid + "不存在.";
   } else {
	   friendList = UserInfoUtil.getUserFriends(uid);
	   // UID是我的好友，就可以查看
	   if ((!action.getLoginUser().getFriendList().contains(uid + "")) || userService.isUserBadGuy(uid,action.getLoginUser().getId())){
		   tip = "对方还不是您的好友,不可查看可信度.";
	   } else {
	   	   userInfo = action.getUserInfo(uid);
	   	   if (type < 0 || type > 4){
	   		   type = 4;
	   	   }
	   	   switch (type){
	   		   case 0:
	   			   userBase = CreditAction.getUserBaseBean(uid);
	   			   topLink="基本|<a href=\"navi2.jsp?t=1&amp;uid=" + uid + "\">工作</a>|<a href=\"navi2.jsp?t=2&amp;uid=" + uid + "\">生活</a>|<a href=\"navi2.jsp?t=3&amp;uid=" + uid + "\">外貌</a>|<a href=\"navi2.jsp?t=4&amp;uid=" + uid + "\">联系</a><br/>";
	   			   break;
	   		   case 1:
	   			   userWork = CreditAction.service.getUserWork(" user_id=" + uid);
	   			   topLink="<a href=\"navi2.jsp?uid=" + uid + "\">基本</a>|工作|<a href=\"navi2.jsp?t=2&amp;uid=" + uid + "\">生活</a>|<a href=\"navi2.jsp?t=3&amp;uid=" + uid + "\">外貌</a>|<a href=\"navi2.jsp?t=4&amp;uid=" + uid + "\">联系</a><br/>";
	   			   break;
	   		   case 2:
	   			   userLive = CreditAction.service.getUserLive(" user_id=" + uid);
	   			   topLink="<a href=\"navi2.jsp?uid=" + uid + "\">基本</a>|<a href=\"navi2.jsp?t=1&amp;uid=" + uid + "\">工作</a>|生活|<a href=\"navi2.jsp?t=3&amp;uid=" + uid + "\">外貌</a>|<a href=\"navi2.jsp?t=4&amp;uid=" + uid + "\">联系</a><br/>";
	   			   break;
	   		   case 3:
	   			   userLooks = CreditAction.service.getUserLooks(" user_id=" + uid);
	   			   topLink="<a href=\"navi2.jsp?uid=" + uid + "\">基本</a>|<a href=\"navi2.jsp?t=1&amp;uid=" + uid + "\">工作</a>|<a href=\"navi2.jsp?t=2&amp;uid=" + uid + "\">生活</a>|外貌|<a href=\"navi2.jsp?t=4&amp;uid=" + uid + "\">联系</a><br/>";
	   			   break;
	   		   case 4:
	   			   userContacts = CreditAction.service.getUserContacts(" user_id=" + uid);
	   			   topLink="<a href=\"navi2.jsp?uid=" + uid + "\">基本</a>|<a href=\"navi2.jsp?t=1&amp;uid=" + uid + "\">工作</a>|<a href=\"navi2.jsp?t=2&amp;uid=" + uid + "\">生活</a>|<a href=\"navi2.jsp?t=3&amp;uid=" + uid + "\">外貌</a>|联系<br/>";
	   			   break;
	   	   } 
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
<%if("".equals(tip)){
%><%=UserInfoUtil.getUser(userInfo.getUserId()).getGenderText()%>目前的交友可信度为<%=userInfo.getTotalPoint()%>%<br/>
<%=topLink%> 
<%switch (type){
case 0:
%><%if(userBase != null && userBase.getPhoto() != null && !"".equals(userBase.getPhoto())){%><img src="<%=CreditAction.ATTACH_URL_ROOT + "/" + userBase.getPhoto()%>" alt="loading..."/><br/><%}%>
生日:<%if(userBase == null || userBase.getBirYear() == 0 || userBase.getBirMonth() == 0 || userBase.getBirDay() == 0){%>未填写<%}else{%><%=userBase.getBirYear()%>年<%=userBase.getBirMonth()%>月<%=userBase.getBirDay()%>日;星座:<%=CreditAction.getAstroString(userBase.getAstro())%>;属<%=userBase.getAnimals()%><%}%><br/>
身高:<%if(userBase == null || userBase.getStature() == 0){%>未填写<%}else{%><%=userBase.getStature()%><%}%><br/>
交友目的:<%if(userBase == null || userBase.getAim() == 0){%>未填写<%}else{%><%=aim[userBase.getAim()]%><%}%><br/>
居住地:<%if(userBase == null || userBase.getProvince() == 0 || userBase.getCity() == 0){%>未填写<%}else{%><%=action.getPlace(userBase)%><%}%><br/>
学历:<%if(userBase == null || userBase.getEducation() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userBase.getEducation())%><%}%><br/>
个性类型:<%if(userBase == null || userBase.getPersonality() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userBase.getPersonality())%><%}%><br/>
血型:<%if(userBase == null || userBase.getBlood() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userBase.getBlood())%><%}%><br/>
<%	   
break;
case 1:
%>
身份:<%if(userWork == null || userWork.getIdentity() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getIdentity())%><%}%><br/>
行业:<%if(userWork == null || userWork.getTrade() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getTrade())%><%}%><br/>
收入:<%if(userWork == null || userWork.getEarning() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getEarning())%><%}%><br/>
职业梦想:<%if(userWork == null || userWork.getDream() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getDream())%><%}%><br/>
<%
break;
case 2:
%>
最长恋爱时间:<%if(userLive == null || userLive.getLoveTime() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getLoveTime())%><%}%><br/>
恋爱谁买单:<%if(userLive == null || userLive.getBill() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getBill())%><%}%><br/>
是否抽烟:<%if(userLive == null || userLive.getSmoke() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getSmoke())%><%}%><br/>
喜欢的影星:<%if(userLive == null || "".equals(userLive.getFilmStart())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getFilmStart()) %><%}%><br/>
喜欢的歌手:<%if(userLive == null || "".equals(userLive.getSinger())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getSinger())%><%}%><br/>
婚姻状况:<%if(userLive == null || userLive.getMarrage() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getMarrage())%><%}%><br/>
有无小孩:<%if(userLive == null || userLive.getChild()== 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getChild())%><%}%><br/>
喜欢的食物:<%if(userLive == null || "".equals(userLive.getFood())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getFood())%><%}%><br/>
喜欢的运动:<%if(userLive == null || "".equals(userLive.getSport())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getSport())%><%}%><br/>
最近关注:<%if(userLive == null || "".equals(userLive.getFocusOn())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getFocusOn())%><%}%><br/>
擅长:<%if(userLive == null || "".equals(userLive.getGoodAt())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getGoodAt())%><%}%><br/>  
<%
break;
case 3:
%>
体型:<%if(userLooks == null || userLooks.getBodyType() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLooks.getBodyType())%><%}%><br/>
外貌自评:<%if(userLooks == null || userLooks.getLooks() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLooks.getLooks())%><%}%><br/>
魅力部位:<%if(userLooks == null || userLooks.getCharm() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLooks.getCharm())%><%}%><br/>
<%
break;
case 4:
if (userContacts == null){
%>真实姓名:未填写<br/>
交友联系方式:未填写<br/>
身份证号码:未填写<br/>
奖品邮寄地址:未填写<br/><%	
} else {
if (userContacts.getPrivateLevel()==1){
// 只对好友公开
%>您好,您没有查看此项的权利.<br/><%
} else if (userContacts.getPrivateLevel()==3){
// 只对UID的好友公开,或你被对方拉入黑名单里
//List tempList = new ArrayList();
//tempList.add(new Integer(431));
if ((!friendList.contains(String.valueOf(action.getLoginUser().getId()))) || userService.isUserBadGuy(uid,action.getLoginUser().getId())){
%>您好,您没有查看此项的权利.<br/><%	
} else {
%>
真实姓名:<%if("".equals(userContacts.getTrueName())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getTrueName())%><%}%><br/>
交友联系方式:<%if("".equals(userContacts.getContacts())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getContacts())%><%}%><br/>
身份证号码:<%if("".equals(userContacts.getIdCard())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getIdCard())%><%}%><br/>
奖品邮寄地址:<%if("".equals(userContacts.getAddress())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getAddress())%><%}%><%
}
} else {
// 公开
%>
真实姓名:<%if("".equals(userContacts.getTrueName())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getTrueName())%><%}%><br/>
交友联系方式:<%if("".equals(userContacts.getContacts())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getContacts())%><%}%><br/>
身份证号码:<%if("".equals(userContacts.getIdCard())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getIdCard())%><%}%><br/>
奖品邮寄地址:<%if("".equals(userContacts.getAddress())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getAddress())%><%}%><br/><%
}
}
break;
}%><%if(userInfo.getModifyTime() != 0){%>资料更新日期:<%=DateUtil.formatDate1(new Date(userInfo.getModifyTime()))%><br/><%}%><a href="credit.jsp?uid=<%=uid%>">[返回可信度首页]</a><br/>
<%	
} else {
%><%=tip%><br/><a href="flist.jsp">返回</a><br/><%	
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>