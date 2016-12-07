<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="高级交友搜索">
<p align="left">
<%=BaseAction.getTop(request, response)%>
性别:<select name="gender" value="2">
    <option value="0">女</option>
    <option value="1">男</option>
    <option value="2">不限</option>
	</select><br/>
年龄:<select name="age" value="0">
    <option value="1">0-15</option>
    <option value="2">16-17</option>
    <option value="3">18-19</option>
    <option value="4">20-24</option>
    <option value="5">25-29</option>
    <option value="6">30-39</option>
    <option value="7">40-49</option>
    <option value="8">50以上</option>
    <option value="0">不限</option>
	</select><br/>
所在城市:<input name="city" maxlength="10"/><br/>
星座:<select name="constellation" value="12">
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
    <option value="12">不限</option>
	</select><br/>
身高:<select name="height" value="0">
    <option value="1">150以下</option>
    <option value="2">150-160</option>
    <option value="3">160-170</option>
    <option value="4">170-180</option>
    <option value="5">180-190</option>
    <option value="6">190-200</option>
    <option value="7">200以上</option>
    <option value="0">不限</option>
	</select>厘米<br/>
体重:<select name="weight" value="0">
    <option value="1">40以下</option>
    <option value="2">40-50</option>
    <option value="3">45-50</option>
    <option value="4">50-55</option>
    <option value="5">55-60</option>
    <option value="6">60-65</option>
    <option value="7">65-70</option>
    <option value="8">70-80</option>
    <option value="9">80以上</option>
    <option value="0">不限</option>
	</select>KG<br/>
性格:<select name="personality" value="6">
    <option value="0">温柔体贴</option>
    <option value="1">活泼开朗</option>
    <option value="2">古灵精怪</option>
    <option value="3">憨厚老实</option>
    <option value="4">豪情奔放</option>
    <option value="5">天真淳朴</option>
    <option value="6">不限</option>
	</select><br/>
婚姻状况:<select name="marriage" value="4">
    <option value="0">未婚</option>
    <option value="1">已婚</option>
    <option value="2">离异</option>
    <option value="3">丧偶</option>
    <option value="4">不限</option>
	</select><br/>
交友目的:<select name="aim" value="5">
    <option value="0">恋人</option>
    <option value="1">知己</option>
    <option value="2">玩伴</option>
    <option value="3">解闷</option>
    <option value="4">其他</option>
    <option value="5">不限</option>
	</select><br/>
有无照片:<select name="attach" value="2">
    <option value="1">有</option>
    <option value="0">无</option>
    <option value="2">不限</option>
	</select><br/>	
<anchor title="确定">开始搜索
	<go href="advSearchResult.jsp" method="post">
	<postfield name="gender" value="$gender"/>
	<postfield name="age" value="$age"/>
	<postfield name="city" value="$city"/>
	<postfield name="constellation" value="$constellation"/>
	<postfield name="height" value="$height"/>
	<postfield name="weight" value="$weight"/>
	<postfield name="personality" value="$personality"/>
	<postfield name="marriage" value="$marriage"/>
	<postfield name="aim" value="$aim"/>
	<postfield name="attach" value="$attach"/>
	</go>
</anchor><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>