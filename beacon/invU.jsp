<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="邀请"><p><%=BaseAction.getTop(request, response)%>
邀请好友|<a href="<%=("help.jsp")%>">查看帮助</a><br />
快速获取邀请函<br/>
<a href="<%=("getLetter.jsp?type=1")%>">照片</a>|<a href="<%=("getLetter.jsp?type=2")%>">报仇</a>|<a href="<%=("getLetter.jsp?type=3")%>">挑战</a><br/>
<a href="<%=("getLetter.jsp?type=4")%>">同学</a>|<a href="<%=("getLetter.jsp?type=5")%>">爱人</a><br/>
自由填写邀请函内容<br/>
<input name="c"/><br/>
<a href="<%=("userLetter.jsp")%>">获取邀请函</a>
<br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>

