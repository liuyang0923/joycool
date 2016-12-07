<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.friend.FriendBean" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.inputRegisterInfo(request);
String result=(String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("failure".equals(result)){
String url = ("/user/userInfo.jsp");
%>
<card title="注册个人家园" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/user/userInfo.jsp">修改基本资料</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("friend".equals(result)){
FriendBean friend = (FriendBean)request.getAttribute("friend");
%>
<card title="注册个人家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
开通个人家园,是为了让朋友们更好的认识你,跟你交流,最好您能留下更详细的交友信息哦:)<br/>
真实姓名：<input name="name"  maxlength="10" value="<%=StringUtil.toWml(friend.getName())%>"/>(必填)<br/>
手机号码：<input name="mobile"  maxlength="11" value="<%=StringUtil.toWml(friend.getMobile())%>"/>(必11位)<br/>
所在城市：<input name="city"  maxlength="10" value="<%=StringUtil.toWml(friend.getCity())%>"/>(必填)<br/>
星座：<select name="constellation" value="<%=friend.getConstellation()%>">
    <option value="0">白羊座</option>
    <option value="1">金牛座</option>
    <option value="2">双子座</option>
    <option value="3">巨蟹座</option>
    <option value="4">狮子座</option>
    <option value="5">处女座</option>
    <option value="6">天秤座</option>
    <option value="7">天蝎座</option>
    <option value="8">人马座</option>
    <option value="9">摩羯座</option>
    <option value="10">宝瓶座</option>
    <option value="11">双鱼座</option>
	</select><br/>
身高：<input name="height"  maxlength="3" value="<%=friend.getHeight()%>"/>(100－229)厘米<br/>
体重：<input name="weight"  maxlength="3" value="<%=friend.getWeight()%>"/>(30－200)KG<br/>
职业：<input name="work"  maxlength="20" value="<%=StringUtil.toWml(friend.getWork())%>"/>(必填)<br/>
性格：<select name="personality" value="<%=friend.getPersonality()%>">
    <option value="0">温柔体贴</option>
    <option value="1">活泼开朗</option>
    <option value="2">古灵精怪</option>
    <option value="3">憨厚老实</option>
    <option value="4">豪情奔放</option>
    <option value="5">天真淳朴</option>
	</select><br/>
婚姻状况：<select name="marriage" value="<%=friend.getMarriage()%>">
    <option value="0">未婚</option>
    <option value="1">已婚</option>
    <option value="2">离异</option>
    <option value="3">丧偶</option>
	</select><br/>
交友目的：<select name="aim" value="<%=friend.getAim()%>">
    <option value="0">恋人</option>
    <option value="1">知己</option>
    <option value="2">玩伴</option>
    <option value="3">解闷</option>
    <option value="4">其他</option>
	</select><br/>
择友条件：<br/><input name="friendCondition"  maxlength="100" value="<%=StringUtil.toWml(friend.getFriendCondition())%>"/>(必填)<br/>
    <anchor title="确定">完成提交
    <go href="registerHome.jsp" method="post">
    <postfield name="homeName" value="$homeName"/>
	<postfield name="name" value="$name"/>
	<postfield name="mobile" value="$mobile"/>
	<postfield name="city" value="$city"/>
	<postfield name="constellation" value="$constellation"/>
	<postfield name="height" value="$height"/>
	<postfield name="weight" value="$weight"/>
	<postfield name="work" value="$work"/>
	<postfield name="personality" value="$personality"/>
	<postfield name="marriage" value="$marriage"/>
	<postfield name="aim" value="$aim"/>
	<postfield name="friendCondition" value="$friendCondition"/>
    </go>
    </anchor><br/>
<a href="/user/userInfo.jsp">修改基本资料</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%} else if("success".equals(result)){
%>
<card title="注册个人家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
开通个人家园,是为了让朋友们更好的认识你,跟你交流,最好您能留下更详细的交友信息哦:)<br/>
真实姓名：<input name="name"  maxlength="10" value=""/><br/>
手机号码：<input name="mobile"  maxlength="11" value="13"/><br/>
所在城市：<input name="city"  maxlength="10" value=""/><br/>
星座：<select name="constellation" value="4">
    <option value="0">白羊座</option>
    <option value="1">金牛座</option>
    <option value="2">双子座</option>
    <option value="3">巨蟹座</option>
    <option value="4">狮子座</option>
    <option value="5">处女座</option>
    <option value="6">天秤座</option>
    <option value="7">天蝎座</option>
    <option value="8">人马座</option>
    <option value="9">摩羯座</option>
    <option value="10">宝瓶座</option>
    <option value="11">双鱼座</option>
	</select><br/>
身高：<input name="height"  maxlength="3" value="1"/>厘米<br/>
体重：<input name="weight"  maxlength="3" value="1"/>KG<br/>
职业：<input name="work"  maxlength="20" value=""/><br/>
性格：<select name="personality" value="0">
    <option value="0">温柔体贴</option>
    <option value="1">活泼开朗</option>
    <option value="2">古灵精怪</option>
    <option value="3">憨厚老实</option>
    <option value="4">豪情奔放</option>
    <option value="5">天真淳朴</option>
	</select><br/>
婚姻状况：<select name="marriage" value="0">
    <option value="0">未婚</option>
    <option value="1">已婚</option>
    <option value="2">离异</option>
    <option value="3">丧偶</option>
	</select><br/>
交友目的：<select name="aim" value="0">
    <option value="0">恋人</option>
    <option value="1">知己</option>
    <option value="2">玩伴</option>
    <option value="3">解闷</option>
    <option value="4">其他</option>
	</select><br/>
择友条件：<br/><input name="friendCondition"  maxlength="100" value=""/><br/>
    <anchor title="确定">完成提交
    <go href="registerHome.jsp" method="post">
    <postfield name="homeName" value="$homeName"/>
	<postfield name="name" value="$name"/>
	<postfield name="mobile" value="$mobile"/>
	<postfield name="city" value="$city"/>
	<postfield name="constellation" value="$constellation"/>
	<postfield name="height" value="$height"/>
	<postfield name="weight" value="$weight"/>
	<postfield name="work" value="$work"/>
	<postfield name="personality" value="$personality"/>
	<postfield name="marriage" value="$marriage"/>
	<postfield name="aim" value="$aim"/>
	<postfield name="friendCondition" value="$friendCondition"/>
    </go>
    </anchor><br/>
<a href="/user/userInfo.jsp">修改基本资料</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>