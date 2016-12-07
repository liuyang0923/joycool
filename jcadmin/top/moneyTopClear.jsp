<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.util.LoadResource" %><%
response.setHeader("Cache-Control","no-cache");
DbOperation dbOp = new DbOperation();
dbOp.init();
dbOp.startTransaction();
// 删除表内容
String query = "TRUNCATE TABLE jc_money_top";
// 执行更新 
dbOp.executeUpdate(query);
%>1<br/><%
// 初始化表id
query = "alter table jc_money_top auto_increment=0";
// 执行更新 
dbOp.executeUpdate(query);
%>2<br/><%
// 插入用户乐币数据
//maq_2006-10-12_更新财富排名规则判断乐币大于10000的用户_start
query = "insert into jc_money_top(game_point,user_id) select game_point,user_id  from user_status where game_point>10000";
//maq_2006-10-12_更新财富排名规则判断乐币大于10000的用户_end
// 执行更新 
dbOp.executeUpdate(query);
%>3<br/><%
// 更新用户存款数据
query = "update jc_money_top a,jc_bank_store_money b set a.bank_store=b.money where a.user_id=b.user_id";
// 执行更新 
dbOp.executeUpdate(query);
%>4<br/><%
// 更新用户贷款数据
//maq_2006-10-12_更新财富排名规则不算贷款_start
//query = "update jc_money_top a,(select sum(money) money,user_id from jc_bank_load_money group by user_id ) b set a.bank_load=b.money where a.user_id=b.user_id";
// 执行更新 
//dbOp.executeUpdate(query);
//maq_2006-10-12_更新财富排名规则不算贷款_end
%>5<br/><%
// 更新用户乐币总计数据
//maq_2006-10-12_更新财富排名规则_start
//query = "update jc_money_top set money_total=game_point+bank_store-bank_load,create_datetime=now()";
query = "update jc_money_top set money_total=game_point+bank_store,create_datetime=now()";
//maq_2006-10-12_更新财富排名规则_end
// 执行更新 
dbOp.executeUpdate(query);
dbOp.commitTransaction();
// 释放资源
dbOp.release();
LoadResource.clearMoneyTopList();
LoadResource.clearRankTopList();
%>
ok 