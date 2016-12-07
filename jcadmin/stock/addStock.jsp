<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.job.*,net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean,net.joycool.wap.bean.stock.*"%><%

String sql = "insert into jc_stock(id, name, introduction, price) values(5,'乐酷聊天','乐酷最火爆的聊天场所，天天爆满！',50)";
SqlUtil.executeUpdate(sql, Constants.DBShortName);

sql = "insert into jc_stock(id, name, introduction, price) values(6,'狩猎公园','乐酷最火爆的人机游戏！',60)";
SqlUtil.executeUpdate(sql, Constants.DBShortName);

sql = "insert into jc_stock(id, name, introduction, price) values(7,'电子书城','精彩好书，更新不断，新书上架时股价同步上扬！',60)";
SqlUtil.executeUpdate(sql, Constants.DBShortName);
%>
done！