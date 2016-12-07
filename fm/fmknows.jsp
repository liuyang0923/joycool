<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族须知"><p align="left"><%=BaseAction.getTop(request, response)%>
家族是家人、朋友们一起聊天、灌水、游戏和娱乐的组织，以和谐、和睦为要素，与家人一起分享快乐，分担痛苦，承担责任，是需要大家一起维护的一个家！<br/>
1.建立家族条件:等级大于15级,社交值达到500,花费1亿乐币且需要邀请至少4个人一起参与申请.<br/>
加入家族条件:等级到达3级就可申请加入家族.<br/>
2.家族等级:家族人数上限由家族等级决定,家族等级越高,家族可容纳的族员人数越多.升级家族等级需要满足相应游戏经验值要求且花费一定数量的乐币.<br/>
3.家族管理:家族族长可对家族进行管理,同时可将部分管理权限下放给族员.具有管理权限的家族成员,将会显示在家族首页的家族官员页中.<br/>
4.家族活动:家族活动包含多个趣味竞技游戏,需要家族成员齐心协力,共同努力,才能击败其他家族,从而获得奖励~(每个游戏的规则说明可通过家族活动中相应游戏页面进行查看)<br/>
5.家族功勋:主要用于记录家族在排行榜或家族活动中取得的优秀成绩.<br/>
个人功勋:家族成员通过参加家族活动获得的个人功勋,将在此记录.<br/>
6.禁止同时使用多个号参与家族活动中各个游戏.情节严重的,管理员会根据情况给予不同程度的封禁权限处理.<br/>
7.家族禁止买卖和交易.<br/>
8.和乐酷的其他游戏一样,家族功能不允许使用任何形式的外挂,否则将给予严厉的惩罚.对于玩家使用外挂的处理办法,请参考<a href="/admin/proto.jsp?jaLineId=10309">乐酷用户协议和游戏规则总则</a><br/>
<a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>