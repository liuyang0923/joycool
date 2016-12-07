<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.user.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.bean.friend.FriendRingBean" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<logic:present name="result" scope="request">

    <logic:equal name="result" value="failure">
<card title="个人资料">
<p align="left">
<%=BaseAction.getTop(request, response)%>
    <bean:write name="tip" filter="false"/><br/>
	<anchor title="back"><prev/>返回个人资料页面</anchor><br/>
<%
String backTo = (String) request.getAttribute("backTo");
backTo = backTo.replace("&", "&amp;");
%>
    <a href="<%=backTo%>">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>

    <logic:equal name="result" value="success">
<%
String backTo = (String) request.getAttribute("backTo");
backTo="/user/userInfo.jsp";
//zhul 2006-06-29 判断如果这个成功页面是从交友中心的修改页跳过来的，则该页自动跳回到交友中心　start
String tag=(String)request.getAttribute("tag");
if(tag!=null) backTo="/friendadver/friendAdverIndex.jsp";
//zhul 2006-06-29 判断如果这个成功页面是从交友中心的修改页跳过来的，则该页自动跳回到交友中心　end
backTo = backTo.replace("&", "&amp;");
%>
<card title="个人设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
个人资料<br/>
------------------<br/>
修改成功！<br/>
<%if(request.getParameter("password")!=null){%>修改密码后旧书签自动失效,请重新<%}%>
<a href="/enter/index.jsp">保存登陆书签</a><br/>
<a href="<%=backTo%>">返回</a>
<%if(loginUser.getHome()==1){%>
<a href="/home/editHome.jsp">修改个人家园</a><br/> 
<%}else{%>
<a href="/home/inputRegisterInfo.jsp">开通个人家园</a><br/> 
<%}%>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
</logic:present>

<logic:notPresent name="result" scope="request">
<card title="个人设置">
<onevent type="onenterforward">
<refresh>
<setvar name="nickname" value="<%=loginUser.getNickNameWml()%>"/>
</refresh>
</onevent>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
String backTo = request.getParameter("backTo");
if(backTo == null){
	backTo = "/wapIndex.jsp";
}
String oldBackTo = backTo;
backTo = URLEncoder.encode(backTo, "UTF-8");
%>
<%--<a href="<%=(oldBackTo.replace("&", "&amp;"))%>" title="进入">返回</a><br/>--%>
登陆ID:<a href="ViewUserInfo.do?userId=<%=loginUser.getId()%>"><%=loginUser.getId()%></a><br/>
昵称:<%=loginUser.getNickNameWml()%><br/>
设置<a href="noticeMark.jsp">免打扰</a>|<a href="../beacon/mo/mood.jsp">心情</a>|<a href="status.jsp">状态</a><br/>
┗<a href="forumSet.jsp">论坛</a>|<a href="shortcut.jsp">快捷通道</a>|<a href="picMark.jsp">图片</a><br/>
┗<a href="setpwd.jsp">密码</a>|<a href="/bank/bankPWHelp.jsp">银行密码</a>|<a href="otherSet.jsp">其他</a><br/>
┗<a href="/enter/index.jsp">书签</a>|<a href="/friend/editFriend.jsp">交友信息</a>|<%if(loginUser.getHome()==0){
%><a href="/home/inputRegisterInfo.jsp">家园</a><%
}else if (loginUser.getHome()==1){
%><a href="/home/house.jsp">家园</a><%
}%><br/>
┗<a href="idle.jsp">进入脱机状态</a><br/>
<br/>
<%--macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_start
性别：<select name="gender" value="<%=loginUser.getGender()%>">
    <option value="1">男</option>
    <option value="0">女</option>
	</select><br/>
年龄：<input name="nianling" maxlength="3" value="<%=loginUser.getAge()%>"/><br/>
macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_end--%>
昵称:<input name="nickname"  maxlength="15" value="<%=StringUtil.toWml(loginUser.getNickName())%>"/>
<anchor title="确定">确认修改<go href="UserInfo.do" method="post">
	<postfield name="nickname" value="$nickname"/>
</go>
</anchor><br/>
自我介绍:<input name="selfIntroduction"  maxlength="100" value=""/>
<anchor title="确定">确认修改<go href="UserInfo.do" method="post">
    <postfield name="selfIntroduction" value="$selfIntroduction"/>
</go>
</anchor><br/>
<%if(loginUser.getHome()==0 && us.getRank()>=3){
%><a href="/home/inputRegisterInfo.jsp">(隆重)开通我的家园,让朋友们来做客!</a><br/><%
}%>
<a href="<%=(oldBackTo.replace("&", "&amp;"))%>">返回</a><br/>
<%=RankAction.getRandStatus(loginUser)%><br/>
<a href="/enter/index.jsp">存书签,以后直接登陆乐酷</a><br/>
<%--<a href="<%= ("sendUserInfo.jsp") %>">将ID和密码短信发送手机上</a><br/>--%>
<%--<%=RankAction.getRandStatus(loginUser)%><br/>
<a href="<%=(oldBackTo.replace("&", "&amp;"))%>">返回</a>--%>
<br/>
如果丢失账号或密码,请拨打13347907223让管理员帮助找回.<br/>

<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:notPresent>
</wml>