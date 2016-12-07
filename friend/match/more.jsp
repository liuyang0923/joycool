<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,jc.match.*,jc.credit.*,net.joycool.wap.bean.UserBean"%>
<% response.setHeader("Cache-Control","no-cache");
MatchAction action = new MatchAction(request);
String tip = "";
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="详细说明"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){%>
详细规则说明<br/>
超级乐后评选,是乐酷社区推出的重要赛事,旨在邀请更多女性用户来乐酷,促进社区的繁荣！所有参赛选手,均可在活动结束后用选票(靓点)兑换实物礼品！排名前10的美女还有额外大奖赠送！<br/>
一.报名条件<br/>
1.必须是在社区注册过的女性.<br/>
2.交友可信度达到60%以上(<a href="/friend/credit/credit.jsp">完善交友可信度</a>).<br/>
3.上传一张近期的生活照为参赛照片(可在参赛后随时更换,以吸引更多粉丝)<br/>
4.比赛中若发现用户不是女性,或恶意捣乱,将直接取消参赛资格和所有选票靓点.<br/>
二.排名和投票<br/>
1.比赛期间,每位乐酷用户均可为自己喜爱的参赛女性用户投票,用户赠送"奢侈品"给参赛女性用户,参赛女性用户则获得选票(靓点).<br/>
2.乐后排名是以选票(靓点)的多少进行排名.<br/>
3.每个参赛女性用户,都会有粉丝排行榜,根据对她投票多少来排名.想讨好女孩的男士抓紧哦^_^.<br/>
4.当参赛女性用户被取消资格,或尚未通过审核时,其他用户将无法对她投票.<br/>
5.用户投票需谨慎,一旦投票不可撤票,因此不要被不良用户所欺骗.乐酷会严查不良用户,一旦发现,则对其严厉处罚.<br/>
三.礼品兑换及奖品说明<br/>
1.选票(靓点)越高的,排名越高,赛事结束时前10名女性用户将获得大奖.<br/>
2.所有参赛选手,赛事结束后都可以用靓点,在<a href="goods.jsp">礼品兑换区</a>换取中意的实物产品.靓点一经兑换,不可再用,兑完为止.剩余的靓点保留,不计入下次比赛选票成绩,但可以与下次比赛累积的靓点一起,来兑换价值更高的礼品.价值更高的礼品.<br/>
3.排名前10名的乐后在乐后结束时将获得丰厚的实物奖励.<br/>
4.本次乐后选拔赛所有奖品均由买卖宝公司(mmb.cn)赞助.<br/>
<%
} else {
%><%=tip%><br/><%		
}
%><a href="join.jsp">返回上一页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>