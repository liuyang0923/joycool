<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.shop.*,java.util.*,net.joycool.wap.bean.UserBean,net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
String[] gend = {"限女性","限男性","通用"};
int Iid = action.getParameterInt("Iid");
int from = action.getParameterInt("from");
Pocket po = CoolShowAction.service.getDate1("del=0 and user_id="+ub.getId()+" and item_id="+Iid);
Commodity com=CoolShowAction.getCommodity(Iid);
if(com!=null&&po==null&&com.getDel()!=0) com=null;	// 如果没有购买，而且商品已经下架
List advlist = CoolShowAction.service.getAdv(" place=0");
List temp = new ArrayList();
if(com != null){
	for(int i = 0;i<advlist.size();i++){
		BeanAdv adv = (BeanAdv)advlist.get(i);
		Commodity com2=CoolShowAction.getCommodity(adv.getCommid());
		if(com2.getType() != com.getType()){
			temp.add(advlist.get(i));
		}
	}
	advlist = temp;
}
UserInfoBean ui = ShopAction.shopService.getUserInfo(ub.getId());
if(ui == null)
	ui = ShopAction.shopService.addUserInfo(ub.getId());
if(advlist!=null && advlist.size() > 2)
	advlist = action.getRandList(advlist,2);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="酷秀信息">
<p><%=BaseAction.getTop(request, response)%><%
if(com == null){
%>没有该商品!<br/><%
}else{
%>【<%=StringUtil.toWml(com.getName())%>】<br/>
<img alt="o" src="/rep/show/comm/<%=com.getBigImg()%>"/><br/><%
	if(po != null){
		if(com != null){
			%>性别:<%=gend[com.getGender()]
			%><br/>过期时间:<%=DateUtil.formatDate2(po.getEndTime())%><%
			%><br/>状态:<%
			if(po.getState() == 1){
			%>已购买|已穿戴<br/><%
			}else{
			%>已购买|未穿戴<br/><a href="room.jsp?Iid=<%=com.getIid()%>">穿戴</a>|<%
			}%><a href="sureBuy.jsp?Iid=<%=Iid%>">续费</a><%
			if(from == 1){
				if(com.getPrice()!=0){
				%>|<a href="suregive.jsp?Iid=<%=Iid%>">赠送</a><%
				}
			%>|<a href="col.jsp?Iid=<%=Iid%>">收藏</a><%
			}%><br/><%
		}else{
			%>没有该商品!<br/><%
		}
	}else{
	%>类型:<%=com.getCatalogName()
	%><br/>性别:<%=gend[com.getGender()]
	%><br/>价格:<%if(com.getPrice()>0){ %><select name="due"><option value="1"><%=CoolShowAction.formatDuePrice(com.getPrice(), 1)%>酷币/月</option><option value="3"><%=CoolShowAction.formatDuePrice(com.getPrice(), 3)%>酷币/3个月</option><option value="6"><%=CoolShowAction.formatDuePrice(com.getPrice(), 6)%>酷币/6个月</option><option value="12"><%=CoolShowAction.formatDuePrice(com.getPrice(), 12)%>酷币/年</option></select><%}else{ %>免费<%} %>
	<br/><a href="room.jsp?Iid=<%=com.getIid()%>">试穿</a>|<anchor>购买<go href="sureBuy.jsp?Iid=<%=Iid%>" method="post"><postfield name="due" value="$due"/></go></anchor>|<%
	if(com.getPrice()!=0){%><a href="suregive.jsp?Iid=<%=Iid%>">赠送</a>|<%}
	%><a href="col.jsp?Iid=<%=Iid%>">收藏</a><br/><%
		}%>您现有<%=ui.getGoldString()%>[<a href="/pay/pay.jsp">充值</a>]<br/><%
	if(from==1){	
	%>[分类]<%
	  	List catalogList = CoolShowAction.getCatalogList();
		for(int i=0;i<catalogList.size();i++){
			CatalogBean catalog = (CatalogBean)catalogList.get(i);
			if(catalog.isHide()) continue;
			if(catalog !=null){
			%><a href="list.jsp?gend=<%=com.getGender()+1%>&amp;p=<%=catalog.getId()%>"><%=catalog.getName()%></a>|<%
			}
		}%><br/>==相关推荐==<br/><%
		if(advlist!=null&&advlist.size()>0){
			for(int i=0;i<advlist.size();i++){
				BeanAdv adv = (BeanAdv)advlist.get(i);
				Commodity comm = CoolShowAction.getCommodity(adv.getCommid());
				if(comm != null){
				%><a href="consult.jsp?from=1&amp;Iid=<%=comm.getIid()%>"><%=StringUtil.toWml(comm.getName())%></a><%=comm.getPrice()==0?"免费":comm.getPrice()+"酷币"%><br/><%
				}
			}
		}
		%><a href="index.jsp">&gt;我的酷秀</a><br/><%
	}
}
%><a href="myGoods.jsp">&lt;我的物品</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>