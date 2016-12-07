package net.joycool.wap.spec.ssc;

import java.util.HashSet;
import java.util.Set;

import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.DateUtil;

public class LhcWorld {

	// 大小
	public static int DA_XIAO = 1;

	// 六肖
	public static int LIU_XIAO = 2;

	// 家禽野兽
	public static int JIA_QIN_YE_SHOU = 3;

	// 前后
	public static int QIAN_HOU = 4;

	// 波色
	public static int BO_SE = 5;

	// 五行
	public static int WU_XING = 6;

	// 单双
	public static int DAN_SHUANG = 7;

	// 平码
	public static int PING_MA = 8;

	// 特码
	public static int TE_MA = 9;

	// 特别特码(中49特码)
	public static int TE_TE_MA = 10;

	// 9种玩法赔率数值数组
	public static double LHC_RATE[] = { 1.0, 1.6, 10.0, 1.6, 1.6, 2.5,
			4.0, 1.6, 7.0, 40.0, 45.0 };

	// 生肖对应数组id
	public static int LHC_SHENG_XIAO_ARRAY[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11 };

	// 生肖对应数组名称
	public static String LHC_SHENG_XIAO_NAME[] = { "猪", "狗", "鸡", "猴", "羊",
			"马", "蛇", "龙", "兔", "虎", "牛", "鼠" };

	// 家禽野兽
	// 家禽野兽对应数组id
	public static int JQYS_ARRAY[] = { 0, 1 };

	// 家禽： 牛、马、羊、鸡、狗、猪
	public static int[] JQYS_JIA_QIN = { 10, 5, 4, 2, 1, 0 };

	// 野兽: 鼠、虎、兔、龙、蛇、猴
	public static int[] JQYS_YE_SHOU = { 11, 9, 8, 7, 6, 3 };

	// 前后
	// 前后对应数组id
	public static int QIANHOU_ARRAY[] = { 0, 1 };

	// 前： 鼠 牛 虎 兔 龙 蛇
	public static int[] QIANHOU_QIAN = { 11, 10, 9, 8, 7, 6 };

	// 后: 马 羊 猴 鸡 狗 猪
	public static int[] QIANHOU_HOU = { 5, 4, 3, 2, 1, 0 };

	// 12生肖
	// 猪
	public static int SHENG_XIAO_ZHU[] = { 1, 13, 25, 37 };

	// 狗
	public static int SHENG_XIAO_GOU[] = { 2, 14, 26, 38 };

	// 鸡
	public static int SHENG_XIAO_JI[] = { 3, 15, 27, 39 };

	// 猴
	public static int SHENG_XIAO_HOU[] = { 4, 16, 28, 40 };

	// 羊
	public static int SHENG_XIAO_YANG[] = { 5, 17, 29, 41 };

	// 马
	public static int SHENG_XIAO_MA[] = { 6, 18, 30, 42 };

	// 蛇
	public static int SHENG_XIAO_SHE[] = { 7, 19, 31, 43 };

	// 龙
	public static int SHENG_XIAO_LONG[] = { 8, 20, 32, 44 };

	// 兔
	public static int SHENG_XIAO_TU[] = { 9, 21, 33, 45 };

	// 虎
	public static int SHENG_XIAO_HU[] = { 10, 22, 34, 46 };

	// 牛
	public static int SHENG_XIAO_NIU[] = { 11, 23, 35, 47, 49 };

	// 鼠
	public static int SHENG_XIAO_SHU[] = { 12, 24, 36, 48 };

	// 波色
	// 波色对应数组id
	public static int LHC_BO_SE_ARRAY[] = { 0, 1, 2 };

	// 波色对应数组名称
	public static String LHC_BO_SE_NAME[] = { "红波", "蓝波", "绿波" };

	// 红波
	public static int BO_SE_HONG[] = { 1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29,
			30, 34, 35, 40, 45, 46 };

	// 蓝波
	public static int BO_SE_LAN[] = { 3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36,
			37, 41, 42, 47, 48 };

	// 绿波
	public static int BO_SE_LV[] = { 5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33,
			38, 39, 43, 44, 49 };

	// 五行
	// 五行对应数组id
	public static int LHC_WU_XING_ARRAY[] = { 0, 1, 2, 3, 4 };

	// 五行对应数组名称
	public static String LHC_WU_XING_NAME[] = { "金", "木", "水", "火", "土" };

	// 金
	public static int[] WU_XING_JIN = { 7, 8, 15, 16, 23, 24, 26, 37, 45, 46,
			49 };

	// 木
	public static int[] WU_XING_MU = { 5, 6, 19, 20, 27, 28, 35, 36, 38 };

	// 水
	public static int[] WU_XING_SHUI = { 3, 4, 11, 12, 14, 25, 33, 34, 41, 42 };

	// 火
	public static int[] WU_XING_HUO = { 2, 12, 21, 22, 29, 30, 43, 44 };

	// 土
	public static int[] WU_XING_TU = { 1, 9, 10, 17, 18, 31, 32, 39, 40, 47, 48 };

	// 类型
	// 1.大小 2.六肖 3.家禽野兽 4.前后 5.波色 6.五行 7.单双 8.平码 9.特码
	public static int[][] LHC_RESULT_ARRAY = { { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 1, 0, 4, 1 }, { 2, 0, 1, 0, 1, 0, 3, 0 },
			{ 3, 0, 2, 0, 1, 1, 2, 1 }, { 4, 0, 3, 1, 1, 1, 2, 0 },
			{ 5, 0, 4, 0, 1, 2, 1, 1 }, { 6, 0, 5, 0, 1, 2, 1, 0 },
			{ 7, 0, 6, 1, 0, 0, 0, 1 }, { 8, 0, 7, 1, 0, 0, 0, 0 },
			{ 9, 0, 8, 1, 0, 1, 4, 1 }, { 10, 0, 9, 1, 0, 1, 4, 0 },
			{ 11, 0, 10, 0, 0, 2, 2, 1 }, { 12, 0, 11, 1, 0, 0, 2, 0 },
			{ 13, 0, 0, 0, 1, 0, 3, 1 }, { 14, 0, 1, 0, 1, 1, 2, 0 },
			{ 15, 0, 2, 0, 1, 1, 0, 1 }, { 16, 0, 3, 1, 1, 2, 0, 0 },
			{ 17, 0, 4, 0, 1, 2, 4, 1 }, { 18, 0, 5, 0, 1, 0, 4, 0 },
			{ 19, 0, 6, 1, 0, 0, 1, 1 }, { 20, 0, 7, 1, 0, 1, 1, 0 },
			{ 21, 0, 8, 1, 0, 2, 3, 1 }, { 22, 0, 9, 1, 0, 2, 3, 0 },
			{ 23, 0, 10, 0, 0, 0, 0, 1 }, { 24, 0, 11, 1, 0, 0, 0, 0 },
			{ 25, 1, 0, 0, 1, 1, 2, 1 }, { 26, 1, 1, 0, 1, 1, 0, 0 },
			{ 27, 1, 2, 0, 1, 2, 1, 1 }, { 28, 1, 3, 1, 1, 2, 1, 0 },
			{ 29, 1, 4, 0, 1, 0, 3, 1 }, { 30, 1, 5, 0, 1, 0, 3, 0 },
			{ 31, 1, 6, 1, 0, 1, 4, 1 }, { 32, 1, 7, 1, 0, 2, 4, 0 },
			{ 33, 1, 8, 1, 0, 2, 2, 1 }, { 34, 1, 9, 1, 0, 0, 2, 0 },
			{ 35, 1, 10, 0, 0, 0, 1, 1 }, { 36, 1, 11, 1, 0, 1, 1, 0 },
			{ 37, 1, 0, 0, 1, 1, 0, 1 }, { 38, 1, 1, 0, 1, 2, 1, 0 },
			{ 39, 1, 2, 0, 1, 2, 4, 1 }, { 40, 1, 3, 1, 1, 0, 4, 0 },
			{ 41, 1, 4, 0, 1, 1, 2, 1 }, { 42, 1, 5, 0, 1, 1, 2, 0 },
			{ 43, 1, 6, 1, 0, 2, 3, 1 }, { 44, 1, 7, 1, 0, 2, 3, 0 },
			{ 45, 1, 8, 1, 0, 0, 0, 1 }, { 46, 1, 9, 1, 0, 0, 0, 0 },
			{ 47, 1, 10, 0, 0, 1, 4, 1 }, { 48, 1, 11, 1, 0, 1, 4, 0 },
			{ 49, 1, 10, 0, 1, 2, 0, 1 } };

	// 类型对应名称
	public static String[][] LHC_NAME_ARRAY = { { "" }, { "小", "大" },
			{ "猪", "狗", "鸡", "猴", "羊", "马", "蛇", "龙", "兔", "虎", "牛", "鼠" },
			{ "家禽", "野兽" }, { "前", "后" }, { "红波", "蓝波", "绿波" },
			{ "金", "木", "水", "火", "土" }, { "双", "单" }, { "平码" }, { "特码" }, };

	// 获取所有PK系统在线用户信息
	static byte[] lock = new byte[0];

	static byte[] kaiLock = new byte[0];

	/**
	 * 
	 * @author macq (修改：maning)
	 * @explain：每期中奖答案
	 * @datetime:2007-8-27 15:02:39
	 * @param number
	 * @return
	 * @return HashSet
	 */
	public static Set getLhcSet(int lhcId) {
		String key = String.valueOf(lhcId);
		Set lhcSet = (Set) OsCacheUtil.get(key,
				OsCacheUtil.LHC_RESULT_SET_CACHE_GROUP,
				OsCacheUtil.LHC_RESULT_SET_FLUSH_PERIOD);
		if (lhcSet == null) {
			synchronized (lock) {
				lhcSet = (Set) OsCacheUtil.get(key,
						OsCacheUtil.LHC_RESULT_SET_CACHE_GROUP,
						OsCacheUtil.LHC_RESULT_SET_FLUSH_PERIOD);
				if (lhcSet != null) {
					return lhcSet;
				}
				LhcResultBean lhcResult = new LhcService().getLhcResult("id="
						+ lhcId);
				if (lhcResult == null) {
					return null;
				}
				int number = lhcResult.getNum7();
				if (number < 1 || number > 49) {
					return null;
				}
				lhcSet = new HashSet();
				for (int i = 1; i <= 7; i++) {
					String setKey = String.valueOf(i) + "-"
							+ String.valueOf(LHC_RESULT_ARRAY[number][i]);
					lhcSet.add(setKey);
				}
				OsCacheUtil.put(key, lhcSet,
						OsCacheUtil.LHC_RESULT_SET_CACHE_GROUP);
			}
		}
		return lhcSet;
	}

	public static LhcResultBean bean = null;

	public static LhcService service = new LhcService();
	public static LhcService getLhcService() {
		return service;
	}

	public static LhcResultBean getSSCBean() {
		if (bean != null)
			return bean;
		else {
			bean = service.getLhcResult(" 1 order by id desc limit 1");
		}
		return bean;
	}

	/**
	 * 
	 * @author macq (修改：maning)
	 * @explain：每天开奖算法
	 * @datetime:2007-8-24 13:57:31
	 * @return void
	 */

	// public static int INTEVER_MIN = 2;
	public static long TOTAL_START = DateUtil.parseDate("2009-09-14").getTime();

	static int termMinute = 15;
	static long termInterval = termMinute * 60000;
	static long daystart = 9 * 60 * 60000;
	public static int dayTerm = 60;

	// public static int dayTermId = 100; // 一天一百个id
	// 根据当前时间判断是当天第几期，如果没有开返回0
	public static int getTerm(long now) {
		long x = now - TOTAL_START;
		int x2 = (int) ((x % DateUtil.MS_IN_DAY - daystart) / termInterval);
		if (x2 < 0)
			return 0;
		if (x2 > dayTerm)
			x2 = dayTerm;
		return x2 + 1;
	}

	public static int getTerm() {
		return getTerm(System.currentTimeMillis());
	}
	// 下一期开奖剩余时间/s，如果已经全部开奖完毕或者第一期的时间没到则返回-1
	public static int getNextTimeLeft() {
		long now = System.currentTimeMillis();

		int next = getTerm(now);
		if(next == 0 || next > dayTerm)
			return -1;
		now = (now - TOTAL_START) % DateUtil.MS_IN_DAY;
		// 下一次要开奖的时间
		long end = daystart + termInterval * next;
		// System.out.println(daystart);
		// System.out.println("间隔时间:" + (end-(now-daystart))/60000);
		return (int)((end - now) / 1000);
	}

	public static void task() {

		bean = getSSCBean();

		long date = 0;
		int term = 1;
		int id = 0;
		if (bean != null) {
			date = bean.getCreateDatetime().getTime();
			term = bean.getTerm();
			id = bean.getId();
		}

		if (date == 0) {
			date = TOTAL_START;
			term = 0;
		}

		// long notermInterval = (1440 - termMinute * dayTerm) * 60000;

		long now = System.currentTimeMillis();

		long cur = date;
		//	循环开奖，把所有该开没开的都开奖
		while (true) {
			term++;
			id++;
			if (term > dayTerm) { // 过了一天
				term = 1;
				//System.out.println("已经过了一天了，不可再买");
				date += 24 * 3600 * 1000;
			}
			cur = date + term * termInterval + daystart;
			if (cur > now){
				//cur是？？
				break;
			}
			bean = service.doOpen(date, term, id);
		}
		return;
	}

}