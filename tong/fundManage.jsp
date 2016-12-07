<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.fundManage(request);
TongBean tong=(TongBean)request.getAttribute("tong");
String count=(String)request.getAttribute("count");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="取走资金">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
目前新人奖金为:<%=tong.getReward()%> <br/>
目前帮会税收系数为：<%=tong.getRate()%>％ <br/>
帮会目前资金<%=tong.getFund()%> <br/>
帮会历史累计资金：<%=tong.getFundTotal()%> <br/>
其中您取走的累计：<%if(count!=null){%><%=count%><%}%> <br/>
今天要取走多少 <br/>
 <input name="fund" maxlength="10" value="" title="名称"/><br/>
<anchor title="确定">确定
    <go href="/tong/fundResult.jsp" method="post">
    <postfield name="fund" value="$fund"/>
    <postfield name="tongId" value="<%=tong.getId()%>"/>
    </go>
</anchor>  <br/>
<%--liuyi 2007-01-16 帮会修改 start --%>
<a href="/tong/rewardManage.jsp?tongId=<%=tong.getId()%>">改变新人奖金</a><br/>
<%--liuyi 2007-01-16 帮会修改 end --%>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a> <a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<%}else{%>
您无权取该帮会基金！<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>