package jc.family.game.yard;

import java.text.DecimalFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;
import jc.family.FamilyUserBean;
import jc.family.FundDetail;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.cache.StaticCacheMap;
import net.joycool.wap.spec.tiny.TinyGame8;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

public class YardAction extends FamilyAction {

	public static YardService yardService = new YardService();
	public static ICacheMap yardCache = CacheManage.addCache(
			new LinkedCacheMap(500), "familyYard");
	public static ICacheMap itemProtoCache = CacheManage.addCache(
			new StaticCacheMap(50), "yardItemProto");
	public static ICacheMap recipeProtoCache = CacheManage.addCache(
			new StaticCacheMap(50), "yardrecipeProto");

	public static List itemProtoList = null;
	public static List recipeProtoList = null; // 初级食谱列表
	public static List middleRecipeProtoList = null; // 中级食谱列表
	public static List worksRecipeProtoList = null; // 工厂列表

	public static TinyGame8 tinyGame = new TinyGame8(4, 3, 2, 2);
	public static String[] prodStr = { "蔬菜种子", "水果种子", "饲料" };

	public static long PLANT_TIME = 20l * 3600 * 1000; // 种植时间,暂定20小时
	public static int LAND_MAX_RANK = 5; // 土地等级上限

	public static DecimalFormat floatDF1 = new DecimalFormat("0.#");
	public static DecimalFormat floatDF2 = new DecimalFormat("0.##");

	static {
		String[] mark = { "口", "肉", "蛋", "鱼", "豆", "果", "蔬" };
		tinyGame.setMark(mark);
	}

	public static int[][] gift = { // 开放餐厅后赠送一些物品
	{ 16, 50 }, { 17, 30 }, };

	public static TinyGame8 getTinyGame8() {
		return tinyGame;
	}

	public YardAction() {

	}

	public YardAction(HttpServletRequest request) {
		super(request);
	}

	public YardAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * 得到本家族信息
	 * 
	 * @param fmid
	 * @return
	 */
	public static YardBean getYardByID(int fmid) {
		YardBean yard = null;
		Integer key = Integer.valueOf(fmid);
		synchronized (yardCache) {
			yard = (YardBean) yardCache.get(key);
			if (yard == null) {
				yard = yardService.getYardBean("fmid=" + fmid);
				if (yard != null) {
					yard.setItemMap(yardService.getYardItemBeanList("fmid="
							+ fmid));
					yard.setTotalDishCount(SqlUtil
							.getIntResult(
									"select sum(a.number) from fm_yard_fm_item a,fm_yard_item_proto b where a.fmid="
											+ fmid
											+ " and a.itemid=b.id and b.t_type=3",
									5));
					// 初级食谱
					yard.setRecipeSet(yardService
							.getIntListResult("select fr.recipeid from fm_yard_fm_recipe fr,fm_yard_recipe_proto rp where fr.fmid="
									+ fmid
									+ " and  rp.id=fr.recipeid and rp.`type`=0"));
					// 中级食谱
					yard.setMiddleRecipeSet(yardService
							.getIntListResult("select fr.recipeid from fm_yard_fm_recipe fr,fm_yard_recipe_proto rp where fr.fmid="
									+ fmid
									+ " and  rp.id=fr.recipeid and rp.`type`=1"));
					// 工厂资料
					yard.setWorksRecipeSet(yardService
							.getIntListResult("select fr.recipeid from fm_yard_fm_recipe fr,fm_yard_recipe_proto rp where fr.fmid="
									+ fmid
									+ " and  rp.id=fr.recipeid and rp.`type`=5"));
					yard.setCookList(yardService
							.getCookBeanList("fmid=" + fmid));
					yard.setLandList(yardService.getYardLandBeanList("fmid="
							+ fmid + " order by id"));
					yard.setPlantList(yardService.getYardPlantBeanList("fmid="
							+ fmid + " order by id"));
					// yard.setTrendsList(yardService.getFmItmeLogBeanList("fmid="
					// + fmid + " order by id desc"));
					yardCache.put(key, yard);
					// 正在烹饪的总数
					yard.setDeploying(SqlUtil.getIntResult("select count(id) from fm_yard_deploying where fmid=" + fmid, 5));
				}
			}
		}
		return yard;
	}

	/**
	 * 物品原型
	 * 
	 * @param id
	 * @return
	 */
	public static YardItemProtoBean getItmeProto(int id) {
		YardItemProtoBean item = null;
		Integer key = Integer.valueOf(id);
		synchronized (itemProtoCache) {
			item = (YardItemProtoBean) itemProtoCache.get(key);
			if (item == null) {
				item = yardService.getYardItemProtoBean("id=" + id);
				if (item != null) {
					itemProtoCache.put(key, item);
				}
			}
		}
		return item;
	}

	/**
	 * 菜谱原型
	 * 
	 * @param id
	 * @return
	 */
	public static YardRecipeProtoBean getRecipeProto(int id) {
		YardRecipeProtoBean recipe = null;
		Integer key = Integer.valueOf(id);
		synchronized (recipeProtoCache) {
			recipe = (YardRecipeProtoBean) recipeProtoCache.get(key);
			if (recipe == null) {
				recipe = yardService.getYardRecipeProtoBean("id=" + id);
				if (recipe != null) {
					recipeProtoCache.put(key, recipe);
				}
			}
		}
		return recipe;
	}

	public static YardService getYardService() {
		return yardService;
	}

	public static long toFundRate = 100000l; // 餐厅基金兑换到家族基金的兑换率

	/**
	 * 兑换家族基金
	 * 
	 * @param fmid
	 * @param fund
	 * @return
	 */
	public static boolean exchangeFmFund(YardBean yard, int money,
			FamilyUserBean fmUser) {
		FamilyHomeBean familyHomeBean = getFmByID(fmUser.getFm_id());// 更新家族基金缓存
		long fund = money * toFundRate;
		if (familyHomeBean != null) {
			updateFund(familyHomeBean, fund, FundDetail.YARD_TYPE);

			yard.addMoney(-money);
		}

		return false;
	}

	/**
	 * 烹饪时间字符串
	 * 
	 * @param time
	 * @return
	 */
	public String getCookingTimeStr(long time) {
		if (time < 0)
			time = 0;
		time = time / 1000;
		StringBuilder sb = new StringBuilder();
		if (time < 60) {
			sb.append(time);
			sb.append("秒");
		} else {
			long minute = time / 60; // 分
			long second = time % 60; // 秒
			// 数据库默认的时间是从1970年1月1日开始.所以如果分钟大于21000000的话,传入的时间100%是默认值.这道菜等于还没开始做.
			if (minute > 21000000)
				return "0";
			if (minute > 60){
				return "超过1小时";
			}
			sb.append(minute);
			sb.append("分");
			if (second > 0) {
				sb.append(second);
				sb.append("秒");
			}
		}
		return sb.toString();
	}

	/**
	 * 开始烹饪
	 * 
	 * @param yard
	 * @param recipe
	 * @return
	 */
	public boolean cooking(YardBean yard, YardRecipeProtoBean recipe) {
		if (yard == null || recipe == null)
			return false;
		// 减掉相应的源料
		yard.removeMaterial(recipe.getMaterialList());
		// 写入烹饪记录
		service.executeUpdate("insert into fm_yard_cook (fmid,createTime,recipeid) values ("
				+ yard.getFmid() + ",now()," + recipe.getId() + " )");
		return true;
	}

	/**
	 * 开始生产
	 * 
	 * @param yard
	 * @param recipe
	 * @return
	 */
	public boolean working(YardBean yard, YardRecipeProtoBean recipe,
			YardPlantBean plant) {
		if (yard == null || recipe == null || plant == null)
			return false;
		UserBean user = getLoginUser();
		if (user == null)
			return false;
		// 减掉相应的源料
		yard.removeMaterial(recipe.getMaterialList());
		// 写入生产记录
		SqlUtil.executeUpdate(
				"update fm_yard_fm_plant set plant_time = now(),user_id="
						+ user.getId() + ",count=1,plant_now=" + recipe.getId()
						+ " where id=" + plant.getId(), 5);
		// 缓存
		plant.setUserId(user.getId());
		plant.setPlantTime(System.currentTimeMillis());
		plant.setPlantNow(recipe.getId());
		return true;
	}

	/**
	 * 完成烹饪
	 * 
	 * @param cook
	 * @return
	 */
	public String finishCooking(YardCookBean cook, YardRecipeProtoBean recipe,
			YardBean yard) {
		if (cook == null || recipe == null)
			return "";
		// 当前时间 / 完美的时间
		int pre = (int) (System.currentTimeMillis() - cook.getCreateTime())
				/ (recipe.getTime() / 100);
		int[] passTime = { 300, 150, 130, 105, 95, 70, 50, -1 };
		int[] reap = { 0, 1, 3, 6, 10, 6, 3, 1 };
		String[] reapStr = { "长毛的", "难吃的", "不新鲜的", "美味的", "完美的", "良好的", "一般的",
				"糟糕的" };
		StringBuilder sb = new StringBuilder(64);
		int i = 0;
		for (; i < passTime.length; i++) {
			if (pre > passTime[i]) {
				break;
			}
		}
		// 完成烹饪
		sb.append("你烹饪了");
		sb.append(reapStr[i]);
		sb.append("[");
		sb.append(recipe.getName());
		sb.append("]!");
		sb.append("[");
		sb.append(recipe.getName());
		sb.append("]+");
		sb.append(reap[i]);
		sb.append(".");
		// 写动态
		yard.getKitchenLog().add(
				getLoginUser().getNickNameWml() + "烹饪了" + reapStr[i] + "["
						+ recipe.getName() + "]!");

		yard.addDish(recipe.getProductList(), reap[i]);

		// 删除掉记录
		service.executeUpdate("delete from fm_yard_cook where id="
				+ cook.getId());
		if (recipe.getRank() == yard.getRank() && reap[i] == 10) {
			yard.addExp(1);
			sb.append("<br/>餐厅经验值增加了!");
		}
		return sb.toString();
	}

	/**
	 * 完成生产
	 * 
	 * @param cook
	 * @return
	 */
	public String finishWorking(YardPlantBean plant,
			YardRecipeProtoBean recipe, YardBean yard) {
		if (plant == null || recipe == null)
			return "";
		StringBuilder sb = new StringBuilder(64);
		// 完成生产
		sb.append("你生产了[");
		sb.append(recipe.getName());
		sb.append("]![");
		sb.append(recipe.getName());
		sb.append("]+1.");
		// 写动态
		yard.getKitchenLog().add(
				getLoginUser().getNickNameWml() + "生产了[" + recipe.getName()
						+ "]!");

		yard.addMaterial(recipe.getProductList(), 1);

		// 删除掉记录
		SqlUtil.executeUpdate(
				"update fm_yard_fm_plant set plant_time='1970-01-01 00:00:00',user_id=0,plant_now=0 where id="
						+ plant.getId(), 5);
		// 缓存
		plant.setPlantTime(0);
		plant.setPlantNow(0);
		plant.setUserId(0);
		return sb.toString();
	}

	/**
	 * 判断是否可以开业.
	 * 
	 * @param allowWeek
	 *            传入一个表示星期的byte数组,星期日为0,星期一至六为1-6.如果数组长度大于7则直接返回false.
	 * @return boolean
	 */
	public static boolean isOpen(byte[] allowWeek) {
		if (allowWeek == null || allowWeek.length > 7)
			return false;
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		// 周一至周六分别为1-6，周日为0
		int weekNow = c.get(java.util.Calendar.DAY_OF_WEEK) - 1;
		for (int i = 0; i < allowWeek.length; i++) {
			if (allowWeek[i] == weekNow)
				return true;
		}
		return false;
	}

	/**
	 * 加种子记录
	 * 
	 * @param fmId
	 * @param count
	 */
	public void addSeedLog(int fmId, int count) {
		int loginUid = getLoginUser().getId();
		YardUserBean userBean = yardService.getYardUserBean(" fmid=" + fmId
				+ " and userid=" + loginUid);
		if (userBean == null) {
			service.executeUpdate("insert into fm_yard_user_log (userid,fmid,seed_count) values ("
					+ loginUid + "," + fmId + "," + count + " )");
		} else {
			service.executeUpdate("update fm_yard_user_log set seed_count=seed_count+"
					+ count + " where id=" + userBean.getId());
		}
	}

	/**
	 * 开通家族餐厅
	 * 
	 * @param fmid
	 */
	public int createEatery(FamilyHomeBean fmHome, String name) {
		service.executeUpdate("insert into fm_yard_info(fmid,money,createtime,name)values("
				+ fmHome.getId() + ",0,now(),'" + StringUtil.toSql(name) + "')");
		YardBean yard = getYardByID(fmHome.getId());
		if (yard == null) {
			return 0;
		}
		for (int i = 0; i < gift.length; i++) {
			yard.addMaterial(gift[i][0], gift[i][1]);
		}

		UserBean userBean = getLoginUser();
		fmHome.setMoney(fmHome.getMoney() - createMoney);
		service.updateFmFund(fmHome.getId(), -createMoney);
		service.insertFundHistory(fmHome.getId(), userBean.getId(),
				userBean.getNickName(), "家族餐厅开张,扣除" + createMoney, 1);
		service.insertFmFundDetail(fmHome.getId(), -createMoney,
				FundDetail.YARD_TYPE, fmHome.getMoney());
		fmHome.setFlag(fmHome.getFlag() | FamilyHomeBean.EATERY);
		service.updateFmHome("flag=" + fmHome.getFlag(), fmHome.getId());
		yardService.addLand(4, yard); // 初始给四块地
		return 1;
	}

	public static List getItemProtoList() {
		if (itemProtoList == null) {
			itemProtoList = yardService.getYardItemProtoBeanList("1");
			seedList = yardService
					.getYardItemProtoBeanList("t_type=0 order by rank desc,id");
		}
		return itemProtoList;
	}

	public static List seedList = null;

	public static List getSeedList() {
		getItemProtoList();
		return seedList;
	}

	/**
	 * 初级菜谱列表
	 * 
	 * @return
	 */
	public static List getRecipeProtoList() {
		if (recipeProtoList == null) {
			recipeProtoList = yardService.getYardRecipeProtoBeanList(" type=0");
		}
		return recipeProtoList;
	}

	/**
	 * 中级食谱列表
	 * 
	 * @return
	 */
	public static List getMiddleRecipeProtoList() {
		if (middleRecipeProtoList == null) {
			middleRecipeProtoList = yardService
					.getYardRecipeProtoBeanList(" type=1");
		}
		return middleRecipeProtoList;
	}

	/**
	 * 工厂列表
	 * 
	 * @return
	 */
	public static List getWorksRecipeProtoList() {
		if (worksRecipeProtoList == null) {
			worksRecipeProtoList = yardService
					.getYardRecipeProtoBeanList(" type=5");
		}
		return worksRecipeProtoList;
	}

	// 生成物品列表工具
	public static String getItemListString(List items) {
		StringBuilder sb = new StringBuilder(32);
		for (int i = 0; i < items.size(); i++) {
			if (i != 0)
				sb.append(",");
			Object obj = items.get(i);
			if (obj instanceof Integer) {
				YardItemProtoBean item = getItmeProto(((Integer) obj)
						.intValue());
				if (item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
			} else {
				int[] tmp = (int[]) obj;
				YardItemProtoBean item = getItmeProto(tmp[0]);
				if (item == null)
					sb.append("(未知)");
				else
					sb.append(item.getName());
				sb.append("x");
				sb.append(tmp[1]);
			}
		}
		return sb.toString();
	}

	/**
	 * 调价
	 */
	public static void adjustPrice() {
		List list = getItemProtoList();
		for (int i = 0; i < list.size(); i++) {
			YardItemProtoBean bean = (YardItemProtoBean) list.get(i);
			if (bean.getType() == 3) {
				bean.setPrice(bean.getBasePrice() * RandomUtil.nextInt(50, 120)
						/ 100);
				itemProtoCache.put(Integer.valueOf(bean.getId()), bean);
				yardService.upd("update fm_yard_item_proto set price="
						+ bean.getPrice() + " where id=" + bean.getId());
			}
		}
	}

	public static String floatFormat1(float f) {
		return floatDF1.format(f);
	}

	public static String floatFormat2(float f) {
		return floatDF2.format(f);
	}

	// 传入price或者整数，除以10后格式化
	public static String moneyFormat(long n) {
		return floatDF1.format((float) n / 10);
	}
}
