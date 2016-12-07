<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.credit.*"%>
<%! static String aim[] = {"未填写","恋人","知已","玩伴","其它"}; %>
<% response.setHeader("Cache-Control","no-cache");
   CreditAction action = new CreditAction(request);
   int type = action.getParameterInt("t");
   int tmp = 0;
   String topLink = "";
   UserBase userBase = null;
   UserWork userWork = null;
   UserLive userLive = null;
   UserLooks userLooks = null;
   UserContacts userContacts = null;
   UserInfo userInfo = action.getUserInfo(action.getLoginUser().getId());
   if (type < 0 || type > 4){
	   type = 0;
   }
   switch (type){
	   case 0:
		   userBase = CreditAction.getUserBaseBean(action.getLoginUser().getId());
		   if (userBase == null){
			   userBase = new UserBase();
			   userBase.setUserId(action.getLoginUser().getId());
			   userBase.setPhoto("");
			   userBase.setAnimals("");
			   userBase.setGender(UserInfoUtil.getUser(action.getLoginUser().getId()).getGender());
			   CreditAction.service.createUserBase(userBase);
		   }
		   topLink="温馨提示:您的可信度分值越高,其他人就会对您越信任,越愿意向您敞开心扉,提升交友成功率!以下信息越完整,可信度分值就越高:<br/>基本|<a href=\"navi.jsp?t=1\">工作</a>|<a href=\"navi.jsp?t=2\">生活</a>|<a href=\"navi.jsp?t=3\">外貌</a>|<a href=\"navi.jsp?t=4\">联系</a><br/>";
		   break;
	   case 1:
		   userWork = CreditAction.service.getUserWork(" user_id=" + action.getLoginUser().getId());
		   topLink="<a href=\"navi.jsp\">基本</a>|工作|<a href=\"navi.jsp?t=2\">生活</a>|<a href=\"navi.jsp?t=3\">外貌</a>|<a href=\"navi.jsp?t=4\">联系</a><br/>";
		   break;
	   case 2:
		   userLive = CreditAction.service.getUserLive(" user_id=" + action.getLoginUser().getId());
		   topLink="<a href=\"navi.jsp\">基本</a>|<a href=\"navi.jsp?t=1\">工作</a>|生活|<a href=\"navi.jsp?t=3\">外貌</a>|<a href=\"navi.jsp?t=4\">联系</a><br/>";
		   break;
	   case 3:
		   userLooks = CreditAction.service.getUserLooks(" user_id=" + action.getLoginUser().getId());
		   topLink="<a href=\"navi.jsp\">基本</a>|<a href=\"navi.jsp?t=1\">工作</a>|<a href=\"navi.jsp?t=2\">生活</a>|外貌|<a href=\"navi.jsp?t=4\">联系</a><br/>";
		   break;
	   case 4:
		   userContacts = CreditAction.service.getUserContacts(" user_id=" + action.getLoginUser().getId());
		   topLink="<a href=\"navi.jsp\">基本</a>|<a href=\"navi.jsp?t=1\">工作</a>|<a href=\"navi.jsp?t=2\">生活</a>|<a href=\"navi.jsp?t=3\">外貌</a>|联系<br/>";
		   break;
   }   
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="交友可信度"><p>
<%=BaseAction.getTop(request, response)%>
您目前的交友可信度为<%=userInfo.getTotalPoint()%>%<br/>信息完整度(40):<%=userInfo.getIntactPoint()%><br/>
<%=topLink%> 
<%switch (type){
case 0:
if(userBase != null && userBase.getPhoto() != null && !"".equals(userBase.getPhoto())){
%><img src="<%=CreditAction.ATTACH_URL_ROOT + "/" + userBase.getPhoto()%>" alt="o"/><br/><%
if ("x.gif".equals(userBase.getPhoto())){
%>原因:请上传本人五官清晰的生活照.<br/><%
}
}%><a href="uppic.jsp">上传本人生活照(wap2.0可上传)</a><br/>
<%tmp = action.integrality(1);
if(tmp==-1){
%><a href="bir.jsp">填写一下信息</a><br/><%
}else if (tmp==1){
%><a href="bir.jsp">继续填写</a><br/><%	
}else if (tmp==2){
%><a href="select.jsp?t=1">继续填写</a><br/><%
}else if(tmp==0){
}else{
%><a href="select2.jsp?t=<%=tmp%>">继续填写</a><br/><%
}%>
生日:<%if(userBase == null || userBase.getBirYear() == 0 || userBase.getBirMonth() == 0 || userBase.getBirDay() == 0){%>未填写<%}else{%><%=userBase.getBirYear()%>年<%=userBase.getBirMonth()%>月<%=userBase.getBirDay()%>日<%=userBase.getBirHour() %>时;星座:<%=CreditAction.getAstroString(userBase.getAstro())%>;属<%=userBase.getAnimals()%>;<a href="bir.jsp?m=1">修改</a><%}%><br/>
身高:<%if(userBase == null || userBase.getStature() == 0){%>未填写<%}else{%><%=userBase.getStature()%><a href="bir.jsp?m=1">修改</a><%}%><br/>
交友目的:<%if(userBase == null || userBase.getAim() == 0){%>未填写<%}else{%><%=aim[userBase.getAim()]%><a href="bir.jsp?m=1">修改</a><%}%><br/>
居住地:<%if(userBase == null || userBase.getProvince() == 0 || userBase.getCity() == 0){%>未填写<%}else{%><%=action.getPlace(userBase)%><a href="select.jsp?t=1&amp;m=1">修改</a><%}%><br/>
学历:<%if(userBase == null || userBase.getEducation() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userBase.getEducation())%><a href="select2.jsp?t=3&amp;m=1">修改</a><%}%><br/>
个性类型:<%if(userBase == null || userBase.getPersonality() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userBase.getPersonality())%><a href="select2.jsp?t=4&amp;m=1">修改</a><%}%><br/>
血型:<%if(userBase == null || userBase.getBlood() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userBase.getBlood())%><a href="select2.jsp?t=5&amp;m=1">修改</a><%}%><br/>
<%	   
break;
case 1:
tmp = action.integrality(2);
if(tmp==-1){
%><a href="select2.jsp?t=6">填写一下信息</a><br/><%
}else if(tmp==0){
}else{
%><a href="select2.jsp?t=<%=tmp%>">继续填写</a><br/><%
}%>
身份:<%if(userWork == null || userWork.getIdentity() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getIdentity())%><a href="select2.jsp?t=6&amp;m=1">修改</a><%}%><br/>
行业:<%if(userWork == null || userWork.getTrade() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getTrade())%><a href="select2.jsp?t=6&amp;m=1">修改</a><%}%><br/>
收入:<%if(userWork == null || userWork.getEarning() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getEarning())%><a href="select2.jsp?t=7&amp;m=1">修改</a><%}%><br/>
职业梦想:<%if(userWork == null || userWork.getDream() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userWork.getDream())%><a href="select2.jsp?t=7&amp;m=1">修改</a><%}%><br/>
<%
break;
case 2:
tmp = action.integrality(3);
if(tmp==-1){
%><a href="select2.jsp?t=8">填写一下信息</a><br/><%
}else if (tmp==1||tmp==2){
%><a href="in.jsp?t=<%=tmp%>">继续填写</a><br/><%
}else if(tmp==0){
}else{
%><a href="select2.jsp?t=<%=tmp%>">继续填写</a><br/><%	
}%>
最长恋爱时间:<%if(userLive == null || userLive.getLoveTime() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getLoveTime())%><a href="select2.jsp?t=8&amp;m=1">修改</a><%}%><br/>
恋爱谁买单:<%if(userLive == null || userLive.getBill() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getBill())%><a href="select2.jsp?t=8&amp;m=1">修改</a><%}%><br/>
是否抽烟:<%if(userLive == null || userLive.getSmoke() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getSmoke())%><a href="select2.jsp?t=8&amp;m=1">修改</a><%}%><br/>
喜欢的影星:<%if(userLive == null || "".equals(userLive.getFilmStart())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getFilmStart()) %><a href="modify.jsp?t=1">修改</a><%}%><br/>
喜欢的歌手:<%if(userLive == null || "".equals(userLive.getSinger())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getSinger())%><a href="modify.jsp?t=2">修改</a><%}%><br/>
婚姻状况:<%if(userLive == null || userLive.getMarrage() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getMarrage())%><a href="select2.jsp?t=9&amp;m=1">修改</a><%}%><br/>
有无小孩:<%if(userLive == null || userLive.getChild()== 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLive.getChild())%><a href="select2.jsp?t=9&amp;m=1">修改</a><%}%><br/>
喜欢的食物:<%if(userLive == null || "".equals(userLive.getFood())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getFood())%><a href="modify.jsp?t=3">修改</a><%}%><br/>
喜欢的运动:<%if(userLive == null || "".equals(userLive.getSport())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getSport())%><a href="modify.jsp?t=4">修改</a><%}%><br/>
最近关注:<%if(userLive == null || "".equals(userLive.getFocusOn())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getFocusOn())%><a href="modify.jsp?t=5">修改</a><%}%><br/>
擅长:<%if(userLive == null || "".equals(userLive.getGoodAt())){%>未填写<%}else{%><%=StringUtil.toWml(userLive.getGoodAt())%><a href="modify.jsp?t=6">修改</a><%}%><br/>  
<%
break;
case 3:
tmp = action.integrality(4);
if(tmp!=0){%><a href="select2.jsp?t=10">填写一下信息</a><br/><%}%>
体型:<%if(userLooks == null || userLooks.getBodyType() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLooks.getBodyType())%><a href="select2.jsp?t=10&amp;m=1">修改</a><%}%><br/>
外貌自评:<%if(userLooks == null || userLooks.getLooks() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLooks.getLooks())%><a href="select2.jsp?t=10&amp;m=1">修改</a><%}%><br/>
魅力部位:<%if(userLooks == null || userLooks.getCharm() == 0){%>未填写<%}else{%><%=CreditAction.getPropertyString(userLooks.getCharm())%><a href="select2.jsp?t=10&amp;m=1">修改</a><%}%><br/>
<%
break;
case 4:
tmp = action.integrality(5);
if (userContacts == null || "".equals(userContacts.getTrueName()) || "".equals(userContacts.getContacts()) || "".equals(userContacts.getIdCard())|| "".equals(userContacts.getAddress()) || userContacts.getPrivateLevel() == 0){
%><a href="in.jsp?t=3">填写一下信息</a><br/><%
}%>
真实姓名:<%if(userContacts == null || "".equals(userContacts.getTrueName())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getTrueName())%><a href="modify.jsp?t=7">修改</a><%}%><br/>
交友联系方式:<%if(userContacts == null || "".equals(userContacts.getContacts())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getContacts())%><a href="modify.jsp?t=8">修改</a><%}%><br/>
身份证号码:<%if(userContacts == null || "".equals(userContacts.getIdCard())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getIdCard())%><a href="modify.jsp?t=9">修改</a><%}%><br/>
奖品邮寄地址:<%if(userContacts == null || "".equals(userContacts.getAddress())){%>未填写<%}else{%><%=StringUtil.toWml(userContacts.getAddress())%><a href="modify.jsp?t=10">修改</a><%}%><br/>
内容公开设置:<%if(userContacts == null || userContacts.getPrivateLevel()==0){%>未填写<%}else{%><%
switch (userContacts.getPrivateLevel()){
case 1:%>不公开<%break;
case 2:%>公开<%break;
case 3:%>只对好友公开<%break;
}%><a href="modify.jsp?t=11">修改</a><%}%><br/><%
	   break;
}%><%if(userInfo != null){%>资料更新日期:<%=DateUtil.formatDate1(new Date(userInfo.getModifyTime()))%><br/><%}%><a href="vouch.jsp">我要去交友</a><br/>
<a href="phone.jsp">手机号码认证(40)</a>:<%=userInfo.getPhonePoint()%><br/>
<a href="net.jsp">网站信用担保(10)</a>:<%=userInfo.getNetPoint()%><br/>
<a href="cons.jsp">消费累积(5)</a>:<%=userInfo.getConPoint()%><br/>
可靠玩家打分:<%=userInfo.getPlayerCount() != 0?userInfo.getPlayerPoint() / userInfo.getPlayerCount():0%><br/>
最近点评人:<%=action.getGradeUserString(action.getLoginUser().getId(),3)%><br/>
<a href="more.jsp">更多点评人</a><br/>
<a href="flist.jsp">给别人去打分</a><br/>
<a href="/user/ViewUserInfo.do?userId=<%=userInfo.getUserId()%>">[查看个人资料]</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>