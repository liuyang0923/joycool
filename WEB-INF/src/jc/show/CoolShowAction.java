package jc.show;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.*;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.cache.StaticCacheMap;
import net.joycool.wap.framework.*;
import net.joycool.wap.spec.shop.ShopAction;
import net.joycool.wap.spec.shop.ShopUtil;
import net.joycool.wap.spec.shop.UserInfoBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.*;
import net.joycool.wap.util.UserInfoUtil;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CoolShowAction extends CustomAction {
	public static CoolShowService service = new CoolShowService();

	public static ICacheMap cooluser = CacheManage.addCache(new LinkedCacheMap(2000, true),
			"cooluser");

	public static int BUY = 3;

	public static int GIVE = 4;

	public static String IMAGE_URL_TRY = Constants.RESOURCE_ROOT_PATH_OLD + "show/tried/";

	public static String IMAGE_URL_GOODS = Constants.RESOURCE_ROOT_PATH_OLD + "show/goods/";

	public static String IMAGE_URL_COMMODITY = Constants.RESOURCE_ROOT_PATH_OLD + "show/comm/";

	public static String IMAGE_URL_ACT = Constants.RESOURCE_ROOT_PATH_OLD + "show/act/";

	public static String IMAGE_URL_THUMB = Constants.RESOURCE_ROOT_PATH_OLD + "show/t/";

	public static String[] IMAGE_URL = { "", "show/goods/", "/show/comm/", "/show/act/",
			"/show/tried/" };

	public static String[] part = { "脸型", "身材", "发型", "上衣", "裤子", "饰品", "背景" };

	public static String[] place = { "商城", "收藏夹" };

	public static int COUNT_PRE_PAGE = 8;

	public static String ALL_URL = Constants.RESOURCE_ROOT_PATH_OLD;

	public CoolShowAction() {
	}

	public CoolShowAction(HttpServletRequest request) {
		super(request);
	}

	public void prepareImage(UserBean ub, int from) {
		CoolUser cu = getCoolUser(ub);
		boolean redraw = false;
		TryBean tryShow = (TryBean) session.getAttribute("tryShow");
		if (tryShow == null) { // 这个session没有进入过试衣间，初始化
			tryShow = prepareTryShow(cu);
			session.setAttribute("tryShow", tryShow);
			redraw = true;
		}
		// 切换性别
		if (hasParam("chg")) {
			tryShow.toggleGender();
			redraw = true;
		}
		// 还原成之前保存的性别
		int changeGender = getParameterIntS("g");
		if (changeGender == 0 || changeGender == 1) {
			if(tryShow.getGender() != changeGender){
				tryShow.setGender(changeGender);
				redraw = true;
			}
		}

		int alld = getParameterInt("alld");
		if (alld > 0) {
			tryShow.reset();
			redraw = true;
		} else {
			int Iid = this.getParameterInt("Iid");
			if (Iid > 0) {

				Commodity comm = getCommodity(Iid);
				if (comm == null)
					return;
				// 性别矛盾不可试穿
				int gender = comm.getGender();
				if (gender != 2 && tryShow.getGender() != gender) { // 通用或者性别适用
					setAttribute("c", Integer.valueOf(gender));	// 切换到适合的性别
					return;
				}
				Pocket po = service.getDate1("del=0 and user_id=" + ub.getId() + " and item_id=" + Iid);
				if (comm.getDel() == 1 && po == null)
					return;

				tryShow.tryOne(comm);
				redraw = true;

			}
			String strId = getParameterString("strId");
			if (strId != null) {
				List list = StringUtil.toInts(strId);
				tryShow.reset();
				redraw = true;
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						int itemId = ((Integer) list.get(i)).intValue();
						Commodity com = CoolShowAction.getCommodity(itemId);
						if (com.getGender() != 2 && tryShow.getGender() != com.getGender()) {
							// 查看每一个物品的性别是否正确，如果有一个不行就切换性别
							tryShow.setGender(com.getGender());
						}
					}
					for (int i = 0; i < list.size(); i++) {
						int itemId = ((Integer) list.get(i)).intValue();
						Pocket po = service.getDate1("del=0 and user_id=" + ub.getId()
								+ " and item_id=" + itemId);
						Commodity com = CoolShowAction.getCommodity(itemId);
						if (com != null) {
							if (com.getDel() == 1 && po == null)
								continue;
							int gender = com.getGender();
							// 性别不矛盾方可试穿
							if (gender == 2 || tryShow.getGender() == gender) { // 通用或者性别适用
								tryShow.tryOne(com);
							}
						}
					}
				}
			}
		}
		if (!redraw) { // 第一次访问需要
			if (cu.getImgurlF().length() == 0 && tryShow.getGender() == 1
					|| cu.getImgurlM().length() == 0 && tryShow.getGender() == 0)
				redraw = true;
		}
		if (from == 1) { // 保存，已经移动到save()

		} else if (redraw) { // 试穿
			String name = mergeImage(ub.getId(), tryShow.getGoodsList(), 4);

			if (name != null) {
				if (tryShow.getGender() == 0) {
					delFile(cu.getImgurlF(), 4);
					cu.setImgurlF(name);
					service.upd("update show_users set img_female_url='" + StringUtil.toSql(name)
							+ "' where user_id=" + cu.getUid());
				} else {
					delFile(cu.getImgurlM(), 4);
					cu.setImgurlM(name);
					service.upd("update show_users set img_male_url='" + StringUtil.toSql(name)
							+ "' where user_id=" + cu.getUid());
				}
			}
		}
	}

	/**
	 * 合成show
	 * 
	 * @param basicId
	 * @param others
	 * @throws IOException
	 */
	public static String mergeImage(int uid, List goodsList, int from) {
		List list = new ArrayList(goodsList.size());
		for (int i = 0; i < goodsList.size(); i++) {
			String img = (String) goodsList.get(i);

			String file = IMAGE_URL_GOODS + img;
			list.add(file);
		}
		String name = getPicName(uid);

		if (from == 3) {
			mergeImage(list, IMAGE_URL_ACT + name, IMAGE_URL_THUMB + name);
		} else {
			mergeImage(list, IMAGE_URL_TRY + name, null);
		}
		return name;
	}

	public static boolean mergeImage(List sourceList, String comboImageName, String thumbImageName) {
		if (sourceList == null || sourceList.size() == 0) {
			return false;
		}
		try {
			Iterator iter = sourceList.listIterator();
			BufferedImage comboImageBI = null;
			while (iter.hasNext()) {
				String oriPath = (String) iter.next();
				File oriImageFile = new File(oriPath);
				Image img = ImageIO.read(oriImageFile);
				if (comboImageBI == null) {
					comboImageBI = new BufferedImage(img.getWidth(null), img.getHeight(null),
							BufferedImage.TYPE_INT_RGB);
					comboImageBI.getGraphics().setColor(Color.white);
					comboImageBI.getGraphics().fillRect(0, 0, img.getWidth(null),
							img.getHeight(null));
				}
				comboImageBI.getGraphics().drawImage(
						img.getScaledInstance(img.getWidth(null), img.getHeight(null),
								Image.SCALE_SMOOTH), 0, 0, null);
			}
			File comboFile = new File(comboImageName);
			Iterator iws = ImageIO.getImageWritersByFormatName("gif");
			ImageWriter iw = (ImageWriter) iws.next();
			ImageOutputStream ios = ImageIO.createImageOutputStream(comboFile);
			iw.setOutput(ios);
			iw.write(comboImageBI);
			ios.close();
			if (thumbImageName != null) {
				File thumbFile = new File(thumbImageName);
				ios = ImageIO.createImageOutputStream(thumbFile);
				iw.setOutput(ios);
				iw.write(comboImageBI.getSubimage(15, 0, 90, 120));
				ios.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获得用户酷秀信息
	 * 
	 * @param ub
	 * @return
	 */
	public static CoolUser getCoolUser(UserBean ub) {
		Integer key = new Integer(ub.getId());
		synchronized (cooluser) {
			CoolUser cu = (CoolUser) cooluser.get(key);
			if (cu == null) {
				cu = service.getCoolUser("user_id=" + ub.getId());
				if (cu == null) {
					service.upd("insert into show_users(gender_useing,user_id,img_url)values("
							+ ub.getGender() + "," + ub.getId() + ",'')");
					cu = new CoolUser();
					cu.setUid(ub.getId());
					cu.setGenderUseing(ub.getGender());
				}
				cooluser.put(key, cu);
			}
			return cu;
		}
	}

	/**
	 * 获得当前其他穿着
	 * 
	 * @return
	 */
	public List getOthers(CoolUser cu) {
		List list = null;
		if (cu.getGenderUseing() == 0)
			list = service
					.getMyGoods("del=0 and gender<>1 and state=1 and user_id =" + cu.getUid());
		else
			list = service
					.getMyGoods("del=0 and gender<>0 and state=1 and user_id =" + cu.getUid());
		// 过滤过期物品
		if (list != null && list.size() > 0) {
			List expired = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Pocket po = (Pocket) list.get(i);
				if (System.currentTimeMillis() > po.getEndTime()) {
					expired.add(po);
				}
			}
			if (expired.size() > 0) {
				doExpired(expired);
				getOthers(cu);
			}
		}
		return list;
	}

	/**
	 * 把过期物品脱下
	 * 
	 * @param expired
	 */
	public void doExpired(List expired) {
		for (int i = 0; i < expired.size(); i++) {
			Pocket po = (Pocket) expired.get(i);
			service.upd("update show_pocket set del=1 where id=" + po.getId());
		}
	}

	/**
	 * 我的物品
	 * 
	 * @param uid
	 * @param g
	 * @param p
	 * @return
	 */
	public List getMyGoods(String cond) {
		List list = service.getMyGoods(cond);
		// 过滤过期物品
		if (list != null && list.size() > 0) {
			List expired = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Pocket po = (Pocket) list.get(i);
				if (po.getDel() == 0 && System.currentTimeMillis() > po.getEndTime()) {
					expired.add(po);
				}
			}
			if (expired.size() > 0) {
				doExpired(expired);
				list = service.getMyGoods(cond);
			}
		}
		return list;
	}

	/**
	 * 商城商品列表
	 * 
	 * @param g
	 * @param p
	 * @return
	 */
	public List getDowntown(int g, int p) {
		if (g > 0 && g < 3) {
			if (g == 2) {
				if (p > 0 && p <= part.length)
					return service.getCommodityList(" del=0 and gender<>0 and type=" + p);
				else
					return service.getCommodityList(" del=0 gender<>0");
			} else {
				if (p > 0 && p <= part.length)
					return service.getCommodityList(" del=0 gender<>1 and type=" + p);
				else
					return service.getCommodityList(" del=0 gender<>1");
			}
		} else
			return null;
	}

	public List getFriShowList(String friendString) {
		if ("".equals(friendString)) {
			return new ArrayList(0);
		} else {
			List list = service.getCoolUsers(" img_url != '' and user_id in (" + friendString
					+ ") group by user_id");
			return list;
		}
	}

	/**
	 * 物品展示,(后台用)
	 * 
	 * @param type
	 * @return
	 */
	// public List getGoodsList(int type) {
	// if (type > 0 && type <= part.length)
	// return service.getGoodsList(" type=" + type);
	// else
	// return service.getGoodsList(" 1");
	// }
	//
	// public static ICacheMap showGoodsCache = CacheManage.addCache(
	// new StaticCacheMap(500), "showGoods");
	//
	// public static Goods getGoods(int id) {
	// Goods bean = null;
	// Integer key = new Integer(id);
	// synchronized (showGoodsCache) {
	// bean = (Goods) showGoodsCache.get(key);
	// if (bean == null) {
	// bean = service.getGoods("id=" + id);
	// if (bean != null) {
	// showGoodsCache.put(key, bean);
	// }
	// }
	// }
	// return bean;
	// }
	//
	// public static void flushGoodsCache(int id) {
	// showGoodsCache.srm(new Integer(id));
	// }
	public static ICacheMap showCommCache = CacheManage.addCache(new StaticCacheMap(500),
			"showComm");

	public static Commodity getCommodity(int id) {
		Commodity bean = null;
		Integer key = new Integer(id);
		synchronized (showCommCache) {
			bean = (Commodity) showCommCache.get(key);
			if (bean == null) {
				bean = service.getCommodity("id=" + id);
				if (bean != null) {
					PartBean part = getPart(bean.getType());
					bean.setPartName(part.getName());
					bean.setLevelLayer(part.getLvlLayer());
					bean.setLevelShow(part.getLvlShow());
					bean.setIndex(part.getIndex());

					CatalogBean catalog = getCatalog(bean.getCatalog());
					bean.setCatalogName(catalog.getName());
					showCommCache.put(key, bean);
				}
			}
		}
		return bean;
	}

	public static void flushCommodityCache(int id) {
		showCommCache.srm(new Integer(id));
	}

	/**
	 * 过滤map,返回需要购买的商品
	 * 
	 * @param cu
	 * @param map
	 */
	public List filterMyGoods(CoolUser cu, TryBean tryShow) {
		List list = new ArrayList();
		Iterator iter = tryShow.getCommodityList().iterator();
		while (iter.hasNext()) {
			Commodity com = (Commodity) iter.next();
			Pocket po = service.getDate1("del=0 and item_id=" + com.getId() + " and user_id="
					+ cu.getUid());
			if (com != null && po == null && com.getDel() == 0) {
				list.add(com);
			}
		}
		return list;

	}

	/**
	 * 保存形象，如果包含没有购买的物品，把总价格算出来，提示购买（即使免费也提示），如果购买成功则自动保存形象
	 * 
	 * @return
	 */
	public static void save(CoolUser cu, TryBean tryShow) {
		if(tryShow == null)	// 根据cu获得一个当前装扮的tryShow bean便于绘制，如果是这样，需要判断每个物品是否过期
			tryShow = prepareTryShow(cu);
		List list = tryShow.getCommodityList();
		service.upd("update show_pocket set state=0 where user_id=" + cu.getUid());
		StringBuilder strId = new StringBuilder();
		long now = System.currentTimeMillis();
		long endTime = now + DateUtil.MS_IN_DAY * 365; // 没有过期时间限制的，默认1年过期
		for (int i = 0; i < list.size(); i++) {
			Commodity comm = (Commodity) list.get(i);
			Pocket po = service.getDate1("del=0 and item_id=" + comm.getId() + " and user_id="
					+ cu.getUid());
			if (po != null) {
				if(po.getEndTime() < now) {	// 过期了
					service.upd("update show_pocket set del=1 where id=" + po.getId());
					po = null;	// 重新为null便于之后判断，脱下物品
				} else {
					service.upd("update show_pocket set state=1 where id=" + po.getId());
					strId.append(comm.getId());
					strId.append(",");
					if (endTime > po.getEndTime()) {
						endTime = po.getEndTime();
					}
				}
			}
			// 注意：不能else同以上合并，因为以上代码中修改了po的值
			if(po == null) {	// 没有或者过期了，脱下物品
				tryShow.takeOff(comm);
			}
		}
		if (strId.length() > 0)
			strId.setLength(strId.length() - 1);
		String s = strId.toString();
		cu.setGenderUseing(tryShow.getGender());
		cu.setEndTime(endTime);
		cu.setCurItem(s);
		
//		 生成酷秀形象图片
		String name = mergeImage(cu.getUid(), tryShow.getGoodsList(), 3);
		delFile(cu.getImgurl(), 3);
		cu.setImgurl(name);
		service.upd("update show_users set gender_useing=" + cu.getGenderUseing() + ",cur_item='"
				+ s + "',img_url='" + name + "',end_time='" + DateUtil.formatSqlDatetime(endTime)
				+ "' where user_id=" + cu.getUid());
	}
	// 根据cooluser初始化一个tryShow
	public static TryBean prepareTryShow(CoolUser cu) {
		TryBean tryShow = new TryBean(cu.getGenderUseing());
		int[] t = tryShow.getCurrentTry(); // 默认穿上保存了的物品
		List list = StringUtil.toInts(cu.getCurItem());
		for (int i = 0; i < list.size(); i++) {
			Commodity com = CoolShowAction.getCommodity(((Integer) list.get(i)).intValue());
			t[com.getIndex()] = com.getId();
			// 如果是一个覆盖多个层的物品，分别把其他层设置好
			for(int i2 = 0;i2 < com.getPartOtherCount();i2++) {
				int ii = com.getPartOtherIndex()[i2];
				t[ii] = -com.getId();		// 覆盖位置，保存-id，不用绘制
			}
		}
		return tryShow;
	}

	/**
	 * 统一购买
	 * 
	 * @param cu
	 * @param list
	 * @return
	 */
	public int buyAll(CoolUser cu, List list, TryBean tryShow) {
		if (list.size() == 0) { // 没有需要购买的,直接保存
			save(cu, tryShow);		// 购买后直接保存
			return 1;
		}

		if ("buyall".equals(session.getAttribute("ses"))) {
			session.removeAttribute("ses");
		} else {
			return 3;
		}

		UserInfoBean bean = ShopAction.shopService.getUserInfo(cu.getUid());
		if (bean == null) {
			ShopAction.shopService.addUserInfo(cu.getUid());
			return 2;
		}
		int due = this.getParameterInt("due");
		if (due != 1 && due != 3 && due != 6 && due != 12)
			due = 1;
		int totalprice = 0;
		for (int i = 0; i < list.size(); i++) {
			Commodity com = (Commodity) list.get(i);

			Pocket po = service.getDate1("del=0 and user_id=" + cu.getUid() + " and item_id="
					+ com.getIid());
			if (com.getDel() == 1 && po == null) {

			} else {
				totalprice += com.getPrice(due);
			}
		}
		if (canPay(totalprice, cu.getUid())) {
			for (int i = 0; i < list.size(); i++) {
				Commodity com = (Commodity) list.get(i);
				float price = com.getPrice(due);
				bean.decreaseGold(price);
				ShopUtil.updateUserGold(cu.getUid(), bean.getGold(), price, BUY, com.getIid(), 0);
				addProductCount(com.getId());// 获得物品
				addGood(com, cu.getUid(), due);// 添加记录
				addHistory(com.getId(), cu.getUid(), 0);
			}
			save(cu, tryShow); 		// 购买后直接保存
			return 1;
		} else {
			return 2;// 资金不足
		}
	}

	/**
	 * 单件购买
	 * 
	 * @param cu
	 * @param Iid
	 * @return
	 */
	public int buy(CoolUser cu, int Iid) {
		if ("buy".equals(session.getAttribute("ses"))) {
			session.removeAttribute("ses");
		} else {
			return 5;
		}
		Commodity com = getCommodity(Iid);
		if (com == null)
			return 0;
		UserInfoBean bean = ShopAction.shopService.getUserInfo(cu.getUid());
		if (bean == null) {
			ShopAction.shopService.addUserInfo(cu.getUid());
			return 3;
		}
		int due = this.getParameterInt("due");
		if (due != 1 && due != 3 && due != 6 && due != 12)
			due = 1;
		Pocket po = service.getDate1("del=0 and user_id=" + cu.getUid() + " and item_id=" + Iid);
		if (com.getDel() == 1 && po == null)
			return 4;
		float price = com.getPrice(due);
		if (canPay(com.getPrice(), cu.getUid())) {
			if (price > 0) {
				bean.decreaseGold(price);
				ShopUtil.updateUserGold(cu.getUid(), bean.getGold(), price, BUY, com.getIid(), 0);
			}
			addProductCount(com.getId());// 所购买商品数量+1;
			addGood(com, cu.getUid(), due);// 获得物品
			addHistory(Iid, cu.getUid(), 0);// 添加记录
			if (po == null)
				return 1;
			else
				return 2;

		} else {
			return 3;// 资金不足
		}
	}

	/**
	 * 单件赠送
	 * 
	 * @param cu
	 * @param Iid
	 * @return
	 */
	public int give(int Iid, int uid, int touid) {
		if ("give".equals(session.getAttribute("ses"))) {
			session.removeAttribute("ses");
		} else {
			return 4;
		}
		Commodity com = getCommodity(Iid);
		if (com == null || com.getDel() != 0)
			return 0;// 不存在酷秀
		UserInfoBean bean = ShopAction.shopService.getUserInfo(uid);
		if (bean == null) {
			ShopAction.shopService.addUserInfo(uid);
			return 2;
		}
		UserBean toub = UserInfoUtil.getUser(touid);
		if (toub == null)
			return 3;// 不存在用户
		int due = this.getParameterInt("due");
		if (due != 1 && due != 3 && due != 6 && due != 12)
			due = 1;
		float price = com.getPrice(due);
		if (canPay(price, uid)) {
			bean.decreaseGold(price);
			ShopUtil.updateUserGold(uid, bean.getGold(), price, GIVE, com.getIid(), touid);
			addProductCount(com.getId());
			// 赠送通知
			NoticeAction.sendNotice(touid, this.getLoginUser().getNickName() + "送您【"
					+ StringUtil.toWml(com.getName()) + "】", "", NoticeBean.GENERAL_NOTICE, "",
					"/kshow/history.jsp?type=2");
			addGood(com, touid, due);// 获得物品
			addHistory(Iid, uid, touid);// 添加历史记录
			return 1;
		} else {
			return 2;// 资金不足
		}
	}

	// 得到酷秀缩略图文件名（不包含路径）
	public static String getCoolShowThumb(UserBean user) {
		CoolUser cu = getCoolUser(user);
		if (cu != null && cu.getImgurl().length() != 0) {
			checkExpired(cu);
			return cu.getImgurl();
		}
		return null;
	}
//	 检查酷秀是否过期
	public static void checkExpired(CoolUser cu) {
		long now = System.currentTimeMillis();
		if(cu.getEndTime() < now) {
			synchronized(cu) {	// 避免多次过期
				if(cu.getEndTime() < now) {
					save(cu, null);	// 重新保存
				}
			}
		}
	}

	/**
	 * 获得物品[判断过期天数]
	 * 
	 * @param com
	 * @param touid
	 * @return
	 */
	public boolean addGood(Commodity com, int touid, int due) {
		if (com == null)
			return false;
		if (due != 1 && due != 3 && due != 6 && due != 12)
			due = 1;
		due *= 30;
		if (com.getPrice() == 0) {
			if (com.getDue() != 0)
				due = com.getDue();
			else
				due = 30;
		}
		return addGood2(com, touid, due);
	}
	
	/**
	 * 获得物品
	 * 
	 * @param com
	 * @param touid
	 * @return
	 */
	public boolean addGood2(Commodity com, int touid, int due) {
		Pocket po = service.getDate1(" item_id=" + com.getIid() + " and user_id=" + touid);
		if (po != null) {
			if (com.getPrice() == 0) {
				service.upd("update show_pocket set del=0,end_time=DATE_ADD(now(),INTERVAL " + due
						+ " DAY) where item_id=" + com.getIid() + " and user_id=" + touid);

			} else if (po.getEndTime() > System.currentTimeMillis()) {
				service.upd("update show_pocket set del=0,end_time=DATE_ADD(end_time,INTERVAL "
						+ due + " DAY) where item_id=" + com.getIid() + " and user_id=" + touid);
			} else {
				service.upd("update show_pocket set del=0,end_time=DATE_ADD(now(),INTERVAL " + due
						+ " DAY) where item_id=" + com.getIid() + " and user_id=" + touid);
			}
		} else {
			service
					.upd("insert into show_pocket (create_time,end_time,type,gender,item_id,user_id,name)values(now(),DATE_ADD(now(),INTERVAL "
							+ due
							+ " DAY),"
							+ com.getType()
							+ ","
							+ com.getGender()
							+ ","
							+ com.getIid()
							+ ","
							+ touid
							+ ",'"
							+ StringUtil.toSql(com.getName())
							+ "')");
		}
		// 个人物品数+1
		service.upd("update show_users set sum_mygoods=sum_mygoods+1 where user_id=" + touid);
		return true;
	}

	/**
	 * 增加商品购买数量
	 * 
	 * @param cid
	 * @return
	 */
	public boolean addProductCount(int cid) {
		if (cid < 0)
			return false;
		Commodity com = getCommodity(cid);
		if (com != null) {
			service.upd("update show_commodity set count=count+1 where id=" + cid);
			return true;
		}
		return false;
	}

	/**
	 * 添加历史记录
	 * 
	 * @return
	 */
	public boolean addHistory(int Iid, int uid1, int uid2) {
		if (Iid < 0)
			return false;
		if (uid1 < 0) {
			return false;
		}
		if (uid2 < 0) {
			return false;
		}
		service
				.upd("insert into show_history (create_time,item_id,user_id_1,user_id_2)values(now(),"
						+ Iid + "," + uid1 + "," + uid2 + ")");
		return true;
	}

	/**
	 * 付费
	 * 
	 * @return
	 */
	public boolean canPay(float price, int uid) {
		UserInfoBean bean = ShopAction.shopService.getUserInfo(uid);
		if (bean == null) {
			ShopAction.shopService.addUserInfo(uid);
			return false;
		} else
			return ShopUtil.hasEnoughMoney(price, bean.getGold());
	}

	/**
	 * 取出系统时间最后num位置,加uid命名法
	 * 
	 * @param uid
	 * @param num
	 * @return
	 */
	public static String getPicName(int uid) {
		StringBuilder sb = new StringBuilder(25);
		sb.append(uid);
		sb.append('_');
		sb.append((System.currentTimeMillis() / 500) % 1000000);
		sb.append(".gif");
		return sb.toString();
	}

	/**
	 * 删除原来show图片
	 * 
	 * @param name
	 * @param from
	 * @return
	 */
	public static boolean delFile(String name, int from) {
		if (name == null || name.length() < 6)
			return false;
		if (from == 3) { // 删除小图
			File file2 = new File(Constants.RESOURCE_ROOT_PATH_OLD + "show/t/" + name);
			if (file2.exists() && !file2.isDirectory()) {
				file2.delete();
			}
		}
		File file = new File(Constants.RESOURCE_ROOT_PATH_OLD + IMAGE_URL[from] + name);
		if (file.exists() && !file.isDirectory()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 收藏
	 * 
	 * @param uid
	 * @param Iid
	 * @return
	 */
	public int collection(int uid, int Iid) {
		Commodity com = getCommodity(Iid);
		if (com == null || com.getDel() != 0)
			return 0;
		else {
			Collection col = service.getCol("user_id=" + uid + " and item_id=" + Iid);
			if (col != null)
				return 2;
			else {
				service
						.upd("insert into show_collection (create_time,user_id,item_id)values(now(),"
								+ uid + "," + Iid + ")");
				return 1;
			}
		}
	}

	/**
	 * 删除收藏
	 */
	public int delcol(int uid, int Iid) {
		Collection col = service.getCol("user_id=" + uid + " and item_id=" + Iid);
		if (col != null) {
			service.upd("delete from show_collection where user_id=" + uid + " and item_id=" + Iid);
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 删除单条推荐
	 * 
	 * @param del
	 * @return
	 */
	public int delAdv(int del) {
		if (del < 0) {
			return 2;
		} else {
			BeanAdv bean = service.getAdvBean(" id=" + del);
			if (bean != null) {
				service.upd("delete from show_adv where id=" + del);
				return 1;
			} else {
				return 2;
			}
		}
	}

	/**
	 * 添加推荐
	 * 
	 * @param cid
	 * @param place
	 */
	public void addAdv(HttpServletResponse rps, int cid, int place) {
		Commodity com = getCommodity(cid);
		if (com == null || com.getDel() != 0) {
			try {
				rps.sendRedirect("advs.jsp?add=false");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		} else {
			service.upd("insert into show_adv (create_time,comm_id,place)values(now()," + cid + ","
					+ place + ")");
			try {
				rps.sendRedirect("advs.jsp?add=true");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	}

	/**
	 * 修改部位
	 */
	public void alterPart() {
		PartBean bean = new PartBean();
		int id = this.getParameterInt("id");
		int layer = this.getParameterInt("lvllayer");
		int show = this.getParameterInt("lvlshow");
		String name = this.getParameterString("name");
		String bak = this.getParameterString("bak");
		if (id > 0 && layer > 0 && show >= 0 && name != null && !"".equals(name)) {
			bean.setId(id);
			bean.setLvlLayer(layer);
			bean.setLvlShow(show);
			bean.setName(name);
			bean.setBak(bak);
			service.alterPart(bean);
			setAttribute("tip", "修改成功!");
		} else {
			setAttribute("tip", "数据不全,修改失败!");
		}
	}

	/**
	 * 添加部位
	 */
	public void insertPart() {
		PartBean bean = new PartBean();
		int id = this.getParameterInt("id");
		int layer = this.getParameterInt("lvllayer");
		int show = this.getParameterInt("lvlshow");
		String name = this.getParameterString("name");
		String bak = this.getParameterString("bak");
		if (id > 0 && layer > 0 && show >= 0 && name != null && !"".equals(name)) {
			bean.setId(id);
			bean.setLvlLayer(layer);
			bean.setLvlShow(show);
			bean.setName(name);
			bean.setBak(bak);
			service.insertPart(bean);
			setAttribute("tip", "添加成功!");
		} else {
			setAttribute("tip", "数据不全,添加失败!");
		}
	}

	/**
	 * 修改商品分类
	 */
	public void alterCatalog() {
		int id = this.getParameterInt("id");
		CatalogBean bean = getCatalog(id);
		int seq = this.getParameterInt("seq");
		int hide = this.getParameterInt("hide");
		String name = this.getParameterString("name");
		String bak = this.getParameterString("bak");
		if (id > 0 && seq >= 0 && name != null && !"".equals(name)) {
			bean.setSeq(seq);
			bean.setHide(hide);
			bean.setName(name);
			bean.setGender(getParameterInt("gender"));
			bean.setBak(bak);
			service.alterCatalog(bean);
			setAttribute("tip", "修改成功!");
		} else {
			setAttribute("tip", "数据不全,修改失败!");
		}
	}

	/**
	 * 添加商品分类
	 */
	public void insertCatalog() {
		CatalogBean bean = new CatalogBean();
		int id = this.getParameterInt("id");
		int seq = this.getParameterInt("seq");
		int hide = this.getParameterInt("hide");
		String name = this.getParameterString("name");
		String bak = this.getParameterString("bak");
		if (id > 0 && seq >= 0 && name != null && !"".equals(name)) {
			bean.setId(id);
			bean.setSeq(seq);
			bean.setHide(hide);
			bean.setName(name);
			bean.setGender(getParameterInt("gender"));
			bean.setBak(bak);
			service.insertCatalog(bean);
			setAttribute("tip", "添加成功!");
		} else {
			setAttribute("tip", "数据不全,添加失败!");
		}
	}

	/**
	 * 从一个list里面随机抽取一定数目的元素
	 * 
	 * @param list
	 * @param size
	 * @return
	 */
	public List getRandList(List list, int size) {
		List rand = new ArrayList();
		if (list == null) {
			return rand;
		}
		if (size <= 0)
			return rand;
		int cound = 0;
		int i = RandomUtil.nextInt(list.size());
		while (cound < size) {
			while (rand.contains(list.get(i))) {
				i = RandomUtil.nextInt(list.size());
			}
			rand.add(list.get(i));
			cound++;
		}
		return rand;
	}

	public static String formatDuePrice(float price, int due) {
		if (due == 1)
			return ShopUtil.formatPrice2(price);
		else if (due == 3)
			return ShopUtil.formatPrice2(price * 2);
		else if (due == 6)
			return ShopUtil.formatPrice2(price * 3);
		else if (due == 12)
			return ShopUtil.formatPrice2(price * 5);
		return "?";
	}

	public static byte[] lock = new byte[0];

	// 商品分类全部载入
	public static int partCount;

	public static int catalogCount;

	public static List partList;

	public static HashMap partMap;

	public static List catalogList;

	public static HashMap catalogMap;

	public static void preparePart() {
		if (partList == null) {
			synchronized (lock) {
				if (partList == null) {
					List list = service.getPartList("1 order by level_layer");
					HashMap map = new HashMap(list.size());
					for (int i = 0; i < list.size(); i++) {
						PartBean part = (PartBean) list.get(i);
						part.setIndex(i);
						map.put(new Integer(part.getId()), part);
					}
					partMap = map;
					partList = service.getPartList("1 order by level_show");
					partCount = list.size();
				}
			}
		}
		if (catalogList == null) {
			synchronized (lock) {
				if (catalogList == null) {
					List list = service.getCatalogList("1 order by seq");
					HashMap map = new HashMap(list.size());
					for (int i = 0; i < list.size(); i++) {
						CatalogBean bean = (CatalogBean) list.get(i);
						map.put(new Integer(bean.getId()), bean);
					}
					catalogMap = map;
					catalogList = list;
					catalogCount = list.size();
				}
			}
		}
	}

	public static HashMap getPartMap() {
		preparePart();
		return partMap;
	}

	public static List getPartList() {
		preparePart();
		return partList;
	}

	public static PartBean getPart(int id) {
		return (PartBean) getPartMap().get(new Integer(id));
	}

	public static void flushPart() {
		partList = null;
		catalogList = null;
	}

	// 商品分类catalog
	public static HashMap getCatalogMap() {
		preparePart();
		return catalogMap;
	}

	public static List getCatalogList() {
		preparePart();
		return catalogList;
	}

	public static CatalogBean getCatalog(int id) {
		return (CatalogBean) getCatalogMap().get(new Integer(id));
	}
	
	/**
	 * 赠送组合
	 * @param uids
	 * 用户ID,要以英文逗号分隔
	 * @param ids
	 * 组合的ID,要以英文逗号分隔
	 */
	public void give2(String uids,String ids) {
		if (uids == null || ids == null || "".equals(uids) || "".equals(ids))
			return;
		
		int uid = 0;
		Commodity com = null;
		GroupBean group = null;
		String[] uidArray = uids.split(",");
		String[] groupArray = ids.split(",");
		
		for (int i = 0 ; i < uidArray.length ; i++){
			uid = StringUtil.toInt(uidArray[i]);
			// 送物品
			for (int j = 0 ; j < groupArray.length ; j++){
				group = service.getGroup(" id=" + StringUtil.toInt(groupArray[j]));
				if (group != null){
					String[] tmp = group.getItemIds().split(",");
					for (int g = 0 ; g < tmp.length ; g++){
						com = getCommodity(StringUtil.toInt(tmp[g]));
						if (com != null){
							addGood2(com, uid, group.getDue());// 获得物品
						}
					}
				}
			}
			// 给此用户发通知
			NoticeAction.sendNotice(uid,"管理员送您一套酷秀", "", NoticeBean.GENERAL_NOTICE, "",
					"/kshow/myGoods.jsp");
		}
	}
}
