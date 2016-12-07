package jc.family.game.boat;

import java.util.*;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;
import jc.family.FundDetail;
import jc.family.game.*;
import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.util.*;

public class BoatAction extends GameAction {
	public BoatAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	public static String IMAGE_URL = Constants.RESOURCE_ROOT_PATH + "family/boat/";
	public static BoatService service = new BoatService();
	public static TreeMap accidentMap = null;
	public static Timer boatTimer = new Timer("boatTimer");
	public static int millis1 = 8000;
	public static int millis2 = 60000;
	public static String[] boatType = {"传统","爆裂","尖峰","凤凰"};
	
	// 是否报名,是否通过审核,家族是否禁赛,是否扣钱
	public boolean canSee(MatchBean matchBean,int mid, int fid, int uid){
		boolean canSee = false;
		if(matchBean != null){
			if(matchBean.getState() == 1 || matchBean.getState() == 2){
				ApplyBean applyBean = service.getApplyBean(" m_id="+mid+" and uid="+uid+" and fid="+fid);
				// 用户是否通过审核
				if(applyBean != null){
					if(applyBean.getState() == 2){
						FmApplyBean fmApplyBean = null;
						List fmList = matchBean.getFmList();
						// 查看参赛家族中是否已经存在玩家所在家族信息
						for(int i=0;i<fmList.size();i++){
							FmApplyBean tempFmApply = (FmApplyBean) fmList.get(i);
							if(tempFmApply.getFid() == fid)
								fmApplyBean = tempFmApply;
						}
						// fmApplyBean=null家族管理员禁玩,或者游戏开始前没人报名
						if(fmApplyBean != null){
							HashMap gameMap = matchBean.getGameMap();
							BoatGameBean bean = (BoatGameBean)gameMap.get(new Integer(fid));
							if(bean == null){
								bean = BoatAction.initializeFmGame(fid,mid);
								gameMap.put(new Integer(fid),bean);
							}
							if(bean.getIsover() != 2){
								HashMap memberMap = bean.getMemberMap();
								Integer memberKey = new Integer(uid);
								MemberBean memberBean = (MemberBean) memberMap.get(memberKey);
								if(applyBean.getIsPay() == 1){
									canSee = true;
								} else {
									// 玩家首次进入,扣取1000W报名费
									FamilyHomeBean familyHomeBean = GameAction.getFmByID(fid);
									long money = familyHomeBean.getMoney();
									int joinRent = 10000000;
									if(memberBean != null){
										if(money >= joinRent){
											pay(familyHomeBean, matchBean, bean, mid, fid, uid, joinRent);
											bean.setNumTotal(bean.getNumTotal() + 1);
											canSee = true;
										}else{
											request.setAttribute("tip", "家族基金不足,无法入赛!");
										}
									}else{
										memberBean = new MemberBean();
										memberBean.setMid(mid);
										memberBean.setFid(fid);
										memberBean.setUid(uid);
										memberMap.put(new Integer(uid),memberBean);
										if(money >= joinRent){
											pay(familyHomeBean, matchBean, bean, mid, fid, uid, joinRent);
											bean.setNumTotal(bean.getNumTotal() + 1);
											canSee = true;
										}else{
											request.setAttribute("tip", "家族基金不足,无法入赛!");
										}
									}
								}
							} else {
								canSee = true;
							}
						}else{
							request.setAttribute("tip", "您家族没有报名参与此游戏!");
						}
					}else{
						request.setAttribute("tip", "您尚未通过审核!");
					}
				} else {
					request.setAttribute("tip", "您还没报名!");
				}
			}else{
				request.setAttribute("tip", "比赛还没开始哦!");
			}
		} else {
			request.setAttribute("tip", "赛事为空!");
		}
		return canSee;
	}
	
	// 扣钱
	public void pay(FamilyHomeBean familyHomeBean, MatchBean matchBean, BoatGameBean bean, int mid, int fid, int uid, int joinRent) {
		service.deductFmMoney(fid,uid,joinRent);
		service.upd("update fm_game_apply set is_pay=1 where m_id="+mid+" and uid="+uid+" and fid="+fid);
		familyHomeBean.setMoney(familyHomeBean.getMoney() - joinRent);
		matchBean.setTotalCoin(matchBean.getTotalCoin()+joinRent);
		bean.setTotalCoin(bean.getTotalCoin()+joinRent);
		
		FamilyAction.service.insertFmFundDetail(fid, -joinRent, FundDetail.GAME_TYPE,
				familyHomeBean.getMoney());
	}
	
	// 超过8秒钟,计算玩家操作数值
	public static void operate(int mid) {
		Integer key1 = new Integer(mid);
		MatchBean matchBean = (MatchBean) BoatAction.matchCache.get(key1);
		// 取出正在游戏中的龙舟游戏
		if (matchBean != null && matchBean.getType() == 1 && matchBean.getState() == 1) {
			HashMap gameMap = matchBean.getGameMap();
			List fmList = matchBean.getFmList();
			if(fmList != null && fmList.size() > 0){
				for (int j = 0; j < fmList.size(); j++) {
					FmApplyBean fab = (FmApplyBean) fmList.get(j);
					Integer key = new Integer(fab.getFid());
					synchronized (gameMap) {
						BoatGameBean bean = (BoatGameBean) gameMap.get(key);
						if (bean != null && bean.getIsover() < 2) {
							for (int i = 0; i < bean.getSeat().length; i++) {
								MemberBean mb = (MemberBean)bean.getSeat()[i];
								if(mb == null)
									continue;
								if (i == 10) {// 掌舵人
									if (mb.getHit() == 1) {
										bean.setAngle(bean.getAngle() - 1);
										mb.setHit(0);
										mb.setTotalHit(mb.getTotalHit() + 1);
									} else if (mb.getHit() == 2) {
										bean.setAngle(bean.getAngle() + 1);
										mb.setHit(0);
										mb.setTotalHit(mb.getTotalHit() + 1);
									}
								} else {// 普通人
									if (mb.getHit() == 1) {
										bean.setSpeedNum(bean.getSpeedNum() + 1);
										mb.setHit(0);
										mb.setTotalHit(mb.getTotalHit() + 1);
									} else if (mb.getHit() == 2) {
										if (i % 2 == 0) {// 左排的人向右划
											bean.setAngle(bean.getAngle() + 1);
										} else {// 右排的人向左划
											bean.setAngle(bean.getAngle() - 1);
										}
										mb.setHit(0);
										mb.setTotalHit(mb.getTotalHit() + 1);
									}
								}
							}
							// 整理数据
							bean = order(bean);
							bean.setDistance(bean.getDistance() + bean.getSpeed() * 8/ 60f);
							// 清空玩家操作信息
							bean.setSpeedNum(0);
							// 距离不能为负
							if(bean.getDistance() < 0){
								bean.setDistance(0);
							}
							// 划完全程,赛事完成
							if (bean.getDistance() >= bean.getAllDistance()) {
								matchBean.getCompleteList().add(bean);
							}
							if(matchBean.getCompleteList().size() > 0){
								orderCompleteList(matchBean);
							}
						}
					}
				}
			}
		}
	}

	// 如果超过1分钟,加入随机事件因素
	public static void operate2(int mid){
		Integer key1 = new Integer(mid);
		MatchBean matchBean = (MatchBean) BoatAction.matchCache.get(key1);
		// 取出正在游戏中的龙舟游戏
		if (matchBean != null && matchBean.getType() == 1 && matchBean.getState() == 1) {
			AccidentBean acb = getRandomAcc();
			if(acb == null)
				return;
			HashMap gameMap = matchBean.getGameMap();
			List fmList = matchBean.getFmList();
			for (int j = 0; j < fmList.size(); j++) {
				FmApplyBean fab = (FmApplyBean) fmList.get(j);
				Integer key = new Integer(fab.getFid());
				synchronized (gameMap) {
					BoatGameBean bean = (BoatGameBean) gameMap.get(key);
					if (bean != null && bean.getIsover() < 2) {
						if(acb != null){
							if (acb.getDistanceType() > 0) {
								int distance1 = acb.getDistance1();
								int distance2 = acb.getDistance2();
								if(distance1 > 0 && distance2 > 0 && distance1 <= distance2){
									if(distance1 > bean.getAllDistance())
										distance1 = bean.getAllDistance();
									if(distance2 > bean.getAllDistance())
										distance2 = bean.getAllDistance();
									int distance = 0;
									if(distance1 == distance2)
										distance = distance1;
									else 
										distance = RandomUtil.nextInt(distance1, distance2);
									if (acb.getDistanceType() == 1) {
										bean.setDistance(bean.getDistance() + distance);
									} else if (acb.getDistanceType() == 2) {
										bean.setDistance(bean.getDistance() - distance);
									}
								}
							}
							if (acb.getSpeedType() > 0) {
								int speed1 = acb.getSpeed1();
								int speed2 = acb.getSpeed2();
								if(speed1 > 0 && speed2 > 0 && speed1 <= speed2){
									if(speed1 > 100)
										speed1 = 100;
									if(speed2 > 100)
										speed2 = 100;
									int speed = 0;
									if(speed1 == speed2)
										speed = speed1;
									else 
										speed = RandomUtil.nextInt(speed1, speed2);
									if (acb.getSpeedType() == 1) {
										float lineSpeed = bean.getLineSpeed() * (1 + speed / 100f);
										bean.setLineSpeed(lineSpeed);
									} else if (acb.getSpeedType() == 2) {
										float lineSpeed = bean.getLineSpeed() * (1 - speed / 100f);
										bean.setLineSpeed(lineSpeed);
									}
								}
							}
							if (acb.getAngleType() > 0) {
								int angle1 = acb.getAngle1();
								int angle2 = acb.getAngle2();
								if(angle1 > 0 && angle2 > 0 && angle1 <= angle2){
									int angle = 0;
									if(angle1 == angle2)
										angle = angle1;
									else 
										angle = RandomUtil.nextInt(angle1, angle2);
									if(angle > 40)
										angle = 40;
									if (0 == RandomUtil.nextInt(2)) {
										bean.setAngle(bean.getAngle() + angle);
									} else {
										bean.setAngle(bean.getAngle() - angle);
									}
								}
							}
							bean.setAccident(acb.getBak());
							bean.setAccidentImg(acb.getBigImg());
							// 整理数据
							bean = order(bean);
							// 距离不能为负
							if(bean.getDistance() < 0){
								bean.setDistance(0);
							}
							// 划完全程,赛事完成
							if (bean.getDistance() >= bean.getAllDistance()) {
								matchBean.getCompleteList().add(bean);
							}
							if(matchBean.getCompleteList().size() > 0){
								orderCompleteList(matchBean);
							}
						}
					}
				}
			}
		}
	}
	
	// 整理数据
	public static BoatGameBean order(BoatGameBean bean){
		// 向左不能超过20度
		if(bean.getAngle() < -20){
			bean.setAngle(-20);
		}
		// 向右不能超过20度
		if(bean.getAngle() > 20){
			bean.setAngle(20);
		}
		// 算出当前速度(speed=(speed+accNum*acc)*(1-angle*0.02f)),已行驶距离
		float lineSpeed = bean.getLineSpeed() + bean.getSpeedNum()* bean.getBoat().getSpeed();
		if(lineSpeed > bean.getBoat().getMaxSpeed()){
			lineSpeed = bean.getBoat().getMaxSpeed();
		}
		float speed = (lineSpeed * (1 - Math.abs(bean.getAngle()) * 0.02f));
		bean.setLineSpeed(lineSpeed);
		bean.setSpeed(speed);
		// 最大速度限制
		if(bean.getSpeed() > bean.getBoat().getMaxSpeed()){
			bean.setSpeed(bean.getBoat().getMaxSpeed());
		}
		// 速度不能为负数
		if(bean.getSpeed() < 0){
			bean.setSpeed(0);
		}
		// 记录最大值
		if (bean.getSpeed() > bean.getMaxSpeed()) {
			bean.setMaxSpeed(bean.getSpeed());
		}
		return bean;
	}
	
	// 记录已划完赛事的家族的排名
	public static void orderCompleteList(MatchBean matchBean){
		List list = matchBean.getCompleteList();
		// 按照 超过赛程距离/当前速度=超过时间 排序,超过时间大的靠前(排名高)
		list = sortByLastTime(list);
		if(list.size() > 0){
			for(int i=0;i<list.size();i++){
				BoatGameBean bean = (BoatGameBean) list.get(i);
				bean.setIsover(2);
				// 龙舟失效
				int fmBoatId = bean.getBoat().getFmBoatId();
				int useTime = bean.getBoat().getUseTime();
				if(bean.getBoat().getId() != 1 && useTime > 0)
					service.upd("update fm_game_fmboat set is_use = 1,use_time=use_time - 1 where id=" + fmBoatId);
				// 整理排名 用时
				matchBean.setRankNum(matchBean.getRankNum() + 1);
				bean.setRank(matchBean.getRankNum());
				bean.setSpendTime(System.currentTimeMillis() - matchBean.getStarttime().getTime());
				bean.setDistance(bean.getAllDistance());
			}
		}
		// 赛事中list清空
		matchBean.setCompleteList(new ArrayList());
	}

	// 坐下and站起
	public void sit(BoatGameBean bean, int uid) {
		int a = this.getParameterInt("a");
		if(a <= 0 || a > 2)
			return;
		int i = this.getParameterIntS("i");
		Object[] seat = bean.getSeat();
		if(i < 0 || i >= seat.length)
			return;
		MemberBean mb = (MemberBean) seat[i];
		if(1 == a){// 坐下
			if(mb != null && mb.getUid() > 0)
				return;
			MemberBean self = (MemberBean) bean.getMemberMap().get(new Integer(uid));
			if(self == null)
				return;
			if(self.getSeat() != -1)
				return;
			self.setSeat(i);
			seat[i] = self;
		}else if(2 == a){// 站起
			if(mb == null || mb.getUid() != uid)
				return;
			mb.setSeat(-1);
			seat[i] = null;
		}
	}
	

	// 随机获得单一事件
	public static AccidentBean getRandomAcc() {
		TreeMap accMap = getAccidentMap();
		// 随机事件
		int sum = ((Integer) accMap.get(new Integer(10000))).intValue();
		if(sum==0){
			return null;
		}
		int rand = RandomUtil.nextInt(sum) + 1;
		if(rand == 10000)
			return null;
		Set keys = accMap.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			int area = key.intValue();
			if(area == 10000)
				continue;
			if (area >= rand) {
				AccidentBean accidentBean = (AccidentBean) accMap.get(key);
				return accidentBean;
			}
		}
		return null;
	}
	
	// 将龙舟随机事件加入缓存
	public static TreeMap getAccidentMap() {
		if (accidentMap != null && accidentMap.size() > 0)
			return accidentMap;
		accidentMap = new TreeMap();
		List list = service.getAccidentList("1");
		int area = 0;
		for (int i = 0; i < list.size(); i++) {
			AccidentBean bean = (AccidentBean) list.get(i);
			area += bean.getPercent();
			accidentMap.put(new Integer(area), bean);
		}
		accidentMap.put(new Integer(10000), new Integer(area));
		return accidentMap;
	}
	
	// 获得一场赛事以及参赛家族
	public MatchBean getAMatch(int mid){
		if(mid < 0)
			return null;
		MatchBean bean = service.getMatchBean("id="+mid);
		if(bean == null)
			return null;
		bean = getMatchFmGameList(bean);
		return bean;
	}
	
	// 某一赛事所有参赛家族信息
	public MatchBean getMatchFmGameList(MatchBean matchBean ){
		List fmGameList = service.getBoatGameList("game_type=1 and m_id=" + matchBean.getId()+" order by rank");
		if(fmGameList != null && fmGameList.size() > 0){
			HashMap gameMap = matchBean.getGameMap();
			List fmList = new ArrayList();
			for(int i = 0; i < fmGameList.size(); i++){
				BoatGameBean gb = (BoatGameBean) fmGameList.get(i);
				gameMap.put(new Integer(gb.getFid1()), gb);
				fmList.add(new Integer(gb.getFid1()));
			}
			matchBean.setFmList(fmList);
		}
		return matchBean;
	}
	
	// 根据行驶路程排序,路程大的靠前
	public static List sortByDistance(List list) {
		if (list == null || list.size() <= 1)
			return list;
		BoatGameBean bean1 = null;
		BoatGameBean bean2 = null;
		for (int i = 0; i < list.size() - 1; i++) {
			bean1 = (BoatGameBean) list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				bean2 = (BoatGameBean) list.get(j);
				if (bean1.getDistance() < bean2.getDistance()) {
					list.set(i, bean2);
					list.set(j, bean1);
					bean1 = bean2;
				}
			}
		}
		return list;
	}
	
	// 按照 超过赛程距离/当前速度=超过时间 排序,超过时间大的靠前(排名高)
	public static List sortByLastTime(List list){
		if (list == null || list.size() <= 0)
			return null;
		if(list.size() == 1)
			return list;
		BoatGameBean bean1 = null;
		BoatGameBean bean2 = null;
		for (int i = 0; i < list.size(); i++) {
			bean1 = (BoatGameBean) list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				bean2 = (BoatGameBean) list.get(j);
				float bean1Distance = bean1.getDistance() - bean1.getAllDistance();
				float bean2Distance = bean2.getDistance() - bean2.getAllDistance();
				float bean1Speed = bean1.getSpeed();
				float bean2Speed = bean2.getSpeed();
				if (bean1Distance/bean1Speed < bean2Distance/bean2Speed) {
					list.set(i, bean2);
					list.set(j, bean1);
					bean1 = bean2;
				}
			}
		}
		return list;
	}

	// 初始化某一赛事各个家族游戏信息
	public static void initializeMatch(MatchBean matchBean){
		if(matchBean == null)
			return;
		synchronized (matchBean) {
			matchBean.setBoatTask(new FamilyGameTask(matchBean.getId(), 3));
			matchBean.setBoatTask2(new FamilyGameTask(matchBean.getId(), 4));
			boatTimer.schedule(matchBean.getBoatTask(), new Date(System.currentTimeMillis() + millis1), millis1);
			boatTimer.schedule(matchBean.getBoatTask2(), new Date(System.currentTimeMillis() + millis2), millis2);
			List fmList = service.getFmApplyList("del!=1 and total_apply>0 and m_id=" + matchBean.getId());
			if(fmList != null){
				if(fmList.size() > 0){
					HashMap gameMap = matchBean.getGameMap();
					for (int j = 0; j < fmList.size(); j++) {
						FmApplyBean fmApplyBean = (FmApplyBean) fmList.get(j);
						int fid = fmApplyBean.getFid();
						Integer key = new Integer(fid);
						gameMap.put(key, initializeFmGame(fid,matchBean.getId()));
					}
				}
			} else {
				fmList = new ArrayList();
			}
			matchBean.setFmList(fmList);
			matchBean.setFmCount(fmList.size());
		}
	}

	// 初始化某一家族龙舟比赛信息
	public static BoatGameBean initializeFmGame(int fid, int mid){
		if(fid < 0)
			return null;
		BoatGameBean boatGameBean = new BoatGameBean();
		BoatBean boat = getFmBoatBean(fid);
		boatGameBean.setMid(mid);
		boatGameBean.setBoatType(boat.getBoatType());
		boatGameBean.setFid1(fid);
		boatGameBean.setType(1);
		boatGameBean.setShipId(boat.getId());
		boatGameBean.setBoat(boat);
		boatGameBean.setFmName(BoatAction.getFmByID(fid).getFm_name());
		List memberList = service.getApplyList("select * from fm_game_apply where state=2 and m_id=" + mid + " and fid=" + fid);
		for (int j = 0; j < memberList.size(); j++) {
			ApplyBean applyBean = (ApplyBean) memberList.get(j);
			if (OnlineUtil.isOnline(String.valueOf(applyBean.getUid()))) {
				// 发送给参加的用户,提示他们龙舟游戏开始了
				NoticeAction.sendNotice(applyBean.getUid(), "家族龙舟活动已经开始", "", NoticeBean.GENERAL_NOTICE, "", "/fm/game/fmgame.jsp");
			}
		}
		return boatGameBean;
	}
	
	// 获得某一家族龙舟
	public static BoatBean getFmBoatBean(int fid){
		FmBoatBean fmboat = service.getFmBoatBean("use_time>0 and is_use=0 and fid="+fid);
		BoatBean boat = null;
		if (fmboat != null && fmboat.getUseTime() > 0) {
			boat = service.getBoatBean(" id=" + fmboat.getBid());
			if(boat != null){
				boat.setUseTime(fmboat.getUseTime());
				boat.setFmBoatId(fmboat.getId());				
			} else {
				// 删除不存在龙舟记录
				service.upd("delete from fm_game_boat where id=" + fmboat.getBid());
				boat = service.getBoatBean(" id=1");// 默认龙舟
			}
		} else {
			boat = service.getBoatBean(" id=1");// 默认龙舟
		}
		return boat;
	}

	// 开始某场赛事
	public static void startMatch(int mid){
		MatchBean matchBean = (MatchBean) matchCache.get(new Integer(mid));
		if(matchBean != null && matchBean.getState2() == 0){
			initializeMatch(matchBean);
		}
	}
	
	// 游戏时间到
	public static void overMatch(int mid){
		// 获得当前正在比赛的龙舟游戏
		MatchBean matchBean = (MatchBean) matchCache.get(new Integer(mid));
		matchBean.getBoatTask().cancel();
		matchBean.getBoatTask2().cancel();
		if(accidentMap != null)
			accidentMap.clear();
		matchBean.setState(2);
		service.upd("update fm_game_match set state=2,end_time=now() where id=" + mid);
		HashMap gameMap = matchBean.getGameMap();
		List fmList = matchBean.getFmList();
		List list = new ArrayList();
		for (int i = 0; i < fmList.size(); i++) {
			FmApplyBean fmApplyBean = (FmApplyBean) fmList.get(i);
			Integer key = new Integer(fmApplyBean.getFid());
			BoatGameBean gb = (BoatGameBean) gameMap.get(key);
			// 把没完成赛事的家族取出来
			if(gb.getDistance() < gb.getAllDistance()){
				gb.setIsover(2);
				gb.setSpendTime(System.currentTimeMillis() - matchBean.getStarttime().getTime());
				// 龙舟失效
				int fmBoatId = gb.getBoat().getFmBoatId();
				int useTime = gb.getBoat().getUseTime();
				if(gb.getBoat().getId() != 1 && useTime > 0){
					if(useTime == 1)
						service.upd("update fm_game_fmboat set is_use = 1,use_time=use_time - 1 where id=" + fmBoatId);
					else
						service.upd("update fm_game_fmboat set use_time=use_time - 1 where id=" + fmBoatId);
				}
				list.add(gb);
			}
		}
		list = sortByDistance(list);
		for (int i = 0; i < list.size(); i++) {
			BoatGameBean gb = (BoatGameBean) list.get(i);
			matchBean.setRankNum(matchBean.getRankNum() + 1);
			gb.setRank(matchBean.getRankNum());
			gameMap.put(new Integer(gb.getFid1()), gb);
		}
		
		for (int i = 0; i < fmList.size(); i++) {
			FmApplyBean fmApplyBean = (FmApplyBean) fmList.get(i);
			int fid = fmApplyBean.getFid();
			Integer key = new Integer(fid);
			BoatGameBean gb = (BoatGameBean) gameMap.get(key);
			if(gb == null || gb.getRank() == 0){
				continue;
			}
			// 添加扣取的总报名费信息	
			if (gb.getTotalCoin() > 0) {
				addFundHistory(fid, "扣除龙舟报名费" + gb.getTotalCoin());
			}
			// 记录家族总经验
			if(gb.getDistance() > gb.getAllDistance() / 2){
				addFmGameInfo(fmApplyBean.getFid(), 1);
				gb.setGamePoint(1);
			}
			// 分贡献
			double rankContribution = Math.pow(gb.getRank(), 0.6);
			double matchContribution = fmList.size() / rankContribution * 10;// 公式:参赛队伍数量/(名次^0.6)*10
			long gameContribution = 300000000 / gb.getSpendTime(); //  公式:(3000/全程使用时间)*100=>3000/(SpendTime/1000)*100=>300000000/SpendTime
			int fmContribution = (int) (matchContribution + gameContribution);
			// 掌舵人的贡献度多得家族贡献度的5%
			Object[] seat = gb.getSeat();
			MemberBean memberBean = (MemberBean) seat[10];
			if (memberBean != null && memberBean.getUid() > 0) {
				int contribution = (int) (fmContribution * 0.05);
				memberBean.setContribution(contribution);
				fmContribution = fmContribution - contribution;
			}
			// 所有人平均分
			HashMap memberMap = gb.getMemberMap();
			if(memberMap.size() > 0){
				int contribution2 = fmContribution / memberMap.size();
				Set menberKey = memberMap.keySet();
				Iterator it = menberKey.iterator();
				while(it.hasNext()){
					MemberBean memberBean2 = (MemberBean) memberMap.get(it.next());
					if (memberBean2 != null && memberBean2.getUid() > 0) {
						if(memberBean2.getSeat() == 10){
							memberBean2.setContribution(memberBean2.getContribution() + contribution2);
						}else{
							memberBean2.setContribution(contribution2);
						}
						addFmUserGameInfo(memberBean2.getUid(),memberBean2.getFid(),memberBean2.getContribution(),"参加龙舟活动|"+memberBean2.getContribution());
						service.insertMember(memberBean2);
					}
				}
			}
			// 前三名 分钱
			if(gb.getRank() == 1){
				long prize = (long) (matchBean.getTotalCoin()*0.5);
				gb.setPrize(prize);
				updateFmFund(gb.getFid1(),prize);
				
				FamilyHomeBean familyHomeBean = getFmByID(gb.getFid1());// 更新家族基金缓存
				FamilyAction.service.insertFmFundDetail(gb.getFid1(), gb.getPrize(), FundDetail.GAME_TYPE,
						familyHomeBean.getMoney());
				
				addScore(fid, 1, 10);// 积分累加
				gb.setScore(10);
				service.addMovement(gb.getFid1(),"本轮家族龙舟","game/boat/rank.jsp?mid="+matchBean.getId());
			}else if(gb.getRank() == 2){
				long prize = (long) (matchBean.getTotalCoin()*0.3);
				gb.setPrize(prize);
				updateFmFund(gb.getFid1(),prize);
				
				FamilyHomeBean familyHomeBean = getFmByID(gb.getFid1());// 更新家族基金缓存
				FamilyAction.service.insertFmFundDetail(gb.getFid1(), gb.getPrize(), FundDetail.GAME_TYPE,
						familyHomeBean.getMoney());
				
				addScore(fid, 1, 7);// 积分累加
				gb.setScore(7);
			}else if(gb.getRank() == 3){
				long prize = (long) (matchBean.getTotalCoin()*0.15);
				gb.setPrize(prize);
				updateFmFund(gb.getFid1(),prize);
				
				FamilyHomeBean familyHomeBean = getFmByID(gb.getFid1());// 更新家族基金缓存
				FamilyAction.service.insertFmFundDetail(gb.getFid1(), gb.getPrize(), FundDetail.GAME_TYPE,
						familyHomeBean.getMoney());
				
				addScore(fid, 1, 5);// 积分累加
				gb.setScore(5);
			} else if(gb.getRank() == 4 || gb.getRank() == 5){
				addScore(fid, 1, 3);// 积分累加
				gb.setScore(3);
			} else if(gb.getRank() > 5) {
				if(gb.getDistance() >= gb.getAllDistance()){
					addScore(fid, 1, 2);// 积分累加
					gb.setScore(2);
				} else if (gb.getDistance() >= gb.getAllDistance() / 2) {
					addScore(fid, 1, 1);// 积分累加
					gb.setScore(1);
				}
			}
			service.insertFmGame(gb);
			// 成绩记录
			BoatRecordBean recordBean = service.getFmRecord("fm_id="+gb.getFid1());
			int complete = 0;
			if (gb.getDistance() < gb.getAllDistance()) {
				complete = 1;
			}
			if (recordBean == null) {
				service.insertFmRecord(gb,complete);
			} else {
				if (complete == 0) {
					if (recordBean.getComplete() == 1 || recordBean.getUseTime() > gb.getSpendTime()) {
						service.upd("update fm_game_boat_record set create_time=now(),complete=0,use_time="+gb.getSpendTime());
					}
				} else {
					if (recordBean.getComplete() == 1 && recordBean.getUseTime() > gb.getSpendTime()) {
						service.upd("update fm_game_boat_record set create_time=now(),use_time="+gb.getSpendTime());
					}
				}
			}
		}
	}

	public static void delFile (String name) {
		File file2 = new File(IMAGE_URL+name);
		if(file2 != null && !file2.isDirectory()){
			file2.delete();
		}
	}
	
	// 龙舟商店用来显示龙舟加速能力
	public static String speedStar(int num){
		if(num<=1)
			return "☆";
		StringBuilder ss = new StringBuilder();
		int num2 = num % 2;
		int num3 = num / 2;
		for(int i=0;i<num3;i++){
			ss.append("★");
		}
		if(num2>0)
			ss.append("☆");
		return ss.toString();
	}
	
	// 龙舟商店用来显示龙舟加速能力
	public static String maxSpeedStar(int num){
		if(num<=500)
			return "★★";
		StringBuilder ss = new StringBuilder();
		ss.append("★★");
		int num2 = num - 500;
		int num3 = num2 / 50;
		int num4 = num3 / 2;
		int num5 = num3 % 2;
		for(int i=0;i<num4;i++){
			ss.append("★");
		}
		if(num5>0)
			ss.append("☆");
		return ss.toString();
	}
	
}
