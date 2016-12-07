<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.PagingBean"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmUser=action.getFmUser();
if(fmUser==null){
response.sendRedirect("index.jsp");return;
}
int p=action.getParameterInt("p");
FamilyHomeBean home=FamilyAction.getFmByID(fmUser.getFm_id());
PagingBean paging= new PagingBean(action, home.getFm_member_num(), 10, "p");
List list=action.service.selectFmGiveList(fmUser.getFm_id(), paging.getStartIndex(), paging.getCountPerPage());
int score=0;
List newlist=action.service.selectAllFmList(fmUser.getFm_id());
for(int i = 0; i < newlist.size(); i++) {
	int uid=((FamilyUserBean)newlist.get(i)).getId();
	if(uid==fmUser.getId()){
		score=i + 1;
		continue;
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="基金贡献榜"><p align="left"><%=BaseAction.getTop(request, response)%>
您总计贡献基金<%=fmUser.getGift_fm()%>,排名:<%=score%><br/><%
if(list!=null&&list.size()>0){
	for(int i=0;i<list.size();i++){
		FamilyUserBean bean=(FamilyUserBean)list.get(i);
		%><%=i+1+p*10%>.<a href="/user/ViewUserInfo.do?userId=<%=bean.getId()%>"><%=bean.getNickNameWml()%></a>&#160;<%=bean.getGift_fm()%><br/><%
	}
}
%><%=paging.shuzifenye("fundapply.jsp", false, "|", response)%>
<a href="fundmgt.jsp">返回家族基金</a><br/>
<a href="myfamily.jsp">返回我的家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>