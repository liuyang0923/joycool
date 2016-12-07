package jc.family;

import java.sql.SQLException;
import net.joycool.wap.util.db.DbOperation;

public class FamilyUtil {

	public static long[] statComposeTime = { 0, 0, 0, 0 };// 启动时间

	public static long statComposeInterval = 30 * 24 * 60 * 60 * 1000L;// 间隔时间

	public static String[] table = { "fm_list_com", "fm_list_belle", "fm_list_moods", "fm_list_focus" };

	public static String[] tablesql = { "(select id,fm_name from fm_home order by fm_member_num desc)",// 综合排行榜
			"(select id,fm_name from fm_home order by fm_member_num desc)",// 美女排行榜
			"(select id,fm_name from fm_home order by fm_member_num desc)",// 人气排行榜
			"(select id,fm_name from fm_home order by money desc)" };// 财富排行榜

	/**
	 * 更新排行榜
	 * 
	 * @param key
	 *            0 fm_list_com 1 fm_list_belle 2 fm_list_moods 3 fm_list_focus
	 */
	public static void statCompose(int key) {
		long now = System.currentTimeMillis();
		if (now < statComposeTime[key])
			return;
		synchronized (statComposeTime) {
			if (now < statComposeTime[key])
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table " + table[key]);
			db.executeUpdate("insert into " + table[key] + " (fm_id,fm_name) " + tablesql[key]);
			deleteExcessivenessData(db);
			db.release();
			statComposeTime[key] = now + statComposeInterval;
		}
	}

	/**
	 * 删除冗余的数据
	 * 
	 * @param db
	 */
	private static void deleteExcessivenessData(DbOperation db) {
		try {
			int maxid = db.getIntResult("select id from fm_movement order by id desc limit 1") - 50;
			if (maxid > 0) {
				db.executeUpdate("delete from fm_movement where id<" + maxid);
			}
		} catch (SQLException e) {
		}
	}
}
