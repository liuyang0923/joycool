<%@ page language="java" import="net.joycool.wap.bean.*,net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.game.yard.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%>
<%! static int COUNT_PRE_PAGE = 10;%>
<%YardAction action=new YardAction(request);
int id = action.getParameterInt("id");
if(id <= 0){
	response.sendRedirect("/fm/index.jsp");
	return;
}
YardBean yard = action.getYardByID(id);
if(yard==null){
	response.sendRedirect("/fm/index.jsp");
	return;
}
int bottomExp = 0;
if(yard.getRank()>0)
	bottomExp=YardBean.rankExps[yard.getRank()-1];
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族餐厅"><p align="left"><%=BaseAction.getTop(request, response)%>
【<%=yard.getNameWml()%>】<br/>
开张于:<%=DateUtil.sformatTime(yard.getCreateTime())%><br/>
等级:<%=yard.getRank()+1%><br/>
经验值:<%=yard.getExp()%>(<%=(yard.getExp()-bottomExp)*100/(YardBean.rankExps[yard.getRank()]-bottomExp)%>%)<br/>
现有资金:<%=YardAction.moneyFormat(yard.getMoney())%>元<br/>
现有食谱:<%=yard.getRecipeSet().size()%>个<br/>
==餐厅大厨==<br/>
<%
List list=YardAction.getService().selectUserIdList(id," and fm_flags&" + FamilyUserBean.FLAG_YARD);
if(list.size()==0){
%>(暂无)<br/><%}else{

for(int i = 0;i < list.size();i++){
	Integer iid = (Integer)list.get(i);
	UserBean user = UserInfoUtil.getUser(iid.intValue());
	%><%=i+1%>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a><br/><%
}
}%>
<a href="index.jsp?id=<%=id%>">返回家族餐厅</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>