<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");



Calendar cal = Calendar.getInstance();
int currentHour = cal.get(Calendar.HOUR_OF_DAY);
int weekday = cal.get(Calendar.DAY_OF_WEEK);

if(currentHour == 5) {
DbOperation dbOp = new DbOperation(true);
try{
	
	dbOp.executeQuery("select @bank:=floor((a+ifnull(e,0))/100000000) ,@stock:=floor(b/100000000) ,@fund:=floor(c/100000000) ,@auction:=floor(ifnull(d,0)/100000000) ,@cash:=floor((f+ifnull(g,0)+ifnull(h,0))/100000000) ,@total:=floor((a+b+c+ifnull(d,0)+ifnull(e,0)+f+ifnull(g,0)+ifnull(h,0))/100000000) ,@log_id:=id from (select sum(money) a from jc_bank_store_money) da , (select sum(money+money_f) b from stock_account) de, (select sum(fund) c from jc_tong) db, (select sum(current_price)  d from jc_auction where mark=0 and right_user_id>0) dc, (select sum(wager) e from wgame_pk_big where mark=1) dd,(select sum(game_point) f from user_status where game_point>=1000000)df,(select sum(wager) g from wgame_pk where mark=1)dg,(select sum(wager) h from wgame_pk_fight where mark=1)dh,(select id from jc_bank_log order by id desc limit 1)idh");
	dbOp.executeUpdate("insert into mcoolhis.bank_stat (`time`,bank,stock,fund,auction,cash,total,log_id) values(now(),@bank,@stock,@fund,@auction,@cash,@total,@log_id)");
	
	StringBuilder sb = new StringBuilder(128);
	long sum = 0;
	for(int i = 1;i < 20;i++) {
		sb.append(",v");
		sb.append(i);
		sb.append('=');
		sb.append(UserInfoUtil.getMoneyStat()[i] / 10000);
		sum += UserInfoUtil.getMoneyStat()[i] / 10000;
	}
	sb.append(",total");
	sb.append('=');
	sb.append(sum);
	dbOp.executeUpdate("insert into mcoolhis.bank_stat2 set time=now()" + sb.toString());
	UserInfoUtil.clearMoneyStat();
	
} catch(Exception e){
	dbOp.release();
}
}


if(currentHour == 4) {
try{
	net.joycool.wap.action.job.HuntAction.countMap.resetCount();
	net.joycool.wap.action.tong.TongAction.countMap.resetCount();
	net.joycool.wap.action.tong.TongAction.countMap2.resetCount();
	net.joycool.wap.action.job.fish.FishAction.countMap.resetCount();
	net.joycool.wap.action.job.CardAction.countMap.resetCount();
	CountMaps.countMap1.resetCount();
	CountMaps.countMap2.resetCount();
	CountMaps.countMap3.resetCount();
	
	jc.family.game.yard.YardAction.adjustPrice();
} catch(Exception e){}
}


if (weekday == 1) {
	if(currentHour == 4) {
	try{
		DbOperation db = new DbOperation(5);
		// 种子 生产统计
		db.executeUpdate("truncate table fm_yard_user_log2");
		db.executeUpdate("insert into fm_yard_user_log2 select * from fm_yard_user_log");
		db.executeUpdate("truncate table fm_yard_user_log");
		
		db.executeUpdate("update fm_user set alive=floor(alive * 0.9)");
		// 活跃点扣除
		db.release();
		jc.family.FamilyAction.familyUserCache.clear();
	} catch(Exception e){}
	}
}

%>