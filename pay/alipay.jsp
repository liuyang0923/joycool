<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.spec.pay.*,net.joycool.wap.spec.shop.*,net.joycool.wap.framework.*"%><%
	response.setHeader("Cache-Control","no-cache");
	PayAction payAction = new PayAction(request);
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="支付宝充值系统"><p>
<%=BaseAction.getTop(request, response)%>
酷币充值说明：<br/>
您可以使用移动、联通手机充值卡，换取乐酷社区产品酷币（单位：克(g)）。每1元手机充值卡面额可等值兑换为1酷币，用于乐酷社区道具商城的消费。<br/>
=操作说明=<br/>
1.选择卡的种类和面额后，按提示在输入框内输入充值卡序列号及充值密码，点击提交；（注意!!!!金额必须选择正确）<br/>
2.充值成功需要3分钟左右的等待过程，这期间不影响您在游戏中的任何操作。至迟5分钟后，就可以在乐秀商城中查看“酷币”是否到帐，如未到帐请拨打官方客服电话13347907223查询解决。<br/>
3.请确认您使用的充值卡面额与选择的面额相同，否则将导致余额丢失和支付失败!!!<br/>
<select name="cardType" value="2">
<option value="2">全国移动充值卡</option>
<option value="3">全国联通充值卡</option>
<option value="4">全国电信充值卡</option>
</select>
<select name="money">
<option value="0">选择充值面额</option>
<option value="30">30充值卡</option>
<option value="50">50充值卡</option>
<option value="100">100充值卡</option>
<option value="200">200充值卡</option>
<option value="300">300充值卡</option>
</select><br/>
卡号:<input name="cardId" format="*N" value=""/><br/>
密码:<input name="cardPwd" format="*N" value=""/><br/>
<anchor>充值<go href="doPay.jsp" method="post">
<postfield name="money" value="$money"/>
<postfield name="cardType" value="$cardType"/>
<postfield name="cardId" value="$cardId"/>
<postfield name="cardPwd" value="$cardPwd"/>
</go></anchor><br/>
<a href="/pay/alipay.jsp">支付宝支付</a>
=特别提醒=<br/>
1.目前只支持规定面额的手机充值卡为酷币充值。如果玩家使用了其它面额的充值卡，导致卡面金额与充值数量不符，官方将不予处理；<br/>
2.此处充值使用的充值卡种类不受您的手机SIM卡种类限制（如，使用神州行号码的用户，也可使用联通充值卡成功充值）；<br/>
3.充值成功以前请保留充值卡，以便充值不成功时客服协助查询。<br/>
&lt;&lt;返回<a href="/shop/index.jsp">乐秀商城</a>|<a href="/kshow/downtown.jsp">酷秀商城</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>