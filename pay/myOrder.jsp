<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.pay.*,net.joycool.wap.framework.*,java.util.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");

	PayAction payAction = new PayAction(request);
	int uid = payAction.getLoginUser().getId();
	int cur = payAction.getParameterIntS("p");
	if(cur<0) cur=0;
	int start = cur * 10;
	List list = PayAction.service.getOrders(" user_id = " + uid + " order by id desc limit " + start + ", 11");
	
	int size = list.size() >= 10?10:list.size();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="历史记录"><p>
<%=BaseAction.getTop(request, response)%>
<%@include file="/shop/head.jsp"%>
金额-状态-结果-时间<br/>
<%for(int i = 0;i<size;i++) {
	PayOrderBean orderBean = (PayOrderBean)list.get(i);
%>
<%=orderBean.getMoney()%>元|<%if(orderBean.getType() == PayOrderBean.TYPE_DOING) {%><%=orderBean.getTypeStr()%><%} else {%><%=orderBean.getTypeStr()%><%} %>|<%=orderBean.getResult2Str()%>|<%=DateUtil.formatDate2(orderBean.getCreateTime())%><br/>
<%} %>
<%if(list.size() > 10) {%><a href="myOrder.jsp?p=<%=cur+1%>">下一页</a><%}else{%>下一页<%}%>
<%if(cur > 0) {%><a href="myOrder.jsp?p=<%=cur-1%>">上一页</a><%}else{%>上一页<%}%><br/>
&lt;&lt;返回<a href="/shop/index.jsp">乐秀商城</a>|<a href="/kshow/downtown.jsp">酷秀商城</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>