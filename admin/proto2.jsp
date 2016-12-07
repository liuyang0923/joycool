<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.CustomAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request, response);
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷用户协议">
<p align="left">
【乐酷服务风险】<br/>
1.乐酷拥有所有服务的完整所有权，乐酷对所有服务保留完整的提供、改变、中断、停止部分或其他行为的权利，并拥有对破坏或影响乐酷这一权益的任何用户行为做出相应处理手段的权利。<br/>
2.用户完全理解并同意，本服务涉及到移动通讯等服务，可能会受各环节不稳定因素影响。因此服务存在因上述不可抗力、计算机病毒或黑客攻击、系统不稳定、用户所在位置、用户关机、GSM网络、互联网络、通信线路原因等造成的服务中断或不能满足用户要求的风险。使用本服务的用户须承担以上风险，乐酷对服务及时性、安全性、准确性不作担保。对于不可抗力或非乐酷过错原因导致的用户损失，乐酷将不承担任何责任。 <br/>
3.对于系统故障影响到本服务正常运行，乐酷承诺及时处理。但用户因此而产生的经济和精神损失，乐酷不承担责任。此外，乐酷保留不经事先通知为维修保养、升级或其他目的暂停本服务任何部分的权利。<br/>
4.乐酷提示，任何经由本服务传播内容，均由内容提供者承担责任。乐酷无法控制经由本服务传播之内容，也无法对用户的使用行为进行全面控制，因此不保证内容的合法性、正确性、完整性、真实性或品质；您已预知使用本服务时，可能会接触到令人不快、不适当或令人厌恶之内容，并同意将自行加以判断并承担所有风险。但在任何情况下，乐酷有权依法停止传输任何前述内容并采取相应行动，包括但不限于暂停用户使用本服务的全部或部分，保存有关记录，并向有关机关报告。但乐酷有权拒绝和删除可经由本服务提供之违反本条款的或其他引起乐酷或其他用户反感的任何内容。<br/>
5.用户完全理解并同意，通过本服务收到广告信息，进而与广告商进行任何形式的商业往来，或参与促销活动，包含相关商品或服务之付款及交付，以及达成的其他任何相关条款、条件、保证或声明，完全为您与广告商之间之行为。除有关法律有明文规定要求乐酷承担责任以外，您因前述任何交易或前述广告商而遭受的任何性质的损失或损害，乐酷均不予负责。<br/>
6. 如发生下列任何一种情形，乐酷有权随时中断或终止向用户提供服务而无需通知该用户：<br/>
6.1 用户违反本服务条款的规定；<br/>
6.2 按照主管部门的要求；<br/>
6.3 用户注册后长期闲置不用的ID；<br/>
<a href="proto.jsp">返回总则</a><br/>
<%if(loginUser==null){%><a href="/register.jsp">注册</a>|<a href="/user/login.jsp">登陆</a><br/><%}%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>