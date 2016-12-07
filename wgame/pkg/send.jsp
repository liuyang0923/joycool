<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
int id = action.getParameterInt("id");
PkgBean bean = action.getPkg(id);
UserBean loginUser = action.getLoginUser();
if(bean==null||bean.getUserId()!=loginUser.getId()){
	response.sendRedirect("my1.jsp");
	return;
}
PkgTypeBean type = (PkgTypeBean)action.getPkgType(bean.getType());
long now = System.currentTimeMillis();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=type.getName()%><br/>
<%=StringUtil.toWml(bean.getTitle())%><br/>
<img src="/rep/img/pkg/<%=type.getId()%>.gif" alt=""/><br/>
<%=StringUtil.toWml(bean.getContent())%><br/>
<%if(bean.getStatus()<1){%>
<a href="write.jsp?id=<%=id%>">修改礼签和正文</a><br/>
<%if(bean.getSendTime()>now){%>礼包定时在<%=DateUtil.sformatTime(bean.getSendTime())%>发送<br/><%}%>
<%if(bean.getOpenTime()>now){%>对方在<%=DateUtil.sformatTime(bean.getOpenTime())%>之后才能打开这个礼包<br/><%}%>
<a href="set.jsp?id=<%=id%>">设置打开时间</a><br/>
<%if(bean.getMoney()>0){%>礼包内含<%=StringUtil.bigNumberFormat(bean.getMoney())%>乐币的红包<br/><%}%>
<%if(bean.getItem().length()>0){%>礼包内放置了<%=bean.getItemName()%><br/><%}%>
<%if(bean.getMoney()==0){%><a href="set2.jsp?id=<%=id%>">设置红包和礼品</a><br/><%}%>
<a href="send2.jsp?id=<%=id%>">发礼包给好友&gt;&gt;</a><br/>
<%}else if(bean.getStatus()==1){%>
这个礼包将在<%=DateUtil.sformatTime(bean.getSendTime())%>送给<a href="/user/ViewUserInfo.do?userId=<%=bean.getToId()%>"><%=UserInfoUtil.getUser(bean.getToId()).getNickNameWml()%></a><br/>
<%}else{%>
这个礼包已经在<%=DateUtil.sformatTime(bean.getSendTime())%>送给<a href="/user/ViewUserInfo.do?userId=<%=bean.getToId()%>"><%=UserInfoUtil.getUser(bean.getToId()).getNickNameWml()%></a><br/>
<%if(bean.getOpenTime()>now){%>对方在<%=DateUtil.sformatTime(bean.getOpenTime())%>之后才能打开这个礼包<br/><%}%>
<%if(bean.getMoney()>0){%>礼包内含<%=StringUtil.bigNumberFormat(bean.getMoney())%>乐币的红包<br/><%}%>
<%if(bean.getItem().length()>0){%>礼包内放置了<%=bean.getItemName()%><br/><%}%>
<%}%>
<br/>
<a href="my1.jsp">返回我的礼包列表</a><br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>