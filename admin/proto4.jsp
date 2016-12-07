<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.CustomAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request, response);
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷用户协议">
<p align="left">
乐酷游戏规则<br/>
1.坚决抵制外挂，对于用户使用工具进行挂机、游戏等行为，乐酷有权在不通知的情况下立刻处理。处理手段包括封禁帐号的一切游戏功能等等，情节严重者将给予删号处罚。<br/>
2.禁止玩家开多个账号同时进行游戏.如果发现有一个玩家开多个账号同时游戏,视为使用外挂同1处理.<br/>
3.用户不得干扰乐酷正常提供产品和服务，包括但不限于：利用程序的漏洞和错误(Bug)破坏游戏的正常进行或传播漏洞或错误(Bug)，乐酷有权对有上述行为的用户采取任何处理措施，包括服务范围内的任意处理并保留法律问责权利。<br/>
4.乐酷反对用户私下用真实货币或者物品换取任何虚拟物品，包括虚拟货币与乐酷帐号等，因为此类交易容易被骗子利用。乐酷将不对此类交易中产生的任何问题进行支持和保障，并视为违反使用规则的行为。如果造成了不良影响，乐酷有权加以处理。<br/>
更详细的规则请见<a href="/admin/proto.jsp">乐酷用户协议和游戏规则总则</a><br/>
<a href="/user/onlineManager.jsp?forumId=355">返回乐酷警局</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>