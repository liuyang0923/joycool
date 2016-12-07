package net.joycool.wap.spec.friend;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.impl.DummyServiceImpl;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LockUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain： 送礼包功能
 * @datetime:1007-10-24
 */
public class PkgAction extends CustomAction{
	
	public static int END_WAIT = 40 * 1000;		// 等待30秒后才能重新开始棋局
	
	UserBean loginUser;
	public static HashMap pkgTypeMap = null;
	
	public static ICacheMap pkgCache = CacheManage.pkg;
	public static PkgService service = new PkgService();
	public static DummyServiceImpl dummyService = new DummyServiceImpl();
	public static UserServiceImpl userService = new UserServiceImpl();
	
	public PkgAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		loginUser = super.getLoginUser();
	}
	public PkgAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}
	
	public void index() {
		
	}
	// 购买礼包
	public void buy() {
		int id = getParameterInt("id");
		PkgTypeBean bean = getPkgType(id);
		if(bean == null){
			tip("tip", "没有找到这类商品!");
			return;
		}
		setAttribute("type", bean);
		if(hasParam("c")) {
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			if(us.getGamePoint() < bean.getPrice()) {
				tip("tip", "乐币不足,购买失败!");
				return;
			}
			UserInfoUtil.updateUserCash(loginUser.getId(), -bean.getPrice(),
					UserCashAction.OTHERS, "购买礼包-" + bean.getPrice());

			PkgBean pkg = addPkg(loginUser.getId(), bean);
			setAttribute("pkg", pkg);
			tip("tip", "购买成功!");
			return;
		}
	}
	public static byte[] sendLock = new byte[0];
	// 发送礼包最后一步
	public void send() {
		int id = getParameterInt("id");
		PkgBean pkg = getPkg(id);
		if(pkg == null || pkg.getUserId() != loginUser.getId() || pkg.getStatus() != 0) {
			tip("tip", "没有找到这类商品!");
			return;
		}
		setAttribute("pkg", pkg);
		int toId = getParameterInt("to");
		UserBean to = UserInfoUtil.getUser(toId);
		if(to == null || toId == loginUser.getId()) {
			tip("tip", "用户不存在!");
			return;
		}
		setAttribute("to", to);
		
		if(hasParam("c")) {		// 确认发送
			synchronized(sendLock) {
				if(pkg.getStatus() != 0) {
					tip("tip", "礼包发送失败!");
					return;
				}
				if(pkg.getSendTime() > System.currentTimeMillis()) {
					pkg.setStatus(1);
					SqlUtil.executeUpdate("insert into mcoolgame.pkg_queue set id=" + pkg.getId() + ",send_time='" + DateUtil.formatSqlDatetime(pkg.getSendTime()) + "'", 4);
					tip("tip", "礼包将会被定时送出!");
				} else {
					pkg.setStatus(2);
					pkg.setSendTime(System.currentTimeMillis());
					NoticeAction.sendNotice(toId, loginUser.getNickNameWml() + "送来一个礼包", NoticeBean.GENERAL_NOTICE, "/wgame/pkg/my2.jsp");
					tip("tip", "成功将礼包送出!");
				}
				pkg.setToId(toId);
				SqlUtil.executeUpdate("update mcoolgame.pkg set send_time=now(),status=" + pkg.getStatus() + ",to_id=" + toId + " where id=" + id, 4);
			}
			
			request.removeAttribute("pkg");	// 发出后不用返回了
		}
	}
	// 打开礼包
	public void open() {
		int id = getParameterInt("id");
		PkgBean pkg = getPkg(id);
		if(pkg == null || pkg.getToId() != loginUser.getId() 
				|| pkg.getStatus() < 2 || pkg.getOpenTime() > System.currentTimeMillis()) {
			tip("tip", "暂时还不能打开这个礼包!");
			return;
		}
		setAttribute("pkg", pkg);
		if(pkg.getStatus() == 2) {
			pkg.setStatus(3);
			pkg.setOpenTime(System.currentTimeMillis());
			SqlUtil.executeUpdate("update mcoolgame.pkg set open_time=now(),status=3 where id=" + id, 4);
			if(pkg.getMoney() > 0) {
				UserInfoUtil.updateUserCash(loginUser.getId(), pkg.getMoney(),
						UserCashAction.OTHERS, "礼包红包" + pkg.getMoney());
			}
			List list = StringUtil.toInts(pkg.getItem());
			for(int i = 0;i < list.size();i++) {
				int id2 = ((Integer)list.get(i)).intValue();
				UserBagCacheUtil.updateUserBagChangeCache("user_id=" + loginUser.getId() , "id="
						+ id2, loginUser.getId(), 0, id2);
			}
		}
	}
	// 写入标题和正文
	public void write() {
		int id = getParameterInt("id");
		PkgBean pkg = getPkg(id);
		if(pkg == null || pkg.getUserId() != loginUser.getId() || pkg.getStatus() != 0) {
			tip("tip", "没有找到这类商品!");
			return;
		}
		setAttribute("pkg", pkg);
		
		String title = getParameterString("title");
		String content = getParameterString("content");
		if(title != null && content != null && !isMethodGet()) {
			if(title.length() > 50)
				title = title.substring(0, 50);
			if(content.length() > 1000)
				content = content.substring(0, 1000);
			pkg.setTitle(title);
			pkg.setContent(content);
			service.updatePkg(pkg);
			tip("tip", "修改成功!");
		}
	}
	
	// 发送时间和打开时间
	public void set() {
		int id = getParameterInt("id");
		PkgBean pkg = getPkg(id);
		if(pkg == null || pkg.getUserId() != loginUser.getId() || pkg.getStatus() != 0) {
			tip("tip", "没有找到这类商品!");
			return;
		}
		setAttribute("pkg", pkg);
		
		String date = request.getParameter("date");
		String hour = request.getParameter("hour");
		String minute = request.getParameter("minute");
		if(date != null) {
			if(date.length() == 0) {
				pkg.setSendTime(0);
				pkg.setOpenTime(0);
				service.updatePkg2(pkg);
				tip("tip", "发送和打开礼包的时间设置已删除!");
			} else {
				String date2 = request.getParameter("date2");
				String hour2 = request.getParameter("hour2");
				String minute2 = request.getParameter("minute2");
				Date sendTime = DateUtil.parseTime(date + ' ' + hour + ':' + minute + ":0");
				Date openTime = DateUtil.parseTime(date2 + ' ' + hour2 + ':' + minute2 + ":0");
				if(sendTime == null || openTime == null) {
					tip("tip", "输入的时间不正确!");
					return;
				}
				pkg.setSendTime(sendTime.getTime());
				pkg.setOpenTime(openTime.getTime());
				service.updatePkg2(pkg);
				tip("tip", "修改成功!");
			}
		}
	}
	
	
	// 设置
	public void set2() {
		int id = getParameterInt("id");
		PkgBean pkg = getPkg(id);
		if(pkg == null || pkg.getUserId() != loginUser.getId() || pkg.getStatus() != 0) {
			tip("tip", "没有找到这类商品!");
			return;
		}
		setAttribute("pkg", pkg);
		if(pkg.getMoney() > 0 || pkg.getItem().length() != 0) {
			tip("tip", "无法进行修改!");
			return;
		}
		int option = getParameterInt("o");
		if(option == 2) {	// 重置
			pkg.setPreMoney(0);
			pkg.setItemList(null);
			return;
		}
		if(option == 1) {	// 确认修改
			Object userLock = LockUtil.userLock.getLock(loginUser.getId());
			synchronized(userLock) {
				UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
				if(us.getGamePoint() < pkg.getPreMoney()) {
					tip("tip", "现金不足!");
					return;
				}
				List items = pkg.getItemList();
				if(items != null) {
					for(int i = 0;i < items.size();i++) {
						UserBagBean item = UserBagCacheUtil.getUserBagCache(((Integer)items.get(i)).intValue());
						if(item == null || item.getUserId() != loginUser.getId()) {
							tip("tip", "有些物品无法设置为礼品");
							return;
						}
						DummyProductBean dummyProduct = dummyService.getDummyProducts(item.getProductId());
						if(dummyProduct == null || dummyProduct.isBind() || item.isMarkBind()) {		// 绑定的不可以赠送
							tip("tip", "有些物品无法设置为礼品");
							return;
						}
					}
					
					StringBuilder sb = new StringBuilder(32);
					StringBuilder sb2 = new StringBuilder(64);
					for(int i = 0;i < items.size();i++) {
						int id2 = ((Integer)items.get(i)).intValue();
						UserBagBean item = UserBagCacheUtil.getUserBagCache(id2);
						DummyProductBean dummyProduct = dummyService.getDummyProducts(item.getProductId());
						UserBagCacheUtil.updateUserBagChangeCache("user_id=0" , "id="
								+ id2, loginUser.getId(), 0, id2);
						userService.addItemLog(loginUser.getId(), 0, item.getId(), item.getProductId(), item.getTime(), 3);
						if(i > 0) {
							sb.append(',');
							sb2.append(',');
						}
						sb.append(id2);
						sb2.append(dummyProduct.getName());
						if(dummyProduct.getTime() == 1) {
							sb2.append('x');
							sb2.append(item.getTime());
						}
					}

					pkg.setItem(sb.toString());
					pkg.setItemName(sb2.toString());
				}
				UserInfoUtil.updateUserCash(loginUser.getId(), -pkg.getPreMoney(),
						UserCashAction.OTHERS, "设置礼包红包-" + pkg.getPreMoney());

			}
			pkg.setMoney(pkg.getPreMoney());
			service.updatePkg3(pkg);
			tip("tip", "修改成功!");
			return;
		}
		int money = getParameterInt("money");
		String items = request.getParameter("items");
		if(money > 0 || items != null && items.length() > 0) {
			
			if(items != null && items.length() > 0) {
				
				List list = new ArrayList();
				String[] items2 = items.split(";");
				for(int i = 0;i < items2.length;i++) {
					int tmp = StringUtil.toInt(items2[i]);
					if(tmp > 0)
						list.add(Integer.valueOf(tmp));
				}
				if(list.size() > 0) {
					PkgTypeBean type = getPkgType(pkg.getType());
					if(list.size() > type.getCount()) {
						tip("tip", "最多只能放置" + type.getCount() + "件礼品!");
						return;
					}
					pkg.setItemList(list);
				}
			}
			pkg.setPreMoney(money);
		}
	}
	
	public UserBean getLoginUser() {
		return loginUser;
	}
	public static List getPkgTypeList() {
		return new ArrayList(getPkgTypeMap().values());
	}
	public static HashMap getPkgTypeMap() {
		if(pkgTypeMap != null)
			return pkgTypeMap;
		synchronized(PkgAction.class) {
			if(pkgTypeMap != null)
				return pkgTypeMap;
			List list = service.getPkgTypeList("1 order by seq");
			pkgTypeMap = new LinkedHashMap();
			for(int i = 0;i < list.size();i++) {
				PkgTypeBean bean = (PkgTypeBean)list.get(i);
				pkgTypeMap.put(Integer.valueOf(bean.getId()), bean);
			}
		}
		return pkgTypeMap;
	}
	public static PkgTypeBean getPkgType(int id) {
		return (PkgTypeBean)getPkgTypeMap().get(Integer.valueOf(id));
	}
	public static PkgTypeBean getPkgType(Integer iid) {
		return (PkgTypeBean)getPkgTypeMap().get(iid);
	}
	// 获得礼包列表，拥有的、接收的
	public static List getPkgList(int id) {
		return SqlUtil.getIntList("select id from mcoolgame.pkg where user_id=" + id + " and status in(0,1,2,3) order by id desc", 4);
	}
	
	public static List getReceiveList(int id) {
		return SqlUtil.getIntList("select id from mcoolgame.pkg where to_id=" + id + " and status in(2,3) order by send_time desc", 4);
	}
	public static long defOpenTime = DateUtil.parseTime("2008-12-25 8:00:00").getTime();
	public static PkgBean addPkg(int userId, PkgTypeBean type) {
		PkgBean pkg = new PkgBean();
		pkg.setUserId(userId);
		pkg.setType(type.getId());
		pkg.setTitle("礼签");
		pkg.setContent("礼包内正文");
		pkg.setItem("");
		pkg.setItemName("");
		pkg.setOpenTime(defOpenTime);
		pkg.setCreateTime(System.currentTimeMillis());
		service.addPkg(pkg);
		pkgCache.spt(new Integer(pkg.getId()), pkg);
		return pkg;
	}
	
	public static PkgBean getPkg(int id) {
		return getPkg(new Integer(id));
	}
	public static PkgBean getPkg(Integer iid) {
		synchronized(pkgCache) {
			PkgBean pkg = (PkgBean)pkgCache.get(iid);
			if(pkg == null) {
				pkg = service.getPkg("id=" + iid);
				if(pkg != null)
					pkgCache.put(iid, pkg);
			}
			return pkg;
		}
	}
	public static DummyProductBean getItem(int itemId) {
		return dummyService.getDummyProducts(itemId);
	}
	
	public static void task() {
		DbOperation dbOp = new DbOperation(4);
		try {
			List list = dbOp.getIntList("select id from mcoolgame.pkg_queue where send_time<=now()");
			for(int i = 0;i < list.size();i++) {
				Integer iid = (Integer)list.get(i);
				PkgBean pkg = getPkg(iid);
				synchronized(sendLock) {
					if(pkg.getStatus() != 1) {
						continue;
					}
					pkg.setStatus(2);
					pkg.setSendTime(System.currentTimeMillis());
					UserBean loginUser = UserInfoUtil.getUser(pkg.getUserId());
					dbOp.executeUpdate("update mcoolgame.pkg set send_time=now(),status=2 where id=" + iid);
					NoticeAction.sendNotice(pkg.getToId(), loginUser.getNickNameWml() + "送来一个礼包", NoticeBean.GENERAL_NOTICE, "/wgame/pkg/my2.jsp");
				}
				dbOp.executeUpdate("delete from mcoolgame.pkg_queue where id=" + iid);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		dbOp.release();
	}
}
