/*
 * 创建日期 2006-5-6 作者李北金。
 */
package net.joycool.wap.servlet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.action.wgamehall.GameDataAction;
import net.joycool.wap.action.wgamepk.PKBaseAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.auction.AuctionBean;
import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.job.JCLotteryGuessBean;
import net.joycool.wap.bean.job.JCLotteryHistoryBean;
import net.joycool.wap.bean.job.JCLotteryNumberBean;
import net.joycool.wap.bean.wgame.CardBean;
import net.joycool.wap.bean.wgame.Cards;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.bean.wgame.WGamePKBean;
import net.joycool.wap.bean.wgamehall.FootballDataBean;
import net.joycool.wap.bean.wgamehall.GoBangDataBean;
import net.joycool.wap.bean.wgamehall.HallBean;
import net.joycool.wap.bean.wgamehall.JinhuaDataBean;
import net.joycool.wap.bean.wgamehall.OthelloDataBean;
import net.joycool.wap.bean.wgamehall.WGameHallBean;
import net.joycool.wap.cache.util.AuctionCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.service.infc.IWGameFightService;
import net.joycool.wap.service.infc.IWGameHallService;
import net.joycool.wap.service.infc.IWGamePKService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class WGameThread extends Thread {
	public int winWager = 100;

	public int TIME_OUT = 180;

	public int COUNT = 100;

	public static boolean lotteryFlag = false;

	public void run() {
		IChatService chatService = ServiceFactory.createChatService();
		IWGamePKService pkService = ServiceFactory.createWGamePKService();
		IWGameFightService fightService = ServiceFactory.createWGameFightService();
		IUserService userService = ServiceFactory.createUserService();
		IWGameHallService hallService = ServiceFactory.createWGameHallService();
		INoticeService noticeService = ServiceFactory.createNoticeService();
		IJobService jobService = ServiceFactory.createJobService();
		// IHomeService homeService = ServiceFactory.createHomeService();
		// 清空在线用户
		SqlUtil.executeUpdate("truncate table jc_room_online");
		SqlUtil.executeUpdate("truncate table jc_online_user");

		Vector roomList = chatService.getJCRoomList(" 1=1");
		JCRoomBean room = null;
		for (int i = 0; i < roomList.size(); i++) {
			room = (JCRoomBean) roomList.get(i);
			chatService.updateJCRoom("current_online_count=0", "id="
					+ room.getId());
		}
		// mcq_2006-9-20_加载家园图片到缓存中_start
		LoadResource.getHomImageSmallFileMap();
		LoadResource.getHomImageBigFileMap();
		// mcq_2006-9-20_加载家园图片到缓存中_end

		String condition = null;
		List pkList = null;
		Vector hallList = null;
		Iterator itr = null;
		WGamePKBean pk = null;
		WGameHallBean hall = null;
		GoBangDataBean gobangData = null;
		FootballDataBean footballData = null;
		JinhuaDataBean jinhuaData = null;
		OthelloDataBean othelloData = null;
		JCLotteryNumberBean jcLotteryNumber = null;
		// JCLotteryNumberBean jcLotteryNumber1 = null;
		JCLotteryGuessBean jcLotteryGuess = null;
		JCLotteryHistoryBean jcLotteryHistory = null;
		Vector jcLotteryGuessList = null;
		int maxNumber = 0;
		// int maxNumber1 = 0;
		int lastDate = 0;
		int today = 0;
		int hour = 0;
		int index = 0;

		// int lastLogHour = -1;
		// boolean hasLogged = false;
		// int lastLogDate = -1;
		// boolean dateHasLogged = false;

		// zhul 2006-09-29家园日记当前最大记录号
		// int maxDiaryId = 0;
		// zhul 2006-09-29家园相册当前最大记录号
		// int maxPhotoId = 0;

		while (true) {
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				return;
			}
			try {
				LogUtil.logTime("WGameThread*****start");
				int minute = getCurrentMinute();
				/**
				 * 猜数字游戏。
				 */
				long startTime = System.currentTimeMillis();

				Calendar ca = Calendar.getInstance();
				hour = ca.get(Calendar.HOUR_OF_DAY);
				today = ca.get(Calendar.DATE);
				if (lastDate != today && hour == 20) {
					WGameThread.lotteryFlag = true;
				}
				if (WGameThread.lotteryFlag) {
					lotteryFlag = false;
					jcLotteryNumber = new JCLotteryNumberBean();
					maxNumber = jobService.getMaxJCLotteryNumber();
					maxNumber = maxNumber > 0 ? maxNumber + 1 : 1;
					// 随机产生开奖数字
					index = RandomUtil.nextInt(100) + 1;
					// set信息到bean中
					jcLotteryNumber.setGuessId(maxNumber);
					jcLotteryNumber.setNumber(index);
					jcLotteryNumber.setLeftWager(0);
					// 添加开奖信息
					jobService.addJCLotteryNumber(jcLotteryNumber);
					// 查询刚刚添加得中奖信息
					jcLotteryGuessList = jobService
							.getJCLotteryGuessList("number=" + index
									+ " and guess_id=" + maxNumber);
					itr = jcLotteryGuessList.iterator();
					// 得到该期总奖金数
					long count1 = jobService.getSumWager("guess_id="
							+ maxNumber);
					long count5 = jobService.getSumWager("guess_id="
							+ maxNumber + " and number=" + index);
					long count3 = jobService
							.getJCLotteryNumberCount("guess_id="
									+ (maxNumber - 1));
					long count4 = count1 + count3;
					// 得到中奖人数
					int count2 = jcLotteryGuessList.size();
					// 判断是否有中奖者
					if (count2 > 0) {
						while (itr.hasNext()) {
							// 得到每个具体得中奖人
							jcLotteryGuess = (JCLotteryGuessBean) itr.next();
							jcLotteryHistory = new JCLotteryHistoryBean();
							// 分奖公式(中奖人下注 / 所有人中奖人下注总和) * 总奖金
							long a = jcLotteryGuess.getWager();
							long totalcount = (long)(((double)a / count5) * count4);
							// set数据到bean中
							jcLotteryHistory.setGuessId(jcLotteryGuess
									.getGuessId());
							jcLotteryHistory.setGuessNumber(jcLotteryGuess
									.getNumber());
							jcLotteryHistory.setUserId(jcLotteryGuess
									.getUserId());
							jcLotteryHistory.setWager(totalcount);
							// 添加中奖用户到历史记录中
							boolean flag = jobService
									.addJCLotteryHistory(jcLotteryHistory);
							if (flag) {
								// 更新中奖用户乐币
								UserStatusBean us = getUserStatus(jcLotteryHistory
										.getUserId());
								UserInfoUtil.updateUserCash(jcLotteryHistory.getUserId(), totalcount,
										UserCashAction.GAME, "用户中奖加乐币"
												+ totalcount);

//								MoneyAction.addMoneyFlowRecord(
//										Constants.LOTTERY, totalcount,
//										Constants.PLUS, us.getUserId());
								
								// 加入消息系统
								NoticeBean notice = new NoticeBean();
								notice.setUserId(us.getUserId());
								notice.setTitle("恭喜您在乐透彩票游戏中赢得大奖!");
								notice.setContent("恭喜您在第" + maxNumber
										+ "期乐透彩票游戏中赢得" +  totalcount
										+ "乐币!本期开奖数字是" + index + ",总奖金"
										+ count4 + "乐币,你下注"
										+ jcLotteryGuess.getWager() + "乐币.");
								notice.setType(NoticeBean.GENERAL_NOTICE);
								notice.setHideUrl("");
								notice.setLink("");
								noticeService.addNotice(notice);
							}
						}
					} else {
						jobService
								.updateJCLotteryNumber("left_wager=" + count4,
										"guess_id=" + maxNumber);
					}
					lastDate = today;
				}
				/*
				 * 单回合PK游戏机器人自动坐庄
				 */
				// mcq_2006-6-22-_增加机器人自动坐庄功能_start
				condition = "left_user_id = " + Constants.PKROBOT_ID
						+ " and mark = " + WGamePKBean.PK_MARK_BKING
						+ " and game_id = " + WGameBean.PK_DICE;
				if (pkService.getWGamePKCount(condition) == 0) {
					WGamePKBean dice = new WGamePKBean();
					dice.setGameId(WGameBean.PK_DICE);
					dice.setLeftUserId(Constants.PKROBOT_ID);
					dice.setLeftNickname(Constants.PKROBOT_NICKNAME);
					int[] dices = new int[3];
					dices[0] = ((RandomUtil.nextInt(100) + 1)) % 6 + 1;
					dices[1] = ((RandomUtil.nextInt(100) + 1)) % 6 + 1;
					dices[2] = ((RandomUtil.nextInt(100) + 1)) % 6 + 1;

					dice.setLeftDices(dices);
					dice.setLeftCardsStr(dices[0] + "," + dices[1] + ","
							+ dices[2]);
					dice.setMark(WGamePKBean.PK_MARK_BKING);
					dice.setWager(100000);
					PKBaseAction.addWGamePK(dice);
				}

				condition = "left_user_id = " + Constants.PKROBOT_ID
						+ " and mark = " + WGamePKBean.PK_MARK_BKING
						+ " and game_id = " + WGameBean.PK_FOOTBALL;
				if (pkService.getWGamePKCount(condition) == 0) {
					WGamePKBean football = new WGamePKBean();
					football.setGameId(WGameBean.PK_FOOTBALL);
					football.setLeftUserId(Constants.PKROBOT_ID);
					football.setLeftNickname(Constants.PKROBOT_NICKNAME);
					int r = RandomUtil.nextInt(3) + 1;
					String systemAction = null;
					switch (r) {
					case 1:
						systemAction = "l";
						break;
					case 2:
						systemAction = "m";
						break;
					case 3:
						systemAction = "r";
						break;
					}
					football.setLeftCardsStr(systemAction);
					football.setMark(WGamePKBean.PK_MARK_BKING);
					football.setWager(Constants.PKROBOT_WAGER);
					pkService.addWGamePK(football);
				}
				// ////////////////////////////////////////////////////////////////
				// 篮球 2007.4.2 liq
				condition = "left_user_id = " + Constants.PKROBOT_ID
						+ " and mark = " + WGamePKBean.PK_MARK_BKING
						+ " and game_id = " + WGameBean.PK_BASKETBALL;
				if (pkService.getWGamePKCount(condition) == 0) {
					WGamePKBean basketball = new WGamePKBean();
					basketball.setGameId(WGameBean.PK_BASKETBALL);
					basketball.setLeftUserId(Constants.PKROBOT_ID);
					basketball.setLeftNickname(Constants.PKROBOT_NICKNAME);
					int r = RandomUtil.nextInt(3) + 1;
					String systemAction = null;
					switch (r) {
					case 1:
						systemAction = "l";
						break;
					case 2:
						systemAction = "m";
						break;
					case 3:
						systemAction = "r";
						break;
					}
					basketball.setLeftCardsStr(systemAction);
					basketball.setMark(WGamePKBean.PK_MARK_BKING);
					basketball.setWager(Constants.PKROBOT_WAGER);
					pkService.addWGamePK(basketball);
				}
				// //////////////////////////////////////////////////////////////////////////////////
				// 篮球 2007.4.2 liq

				// ////////////////////////////////////////////////////////////////
				// 老虎杠子鸡 2007.4.2 liq
				condition = "left_user_id = " + Constants.PKROBOT_ID
						+ " and mark = " + WGamePKBean.PK_MARK_BKING
						+ " and game_id = " + WGameBean.PK_LGJ;
				if (pkService.getWGamePKCount(condition) == 0) {
					WGamePKBean lgj = new WGamePKBean();
					lgj.setGameId(WGameBean.PK_LGJ);
					lgj.setLeftUserId(Constants.PKROBOT_ID);
					lgj.setLeftNickname(Constants.PKROBOT_NICKNAME);
					int r = RandomUtil.nextInt(4) + 1;
					String systemAction = null;
					switch (r) {
					case 1:
						systemAction = "a";
						break;
					case 2:
						systemAction = "b";
						break;
					case 3:
						systemAction = "c";
						break;
					case 4:
						systemAction = "d";
						break;
					}
					lgj.setLeftCardsStr(systemAction);
					lgj.setMark(WGamePKBean.PK_MARK_BKING);
					lgj.setWager(Constants.PKROBOT_WAGER);
					pkService.addWGamePK(lgj);
				}
				// ////////////////////////////////////////////////////////////////
				// 老虎杠子鸡 2007.4.2 liq

				condition = "left_user_id = " + Constants.PKROBOT_ID
						+ " and mark = " + WGamePKBean.PK_MARK_BKING
						+ " and game_id = " + WGameBean.PK_GONG3;
				if (pkService.getWGamePKCount(condition) == 0) {
					WGamePKBean gong3 = new WGamePKBean();
					gong3.setGameId(WGameBean.PK_GONG3);
					gong3.setLeftUserId(Constants.PKROBOT_ID);
					gong3.setLeftNickname(Constants.PKROBOT_NICKNAME);
					Vector cardList = new Vector();
					Cards cards = new Cards();
					cardList.add(cards.getCard());
					cardList.add(cards.getCard());
					cardList.add(cards.getCard());
					CardBean card1 = (CardBean) cardList.get(0);
					int a = card1.getValue();
					int aa = card1.getType();
					CardBean card2 = (CardBean) cardList.get(1);
					int b = card2.getValue();
					int bb = card2.getType();
					CardBean card3 = (CardBean) cardList.get(2);
					int c = card3.getValue();
					int cc = card3.getType();
					gong3.setLeftCardsStr(a + "_" + aa + "," + b + "_" + bb
							+ "," + c + "_" + cc);
					gong3.setMark(WGamePKBean.PK_MARK_BKING);
					gong3.setWager(Constants.PKROBOT_WAGER);
					pkService.addWGamePK(gong3);
				}

				condition = "left_user_id = " + Constants.PKROBOT_ID
						+ " and mark = " + WGamePKBean.PK_MARK_BKING
						+ " and game_id = " + WGameBean.PK_JSB;
				if (pkService.getWGamePKCount(condition) == 0) {
					WGamePKBean jsb = new WGamePKBean();
					jsb.setGameId(WGameBean.PK_JSB);
					jsb.setLeftUserId(Constants.PKROBOT_ID);
					jsb.setLeftNickname(Constants.PKROBOT_NICKNAME);
					int r = RandomUtil.nextInt(3) + 1;
					String systemAction = null;
					switch (r) {
					case 1:
						systemAction = "j";
						break;
					case 2:
						systemAction = "s";
						break;
					case 3:
						systemAction = "b";
						break;
					}
					jsb.setLeftCardsStr(systemAction);
					jsb.setMark(WGamePKBean.PK_MARK_BKING);
					jsb.setWager(Constants.PKROBOT_WAGER);
					pkService.addWGamePK(jsb);
				}
				// mcq_2006-6-22-_增加机器人自动坐庄功能_end
				if(minute % 10 == 9) {
					/**
					 * 单回合游戏。pk对方后，对方无应答则超时，每10分钟判断一次
					 */
					condition = "mark = "
							+ WGamePKBean.PK_MARK_PKING
							+ " and DATE_ADD(start_datetime, INTERVAL 5 minute) < now()";
					pkList = pkService.getWGamePKList(condition);
					itr = pkList.iterator();
					while (itr.hasNext()) {
						pk = (WGamePKBean) itr.next();
						// 掷骰子
						if (pk.getGameId() == WGameBean.PK_DICE) {
							if (pk.getMark() == WGamePKBean.PK_MARK_END) {
								continue;
							}
	
							// UserStatusBean us =
							// getUserStatus(pk.getLeftUserId());
	
							int winUserId = 0;
							// 庄家赢
							// 庄家加上乐币
							// userService.updateUserStatus(
							// "game_point = (game_point + "
							// + (winWager + pk.getWager()) + ")",
							// "user_id = " + pk.getLeftUserId());
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + "
											+ (winWager + pk.getWager()),
									"user_id = " + pk.getLeftUserId(), pk
											.getLeftUserId(), UserCashAction.WAGER,
									"庄家加上乐币" + (winWager + pk.getWager()));
							// zhul_2006-08-11 modify userstatus end
	
							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(pk.getLeftUserId());
							// mcq_end
							winUserId = pk.getLeftUserId();
	
							String set = "end_datetime = now(), mark = "
									+ WGamePKBean.PK_MARK_END + ", win_user_id = "
									+ winUserId + ", left_viewed = 1";
							condition = "id = " + pk.getId();
							pkService.updateWGamePK(set, condition);
	
							// **更新用户状态**
							// **加入消息系统**
	
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getPKTimeoutNoticeTitle(pk
									.getRightNickname(), "掷骰子"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(pk.getLeftUserId());
							notice.setHideUrl("");
							notice.setLink("/wgamepk/dice/viewPK.jsp?id=" + pk.getId());
							// macq_2007-5-16_增加人人对战PK游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME);
							// macq_2007-5-16_增加人人对战PK游戏消息类型_end
							noticeService.addNotice(notice);
						}
						// 射门
						else if (pk.getGameId() == WGameBean.PK_FOOTBALL) {
							if (pk.getMark() == WGamePKBean.PK_MARK_END) {
								continue;
							}
	
							// UserStatusBean us =
							// getUserStatus(pk.getLeftUserId());
	
							int winUserId = 0;
							// 庄家赢
							// 庄家加上乐币
							// userService.updateUserStatus(
							// "game_point = (game_point + " + (winWager +
							// pk.getWager()) + ")",
							// "user_id = " + pk.getLeftUserId());
							// zhul_2006-08-11 modify userstatus start
							int cWager = winWager + pk.getWager() * 2;
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + "
											+ cWager,
									"user_id = " + pk.getLeftUserId(), pk
											.getLeftUserId(), UserCashAction.WAGER,
									"庄家加上乐币" + (winWager + pk.getWager()));
							// zhul_2006-08-11 modify userstatus end
	
							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(pk.getLeftUserId());
							// mcq_end
							winUserId = pk.getLeftUserId();
	
							String set = "end_datetime = now(), mark = "
									+ WGamePKBean.PK_MARK_END + ", win_user_id = "
									+ winUserId + ", left_viewed = 1";
							condition = "id = " + pk.getId();
							pkService.updateWGamePK(set, condition);
	
							// **更新用户状态**
							// **加入消息系统**
	
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getPKTimeoutNoticeTitle(pk
									.getRightNickname(), "射门"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(pk.getLeftUserId());
							notice.setHideUrl("");
							notice.setLink("/wgamepk/football/viewPK.jsp?id="
									+ pk.getId());
							// macq_2007-5-16_增加人人对战PK游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME);
							// macq_2007-5-16_增加人人对战PK游戏消息类型_end
							noticeService.addNotice(notice);
						}
						// 篮球
						else if (pk.getGameId() == WGameBean.PK_BASKETBALL) {
							if (pk.getMark() == WGamePKBean.PK_MARK_END) {
								continue;
							}
	
							// UserStatusBean us =
							// getUserStatus(pk.getLeftUserId());
	
							int winUserId = 0;
							// 庄家赢
							// 庄家加上乐币
							// userService.updateUserStatus(
							// "game_point = (game_point + " + (winWager +
							// pk.getWager()) + ")",
							// "user_id = " + pk.getLeftUserId());
							// zhul_2006-08-11 modify userstatus start
							int cWager = winWager + pk.getWager() * 2;
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + "
											+ cWager,
									"user_id = " + pk.getLeftUserId(), pk
											.getLeftUserId(), UserCashAction.WAGER,
									"庄家加上乐币" + (winWager + pk.getWager()));
							// zhul_2006-08-11 modify userstatus end
	
							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(pk.getLeftUserId());
							// mcq_end
							winUserId = pk.getLeftUserId();
	
							String set = "end_datetime = now(), mark = "
									+ WGamePKBean.PK_MARK_END + ", win_user_id = "
									+ winUserId + ", left_viewed = 1";
							condition = "id = " + pk.getId();
							pkService.updateWGamePK(set, condition);
	
							// **更新用户状态**
							// **加入消息系统**
	
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getPKTimeoutNoticeTitle(pk
									.getRightNickname(), "篮球"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(pk.getLeftUserId());
							notice.setHideUrl("");
							notice.setLink("/wgamepk/basketball/viewPK.jsp?id="
									+ pk.getId());
							// macq_2007-5-16_增加人人对战PK游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME);
							// macq_2007-5-16_增加人人对战PK游戏消息类型_end
							noticeService.addNotice(notice);
						}
						// 剪刀石头布
						else if (pk.getGameId() == WGameBean.PK_JSB) {
							if (pk.getMark() == WGamePKBean.PK_MARK_END) {
								continue;
							}
	
							// UserStatusBean us =
							// getUserStatus(pk.getLeftUserId());
	
							int winUserId = 0;
							// 庄家赢
							// 庄家加上乐币
							// userService.updateUserStatus(
							// "game_point = (game_point + " + (winWager +
							// pk.getWager()) + ")",
							// "user_id = " + pk.getLeftUserId());
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + "
											+ (winWager + pk.getWager()),
									"user_id = " + pk.getLeftUserId(), pk
											.getLeftUserId(), UserCashAction.WAGER,
									"庄家加上乐币" + (winWager + pk.getWager()));
							// zhul_2006-08-11 modify userstatus end
	
							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(pk.getLeftUserId());
							// mcq_end
							winUserId = pk.getLeftUserId();
	
							String set = "end_datetime = now(), mark = "
									+ WGamePKBean.PK_MARK_END + ", win_user_id = "
									+ winUserId + ", left_viewed = 1";
							condition = "id = " + pk.getId();
							pkService.updateWGamePK(set, condition);
	
							// **更新用户状态**
							// **加入消息系统**
	
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getPKTimeoutNoticeTitle(pk
									.getRightNickname(), "剪刀石头布"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(pk.getLeftUserId());
							notice.setHideUrl("");
							notice.setLink("/wgamepk/jsb/viewPK.jsp?id=" + pk.getId());
							// macq_2007-5-16_增加人人对战PK游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME);
							// macq_2007-5-16_增加人人对战PK游戏消息类型_end
							noticeService.addNotice(notice);
						}
						// 老虎棒子鸡
						else if (pk.getGameId() == WGameBean.PK_LGJ) {
							if (pk.getMark() == WGamePKBean.PK_MARK_END) {
								continue;
							}
	
							// UserStatusBean us =
							// getUserStatus(pk.getLeftUserId());
	
							int winUserId = 0;
							// 庄家赢
							// 庄家加上乐币
							// userService.updateUserStatus(
							// "game_point = (game_point + " + (winWager +
							// pk.getWager()) + ")",
							// "user_id = " + pk.getLeftUserId());
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + "
											+ (winWager + pk.getWager()),
									"user_id = " + pk.getLeftUserId(), pk
											.getLeftUserId(), UserCashAction.WAGER,
									"庄家加上乐币" + (winWager + pk.getWager()));
							// zhul_2006-08-11 modify userstatus end
	
							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(pk.getLeftUserId());
							// mcq_end
							winUserId = pk.getLeftUserId();
	
							String set = "end_datetime = now(), mark = "
									+ WGamePKBean.PK_MARK_END + ", win_user_id = "
									+ winUserId + ", left_viewed = 1";
							condition = "id = " + pk.getId();
							pkService.updateWGamePK(set, condition);
	
							// **更新用户状态**
							// **加入消息系统**
	
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getPKTimeoutNoticeTitle(pk
									.getRightNickname(), "老虎棒子鸡"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(pk.getLeftUserId());
							notice.setHideUrl("");
							notice.setLink("/wgamepk/lgj/viewPK.jsp?id=" + pk.getId());
							// macq_2007-5-16_增加人人对战PK游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME);
							// macq_2007-5-16_增加人人对战PK游戏消息类型_end
							noticeService.addNotice(notice);
						}
						// 三公
						else if (pk.getGameId() == WGameBean.PK_GONG3) {
							if (pk.getMark() == WGamePKBean.PK_MARK_END) {
								continue;
							}
	
							// UserStatusBean us =
							// getUserStatus(pk.getLeftUserId());
	
							int winUserId = 0;
							// 庄家赢
							// 庄家加上乐币
							// userService.updateUserStatus(
							// "game_point = (game_point + " + (winWager +
							// pk.getWager()) + ")",
							// "user_id = " + pk.getLeftUserId());
							// zhul_2006-08-11 modify userstatus start
							int cWager = winWager + pk.getWager() * 5;
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + " + cWager,
									"user_id = " + pk.getLeftUserId(), pk
											.getLeftUserId(), UserCashAction.WAGER,
									"庄家加上乐币" + (winWager + pk.getWager()));
							// zhul_2006-08-11 modify userstatus end
	
							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(pk.getLeftUserId());
							// mcq_end
							winUserId = pk.getLeftUserId();
	
							String set = "end_datetime = now(), mark = "
									+ WGamePKBean.PK_MARK_END + ", win_user_id = "
									+ winUserId + ", left_viewed = 1";
							condition = "id = " + pk.getId();
							pkService.updateWGamePK(set, condition);
	
							// **更新用户状态**
							// **加入消息系统**
	
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getPKTimeoutNoticeTitle(pk
									.getRightNickname(), "三公"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(pk.getLeftUserId());
							notice.setHideUrl("");
							notice.setLink("/wgamepk/3gong/viewPK.jsp?id=" + pk.getId());
							// macq_2007-5-16_增加人人对战PK游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME);
							// macq_2007-5-16_增加人人对战PK游戏消息类型_end
							noticeService.addNotice(notice);
						}
					}
				}
				/**
				 * 多回合游戏
				 */
				// 五子棋
				condition = "mark != " + HallBean.GS_END + " and game_id = "
						+ HallBean.GOBANG;
				hallList = hallService.getWGameHallList(condition);
				itr = hallList.iterator();
				long currentTime = DateUtil.getCurrentTime();
				int seconds = 0;
				int winUserId = 0;
				int loseUserId = 0;
				String timeoutNickname = null;
				while (itr.hasNext()) {
					hall = (WGameHallBean) itr.next();
					gobangData = GameDataAction.getGoBangData(hall
							.getUniqueMark());
					if (hall.getMark() != HallBean.GS_END) {
						if (gobangData == null) {
							condition = "id = " + hall.getId();
							hallService.updateWGameHall("mark = "
									+ HallBean.GS_END + ", result = '"
									+ "超时没接受邀请'", condition);
							continue;
						}
						seconds = (int) ((currentTime - gobangData
								.getLastActiveTime()) / 1000);
					} else {
						continue;
					}

					winUserId = gobangData.getLastActiveUserId();
					getUserStatus(winUserId);
					timeoutNickname = null;
					if (winUserId == hall.getLeftUserId()) {
						timeoutNickname = hall.getRightNickname();
					} else {
						timeoutNickname = hall.getLeftNickname();
					}

					// 邀请超时
					if (hall.getMark() == HallBean.GS_WAITING) {
						// 超时了
						if (seconds >= TIME_OUT) {
							// 增加100个乐币
							condition = "user_id = " + winUserId;
							// userService.updateUserStatus(
							// "game_point = game_point + " + winWager,
							// condition);
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + " + winWager,
									condition, winUserId, UserCashAction.WAGER,
									"邀请超时加上乐币100");
							// zhul_2006-08-11 modify userstatus end

							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(winUserId);
							// mcq_end
							// 更新游戏状态
							condition = "id = " + hall.getId();
							hallService.updateWGameHall("mark = "
									+ HallBean.GS_END + ", result = '"
									+ StringUtil.toSql(timeoutNickname) + "超时没接受邀请'", condition);

							// 删除本局数据
							GameDataAction.removeGoBangData(hall
									.getUniqueMark());

							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getHallTimeoutNoticeTitle(
									timeoutNickname, "五子棋", "回应邀请"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(winUserId);
							notice.setHideUrl("/wgamehall/gobang/playing.jsp");
							notice.setLink("/wgamehall/gobang/playing.jsp?gameId="
									+ hall.getId());
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME_HALL);
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_end
							noticeService.addNotice(notice);
						}
					}
					// 下子超时
					else if (hall.getMark() == HallBean.GS_PLAYING) {
						// 超时了
						if (seconds >= TIME_OUT) {
							loseUserId = hall.getLeftUserId() == winUserId ? hall
									.getRightUserId()
									: hall.getLeftUserId();
							// 更新双方积分
							// userService.updateUserStatus(
							// "game_point = game_point + 500",
							// "user_id = " + winUserId);
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + 500",
									"user_id = " + winUserId, winUserId,
									UserCashAction.WAGER, "下子超时了,给winuser+500");
							// zhul_2006-08-11 modify userstatus end

							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(winUserId);
							// mcq_end
							hallService.updateWGameHall("win_user_id = "
									+ winUserId + ", mark = " + HallBean.GS_END
									+ ", result = '" + StringUtil.toSql(timeoutNickname)
									+ "超时没回应'", "id = " + hall.getId());
							// 删除本局数据
							GameDataAction.removeGoBangData(hall
									.getUniqueMark());
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							if (gobangData.getActionType() == GoBangDataBean.ACTION_SUE_FOR_PEACE) {
								notice.setTitle(getHallTimeoutNoticeTitle(
										timeoutNickname, "五子棋", "响应"));
							} else {
								notice.setTitle(getHallTimeoutNoticeTitle(
										timeoutNickname, "五子棋", "下子"));
							}
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(winUserId);
							notice.setHideUrl("/wgamehall/gobang/playing.jsp");
							notice.setLink("/wgamehall/gobang/playing.jsp?gameId="
									+ hall.getId());
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME_HALL);
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_end
							noticeService.addNotice(notice);
						}
					}
				}

				// 点球决战
				condition = "mark != " + HallBean.GS_END + " and game_id = "
						+ HallBean.FOOTBALL;
				hallList = hallService.getWGameHallList(condition);
				itr = hallList.iterator();
				currentTime = DateUtil.getCurrentTime();
				seconds = 0;
				winUserId = 0;
				loseUserId = 0;
				timeoutNickname = null;
				while (itr.hasNext()) {
					hall = (WGameHallBean) itr.next();
					footballData = GameDataAction.getFootballData(hall
							.getUniqueMark());
					if (hall.getMark() != HallBean.GS_END) {
						if (footballData == null) {
							condition = "id = " + hall.getId();
							hallService.updateWGameHall("mark = "
									+ HallBean.GS_END + ", result = '"
									+ "超时没接受邀请'", condition);
							continue;
						}
						seconds = (int) ((currentTime - footballData
								.getLastActiveTime()) / 1000);
					} else {
						continue;
					}

					winUserId = footballData.getLastActiveUserId();
					getUserStatus(winUserId);
					timeoutNickname = null;
					if (winUserId == hall.getLeftUserId()) {
						timeoutNickname = hall.getRightNickname();
					} else {
						timeoutNickname = hall.getLeftNickname();
					}

					// 邀请超时
					if (hall.getMark() == HallBean.GS_WAITING) {
						// 超时了
						if (seconds >= TIME_OUT) {
							// 增加100个乐币
							condition = "user_id = " + winUserId;
							// userService.updateUserStatus(
							// "game_point = game_point + " + winWager,
							// condition);
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + " + winWager,
									condition, winUserId, UserCashAction.WAGER,
									"邀请超时了,给winuser+100");
							// zhul_2006-08-11 modify userstatus end

							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(winUserId);
							// mcq_end
							// 更新游戏状态
							condition = "id = " + hall.getId();
							hallService.updateWGameHall("mark = "
									+ HallBean.GS_END + ", result = '"
									+ StringUtil.toSql(timeoutNickname) + "超时没接受邀请'", condition);

							// 删除本局数据
							GameDataAction.removeFootballData(hall
									.getUniqueMark());

							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getHallTimeoutNoticeTitle(
									timeoutNickname, "点球决战", "回应邀请"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(winUserId);
							notice.setHideUrl("/wgamehall/football/playing.jsp");
							notice.setLink("/wgamehall/football/playing.jsp?gameId="
									+ hall.getId());
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME_HALL);
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_end
							noticeService.addNotice(notice);
						}
					}
					// 下子超时
					else if (hall.getMark() == HallBean.GS_PLAYING) {
						// 超时了
						if (seconds >= TIME_OUT) {
							loseUserId = hall.getLeftUserId() == winUserId ? hall
									.getRightUserId()
									: hall.getLeftUserId();
							// 更新双方积分
							// userService.updateUserStatus(
							// "game_point = game_point + 500",
							// "user_id = " + winUserId);
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + 500",
									"user_id = " + winUserId, winUserId,
									UserCashAction.WAGER, "下子超时了,给winuser+500");
							// zhul_2006-08-11 modify userstatus end

							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(winUserId);
							// mcq_end

							hallService.updateWGameHall("win_user_id = "
									+ winUserId + ", mark = " + HallBean.GS_END
									+ ", result = '" + StringUtil.toSql(timeoutNickname)
									+ "超时没回应'", "id = " + hall.getId());
							// 删除本局数据
							GameDataAction.removeFootballData(hall
									.getUniqueMark());
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getHallTimeoutNoticeTitle(
									timeoutNickname, "点球决战", "响应"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(winUserId);
							notice.setHideUrl("/wgamehall/football/playing.jsp");
							notice.setLink("/wgamehall/football/playing.jsp?gameId="
									+ hall.getId());
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME_HALL);
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_end
							noticeService.addNotice(notice);
						}
					}
				}

				// 砸金花
				condition = "mark != " + HallBean.GS_END + " and game_id = "
						+ HallBean.JINHUA;
				hallList = hallService.getWGameHallList(condition);
				itr = hallList.iterator();
				currentTime = DateUtil.getCurrentTime();
				seconds = 0;
				winUserId = 0;
				loseUserId = 0;
				timeoutNickname = null;
				while (itr.hasNext()) {
					hall = (WGameHallBean) itr.next();
					jinhuaData = GameDataAction.getJinhuaData(hall
							.getUniqueMark());
					if (hall.getMark() != HallBean.GS_END) {
						if (jinhuaData == null) {
							condition = "id = " + hall.getId();
							hallService.updateWGameHall("mark = "
									+ HallBean.GS_END + ", result = '"
									+ "超时没接受邀请'", condition);
							continue;
						}
						seconds = (int) ((currentTime - jinhuaData
								.getLastActiveTime()) / 1000);
					} else {
						continue;
					}

					winUserId = jinhuaData.getLastActiveUserId();
					getUserStatus(winUserId);
					timeoutNickname = null;
					if (winUserId == hall.getLeftUserId()) {
						timeoutNickname = hall.getRightNickname();
					} else {
						timeoutNickname = hall.getLeftNickname();
					}

					// 邀请超时
					if (hall.getMark() == HallBean.GS_WAITING) {
						// 超时了
						if (seconds >= TIME_OUT) {
							// 增加100个乐币
							condition = "user_id = " + winUserId;
							// userService.updateUserStatus(
							// "game_point = game_point + " + winWager,
							// condition);
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + " + winWager,
									condition, winUserId, UserCashAction.WAGER,
									"邀请超时了,给winuser+100");
							// zhul_2006-08-11 modify userstatus end

							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(winUserId);
							// mcq_end
							// 更新游戏状态
							condition = "id = " + hall.getId();
							hallService.updateWGameHall("mark = "
									+ HallBean.GS_END + ", result = '"
									+ StringUtil.toSql(timeoutNickname) + "超时没接受邀请'", condition);

							// 删除本局数据
							GameDataAction.removeJinhuaData(hall
									.getUniqueMark());

							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getHallTimeoutNoticeTitle(
									timeoutNickname, "砸金花", "回应邀请"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(winUserId);
							notice.setHideUrl("/wgamehall/jinhua/playing.jsp");
							notice.setLink("/wgamehall/jinhua/playing.jsp?gameId="
									+ hall.getId());
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME_HALL);
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_end
							noticeService.addNotice(notice);
						}
					}
					// 下子超时
					else if (hall.getMark() == HallBean.GS_PLAYING) {
						// 超时了
						if (seconds >= TIME_OUT) {
							loseUserId = hall.getLeftUserId() == winUserId ? hall
									.getRightUserId()
									: hall.getLeftUserId();
							// 更新双方积分
							// userService.updateUserStatus(
							// "game_point = game_point + 500",
							// "user_id = " + winUserId);
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + 500",
									"user_id = " + winUserId, winUserId,
									UserCashAction.WAGER, "下子超时了,给winuser+500");
							// zhul_2006-08-11 modify userstatus end

							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(winUserId);
							// mcq_end

							hallService.updateWGameHall("win_user_id = "
									+ winUserId + ", mark = " + HallBean.GS_END
									+ ", result = '" + StringUtil.toSql(timeoutNickname)
									+ "超时没回应'", "id = " + hall.getId());
							// 删除本局数据
							GameDataAction.removeJinhuaData(hall
									.getUniqueMark());
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getHallTimeoutNoticeTitle(
									timeoutNickname, "砸金花", "响应"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(winUserId);
							notice.setHideUrl("/wgamehall/jinhua/playing.jsp");
							notice.setLink("/wgamehall/jinhua/playing.jsp?gameId="
									+ hall.getId());
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME_HALL);
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_end
							noticeService.addNotice(notice);
						}
					}
				}

				// 黑白棋
				condition = "mark != " + HallBean.GS_END + " and game_id = "
						+ HallBean.OTHELLO;
				hallList = hallService.getWGameHallList(condition);
				itr = hallList.iterator();
				currentTime = DateUtil.getCurrentTime();
				seconds = 0;
				winUserId = 0;
				loseUserId = 0;
				timeoutNickname = null;
				while (itr.hasNext()) {
					hall = (WGameHallBean) itr.next();
					othelloData = GameDataAction.getOthelloData(hall
							.getUniqueMark());
					if (hall.getMark() != HallBean.GS_END) {
						if (othelloData == null) {
							condition = "id = " + hall.getId();
							hallService.updateWGameHall("mark = "
									+ HallBean.GS_END + ", result = '"
									+ "超时没接受邀请'", condition);
							continue;
						}
						seconds = (int) ((currentTime - othelloData
								.getLastActiveTime()) / 1000);
					} else {
						continue;
					}

					winUserId = othelloData.getLastActiveUserId();
					getUserStatus(winUserId);
					timeoutNickname = null;
					if (winUserId == hall.getLeftUserId()) {
						timeoutNickname = hall.getRightNickname();
					} else {
						timeoutNickname = hall.getLeftNickname();
					}

					// 邀请超时
					if (hall.getMark() == HallBean.GS_WAITING) {
						// 超时了
						if (seconds >= TIME_OUT) {
							// 增加100个乐币
							condition = "user_id = " + winUserId;
							// userService.updateUserStatus(
							// "game_point = game_point + " + winWager,
							// condition);
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + " + winWager,
									condition, winUserId, UserCashAction.WAGER,
									"邀请超时了,winWager+100");
							// zhul_2006-08-11 modify userstatus end

							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(winUserId);
							// mcq_end
							// 更新游戏状态
							condition = "id = " + hall.getId();
							hallService.updateWGameHall("mark = "
									+ HallBean.GS_END + ", result = '"
									+ StringUtil.toSql(timeoutNickname) + "超时没接受邀请'", condition);

							// 删除本局数据
							GameDataAction.removeOthelloData(hall
									.getUniqueMark());

							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getHallTimeoutNoticeTitle(
									timeoutNickname, "黑白棋", "回应邀请"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(winUserId);
							notice.setHideUrl("/wgamehall/othello/playing.jsp");
							notice.setLink("/wgamehall/othello/playing.jsp?gameId="
									+ hall.getId());
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME_HALL);
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_end
							noticeService.addNotice(notice);
						}
					}
					// 下子超时
					else if (hall.getMark() == HallBean.GS_PLAYING) {
						// 超时了
						if (seconds >= TIME_OUT) {
							loseUserId = hall.getLeftUserId() == winUserId ? hall
									.getRightUserId()
									: hall.getLeftUserId();
							// 更新双方积分
							// userService.updateUserStatus(
							// "game_point = game_point + 500",
							// "user_id = " + winUserId);
							// zhul_2006-08-11 modify userstatus start
							UserInfoUtil.updateUserStatus(
									"game_point = game_point + 500",
									"user_id = " + winUserId, winUserId,
									UserCashAction.WAGER, "下子超时,winuser+500");
							// zhul_2006-08-11 modify userstatus end

							// mcq_1_把更改过乐币的用户添加到vector中
							// UserInfoUtil.addUserInfo(winUserId);
							// mcq_end

							hallService.updateWGameHall("win_user_id = "
									+ winUserId + ", mark = " + HallBean.GS_END
									+ ", result = '" + StringUtil.toSql(timeoutNickname)
									+ "超时没回应'", "id = " + hall.getId());
							// 删除本局数据
							GameDataAction.removeOthelloData(hall
									.getUniqueMark());
							// 加入消息系统
							NoticeBean notice = new NoticeBean();
							notice.setTitle(getHallTimeoutNoticeTitle(
									timeoutNickname, "黑白棋", "响应"));
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setUserId(winUserId);
							notice.setHideUrl("/wgamehall/othello/playing.jsp");
							notice.setLink("/wgamehall/othello/playing.jsp?gameId="
									+ hall.getId());
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_start
							notice.setMark(NoticeBean.PK_GAME_HALL);
							// macq_2007-5-16_增加人人多回合对战游戏消息类型_end
							noticeService.addNotice(notice);
						}
					}
				}
				// macq_2007-11-22_每隔9分钟或19分钟或28分钟系统自动添加道具到拍卖大厅_start
				if (minute % 29 == 0) {
					// addAuctionProduct();
				}
				// macq_2007-11-22_自动添加道具到拍卖大厅_end

				// zhul 2006-09-29 每十分钟从家园日记、相册中取出一个新记录 start

				// if (getCurrentMinute() % 2 == 0) {
				// // 之前10分钟内的一篇新日记
				// HomeDiaryBean diary = homeService.getHomeDiary("id>"
				// + maxDiaryId + " order by rand() limit 0,1 ");
				// UserBean user = null;
				// JCRoomContentBean content = null;
				// if (diary != null) {
				// // user=userService.getUser("id="+diary.getUserId());
				// // zhul 2006-10-12_优化用户信息查询
				// user = UserInfoUtil.getUser(diary.getUserId());
				// // 在聊天室显示的提示信息
				// content = new JCRoomContentBean();
				// content.setFromId(0);
				// content.setFromNickName(user.getNickName());
				// content.setToNickName(diary.getUserId() + "");
				// content.setContent("的家园更新了日记。");
				// content.setAttach("");
				// content.setSecRoomId(-1);
				// content.setMark(6);
				// chatService.addContent(content);
				// // 当前日记最大记录号
				// diary = homeService
				// .getHomeDiary("0=0 order by id desc limit 0,1");
				// maxDiaryId = diary.getId();
				// }
				// // 之前10分钟内的一张新相片
				// HomePhotoBean photo = homeService.getHomePhoto("id>"
				// + maxPhotoId + " order by rand() limit 0,1 ");
				// if (photo != null) {
				// // user=userService.getUser("id="+photo.getUserId());
				// // zhul 2006-10-12_优化用户信息查询
				// user = UserInfoUtil.getUser(photo.getUserId());
				// // 在聊天室显示的提示信息
				// content = new JCRoomContentBean();
				// content.setFromId(0);
				// content.setFromNickName(user.getNickName());
				// content.setToNickName(photo.getUserId() + "");
				// content.setContent("的家园更新了相册。");
				// content.setAttach("");
				// content.setSecRoomId(-1);
				// content.setMark(6);
				// chatService.addContent(content);
				// // 当前相册最大记录号
				// photo = homeService
				// .getHomePhoto("0=0 order by id desc limit 0,1");
				// maxPhotoId = photo.getId();
				// }
				// }
				// zhul 2006-09-29 每十分钟从家园日记、相册中取出一个新记录 end
				// zhul 2006-09-18 用户在坐庄后72小时内无人挑庄，系统自动取消此庄 start
				
				// 如果超过48小时没有人挑战，庄自己消失(hourtask)，乐币损失 zhouj
//				Vector pks = pkService
//						.getWGamePKList("ADDDATE(start_datetime,interval 72 HOUR)<NOW() AND mark="
//								+ WGamePKBean.PK_MARK_BKING);
//				pkService
//						.deleteWGamePK("ADDDATE(start_datetime,interval 72 HOUR)<NOW() AND mark="
//								+ WGamePKBean.PK_MARK_BKING);
//				for (int i = 0; i < pks.size(); i++) {
//					WGamePKBean pkBean = (WGamePKBean) pks.get(i);
//					int cWager = pkBean.getWager();
//					if(pkBean.getGameId()==WGameBean.PK_GONG3){
//						cWager=cWager*5;
//					}else if(pkBean.getGameId()==WGameBean.PK_FOOTBALL || pkBean.getGameId()==WGameBean.PK_BASKETBALL){
//						cWager=cWager*2;
//					}
//					UserInfoUtil.updateUserCash(pkBean.getLeftUserId(), cWager,
//							UserCashAction.WAGER, "PK单回合游戏增加用户乐币"
//									+ cWager);
//				}
				
				
				// zhul 2006-09-18 用户在坐庄后72小时内无人挑庄，系统自动取消此庄 end
				// macq_2007-7-19_大富豪用户在坐庄后72小时内无人挑庄，系统自动取消此庄 start
				/*pks = pkService
						.getWGamePKBigList("ADDDATE(start_datetime,interval 72 HOUR)<NOW() AND mark="
								+ WGamePKBigBean.PK_MARK_BKING);
				pkService
						.deleteWGamePKBig("ADDDATE(start_datetime,interval 72 HOUR)<NOW() AND mark="
								+ WGamePKBigBean.PK_MARK_BKING);
				for (int i = 0; i < pks.size(); i++) {
					WGamePKBigBean pkBigBean = (WGamePKBigBean) pks.get(i);
					BankCacheUtil.updateBankStoreCacheById(
							pkBigBean.getWager(), pkBigBean.getLeftUserId(),0,Constants.BANK_DAFUHAO_TYPE);
				}*/
				// macq_2007-7-19_大富豪用户在坐庄后72小时内无人挑庄，系统自动取消此庄 start
                //guip_2007-11-06_街霸用户在坐庄后72小时内无人挑庄，系统自动取消此庄 start
				/*pks = fightService.getWGameFightList("ADDDATE(start_datetime,interval 72 HOUR)<NOW() AND mark=" + WGameFightBean.PK_MARK_BKING);
				//删除10天外的记录
				fightService
						.deleteWGameFight("ADDDATE(start_datetime,interval 10 DAY)<NOW() AND mark="
								+ WGameFightBean.PK_MARK_END);
				//删除72小时的庄
				fightService
				.deleteWGameFight("ADDDATE(start_datetime,interval 72 HOUR)<NOW() AND mark="
						+ WGameFightBean.PK_MARK_BKING);
				for (int i = 0; i < pks.size(); i++) {
					WGameFightBean fightBean = (WGameFightBean) pks.get(i);
                //给用户返还乐币
					UserInfoUtil.updateUserStatus("game_point = game_point + "
							+ fightBean.getWager(), "user_id = " + fightBean.getLeftUserId(),fightBean.getLeftUserId(),UserCashAction.GAME,"wgame--乐酷街霸取消坐庄--返还乐币"+ fightBean.getWager());
				}*/
				// guip_2007-11-06_街霸用户在坐庄后72小时内无人挑庄，系统自动取消此庄 start

				long endTime = System.currentTimeMillis();
				LogUtil.logTime("WGameThread*****end:" + (endTime - startTime));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// macq_2007-1-22_自动添加拍卖大厅物品_start
	public void addAuctionProduct() {
		IDummyService dummyProductSerivce = ServiceFactory.createDummyService();
		List dummyProductList = getDummyProductList();// 3个虚拟物品(除去家具)的id列表
		Integer dummyProductId = null;
		DummyProductBean dummyProduct = null;
		int startPrice = 100000;// 起始竞价
		for (int i = 0; i < dummyProductList.size(); i++) {
			int auctionUserIds = getAuctionUserId();
			long bitePrice = (long) (RandomUtil.nextInt(30) + 20) * 100000000;
			dummyProductId = (Integer) dummyProductList.get(i);
			dummyProduct = dummyProductSerivce.getDummyProducts(dummyProductId.intValue());
			AuctionBean auction = new AuctionBean();
			auction.setLeftUserId(auctionUserIds);
			auction.setRightUserId(0);
			auction.setProductId(dummyProduct.getId());
			auction.setDummyId(dummyProduct.getDummyId());
			auction.setTime(dummyProduct.getTime());
			auction.setStartPrice(startPrice);
			auction.setBitePrice(bitePrice);
			auction.setCurrentPrice(startPrice);
			auction.setMark(0);
			AuctionCacheUtil.addAction(auction);
		}
	}

	// macq_2007-1-22_自动添加拍卖大厅物品_end

	// macq_2007-1-22_随机生成拍卖师ID_start
	public int getAuctionUserId() {
		int[] auctionUserIds = { 123, 124, 125 };
		return auctionUserIds[RandomUtil.nextInt(auctionUserIds.length)];
	}

	// macq_2007-1-22_随机生成拍卖师ID_end

	public boolean flag = false;

	public void setDummyProductFlag() {
		flag = true;
	}

	public List getDummyProductList() {
		String sql = null;
		if (flag) {
			sql = "select id from item where dummy_id!=1 order by rand() limit 0,3";
		} else {
			sql = "select id from item where dummy_id!=1 and id!=10 and id!=11 order by rand() limit 0,3";
		}
		List dummyProductList = SqlUtil.getIntList(sql, 4);
		if (dummyProductList == null) {
			dummyProductList = new ArrayList();
		}
		return dummyProductList;
	}

	public UserStatusBean getUserStatus(int userId) {
		IUserService userService = ServiceFactory.createUserService();
		// UserStatusBean status = userService
		// .getUserStatus("user_id = " + userId);
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean status = UserInfoUtil.getUserStatus(userId);
		// zhul_modify us _2006-08-14_获取用户状态信息 end
		if (status != null) {
			return status;
		} else {
			status = new UserStatusBean();
			status.setUserId(userId);
			status.setGamePoint(10000);
			status.setPoint(100);
			userService.addUserStatus(status);
			// zhul_2006-07-25 记录用户的现金流 start
			MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER, 10000,
					Constants.PLUS, userId);
			// zhul_2006-07-25 记录用户的现金流 end

			return status;
		}
	}

	public String getPKTimeoutNoticeTitle(String enemyNickname, String gameName) {
		return enemyNickname + "超时没应战你挑战的" + gameName;
	}

	public String getHallTimeoutNoticeTitle(String enemyNickname,
			String gameName, String action) {
		return gameName + ":" + enemyNickname + "超时没" + action;
	}

	public int getCurrentHour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public int getCurrentMinute() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MINUTE);
	}
}
