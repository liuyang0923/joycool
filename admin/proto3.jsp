<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.CustomAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request, response);
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷用户协议">
<p align="left">
【服务使用规则】<br/>
1.用户不得利用乐酷服务制作、复制、上传、发布、传播含有下列内容之一的信息：<br/>
1） 反对宪法所确定的基本原则的； <br/>
2） 危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的； <br/>
3） 损害国家荣誉和利益的； <br/>
4） 煽动民族仇恨、民族歧视、破坏民族团结的； <br/>
5） 破坏国家宗教政策，宣扬邪教和封建迷信的； <br/>
6） 散布谣言，扰乱社会秩序，破坏社会稳定的； <br/>
7） 散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的； <br/>
8） 侮辱或者诽谤他人，侵害他人合法权利的； <br/>
9） 煽动非法集会、结社、游行、示威、聚众扰乱社会秩序的；<br/>
10）以非法民间组织名义活动的；<br/>
11）含有虚假、有害、胁迫、侵害他人隐私、骚扰、侵害、中伤、粗俗、猥亵、或其它道德上令人反感的内容； <br/>
12） 含有中国法律、法规、规章、条例以及任何具有法律效力之规范所限制或禁止的其它内容的。<br/> 
2.用户必须保证，您拥有您传播之照片、文字及背景音乐等作品之著作权或已获合法授权，您在乐酷之传播行为未侵犯任何第三方之合法权益。否则，将由您承担由此带来的一切法律责任；用户不得将任何内部资料、机密资料、涉及他人隐私资料或侵犯任何人的专利、商标、著作权、商业秘密或其他专属权利之内容加以任何方式之传播。<br/>
3.用户不得通过不正当的手段或其他不公平的手段使用乐酷产品或参与乐酷活动。用户不得干扰乐酷正常提供产品和服务，包括但不限于：利用程序的漏洞和错误(Bug)破坏游戏的正常进行或传播漏洞或错误(Bug)；不合理地干扰或阻碍他人使用乐酷产品和服务。乐酷有权对有上述行为的用户采取任何处理措施，包括服务范围内的任意处理并保留法律问责权利。<br/>
4.乐酷要求用户之间不得进行任何虚拟物品与真实货币或者物品的交易行为，包括虚拟货币、虚拟物品与乐酷id等。乐酷将不对此类交易中产生的任何问题进行支持和保障，并视为违反使用规则的行为，有权加以处理。<br/>
5.乐酷有权对用户传播的任何内容进行审核，有任何违反本协议规定的内容，乐酷有权立即处理。<br/>

<a href="proto.jsp">返回总则</a><br/>
<%if(loginUser==null){%><a href="/register.jsp">注册</a>|<a href="/user/login.jsp">登陆</a><br/><%}%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>