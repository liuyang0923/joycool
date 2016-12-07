<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.UserBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<%! static int COUNT_PRE_PAGE = 5;%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
MatchUser matchUser = MatchAction.getMatchUser(loginUser.getId());
int orderId = 0;
String tip = "";
MatchOrder matchOrder = null;
MatchExch matchExch = null;
PagingBean paging = null;
List exchList = null;
int type = action.getParameterInt("t");
if (type < 0 || type > 1){
	type = 0;
}
if (type == 0){
	if (matchUser != null){
		paging = new PagingBean(action,matchUser.getConsCount(),COUNT_PRE_PAGE,"p");
		exchList = MatchAction.service.getExchList(" user_id=" + matchUser.getUserId() + " order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);	
	}
} else if (type == 1){
	orderId = action.getParameterInt("oid");
	if (orderId <= 0){
		tip = "该订单不存在.";
	} else {
		matchOrder = MatchAction.service.getOrder(" id=" + orderId);
		if (matchOrder == null){
			tip = "该订单不存在.";
		} else if (matchOrder.getUserId() != loginUser.getId()){
			tip = "只能查看您自己的订单.";
		}
	}
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="兑换记录"><p>
<%=BaseAction.getTop(request, response)%>
<%if("".equals(tip)){
if (type == 0){
//靓点列表
    if (exchList != null && exchList.size() > 0){
    	%>我的靓点:<br/>您目前的可使用靓点是<%=matchUser.getCurrentVote2()%>!!<br/>靓点购买商品记录<br/>---------------<br/><%
		for(int i = 0 ; i < exchList.size() ; i++){
			matchExch = (MatchExch)exchList.get(i);
			if (matchExch != null){
				%><%=i+paging.getStartIndex()+1%>.<a href="elog.jsp?t=1&amp;oid=<%=matchExch.getOrderId()%>"><%=matchExch.getGoodNameWml()%>:<%=DateUtil.sformatTime(matchExch.getBuyTime())%></a><br/><%
			}
		}%><%=paging!=null?paging.shuzifenye("elog.jsp",false,"|",response):""%><%
	} else {
		%>您还没有兑换任何商品,快到商品兑换商城兑换自己喜爱的商品吧!!<br/><%
	}
} else if (type == 1){
//查看订单
%>您已兑换的商品:<%=matchOrder.getGoodNameWml()%><br/>
购买靓点:<%=matchOrder.getActualPrice()%><br/>
您的联系电话:<%=matchOrder.getPhone()%><br/>
收货人姓名:<%=matchOrder.getUserNameWml()%><br/>
收货地址:<%=matchOrder.getAddressWml()%><br/>
**每周周三统一发货,咨询电话400-779-0940<br/>
<a href="elog.jsp">返回订单记录首页</a><br/>
<%
}
} else {
%><%=tip%><br/><%
}
%><a href="goods.jsp">返回商品兑换商城</a><br/><a href="index.jsp">乐后选拔赛首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>