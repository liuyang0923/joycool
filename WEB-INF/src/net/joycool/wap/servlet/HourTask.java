/*
 * Created on 2006-6-27
 * 
 * 李北金_2006_6_27，每小时运行一次的任务。
 */
package net.joycool.wap.servlet;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;

import jc.family.game.GameAction;
import jc.match.MatchAction;

import net.joycool.wap.action.auction.AuctionAction;
import net.joycool.wap.action.auction.LuckyAction;
import net.joycool.wap.action.bank.BankAction;
import net.joycool.wap.action.chat.JCRoomChatAction;
import net.joycool.wap.action.friend.FriendAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.job.HappyCardAction;
import net.joycool.wap.action.job.LuckAction;
import net.joycool.wap.action.lhc.LhcWorld;
import net.joycool.wap.action.pet.StakeAction;
import net.joycool.wap.action.stock2.StockService;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.auction.AuctionBean;
import net.joycool.wap.bean.auction.AuctionHistoryBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.beginner.BeginnerHelpBean;
import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.friend.FriendMarriageBean;
import net.joycool.wap.bean.job.HuntQuarryAppearRateBean;
import net.joycool.wap.bean.job.HuntQuarryBean;
import net.joycool.wap.bean.job.HuntTaskBean;
import net.joycool.wap.bean.question.QuestionService;
import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.cache.*;
import net.joycool.wap.cache.util.*;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.FloorServiceImpl;
import net.joycool.wap.service.infc.*;
import net.joycool.wap.spec.castle.CastleAction;
import net.joycool.wap.util.*;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class HourTask extends TimerTask {
	INoticeService noticeService = ServiceFactory.createNoticeService();

	IChatService chatService = ServiceFactory.createChatService();

	IJobService jobService = ServiceFactory.createJobService();

	IUserService userService = ServiceFactory.createUserService();

	IHomeService homeService = ServiceFactory.createHomeService();

	//IStockService stockService = ServiceFactory.createStockService();

	IFriendService friendService = ServiceFactory.createFriendService();

	IBeginnerService beginnerService = ServiceFactory.createBeginnerService();

	IJcForumService jcForumService = ServiceFactory.createJcForumService();

	IDummyService dummyService = ServiceFactory.createDummyService();

	IPKService pkService = ServiceFactory.createPKService();

	StockService stock2Service = new StockService();

	ITongService tongService = ServiceFactory.createTongService();

	// liq_2007-02-13_每天0点准时清空当日排行_start
	QuestionService questionService = ServiceFactory.createQuestionService();

	// liq_2007-02-13_每天0点准时清空当日排行_end
	int ROOM_WAITING = 2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		LogUtil.logTime("HourTask***start");

		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int weekday = cal.get(Calendar.DAY_OF_WEEK);

		try {
			// liuyi 2006-11-30 乐酷程序排查 start

			// 各小时在线LOG
			LogUtil.logOnline(currentHour + ":"
					+ JoycoolSpecialUtil.getRealOnlineUserCount(null));
			// 各小时在线LOG
			// 删除用户邀请聊天表数据

			long startTime = System.currentTimeMillis();
			try {	//每天晚上8点六合彩开奖，并删除5天以外用户下注数据
				if (currentHour == 20) {
					LhcWorld.task();
					String sql = "delete from lhc_wager_record where to_days(now()) - to_days(create_datetime)>10";
					SqlUtil.executeUpdate(sql, Constants.DBShortName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {	//	长东西出来给他们拣
				LuckyAction.addPickup();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (currentHour == 0) {
				// mcq_2007-5-8_每天0点准时更新新版本股票收盘价格_start
				stock2Service.updateStock("end_price=price,count=0", " 1=1");
				// mcq_2007-5-8_每天0点准时更新新版本股票收盘价格_start

				// mcq_2007-5-8_每天0点准时更新pk系统杀人数据_start
				pkService.updatePKUser("old_kcount=kcount", " 1=1");
				pkService.updatePKUser("kcount=0", " 1=1");
				// mcq_2007-5-8_每天0点准时更新pk系统杀人数据_start

				IChatService chatService = ServiceFactory.createChatService();
				chatService.delRoomHallInvite("1=1");

				// mcq_2006-10-19_每天零点清空当日家园点击率_start

				// macq_2006-12-20_增加家园的缓存_start
				HomeCacheUtil.updateHomeCahce("hits=0", "1=1");
				// homeService.updateHomeUser("hits=0", "1=1");
				// macq_2006-12-20_增加家园的缓存_end
				// mcq_2006-10-19_每天零点清空当日家园点击率_end
				// // mcq_2006-9-19_每天零点的时候更新用户个人家园访问次数保存历史记录并清空缓存组_start
				// IHomeService homeService =
				// ServiceFactory.createHomeService();
				// Vector homeHitsList = homeService.getHomeUserList("1=1");
				// HomeUserBean homeUser = null;
				// HomeHitsBean homeHits = null;
				// // 循环更新所有房间的访问次数,并保留昨夜数据
				// for (int i = 0; i < homeHitsList.size(); i++) {
				// homeUser = (HomeUserBean) homeHitsList.get(i);
				// homeHits = new HomeHitsBean();
				// homeHits.setUserId(homeUser.getUserId());
				// homeHits.setHits(homeUser.getHits());
				// // 添加昨日访问次数历史数据
				// boolean flag = homeService.addHomeHits(homeHits);
				// // 更新成功
				// if (flag) {
				// // 清空当前家园访问次数
				// homeService.updateHomeUser("hits=0", "user_id="
				// + homeUser.getUserId());
				// }
				// }
				// // 更新homeHits的缓存组
				// HomeHitsCacheUtil.flushHomeHitsGroup();
				// // mcq_2006-9-19_每天零点的时候更新用户个人家园访问次数保存历史记录并清空缓存组_end
			}
			long endTime = System.currentTimeMillis();
			LogUtil.logTime("homeService.updateHomeUser*****"
					+ (endTime - startTime));
			startTime = endTime;
			// mcq_2006-6-28_判断聊天室快到期时发送通知给用户_start
			if (currentHour == 15) {
				String condition = "to_days(expire_datetime) - to_days(now()) = 1 and pay_way = 1";
				Vector roomList = chatService.getJCRoomList(condition);
				if (roomList != null) {
					JCRoomBean room = null;
					for (int i = 0; i < roomList.size(); i++) {
						room = (JCRoomBean) roomList.get(i);
						// 加入消息系统
						NoticeBean notice = new NoticeBean();
						notice.setUserId(room.getCreatorId());
						notice.setTitle("请续费您的" + room.getName() + "聊天房间");
						notice.setContent("");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setHideUrl("");
						notice.setLink("/chat/hall.jsp?roomId=" + room.getId());
						noticeService.addNotice(notice);
						// mcq_2006-6-26_增加一条聊天记录_start
						// liuyi 2006-09-16 聊天室加缓存 start
						JCRoomContentBean jcRoomContent = new JCRoomContentBean();
						jcRoomContent.setFromId(room.getCreatorId());
						jcRoomContent.setToId(0);
						jcRoomContent.setFromNickName("");
						jcRoomContent.setToNickName("");
						jcRoomContent.setContent("号外：" + room.getName()
								+ "聊天室租赁时间即将到期");
						jcRoomContent.setAttach("");
						jcRoomContent.setIsPrivate(0);
						jcRoomContent.setRoomId(room.getId());
						jcRoomContent.setSecRoomId(-1);
						jcRoomContent.setMark(2);
						ServiceFactory.createChatService().addContent(
								jcRoomContent);
						// ServiceFactory.createChatService().addContent(
						// room.getCreatorId() + ",0,'','','号外："
						// + room.getName()
						// + "聊天室租赁时间即将到期','',now(),0,"
						// + room.getId() + ",-1,2");
						// liuyi 2006-09-16 聊天室加缓存 end
						// 清空聊天室在map中的记录
						// RoomCacheUtil.flushRoomContentId(room.getId());
						// 清空聊天室在map中的记录
						// mcq_2006-6-26_增加一条聊天记录_end
					}
				}
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("聊天室快到期时发送通知*****" + (endTime - startTime));
			startTime = endTime;
			// mcq_2006-6-28_判断聊天室快到期时发送通知给用户_end

			// mcq_2006-6-28_明天0点准时清空用户登录记录表_start
			if (currentHour == 0) {
				userService.delDaysLoginUse(" 1=1");
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("0点准时清空用户登录记录*****" + (endTime - startTime));
			startTime = endTime;
			// mcq_2006-6-28_明天0点准时清空用户登录记录表_end

			if (currentHour == 0) {
				//guip_2007-08-30_每天0点准时清空踩踩乐当日排行
				FloorServiceImpl.deleteFloorTop("1=1");
				// liq_2007-02-13_每天0点准时清空当日排行(数据库)_start
				questionService.setNullToday();
				// liq_2007-02-13_每天0点准时清空当日排行(数据库)_end

				// liq_2007-02-12_每天0点准时清空历史大排名缓存_start
				OsCacheUtil
						.flushGroup(OsCacheUtil.GAME_QUESTION_TOTAL_CACHE_GROUP);
				// liq_2007-02-12_每天0点准时清空历史大排名缓存_end

				// 书库页面主页缓存时间liq_2007-03-19随机选择5种图书,每种图书选一本
				OsCacheUtil.flushGroup(OsCacheUtil.EBOOK_CACHE_GROUP_PAGE);

				// 书库页面主页缓存时间liq_2007-03-19在最新上传的80本书中随机选取5本
				OsCacheUtil.flushGroup(OsCacheUtil.EBOOK_CACHE_GROUP_PAGE_5);
			}

			// mcq_2006-6-28_判断聊天室过期时发送通知给室主_start
			if (currentHour == 15) {
				String condition = "to_days(now()) - to_days(expire_datetime)=0 and pay_way = 1";
				Vector roomList = chatService.getJCRoomList(condition);
				if (roomList != null) {
					JCRoomBean room = null;
					for (int i = 0; i < roomList.size(); i++) {
						room = (JCRoomBean) roomList.get(i);
						// 加入消息系统
						NoticeBean notice = new NoticeBean();
						notice.setUserId(room.getCreatorId());
						notice.setTitle("您的房间因欠费已经被关闭");
						notice.setContent("");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setHideUrl("");
						notice.setLink("/chat/payment.jsp?roomId=" + room.getId());
						noticeService.addNotice(notice);
						// mcq_2006-6-28_更新房间状态_start
						condition = "status=" + ROOM_WAITING;
						chatService.updateJCRoom(condition, "id="
								+ room.getId());
						// mcq_2006-6-28_更新房间状态_end
					}
				}
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("聊天室过期时发送通知给室主*****" + (endTime - startTime));
			startTime = endTime;

			// macq_2007-1-3_3点下降城墙最高耐久度_start
			if (currentHour == 3) {
				try {
					tongCityResult();
				} catch (Exception e) { e.printStackTrace(); }
			}
			// macq_2007-1-3_3点下降城墙最高耐久度_start

			// mcq_2006-6-28_判断聊天室过期时发送通知给室主_end
			// 给对已经建立了交友资料的老用户，没有上传照片的由系统自动随机给其分配一个卡通头像
			if (currentHour == 5) {
				FriendAction.cartoonSend();
				// macq_2007-1-3_更新申请进入帮会24内无人响应的用户申请状态_start
				tongApplyResult();
				// macq_2007-1-3_更新申请进入帮会24内无人响应的用户申请状态_start
			}

			// 更新热心人发言数量
			if (currentHour == 4) {
				try {
					GameAction.inserTodayMatch();
				} catch(Exception e) {e.printStackTrace();}
				// liuyi 2006-12-02 删除过期的pk游戏 start
				SqlUtil.executeUpdate("delete from wgame_pk where to_days(now()) - to_days(start_datetime)>3 or to_days(now()) - to_days(start_datetime)>2 and mark=1");
				SqlUtil.executeUpdate("delete from wgame_hall where to_days(now()) - to_days(start_datetime)>3");
				// liuyi 2006-12-02 删除过期的pk游戏 end

				int sendCount = 0;
				int receiveCount = 0;
				// 获取所有热心人列表
				Vector beginnerHelpList = beginnerService
						.getBeginnerHelpList("1=1");
				BeginnerHelpBean beginnerHelp = null;
				for (int i = 0; i < beginnerHelpList.size(); i++) {
					beginnerHelp = (BeginnerHelpBean) beginnerHelpList.get(i);
					// 排除3个管理员
					if (beginnerHelp.getUserId() == 431
							|| beginnerHelp.getUserId() == 519610
							|| beginnerHelp.getUserId() == 914727)
						continue;
					DbOperation dbOp = new DbOperation();
					dbOp.init();
					// 构建更新语句(查询热心人对三级一下用户的说话数量)
					String query = "select count(a.id) c_id from jc_room_content a left join user_status b on a.to_id=b.user_id where a.from_id="
							+ beginnerHelp.getUserId() + " and b.rank=0";
					ResultSet rs = dbOp.executeQuery(query);
					try {
						if (rs.next()) {
							sendCount = rs.getInt("c_id");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					// 构建更新语句(查询三级一下用户对热心人的说话数量)
					query = "select count(a.id) c_id from jc_room_content a left join user_status b on a.from_id=b.user_id "
							+ "where a.to_id="
							+ beginnerHelp.getUserId()
							+ " and b.rank=0";
					rs = dbOp.executeQuery(query);
					try {
						if (rs.next()) {
							receiveCount = rs.getInt("c_id");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					// 构建更新语句(更新热心人对三级以下用户的发言数量)
					if (sendCount > 0) {
						query = "UPDATE jc_beginner_help SET send_count="
								+ sendCount + " WHERE user_id="
								+ beginnerHelp.getUserId();
						// 执行更新
						dbOp.executeUpdate(query);
					}
					// 构建更新语句(更新三级以下用户对热心人的发言数量)
					if (receiveCount > 0) {
						query = "UPDATE jc_beginner_help SET receive_count="
								+ receiveCount + " WHERE user_id="
								+ beginnerHelp.getUserId();
						// 执行更新
						dbOp.executeUpdate(query);
					}
					// 释放资源
					dbOp.release();
				}
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("更新热心人发言数量*****" + (endTime - startTime));
			startTime = endTime;
			// mcq_2006-6-28_更新财富记录并清空财富记录缓存和等级排名缓存_start
			if (currentHour == 5) {
				// macq_2006-11-22_更新所有在交友中心照片有投票的用户投票数量加一_start
				friendService.updateFriendVote("count=count+1", "1=1");
				// macq_2006-11-22_更新所有在交友中心照片有投票的用户投票数量加一_start
				DbOperation dbOp = new DbOperation(true);
				// 删除表内容
				String query = "TRUNCATE TABLE jc_money_top";
				// 执行更新
				dbOp.executeUpdate(query);
				// 初始化表id
				query = "alter table jc_money_top auto_increment=0";
				// 执行更新
				dbOp.executeUpdate(query);
				// 插入用户乐币数据
				// maq_2006-10-12_更新财富排名规则判断乐币大于10000的用户_start
				// liuyi 2006-12-02 程序优化 start
				// query = "insert into jc_money_top(game_point,user_id) select
				// game_point,user_id from user_status where game_point>10000";
				query = "insert into jc_money_top(game_point,user_id) (select game_point,user_id  from user_status where game_point>100000 order by game_point desc limit 0,10000)";
				// liuyi 2006-12-02 程序优化 end
				// maq_2006-10-12_更新财富排名规则判断乐币大于10000的用户_end
				// 执行更新
				dbOp.executeUpdate(query);
				// 更新用户存款数据
				query = "update jc_money_top a,jc_bank_store_money b set a.bank_store=b.money where a.user_id=b.user_id";
				// 执行更新
				dbOp.executeUpdate(query);
				// 更新用户贷款数据
				// maq_2006-10-12_更新财富排名规则不算贷款_start
				// query = "update jc_money_top a,(select sum(money)
				// money,user_id from jc_bank_load_money group by user_id ) b
				// set a.bank_load=b.money where a.user_id=b.user_id";
				// 执行更新
				// dbOp.executeUpdate(query);
				// maq_2006-10-12_更新财富排名规则不算贷款_end
				// 更新用户乐币总计数据
				// maq_2006-10-12_更新财富排名规则不算贷款_start
				// query = "update jc_money_top set
				// money_total=game_point+bank_store-bank_load,create_datetime=now()";
				query = "update jc_money_top set money_total=game_point+bank_store,create_datetime=now()";
				// maq_2006-10-12_更新财富排名规则不算贷款_end
				// 执行更新
				dbOp.executeUpdate(query);
				// 释放资源
				dbOp.executeUpdate("delete from jc_user_top where mark=3 or mark=4 or mark=5");
				
				dbOp.executeUpdate("insert into jc_user_top(priority,user_id,mark)(select social,user_id,3 from user_status order by social desc limit 1000)");
				dbOp.executeUpdate("insert into jc_user_top(priority,user_id,mark)(select spirit,user_id,4 from user_status order by spirit desc limit 1000)");
				dbOp.executeUpdate("insert into jc_user_top(priority,user_id,mark)(select charitarian,user_id,5 from user_status order by charitarian desc limit 1000)");
				
				dbOp.release();
				LoadResource.clearMoneyTopList();
				LoadResource.clearRankTopList();
				LoadResource.clearSocialTopList();
				LoadResource.clearSpiritTopList();
				//LoadResource.clearStockTopList();
				LoadResource.clearFriendPhotoManTopList();
				LoadResource.clearFriendPhotoWomanTopList();
				LoadResource.clearCharitarianTopList();
				LoadResource.clearPKUserKTopList();
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("更新财富记录*****" + (endTime - startTime));
			startTime = endTime;
			// mcq_2006-6-28_更新财富记录_end

			if (currentHour == 0) {
				// 每天清空一次pv总数
				CountUtil.reset();
				JoycoolSessionListener.maxSessionCount = JoycoolSpecialUtil
						.getRealOnlineUserCount(null);
				// 每天清空一次pv总数
				// zhul_2006-08-29 start 每天0点开始新一天的运势
				LuckAction.clearLuckMap();
				// zhul_2006-08-29 end 每天0点开始新一天的运势
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("清空一次pv总数*****" + (endTime - startTime));
			startTime = endTime;

			// 每天凌晨5点，清空三天前的聊天记录和通知, 删除用户行囊中使用次数等于0的物品
			if (currentHour == 5) {
				SqlUtil.executeUpdate("DELETE FROM jc_tong_notice WHERE to_days(now()) - to_days(create_datetime) > 2");
				SqlUtil.executeUpdate("DELETE FROM jc_notice WHERE to_days(now()) - to_days(create_datetime) > 2");
				String condition = "from_id != 431 and to_id != 431 and from_id != 519610 and to_id != 519610 and from_id != 914727 and to_id != 914727 and to_days(now()) - to_days(send_datetime) > 2";
				chatService.deleteContent(condition);
				// liuyi 2006-09-16 聊天室加缓存 start
				OsCacheUtil.flushAll();
				// liuyi 2006-09-16 聊天室加缓存 end
				// mcq_2007-3-12_清空所有聊天室的所有记录id_start
				RoomCacheUtil.flushRoomContentIdByALL();
				// mcq_2007-3-12_清空所有聊天室的所有记录id_end
				// macq_2006-12-20_删除用户行囊中使用次数等于0的物品_start
				String update = "delete from user_bag where time<=0 or end_datetime<now()";
				SqlUtil.executeUpdate(update);
				CacheManage.userBagList.clear();
				CacheManage.userBagMap.clear();
				// macq_2006-12-20_删除用户行囊中使用次数等于0的物品_start
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("清空三天前的聊天记录*****" + (endTime - startTime));
			startTime = endTime;

			// add by zhangyi 2006-07-26 每天凌晨1点,向每日流量统计表中插前一天统计数据
			if (currentHour == 1) {
				// 计算银行前一天的现金流
				IBankService bankService = ServiceFactory.createBankService();
				String date = DateUtil.formatDate(DateUtil.rollDate(-1));
//				bankService.addMoneyFlux(date);

				// liuyi 2006-12-02 删除过期的银行操作记录 start
				bankService.deleteMoneyLog("to_days(now()) - to_days(time)>10 and money between -100000000 and 100000000 or to_days(now()) - to_days(time)>20 and type=7 or to_days(now()) - to_days(time)>60 and money between -5000000000 and 5000000000");
				// liuyi 2006-12-02 删除过期的银行操作记录 end

//				MoneyAction moneyAction = new MoneyAction();
//				// 删除前10天的流量统计录
//				moneyAction.delMoneyFlowRecord();
//				// 向每日流量统计表中插记录
//				moneyAction.addMoneyFlux();
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("计算银行前一天的现金流*****" + (endTime - startTime));
			startTime = endTime;

			// zhul 2006-09-20 每天1点，对用户家园进行重新排名 start
			// zhouj 产生家园采访车
			if (currentHour == 1) {
				HomeCacheUtil.resetStarUser();
				// HomeAction.homeOrder = homeService.getHomeOrder();
				// HomeAction.hitsOrder = null;
				// 每天产生一位今日之星
				// macq_2006-12-20_增加家园的缓存_start
				// TODO 这块肯定慢
//				HomeCacheUtil.updateHomeCahce("mark=0", "mark=1");
				// homeService.updateHomeUser("mark=0", "mark=1");
				// macq_2006-12-20_增加家园的缓存_end
//				HomeUserBean homeUser = homeService
//						.getHomeUser("0=0 order by rand()");
				// macq_2006-12-20_增加家园的缓存_start
//				HomeCacheUtil.updateHomeCacheById("mark=1", "id="
//						+ homeUser.getId(), homeUser.getId());
				// homeService.updateHomeUser("mark=1", "id=" +
				// homeUser.getId());
				// macq_2006-12-20_增加家园的缓存_end
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("计算银行前一天的现金流*****" + (endTime - startTime));
			startTime = endTime;
			// zhul 2006-09-20 每天1点，对用户家园进行重新排名 end
			// liuyi 2006-11-16 股市10点更新程序放到jsp start
			// liuyi 2006-11-16 股市10点更新程序放到jsp end
			// fanys 2006-08-04 start
			BankAction.hourTask();
			endTime = System.currentTimeMillis();
			LogUtil.logTime("BankAction.hourTask*****" + (endTime - startTime));
			startTime = endTime;
			// fanys 2006-08-04 end
			// fanys 2006-08-17 start 邀请好友来乐酷
			JCRoomChatAction.hourTask();
			endTime = System.currentTimeMillis();
			LogUtil.logTime("JCRoomChatAction.hourTask*****"
					+ (endTime - startTime));
			startTime = endTime;
			// fanys 2006-08-17 end
			// fanys 2006-09-21 start 贺卡统计
			HappyCardAction.hourTask();
			endTime = System.currentTimeMillis();
			LogUtil.logTime("HappyCardAction.hourTask*****"
					+ (endTime - startTime));
			startTime = endTime;
			// fanys2006-09-21end

			// wucx 2006-10-10 start 家园的点击数每小时随即加0到10
//			HomeAction.setRandomHits();
			// 4点用户家园一周（7天）之内没有新日记或新照片增加，则第8日起家园房间变为“无人管理”状态

			if (currentHour == 4) {
				// HomeAction.weekTask();
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("HomeAction.setRandomHits*****"
					+ (endTime - startTime));
			startTime = endTime;
			// wucx2006-10-101end

			// wucx 2006-10-22 start 数据统计－－每天聊天的统计
			// liuyi 2006-11-13 去掉聊天统计功能 start
			// ChatStatAction.hourTask();
			// liuyi 2006-11-13 去掉聊天统计功能 end
			// wucx-10-22end数据统计－－每天聊天的统计

			// 判断结婚开始的时候向所有聊天室发送公告
			marriageSendMsg();
			endTime = System.currentTimeMillis();
			LogUtil.logTime("marriageSendMsg*****" + (endTime - startTime));
			startTime = endTime;

			// wucx 2006-10-26 start 72小时未回复，视为拒绝
			FriendAction.replyNoAnswer();
			endTime = System.currentTimeMillis();
			LogUtil.logTime("FriendAction.replyNoAnswer*****"
					+ (endTime - startTime));
			startTime = endTime;
			// wucx-10-26end72小时未回复，视为拒绝

			// wucx 2006-10-26 start 红包宾客统计
			FriendAction.statGuestAndBag();
			endTime = System.currentTimeMillis();
			LogUtil.logTime("FriendAction.statGuestAndBag*****"
					+ (endTime - startTime));
			startTime = endTime;
			// wucx-10-26end红包宾客统计

			// macq_12-18_拍卖系统计算_start
			try {
				auctionStatus();
			} catch (Exception e) {	e.printStackTrace();}
			
			endTime = System.currentTimeMillis();
			LogUtil.logTime("auctionStatus*****" + (endTime - startTime));
			startTime = endTime;
			// macq_12-18_拍卖系统计算_end

			// liuyi 2006-10-29 结义请求过期处理 start
			FriendAction.hourTask();
			endTime = System.currentTimeMillis();
			LogUtil.logTime("FriendAction.hourTask*****"
					+ (endTime - startTime));
			startTime = endTime;
			// liuyi 2006-10-29 结义请求过期处理 end

			// wucx 2006-11-15 start 股票交易记录保持前10条，其余删除
			if (currentHour == 5)
				//StockAction.updateStockDeal();
			endTime = System.currentTimeMillis();
			LogUtil.logTime("StockAction.updateStockDeal*****"
					+ (endTime - startTime));
			startTime = endTime;
			// wucx-11-15end股票交易记录保持前10条，其余删除

			// liuyi 2007-01-23 生日贺卡功能 start
			// if(currentHour==6){
			// FriendAction.sendBirthdayCard();
			// }
			// endTime = System.currentTimeMillis();
			// LogUtil.logTime("FriendAction.sendBirthdayCard*****"
			// + (endTime - startTime));
			// startTime = endTime;
			// liuyi 2007-01-23 生日贺卡功能 end

			// zhul_2006-08-17 add for hunt_timer_task start
			// 获取在当前小时要启动的任务并启动
			Vector startTask = jobService
					.getHuntTaskList(" TO_DAYS(start_time)=TO_DAYS(NOW()) AND HOUR(start_time)="
							+ currentHour);
			for (int i = 0; i < startTask.size(); i++) {
				HuntTaskBean huntTask = (HuntTaskBean) startTask.get(i);
				this.startHuntTask(huntTask);
			}
			// 获取在当前小时要取消的任务并取消。
			Vector stopTask = jobService
					.getHuntTaskList("TO_DAYS(end_time)=TO_DAYS(NOW()) AND HOUR(end_time)="
							+ currentHour);
			for (int i = 0; i < stopTask.size(); i++) {
				HuntTaskBean huntTask = (HuntTaskBean) stopTask.get(i);
				this.stopHuntTask(huntTask);
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("HuntTask*****" + (endTime - startTime));
			startTime = endTime;
			// zhul_2006-08-17 add for hunt_timer_task end
			// zhul 2006-10-11 初始化乐客日记相册的日点击率为0 start
			if (currentHour == 0) {
				homeService.updateHomeDiary("daily_hits=0", "0=0");
				homeService.updateHomePhoto("daily_hits=0", "0=0");
			}
			endTime = System.currentTimeMillis();
			LogUtil.logTime("homeService.updateHomeDiary*****"
					+ (endTime - startTime));
			// zhul 2006-10-11 初始化乐客日记相册的日点击率为0 end

			// liuyi 2006-11-30 乐酷程序排查 end
			// wucx2006-12-13乐酷论坛日新贴出书为0start
			if (currentHour == 0) {
				jcForumService.updateForum("today_count=0", "0=0");
				ForumCacheUtil.deleteForumList();
			}
			// wucx2006-12-13乐酷论坛日新贴出书为0end

			// liuyi 2007-02-08 清除城墙破坏记录 start
			if (currentHour == 18) {
				String sql = "TRUNCATE TABLE jc_tong_city_record";
				SqlUtil.executeUpdate(sql, Constants.DBShortName);
			}
			// liuyi 2007-02-08 清除城墙破坏记录 end
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {	// 每天晚12点撤所有单
			if (currentHour == 0) {
				net.joycool.wap.action.stock2.StockAction.timeTask();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {	// 删除5天以外的浮生最近高分记录
			if (currentHour == 1) {
				String sql = "update fs_top set recent_high_score=0 where recent_high_score!=0 and create_datetime<curdate()-interval 5 day";
				SqlUtil.executeUpdate(sql, Constants.DBShortName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {	// 计算所有股市帐号的总资产
			String sql = "update stock_account c set asset=ifnull((select sum(a.count*b.price) from stock_cc a,stock b where a.user_id=c.user_id and a.stock_id=b.id),0)+c.money+c.money_f";
			SqlUtil.executeUpdate(sql, Constants.DBShortName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 每天早上4点删除论坛1天以外的被保留删除的帖子
			if (currentHour == 4) {
				ForbidUtil.task();
				// 处理荣誉排名
				if (weekday == 1) {		// 星期天凌晨四点计算
					if(NO_DEC_HONOR > 0)
						NO_DEC_HONOR--;
					else
						UserInfoUtil.weekUpdateHonor();

					UserInfoUtil.refreshUserInterval(0); // 每周间隔的数据刷新
					// 更新帮会本周仓库和上周仓库等等
					SqlUtil.executeUpdate("update jc_tong set depot=depot+depot_week,depot_last=depot_week,depot_week=0");
					TongCacheUtil.flushTongAll();
					// 更新桃花源荣誉
					SqlUtil.executeUpdate("update farm_user_honor set honor=honor+honor_week,honor_last=honor_week,honor_week=0", 4);
					SqlUtil.executeUpdate("update farm_user_honor set honor_high=honor_last where honor_high < honor_last", 4);		// 一周最高
					CacheManage.farmUserHonor.clear();
				}
				CastleAction.dayTask(weekday);
				// 删除过期的贺卡信息
				SqlUtil.executeUpdate("delete FROM jc_happy_card_send where to_days(now()) - to_days(send_datetime) > 20");
				// 把今日战绩加到总战绩 wgame
				SqlUtil.executeUpdate("update wgame_history0 set win_total=win_total+win_count,draw_total=draw_total+draw_count,lose_total=lose_total+lose_count,win_count=0,draw_count=0,lose_count=0,money_total=money_total+money,money=0 where (win_count>0 or draw_count>0 or lose_count>0)");
				SqlUtil.executeUpdate("update wgame_history0_big set win_total=win_total+win_count,draw_total=draw_total+draw_count,lose_total=lose_total+lose_count,win_count=0,draw_count=0,lose_count=0,money_total=money_total+money,money=0 where (win_count>0 or draw_count>0 or lose_count>0)");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {	//每天早上4点删除论坛1天以外的被保留删除的回复帖子
			if (currentHour == 4) {
				// 删除帖子列表放置到jc_forum_del，何时删除待定
				
				
//				int maxContent = SqlUtil.getIntResult("select max(id) from jc_forum_content", 2);
//				SqlUtil.executeUpdate("delete FROM jc_forum_content where id>" + (maxContent-20000) + " and del_mark = 1 and to_days(now()) - to_days(create_datetime) > 7", 2);
//
//				int maxReply = SqlUtil.getIntResult("select max(id) from jc_forum_reply", 2);
//				SqlUtil.executeUpdate("delete FROM jc_forum_reply where id>" + (maxReply - 200000) + " and del_mark = 1 and to_days(now()) - to_days(create_datetime) > 3", 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {	//每天早上4点删除大富豪5天以外的数据
			if (currentHour == 4) {
				String sql = "delete from wgame_pk_big where wager<5000000000 and wager>-5000000000 and to_days(now()) - to_days(start_datetime)>5";
				SqlUtil.executeUpdate(sql, Constants.DBShortName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try { // 每天早晨1点更新删除宠物比赛当天经验点
			if (currentHour == 1) {
				StakeAction.hourTask();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			//每天零点清空当日的粉丝排行
			if (currentHour == 0) {
				MatchAction.todayFansRank();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.logTime("HourTask***end");
	}

	// 启动打猎任务
	private boolean startHuntTask(HuntTaskBean huntTask) {

		HuntQuarryBean quarry = jobService.getHuntQuarry("name='"
				+ huntTask.getQuarryName() + "'");
		// 如果猎物已经存在，返回启动成功
		if (quarry != null) {
			if (!quarry.getImage().equals(huntTask.getImage())) {
				File file = new File(Constants.HUNT_IMAGE_PATH + "/"
						+ quarry.getImage());
				file.delete();
			}
			jobService.updateHuntQuarry("price=" + huntTask.getPrice()
					+ ",harm_price=" + huntTask.getHarmPrice() + ",hit_point="
					+ huntTask.getHitPoint() + ",image='" + huntTask.getImage()
					+ "'", "id=" + quarry.getId());
			// 更新猎物出现机率
			jobService.updateHuntQuarryAppearRate("appear_rate="
					+ huntTask.getArrow(), "quarry_id=" + quarry.getId()
					+ " and weapon_id=" + Constants.ARROW);
			jobService.updateHuntQuarryAppearRate("appear_rate="
					+ huntTask.getHandGun(), "quarry_id=" + quarry.getId()
					+ " and weapon_id=" + Constants.HANDGUN);
			jobService.updateHuntQuarryAppearRate("appear_rate="
					+ huntTask.getHuntGun(), "quarry_id=" + quarry.getId()
					+ " and weapon_id=" + Constants.HUNTGUN);
			jobService.updateHuntQuarryAppearRate("appear_rate="
					+ huntTask.getAk47(), "quarry_id=" + quarry.getId()
					+ " and weapon_id=" + Constants.AK47);
			//macq_2007-6-27_增加awp
			jobService.updateHuntQuarryAppearRate("appear_rate="
					+ huntTask.getAwp(), "quarry_id=" + quarry.getId()
					+ " and weapon_id=" + Constants.AWP);

		} else {

			// 将新猎物加入数据库
			quarry = new HuntQuarryBean();
			quarry.setName(huntTask.getQuarryName());
			quarry.setPrice(huntTask.getPrice());
			quarry.setHarmPrice(huntTask.getHarmPrice());
			quarry.setHitPoint(huntTask.getHitPoint());
			quarry.setImage(huntTask.getImage());
			if (!jobService.addHuntQuarry(quarry)) {
				return false;
			}
			// 获取猎物id
			quarry = jobService.getHuntQuarry("name='"
					+ huntTask.getQuarryName() + "'");
			// 将猎物对于各种武器出现的机率加入jc_hunt_quarry_appear_rate
			HuntQuarryAppearRateBean appearRate = new HuntQuarryAppearRateBean();
			appearRate.setWeaponId(Constants.ARROW);
			appearRate.setQuarryId(quarry.getId());
			appearRate.setAppearRate(huntTask.getArrow());
			if (!jobService.addHuntQuarryAppearRate(appearRate)) {
				return false;
			}

			appearRate.setWeaponId(Constants.HANDGUN);
			appearRate.setQuarryId(quarry.getId());
			appearRate.setAppearRate(huntTask.getHandGun());
			if (!jobService.addHuntQuarryAppearRate(appearRate)) {
				return false;
			}

			appearRate.setWeaponId(Constants.HUNTGUN);
			appearRate.setQuarryId(quarry.getId());
			appearRate.setAppearRate(huntTask.getHuntGun());
			if (!jobService.addHuntQuarryAppearRate(appearRate)) {
				return false;
			}

			appearRate.setWeaponId(Constants.AK47);
			appearRate.setQuarryId(quarry.getId());
			appearRate.setAppearRate(huntTask.getAk47());
			if (!jobService.addHuntQuarryAppearRate(appearRate)) {
				return false;
			}
			//macq_2007-6-27_增加awp
			appearRate.setWeaponId(Constants.AWP);
			appearRate.setQuarryId(quarry.getId());
			appearRate.setAppearRate(huntTask.getAwp());
			if (!jobService.addHuntQuarryAppearRate(appearRate)) {
				return false;
			}
		}
		// 更新打猎内存
		LoadResource resource = new LoadResource();
		resource.clearArrowMap();
		resource.clearHandGunMap();
		resource.clearHuntGunMap();
		resource.clearAk47Map();
		resource.clearQuarryMap();
		resource.clearWeaponMap();
		resource.clearAWPMap();
		// 添加系统提示消息
		NoticeBean notice = new NoticeBean();
		// notice.setUserId();
		notice.setTitle(huntTask.getNotice());
		notice.setContent("");
		notice.setType(NoticeBean.SYSTEM_NOTICE);
		notice.setHideUrl("");
		notice.setLink("/job/hunt/hunt.jsp");
		ServiceFactory.createNoticeService().addNotice(notice);
		// liuyi 2006-12-26
		OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_GROUP);

		return true;
	}

	// 取消打猎任务
	private boolean stopHuntTask(HuntTaskBean huntTask) {

		HuntQuarryBean quarry = jobService.getHuntQuarry("name='"
				+ huntTask.getQuarryName() + "'");

		if (!jobService.updateHuntQuarryAppearRate("appear_rate=0",
				"quarry_id=" + quarry.getId()))
			return false;
		// 更新打猎内存
		LoadResource resource = new LoadResource();
		resource.clearArrowMap();
		resource.clearHandGunMap();
		resource.clearHuntGunMap();
		resource.clearAk47Map();
		resource.clearAWPMap();
		// 删除系统消息
		ServiceFactory.createNoticeService().deleteNotice(
				"type=" + NoticeBean.SYSTEM_NOTICE + " and title='"
						+ huntTask.getNotice() + "'");

		// 清除对应系统消息id所在缓存组中的记录
		OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_GROUP);
		// 清除对应系统消息id所在缓存组中的记录
		// OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_READED_GROUP);
		// 清除缓存中对应记录
		return true;
	}

	// 开始举行时给每个聊天室发送通知
	private boolean marriageSendMsg() {
		// 从缓存中获取聊天室
		Vector roomCacheList = RoomCacheUtil.getRoomListCache();
		JCRoomBean room = null;
		FriendMarriageBean friendMarriage = null;
		JCRoomContentBean jcRoomContent = null;
		// 获取系统时间
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		// 得到当前结婚时间
		String marriageDatetime = year + "-" + month + "-" + day + " "
				+ currentHour + ":00:00";
		// 得到当前要结婚的信息列表
		Vector friendMarriageList = friendService
				.getFriendMarriageList("mark=0 and marriage_datetime='"
						+ marriageDatetime + "'");
		for (int i = 0; i < friendMarriageList.size(); i++) {
			// 得到一条结婚记录
			friendMarriage = (FriendMarriageBean) friendMarriageList.get(i);
			for (int j = 0; j < roomCacheList.size(); j++) {
				// 取得一个房间
				room = (JCRoomBean) roomCacheList.get(j);
				// 小黑屋不发结婚通知
				if (room.getId() == 1) {
					continue;
				}
				// 发送结婚通知
				jcRoomContent = new JCRoomContentBean();
				jcRoomContent.setFromId(0);
				jcRoomContent.setToId(0);
				jcRoomContent.setFromNickName(friendMarriage.getFromId() + "");
				jcRoomContent.setToNickName(friendMarriage.getToId() + "");
				jcRoomContent.setContent(friendMarriage.getId() + "");
				jcRoomContent.setAttach("");
				jcRoomContent.setIsPrivate(0);
				jcRoomContent.setRoomId(room.getId());
				jcRoomContent.setSecRoomId(-1);
				jcRoomContent.setMark(7);
				chatService.addContent(jcRoomContent);
			}
		}
		return true;
	}

	// 拍卖系统处理拍卖物品8小时内的状态
	private void auctionStatus() {
		// 获取所有拍卖时间大于8小时并没有结束的拍卖记录记录Id
		int maxId = SqlUtil.getIntResult("select max(id) from jc_auction");
		String sql = "SELECT id FROM jc_auction where ADDDATE(create_datetime,interval 8 HOUR)<NOW() and ADDDATE(last_datetime,interval 1 HOUR)<NOW() and mark=0 and id>" + (maxId - 5000);
		List auctionList = SqlUtil.getIntList(sql, Constants.DBShortName);
		Integer auctionId = null;
		AuctionBean auction = null;
		DummyProductBean dummyProduct = null;
		for (int i = 0; i < auctionList.size(); i++) {
			auctionId = (Integer) auctionList.get(i);
			// 获取一条拍卖记录
			auction = AuctionCacheUtil
					.getAuctionCacheById(auctionId.intValue());
			if (auction == null) {
				continue;
			}
			// 获取虚拟物品记录
			dummyProduct = dummyService.getDummyProducts(auction.getProductId());
			// 如果该物品没有被竞拍过,则默认管理员回收该物品,回收价格参见虚拟物品参考价
			synchronized(AuctionAction.getAuctionLock()) {
				if (auction.getRightUserId() == 0) {
					auction.setRightUserId(431);
					auction.setCurrentPrice(dummyProduct.getPrice());
					// 更新拍卖记录(系统回收)
					AuctionCacheUtil.updateActionCacheByUserId("current_price="
							+ dummyProduct.getPrice() + ",mark=1", "id="
							+ auctionId, auction.getRightUserId(), auction.getId());
					// 给拍卖者加钱,收取拍卖金额的10%做为手续费
					//long gamePoint = Math.round(dummyProduct.getPrice() * 0.95);
					long gamePoint = (long)dummyProduct.getPrice() * auction.getTime();	// 价格乘以剩余次数
					// UserInfoUtil.updateUserStatus("game_point=game_point+"
					// + gamePoint, "user_id=" + auction.getLeftUserId(),
					// auction.getLeftUserId(), UserCashAction.OTHERS,
					// "拍卖完成给拍卖用户加钱");
					StoreBean leftUserBankStore = BankCacheUtil
							.getBankStoreCache(auction.getLeftUserId());
					if (leftUserBankStore == null) {// 增加用户存款记录
						leftUserBankStore = new StoreBean();
						leftUserBankStore.setUserId(auction.getLeftUserId());
						leftUserBankStore.setMoney(gamePoint);
						BankCacheUtil.addBankStoreCache(leftUserBankStore);
					} else {// 更新用户存款记录
	//					BankCacheUtil.updateBankStoreCacheById("money=money+"
	//							+ gamePoint, "user_id=" + auction.getLeftUserId(),
	//							auction.getLeftUserId());
						BankCacheUtil.updateBankStoreCacheById(gamePoint,auction.getLeftUserId(),0,Constants.BANK_ACUTION_TYPE);
					}
					UserBagCacheUtil.removeUserBagCache(auction.getUserBagId());
					// 给拍卖发布者发布发送消息
					NoticeBean notice = new NoticeBean();
					notice.setUserId(auction.getLeftUserId());
					notice.setTitle(dummyProduct.getName() + "拍卖失败获"
							+ gamePoint + "乐币");
					notice.setContent("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setHideUrl("");
					notice.setLink("/user/userBag.jsp");
					ServiceFactory.createNoticeService().addNotice(notice);
				} else {
					// 更新拍卖记录
					AuctionCacheUtil.updateActionCacheByUserId("mark=1", "id="
							+ auctionId, auction.getRightUserId(), auction.getId());
					// 添加拍卖历史记录
					AuctionHistoryBean auctionHistory = new AuctionHistoryBean();
					auctionHistory.setAuctionId(auction.getId());
					auctionHistory.setProductId(auction.getProductId());
					auctionHistory.setDummyId(auction.getDummyId());
					auctionHistory.setUserId(auction.getRightUserId());
					auctionHistory.setPrice(auction.getCurrentPrice());
					AuctionCacheUtil.addActionHistory(auctionHistory);
					// 更新用户行囊记录
					if(auction.getUserBagId() == 0) {	// 老数据兼容
						UserBagBean userBag = new UserBagBean();
						userBag.setUserId(auction.getRightUserId());
						userBag.setProductId(auction.getProductId());
						userBag.setTypeId(auction.getDummyId());
						userBag.setTime(auction.getTime());
						userBag.setMark(0);
						UserBagCacheUtil.addUserBagCache(userBag);
					} else {
						UserBagCacheUtil.changeUserBagOwner(auction.getRightUserId(), auction.getUserBagId());
					}
					// 给拍卖者加钱,收取拍卖金额的10%做为手续费
					long gamePoint = AuctionAction.calcSellMoney(auction.getCurrentPrice());
					// UserInfoUtil.updateUserStatus("game_point=game_point+"
					// + game_point, "user_id=" + auction.getLeftUserId(),
					// auction.getLeftUserId(), UserCashAction.OTHERS,
					// "拍卖完成给拍卖用户加钱");
					StoreBean leftUserBankStore = BankCacheUtil
							.getBankStoreCache(auction.getLeftUserId());
					if (leftUserBankStore == null) {// 增加用户存款记录
						leftUserBankStore = new StoreBean();
						leftUserBankStore.setUserId(auction.getLeftUserId());
						leftUserBankStore.setMoney(gamePoint);
						BankCacheUtil.addBankStoreCache(leftUserBankStore);
					} else {// 更新用户存款记录
	//					BankCacheUtil.updateBankStoreCacheById("money=money+"
	//							+ gamePoint, "user_id=" + auction.getLeftUserId(),
	//							auction.getLeftUserId());
						BankCacheUtil.updateBankStoreCacheById(gamePoint,auction.getLeftUserId(),0,Constants.BANK_ACUTION_TYPE);
					}
					// 给拍卖发布者发布发送消息
					NoticeBean notice = new NoticeBean();
					notice.setUserId(auction.getLeftUserId());
					notice.setTitle(dummyProduct.getName() + "拍出获"
							+ gamePoint + "乐币");
					notice.setContent("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setHideUrl("");
					notice.setLink("/user/userBag.jsp");
					ServiceFactory.createNoticeService().addNotice(notice);
					// 给拍卖最终获得者发布发送消息
					notice = new NoticeBean();
					notice.setUserId(auction.getRightUserId());
					notice.setTitle(dummyProduct.getName() + "到手花"
							+ auction.getCurrentPrice() + "乐币");
					notice.setContent("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setHideUrl("");
					notice.setLink("/user/userBag.jsp");
					ServiceFactory.createNoticeService().addNotice(notice);
				}
			}
		}
		OsCacheUtil.flushGroup(OsCacheUtil.USER_AUCTION_BY_ID_CACHE_GROUP, "auctionList");
	}

	// macq_2007-1-3_更新申请进入帮会24内无人响应的用户申请状态_start
	private void tongApplyResult() {
		// 获取所有申请时间大于1天的用户id列表
		String sql = "SELECT user_id from jc_tong_apply where mark=0 and to_days(now())-to_days(create_datetime)>=1";
		List tongUserApplyList = (List) SqlUtil.getIntList(sql,
				Constants.DBShortName);
		if (tongUserApplyList != null) {
			Integer userId = null;
			for (int i = 0; i < tongUserApplyList.size(); i++) {
				userId = (Integer) tongUserApplyList.get(i);
				// 删除用户申请记录
				tongService.delTongApply("user_id=" + userId.intValue());
				// 更新用户信息tong字段
				UserInfoUtil.updateUserStatus("tong=0", "user_id="
						+ userId.intValue(), userId.intValue(),
						UserCashAction.OTHERS, "更新用户帮会属性字段");
			}
		}
	}

	// macq_2007-1-3_更新申请进入帮会24内无人响应的用户申请状态_start

	// macq_2007-1-3_3点下降城墙最高耐久度_start
	public static int NO_DEC_HONOR = 0;	// 这周是否下降荣誉
	public static int NO_DESTROY = 0;
	private void tongCityResult() {
		if(NO_DESTROY > 0) {	// 可以设置晚上不自动扣除能维持的天数
			NO_DESTROY--;
			return;
		}
		List tongList = TongCacheUtil.getTongListById("userCount");
		if (tongList != null) {
			Integer tongId = null;
			TongBean tong = null;
			for (int i = 0; i < tongList.size(); i++) {
				tongId = (Integer) tongList.get(i);
				tong = TongCacheUtil.getTong(tongId.intValue());
				if (tong != null) {
					if (tong.getMark() == 1) {
						// 获取荒城降幅比率
						double ciryRate = Arith.div(Arith.sub(100, 1), 100, 2);
						int nowEndure = (int) Arith.mul(tong.getNowEndure(),
								ciryRate);
						tong.setNowEndure(nowEndure);
						TongCacheUtil.updateTong("now_endure=" + nowEndure,
								"id=" + tong.getId(), tong.getId());
					} else {
						// 获取随机降幅比率
//						double ciryRate = Arith.div(Arith.sub(100, RandomUtil
//								.nextIntNoZero(5)), 100, 2);
//						int nowEndure = (int) Arith.mul(tong.getNowEndure(),
//								ciryRate);
						int nowEndure = (int) (tong.getNowEndure() * 0.975f);
						tong.setNowEndure(nowEndure);	// 减少缓存冲突导致和内存中不匹配
						TongCacheUtil.updateTong("now_endure=" + nowEndure,
								"id=" + tong.getId(), tong.getId());
					}
				}
			}
		}
	}
	// macq_2007-1-3_3点下降城墙最高耐久度_end

}
