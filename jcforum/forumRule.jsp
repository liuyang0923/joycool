<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%response.setHeader("Cache-Control","no-cache"); %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛须知">
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(0,request,response)%>
乐酷论坛是完全免费的！我们希望您遵守以下的论坛规定，请仔细阅读下列条款：<br/>
1. 遵守中华人民共和国的各项有关法律法规。<br/>
2. 不得在发布任何色情非法，以及危害国家安全的言论。<br/>
3. 严禁链接有关政治，色情，宗教，迷信等违法信息。<br/>
4. 承担一切因您的行为而直接或间接导致的民事或刑事法律责任。<br/>
5. 互相尊重，遵守互联网络道德，严禁互相恶意攻击，漫骂。<br/>
6. 管理员及版主有权保留或删除论坛中的任意内容。<br/>
7. 本站管理员拥有一切管理权力。<br/>
8. 禁止在论坛上随意公布他人手机号等隐私信息。<br/>
9. 为了保证用户的账户安全，禁止发布外网连接。<br/>
10.禁止恶意刷论坛或者使用外挂刷论坛。<br/>
<br/>
<a href="/jcforum/index.jsp">返回论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
