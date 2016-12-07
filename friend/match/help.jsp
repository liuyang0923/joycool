<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.UserBean,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*"%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
String tip = "";
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="奖品兑换规则"><p>
<%=BaseAction.getTop(request, response)%>
<%if("".equals(tip)){
%>奖品兑换规则<br/>
1.选票(靓点)越高的,排名越高,赛事结束时前10名乐后将获得大奖.<br/>
2.所有参赛选手,都可以用靓点,在<a href="goods.jsp">礼品兑换区</a>换取中意的实物产品.靓点一经兑换,不可再用,兑完为止.剩余的靓点保留,不计入下次比赛选票成绩,但可以与下次比赛累积的靓点一起,来兑换价值更高的礼品.<br/>
3.本次乐后选拔赛所有奖品均由买卖宝公司(mmb.cn)赞助.<br/>
4.本活动最终解释权归乐酷所有.<br/>
<%
} else {
%><%=tip%><br/><%
}
%><a href="goods.jsp">返回上一页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>