package net.joycool.wap.action.pet;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

public class ClimbAction extends PetAction{

	public ClimbAction(HttpServletRequest request){
		super(request);
	}
	


	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 长跑游戏的类型id
	public static int CLIMB = 4;

	// 单位时间内，标准的距离
	public static int LONG = 200;

	// 一圈的长度
	public static int CIRCLELONG = 600;

	// 分页数
	public static Integer lockswim = new Integer(5);

	//游戏中设定的玩家人数
	public static int CLIMB_PLAYNUMBER = 5;
	
	// 分页数
	public static int NUMBER_PER_PAGE = 10;

	// 共多少圈
	public static int CIRCLENUMBER = 3;

	public static int TOTAL_LONG = CIRCLELONG * CIRCLENUMBER;

	public static String[] CLIMB_SWIM = { "登山靴", "万能钩", "胶水" };

	public void climbing() {
		// 游戏场次id
		int id = StringUtil.toInt(request.getParameter("id"));
		// 游戏类型
		int type = StringUtil.toInt(request.getParameter("type"));
		MatchRunBean matchrunbean = (MatchRunBean) matchMap[CLIMB].get(new Integer(id));
		if (matchrunbean != null) {
			request.setAttribute("matchrunbean", matchrunbean);
			this.doTip("wait", "游戏中");
		} else {
			this.doTip(null, "此游戏不存在");
		}
		if (petUser != null)
			request.setAttribute("petUser", petUser);
	}

	// 游戏首页
	public void climbIndex() {
		// 坐庄页码
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得总数
		int totalHallCount = matchMap[CLIMB].size();
		int totalHallPageCount = totalHallCount / NUMBER_PER_PAGE;
		if (totalHallCount % NUMBER_PER_PAGE != 0) {
			totalHallPageCount++;
		}
		if (pageIndex > totalHallPageCount - 1) {
			pageIndex = totalHallPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Iterator iter = matchMap[CLIMB].values().iterator();
		int number = 0;
		if (totalHallCount > NUMBER_PER_PAGE) {
			for (int i = 0; i < totalHallCount - (pageIndex + 1)
					* NUMBER_PER_PAGE; i++)
				iter.next();
			number = NUMBER_PER_PAGE;
		}
		number = totalHallCount;

		Vector vector = new Vector(number);
		int temp = 0;
		while (iter.hasNext()) {
			vector.add(iter.next());
			temp++;
			if (pageIndex + 1 != totalHallPageCount) {
				if (temp >= NUMBER_PER_PAGE)
					break;
			} else {
				if (temp >= totalHallCount - pageIndex * NUMBER_PER_PAGE)
					break;
			}

		}

		String prefixUrl = "climbindex.jsp?type=4";
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("totalHallPageCount", new Integer(
				totalHallPageCount));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("vector", vector);

		request.setAttribute("petUser", petUser);

	}

	// 非第一个玩家加入游戏
	public void climbMatchAct() {
		String url = "/pet/climbing.jsp?";
		// 游戏场次id
		int task = StringUtil.toInt(request.getParameter("task"));
		// 第一个宠物玩家加入游戏
		if (task == 1) {
			if ((petUser.getHungry() < 50) || (petUser.getClear() < 50)
					|| (petUser.getHealth() < 50) || (petUser.getRank() < 11) || (petUser.getRank() > 20)) {
				doTip(null, "您的宠物不符合参加比赛的条件!");
				request.setAttribute("url", "/pet/climbindex.jsp");
			} else if ((petUser.getMatchid() == 0)
					&& (petUser.getMatchtype() == 0)) {
				// 第一个宠物玩家创立游戏
				// 游戏类型
				int type = StringUtil.toInt(request.getParameter("type"));
				if (type == CLIMB) {

					// 新建一个比赛的bean
					MatchRunBean matchrunbean = new MatchRunBean(CLIMB_PLAYNUMBER);
					synchronized (lockswim) {
						// 设定长跑比赛计数器,以及treemap的key
						MATCH[CLIMB]++;
						matchrunbean.setId(MATCH[CLIMB]);
						// 设定游戏类型
						matchrunbean.setType(type);
						// 将宠物标记为游戏状态
						petUser.setMatchid(matchrunbean.getId());
						// 赛跑类型
						petUser.setMatchtype(CLIMB);
						// 将宠物放入玩家列表
						matchrunbean.addPlayer(petUser);
						// 将比赛加入长跑比赛的map中
						matchMap[CLIMB].put(new Integer(matchrunbean.getId()),
								matchrunbean);
					}
					request.setAttribute("matchrunbean", matchrunbean);

					url = url + "id=" + matchrunbean.getId() + "&type="
							+ matchrunbean.getType();
					request.setAttribute("url", url);
					doTip("wait", "wait");
				} else
					doTip(null, "参数错误");
			} else {
				doTip(null, "参数错误");
			}
			// 非第一个宠物玩家加入游戏
		} else if (task == 2) {
			if ((petUser.getHungry() < 50) || (petUser.getClear() < 50)
					|| (petUser.getHealth() < 50)  || (petUser.getRank() < 11) || (petUser.getRank() > 20)) {
				doTip(null, "您的宠物不符合参加比赛的条件!");
				request.setAttribute("url", "/pet/climbindex.jsp");
			} else if ((petUser.getMatchtype() == 0)
					&& (petUser.getMatchid() == 0)) {
				// 游戏场次id
				int id = StringUtil.toInt(request.getParameter("id"));
				// 游戏类型
				int type = StringUtil.toInt(request.getParameter("type"));
				synchronized (lockswim) {
				MatchRunBean matchrunbean = (MatchRunBean) matchMap[CLIMB]
						.get(new Integer(id));
				if (matchrunbean == null) {
					doTip(null, "参数错误");
				} else {
						// 宠物加入游戏
						if (matchrunbean.getCondition() != 0) {
							request.setAttribute("matchrunbean", matchrunbean);
							url = url + "id=" + matchrunbean.getId() + "&type="
									+ matchrunbean.getType();
							request.setAttribute("url", url);
						} else {
							// 将宠物标记为游戏状态
							petUser.setMatchid(matchrunbean.getId());
							// 赛跑类型
							petUser.setMatchtype(CLIMB);
							// 将宠物放入玩家列表
							matchrunbean.addPlayer(petUser);
							// 判断是否开始游戏
							if (matchrunbean.getPeoplenumber() == CLIMB_PLAYNUMBER) {
								// 开始游戏
								this.doTip("wait", "游戏中");
								climbStartGame(matchrunbean, loginUser.getId());
							} else
								this.doTip("wait", "等待中");
							request.setAttribute("matchrunbean", matchrunbean);
	
							url = url + "id=" + matchrunbean.getId() + "&type="
									+ matchrunbean.getType();
							request.setAttribute("url", url);
						}
					}
				}
			} else {
				doTip(null, "只能同時參加一項比賽");
			}
			// 退出游戏
		} else if (task == 3) {
			int id = StringUtil.toInt(request.getParameter("return"));
			// 退出游戏
			if ((id != -1) && (petUser.getMatchtype() != 0)
					&& (petUser.getMatchid() != 0)) {
				// 删除比赛中的数据
				synchronized (lockswim) {
				MatchRunBean matchrunbean = (MatchRunBean) matchMap[CLIMB]
						.get(new Integer(petUser.getMatchid()));
				if (matchrunbean != null){
					
						if (matchrunbean.getCondition() == 0) {
							matchrunbean.exitPlayer(petUser);
							// 等待游戏的宠物玩家数量为零时,删除比赛数据
							if (matchrunbean.getPeoplenumber() <= 0)
								matchMap[CLIMB].remove(new Integer(matchrunbean.getId()));
							// 删除宠物bean中的游戏状态
							petUser.setMatchid(0);
							petUser.setMatchtype(0);
						
						} else if (matchrunbean.getCondition() == 2) {
							petUser.setMatchid(0);
							petUser.setMatchtype(0);						
						}
					request.setAttribute("url", "/pet/climbindex.jsp");
				}else {
					doTip(null, "参数错误");
				}
				}
					
			}
			// 使用道具
		} else if (task == 4) {
			// 使用道具
			if ((petUser.getMatchtype() != 0) && (petUser.getMatchid() != 0)) {
				MatchRunBean matchrunbean = (MatchRunBean) matchMap[CLIMB]
						.get(new Integer(petUser.getMatchid()));
				if ((matchrunbean != null)
						&& (matchrunbean.getCondition() == 1)) {
					PlayerBean[] playbean = matchrunbean.getPlayer();
					// 取得是第几名使用道具
					int order = 0;
					for (order = 0; order < CLIMB_PLAYNUMBER; order++)
						if ((playbean[order] != null)
								&& (playbean[order].getPetid() == petUser
										.getId()))
							break;
					// 如果本人已经过终点线，道具无效
					if (playbean[order].getPosition() < TOTAL_LONG) {
						// order这个人使用道具
						if (playbean[order].getStage()[0] == 1) {
							// 给自己加60米
							playbean[order].setPosition(playbean[order]
									.getPosition() + 60);
							matchrunbean.addLog(StringUtil.toWml(playbean[order]
											.getName()) + "穿上了登山靴，提升了抓地力，超越了其他对手！");
							// 给第一的人减30米
							// 此人不是第一名
						} else if ((playbean[order].getStage()[0] == 2)
								&& ((order > 0))
								&& (playbean[0].getPosition() < TOTAL_LONG)) {
							playbean[0]
									.setPosition(playbean[0].getPosition() - 30);
							matchrunbean.addLog(StringUtil
									.toWml(playbean[order].getName())
									+ "用万能钩钩住了"
									+ StringUtil.toWml(playbean[0].getName())
									+ "的裤衩,"+ StringUtil.toWml(playbean[0].getName())
									+ "被拽了下去！");
							// 给前边的人减60米
							// 此人不是第一名
						} else if ((playbean[order].getStage()[0] == 3)
								&& (order > 0)
								&& (playbean[order - 1].getPosition() < TOTAL_LONG)) {
							//判断是否有防弹衣道具
							int temp = UserBagCacheUtil.getUserBagById(18, playbean[order - 1].getUserid());
							if ((temp > 0)&&(RandomUtil.percentRandom(STAGE_PROBABILITY))) {
								// 删除道具
								UserBagCacheUtil.UseUserBagCacheById(playbean[order - 1].getUserid(), temp);
     							matchrunbean.addLog(StringUtil.toWml(StringUtil.toWml(playbean[order - 1].getName())) + "的主人给它穿了防弹衣，胶水失效了！");
							}else{
								playbean[order - 1].setPosition(playbean[order - 1]
								     		.getPosition() - 60);
								matchrunbean.addLog(StringUtil.toWml(playbean[order].getName())
								     	+ "把"+ StringUtil.toWml(playbean[order - 1].getName())+ "的鞋底和手上涂满了胶水，"+ StringUtil.toWml(playbean[order - 1].getName())+ "被粘在了原地！");
							}
						} else {
							this.doTip(null, "参数错误");
						}
					}
					playbean[order].changeStage();
					this.doTip("wait", "游戏中");
					request.setAttribute("matchrunbean", matchrunbean);
					url = url + "id=" + matchrunbean.getId() + "&type="
							+ matchrunbean.getType();
					request.setAttribute("url", url);
				} else
					this.doTip(null, "参数错误");
			}
		}
		request.setAttribute("petUser", petUser);
	}

	// 游戏开始前要做的事
	public void climbStartGame(MatchRunBean matchrunbean, int id) {
		MatchEventBean matchEventBean;
		MatchFactorBean factorBean;
		PetUserBean petBean;
		float change = 0;
		int temp = 0;

		// 开始游戏

		PlayerBean[] playbean = matchrunbean.getPlayer();
		// 计算因子
		factorBean = server.getFactor("id =" + matchrunbean.getType());
		for (int j = 0; j < playbean.length; j++) {
			// 取得宠物id
//			petBean = (PetUserBean) userMap.get(new Integer(playbean[j]
//					.getPetid()));
//			if (petBean == null)
			petBean = load(0, playbean[j].getPetid());
			if (petBean != null) {
				// 加入消息系统,如果是最后进入游戏那个玩家的话,就不发信息
				if (petBean.getUser_id() != id) {
					NoticeBean notice = new NoticeBean();
					notice.setTitle(petBean.getName() + "开始比赛了,快去看吧");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setUserId(petBean.getUser_id());
					notice.setHideUrl("/pet/climbindex.jsp");
					notice.setLink("/pet/climbing.jsp?id=" + matchrunbean.getId()
							+ "&type=" + matchrunbean.getType());
					// liq_2007-7-16_增加宠物消息类型_start
					notice.setMark(NoticeBean.PET);
					// liq_2007-7-16_增加宠物消息类型_end
					noticeService.addNotice(notice);
				}
				// 属性因子发生作用
				playbean[j].setFactor(factor(petBean, factorBean));

				// 饥饿减少10
				petBean.setHungry(petBean.getHungry() - 10);
				server.updateUser(" hungry =" + petBean.getHungry(), "id = "
						+ petBean.getId());

			}
		}
		matchrunbean.setCondition(1);

	}

	// 计算因子
	public static int factor(PetUserBean petBean, MatchFactorBean factorBean) {
		int x = (int) (petBean.getHungry()
				* petBean.getHealth()
				* (factorBean.getAl() * Math.sqrt(petBean.getAgile())
						+ factorBean.getIn() * Math.sqrt(petBean.getIntel()) + factorBean
						.getSt()
						* Math.sqrt(petBean.getStrength())) / 10000);

		return x;
	}

	/**
	 * 
	 * @author liq
	 * @explain，
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static void climbtask() {
		MatchFactorBean factorBean;
		String log;
		Vector eventList;
		MatchEventBean matchEventBean;
		PetUserBean petBean = new PetUserBean();
		float change = 0;
		int factor = 0;
		Iterator iter = matchMap[CLIMB].values().iterator();
		while (iter.hasNext()) {
			MatchRunBean matchrunbean = (MatchRunBean) iter.next();

			if (matchrunbean.getCondition() == 0) {
				// 建立后等待中
			} else if (matchrunbean.getCondition() == 1) {
				// 取得随机时间列表
				eventList = server.getMatchEventList("gameid ="
						+ matchrunbean.getType());
				// 游戏过程中
				PlayerBean[] playbean = matchrunbean.getPlayer();
				for (int i = 0; i < CLIMB_PLAYNUMBER; i++) {
					if (playbean[i].getPosition() < TOTAL_LONG) {
						// ////////////////////////////////////////////////////////////////////////////////////////////////////
						// 宠物属性因子发生作用
						// 取得宠物id
						petBean = (PetUserBean) userMap.get(new Integer(
								playbean[i].getPetid()));

						if (playbean[i].getFactor() > 0)
							factor = RandomUtil
									.nextInt(playbean[i].getFactor());
						change = LONG;
						// /////////////////////////////////////////////////////////////////////////////////////////////////
						// 发生了随机事件
						if (RandomUtil
								.percentRandom(MatchEventBean.PROBABILITY)) {
							// 取得随机事件
							// 取得具体的事件
							matchEventBean = (MatchEventBean) eventList
									.get(RandomUtil.nextInt(eventList.size()));
							// 事件对长跑成绩发生影响
							change = change + change
									* matchEventBean.getFactor();

							// 将发生的时间记录对应比赛的log中，在页面上显示
							log = matchEventBean.getDescription().replace(
									"@", StringUtil.toWml(petBean.getName()));
							matchrunbean.addLog(log);
						} else {
							// 没有发生随机事件
						}
						// /////////////////////////////////////////////////////////////////////////////////////////////////
						// 总距离加上单位时间内的距离 以及属性因子的影响
						playbean[i].setPosition(playbean[i].getPosition()
								+ (int) change + factor);

						if (playbean[i].getPosition() >= TOTAL_LONG) {
							matchrunbean.addLog(StringUtil.toWml(petBean
									.getName())
									+ "已经攀登到了山顶");
						}
					} else {
						// 过终点后每圈加10000
						playbean[i]
								.setPosition(playbean[i].getPosition() + 10000);
					}
					factor = 0;
				}
				// /////////////////////////////////////////////////////////////////////////////////////////////////
				// 根据宠物的位置排序 0-8对应第一到第八

				Arrays.sort(playbean);

				// 比赛结束,判断排名倒地一的人是否跑完
				if (TOTAL_LONG <= playbean[playbean.length - 1].getPosition()) {
					matchrunbean.setCondition(2);

					for (int j = 0; j < playbean.length; j++) {
						// 取得宠物id
						petBean = (PetUserBean) userMap.get(new Integer(
								playbean[j].getPetid()));
						// 比赛结束,取消标记,可以参加新的 比赛了
						petBean.setMatchid(0);
						petBean.setMatchtype(0);

						// 加入消息系统
						NoticeBean notice = new NoticeBean();
						notice.setTitle(petBean.getName() + "取得了第" + (j + 1)
								+ "名,快去看吧");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setUserId(petBean.getUser_id());
						notice.setHideUrl("/pet/climbindex.jsp");
						notice.setLink("/pet/climbing.jsp?task=4&id="
								+ matchrunbean.getId() + "&type="
								+ matchrunbean.getType());
						// liq_2007-7-16_增加宠物消息类型_start
						notice.setMark(NoticeBean.PET);
						// liq_2007-7-16_增加宠物消息类型_end
						noticeService.addNotice(notice);
							
						if (j == 0) {
							// 根据成绩给玩家加积分
							petBean.setIntegral(petBean.getIntegral() + 2);
							petBean.setLeftintegral(petBean.getLeftintegral() + 2);
							petBean.setToday(petBean.getToday() + 2);
							// 更新数据库
							server.updateUser(" integral ="+ petBean.getIntegral()+ ", leftintegral ="+ petBean.getLeftintegral()+ ", today =" + petBean.getToday(), "id = "+ petBean.getId());
						} else if (j == 1) {
							petBean.setIntegral(petBean.getIntegral() + 1);
							petBean.setLeftintegral(petBean.getLeftintegral() + 1);
							petBean.setToday(petBean.getToday() + 1);
							// 更新数据库
							server.updateUser(" integral ="+ petBean.getIntegral()+ ", leftintegral ="+ petBean.getLeftintegral()+ ", today =" + petBean.getToday(), "id = "+ petBean.getId());
						}
					}
				}

				// 道具，每圈8个人中挑两个给道具，道具随机
				// 产生中奖的宠物
				int peop_1 = RandomUtil.nextInt(CLIMB_PLAYNUMBER);
				int peop_2;
				do {
					peop_2 = RandomUtil.nextInt(CLIMB_PLAYNUMBER);
				} while (peop_1 == peop_2);
				// 产生送的道具
				int stage_1 = RandomUtil.nextIntNoZero(3);
				int stage_2 = RandomUtil.nextIntNoZero(3);
				// 送道具
				playbean[peop_1].inputStage(stage_1);
				playbean[peop_2].inputStage(stage_2);

			} else {
				// 结束后
			}
		}
	}
}
