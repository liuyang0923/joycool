<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><% 

	response.setHeader("Cache-Control","no-cache");

 %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="数独"><p>
<%=BaseAction.getTop(request, response)%>
在9*9的格中给出一定的已知数字，利用逻辑和推理，在其他的空格上填入1-9的数字。使1-9每个数字在每一行、每一列和每一宫中都只出现一次。<br/>
<a href="shudu.jsp?ano=yes&amp;lvl=1">简单</a>需花费1游币<br/>
<a href="shudu.jsp?ano=yes&amp;lvl=2">中等</a>需花费1游币<br/>
<a href="shudu.jsp?ano=yes&amp;lvl=3">困难</a>需花费2游币<br/>
<a href="shudu.jsp?ano=yes&amp;lvl=4">终极</a>需花费3游币<br/>
<a href="index.jsp">返回数独</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>