package net.joycool.wap.action.floor;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.FloorBean;
import net.joycool.wap.bean.MessageBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.FloorServiceImpl;
import net.joycool.wap.service.impl.MessageServiceImpl;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/** 
 * @author guip
 * @explain：
 * @datetime:2007-8-27 16:40:36
 */
public class FloorAction extends CustomAction{
	UserBean loginUser;
//	 消息系统
	static INoticeService noticeService = ServiceFactory.createNoticeService();
	Vector Toplist = null;
	static SimpleGameLog log = new SimpleGameLog();
//	同步锁
	static byte[] lock = new byte[0];
	public FloorAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
		
	}
	FloorServiceImpl floorImpl = new FloorServiceImpl();
	
	public static String getLogString() {
		return log.getLogString(5);
	}
	public void addLog(String content) {
		log.add(content);
	}
	public void index(HttpServletRequest request)
	{
		String result = null;
		String tip = null;
		
		//得到踩楼游戏mark=0为当前游戏
		FloorBean bean = floorImpl.getFloorProduct("mark=0");
		if(bean==null)
		{
			doTip(null, "对不起，暂时没有游戏");
			return;
		}
		request.setAttribute("FloorBean", bean);
		Toplist =new Vector();
		String condition = "1=1 order by count DESC limit 3";
		//得到排行榜前三名
		Toplist=floorImpl.getFloorTopList(condition);
     	request.setAttribute("Toplist", Toplist);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("Check", "ture");
		//清除缓存
		OsCacheUtil.flushGroup(OsCacheUtil.FLOORTOP_CACHE_GROUP,
		"SELECT * FROM floortop WHERE 1=1 order by count DESC limit 3");
	}
	public void registerFloor(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 接收页面所传参数
		int userId = loginUser.getId();
		int prize =StringUtil.toInt(request.getParameter("prize"));
		int number = StringUtil.toInt(request.getParameter("number"));
		int floor = StringUtil.toInt(request.getParameter("floor"));
		String content = getParameterNoEnter("content");
		if (content == null || content.equals("")) {
			doTip(null, "对不起，您的填写有误，填写时仔细察看每项要求");
			return;
		} else if (prize < 100000 || prize > 1000000000) {
			doTip(null, "对不起，您的填写有误，填写时仔细察看每项要求");
			return;
		} else if (number < 0 || number > 20) {
			doTip(null, "对不起，您的填写有误，填写时仔细察看每项要求");
			return;
		}else if (floor < 0 || floor > 99) {
			doTip(null, "对不起，您的填写有误，填写时仔细察看每项要求");
			return;
		}
		request.setAttribute("content",content);
		request.setAttribute("floor",String.valueOf(floor));
		request.setAttribute("userId",String.valueOf(userId));
		request.setAttribute("prize",String.valueOf(prize));
		request.setAttribute("number",String.valueOf(number));
		result = "success";
		request.setAttribute("result", result);
	}
	public void sureFloor(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int userId = loginUser.getId();
		
		String content = getParameterNoEnter("content");
		int prize = StringUtil.toInt(request.getParameter("prize"));
		int number =StringUtil.toInt(request.getParameter("number"));
		int floor =StringUtil.toInt(request.getParameter("floor"));
		long num = (long)prize*number;
		StoreBean store = (StoreBean)BankCacheUtil.getBankStoreCache(userId);
		long money = store.getMoney();
		if(num>money||num<=100000||floor<0||num>1000000000||floor>99)
		{
			doTip(null, "对不起，您的乐币不够，请仔细计算后再创建游戏");
			return;
		}else{
		FloorBean floorBean = new FloorBean();
		floorBean.setContent(content);
		floorBean.setUserId(userId);
		floorBean.setFloor(floor);
		floorBean.setPrize(prize);
		floorBean.setNumber(number);
		//创建一个新游戏
		floorImpl.addFloorProduct(floorBean);
		//用从户银行扣钱
		BankCacheUtil.updateBankStoreCacheById(-num,userId,0,Constants.BANK_FLOOR_TYPE);
		result = "success";
		doTip("success", "游戏创建成功，招呼大家来玩吧!大家会感谢你的!");
		request.setAttribute("result", result);
		}
	}
	public void treadFloor(HttpServletRequest request) {
		//防刷新
		if (session.getAttribute("Check") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("Check");
		String result = null;
		String tip = null;
		
		synchronized (lock) {
			int id = StringUtil.toInt(request.getParameter("id"));	
			FloorBean floorBean = (FloorBean)floorImpl.getFloorProduct("id="+id);
			int userId = StringUtil.toInt(request.getParameter("userId"));	
			int count = floorBean.getCount();
			int number = floorBean.getNumber();
			int floor = floorBean.getFloor();
			long prize = floorBean.getPrize();
			long num = prize*number;
			int nowPrize= floorBean.getNowPrize();
			String outLog =null;
			//StoreBean store = (StoreBean)BankCacheUtil.getBankStoreCache(userId);
			//int a= count%10;
			//踩中楼数或尾数是指定数的
			if(count==floor || count%10==floor || count%100==floor)
			{
				BankCacheUtil.updateBankStoreCacheById(prize,userId,0,Constants.BANK_FLOOR_TYPE);
				//中奖数加一
				floorImpl.updateFloorProduct("nowPrize=nowPrize+1","id="+id);
				//往排行榜写数据
				floorImpl.updateFloorTopCacheById(id,userId);
				//楼层加一
				floorImpl.updateFloorProduct("count=count+1","id="+id);
                //清理缓存
				/*OsCacheUtil.flushGroup(OsCacheUtil.FLOOR_CACHE_GROUP,
						"SELECT * FROM tread_floor WHERE mark=0");
				OsCacheUtil.flushGroup(OsCacheUtil.FLOOR_CACHE_GROUP,
				"SELECT * FROM tread_floor WHERE id="+id);*/
				
				//实况输出
				UserBean user = (UserBean)UserInfoUtil.getUser(loginUser.getId());
				String name = user.getNickName();
				outLog = "中奖"+":"+ StringUtil.toWml(name);
				addLog(outLog);
                //内存里面的count值至加一
				floorBean.setCount(count+1);
                //内存里面的nowPrize值至加一
				floorBean.setNowPrize(nowPrize+1);
				//发信给用户
				MessageServiceImpl impl = new MessageServiceImpl();
				MessageBean message = new MessageBean();
				message.setFromUserId(100);
				message.setToUserId(userId);
				message.setContent("恭喜，您在踩踩乐踩中幸运楼，奖金"+prize+"乐币，已经存入银行");
				message.setMark(0);
				impl.addMessage(message);
				doTip("prize", "您踩中了"+count+"楼，祝好运!");
				return;
			}
            //楼层加一
			floorImpl.updateFloorProduct("count=count+1","id="+id);
			//如果踩过1000楼，楼层返回0
			if(count>=1000)
			{
				
				//数据库里的count字段为0
				floorImpl.updateFloorProduct("count=0","id="+id);
                //内存里面的count值至为0
				floorBean.setCount(count=0);
			}
			//如果中奖数大于等于设定的数，游戏结束
			if(nowPrize>=number)
			{
				floorImpl.updateFloorProduct("mark=1","id="+id);
				OsCacheUtil.flushGroup(OsCacheUtil.FLOOR_CACHE_GROUP,
						"query");
                //发信给用户
				MessageServiceImpl impl = new MessageServiceImpl();
				MessageBean message = new MessageBean();
				message.setFromUserId(100);
				message.setToUserId(floorBean.getUserId());
				message.setContent("您出资的乐酷踩踩乐游戏"+StringUtil.toWml(floorBean.getContent())+"已经结束，总奖金"+num+"乐币，共有"+floorBean.getNumber()+"楼中奖，每楼奖金"+floorBean.getPrize());
				message.setMark(0);
				impl.addMessage(message);
			}
			//清理缓存
			/*OsCacheUtil.flushGroup(OsCacheUtil.FLOOR_CACHE_GROUP,
					"SELECT * FROM tread_floor WHERE mark=0");
			OsCacheUtil.flushGroup(OsCacheUtil.FLOOR_CACHE_GROUP,
			"SELECT * FROM tread_floor WHERE id="+id);*/
			
			result = "success";
			request.setAttribute("result", result);
             //把踩楼实况输出到页面
			UserBean user = (UserBean)UserInfoUtil.getUser(loginUser.getId());
			String name = user.getNickName();
			int counts = floorBean.getCount();
			int floors = floorBean.getFloor();
				if(counts<0)
				{
					counts=1;
				}else{
		    outLog = (counts+"楼"+":"+ StringUtil.toWml(name));
		    addLog(outLog);
            //内存里面的count值至加一
		    floorBean.setCount(count+1);
				}
			
		}
		
	}
	
}
