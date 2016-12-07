<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%response.setHeader("Cache-Control","no-cache"); %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛等级">
<p align="left">
<%=BaseAction.getTop(request, response)%>
【乐酷论坛等级】<br/>
论坛等级直接与经验值相关,经验值越高,等级越高.<br/>
等级/称号<br/>
R1:樱桃<br/>
R2:葡萄<br/>
R3:草莓<br/>
R4:杨梅<br/>
R5:荔枝<br/>
R6:桔子<br/>
R7:杨桃<br/>
R8:苹果<br/>
R9:芒果<br/>
R10:蟠桃<br/>
R11:柚子<br/>
R12:菠萝<br/>
R13:椰子<br/>
R14:西瓜<br/>
R15:菠萝蜜<br/>
<br/>
<a href="index.jsp">返回论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
