<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%
	
	
	CastleAction action = new CastleAction(request, response);
	CastleUserBean user = action.getCastleUser();
	int uid = user.getUid();
	int id = action.getParameterInt("id");
	CastleBean castle = action.getCastle();
	if(id!=0){
		CastleBean castle2 = CastleUtil.getCastleById(id);
		if(castle2!=null&&castle2.getUid()==uid) {
			user.setCur(castle2.getId());
			SqlUtil.executeUpdate("update castle_user set cur=" + id + " where uid=" + uid, 5);
			castle = castle2;
			action.setCastle(castle);
			action.setUserResBean(CastleUtil.getUserResBeanById(castle.getId()));
			String redirect = (String)action.getAttribute2("casSwi");
			if(redirect!=null){
				action.innerRedirect(redirect);
				return;
			}
		}
	}
	action.curPage = 5;
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="切换城堡"><p><%@include file="top.jsp"%><%

	List list = action.getCastleService().getCastleList(uid);
	int cid = user.getCur();
	
%>选择下列城堡为当前城堡<br/>
<%for(int i = 0; i < list.size() ; i++) {
	CastleBean castleBean = (CastleBean)list.get(i);
	UserResBean userRes = (UserResBean)CastleUtil.getUserResBeanById(castleBean.getId());
%><%if(user.getMain()==castleBean.getId()){%>*<%}%><%=i+1 %>.<%if(castleBean.getId()==cid){%><%=castleBean.getCastleNameWml()%><%}else{%><a href="castle.jsp?id=<%=castleBean.getId()%>"><%=castleBean.getCastleNameWml()%></a><%}%>(<%=castleBean.getX() %>|<%=castleBean.getY() %>)人口<%=userRes.getPeople()%><br/><%
}%>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>