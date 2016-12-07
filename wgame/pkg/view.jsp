<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
int id = action.getParameterInt("id");
UserBean loginUser = action.getLoginUser();
PkgBean bean = action.getPkg(id);
if(bean==null||bean.getToId()!=loginUser.getId()){
	response.sendRedirect("my2.jsp");
	return;
}
UserBean user = UserInfoUtil.getUser(bean.getUserId());
PkgTypeBean type = (PkgTypeBean)action.getPkgType(bean.getType());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=type.getName()%>(<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>)<br/>
<%=StringUtil.toWml(bean.getTitle())%><br/>
<img src="/rep/img/pkg/<%=type.getId()%>.gif" alt=""/><br/>
<%if(bean.getStatus()==2){%>
<%	if(bean.getOpenTime() <= System.currentTimeMillis()){%>
<a href="open.jsp?id=<%=id%>">拆开这个礼包&gt;&gt;</a><br/>
<%	}else{%>
这个礼包要在<%=DateUtil.sformatTime(bean.getOpenTime())%>之后才能打开。<br/>
<%	}%>
<%}else{%>
<%=StringUtil.toWml(bean.getContent())%><br/>
<%if(bean.getMoney()>0){%>礼包内含<%=StringUtil.bigNumberFormat(bean.getMoney())%>乐币的红包<br/><%}%>
<%if(bean.getItem().length()>0){%>礼包内放置了<%=bean.getItemName()%><br/><%}%>
<%}%>
<br/>
<a href="my2.jsp">返回我的礼包列表</a><br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>