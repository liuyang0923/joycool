<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
action.inputRegisterInfo(request);
String result=(String) request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean user=(UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("autoCommit".equals(result)){
String url = ("/friend/editPhotoTemp.jsp");
%>
<card title="注册交友信息" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/> 
<p align="left">
<%=BaseAction.getTop(request, response)%>
您已经开通了个人家园,系统帮你自动开通交友中心,记得增加个性照片哟!(3秒后自动跳转交友中心个人相片页面)<br/>
<a href="/friend/editPhotoTemp.jsp">选择个人图片</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
%>
<card title="注册交友信息">
<p align="left">
<%=BaseAction.getTop(request, response)%>
为了让朋友们更好的认识您，跟您交流，希望您能留下详细的交友信息，最好是上传一张漂亮的形象照片哦:)<br/>
真实姓名：<input name="name"  maxlength="10" value="v"/>(必填)<br/>
<%if(user.getMark()==0){%>
性别：<%=loginUser.getGender()==1?'男':'女'%><br/>
<%}%>
年龄：<input name="age"  maxlength="3" value=""/>(1-200)岁<br/>
<%--//liuyi 2007-01-22 生日信息 start --%>
生日：<input name="year"  maxlength="4" value="1986"/>年 
<input name="month"  maxlength="2" value="1"/>月
<input name="day"  maxlength="2" value="1"/>日（必填，每月只能修改一次）<br/>
<%--select name="open" value="0">
    <option value="0">仅对好友公开</option>
    <option value="1">对所有人公开</option>
    <option value="2">不公开</option>
</select><br/--%>
<%--//liuyi 2007-01-22 生日信息 end --%>
手机号码：<input name="mobile"  maxlength="11" value="13"/>(必11位)<br/>
所在城市：<input name="city"  maxlength="10" value="v"/>(必填)<br/>
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
身高：<input name="height"  maxlength="3" value="1"/>(100-299)厘米<br/>
体重：<input name="weight"  maxlength="3" value="1"/>(30-200)KG<br/>
职业：<input name="work"  maxlength="20" value="v"/>(必填)<br/>
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
择友条件：<br/><input name="friendCondition"  maxlength="100" value="v"/>(必填)<br/>
    <anchor title="确定">完成提交
    <go href="registerFriend.jsp" method="post">
    <postfield name="homeName" value="$homeName"/>
	<postfield name="name" value="$name"/>
	<postfield name="gender" value="<%=loginUser.getGender()%>"/>
	<postfield name="age" value="$age"/>
	<postfield name="mobile" value="$mobile"/>
	<postfield name="city" value="$city"/>
	<%--//liuyi 2007-01-22 生日信息 start --%>
	<postfield name="year" value="$year"/>
	<postfield name="month" value="$month"/>
	<postfield name="day" value="$day"/>
	<postfield name="open" value="$open"/>
	<%--//liuyi 2007-01-22 生日信息 end --%>
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