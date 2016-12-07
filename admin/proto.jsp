<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.framework.CustomAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request, response);
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷用户协议">
<p align="left">
乐酷用户协议和游戏规则总则<br/>
【<a href="proto3.jsp">服务使用规则</a>】<br/>
1.用户不得利用乐酷服务传播任何具有法律效力之规范所限制或禁止的内容信息。<br/>
2.用户不得干扰乐酷正常提供产品和服务，包括但不限于：利用程序的漏洞和错误(Bug)破坏游戏的正常进行或传播漏洞或错误(Bug)，乐酷有权对有上述行为的用户采取任何处理措施，包括服务范围内的任意处理并保留法律问责权利。<br/>
3.乐酷反对用户私下用真实货币或者物品换取任何虚拟物品，包括虚拟货币与乐酷帐号等，因为此类交易容易被骗子利用。乐酷将不对此类交易中产生的任何问题进行支持和保障，并视为违反使用规则的行为。如果造成了不良影响，乐酷有权加以处理。<br/>
4.乐酷坚决抵制外挂，对于用户使用非官方承认的工具进行挂机、游戏等行为，乐酷有权在不通知的情况下立刻处理。处理手段包括封禁帐号的一切游戏功能等等，情节严重者将给予删号处罚。<br/>
<%if(loginUser==null){%><a href="/register.jsp">注册</a>|<a href="/user/login.jsp">登陆</a><br/><%}%>
【用户帐号.密码】<br/>
1.乐酷帐号使用权仅属于初始申请注册人，禁止赠与、借用、租用、转让或售卖。如果管理员发现使用者并非帐号初始注册人，有权在未经通知的情况下回收该帐号而无需向该帐号使用人承担法律责任，由此带来的包括并不限于用户数据资料和游戏道具等清空等损失由用户自行承担。乐酷禁止用户私下有偿或无偿转让帐号，以免因帐号问题产生纠纷，用户应当自行承担因违反此要求而遭致的任何损失，同时乐酷保留追究上述行为人法律责任的权利。<br/>
2.用户承担乐酷帐号与密码的保管责任，并就其帐号及密码项下之一切活动负全部责任。用户同意如发现他人未经许可使用其帐号时立即通知管理员。<br/> 
3.用户帐号在丢失或遗忘密码后，须遵照乐酷的申诉途径及时申诉请求找回帐号。对用户因被他人冒名申诉而致的任何损失，乐酷不承担任何责任，用户知晓乐酷帐号及密码保管责任在于用户，乐酷并不承诺帐号丢失或遗忘密码后用户一定能通过申诉找回帐号。<br/>
4.用户注册帐号后如果长期不使用，乐酷有权回收帐号，以免造成资源浪费，由此带来的包括并不限于用户数据资料、游戏道具丢失等损失由用户自行承担。<br/>
【<a href="proto2.jsp">乐酷服务风险</a>】<br/>
【用户隐私保护】<br/>
 保护用户个人隐私是乐酷的基本政策。所以，除非遵守国家法律规定在有关国家机关查询时、遵守乐酷产品的特定程序、为维护乐酷的合法权益、被侵害的第三人提出权利主张或在紧急情况下竭力维护用户个人和社会大众的隐私安全的，乐酷不会公开、编辑或透露用户的注册资料及保存在本服务中的任何非公开内容。<br/>
 乐酷会竭尽全力保护用户的信息，但乐酷不能确信或保证任何个人信息的安全性，用户须自己承担风险。<br/>
【乐酷拥有对本协议服务条款的解释权】<br/>
 用户对服务之任何部分或本用户协议的任何意见及建议可通过乐酷管理员与乐酷联系。<br/>
<%if(loginUser==null){%><a href="/register.jsp">注册</a>|<a href="/user/login.jsp">登陆</a><br/><%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>