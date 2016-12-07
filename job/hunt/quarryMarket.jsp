<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.job.*,java.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.action.job.*" %><%
response.setHeader("Cache-Control","no-cache");
String sales=request.getParameter("sales");
HuntAction  hunt=new HuntAction(request);
int sale=0;
int mark=1;
if(sales!=null)
{
	Calendar cal = Calendar.getInstance();
	int currentHour = cal.get(Calendar.HOUR_OF_DAY);
	if(currentHour < 18)
		sale=5;
	else
		sale=hunt.saleQuarry(request);

	mark=2;
	
}
hunt.quarryList(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=UserInfoUtil.getUserStatus(loginUser.getId());
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector quarryList=(Vector)request.getAttribute("quarryList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="猎物列表">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(mark==1){
%>
走私贩子：嘿嘿，您卖什么猎物？小弟价格公道、童叟无欺啊!<br/>
<%if(quarryList!=null){%>
<a href="saleTotal.jsp">卖出全部猎物</a>（注意!请保证卖完之后现金不超过21亿，否则超出部分会溢出无法进账户!）<br/>
<%}
}else if(sale==HuntAction.DEALOK){
String quarryName=(String)request.getAttribute("quarry");
String saleNum=(String)request.getAttribute("sales");
String money=(String)request.getAttribute("money");

%>走私贩子：您卖了<%=saleNum%>个<%=quarryName%>，获得<%=money%>乐币!现有乐币<%=us.getGamePoint()%>。还想卖点什么？<br/>
<%}else if(sale==HuntAction.WRONGNUM){%>
走私贩子：对不起您的猎物数量不对呀!嘿嘿，您还想卖什么猎物？<br/>
<%}else if(sale==HuntAction.DEALFAILURE){%>
走私贩子：对不起,本次交易已经结束!嘿嘿，您还想卖什么猎物？<br/>
<%}else if(sale==5){%>
走私贩子：对不起,下午6点之后再来！<br/>
<%}
if(quarryList!=null){
for(int i = 0; i < quarryList.size(); i ++){
	HuntUserQuarryBean userQuarry = (HuntUserQuarryBean) quarryList.get(i);
	HashMap quarryMap=LoadResource.getQuarryMap();
	HuntQuarryBean quarry=(HuntQuarryBean)quarryMap.get(new Integer(userQuarry.getQuarryId()));
%>
<%=(Integer.parseInt(pageIndex)*Integer.parseInt(perPage)+i + 1)%>.<%=StringUtil.toWml(quarry.getName())%>
<%=userQuarry.getQuarryCount()%>(个)
<%=quarry.getPrice()%>乐币
<a href="viewSaleQuarry.jsp?quarry=<%=userQuarry.getQuarryId()%>&amp;count=<%=userQuarry.getQuarryCount()%>&amp;backTo=<%=PageUtil.getBackTo(request)%>" >卖</a><br/>
<%
}}
%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"quarryMarket.jsp",false," ",response)%><br/>
<br/>
<a href="huntArea.jsp">返回狩猎区</a><br/>
<a href="hunt.jsp">返回狩猎首页</a><br/>
<a href="<%=("/lswjs/index.jsp")%>">返回导航中心</a><br/>
</p>
</card>
</wml>