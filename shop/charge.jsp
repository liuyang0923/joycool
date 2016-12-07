<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.spec.shop.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="充值"><p>
<%=BaseAction.getTop(request, response)%>
请您输入充值卡密码:<br/><input name="pwd"/><br/>
请选择充值卡的数额<br/>
<select name="s">
<option value="10"><%=10 %>(<%=10 %><%=ShopUtil.GOLD_NAME %>)</option>
<option value="10"><%=30 %>(<%=30 %><%=ShopUtil.GOLD_NAME %>)</option>
<option value="10"><%=50 %>(<%=50 %><%=ShopUtil.GOLD_NAME %>)</option>
<option value="10"><%=100 %>(<%=100 %><%=ShopUtil.GOLD_NAME %>)</option>
</select>
<a href="index.jsp">&lt;&lt;返回商城首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>