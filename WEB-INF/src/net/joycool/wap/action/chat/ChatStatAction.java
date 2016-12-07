package net.joycool.wap.action.chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.joycool.wap.bean.chat.ChatStatBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatStatService;
import net.joycool.wap.util.db.DbOperation;

public class ChatStatAction {
	/**
	 * 后台统计，每天1点钟执行
	 * 
	 */
	public static void hourTask() {
		try {
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			if (hour == 3) {
				dayTask();
			}
		} catch (Exception ex) {

		}
	}

	// 每天3点，对前一天的聊天进行统计
	public static void dayTask() {
		
		//liuyi 2006-11-13 去掉聊天统计功能 start
		if(true)return;
		//liuyi 2006-11-13 去掉聊天统计功能 end
		
		int userCount = 0;
		int sendCount = 0;
		int sendToPerson = 0;
		int sendFriend = 0;
		int noReply = 0;
		int reply = 0;
		int userTotal = 0;
		int friendTotal = 0;
		int chatMessage = 0;
		int chatPerson = 0;
		int actionMessage = 0;
		int actionPerson = 0;
		int messageMessage = 0;
		int messagePerson = 0;
		int pkMessage = 0;
		int pkPerson = 0;
		// 数据库操作类
		DbOperation dbOp = null;
		ResultSet rs = null;
		String query = null;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		c.add(Calendar.DAY_OF_YEAR, -1);
		String endDate = df.format(c.getTime());
		IChatStatService chatStatService = ServiceFactory
				.createChatStatService();
		chatStatService.deleteChatStat(" create_datetime='" + endDate + "'");
		try {
			dbOp = new DbOperation();
			dbOp.init();
			// 构建查询语句
			// 2级以上的用户数和发送信息数聊天
			query = "select count(distinct from_id) oid,sum(message_count)CO FROM (select from_id,count(from_id) message_count FROM(select from_id "
					+ "from jc_room_content where from_id!=0 and from_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1) as a  group BY a.from_id  )as b";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				sendCount = rs.getInt("CO");
				chatMessage = rs.getInt("CO");
				chatPerson = rs.getInt("oid");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
		} finally {
			dbOp.release();
		}
		try {
			dbOp = new DbOperation();
			dbOp.init();
			// 构建查询语句
			// 2级以上的用户数和发送人数动作
			query = "select count(distinct from_id) oid,sum(message_count)CO FROM (select from_id,count(from_id) message_count FROM(select from_id "
					+ "from jc_action_record where from_id!=0 and from_id in (SELECT user_id from user_status where rank>2) and to_days(action_datetime)=to_days(NOW())-1) as a  group BY a.from_id  )as b";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				actionMessage = rs.getInt("CO");
				actionPerson = rs.getInt("oid");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		try {
			dbOp = new DbOperation();
			dbOp.init();
			// 2级以上的用户数和发送信息数PK
			query = "select count(distinct left_user_id) oid,sum(message_count)CO FROM (select left_user_id,count(left_user_id) message_count FROM(select left_user_id "
					+

					"from wgame_pk where left_user_id!=0 and left_user_id in (SELECT user_id from user_status where rank>2) and to_days(start_datetime)=to_days(NOW())-1) as a  group BY a.left_user_id  )as b";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				sendCount = sendCount + rs.getInt("CO");
				pkMessage = rs.getInt("CO");
				pkPerson = rs.getInt("oid");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
		}
		// 释放资源
		finally {
			dbOp.release();
		}

		try {
			dbOp = new DbOperation();
			dbOp.init();

			// 2级以上的用户数和发送信息数 信件
			query = "select count(distinct from_user_id) oid,sum(message_count)CO FROM (select from_user_id,count(from_user_id) message_count FROM(select from_user_id "
					+

					"from user_message where from_user_id!=0 and from_user_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1) as a  group BY a.from_user_id  )as b";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				sendCount = sendCount + rs.getInt("CO");
				messageMessage = rs.getInt("CO");
				messagePerson = rs.getInt("oid");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
		}
		// 释放资源
		finally {
			dbOp.release();
		}

		try {
			dbOp = new DbOperation();
			dbOp.init();

			// 2级以上的用户数和发送人数
			query = "SELECT COUNT( DISTINCT b.from_id )fromperson ,COUNT(b.to_id) toperson FROM((select from_id,  to_id   FROM(select DISTINCT from_id,  to_id  "
					+

					"from jc_room_content where from_id!=0 and from_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime) "
					+

					"=to_days(NOW())-1) as a )  union (select from_user_id,  to_user_id   FROM(select DISTINCT from_user_id,  to_user_id  "
					+

					"from user_message where from_user_id!=0 and from_user_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime) "
					+

					"=to_days(NOW())-1) as a  ) union (select left_user_id,  right_user_id   FROM(select DISTINCT left_user_id,  right_user_id  "
					+

					"from wgame_pk where left_user_id!=0 and left_user_id in (SELECT user_id from user_status where rank>2) and to_days(start_datetime) "
					+

					"=to_days(NOW())-1) as a  ) )as b";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				sendToPerson = rs.getInt("toperson");
				userCount = rs.getInt("fromperson");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
		}
		// 释放资源
		finally {
			dbOp.release();
		}
		// 多少是好友
		// 数据库操作类
		try {
			dbOp = new DbOperation();
			dbOp.init();
			// 构建查询语句2级以上的用户数和发送好友数聊天
			query = "SELECT COUNT( DISTINCT b.from_id )fromperson ,COUNT(b.to_id) toperson FROM((select "
					+ " distinct from_id, to_id  from jc_room_content where from_id!=0 and to_id IN(SELECT friend_id from user_friend where user_id=from_id ) "
					+ "and from_id in "
					+ "(SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1 )union(select distinct "
					+ "from_user_id, to_user_id  "
					+ "from user_message where from_user_id!=0 and to_user_id IN(SELECT friend_id from user_friend where user_id=from_user_id )and from_user_id in "
					+

					"(SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1)union(select DISTINCT left_user_id, right_user_id  from wgame_pk where left_user_id!=0 and right_user_id IN(SELECT friend_id from user_friend where user_id=left_user_id )and left_user_id in (SELECT user_id from user_status where rank>2) and to_days(start_datetime)=to_days(NOW())-1))as b";
			// 查询
			rs = dbOp.executeQuery(query);

			if (rs.next()) {
				sendFriend = rs.getInt("toperson");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
		}
		// 释放资源
		finally {
			dbOp.release();
		}

		// 多少人回复
		// 数据库操作类
		try {
			dbOp = new DbOperation();
			dbOp.init();

			query = "SELECT COUNT( DISTINCT b.from_user_id )toperson ,COUNT(b.to_user_id) fromperson FROM((select distinct from_user_id,to_user_id from user_message  where send_datetime>(select  send_datetime from user_message  where  "
					+ "to_user_id!=0 and from_user_id!=0 and from_user_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1 order BY send_datetime LIMIT 0,1) and 	"
					+ "from_user_id IN( select to_user_id  from "
					+ "user_message where "
					+ "to_user_id!=0 and from_user_id!=0 and from_user_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1 ) )union(select distinct left_user_id,right_user_id from wgame_pk  where start_datetime>(select  start_datetime from wgame_pk where "
					+ " right_user_id!=0 and left_user_id!=0 and left_user_id in (SELECT user_id from user_status where rank>2) and to_days(start_datetime)=to_days(NOW())-1 order BY start_datetime LIMIT 0,1)  and "
					+ "left_user_id IN( select right_user_id  from "
					+ "wgame_pk where "
					+ "right_user_id!=0 and left_user_id!=0 and left_user_id in (SELECT user_id from user_status where rank>2) and to_days(start_datetime)=to_days(NOW())-1 ) )union(select distinct from_id ,to_id from jc_room_content  where send_datetime>(select  send_datetime from jc_room_content where "
					+ " to_id!=0 and from_id!=0 and from_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1 order BY send_datetime LIMIT 0,1) and from_id "
					+ " IN( select to_id  from jc_room_content where "
					+ "to_id!=0 and from_id!=0 and from_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1) ))AS b";
			// 查询
			rs = dbOp.executeQuery(query);

			if (rs.next()) {
				reply = rs.getInt("fromperson");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
		}
		// 释放资源
		finally {
			dbOp.release();
		}

		// 数据库操作类
		try {
			dbOp = new DbOperation();
			dbOp.init();

			// 构建查询语句用户及好友个数
			query = "select count(id) friendnum ,COUNT(distinct user_id) usernum from user_friend  where user_id!=0 and user_id in (SELECT "
					+ "user_id from user_status where rank>2)   ";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				userTotal = rs.getInt("usernum");
				friendTotal = rs.getInt("friendnum");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
		}
		// 释放资源
		finally {
			dbOp.release();
		}
		noReply = sendToPerson - reply;
		if (noReply < 0)
			noReply = 0;
		float perChat = 0;
		float perPk = 0;
		float perAction = 0;
		float perMessage = 0;
		float perSend = 0;
		float perPerson = 0;
		float perFriend = 0;
		float perNoReply = 0;
		float perFriendNum = 0;
	
			

		if (userTotal != 0)
			{
			perFriendNum = (float) friendTotal / userTotal;
			perSend = (float) sendCount / userTotal;
			perPerson = (float) sendToPerson / userTotal;
			perFriend = (float) sendFriend / userTotal;
			perNoReply = (float) noReply / userTotal;
			perChat = (float) chatMessage / userTotal;
			perAction = (float) actionMessage / userTotal;
			perPk = (float) pkMessage / userTotal;
			perMessage = (float) messageMessage / userTotal;
			}
	
			
		 NumberFormat   numFormat   =   NumberFormat.getNumberInstance();   
         numFormat.setMaximumFractionDigits(2);   
         perFriendNum   =  Float.parseFloat(numFormat.format(perFriendNum));   
         perSend   =  Float.parseFloat(numFormat.format(perSend));   
         perPerson   =  Float.parseFloat(numFormat.format(perPerson));   
         perFriend   =  Float.parseFloat(numFormat.format(perFriend));   
         perNoReply   =  Float.parseFloat(numFormat.format(perNoReply));   
         perChat   =  Float.parseFloat(numFormat.format(perChat));   
         perAction   =  Float.parseFloat(numFormat.format(perAction));   
         perPk   =  Float.parseFloat(numFormat.format(perPk));   
         perMessage   =  Float.parseFloat(numFormat.format(perMessage));   

		ChatStatBean chatStat = new ChatStatBean();
		chatStat.setFriendNum(perFriendNum);
		chatStat.setNoReply(perNoReply);
		chatStat.setSendFriend(perFriend);
		chatStat.setSendNum(perSend);
		chatStat.setSendToPerson(perPerson);
		chatStat.setUserCount(userCount);
		chatStat.setUserTotal(userTotal);
		chatStat.setActionNum(perAction);
		chatStat.setChatNum(perChat);
		chatStat.setPkNum(perPk);
		chatStat.setMessageNum(perMessage);
		chatStatService.addChatStat(chatStat);

	}

}
