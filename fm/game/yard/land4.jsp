<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
YardAction action=new YardAction(request);
String tip = "";
int result = 0;
int id = action.getFmId();
FamilyUserBean fmUser = action.getFmUser();
int fmId = fmUser.getFmId();
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard = action.getYardByID(id);
YardItemProtoBean item = null;
int li = action.getParameterIntS("li");
List list = yard.getLandList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="培育中心"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if (li<0){
if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		YardLandBean land = (YardLandBean)list.get(i);
		if (land != null){
			item = land.getItemOnLand(); // 取得这块地上正在种着的物品
			if (item != null){
				%><%=i+1%>.<%=item.getNameWml()%><%
			} else {
				%><%=i+1%>.空地<%
			}
			%>(<%=land.getMaxCount()%>)<%if(land.getRank()<4){%><a href="land4.jsp?li=<%=i%>">升级</a><%}%><br/><%
		}
	}
}

} else {	// 选中一个地

YardLandBean land = (YardLandBean) list.get(li);
if (li >= 0){
	int comfirm = action.getParameterIntS("c");
	if(comfirm >= 0 && comfirm == land.getRank()){
		// 升级地
		if(!fmUser.isflagYard()){
			action.tip("tip","!只有餐厅大厨才有这个权限!");
		} else if (yard.updateLand(li)!=0){
			action.tip("tip","!您的资金不足!");
		} else {
			action.tip("tip","!土地升级成功!");
		}
	}
}

	%><%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%><%
	
%>当前容量:<%=land.getMaxCount()%><br/>
<%if(land.getRank()>=4){%>
已达到最高等级.<br/>
<%}else{%>
升级后容量:<%=YardLandBean.maxCount[land.getRank()+1]%><br/>
升级需要资金<%=YardAction.moneyFormat(YardBean.landUpdateMoney[land.getRank()])%>元<br/>
<a href="land4.jsp?li=<%=li%>&amp;c=<%=land.getRank()%>">确认升级</a><br/>
<%}%>
<a href="land4.jsp">返回</a><br/><%
}
%>
<a href="land2.jsp">返回培育中心</a><br/>
<a href="index.jsp">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>