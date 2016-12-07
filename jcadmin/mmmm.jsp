<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.bean.chat.*,
                 net.joycool.wap.action.chat.*,
                 java.text.SimpleDateFormat,
                 java.util.Calendar,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*"%><%!
public void dayTask() {
System.out.println("dayTask()");
		int userCount = 0;
		int sendCount = 0;
		int sendToPerson = 0;
		int sendFriend = 0;
		int noReply = 0;
		int reply = 0;
		int userTotal = 0;
		int friendTotal = 0;
		// 数据库操作类
		DbOperation dbOp = null;
		ResultSet rs = null;
		String query = null;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = df.format(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, -1);
		String endDate = df.format(c.getTime());
		IChatStatService chatStatService = ServiceFactory
				.createChatStatService();
		chatStatService.deleteChatStat(" create_datetime='" + endDate + "'");
		// ArrayList array=new ArrayList();
		// //查询2级以上用户列表
		// try {
		// dbOp = new DbOperation();
		// dbOp.init();
		// // 构建查询语句
		// // 2级以上的用户数和发送信息数聊天
		// query = "SELECT user_id from user_status where rank>2";
		// // 查询
		// rs = dbOp.executeQuery(query);
		// System.out.println("000" + query);
		// if (rs.next()) {
		// array.add(rs.getString("user_id"));
		// }
		// System.out.println(array);
		// } catch (SQLException e) {
		// dbOp.release();
		// e.printStackTrace(System.out);
		// } finally {
		// dbOp.release();
		// }
		//		
		// String user=array.toString().substring(1,array.size()-1);
		try {
			dbOp = new DbOperation();
			dbOp.init();
			// 构建查询语句
			// 2级以上的用户数和发送信息数聊天
			query = "select count(distinct from_id) oid,sum(message_count)CO FROM (select from_id,count(from_id) message_count FROM(select from_id "
					+

					"from jc_room_content where from_id!=0 and from_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1) as a  group BY a.from_id  )as b";
			System.out.println(query);
			// 查询
			rs = dbOp.executeQuery(query);
			System.out.println("000" + query);
			if (rs.next()) {
				userCount = rs.getInt("oid");
				sendCount = rs.getInt("CO");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace(System.out);
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
				userCount = userCount + rs.getInt("oid");
				sendCount = sendCount + rs.getInt("CO");
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
				userCount = userCount + rs.getInt("oid");
				sendCount = sendCount + rs.getInt("CO");
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
			// 构建查询语句
			// 2级以上的用户数和发送人数聊天
			query = "select sum(message_count)CO FROM (select count(DISTINCT to_id) message_count FROM(select "
					+

					"from_id, to_id  "
					+

					"from jc_room_content where from_id!=0 and from_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1) as a  group BY a.from_id  )as b";
			System.out.println(query);
			// 查询
			rs = dbOp.executeQuery(query);
			System.out.println("000" + query);
			if (rs.next()) {
				sendToPerson = rs.getInt("CO");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace(System.out);
		} finally {
			dbOp.release();
		}

		try {
			dbOp = new DbOperation();
			dbOp.init();
			// 2级以上的用户数和发送人数PK
			query = "select sum(message_count)CO FROM (select left_user_id,count(DISTINCT right_user_id) message_count FROM(select "
					+

					"left_user_id, right_user_id  "
					+

					"from wgame_pk where left_user_id!=0 and left_user_id in (SELECT user_id from user_status where rank>2) and to_days(start_datetime)=to_days(NOW())-1) as a  group BY a.left_user_id  )as b";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				sendToPerson = sendToPerson + rs.getInt("CO");
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

			// 2级以上的用户数和发送人数 信件
			query = "select sum(message_count)CO FROM (select from_user_id,count(DISTINCT to_user_id) message_count FROM(select "
					+

					"from_user_id, to_user_id  "
					+

					"from user_message where from_user_id!=0 and from_user_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1) as a  group BY a.from_user_id  )as b";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				sendToPerson = sendToPerson + rs.getInt("CO");
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
			query = "select count(distinct from_id) oid,sum(message_count) CO FROM (select from_id,count(DISTINCT to_id) message_count FROM(select "
					+ "from_id, to_id  from jc_room_content where from_id!=0 and to_id IN(SELECT friend_id from user_friend where user_id=from_id ) "
					+ "and from_id in "
					+ "(SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1) as a  group BY a.from_id  )as b";
			// 查询
			rs = dbOp.executeQuery(query);

			if (rs.next()) {
				sendFriend = rs.getInt("CO");
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
			// 构建查询语句2级以上的用户数和发送好友数PK
			query = "select count(distinct left_user_id) oid,sum(message_count) CO FROM (select left_user_id,count(DISTINCT right_user_id) message_count FROM(select left_user_id, right_user_id  from wgame_pk where left_user_id!=0 and right_user_id IN(SELECT friend_id from user_friend where user_id=left_user_id )and left_user_id in (SELECT user_id from user_status where rank>2) and to_days(start_datetime)=to_days(NOW())-1) as a  group BY a.left_user_id  )as b";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				sendFriend = sendFriend + rs.getInt("CO");
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
			// 构建查询语句2级以上的用户数和发送好友数信件
			query = "select count(distinct from_user_id) oid,sum(message_count) CO FROM (select from_user_id,count(DISTINCT to_user_id) message_count FROM(select "
					+

					"from_user_id, to_user_id  "
					+

					"from user_message where from_user_id!=0 and to_user_id IN(SELECT friend_id from user_friend where user_id=from_user_id )and from_user_id in "
					+

					"(SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1) as a  group BY a.from_user_id  )as b";

			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				sendFriend = sendFriend + rs.getInt("CO");
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
			// 聊天
			query = "select COUNT(from_id) reply  FROM(select distinct from_id from jc_room_content  where send_datetime>(to_days(NOW())-1) and from_id "
					+

					" IN( select to_id  from jc_room_content where "
					+

					" to_id!=0 and from_id!=0 and from_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1 ) )as a";
			// 查询
			rs = dbOp.executeQuery(query);

			if (rs.next()) {
				reply = rs.getInt("reply");
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
			// 构建查询语句2级以上的用户数和发送好友数PK
			query = "select COUNT(left_user_id) reply FROM(select distinct left_user_id from wgame_pk  where start_datetime>(to_days(NOW())-1) and "
					+

					"left_user_id IN( select right_user_id  from "
					+

					"wgame_pk where "
					+

					"right_user_id!=0 and left_user_id!=0 and left_user_id in (SELECT user_id from user_status where rank>2) and to_days(start_datetime)=to_days(NOW())-1 ) )as a";
			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				reply = reply + rs.getInt("reply");
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
			// 构建查询语句2级以上的用户数和发送好友数信件
			query = " select COUNT(from_user_id) reply FROM(select distinct from_user_id from user_message  where send_datetime>(to_days(NOW())-1) and "
					+

					"from_user_id IN( select to_user_id  from "
					+

					"user_message where "
					+

					"to_user_id!=0 and from_user_id!=0 and from_user_id in (SELECT user_id from user_status where rank>2) and to_days(send_datetime)=to_days(NOW())-1 ) )as a";

			// 查询
			rs = dbOp.executeQuery(query);
			if (rs.next()) {
				reply = reply + rs.getInt("reply");
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
		float perSend = 0;
		float perPerson = 0;
		float perFriend = 0;
		float perNoReply = 0;
		float perFriendNum = 0;
		if (userCount != 0) {
			perSend = (float) sendCount / userCount;
			perPerson = (float) sendToPerson / userCount;
			perFriend = (float) sendFriend / userCount;
			perNoReply = (float) noReply / userCount;
		}
		if (userTotal != 0)
			perFriendNum = (float) friendTotal / userTotal;

		ChatStatBean chatStat = new ChatStatBean();
		chatStat.setFriendNum(perFriendNum);
		chatStat.setNoReply(perNoReply);
		chatStat.setSendFriend(perFriend);
		chatStat.setSendNum(perSend);
		chatStat.setSendToPerson(perPerson);
		chatStat.setUserCount(userCount);
		chatStat.setUserTotal(userTotal);
		chatStatService.addChatStat(chatStat);


	
	}

%>                 
<%
dayTask();
%>                 