<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游戏规则"><p align="left"><%=BaseAction.getTop(request, response)%>
1.家族问答活动开始后已通过审核的成员会收到提示链接,点击链接即可进入.家族活动页面同时会设有相应链接.<br/>
2.活动采用以家族整体计分,初始为0分,每位家族成员最多可答30题.每答对一题加2分,答错扣1分.当用户结束答题后,显示答题统计并计入家族总分中.<br/>
3.单道题答题时间为15秒,若在该时间内未完成答题,按答错计.若活动结束时未完成答题,所剩题目将按答错计算.<br/>
4.胜利奖励:第一名,获得全部报名费的50%.第二名获得30%.第三名获得15%.5%作为乐酷税收.<br/>
5.家族答对一定数量的题,可以获得相应的家族经验哟~<br/>
<a href="index.jsp">返回家族问答</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>