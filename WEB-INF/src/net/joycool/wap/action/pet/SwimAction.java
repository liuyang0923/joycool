package net.joycool.wap.action.pet;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

public class SwimAction extends PetAction {

	public SwimAction(HttpServletRequest request) {
		super(request);
	}
	HttpServletResponse  response ;
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 长跑游戏的类型id
	public static int SWIM = 2;

	// 单位时间内，标准的距离
	public static int LONG = 200;

	// 一圈的长度
	public static int CIRCLELONG = 600;

	// 分页数
	public static Integer lockswim = new Integer(5);

	// 游戏中设定的玩家人数
	public static int SWIM_PLAYNUMBER = 5;

	// 分页数
	public static int NUMBER_PER_PAGE = 10;

	// 共多少圈
	public static int CIRCLENUMBER = 3;

	public static int TOTAL_LONG = CIRCLELONG * CIRCLENUMBER;

	public static String[] STAGE_SWIM = { "推进器", "高压水枪", "秤砣", "渔网","渔叉","水雷" };

	public void swimming() {
		// 游戏场次id
		int id = StringUtil.toInt(request.getParameter("id"));
		// 游戏类型
		int type = StringUtil.toInt(request.getParameter("type"));
		MatchRunBean matchrunbean = (MatchRunBean) matchMap[SWIM]
				.get(new Integer(id));
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
	public void swimIndex() {
		// 坐庄页码
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得总数
		int totalHallCount = matchMap[SWIM].size();
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
		Iterator iter = matchMap[SWIM].values().iterator();
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

		String prefixUrl = "swimindex.jsp?type=2";
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("totalHallPageCount", new Integer(
				totalHallPageCount));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("vector", vector);

		request.setAttribute("petUser", petUser);

	}

	// 非第一个玩家加入游戏
	public void swimMatchAct() {
		String url = "/pet/swimming.jsp?";
		// 游戏场次id
		int task = StringUtil.toInt(request.getParameter("task"));
		// 第一个宠物玩家加入游戏
		if (task == 1) {
			if ((petUser.getHungry() < 50) || (petUser.getClear() < 50)
					|| (petUser.getHealth() < 50)) {
				doTip(null, "您的宠物不符合参加比赛的条件!");
				request.setAttribute("url", "/pet/swimindex.jsp");
			} else if ((petUser.getMatchid() == 0)
					&& (petUser.getMatchtype() == 0)) {
				// 第一个宠物玩家创立游戏
				// 游戏类型
				int type = StringUtil.toInt(request.getParameter("type"));
				if (type == 2) {

					// 新建一个比赛的bean
					MatchRunBean matchrunbean = new MatchRunBean(
							SWIM_PLAYNUMBER);
					synchronized (lockswim) {
						// 设定长跑比赛计数器,以及treemap的key
						MATCH[SWIM]++;
						matchrunbean.setId(MATCH[SWIM]);
						// 设定游戏类型
						matchrunbean.setType(type);
						// 将宠物标记为游戏状态
						petUser.setMatchid(matchrunbean.getId());
						// 赛跑类型
						petUser.setMatchtype(SWIM);
						// 将宠物放入玩家列表
						matchrunbean.addPlayer(petUser);
						// 将比赛加入长跑比赛的map中
						matchMap[SWIM].put(new Integer(matchrunbean.getId()),
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
					|| (petUser.getHealth() < 50)) {
				doTip(null, "您的宠物不符合参加比赛的条件!");
				request.setAttribute("url", "/pet/swimindex.jsp");
			} else if ((petUser.getMatchtype() == 0)
					&& (petUser.getMatchid() == 0)) {
				// 游戏场次id
				int id = StringUtil.toInt(request.getParameter("id"));
				// 游戏类型
				int type = StringUtil.toInt(request.getParameter("type"));
				synchronized (lockswim) {
					MatchRunBean matchrunbean = (MatchRunBean) matchMap[SWIM]
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
							petUser.setMatchtype(SWIM);
							// 将宠物放入玩家列表
							matchrunbean.addPlayer(petUser);
							// 判断是否开始游戏
							if (matchrunbean.getPeoplenumber() == SWIM_PLAYNUMBER) {
								// 开始游戏
								this.doTip("wait", "游戏中");
								swimStartGame(matchrunbean, loginUser.getId());
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
					MatchRunBean matchrunbean = (MatchRunBean) matchMap[SWIM]
							.get(new Integer(petUser.getMatchid()));
					if (matchrunbean != null) {

						if (matchrunbean.getCondition() == 0) {
							matchrunbean.exitPlayer(petUser);
							// 等待游戏的宠物玩家数量为零时,删除比赛数据
							if (matchrunbean.getPeoplenumber() <= 0)
								matchMap[SWIM].remove(new Integer(matchrunbean
										.getId()));
							// 删除宠物bean中的游戏状态
							petUser.setMatchid(0);
							petUser.setMatchtype(0);

						} else if (matchrunbean.getCondition() == 2) {
							petUser.setMatchid(0);
							petUser.setMatchtype(0);
						}
						request.setAttribute("url", "/pet/swimindex.jsp");
					} else {
						doTip(null, "参数错误");
					}
				}

			}
			// 使用道具
		} else if (task == 4) {		// 使用道具
			if ((petUser.getMatchtype() != 0) && (petUser.getMatchid() != 0)) {
				MatchRunBean matchrunbean = (MatchRunBean) matchMap[SWIM]
						.get(new Integer(petUser.getMatchid()));
				if ((matchrunbean != null)
						&& (matchrunbean.getCondition() == 1)) {
					PlayerBean[] playbean = matchrunbean.getPlayer();
					// 取得是第几名使用道具
					int order = 0;
					for (order = 0; order < SWIM_PLAYNUMBER; order++)
						if ((playbean[order] != null)
								&& (playbean[order].getPetid() == petUser
										.getId()))
							break;
					
					// 如果本人已经过终点线，道具无效
					if (playbean[order].getPosition() < TOTAL_LONG) {
						
						int item = playbean[order].getStage()[0];	// 正使用的道具
						 if (item == 1) {	// 加速器
							// 给自己加50米
							playbean[order].setPosition(playbean[order]
									.getPosition() + 40);
							matchrunbean.addLog(StringUtil
									.toWml(playbean[order].getName())
									+ "在屁股上装了个推进器，嘟嘟嘟嘟冲向前方！");
							// 给第一的人减30米
							// 此人不是第一名
						} else if (item == 2 && order > 0 && playbean[0].getPosition() < TOTAL_LONG) {
							playbean[0]
									.setPosition(playbean[0].getPosition() - 30);
							matchrunbean.addLog(StringUtil
									.toWml(playbean[order].getName())
									+ "用水枪把"
									+ StringUtil.toWml(playbean[0].getName())
									+ "喷得晕头转向，慢了。");
							// 给前边的人减60米
							// 此人不是第一名
						} else if (item == 3 && order > 0 && playbean[order - 1].getPosition() < TOTAL_LONG) {
							// 判断是否有防弹衣道具
							int temp = UserBagCacheUtil.getUserBagById(18,
									playbean[order - 1].getUserid());
							if (temp > 0 && (RandomUtil.percentRandom(STAGE_PROBABILITY))) {
								// 删除道具
								UserBagCacheUtil.UseUserBagCacheById(
										playbean[order - 1].getUserid(), temp);
								matchrunbean.addLog(StringUtil.toWml(StringUtil
										.toWml(playbean[order - 1].getName()))
										+ "的主人给它穿了防弹衣，秤砣失效了！");
							} else {
								playbean[order - 1]
										.setPosition(playbean[order - 1]
												.getPosition() - 40);
								matchrunbean.addLog(StringUtil
										.toWml(playbean[order].getName())
										+ "悄悄把一个秤砣系到"
										+ StringUtil.toWml(playbean[order - 1]
												.getName()) + "腿上，让它沉了下去。");
							}
						} else if (item == 4 && order > 0) {	//使用鱼网
							int orderPosi = playbean[order].getPosition();
							for (int i = 0; i < order; i++) {
								int posi = playbean[i].getPosition()- orderPosi;

								if (posi <= 100 && posi >=50 && playbean[i].getPosition() < TOTAL_LONG) {
									playbean[i].setPosition(playbean[i].getPosition() - 50);
								}
							}
							matchrunbean.addLog(StringUtil.toWml(playbean[order].getName()) + "撒出鱼网，网住了前面在中路游的好几个家伙!");
						} else if (item == 5) {		//使用鱼叉
							int sid = StringUtil.toInt(request.getParameter("id"));

							if(sid>=0 && sid < SWIM_PLAYNUMBER && playbean[sid].getPosition() < TOTAL_LONG)
							{
								playbean[sid].setPosition(playbean[sid].getPosition()-30);
								matchrunbean.addLog(StringUtil.toWml(playbean[order].getName())+"发射渔叉把"
										+ StringUtil.toWml(playbean[sid]
												.getName()) + "钉在了水底!");
							}
							else if(sid == -1){
								doTip("send", "send");
								String url1 = "/pet/player.jsp?id="+ matchrunbean.getId() + "&type=" + matchrunbean.getType();
								request.setAttribute("url",url1);
								return;
							}

						} else if (item == 6 && order < SWIM_PLAYNUMBER-1 && playbean[order+1].getPosition() < TOTAL_LONG) {		// //使用水雷
							playbean[order+1].setPosition(playbean[order+1].getPosition() - 30);
							matchrunbean.addLog(StringUtil.toWml(playbean[order+1].getName())
									+ "在中路游撞上了水雷，被炸沉了");
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
	public void swimStartGame(MatchRunBean matchrunbean, int id) {
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
			// petBean = (PetUserBean) userMap.get(new Integer(playbean[j]
			// .getPetid()));
			// if (petBean == null)
			petBean = load(0, playbean[j].getPetid());
			if (petBean != null) {
				// 加入消息系统,如果是最后进入游戏那个玩家的话,就不发信息
				if (petBean.getUser_id() != id) {
					NoticeBean notice = new NoticeBean();
					notice.setTitle(petBean.getName() + "开始比赛了,快去看吧");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setUserId(petBean.getUser_id());
					notice.setHideUrl("/pet/swimindex.jsp");
					notice.setLink("/pet/swimming.jsp?id=" + matchrunbean.getId()
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
		int x = (int) (petBean.getHealth()
				* (factorBean.getAl() * Math.sqrt(petBean.getAgile())
						+ factorBean.getIn() * Math.sqrt(petBean.getIntel()) + factorBean
						.getSt()
						* Math.sqrt(petBean.getStrength())) / 100);

		return x;
	}

	/**
	 * 
	 * @author liq
	 * @explain，
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static void swimtask() {
		MatchFactorBean factorBean;
		String log;
		Vector eventList;
		MatchEventBean matchEventBean;
		PetUserBean petBean = new PetUserBean();
		float change = 0;
		int factor = 0;
		Iterator iter = matchMap[SWIM].values().iterator();
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
				for (int i = 0; i < SWIM_PLAYNUMBER; i++) {
					if (playbean[i].getPosition() < TOTAL_LONG) {
						// ////////////////////////////////////////////////////////////////////////////////////////////////////
						// 宠物属性因子发生作用
						// 取得宠物id
						petBean = (PetUserBean) userMap.get(new Integer(
								playbean[i].getPetid()));
						if(petBean == null)
							continue;

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
									+ "已经游过了终点线");
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
						if(petBean == null)
							continue;
						petBean.setMatchid(0);
						petBean.setMatchtype(0);

						// 加入消息系统
						NoticeBean notice = new NoticeBean();
						notice.setTitle(petBean.getName() + "取得了第" + (j + 1)
								+ "名,快去看吧");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setUserId(petBean.getUser_id());
						notice.setHideUrl("/pet/swimindex.jsp");
						notice.setLink("/pet/swimming.jsp?task=4&id="
								+ matchrunbean.getId() + "&type="
								+ matchrunbean.getType());
						// liq_2007-7-16_增加宠物消息类型_start
						notice.setMark(NoticeBean.PET);
						// liq_2007-7-16_增加宠物消息类型_end
						noticeService.addNotice(notice);

						if (j == 0) {
							// 根据成绩给玩家加积分
							petBean.setExp(petBean.getExp() + 50);
						} else if (j == 1) {
							petBean.setExp(petBean.getExp() + 45);
						} else if (j == 2) {
							petBean.setExp(petBean.getExp() + 40);
						} else if (j == 3) {
							petBean.setExp(petBean.getExp() + 35);
						} else if (j == 4) {
							petBean.setExp(petBean.getExp() + 30);
						}
						// 更新数据库
						server.updateUser(" exp =" + petBean.getExp()
								+ ",rank =" + petBean.getRank() + ",spot="
								+ petBean.getSpot(), "id = " + petBean.getId());
					}
				}

				// 道具，每圈8个人中挑两个给道具，道具随机
				// 产生中奖的宠物
				playbean[0].inputStage(RandomUtil.randomRateInt(randomRate, randomTotal) + 1);
				for(int i = 1;i < SWIM_PLAYNUMBER;i++) {
					playbean[i].inputStage(RandomUtil.randomRateInt(randomRate2, randomTotal2) + 1);
				}

			} else {
				// 结束后
			}
		}
	}
	
	protected static int randomRate[] = {10,0,0,0,10,10};	// 除了第一名的道具出现几率
	protected static int randomRate2[] = {10,4,10,10,10,0};	// 第一名的道具出现几率
	protected static int randomTotal;
	protected static int randomTotal2;
	static {
		randomTotal = RandomUtil.sumRate(randomRate);
		randomTotal2 = RandomUtil.sumRate(randomRate2);
	}
}
