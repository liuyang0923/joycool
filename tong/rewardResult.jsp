<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%TongAction action=new TongAction(request);
int reward = action.getParameterInt("reward");
if(reward > 100000000) {
	int tongId = action.getParameterInt("tongId");
	request.setAttribute("tong", net.joycool.wap.cache.util.TongCacheUtil.getTong(tongId));
	request.setAttribute("tip", "新人奖最多不能超过1亿");
} else
	action.rewardResult(request);
TongBean tong=(TongBean)request.getAttribute("tong");
String tip=(String)request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(tong!=null){%>
<card title="修改结果" ontimer="<%=response.encodeURL("rewardManage.jsp?tongId="+tong.getId())%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tip!=null){%>
<%=tip%>
<%}%><br/>
<a href="rewardManage.jsp?tongId=<%=tong.getId()%>">直接跳转 </a> <br/>
<a href="tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a> <a href="tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="修改结果">
<p align="left">
<%=BaseAction.getTop(request, response)%>
参数错误！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>