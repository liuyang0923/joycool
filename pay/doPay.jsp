<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.shop.*,net.joycool.wap.spec.pay.*,test.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	
	PayAction payAction = new PayAction(request);
	
	int uid = payAction.getLoginUser().getId();
	PayService payService = new PayService();
	int money = payAction.getParameterInt("money");
	int cardType = payAction.getParameterInt("cardType");
	
	String cardId = payAction.getParameterNoEnter("cardId");
	String cardPwd = payAction.getParameterNoEnter("cardPwd");
	
	String c = request.getParameter("c");
	
	boolean flag = true;
	
	if(money != 30 && money != 50 && money != 100 && money != 200 && money != 300) {
		request.setAttribute("msg","输入金额有误");
		flag = false;
	}
	
	else if(cardId == null || cardId.length() < 10) {
		request.setAttribute("msg","卡号不能为空");
		flag = false;
	}
	
	else if(cardPwd == null) {
		request.setAttribute("msg","密码不能为空");
		flag = false;
	}
	else {
		if(cardId.length() == 17 && cardPwd.length() == 18) {
			cardType = 8;
		}
		else if(cardId.length() == 15 && cardPwd.length() == 19) {
			cardType = 9;
		}
		else if(cardId.length() == 19 && cardPwd.length() == 18) {
			cardType = 10;
		}
		/*
		else if(cardId.length() == 10 && cardPwd.length() == 8) {
			cardType = 4;
		}
		else if(cardId.length() == 16 && cardPwd.length() == 21) {
			cardType = 7;
		}
		else if(cardId.length() == 16 && cardPwd.length() == 17) {
			if(cardType != 5 && cardType != 6)
				cardType = 5;
		}*/
		
		 else {
			request.setAttribute("msg","卡号或密码不正确");
			flag = false;
		}
	}
	if(flag&&c != null && c.equals("c")){
		
		PayOrderBean orderBean = new PayOrderBean();
		orderBean.setBak("");
		orderBean.setCardId(cardId);
		orderBean.setCardPwd(cardPwd);
		orderBean.setCode("code");
		orderBean.setMoney(money);
		orderBean.setType(PayOrderBean.TYPE_UNDO);
		orderBean.setUserId(uid);
		orderBean.setChannelId(cardType);
		payService.addOrder(orderBean);
		
		String xmlString = payAction.submitPay(orderBean.getId(), orderBean.getMoney(), orderBean.getCardId(), orderBean.getCardPwd(),cardType);
		//System.out.println(xmlString);
		String result = PayAction.parseXmlNodeValue(xmlString, "result");
		String resultStr = PayAction.parseXmlNodeValue(xmlString, "resultstr");
		boolean doing = false;
		//System.out.println(result);
		//System.out.println(resultStr);
		if(result.equals("P")){
			doing = true;
		}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="充值系统" ontimer="<%=response.encodeURL("myOrder.jsp")%>">
<timer value="50"/><p>
<%=BaseAction.getTop(request, response)%>
<%if(doing) {
	SqlUtil.executeUpdate("update pay_order set type = " + PayOrderBean.TYPE_DOING + ", result = '" + StringUtil.toSql(result) + "', resultstr = '" + StringUtil.toSql(resultStr) + "' where id = " + orderBean.getId() + " and type!=2",5);
//	payService.updateOrderType(orderBean.getId(), PayOrderBean.TYPE_DOING, result, resultStr);
%>正在充值，请稍候...<br/>
<%} else {
	String failure = "";
	if(result.equals("F")) {
		if(resultStr.equals("10016")) {
			failure = "订单已支付不能重复支付";
		} else if(resultStr.equals("10082")) {
			failure = "该卡号已经成功处理,已经被锁卡";
		} else if(resultStr.equals("81007")) {
			failure = "充值卡卡号密码错误";
		} else {
			failure = "充值失败,卡号或密码错误";
		}
	}
%>
<%=failure%><br/>
<%}%>
5秒钟之后自动<a href="myOrder.jsp">返回充值记录</a><br/>
<a href="/shop/index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>
<%} else if(flag) {
	PayBean payBean = PayAction.service.getPayBeanById(cardType);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="充值系统"><p>
充值确认<br/>

充值人:<%=payAction.getLoginUser().getNickNameWml() %>(<%=payAction.getLoginUser().getId() %>)<br/>
类型:<%=payBean.getName() %><br/>
金额:<%=money %>元<br/>
卡号:<%=cardId %><br/>
密码:<%=cardPwd %><br/>
请确认以上信息正确无误<br/>
<anchor>确认并提交充值<go href="doPay.jsp?c=c" method="post"><postfield name="cardType" value="<%=cardType %>"/><postfield name="cardId" value="<%=cardId %>"/><postfield name="cardPwd" value="<%=cardPwd %>"/><postfield name="money" value="<%=money %>"/></go></anchor><br/><a href="pay.jsp">&lt;&lt;返回重填</a><br/>
<a href="/shop/index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>
<%} else {%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="充值系统" ontimer="<%=response.encodeURL("pay.jsp")%>">
<timer value="50"/><p>
<%=request.getAttribute("msg") == null?"":request.getAttribute("msg")+"<br/>" %>
5秒钟之后自动<a href="pay.jsp">返回重填</a><br/>
&lt;&lt;返回<a href="/shop/index.jsp">乐秀商城</a>|<a href="/kshow/downtown.jsp">酷秀商城</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>
<%}%>