<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.shop.*,net.joycool.wap.util.*,net.joycool.wap.spec.pay.*,java.util.*,net.joycool.wap.action.*,net.joycool.wap.bean.*"%><%
//System.out.println("notify>>");
		//PayAction payAction = new PayAction(request);
		//String postData = "version_id=2.00&merchant_id=7608&verifystring=4fe7bb7e072be0d640d326773e433df4&order_date=20090417&order_id=21&result=Y&amount=50.00&currency=RMB&pay_sq=GW090417142106904062&pay_date=20090417142107&pm_id=CMJFK&pc_id=CMJFK00010001&pay_cardno=07323019722885804&pay_cardpwd=&user_name=&user_phone=&user_mobile=&user_email=&order_pname=&order_pdesc=";
		//if(request.getMethod().equalsIgnoreCase("post")) {
			//byte[] buf = new byte[request.getContentLength()];
			try {
				//request.getInputStream().read(buf);		// 必须先执行，不能在getparameter之后，否则为空
				//postData = new String(buf);
				//System.out.println(postData);
				int orderId = StringUtil.toInt(request.getParameter("order_id"));
//				System.out.print(DateUtil.formatTime(new Date()) + ":");
//				System.out.println(orderId + "-" + result);
			synchronized(PayAction.class){
				//int orderIds = StringUtil.toInt(orderId);
				PayOrderBean orderBean = PayAction.service.getOrder(" id = " + orderId);
				if(orderBean==null){
					%>N<%
					return;
				}
				//service.getPayBeanById(id)
				PayBean payBean = PayAction.service.getPayBeanById(orderBean.getChannelId());
				
				StringBuilder sb = new StringBuilder(256);
				sb.append("version_id=");
				sb.append(request.getParameter("version_id"));
				sb.append("&merchant_id=");
				sb.append(request.getParameter("merchant_id"));
				

				sb.append("&order_date=");
				sb.append(request.getParameter("order_date"));
				sb.append("&order_id=");
				sb.append(request.getParameter("order_id"));

				sb.append("&currency=");
				sb.append(request.getParameter("currency"));
				sb.append("&pay_sq=");
				sb.append(request.getParameter("pay_sq"));
				sb.append("&pay_date=");
				sb.append(request.getParameter("pay_date"));				
				
				sb.append("&card_num=");
				sb.append(request.getParameter("card_num"));
				sb.append("&card_pwd=");
				sb.append(request.getParameter("card_pwd"));

				sb.append("&pc_id=");
				sb.append(request.getParameter("pc_id"));
				
				sb.append("&card_status=");
				sb.append(request.getParameter("card_status"));
				sb.append("&card_code=");
				sb.append(request.getParameter("card_code"));
				sb.append("&card_amount=");
				sb.append(request.getParameter("card_amount"));
				


				sb.append("&merchant_key=");
				sb.append(payBean.getMerchantKey());
				
				String dstr = sb.toString();
//				System.out.println("notify ="+request.getQueryString());
//				System.out.println("notify dstr="+dstr);
				dstr = PayAction.getMD5(dstr).toLowerCase();
//				System.out.println("verifyString1="+dstr);
				String verifyString = request.getParameter("verifystring");
//				System.out.println("verifyString2="+verifyString);

				String card_status=request.getParameter("card_status");

//				System.out.println("card_status="+request.getParameter("card_status"));				
//				System.out.println("card_code="+request.getParameter("card_code"));
				System.out.println("card_amount="+request.getParameter("card_amount")+",id="+orderId);
				
				boolean verify = dstr.equals(verifyString);
				if(verify) {
					if(card_status.equals("0")){
						
						//System.out.println(orderBean.getType());
						//System.out.println(PayOrderBean.TYPE_DONE);
						if(!orderBean.getResult().equals("Y") ) {
							
							float money = StringUtil.toFloat(request.getParameter("card_amount"));
							if(orderBean.getMoney()!=(int)money) {
								SqlUtil.executeUpdate("update pay_order set money = " + money + " where id = " + orderId,5);
							}
							ShopUtil.processCharge(orderId, orderBean.getUserId(), money, "Y");

						}
					}else{
						PayAction.service.updateOrderResult2(orderId, PayOrderBean.TYPE_DONE, PayOrderBean.FALSE);
					}
					%>Y<%
				}else{
					%>N<%
				}
			}
			} catch (Exception e) {
				%>N<%
				e.printStackTrace();
			}
		//}

%>