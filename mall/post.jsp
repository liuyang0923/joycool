<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.mall.*,net.wxsj.action.mall.*,java.util.*,net.wxsj.util.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");

MallAction action = new MallAction();
action.post(request, response);

String result = (String) request.getAttribute("result");
//进入页面
if(result == null){
%>
<wml>
<card title="发表帖子">
<p align="left">
第2-1步 填写物品信息<br/>
你要：<select name="infoType"><option value="1">卖出</option><option value="0">买入</option></select><br/>
物品名称：<br/>
<input type="text" name="name" maxlength="20"/><br/>
(如nokia手机,神州行sim卡)<br/>
期望价格：<br/>
<input type="text" name="price" maxlength="50"/><br/>
(可以给具体数字,如3000;也可以给范围,如不低于1000)<br/>
物品描述：<br/>
<input type="text" name="intro" maxlength="500"/><br/>
(建议描述包括品牌型号,新旧程度,功能,交易方式等)<br/>
<anchor>下一步
  <go href="post.jsp?zzz=1" method="post">    
	<postfield name="infoType" value="$infoType"/>
	<postfield name="name" value="$name"/>
	<postfield name="price" value="$price"/>
	<postfield name="intro" value="$intro"/>
	<postfield name="act" value="step1"/>
  </go>
</anchor><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>
<%
	return;
}
//第二步
else if("toStep2".equals(result)){
%>
<wml>
<card title="发表帖子">
<p align="left">
第2-2步 填写交易信息<br/>
交易沟通方式(多选项)：<br/>
<select name="buyMode" multiple="true"><option value="站内交流">站内交流</option><option value="电话短信沟通">电话短信沟通</option><option value="当面沟通">当面沟通</option></select><br/>
你的联系方式(可不填)：<br/>
<input type="text" name="telephone" maxlength="20"/><br/>
(建议填手机号，例如13512345678。乐酷将帮您进行手机号码真实性验证，增加你的交易成功率, <a href="validate.jsp">认证说明</a>)<br/>
你所在地区：(可不填)<br/>
<input type="text" name="address" maxlength="100"/><br/>
<anchor>确认发表
  <go href="post.jsp?zzz=1" method="post">
	<postfield name="buyMode" value="$buyMode"/>
	<postfield name="telephone" value="$telephone"/>
	<postfield name="address" value="$address"/>
	<postfield name="act" value="step2"/>
  </go>
</anchor><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>
<%
	return;
}
//失败
else if("failure".equals(result)){
	String tip = (String) request.getAttribute("tip");
%>
<wml>
<card title="发表帖子">
<timer value="30"/>
<p align="left">
发表帖子<br/>
--------------<br/>
<%=StringUtil.toWml(tip)%><br/>
<anchor>马上返回
<prev/>
</anchor><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>
<%
	return;
}
//成功
else if("success".equals(result)){
	String validated = (String) request.getAttribute("validated");
	String infoId = (String) request.getAttribute("infoId");
%>
<wml>
<card title="发表帖子">
<p align="left">
发表帖子<br/>
--------------<br/>
发表成功<br/>
<%
	if(validated != null){
%>
交易信息已发布，安心等别人跟你联系吧。<br/>
你填写的电话号码自动通过真实性认证。<br/>
<%
    }
    else {
%>
交易信息已发布，安心等别人跟你联系吧。<br/>
建议您对刚才填写的手机号进行认证。<br/>
认证方法：用您刚才填写的号码发送短信<%=infoId%>到13718998855，系统将在贴子里帮你标注手机号为已认证<br/>
<%
	}
%>
<a href="validate.jsp">什么是“认证”</a><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>
<%
	return;
}
//成功
else if("timeout".equals(result)){
%>
<wml>
<card title="发表帖子" ontimer="<%=response.encodeURL("post.jsp")%>">
<timer value="30"/>
<p align="left">
发表帖子<br/>
--------------<br/>
请勿重复发帖！<br/>
<a href="post.jsp">马上返回</a><br/>
<a href="index.jsp">返回卖场首页</a><br/>
</p>
</card>
</wml>
<%
	return;
}
%>