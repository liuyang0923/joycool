<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamepk.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "select id from user_earn_money where user_id = "
                + loginUser.getId()
                + " and (TO_DAYS(now()) - TO_DAYS(create_datetime) = 0) and type = 1";
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
                    + loginUser.getId() + ", create_datetime = now(), type = 1";
            dbOp.executeUpdate(query);
        }
        dbOp.release();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="灭蟑螂">
<p align="center">灭蟑螂</p>
<p align="left">
<%
if(flag) {
%>
周星星：您今天已经踩过俺的小强一回了，还想踩？不好吧，明天再来吧。<br/>
<%
} else {
%>
周星星：呜呜呜，跟我相依为命的小强兄弟呀，就这样被一个猪头踩死了！天啊！<br/>
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