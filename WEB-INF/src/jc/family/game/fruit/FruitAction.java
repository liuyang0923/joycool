package jc.family.game.fruit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.RandomUtil;
import jc.family.game.GameAction;
import jc.family.game.vs.VsAction;

/**
 * 
 * 这个是游戏主要的action啦
 * 
 * @author guigui
 * 
 */
public class FruitAction extends VsAction {

	public FruitAction() {

	}

	public FruitAction(HttpServletRequest request) {
		super(request);
	}

	public FruitAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public int getGameType() {
		return FruitGameBean.GType;
	}

	/**
	 * 得到果园分布数组
	 * 
	 * @return
	 */
	public OrchardBean[][] getOrchards() {

		FruitGameBean bean = (FruitGameBean) getVsGame();
		OrchardBean[][] show = bean.getOrchardBean();
		return show;
	}

	public OrchardBean getOneOrchards() {// 根据页面上传来的两个参数，得到一个果园

		int x = getParameterInt("x");
		int y = getParameterInt("y");
		OrchardBean bean = null;

		OrchardBean[][] os = getOrchards();
		try {
			bean = os[x][y];
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bean;
	}

	/**
	 * 初始化一个野生果园
	 * 
	 * @param type
	 * @param x
	 * @param y
	 * @return
	 */
	public static OrchardBean makeWildOrchard(int type, int x, int y) {

		OrchardBean bean = new OrchardBean();

		if (type == 1) {// 水果初始20~40 阳光采集率10~35
			int fruitCount = RandomUtil.nextInt(21) + 20;
			int sunCount = RandomUtil.nextInt(26) + 10;

			bean.setFruitCount(fruitCount);
			bean.setSunCaptureRate(sunCount);

		} else if (type == 2) {// 水果初始50~120 阳光采集率30~70
			int fruitCount = RandomUtil.nextInt(71) + 50;
			int sunCount = RandomUtil.nextInt(41) + 30;

			bean.setFruitCount(fruitCount);
			bean.setSunCaptureRate(sunCount);
		} else if (type == 3) {// 水果初始600 阳光采集率120
			bean.setFruitCount(360);
			bean.setSunCaptureRate(120);
		}
		bean.setSun0(0);
		bean.setX_Point(x);
		bean.setY_Point(y);
		bean.setOrchardName();

		bean.setSide(3);
		bean.setSunGlassGrade(0);
		return bean;
	}

	/**
	 * 初始化一个家族果园
	 * 
	 * @param x
	 * @param y
	 * @param sun
	 * @param owerID
	 * @return
	 */
	public static OrchardBean makeFamilyOrchard(int x, int y, int sun, int side) {// 构建一个家族果园ABHI列
		OrchardBean bean = new OrchardBean();
		bean.setSun0(0);
		bean.setFruitCount(100);
		bean.setX_Point(x);
		bean.setY_Point(y);
		bean.setOrchardName();
		bean.setSide(side);
		bean.setSunCaptureRate(sun);
		return bean;
	}

	/**
	 * 变更水果科技和升级水果科技
	 * 
	 * @param type
	 *            :1扔水果比例2尖刺果皮3加厚果皮4喷气水果5果影分身
	 * @return
	 */
	public int updateFamilyFruitDetails(int type) {
		// 用这个方法时要判断用户是不是为null
		FruitUserBean user = (FruitUserBean) getVsUser();
		FruitFamilyBean bean = getFruitFamilyBean(user.getSide());
		FruitGameBean vsGame = (FruitGameBean) getVsGame();

		OrchardBean orchards = getOneOrchards();
		
		int grade = 0;
		int minSunCount = 0;
		
		synchronized (vsGame){
			
			int sunCount = orchards.getSunCount(System.currentTimeMillis());
	
			switch(type) {
			case 1: {// 更改水果比例
	
				int percent = getParameterInt("cp");
				if (percent > 10 || percent % 2 != 0) {
					return 0;
				}
				bean.setFruitThrowProportion(percent);
			} break;
			case 2: {// 升级尖刺果皮
	
				grade = bean.getFruitATKGrade();
				if (grade > 9) {// 已经最高级别了
					return 0;
				}
				minSunCount = 50 + grade * 25;
				if (sunCount < minSunCount) {
					return 1;// 阳光不够
				}
				bean.setFruitATKGrade(bean.getFruitATKGrade() + 1);// 提高等级
				
				orchards.addSun0(-minSunCount);// 增加用掉的阳光
				
				user.setUpdTeckCount(user.getUpdTeckCount()+1);// 增加用户升级科技成功次数
				user.setOperateCount(user.getOperateCount() + 1);// 增加缓存中用户的操作次数
			} break;
			case 3: {// 升级加厚果皮
	
				grade = bean.getFruitHP();
				if (grade > 9) {// 已经最高级别了
					return 0;
				}
				minSunCount = 50 + grade * 25;
				if (sunCount < minSunCount) {
					return 1;// 阳光不够
				}
				bean.setFruitHP(grade + 1);
	
				user.setUpdTeckCount(user.getUpdTeckCount()+1);// 增加用户升级科技成功次数
				user.setOperateCount(user.getOperateCount() + 1);// 增加缓存中用户的操作次数
	
				orchards.addSun0(-minSunCount);// 增加用掉的阳光
			} break;
			case 4: {// 升级喷气水果
	
				grade = bean.getFruitSpeed();
				if (grade > 3) {// 已经最高级别了
					return 0;
				}
				minSunCount = 100 + grade * 100;
				if (sunCount < minSunCount) {
					return 1;// 阳光不够
				}
				bean.setFruitSpeed(grade + 1);
	
				user.setUpdTeckCount(user.getUpdTeckCount()+1);// 增加用户升级科技成功次数
				user.setOperateCount(user.getOperateCount() + 1);// 增加缓存中用户的操作次数
	
				orchards.addSun0(-minSunCount);// 增加用掉的阳光
				
			} break;
			case 5: {// 升级果影分身
	
				grade = bean.getFruitYield();
				if (grade > 9) {
					return 0;
				}
				minSunCount = 50 + grade * 50;
				if (sunCount < minSunCount) {
					return 1;// 阳光不够
				}
				bean.setFruitYield(grade + 1);
				user.setUpdTeckCount(user.getUpdTeckCount()+1);// 增加用户升级科技成功次数
				user.setOperateCount(user.getOperateCount() + 1);// 增加缓存中用户的操作次数
	
				orchards.addSun0(-minSunCount);// 增加用掉的阳光
				
			} break;
			case 6: {
	
				grade= orchards.getSunGlassGrade();
				minSunCount = 25 + grade * 25;
				if (grade > 9) {
					return 0; // 已经到最高级别
				}
				if (sunCount < minSunCount) {
					return 1;// 阳光不够
				}
				// 升级果园的总阳光和采集率
				orchards.setSunGlassGrade(grade + 1);
	
				orchards.addSun0(-minSunCount);// 增加用掉的阳光
	
				orchards.updateSun();// 升级成功，修改初始阳光和时间
				orchards.setSunCaptureRate(orchards.getSunCaptureRate() + FruitGameBean.sunglassIncrease);
				// 升级家族的采集率
				bean.setSunTotalCaptureRate(bean.getSunTotalCaptureRate() + FruitGameBean.sunglassIncrease);
				user.setUpdTeckCount(user.getUpdTeckCount()+1);// 增加用户升级科技成功次数
				user.setOperateCount(user.getOperateCount() + 1);// 增加缓存中用户的操作次数
			} break;
			}
			
			if(type>1){
				vsGame.getLogList().addFirst(vsGame.getTrendsStr(4,orchards,null,0,0,vsGame.getFmNameBySide(orchards.getSide()),type-1,grade+1));//增加游戏动态
			}
		}

		return 2;
	}
	

	/**
	 * 增加战斗动态。<br/>
	 * 每个动态分自动分配一个id，整个动态放到linkedHashList中
	 * 
	 * @param bean
	 */
	public void addFruitHitsMode(FruitTask task) {// 增加战斗动态
		FruitGameBean game = (FruitGameBean) getVsGame();
		game.getFruitHitsMode().put(new Integer(task.getTaskID()), task);
		return;
	}

	/**
	 * 
	 * @return
	 */
	public List getFruitHitsModeList(int countPrePage, String url) {// 战斗动态分页列表
		FruitGameBean bean = (FruitGameBean) getVsGame();
		return getSubList(new ArrayList(bean.getFruitHitsMode().keySet()), countPrePage, "fruitHitsModeList", url,false);
	}

	/**
	 * 果园列表（自己家族果园，敌方家族果园，野生果园），按A~Z排列，和1~9排列
	 * @param owerID
	 * @return
	 */
	public List getOrchardLists(int side) {

		FruitGameBean bean = (FruitGameBean) getVsGame();
		OrchardBean orchards[][] = bean.getOrchardBean();

		List orchardList = new ArrayList();

		for (int i = 0; i < orchards[0].length; i++) {
			for (int j = 0; j < orchards.length; j++) {
				OrchardBean o = orchards[j][i];
				if (o != null && o.getSide() == side) {
					orchardList.add(o);
				}
			}
		}
		return orchardList;
	}

	public static int fruitPercent[] = { 2, 4, 6, 8, 10 };
	/**
	 * 生产水果
	 */
	public void productFruits() {// 生产水果，兑换比例是1:1

		int index = getParameterInt("fp");// 页面传过来的值
		if (index > 5 || index == 0) {
			return;
		}
		
		FruitGameBean vsGame = (FruitGameBean) getVsGame();
		OrchardBean orchards = getOneOrchards();
		if (orchards == null) {
			return;
		}
		FruitUserBean user = (FruitUserBean) getVsUser();
		FruitFamilyBean ffb = getFruitFamilyBean(user.getSide());
		synchronized (vsGame){
			
			if(orchards.getSide() != user.getSide())
				return;
			
			int sunTotal = orchards.getSunCount(System.currentTimeMillis());
	
			int cost = sunTotal * fruitPercent[index - 1] / 10;
			int fruit = (int)(sunTotal *(1+0.05*ffb.getFruitYield()))* fruitPercent[index - 1] / 10;
	
			// 增加该家族的总水果量
			ffb.setFruitTotalcount(ffb.getFruitTotalcount() + fruit);
	
			// 增加该果园的水果量
			orchards.addFruit(fruit);
			// 增加缓存中用户的操作次数
			user.setOperateCount(user.getOperateCount() + 1);
			// 修改果园起始阳光值
			orchards.addSun0(-cost);
		}
	}

	/**
	 * 进攻
	 */
	public String getAttack() {// 创建计时器 进攻，用于扔的水果到达目的地，开打

		// 下面的这些数字要从页面取到
		int x1 = getParameterInt("x1"), y1 = getParameterInt("y1"), x2 = getParameterInt("x2"), y2 = getParameterInt("y2");
		int fruitCount = 0;

		String remand = "";// 扔出水过之后的提示语
		if (x1 < 0 || x1 > 11 || x2 < 0 || x2 > 11 || y1 < 0 || y1 > 8 || y2 < 0 || y2 > 8) {// 检测输入的数字是否正确
			remand = "果园不存在 !";
			return remand;
		}

		FruitGameBean bean = (FruitGameBean) getVsGame();
		FruitUserBean user = (FruitUserBean) getVsUser();
		if (user == null) {
			remand = "你没有这个权限 !";
			return remand;
		}

		OrchardBean o1 = bean.getOrchardBean()[x1][y1];
		OrchardBean o2 = bean.getOrchardBean()[x2][y2];
		int userSide = user.getSide();

		if (o1 == null || o2 == null) {
			remand = "果园不存在 !";
			return remand;
		}
		FruitFamilyBean bean1 = getFruitFamilyBean(user.getSide());
		synchronized (bean){
			
			if (o1.getSide() != userSide) {// 不是自己家族的果园
				remand = "不是自己的果园不能扔水果!";
				return remand;
			}
	
			fruitCount = bean1.getFruitThrowProportion() * 10 * o1.getFruitCount() / 100;// 要扔的水果数目
			if(bean1.getFruitThrowProportion() * 10 * o1.getFruitCount()%100!=0){
				fruitCount+=1;
			}
			if (fruitCount == 0) {
				remand = "当前果园没有水果!";
				return remand;
			}

			o1.setFruitCount(o1.getFruitCount() - fruitCount);// 修改果园的水果数
			
	// bean1.setFruitTotalcount(bean1.getFruitTotalcount()-fruitCount);//// 修改家族总的水果数
	
			// 计算开始攻击时间
			int x = Math.abs(x2 - x1);
			int y = Math.abs(y2 - y1);
	
			int distence = x * x + y * y;
			double n = Math.sqrt(distence);
			distence = (int) Math.ceil(n);
			int time = distence * (8 - bean1.getFruitSpeed());
			
//			bean.updateUserOprate(2,fruitCount,user.getUserId());// 修改用户操作信息
			user.setThrowFruitCount(user.getThrowFruitCount()+fruitCount);
			user.setOperateCount(user.getOperateCount() + 1);// 增加缓存中用户的操作次数
	
			// 新建定时器
			FruitTask task = new FruitTask(o1, o2 ,bean, fruitCount,time,user.getUserId());// 新建任务
			long arriveTime = ((task.getStartTime() + task.getArrivalTime() * 1000) - System.currentTimeMillis()) / 1000;
			task.setTrends(bean.getTrendsStr(5,o1,o2,fruitCount,0,"",0,0));// 增加战况
			GameAction.fmTimer.schedule(task, time * 1000);// 启动计时器
			
			addFruitHitsMode(task);// 增加游戏动态
	
			remand = "您已扔出" + fruitCount + "个水果，预计" + time + "秒后到达目标" + o2.getOrchardName();// 用于页面显示
			bean.getLogList().addFirst(bean.getTrendsStr(1,o1,o2,fruitCount,arriveTime,"",0,0));// 增加水果动态
			
		}
		return remand;

	}

	/**
	 * 根据side得到家族A(挑战者)或者家族B（应战者）的游戏信息
	 * 
	 * @param id
	 * @return
	 */
	public FruitFamilyBean getFruitFamilyBean(int side) {
		FruitGameBean bean = (FruitGameBean) getVsGame();
		if (side == 0) {
			return bean.getFamilyA();
		} else if (side == 1) {
			return bean.getFamilyB();
		}
		return null;
	}
	
	/**
	 * 取出用户的操作次数的列表
	 * 
	 * @return
	 */
	public List getUserOprateCountList() {
		FruitGameBean bean = (FruitGameBean) getVsGame();
		HashMap map = bean.getUserMap();
		List list = new ArrayList(map.values());
		return getSubList(list, 10, "oprateCountList", "", false);
	}

	/**
	 * 工具类，用于分页
	 * 
	 * @param list
	 * @param pageNum
	 * @param strName
	 * @param url
	 * @param para
	 * @return
	 */
	public List getSubList(List list, int pageNum, String strName, String url, boolean para) {
		int c = list.size();
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, pageNum, "p");
		int start = paging.getStartIndex(), end = paging.getStartIndex() + paging.getCountPerPage();
		if (list.size() < pageNum) {
			list = list.subList(paging.getStartIndex(), list.size());
		} else if (list.size() % pageNum < pageNum) {
			if (start == (list.size() / pageNum) * pageNum) {
				end = list.size();
			}
			list = list.subList(start, end);
		}
		String s = paging.shuzifenye(url, para, "|", response);
		setAttribute(strName, s);
		return list;
	}

	/**
	 * 对一个LinkedList分页
	 * 
	 * @param list
	 *            要分的linkedList
	 * @param pageNum
	 *            当前页码
	 * @param countPrePage
	 *            每页要显示的数量
	 * @param strName
	 * @param url
	 * @param para
	 * @return
	 */
	public List getSubList2(LinkedList list, int countPrePage, String strName, String url, boolean para) {
		int tmp = 1;
		List list2 = new ArrayList();
		if (list == null || list.size() == 0)
			return list2;
		if (strName == null || url == null)
			return list2;
		if (countPrePage > list.size())
			return (List) list; // 我不确定这里用不用强制转换成List
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, list.size(), countPrePage,
				"p");
		ListIterator li = list.listIterator(paging.getCurrentPageIndex() * countPrePage);
		while (li.hasNext()) {
			list2.add(li.next());
			tmp++;
			if (tmp > countPrePage)
				break;
		}
		String s = paging.shuzifenye(url, para, "|", response);
		setAttribute(strName, s);
		return list2;
	}
	
	public static char[] symbols = {'■', '▲', '■', '△'};
	public static char getSymbol(int side) {
		return symbols[side];
	}
}
