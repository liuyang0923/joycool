<%@ page language="java" import="jc.family.game.*,java.util.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%!

%><%
YardAction action=new YardAction(request);
int id=action.getFmId();
int li = action.getParameterInt("li");
FamilyHomeBean fm=action.getFmByID(id);
YardBean yard=action.getYardByID(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="培育中心"><p align="left"><%=BaseAction.getTop(request, response)%><%
Calendar cale=Calendar.getInstance();
if(cale.get(Calendar.DAY_OF_WEEK)>7||cale.get(Calendar.DAY_OF_WEEK)<0){
%>今日培育中心不开放!<br/>
餐厅作息安排:<br/>
生产基地开放:周一～周日<br/>
培育中心开放:周一～周三<br/>
烹饪功能开放:周五～周日<br/>
杂货铺开放:周一～周日<br/><%
}else{
	List list = YardAction.getSeedList();
	for(int i=0;i<list.size();i++){
		YardItemProtoBean proto = (YardItemProtoBean)list.get(i);
		YardItemBean yardItem=yard.getItem(proto.getId());
		if(yardItem!=null&&yardItem.getNumber()>0){
			%><%=yardItem.getItemNameWml()%>(<%=yardItem.getNumber()%>)|<a href="land.jsp?li=<%=li%>&amp;itemid=<%=proto.getId()%>">选定</a><br/><%
		}
	}
}
%><a href="land2.jsp?id=<%=id%>">返回培育中心</a><br/><a href="index.jsp?id=<%=id%>">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>