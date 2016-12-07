package net.joycool.wap.call;

import java.util.*;

import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.spec.castle.CastleMessage;
import net.joycool.wap.spec.castle.CastleService;
import net.joycool.wap.spec.farm.FarmUserBean;
import net.joycool.wap.spec.garden.GardenUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.spec.garden.*;

/**
 * 最新动态，聊天、论坛、圈子等等各种信息的最新
 * 
 * @author bomb
 * 
 */
public class Latest2 {

	static CastleService castleService = CastleService.getInstance();

	public static int CASTLE_LIMIT = 5;

	public static int CASTLE_TYPE = 2;

	public static CastleMessage getCastleMessage(int limit, int type) {
		String key = "castle" + type;
		List list = (List) OsCacheUtil.get(key, "latest", 600);
		if (list == null) {
			synchronized (Latest2.class) {
				list = (List) OsCacheUtil.get(key, "latest", 600);
				if (list == null) {
					list = castleService.getCastleMessageList(" type = " + type
							+ " order by id desc limit " + limit);

					OsCacheUtil.put(key, list, "latest");
				}
			}
		}
		int rnd = RandomUtil.nextInt(limit - 1);
		CastleMessage msg = (CastleMessage) list.get(rnd);
		return msg;
	}

	public static String getCastleString(CallParam callParam) {
		CastleMessage msg = getCastleMessage(CASTLE_LIMIT, CASTLE_TYPE);

		if (msg == null) {
			return "";
		} else {
			return "<a href=\"/cast/report.jsp?id=" + msg.getId() + "\">" + msg.getContent()
					+ "</a>";
		}

	}

	public static int GARDEN_LIMIT = 5;

	public static int GARDEN_TYPE = 1;

	public static GardenMessage getGardenMessage(int limit, int type) {
		String key = "garden" + type;
		List list = (List) OsCacheUtil.get(key, "latest", 600);
		if (list == null) {
			synchronized (Latest2.class) {
				list = (List) OsCacheUtil.get(key, "latest", 600);
				if (list == null) {
					list = GardenUtil.gardenService
							.getMessages(" from_uid = 0  order by id desc limit 5");// ("type
																					// = "
																					// +
																					// type
																					// + "
																					// order
																					// by
																					// id
																					// desc
																					// limit
																					// " +
																					// limit);

					OsCacheUtil.put(key, list, "latest");
				}
			}
		}
		int rnd = RandomUtil.nextInt(limit - 1);
		return (GardenMessage) list.get(rnd);
	}

	public static String getGardenString(CallParam callParam) {
		GardenMessage msg = getGardenMessage(GARDEN_LIMIT, GARDEN_TYPE);
		if (msg == null)
			return "";
		return "<a href=\"garden.jsp?uid=" + msg.getFromUid() + "\">"
				+ UserInfoUtil.getUser(msg.getUid()).getNickNameWml() + "</a>"
				+ StringUtil.toWml(msg.getMessage());
	}

	public static int FARM_ID = 149;

	public static String OPTION = "honor_week";

	public static FarmUserBean getFarmUserBean() {
		// String key = "farm_index_top";

		// String query = "select user_id from farm_user_honor where arena=" +
		// FARM_ID + " and "+OPTION+">0 order by " + OPTION + " desc limit 10";
		return null;
	}

	public static String getFarmTop(CallParam callParam) {

		// SqlUtil.getIntList("select user_id from farm_user_honor where arena="
		// + id + " and "+orders[option]+">0 order by " + orders[option] + "
		// desc limit 10")

		return "";
	}

}
