<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.ssc.*" %><%@ page import="net.joycool.wap.util.*,java.util.List" %><%@ page import="net.joycool.wap.bean.*" %><%
response.setHeader("Cache-Control","no-cache");
LhcAction action = new LhcAction(request);
PagingBean paging = new PagingBean(action, 200, 10, "p");
List lhcWagerRecordList = action.getLhcService().getLhcWagerRecordList("mark=1 and prize>=100000000 order by id desc limit "+paging.getStartIndex()+",10");	// 最新的5条中奖记录
LhcWagerRecordBean lhcWagerRecord = null;
UserBean user = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="六时彩">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if (lhcWagerRecordList != null && lhcWagerRecordList.size() > 0){
	for (int i = 0 ; i < 10&&i<lhcWagerRecordList.size() ; i++){
		lhcWagerRecord = (LhcWagerRecordBean)lhcWagerRecordList.get(i);
		user = UserInfoUtil.getUser(lhcWagerRecord.getUserId());
		if (user != null){
			%><%=i+1+paging.getStartIndex()%>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>中<%
			if(lhcWagerRecord.getType()<=7){
				%><%=LhcWorld.LHC_NAME_ARRAY[lhcWagerRecord.getType()][lhcWagerRecord.getNum()]%><%
			}else if(lhcWagerRecord.getType()==8){
				%>平码<%
			}else if(lhcWagerRecord.getType()==9){
				%>特码<%
			}%><%=lhcWagerRecord.getPrize() / 100000000%>亿<br/><%
		}
	}
}%><%=paging.shuzifenye("more.jsp",false,"|",response)%>
<a href="index.jsp">返回首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>