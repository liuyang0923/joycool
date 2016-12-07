package jc.family.game.ask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;
import jc.family.FamilyUserBean;
import jc.family.FundDetail;
import jc.family.game.ApplyBean;
import jc.family.game.FmApplyBean;
import jc.family.game.GameAction;
import jc.family.game.MatchBean;
import jc.family.game.MemberBean;
import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.util.RandomUtil;

public class AskAction extends GameAction {

	public final static int ASK_MATCH = 3;
	public static String[] quickprompt = {
			"您好不要走开,现在是广告时间:如果是来找人解闷的,请去交友中心,可以看到在线美女帅哥照片日记,交友测评,或者直接聊天.如果您喜欢打游戏赚币的,请去娱乐城,这里有最丰富的手机游戏.如果您爱好赌博,请来通吃岛,21点老虎机拉耗子,让您爽翻天.有不懂的问题,您可以去论坛、聊天室、管理员找人回答,也可以看看新手帮助…………什么?这些你都知道?那让我们继续下一道题吧.",
			"这位施主请留步,您知道大猪说知道,小猪说不知道这句禅语么?…………请不要扁我,我只是来打酱油的.",
			"你要进行下一题么?真的要进行么?那好吧,请点确认吧……………………………………什么?你没看到确认?那就对了~休息 休息一下.",
			"朋友,你猜猜下一道题是什么,你要猜对了我就让你继续答题.…………算了看你怪不容易的,我就直接让你答吧.",
			"我嘞个去,答这么快!你这货开挂了吧.待为师来为你吟诵一首李姑娘的名作《声声慢》:从前有座山,山里有座庙……………………二师兄醒醒,该接着答题了!",
			"炒茄子前,用开水焯一下或用盐腌出苦水,这样就不吸油了",
			" 芒果在煤气炉上转几秒,那皮就很好剥了,番茄剥皮,用开水烫下,皮就容易剥下来了",
			"普通的铁锅,需要煎东西时,先把锅放在火上加热,倒少量油,油热后倒出,再倒入冷油,就变成不粘锅了,煎鱼、水煎包都不粘",
			"炖排骨或汤的时候,放点醋,有利有钙的吸收",
			"喝不完的大瓶装可乐,可以到过来放比较不容易漏气",
			"蒸米饭,在锅里加几滴生油.搅一搅.蒸出来的饭一粒一粒的,很好吃.而且不粘锅.",
			"做猪扒或者牛扒的时候,先将肉块用口乐浸泡１０分钟再煮或煎,肉质会容易酥软美味．也不会残留可乐的味道",
			"洗黑木耳的时候放一点点面粉,会洗下很多脏东西的",
			"剥大蒜之前,用水把整个蒜头泡过,去皮就很容易了.",
			"煮饺子时要添足水,待水开后加入2%的食盐,溶解后再下饺子,能增加面筋的韧性,饺子不会粘皮粘底,汤清饺香",
			"把核桃放进锅里蒸十分钟,取出放在凉水里再砸开,就能取出完整的桃核仁了",
			"将鸡蛋打入碗中,加入少许温水搅拌均匀,倒入油锅里炒时往锅里滴少许酒,这样炒出的鸡蛋蓬松、鲜嫩、可口.",
			"过多食用生葱蒜,会刺激口腔肠胃, 不利健康,最好加一点醋再食用",
			"把虾仁放进碗里,加一点精盐和食用碱粉,抓搓一会儿后,用清水浸泡然后冲洗,炒出的虾仁会透明如水晶,爽嫩可口",
			"做啤酒鸭,红烧猪蹄之类的菜式,在加水焖的时候,从炒锅里倒进电饭煲里面焖,肉更嫩、更香、更滑、水汽还少,特别好吃",
			"洗桃子的时候 ,给水里放点碱面,桃毛自然都漂在水面,而且洗完手还不痒 ,这样洗桃绝对干净",
			"喝酸奶,能解酒后烦躁,酸奶能保护胃黏膜、延缓酒精吸收,并且含钙丰富,对缓解酒后烦躁尤其有效",
			"炒肉时,先把肉用小苏打水浸泡十几分钟,倒掉水后再入味,炒出来会很嫩滑",
			"洗葡萄的时候放些淀粉,随便搓一搓,就完全能把葡萄洗得亮晶晶了",
			"红薯擦成丝状,炒出来味道也很独特,通肠的效果很好",
			"用刀切粘乎乎的东西(年糕、粑粑类)前先把刀淋过冷水,就一点都不粘了",
			"夹生饭重煮法:可用筷子在饭内扎些直通锅底的孔,洒入少许黄酒重焖,若只表面夹生,只要将表层翻到中间再焖即可",
			"如何鉴别鲜蛋:新鲜蛋用灯光照,空头很小,蛋内完全透亮,呈桔红色,蛋内无黑点,无红影.若是要测量蛋的新鲜度,可以将蛋浸在冷水里.如果蛋是平躺在水 里,说明十分新鲜;如果倾斜在水中,则该蛋至少已存放了3～5天了;若是笔直立在水中,可能存放10天之久.此外,若是蛋浮在水面上,那么该蛋十分有可能 已经变质了.",
			"吃过于肥腻的食物后喝茶,能刺激自律神经,促进脂肪代谢" };

	public static AskService service = new AskService();
	public static long outTime = 22 * 1000;
	public static long quickTime = 10 * 1000;
	public static List asklist;
	public static int askGameState = 0;// 0:未开始,1:开始,2:结束
	public static Map fm_nnt_Map = new HashMap();// 参加游戏人数
	public static long cout_entry_fee = 0;// 报名钱数

	public AskAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * 初始化答题数据
	 */
	public static void initaskdata(int mid) {
		MatchBean matchBean = (MatchBean) matchCache.get(Integer.valueOf(mid));
		if (matchBean == null || matchBean.getType() != ASK_MATCH) {// 是否是问答
			return;
		}
		asklist = service.selectAskListbyRandom();// 初始化问答数据
		askGameState = 1;// 改变游戏状态
		matchBean.setState(askGameState);
		service.upd("update fm_game_match set state=1,create_time=now() where id=" + matchBean.getId());
		List fmHomeList = service.getFmApplyList("del!=1 and total_apply>0 and m_id=" + matchBean.getId());
		matchBean.setFmList(fmHomeList);// 得到参赛家族
		for (int i = 0; i < fmHomeList.size(); i++) {
			FmApplyBean fmApplyBean = (FmApplyBean) fmHomeList.get(i);
			matchBean.getGameMap().put(Integer.valueOf(fmApplyBean.getFid()), new Object());

			fm_nnt_Map.put(Integer.valueOf(fmApplyBean.getFid()), new FmOverBean(fmApplyBean.getFid()));// 添加所有家族信息
		}
		List memberList = service.getApplyList("select * from fm_game_apply where state=2 and m_id=" + mid);
		for (int j = 0; j < memberList.size(); j++) {
			ApplyBean applyBean = (ApplyBean) memberList.get(j);
			if (OnlineUtil.isOnline(String.valueOf(applyBean.getUid()))) {
				NoticeAction.sendNotice(applyBean.getUid(), "家族问答活动已经开始", "", NoticeBean.GENERAL_NOTICE, "",
						"/fm/game/fmgame.jsp");// 发送给参加的用户,提示他们雪仗游戏开始了
			}
		}
	}

	/**
	 * 开始答题
	 * 
	 * @return
	 */
	public int beginask() {
		if (askGameState != 1) {
			this.tip = "游戏还未开始,无法进入游戏.(3秒后自动返回家族活动首页)";
			return 0;// 游戏未开始
		}
		FamilyUserBean fmUser = getFmUser();// 用户是否存在
		if (fmUser == null) {
			this.tip = "未登录.(3秒后自动返回家族活动首页)";
			return 0;// 非法登录
		}
		int fmid = fmUser.getFm_id();// 是否加入家族
		if (fmid == 0) {
			this.tip = "家族不正确.(3秒后自动返回家族活动首页)";
			return 0;
		}
		int mid = getParameterInt("mid");
		if (mid == 0 || !checkBlack(mid, fmUser.getId(), fmid)) {// 判断是否参加游戏
			this.tip = "您的报名审核没有通过,不能参加此次比赛!(3秒后自动返回家族活动首页)";
			return 0;
		}
		// FmApplyBean fmApplyBean = service.getFmApplyBean(" fid=" + fmid +
		// " and m_id=" + mid);
		// if (fmApplyBean.getTotalApply() < 5) {// 判断报名人数如果小于5人将不允许参赛
		// this.tip = "家族报名人数少于5人,无法加入游戏(3秒后自动返回家族活动首页)";
		// return 0;
		// }
		Overbean overbeans = (Overbean) getAttribute2("overbean");
		if (overbeans != null) {
			return fmid;
		}
		Integer fmId = Integer.valueOf(fmid);// 已经答题过
		FmOverBean fmOverBean = (FmOverBean) fm_nnt_Map.get(fmId);
		if (fmOverBean != null && fmOverBean.userOverBean.get(Integer.valueOf(fmUser.getId())) != null) {
			return -1;
		}

		MatchBean matchBean = (MatchBean) matchCache.get(Integer.valueOf(mid));
		matchBean.setState(askGameState);
		FamilyHomeBean familyHomeBean = getFmByID(fmid);
		if (familyHomeBean != null) {
			long money = familyHomeBean.getMoney();
			if (money < 10000000) {
				this.tip = "家族基金不足,无法进入比赛.(3秒后自动返回家族活动首页)";
				return 0;// 家族基金不够
			}
			service.deductFmMoney(fmid, fmUser.getId(), 10000000);// 扣钱
			familyHomeBean.setMoney(familyHomeBean.getMoney() - 10000000);
			
			FamilyAction.service.insertFmFundDetail(fmid, -10000000l, FundDetail.GAME_TYPE,
					familyHomeBean.getMoney());
			
			service.upd("update fm_game_apply set is_pay=1 where m_id=" + mid + " and uid=" + fmUser.getId());// 更新状态为付款

			cout_entry_fee += 10000000;// 记录钱数,最后分钱用

			fmOverBean = (FmOverBean) fm_nnt_Map.get(fmId);
			if (fmOverBean == null) {
				fmOverBean = new FmOverBean();
			}
			fmOverBean.fm_entry_fee += 10000000l;// 记录家族扣得总钱数
			fmOverBean.fmid = fmid;
			Map overbeanMap = fmOverBean.userOverBean;
			Overbean overbean = new Overbean();
			overbean.userid = fmUser.getId();
			overbeanMap.put(Integer.valueOf(fmUser.getId()), overbean);
			fm_nnt_Map.put(fmId, fmOverBean);// 增加答题家族
			setAttribute2("overbean", overbean);
			return fmid;
		}
		return 0;// 非法登录
	}

	/**
	 * 结束答题
	 * 
	 * DOTO 这里操作数据库数:家族数*(4+家族成员数*2)
	 */
	public static void endaskdata(int mid) {
		askGameState = 2;
		asklist = null;
		MatchBean matchBean = (MatchBean) matchCache.get(Integer.valueOf(mid));
		if (matchBean == null || matchBean.getType() != ASK_MATCH) {// 是否是问答
			return;
		}
		matchBean.setState(askGameState);
		service.upd("update fm_game_match set state=2,end_time=now() where id=" + matchBean.getId());
		List list = new ArrayList();
		int teammun = fm_nnt_Map.size();// 参赛家族的数量
		Iterator iterator = fm_nnt_Map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			FmOverBean fmOverBean = (FmOverBean) entry.getValue();
			Iterator overiterator = fmOverBean.userOverBean.entrySet().iterator();
			while (overiterator.hasNext()) {// 遍历，累计积分
				Map.Entry overentry = (Map.Entry) overiterator.next();
				Overbean overBean = (Overbean) overentry.getValue();
				if (overBean.right + overBean.wrong < 30) {// 到时间未完成答题
					overBean.wrong = 30 - overBean.right;
				}
				fmOverBean.right += overBean.right;// 答对的总数
				fmOverBean.ask_score += overBean.right * 2 - overBean.wrong;// 问答积分
				fmOverBean.nnt += 1;// 参赛人数加1;
			}
			list.add(fmOverBean);
		}
		Collections.sort(list);
		for (int top = 1; top <= list.size(); top++) {
			FmOverBean fmOverBean = (FmOverBean) list.get(top - 1);
			if (top == 1) {// 10分7分5分，第四第五名得3分
				fmOverBean.prize = cout_entry_fee * 5 / 10;// 第一名，获得全部报名费和过程花费的50%
				fmOverBean.score = 10;
				service.addMovement(fmOverBean.fmid,"本轮家族问答","game/ask/historyDetails.jsp?mid="+matchBean.getId());
			}
			if (top == 2) {
				fmOverBean.prize = cout_entry_fee * 3 / 10;// 第二名获得30%
				fmOverBean.score = 7;
			}
			if (top == 3) {
				fmOverBean.prize = cout_entry_fee * 15 / 100;// 第三名获得15%
				fmOverBean.score = 5;
			}
			if (top == 4 || top == 5) {
				fmOverBean.score = 3;
			}
			if (top > 5) {// 家族报名没有到前5名的，答对题数20道题以上的给予2分，
				if (fmOverBean.right > 20) {
					fmOverBean.score = 2;
				} else if (fmOverBean.right > 10) {// 答对题数10道题以上的给予1分
					fmOverBean.score = 1;
				}
			}
			fmOverBean.exploit = (int) (teammun / Math.pow(top, 0.6) * 10 + fmOverBean.right);// INT(参赛家族数量/(名次^0.6)*10+家族答对题目数)
			if (fmOverBean.exploit < 0) {
				fmOverBean.exploit = 0;
			}
			if (fmOverBean.right > 9) {// 总答对题数>=10的家族，可以获得1点家族经验
				fmOverBean.game_num = 1;
				addFmGameInfo(fmOverBean.fmid, 1);
			}
			service.insertaskfmscore(mid, fmOverBean.fmid, fmOverBean.ask_score, fmOverBean.nnt, top, fmOverBean.score,
					fmOverBean.prize, fmOverBean.game_num);// 保存家族游戏信息
			if (fmOverBean.fm_entry_fee > 0) {
				addFundHistory(fmOverBean.fmid, "扣除问答报名费" + fmOverBean.fm_entry_fee);
			}
			addScore(fmOverBean.fmid, 3, fmOverBean.score);// 积分累加
			// addFmGameInfo(fmOverBean.fmid, 0, 1, "第" + top + "名", 1, mid);
			updateFmFund(fmOverBean.fmid, fmOverBean.prize);
			
			FamilyHomeBean fmHome = FamilyAction.getFmByID(fmOverBean.fmid);
			FamilyAction.service.insertFmFundDetail(fmOverBean.fmid, fmOverBean.prize, FundDetail.GAME_TYPE,
					fmHome.getMoney());
			
			Iterator overiterator = fmOverBean.userOverBean.entrySet().iterator();
			while (overiterator.hasNext()) {
				Map.Entry overentry = (Map.Entry) overiterator.next();
				Overbean overBean = (Overbean) overentry.getValue();
				overBean.exploit = fmOverBean.exploit * overBean.right / (fmOverBean.right == 0 ? 1 : fmOverBean.right);// 总贡献度*个人答对题目数/家族答对题目数
				if (overBean.exploit == 0) {
					overBean.exploit = 1;
				}
				service.insertaskscore(overBean.userid, fmOverBean.fmid, mid, overBean.right, overBean.wrong,
						overBean.right * 2 - overBean.wrong, overBean.exploit);// 个人信息累加
				addFmUserGameInfo(overBean.userid, fmOverBean.fmid, overBean.exploit, "参加问答|" + overBean.exploit);
			}

		}
		// // 扣积分
		// List temp = matchBean.getFmList();
		// List list2 = new ArrayList();
		// for (int i = 0; i < temp.size(); i++) {
		// FmApplyBean fmapplyBean = (FmApplyBean) temp.get(i);
		// list2.add(Integer.valueOf(fmapplyBean.getFid()));
		// }
		// List list3 = new ArrayList();
		// for (int i = 0; i < list.size(); i++) {
		// FmOverBean fmOverBean = (FmOverBean) list.get(i);
		// list3.add(Integer.valueOf(fmOverBean.fmid));
		// }
		// if (list2.removeAll(list3)) {// 报名未参赛，扣分
		// for (int i = 0; i < list2.size(); i++) {
		// Integer fmid = (Integer) list2.get(i);
		// fineFamilyGameScore(fmid.intValue(), 5, 3);
		// }
		// }
		cout_entry_fee = 0;
		fm_nnt_Map.clear();
	}

	/**
	 * 答题
	 * 
	 * @return
	 */
	public int nextask() {
		if (askGameState == 0) {
			removeAttribute2("overbean");
			return 1;// 游戏未开始
		}
		if (askGameState == 2) {
			removeAttribute2("overbean");
			return 5;// 游戏结束
		}
		if (asklist == null || asklist.size() != 30) {// 未开始
			removeAttribute2("overbean");
			return 1;
		}
		Overbean overbean = (Overbean) getAttribute2("overbean");
		if (overbean == null) {// 游戏结束
			return 5;
		}
		long nowtime = System.currentTimeMillis();
		int answer = getParameterInt("a");
		if (answer == 0 && overbean.index == 0 && overbean.exploit != -1) {// 第一题计时,用功勋判断是否头一次进入ask页面
			overbean.exploit = -1;
			overbean.timer = System.currentTimeMillis();
		}
		if (answer != 0 && nowtime - overbean.timer < quickTime) {// 答题过快
			long time = (overbean.timer + quickTime - nowtime) / 100;
			setAttribute("quickTime", time + 1);
			setAttribute("answer", answer);
			return 3;
		}
		if (nowtime > overbean.timer + outTime) {// 答题超时
			long muber = (nowtime - overbean.timer) / outTime;

			if (overbean.index + muber > 28) {// 答完
				overbean.wrong = 30 - overbean.right;
				return 5;
			}
			overbean.timer = System.currentTimeMillis();
			overbean.index += muber;
			overbean.wrong += muber;
			setAttribute("askBean", asklist.get(overbean.index));
			setAttribute("index", overbean.index + 1);
			setAttribute("note", overbean.index);
			return 4;
		}

		if (answer != 0) {// 是否从超时页跳回
			AskBean askBean = (AskBean) asklist.get(overbean.index);// 判断是否正确
			if (answer == askBean.getRightanswers()) {
				overbean.right += 1;
			} else {
				overbean.wrong += 1;
			}
			overbean.total += 1;
			if (overbean.index == 29) {// 答完
				return 5;
			}
			overbean.index += 1;
			overbean.timer = System.currentTimeMillis();
		}
		setAttribute("askBean", asklist.get(overbean.index));
		setAttribute("index", overbean.index + 1);
		return 6;
	}

	/**
	 * 答题完成
	 * 
	 * @return
	 */
	public String overask() {
		int mid = getParameterInt("mid");
		Overbean overbean = (Overbean) getAttribute2("overbean");
		FamilyUserBean fmUser = getFmUser();// 用户是否存在
		if (fmUser == null) {
			return "";// 非法登录
		}
		int fmid = fmUser.getFm_id();// 是否加入家族
		if (fmid == 0 || mid == 0) {
			return "";
		}
		if (overbean != null) {
			removeAttribute2("overbean");
			return "答题结束<br/>总计答题数:30<br/>正确数:" + overbean.right + "<br/>错误数:" + overbean.wrong + "<br/>个人得分:"
					+ (overbean.right * 2 - overbean.wrong);
		}
		FmOverBean fmOverBean = (FmOverBean) fm_nnt_Map.get(Integer.valueOf(fmid));
		if (fmOverBean != null) {
			Map overbeanMap = fmOverBean.userOverBean;
			overbean = (Overbean) overbeanMap.get(Integer.valueOf(fmUser.getId()));
			if (overbean != null) {
				return "答题结束<br/>总计答题数:30<br/>正确数:" + overbean.right + "<br/>错误数:" + overbean.wrong + "<br/>个人得分:"
						+ (overbean.right * 2 - overbean.wrong);
			}
		}
		MemberBean memberBean = service.selectMyGameMember(mid, fmUser.getId());
		if (memberBean != null) {
			return "答题结束<br/>总计答题数:30<br/>正确数:" + memberBean.getAsk_right() + "<br/>错误数:" + memberBean.getAsk_wrong()
					+ "<br/>个人得分:" + (memberBean.getAsk_right() * 2 - memberBean.getAsk_wrong());
		}
		return "";
	}

	/**
	 * 得到随机提示语
	 * 
	 * @return
	 */
	public String getQuickPrompt() {
		return quickprompt[RandomUtil.nextInt(quickprompt.length)];
	}

	/**
	 * 得到题目列表
	 * 
	 * @return
	 */
	public List getAskBeanList() {
		int joinConut = service.getIntResult("select count(id) from fm_game_ask");
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, joinConut, 10, "p");
		List list = service.selectAskList(paging.getStartIndex(), paging.getCountPerPage());
		String s = paging.shuzifenye("asklist.jsp", false, "|", response);
		setAttribute("pages", s);
		setAttribute("count", joinConut);
		return list;
	}

	/**
	 * 得到一个题目
	 * 
	 * @return
	 */
	public AskBean getAskBean() {
		int askid = getParameterInt("askid");
		if (askid == 0) {
			return null;
		}
		return service.selectAskBean(askid);
	}

	/**
	 * 添加新题目
	 * 
	 * @return
	 */
	public boolean addAskBean() {
		String question = getParameterString("question");
		String answer1 = getParameterString("answer1");
		String answer2 = getParameterString("answer2");
		String answer3 = getParameterString("answer3");
		String answer4 = getParameterString("answer4");
		int rightanswers = getParameterInt("rightanswers");

		if (rightanswers == 0 || rightanswers > 4) {// 没有选择答案
			return false;
		}
		AskBean askBean = new AskBean(question, answer1, answer2, answer3, answer4, rightanswers);

		return service.insertAskBean(askBean);
	}

	/**
	 * 修改题目
	 * 
	 * @return
	 */
	public boolean editAskBean() {
		int askid = getParameterInt("askid");
		String question = getParameterString("question");
		String answer1 = getParameterString("answer1");
		String answer2 = getParameterString("answer2");
		String answer3 = getParameterString("answer3");
		String answer4 = getParameterString("answer4");
		int rightanswers = getParameterInt("rightanswers");

		if (askid == 0 || rightanswers == 0 || rightanswers > 4) {// 没有选择答案
			return false;
		}
		AskBean askBean = new AskBean(askid, question, answer1, answer2, answer3, answer4, rightanswers);

		return service.updateAskBean(askBean);
	}

	/**
	 * 删除题目
	 * 
	 * @return
	 */
	public boolean deleteAskBean() {
		int askid = getParameterInt("askid");
		if (askid == 0) {
			return false;
		}
		return service.executeUpdate("delete from fm_game_ask where id=" + askid);
	}

	/**
	 * 删除所有题目
	 * 
	 * @return
	 */
	public boolean deleteAllAsk() {
		return service.executeUpdate("truncate table fm_game_ask");
	}

	/**
	 * 得到问答历史记录
	 * 
	 * @return
	 */
	public List getFmAskHistoryList() {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return null;// 不是家族成员
		}
		int c = service.selectIntResult("select count(id) from fm_game_match where game_type=3 and state=2");
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		List list = service.selectAskHistoryList(fmUser.getFm_id(), paging.getStartIndex(), paging.getCountPerPage());
		String s = paging.shuzifenye("history.jsp", false, "|", response);
		setAttribute("paging", s);
		return list;
	}

	/**
	 * 得到本家族的一场比赛的问答记录
	 * 
	 * @return
	 */
	public List getFmAskHistory() {
		int mid = getParameterInt("mid");
		if (mid == 0) {
			return null;
		}
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || fmUser.getFm_id() == 0) {
			return null;// 不是家族成员
		}
		int c = service.selectIntResult("select count(id) from fm_game_game where game_type=3 and m_id=" + mid);
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		List list = service.selectAskHistory(mid, paging.getStartIndex(), paging.getCountPerPage());
		setAttribute("myGameBean", service.selectMyFmAskHistory(mid, fmUser.getFm_id()));
		String s = paging.shuzifenye("historyDetails.jsp?mid=" + mid, true, "|", response);
		setAttribute("paging", s);
		return list;
	}

	/**
	 * 得到本场历史记录
	 * 
	 * @return
	 */
	public List getRecord() {
		int mid = getParameterInt("mid");
		if (mid == 0) {
			mid = service.selectIntResult("select id from fm_game_match where game_type=3 order by id desc limit 1");
		}
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null || fmUser.getFm_id() == 0) {
			return null;// 不是家族成员
		}
		int c = service.selectIntResult("select count(id) from fm_game_member where m_id=" + mid + " and fid="
				+ fmUser.getFm_id());
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		List list = service.selectGameMember(mid, fmUser.getFm_id(), paging.getStartIndex(), paging.getCountPerPage());
		setAttribute("myMemberBean", service.selectMyGameMember(mid, fmUser.getId()));
		setAttribute("myGameBean", service.selectMyFmAskHistory(mid, fmUser.getFm_id()));
		String s = paging.shuzifenye("record.jsp?mid=" + mid, true, "|", response);
		setAttribute("paging", s);
		return list;
	}
}

/**
 * 答题信息
 * 
 * @author qiuranke
 * 
 */
class Overbean {
	int userid = 0;
	int index = 0;
	int total = 0;
	int right = 0;
	int wrong = 0;
	int exploit = 0;
	long timer = System.currentTimeMillis();
}

/**
 * 家族答题信息
 * 
 * @author qiuranke
 * 
 */
class FmOverBean implements Comparable {
	Map userOverBean = new HashMap();
	long fm_entry_fee = 0;
	long prize = 0;
	int score = 0;
	int ask_score = 0;
	int nnt = 0;
	int exploit = 0;
	int right = 0;
	int fmid = 0;
	int game_num = 0;

	FmOverBean() {

	}

	FmOverBean(int fmid) {
		this.fmid = fmid;
	}

	public int compareTo(Object o) {
		if (o instanceof FmOverBean) {
			if (ask_score < ((FmOverBean) o).ask_score) {
				return 1;
			} else if (ask_score > ((FmOverBean) o).ask_score) {
				return -1;
			} else {
				return 0;
			}
		} else {
			throw new ClassCastException("Can't compare");
		}

	}

	public boolean equals(Object obj) {
		if (obj instanceof FmOverBean) {
			if (right == ((FmOverBean) obj).right) {
				return true;
			}
		}
		return false;
	}
}