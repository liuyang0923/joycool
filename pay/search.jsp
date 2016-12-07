<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.spec.shop.*,net.joycool.wap.spec.pay.*,test.*,net.joycool.wap.framework.*"%><%
	response.setHeader("Cache-Control","no-cache");

	PayAction payAction = new PayAction(request);
	int id = payAction.getParameterInt("id");
	PayOrderBean order = null;
	synchronized(PayAction.class){
		if(id != 0) {
		
			int uid = payAction.getLoginUser().getId();
			
			order = PayAction.service.getOrder(" id = " + id);
			
			if(order != null&&uid == order.getUserId()) {

				if(order.getType() != PayOrderBean.TYPE_DONE) {
					String xmlString = payAction.searchOrder(order);
					if(xmlString.startsWith("<?xml")) {
						String result = payAction.parseXmlNodeAttribute(xmlString, "head", 2, "value").toUpperCase();
						if(result.equals("Y")) {
							//支付成功 处理 <item name="amount" value="50"/>
							if(order.getType() != PayOrderBean.TYPE_DONE ) {
								String name = payAction.parseXmlNodeAttribute(xmlString, "item", 1, "name").toLowerCase();
								if(name.equals("amount")) {
									String amountStr = payAction.parseXmlNodeAttribute(xmlString, "item", 1, "value");
									float amount = StringUtil.toFloat(amountStr);
									ShopUtil.processCharge(id, order.getUserId(), amount, result);
									
									//ShopUtil.charge(order.getUserId(), amount);
									//NoticeAction.sendNotice(order.getUserId(), "充值成功获酷币"+amount+"g", NoticeBean.GENERAL_NOTICE, "/shop/info.jsp" );
									//PayAction.service.updateOrderResult2(id, PayOrderBean.TYPE_DONE, result);
								}
							}
						} else if(result.equals("F")) {
							PayAction.service.updateOrderResult2(id, PayOrderBean.TYPE_DONE, PayOrderBean.FALSE);
							order.setType(PayOrderBean.TYPE_DONE);
							order.setResult2(PayOrderBean.FALSE);
							//支付失败
						} else if(result.equals("P")) {
							//正在处理中 不处理
						}
					} else {
						System.out.println("pay order (id=" + order.getId() + ") search error!");
					}
				}
			} else {
				order = null;
			}
		}
	}
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="充值结果"><p>
<%=BaseAction.getTop(request, response)%>
<%if(order == null) {%>
没有该订单或您没有权限查看该订单<br/>
<%} else {%>
订单号:<%=order.getId() %><br/>
充值结果:<%=order.getResult2Str() %><br/>
充值状态:<%=order.getTypeStr() %><br/>
充值金额:<%=order.getMoney() %>元<br/>
充值时间:<%=DateUtil.formatDate(new Date(order.getCreateTime()),"yy-MM-dd") %><br/>

<%} %>
<a href="myOrder.jsp">返回我的充值记录</a>
<br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>