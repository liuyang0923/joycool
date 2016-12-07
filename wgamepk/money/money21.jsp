<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "select id from user_earn_money where user_id = "
                + loginUser.getId()
                + " and (TO_DAYS(now()) - TO_DAYS(create_datetime) = 0) and type = 2";
		System.out.println(query);
        ResultSet rs = dbOp.executeQuery(query);
        boolean flag = false;
        if (rs.next()) {
            flag = true;
        } else {
            query = "update user_status set game_point = (game_point + 10000) where user_id = "
                    + loginUser.getId();
            dbOp.executeUpdate(query);
            query = "insert into user_earn_money set user_id = "
                    + loginUser.getId() + ", create_datetime = now(), type = 2";
            dbOp.executeUpdate(query);
        }
        dbOp.release();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="喂小狗">
<p align="center">喂小狗</p>
<p align="left">
<%
if(flag) {
%>
周星星：恩人哪，俺的旺财您今天已经喂过了，它已经吃饱了，吃太多会太胖的。您明天再来吧，谢谢啦。<br/>
<%
} else {
%>
周星星：好人啊！您的大恩大德我替旺财谢谢您了！我一定会再拍出很多让您笑破肚皮的电影！<br/>
您得到10000乐币。<br/>
<%
}
%>
<br/>
<a href="/wgamepk/money/moneyIndex.jsp">返回赚钱首页</a><br/>
<a href="/wgame/hall.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>