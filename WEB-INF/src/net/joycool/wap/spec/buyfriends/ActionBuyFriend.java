package net.joycool.wap.spec.buyfriends;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SecurityUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class ActionBuyFriend extends CustomAction {

	private static final float BUY_FACTOR = 0.05f;//每次朋友买卖的差价百分比
	//TODO 4 * 60 * 60 * 1000l => 2 * 60 * 1000l
	public static long PUNISH_TIME = 4 * 60 * 60 * 1000l;
	//TODO 23 * 60 * 60 * 1000l => 5 * 60 * 1000l
	public static long FROZEN_TIME = 23 * 60 * 60 * 1000l;
	static UserServiceImpl userService = new UserServiceImpl();
	
	public static String getUserLink(int uid, String nickName) {
		StringBuilder sb = new StringBuilder("<a href=\"");
		sb.append("/homeland/home.jsp?uid=" + uid);
		sb.append("\">");
		sb.append(StringUtil.toWml(nickName));
		sb.append("</a>");
		return sb.toString();
	}
	
	public ActionBuyFriend() {
		super();
	}

	public ActionBuyFriend(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public ActionBuyFriend(HttpServletRequest request) {
		super(request);
	}

	
	public boolean buyFriend(){
		ServiceSlave serviceSlave = ServiceSlave.getInstance();
		
		int uid = this.getParameterInt("uid");
		int type = this.getParameterInt("type");
		String alias = this.request.getParameter("alias");
		
		if(alias == null || alias.length() == 0) {
			alias = "奴隶";
		}
		
		if(userService.getUserFriend(getLoginUser().getId(), uid) == null 
			|| userService.getUserFriend(uid, getLoginUser().getId()) == null) {
			request.setAttribute("msg", "不是双向好友，不能购买");
			return false;
		}
		
		
		BeanMaster master = serviceMaster.getMasterByUid(uid);
		BeanMaster me = serviceMaster.getMasterByUid(this.getLoginUser().getId());
		if(me.getMoney() >= master.getPrice()) {
			if(SecurityUtil.isAdmin(uid)) {
				request.setAttribute("msg", "管理员不能被购买");
				return false;
			}
			int priceOffSet = (int)(master.getPrice() * BUY_FACTOR);
			//处理购买奴隶 start
			BeanSlave slave = serviceSlave.getSlaveBySlaveUid(uid);
			
			int oldMasterUid = 0;
			
			if(slave != null) {
				
//				if(slave.getRank() == 0) {
//					request.setAttribute("msg", "临时奴隶不能被购买");
//					return false;
//				}
				
				if(me.getUid() == slave.getMasterUid()) {
					request.setAttribute("msg", "已经是你的奴隶了");
					return false;
				}
				
					if(serviceSlave.isPunish(uid)) {
						request.setAttribute("msg", "该奴隶正在受惩罚中，不能购买");
						return false;
					} else {
						serviceSlave.deletePunish(uid);
					}
				
				
				serviceSlave.deleteSlave(slave.getMasterUid(), slave.getSlaveUid());
				//买卖朋友赚的差价
				serviceMaster.increaseMoneyByUid(slave.getMasterUid(), master.getPrice(), true);	
				
				oldMasterUid = slave.getMasterUid();
				
			} else {
				
				if(master.getRansomTime().getTime() > System.currentTimeMillis()) {
					request.setAttribute("msg", "我有钱赎身，你买不了我，哈哈");					
					return false;
				}
			}
			slave = new BeanSlave();
			slave.setMasterUid(this.getLoginUser().getId());
			slave.setMasterNickName(this.getLoginUser().getNickName());
			slave.setSlaveType(type);
			slave.setSlaveAlias(alias);
			slave.setOldMasterUid(oldMasterUid);
			slave.setSlaveUid(master.getUid());
			slave.setSlaveNickName(master.getNickName());
			slave.setBuyPrice(master.getPrice());
			slave.setRank(1);
			serviceSlave.addSlave(slave);
			//处理购买奴隶 end
			
			//动态
			ActionTrend.addGameTrend(getLoginUser().getId(), BeanTrend.TYPE_BUY_FRIEND, "%1花"+master.getPrice()+"元买了%2,起个外号叫"+StringUtil.toWml(alias), 
					getLoginUser().getNickName(),master.getUid(), master.getNickName());
			
			//购买奴隶金钱减少，奴隶数加1
			if(serviceMaster.decreaseMoneyByUid(this.getLoginUser().getId(), master.getPrice(), true)){				
				//身价涨5%
				serviceMaster.increasePriceByUid(uid, priceOffSet);
			}
			
			
			NoticeAction.sendNotice(uid, getLoginUser().getNickNameWml() + "购买你为奴隶", NoticeBean.GENERAL_NOTICE, "/beacon/bFri/myInfo.jsp");
			
			request.setAttribute("msg", "购买成功");
		} else {
			request.setAttribute("msg", "你的金钱不够，赶快邀请朋友获得更多金钱");
			return false;
		}
		return true;
	}
	
	//安抚奴隶
	public void appease(){
		int uid = this.getParameterInt("uid");
		int type = this.getParameterInt("type");
		
		int loginUid = getLoginUser().getId();
		
		ServiceSlave serviceSlave = ServiceSlave.getInstance();
		
		BeanSlave slave = serviceSlave.getSlaveBySlaveUid(uid);
		
		if(slave == null) {
			request.setAttribute("msg", "安抚失败,你的奴隶已经赎身");
			return;
		} else if(slave.getMasterUid() != loginUid) {
			request.setAttribute("msg", "安抚失败,你的奴隶已经被买走");
			return;
		}
		
		//判断是否安抚过该奴隶
		if(serviceSlave.isAppease(getLoginUser().getId(), uid)) {
			request.setAttribute("msg", "你对他再好，一天也只能安抚一回哦！你可不能把他惯坏了");
			return;
		} else {
			
			if(slave.getFrozenTime() > System.currentTimeMillis()) {
				request.setAttribute("msg", "你对他再好，一天也只能安抚一回哦！你可不能把他惯坏了");
				return;
			}
			
			BeanFlag flagBean = new BeanFlag();
			flagBean.masterUid = getLoginUser().getId();
			flagBean.slaveUid = uid;
			
			//flagBean.endTime = System.currentTimeMillis() + DateUtil.MS_IN_DAY;
			
			serviceSlave.addPunish(flagBean);
			
			
		}
		
		UserBean userBean = UserInfoUtil.getUser(uid);
		//加入安抚操作的动态
		String content = getAppease(type, this.getLoginUser(), userBean);
		ActionTrend.addGameTrend(getLoginUser().getId(), BeanTrend.TYPE_BUY_FRIEND,
				content, getLoginUser().getNickName(),
				userBean.getId(), userBean.getNickName());
		/*
		ActionTrend.addTrend(getLoginUser().getId(), BeanTrend.TYPE_BUY_FRIEND,
				content, getLoginUser().getNickName(),
				userBean.getId(), userBean.getNickName());
		*/
		
		//NoticeAction.sendNotice(uid, getLoginUser().getNickNameWml() + "在朋友买卖中安抚你", NoticeBean.GENERAL_NOTICE, "/beacon/bFri/myInfo.jsp");
		request.setAttribute("msg", content.replace("%1", "我").replace("%2", userBean.getNickNameWml()));
	}
	
	//安抚的操作
	private String getAppease(int type, UserBean loginUser, UserBean user) {
		StringBuilder sb = new StringBuilder();
		switch(type) {
		case 1:
			sb.append("%1为%2上个烟，还点了火，%2很满意");
			break;
		case 2:
			sb.append("%1把%2的内衣内裤都洗得干干净净");
			break;
		case 3:
			sb.append("%1轻轻的抚摸%2的头，%2倒在%1怀里");
			break;
		case 4:
			serviceMaster.decreaseMoneyByUid(this.getLoginUser().getId(), 50, false);
			sb.append("%2开心的接过%1给的零用钱，并给%1鞠了一躬");
			break;
		case 5:
			sb.append("%1认真的为%2穿上袜子，%2开心的对%1点点头");
			break;
		case 6:
			sb.append("%1专注的听%2阐述心声，拉近了彼此的距离");
			break;
		case 10:
			sb.append("%1");
			sb.append(request.getParameter("a"));
			sb.append("%2");
			sb.append(request.getParameter("c"));
			break;
		}
		return sb.toString();
	}
	
	//惩罚的操作
	public void punish(){
		int uid = this.getParameterInt("uid");
		int type = this.getParameterInt("type");
		
		int loginUid = getLoginUser().getId();
		
		ServiceSlave serviceSlave = ServiceSlave.getInstance();
		
		BeanSlave slave = serviceSlave.getSlaveBySlaveUid(uid);
		
		if(slave == null) {
			request.setAttribute("msg", "惩罚失败,你的奴隶已经赎身");
			return;
		} else if(slave.getMasterUid() != loginUid) {
			request.setAttribute("msg", "惩罚失败,你的奴隶已经被买走");
			return;
		}
		
		if(slave.getFrozenTime() > System.currentTimeMillis()) {
			request.setAttribute("msg", "今天你已经折磨过，奴隶也是人啊，明天再来吧");
			return;
		}
		
		if(!serviceSlave.isAppease(loginUid, uid)) {
			serviceSlave.deleteSlave(this.getLoginUser().getId(), uid);
			serviceMaster.increaseMoneyByUid(this.getLoginUser().getId(), 0, true);
			request.setAttribute("msg", "你的奴隶忍受不了你对他惨无人道的折磨，他趁你不注意的时候，偷偷地逃跑了！");
			return;
		}
		
		
		BeanFlag flagBean = serviceSlave.getBeanFlag(uid);
		//检测是否惩罚过，一天只能惩罚一次
		flagBean.punish = true;
		flagBean.punishType = type;
		flagBean.masterUid = loginUid;
		flagBean.slaveUid = uid;
		flagBean.endTime = System.currentTimeMillis() + PUNISH_TIME;//DateUtil.MS_IN_DAY;
		
		
		if(type == 8) {
			//身价大跌30%
			serviceMaster.decreasePricePercentByUid(uid, 0.3f);
			flagBean.money = 0;
		} else {
			//惩罚奴隶随机获得20~160的金钱
			flagBean.money = (int)(160 * Math.random());
			if(flagBean.money < 20) {
				flagBean.money = 20 + flagBean.money;
			}
			//flagBean.endTime = System.currentTimeMillis() + 10000;//DateUtil.MS_IN_DAY;
		}
		
		serviceSlave.updatePunish(flagBean);
		slave.setFrozenTime(System.currentTimeMillis() + FROZEN_TIME);
		serviceSlave.updateSlaveFrozenTime(slave.getSlaveUid(), slave.getFrozenTime());
		UserBean userBean = UserInfoUtil.getUser(uid);
		
		String content = getPunish(type, this.getLoginUser(), userBean);
		
		ActionTrend.addGameTrend(getLoginUser().getId(), BeanTrend.TYPE_BUY_FRIEND,
				content, getLoginUser().getNickName(),
				userBean.getId(), userBean.getNickName());
		
		/*
		ActionTrend.addTrend(getLoginUser().getId(), BeanTrend.TYPE_BUY_FRIEND,
				content, getLoginUser().getNickName(),
				userBean.getId(), userBean.getNickName());
		*/		
		
		NoticeAction.sendNotice(uid, getLoginUser().getNickNameWml() + "惩罚了你", NoticeBean.GENERAL_NOTICE, "/beacon/bFri/myInfo.jsp");
		
		request.setAttribute("msg", content.replace("%1", "我").replace("%2", userBean.getNickNameWml()));
		request.setAttribute("money", new Integer(flagBean.money));
	}

	
	private String getPunish(int type, UserBean loginUser, UserBean user){
		StringBuilder sb = new StringBuilder();
		switch(type) {
		case 1:
			sb.append("%1让%2跪在街上要饭来挣零用钱花");
			break;
		case 2:
			sb.append("%1把%2租给包工头卖苦力");
			break;
		case 3:
			sb.append("%1让%2清理卫生死角");
			sb.append(user.getGenderText());
			break;
		case 4:
			sb.append("%1让%2去煤窑挖煤，并没收全部收入");
			break;
		case 5:
			sb.append("%2让%1很生气，罚%2一天不许吃饭");
			sb.append("");
			break;
		case 6:
			sb.append("%1让%2去清理鸟巢体育场，累吐了血");
			break;
		case 7:
			sb.append("%1让%2给老百姓挨家挨户的送粮食");
			break;
		case 8:
			sb.append("%1把%2许配给了老黑奴，使%2的身价大跌30%");
			break;
		case 10:
			sb.append("%1");
			sb.append(request.getParameter("a"));
			sb.append("%2");
			sb.append(request.getParameter("c"));
			break;
		}
		return sb.toString();
	}
	
	static ServiceMaster serviceMaster = ServiceMaster.getInstance();
	static ServiceSlave serviceSlave = ServiceSlave.getInstance();

	public static void register(UserBean loginUser, UserBean sendUser) {
//		BeanMaster master = new BeanMaster();
//		master.setUid(loginUser.getId());
//		master.setSlaveCount(0);
//		master.setPrice(600);
//		master.setMoney(1000);
//		master.setNickName(loginUser.getNickName());
//		
//		serviceMaster.getMasterByUid(sendUser.getId());//如果sendUid不是奴隶主则会初始化该奴隶主
//		serviceMaster.addMaster(master);
//		
//		BeanSlave slave = new BeanSlave();
//		slave.setMasterUid(sendUser.getId());
//		slave.setMasterNickName(sendUser.getNickName());
//		slave.setSlaveType(1);
//		slave.setSlaveAlias("奴隶");
//		slave.setOldMasterUid(0);
//		slave.setSlaveUid(loginUser.getId());
//		slave.setSlaveNickName(loginUser.getNickName());
//		slave.setBuyPrice(0);
//		slave.setRank(0);
//		
//		serviceSlave.addSlave(slave);
		
		//加入成功邀请好友动态				
//		ActionTrend.addTrend(sendUser.getId(), BeanTrend.TYPE_BUY_FRIEND, "%1 成功邀请 %2 成为自己的奴隶，并获得300元", 
//				sendUser.getNickName(),loginUser.getId(), loginUser.getNickName());
		//金钱加500，并且奴隶数加1
		serviceMaster.decreaseMoneyByUid(sendUser.getId(), -300, true);


	}
	
	public void salary() {
		BeanMaster master = serviceMaster.getMasterByUid(this.getLoginUser().getId());
		if(master.getMoney() >= 1000) {
			tip("tip", "补助已到期.");
			return;
		}
		long now = System.currentTimeMillis();
		if(master.isSalaryTime(now)) {
			int salary = RandomUtil.nextInt(60) + 20;
			serviceMaster.increaseMoneyByUid(master.getUid(), salary, false);
			master.setSalaryTime(now);
			SqlUtil.executeUpdate("UPDATE master SET salary_time=now() where uid=" + master.getUid(), 5);
			tip("tip", "领取了补助" + salary + "元.");
		} else {
			tip("tip", "今天的补助已领取过了.");
		}
	}
}
