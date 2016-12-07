<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.BaseAction,jc.show.*,java.util.*,net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
String[] gend = {"限女性","限男性","通用"};
CoolUser cu = CoolShowAction.getCoolUser(ub);
TryBean tryShow = (TryBean)session.getAttribute("tryShow");
List list = action.filterMyGoods(cu,tryShow);
List temp = new ArrayList();
float price = 0;
for(int i=0;i<list.size();i++){
	Commodity com = (Commodity)list.get(i);
	if(com.getDel()==0){
		price+=com.getPrice();
		temp.add(com);
	}
}		
int back = 0;
if(list.size()==0||action.hasParam("sure")){	// 用户确认购买,或者没有需要购买的
	back = action.buyAll(cu,list,tryShow);
}
int due = action.getParameterInt("due");
if(due != 1 && due != 3 && due != 6 && due != 12)
	due = 0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的试衣间">
<p><%=BaseAction.getTop(request, response)%><%
if(back == 1){
%><%if(list.size()!=0){%>购买成功!形象保存成功!<br/>酷秀过期时间<%=DateUtil.sformatTime(cu.getEndTime())%><%}else{%>形象保存成功!<%}%><br/><%
}else if(back == 2){
%>购买失败!您的酷币不足!点击【<a href="/pay/pay.jsp">充值</a>】<br/><a href="myGoods.jsp">&lt;我的物品</a><br/><%
}else if(back == 3){
%>无法重复操作!<br/><a href="myGoods.jsp">&lt;我的物品</a><br/><%
}else{
	%>==购买酷秀==<br/>共<%=temp.size()%>件酷秀待购买<br/><%
	if(temp !=null&&temp.size()>0){
		session.setAttribute("ses","buyall");
		%>总计价格:<%if(due==0){
			if(price>0){
%><select name="due"><option value="1"><%=CoolShowAction.formatDuePrice(price, 1)%>/月</option><option value="3"><%=CoolShowAction.formatDuePrice(price, 3)%>/3个月</option><option value="6"><%=CoolShowAction.formatDuePrice(price, 6)%>/6个月</option><option value="12"><%=CoolShowAction.formatDuePrice(price, 12)%>/年</option></select><br/><%
			}else{
			%>免费<br/><%
			}
		%><%if(price==0){%><a href="save.jsp?sure=1">确定购买</a><%}else{%><anchor>购买<go href="save.jsp" method="post"><postfield name="due" value="$due"/></go></anchor><%}%>|<a href="room.jsp">返回</a><br/><%
		}else{
			%><%=price==0?"免费":CoolShowAction.formatDuePrice(price, due)%>/<%=due%>个月<br/><%
			%><a href="save.jsp?sure=1&amp;due=<%=due%>">确定购买</a>|<a href="save.jsp">返回</a><br/><%
		}
	%>==单品列表==<br/><%
		for(int i=0;i<temp.size();i++){
			Commodity com = (Commodity)temp.get(i);
			if(com != null && com.getDel()==0){
%><%=i+1%>.<%=com.getName()%><%if(due==0){%>(<%=CoolShowAction.formatDuePrice(com.getPrice(), 1)%>)<a href="sureBuy.jsp?Iid=<%=com.getIid()%>">购买单件</a><%}%><br/><%
			}
		}
	}
}
%><a href="room.jsp">返回试衣间</a><br/><a href="index.jsp">&gt;我的酷秀</a><br/>
<a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>