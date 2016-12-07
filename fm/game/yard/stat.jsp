<%@ page language="java" import="net.joycool.wap.util.*,net.joycool.wap.bean.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%! static int COUNT_PRE_PAGE = 10;%>
<%YardAction action=new YardAction(request);
String tip = "";
int id=action.getParameterInt("id");
int fmId = action.getFmId();
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
List list = null;
UserBean user = null;
YardUserBean yardUserBean = null;
FamilyHomeBean fm=action.getFmByID(id);
YardBean yard=action.getYardByID(id);
int type = action.getParameterInt("t");
if (type < 0 || type > 1)
	type = 0;
if (type == 0){
	// 本周
	list = YardAction.yardService.getYardUserBeanList(" fmid=" + id + " order by seed_count desc");
} else {
	// 上周
	list = YardAction.yardService.getYardUserBeanList2(" fmid=" + id + " order by seed_count desc"); 
}
PagingBean paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="生产统计"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>生产统计(<%if(type==0){%>本周|<a href="stat.jsp?id=<%=id%>&amp;t=1">上周</a><%}else{%><a href="stat.jsp?id=<%=id%>">本周</a>|上周<%}%>)<br/>
用户|贡献<br/>
<%if (list != null && list.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		yardUserBean = (YardUserBean)list.get(i);
		if (yardUserBean != null){
			user = UserInfoUtil.getUser(yardUserBean.getUserid());
			if (user != null){
				%><%=user.getNickNameWml()%>|<%=yardUserBean.getSeedCount()%>种子<br/><%
			}
		}
	}%><%=paging.shuzifenye("stat.jsp?id=" + id + "&amp;t=" + type ,true,"|",response)%><%
}
%>
<%
} else {
%><%=tip%><br/><%
}%>
<a href="prod.jsp?id=<%=id%>">返回生产基地</a><br/>
<a href="index.jsp?id=<%=id%>">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>