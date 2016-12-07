<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%TongAction action=new TongAction(request);
action.revenueManage(request);
TongBean tong=(TongBean)request.getAttribute("tong");
String revenue=(String)request.getAttribute("revenue");
String donation=(String)request.getAttribute("donation");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="税收管理">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
目前帮会税收系数为：<%=tong.getRate()%>％<br/>
帮会目前资金<%=tong.getFund()%><br/>
帮会历史累计资金：<%=tong.getFundTotal()%><br/>
其中帮会会员捐款：
<%if(donation!=null){%>
<%=donation%>
<%}%><br/>
昨日税务收益：<br/>
当铺
<%if(revenue!=null){%>
<%=revenue%>
<%}%><br/>
服装店(待上线)<br/>
药房(待上线)<br/>
铁匠铺(待上线)<br/>
要更改税收吗？<br/>
税收<input name="revenue"  maxlength="3" value="" title="名称"/>％ （只能正整数）<br/>
<anchor title="确定">确定
    <go href="/tong/revenueResult.jsp?tongId=<%=tong.getId()%>" method="post">
    <postfield name="revenue" value="$revenue"/>
    </go>
</anchor>  <br/>
<%}else{%>
您无权管理该帮会税收！<br/>
<%}%>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a> <a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>