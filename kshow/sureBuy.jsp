<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.shop.*,net.joycool.wap.bean.*,net.joycool.wap.util.*,net.joycool.wap.framework.BaseAction,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
String show[] = {"限女性","限男性","通用","续费","购买"};
int uid = ub.getId();
UserInfoBean bean = ShopAction.shopService.getUserInfo(uid);
if(bean == null)
	bean = ShopAction.shopService.addUserInfo(uid);
int Iid = action.getParameterInt("Iid");
int temp = 0;
long time = 0;
Commodity com = CoolShowAction.getCommodity(Iid);
Pocket po = CoolShowAction.service.getDate1("del=0 and user_id="+ub.getId()+" and item_id="+Iid);
int due = action.getParameterInt("due");
if(com.getPrice()==0)
	due=1;
if(due != 1 && due != 3 && due != 6 && due != 12)
	due = 0;
if (po != null) {
	if(po.getEndTime() > System.currentTimeMillis()){
		time = po.getEndTime()+24l*60*60*1000*30*due;
	}
	if(com.getPrice() == 0){
		time = System.currentTimeMillis()+24l*60*60*1000*30*due;
	}
	temp = 3;
}else{
	temp = 4;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=show[temp]%>酷秀">
<p><%=BaseAction.getTop(request, response)%><%
if(com != null){
	session.setAttribute("ses","buy");
%>您要<%=show[temp]%>的酷秀如下:<br/>名称:【<a href="consult.jsp?from=1&amp;Iid=<%=Iid%>"><%=StringUtil.toWml(com.getName())%></a>】<br/>
类型:<%=com.getPartName()%><br/>
性别:<%if(com.getGender()>=0&&com.getGender()<3){%><%=show[com.getGender()]%><%}%><br/>
价格:<%if(due==0){%><%if(com.getPrice()>0){%><select name="due"><option value="1"><%=CoolShowAction.formatDuePrice(com.getPrice(), 1)%>/月</option><option value="3"><%=CoolShowAction.formatDuePrice(com.getPrice(), 3)%>/3个月</option><option value="6"><%=CoolShowAction.formatDuePrice(com.getPrice(), 6)%>/6个月</option><option value="12"><%=CoolShowAction.formatDuePrice(com.getPrice(), 12)%>/年</option></select><%}else{%>免费<%}%>
<br/>
<anchor><%=show[temp]%><go href="sureBuy.jsp?Iid=<%=Iid%>" method="post"><postfield name="due" value="$due"/></go></anchor>
<%}else{%><%if(com.getPrice()>0){%><%if(due==1){%><%=com.getPrice()%>酷币/月<%}else if(due==3){%><%=com.getPrice3()%>酷币/3个月<%}else if(due==6){%><%=com.getPrice6()%>酷币/6个月<%}else if(due==12){%><%=com.getPrice12()%>酷币/年<%}%><%}else{ %>免费<%}%>
<br/><%if(time!=0){%>续费后到期时间:<%=DateUtil.formatDate2(time)%><br/><%}%><a href="buy.jsp?Iid=<%=Iid%>&amp;due=<%=due %>">确认<%=show[temp]%></a>
<% }%><br/><%
}else{
%>没有此酷秀!<br/><%	
}
%><a href="myGoods.jsp">&lt;我的物品</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>