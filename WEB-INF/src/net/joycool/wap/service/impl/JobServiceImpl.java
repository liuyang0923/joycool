package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import net.joycool.wap.bean.job.AngerBean;
import net.joycool.wap.bean.job.AngerCardBean;
import net.joycool.wap.bean.job.AngerExpressionBean;
import net.joycool.wap.bean.job.CardBean;
import net.joycool.wap.bean.job.CardTypeBean;
import net.joycool.wap.bean.job.HandbookingerBean;
import net.joycool.wap.bean.job.HandbookingerRecordBean;
import net.joycool.wap.bean.job.HappyCardBean;
import net.joycool.wap.bean.job.HappyCardCategoryBean;
import net.joycool.wap.bean.job.HappyCardSendBean;
import net.joycool.wap.bean.job.HappyCardStatBean;
import net.joycool.wap.bean.job.HappyCardTypeBean;
import net.joycool.wap.bean.job.HuntQuarryAppearRateBean;
import net.joycool.wap.bean.job.HuntQuarryBean;
import net.joycool.wap.bean.job.HuntTaskBean;
import net.joycool.wap.bean.job.HuntUserQuarryBean;
import net.joycool.wap.bean.job.HuntUserWeaponBean;
import net.joycool.wap.bean.job.HuntWeaponBean;
import net.joycool.wap.bean.job.JCLotteryGuessBean;
import net.joycool.wap.bean.job.JCLotteryHistoryBean;
import net.joycool.wap.bean.job.JCLotteryNumberBean;
import net.joycool.wap.bean.job.JobMusicBean;
import net.joycool.wap.bean.job.JobWareHouseBean;
import net.joycool.wap.bean.job.LuckBean;
import net.joycool.wap.bean.job.PsychologyAnswerBean;
import net.joycool.wap.bean.job.PsychologyBean;
import net.joycool.wap.bean.job.SpiritBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.util.db.DbOperation;

public class JobServiceImpl implements IJobService {
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#addLuck(net.joycool.wap.bean.job.LuckBean)
	 */
	public boolean addLuck(LuckBean luck) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#deleteLuck(java.lang.String)
	 */
	public boolean deleteLuck(String condition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getLuck(java.lang.String)
	 */
	public LuckBean getLuck(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getLuckBeanCount(java.lang.String)
	 */
	public int getLuckBeanCount(String condition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getLuckBeanMap(java.lang.String)
	 */
	public HashMap getLuckBeanMap(String condition) {
		// TODO Auto-generated method stub
		HashMap luckMap = new HashMap();
		LuckBean luck = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_luck  ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			int i = 0;
			while (rs.next()) {
				luck = new LuckBean();
				luck.setId(rs.getInt("id"));
				luck.setDay(rs.getInt("day"));
				luck.setConstellation(rs.getString("constellation"));
				luck.setSynthesis(rs.getString("synthesis"));
				luck.setLove(rs.getString("love"));
				luck.setJob(rs.getString("job"));
				luck.setFinace(rs.getString("finace"));
				luck.setHealth(rs.getString("health"));
				luck.setColor(rs.getString("color"));
				luck.setNum(rs.getInt("num"));
				luck.setMate(rs.getString("mate"));
				luck.setAppraise(rs.getString("appraise"));
				luckMap.put((++i) + "", luck);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return luckMap;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#updateLuck(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateLuck(String set, String condition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#addHuntTask(net.joycool.wap.bean.job.HuntTaskBean)
	 */
	public boolean addHuntTask(HuntTaskBean huntTask) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_hunt_timer_task(start_time, end_time, quarry_name, price, harm_price, hit_point, image, arrow, hand_gun, hunt_gun, ak47,awp, notice, create_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, huntTask.getStartTime());
			pstmt.setString(2, huntTask.getEndTime());
			pstmt.setString(3, huntTask.getQuarryName());
			pstmt.setInt(4, huntTask.getPrice());
			pstmt.setInt(5, huntTask.getHarmPrice());
			pstmt.setInt(6, huntTask.getHitPoint());
			pstmt.setString(7, huntTask.getImage());
			pstmt.setInt(8, huntTask.getArrow());
			pstmt.setInt(9, huntTask.getHandGun());
			pstmt.setInt(10, huntTask.getHuntGun());
			pstmt.setInt(11, huntTask.getAk47());
			pstmt.setInt(12, huntTask.getAwp());
			pstmt.setString(13, huntTask.getNotice());
			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#deleteHuntTask(java.lang.String)
	 */
	public boolean deleteHuntTask(String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_hunt_timer_task WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntTask(java.lang.String)
	 */
	public HuntTaskBean getHuntTask(String condition) {

		HuntTaskBean task = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, start_time, end_time, quarry_name, price, harm_price, hit_point, image, arrow, hand_gun, hunt_gun, ak47, awp,notice, create_time  FROM jc_hunt_timer_task ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				task = new HuntTaskBean();
				task.setId(rs.getInt("id"));
				task.setStartTime(rs.getString("start_time"));
				task.setEndTime(rs.getString("end_time"));
				task.setQuarryName(rs.getString("quarry_name"));
				task.setPrice(rs.getInt("price"));
				task.setHarmPrice(rs.getInt("harm_price"));
				task.setHitPoint(rs.getInt("hit_point"));
				task.setImage(rs.getString("image"));
				task.setArrow(rs.getInt("arrow"));
				task.setHandGun(rs.getInt("hand_gun"));
				task.setHuntGun(rs.getInt("hunt_gun"));
				task.setAk47(rs.getInt("ak47"));
				task.setAwp(rs.getInt("awp"));
				task.setNotice(rs.getString("notice"));
				task.setCreateTime(rs.getString("create_time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntTaskCount(java.lang.String)
	 */
	public int getHuntTaskCount(String condition) {

		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_hunt_timer_task";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntTaskList(java.lang.String)
	 */
	public Vector getHuntTaskList(String condition) {

		Vector taskList = new Vector();

		HuntTaskBean task = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, start_time, end_time, quarry_name, price, harm_price, hit_point, image, arrow, hand_gun, hunt_gun, ak47,awp, notice, create_time  FROM jc_hunt_timer_task ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				task = new HuntTaskBean();
				task.setId(rs.getInt("id"));
				task.setStartTime(rs.getString("start_time"));
				task.setEndTime(rs.getString("end_time"));
				task.setQuarryName(rs.getString("quarry_name"));
				task.setPrice(rs.getInt("price"));
				task.setHarmPrice(rs.getInt("harm_price"));
				task.setHitPoint(rs.getInt("hit_point"));
				task.setImage(rs.getString("image"));
				task.setArrow(rs.getInt("arrow"));
				task.setHandGun(rs.getInt("hand_gun"));
				task.setHuntGun(rs.getInt("hunt_gun"));
				task.setAk47(rs.getInt("ak47"));
				task.setAwp(rs.getInt("awp"));
				task.setNotice(rs.getString("notice"));
				task.setCreateTime(rs.getString("create_time"));
				taskList.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return taskList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#updateHuntTask(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateHuntTask(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_hunt_timer_task SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// 通过条件更新登陆用户在用户状态表中的记录
	public boolean updateUserStatus(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE user_status SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// 获取所有系统题库中的题目
	public JobWareHouseBean getJobWareHouse() {
		JobWareHouseBean wareHouse = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select * from job_warehouse order by rand() limit 0, 1";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				wareHouse = new JobWareHouseBean();
				wareHouse.setId(rs.getInt("id"));
				wareHouse.setName(rs.getString("name"));
				wareHouse.setKey1(rs.getString("key1"));
				wareHouse.setKey2(rs.getString("key2"));
				wareHouse.setKey3(rs.getString("key3"));
				wareHouse.setKey4(rs.getString("key4"));
				wareHouse.setKey5(rs.getString("key5"));
				wareHouse.setResult(rs.getInt("result"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return wareHouse;
	}

	// 通过条件获取系统题库中指定的题目(为取得答案使用)
	public JobWareHouseBean getJobWareHouse(String condition) {
		JobWareHouseBean wareHouse = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM job_warehouse";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				wareHouse = new JobWareHouseBean();
				wareHouse.setId(rs.getInt("id"));
				wareHouse.setName(rs.getString("name"));
				wareHouse.setKey1(rs.getString("key1"));
				wareHouse.setKey2(rs.getString("key2"));
				wareHouse.setKey3(rs.getString("key3"));
				wareHouse.setKey4(rs.getString("key4"));
				wareHouse.setKey5(rs.getString("key5"));
				wareHouse.setResult(rs.getInt("result"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return wareHouse;
	}

	public JobMusicBean getJobMusicRand(String condition) {
		JobMusicBean music = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select * from job_music ";
		if (condition != null) {
			query = query + " WHERE " + condition
					+ " order by rand() limit 0, 1";
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				music = new JobMusicBean();
				music.setId(rs.getInt("id"));
				music.setName(rs.getString("name"));
				music.setKey1(rs.getString("key1"));
				music.setKey2(rs.getString("key2"));
				music.setKey3(rs.getString("key3"));
				music.setKey4(rs.getString("key4"));
				music.setKey5(rs.getString("key5"));
				music.setResult(rs.getInt("result"));
				music.setTypes(rs.getString("types"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return music;
	}

	public JobMusicBean getJobMusic(String condition) {
		JobMusicBean music = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM job_music";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				music = new JobMusicBean();
				music.setId(rs.getInt("id"));
				music.setName(rs.getString("name"));
				music.setKey1(rs.getString("key1"));
				music.setKey2(rs.getString("key2"));
				music.setKey3(rs.getString("key3"));
				music.setKey4(rs.getString("key4"));
				music.setKey5(rs.getString("key5"));
				music.setResult(rs.getInt("result"));
				music.setTypes(rs.getString("types"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return music;
	}

	// 通过条件增加一条用户下注信息
	public boolean addJCLotteryGuess(JCLotteryGuessBean jcLotteryGuess) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_lottery_guess set guess_id=?, user_id=?,  number=?, wager=?, guess_datetime=now()";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, jcLotteryGuess.getGuessId());
			pstmt.setInt(2, jcLotteryGuess.getUserId());
			pstmt.setInt(3, jcLotteryGuess.getNumber());
			pstmt.setLong(4, jcLotteryGuess.getWager());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	// 通过条件增加开奖号码信息
	public boolean addJCLotteryNumber(JCLotteryNumberBean jcLotteryNumber) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_lottery_number set  guess_id=?, number=?, left_wager=?, lottery_date=now()";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, jcLotteryNumber.getGuessId());
			pstmt.setInt(2, jcLotteryNumber.getNumber());
			pstmt.setLong(3, jcLotteryNumber.getLeftWager());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	// 通过条件增加历史开奖记录
	public boolean addJCLotteryHistory(JCLotteryHistoryBean jcLotteryHistory) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_lottery_history set guess_id=?, guess_number=?, user_id=?, wager=?, lottery_date=now()";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, jcLotteryHistory.getGuessId());
			pstmt.setInt(2, jcLotteryHistory.getGuessNumber());
			pstmt.setInt(3, jcLotteryHistory.getUserId());
			pstmt.setLong(4, jcLotteryHistory.getWager());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	// 通过条件取得用户下注次数
	public int getJCLotteryGuessCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select count(user_id) as id from jc_lottery_guess";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				count = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return count;
	}

	// 取得最大的jc_lottery_number中ID字段值
	public int getMaxJCLotteryNumber() {
		int number = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT max(guess_id) as id FROM jc_lottery_number";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				number = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return number;
	}

	// 通过条件取得jc_lottery_number中上期遗留奖金
	public long getJCLotteryNumberCount(String condition) {
		long count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select left_wager as swager from jc_lottery_number";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				count = rs.getLong("swager");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return count;
	}

	// 通过条件获得当前下注金额
	public long getSumWager(String condition) {
		long count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select sum(wager) as swager from jc_lottery_guess";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				count = rs.getLong("swager");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return count;
	}

	// 通过条件获得中奖号码
	public JCLotteryNumberBean getJCLotteryNumber(String condition) {
		JCLotteryNumberBean lotterNumber = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_lottery_number";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				lotterNumber = new JCLotteryNumberBean();
				lotterNumber.setId(rs.getInt("id"));
				lotterNumber.setGuessId(rs.getInt("guess_id"));
				lotterNumber.setNumber(rs.getInt("number"));
				lotterNumber.setLeftWager(rs.getLong("left_wager"));
				lotterNumber.setLotteryDate(rs.getString("lottery_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return lotterNumber;
	}

	// 通过条件获得用户下注信息
	public Vector getJCLotteryGuessList(String condition) {
		Vector lotteryGuessList = new Vector();
		JCLotteryGuessBean lotterGuess = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_lottery_guess";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				lotterGuess = new JCLotteryGuessBean();
				lotterGuess.setId(rs.getInt("id"));
				lotterGuess.setGuessId(rs.getInt("guess_id"));
				lotterGuess.setUserId(rs.getInt("user_id"));
				lotterGuess.setNumber(rs.getInt("number"));
				lotterGuess.setWager(rs.getLong("wager"));
				lotterGuess.setGuessDatetime(rs.getString("guess_datetime"));
				lotteryGuessList.add(lotterGuess);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return lotteryGuessList;
	}

	// 通过条件获得中奖历史记录
	public Vector getJCLotteryHistoryList(String condition) {
		Vector lotteryHistoryList = new Vector();
		JCLotteryHistoryBean jcLotteryhistory = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_lottery_history";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				jcLotteryhistory = new JCLotteryHistoryBean();
				jcLotteryhistory.setId(rs.getInt("id"));
				jcLotteryhistory.setGuessId(rs.getInt("guess_id"));
				jcLotteryhistory.setGuessNumber(rs.getInt("guess_number"));
				jcLotteryhistory.setUserId(rs.getInt("user_id"));
				jcLotteryhistory.setWager(rs.getLong("wager"));
				jcLotteryhistory.setLotteryDate(rs.getString("lottery_date"));
				lotteryHistoryList.add(jcLotteryhistory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return lotteryHistoryList;
	}

	// 通过条件获得用户下注信息
	public JCLotteryGuessBean getJCLotteryGuess(String condition) {
		JCLotteryGuessBean jcLotteryGuess = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_lottery_guess";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				jcLotteryGuess = new JCLotteryGuessBean();
				jcLotteryGuess.setId(rs.getInt("id"));
				jcLotteryGuess.setGuessId(rs.getInt("guess_id"));
				jcLotteryGuess.setUserId(rs.getInt("user_id"));
				jcLotteryGuess.setNumber(rs.getInt("number"));
				jcLotteryGuess.setWager(rs.getLong("wager"));
				jcLotteryGuess.setGuessDatetime(rs.getString("guess_datetime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return jcLotteryGuess;
	}

	// 通过条件获得用户下注信息
	public boolean updateJCLotteryGuess(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_lottery_guess SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// 通过条件更新上期遗留奖金
	public boolean updateJCLotteryNumber(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_lottery_number SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// zhul_2006-07-11_新增机会卡游戏,以下为实现接口的类_start
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#addCardType(net.joycool.wap.bean.job.card.CardTypeBean)
	 */
	public boolean addCardType(CardTypeBean cardType) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#getCardType(java.lang.String)
	 */
	public CardTypeBean getCardType(String condition) {
		// TODO Auto-generated method stub
		CardTypeBean cardType = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_card_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				cardType = new CardTypeBean();
				cardType.setId(rs.getInt("id"));
				cardType.setName(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return cardType;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#getCardTypeList(java.lang.String)
	 */
	public Vector getCardTypeList(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#deleteCardType(java.lang.String)
	 */
	public boolean deleteCardType(String condition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#updateCardType(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateCardType(String set, String condition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#getCardTypeCount(java.lang.String)
	 */
	public int getCardTypeCount(String condition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#addCard(net.joycool.wap.bean.job.card.CardBean)
	 */
	public boolean addCard(CardBean card) {
		// TODO Auto-generated method stub
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		
		String query = "INSERT INTO jc_card(type_id,appear_rate,value_type,action_value,action_field,action_direction,description" +
				") values(?,?,?,?,?,?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1,card.getTypeId());
			pstmt.setInt(2,card.getAppearRate());
			pstmt.setInt(3,card.getValueType());
			pstmt.setInt(4,card.getActionValue());
			pstmt.setInt(5,card.getActionfield());
			pstmt.setInt(6,card.getActionDirection());
			pstmt.setString(7,card.getDescription());
			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#getCard(java.lang.String)
	 */
	public CardBean getCard(String condition) {
		// TODO Auto-generated method stub
		CardBean card = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT *,jc_card_type.name FROM jc_card inner join jc_card_type on jc_card.type_id=jc_card_type.id ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				card = new CardBean();
				card.setId(rs.getInt("jc_card.id"));
				card.setTypeId(rs.getInt("type_id"));
				card.setAppearRate(rs.getInt("appear_rate"));
				card.setValueType(rs.getInt("value_type"));
				card.setActionValue(rs.getInt("action_value"));
				card.setActionfield(rs.getInt("action_field"));
				card.setActionDirection(rs.getInt("action_direction"));
				card.setDescription(rs.getString("description"));
				card.setCardName(rs.getString("jc_card_type.name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return card;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#getCardList(java.lang.String)
	 */
	public Vector getCardList(String condition) {
		// TODO Auto-generated method stub
		Vector cardList = new Vector();
		CardBean card = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT *,jc_card_type.name FROM jc_card inner join jc_card_type on jc_card.type_id=jc_card_type.id ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				card = new CardBean();
				card.setId(rs.getInt("jc_card.id"));
				card.setTypeId(rs.getInt("type_id"));
				card.setAppearRate(rs.getInt("appear_rate"));
				card.setValueType(rs.getInt("value_type"));
				card.setActionValue(rs.getInt("action_value"));
				card.setActionfield(rs.getInt("action_field"));
				card.setActionDirection(rs.getInt("action_direction"));
				card.setDescription(rs.getString("description"));
				card.setCardName(rs.getString("jc_card_type.name"));
				cardList.add(card);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return cardList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#deleteCard(java.lang.String)
	 */
	public boolean deleteCard(String condition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#updateCard(java.lang.String)
	 */
	public boolean updateCard(String condition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.ICardService#getCardCount(java.lang.String)
	 */
	public int getCardCount(String condition) {
		// TODO Auto-generated method stub
		return 0;
	}

	// zhul_2006-07-11_新增机会卡游戏 end

	// zhul_2006-07-17_新增打猎游戏 start
	/*
	 * about hunt_quarry_appear_rate implements
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#addHuntQuarryAppearRate(net.joycool.wap.bean.job.HuntQuarryAppearRateBean)
	 */
	public boolean addHuntQuarryAppearRate(
			HuntQuarryAppearRateBean quarryAppearRate) {
		// TODO Auto-generated method stub
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_hunt_quarry_appear_rate(weapon_id, quarry_id, appear_rate) values(?,?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, quarryAppearRate.getWeaponId());
			pstmt.setInt(2, quarryAppearRate.getQuarryId());
			pstmt.setInt(3, quarryAppearRate.getAppearRate());
			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#deleteHuntQuarryAppearRateList(java.lang.String)
	 */
	public boolean deleteHuntQuarryAppearRate(String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_hunt_quarry_appear_rate WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntQuarryAppearRate(java.lang.String)
	 */
	public HuntQuarryAppearRateBean getHuntQuarryAppearRate(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntQuarryAppearRateCount(java.lang.String)
	 */
	public int getHuntQuarryAppearRateCount(String condition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntQuarryAppearRateList(java.lang.String)
	 */
	public Vector getHuntQuarryAppearRateList(String condition) {
		// TODO Auto-generated method stub
		Vector rateList = new Vector();
		HuntQuarryAppearRateBean appearRate = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_hunt_quarry_appear_rate ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				appearRate = new HuntQuarryAppearRateBean();
				appearRate.setId(rs.getInt("id"));
				appearRate.setWeaponId(rs.getInt("weapon_id"));
				appearRate.setQuarryId(rs.getInt("quarry_id"));
				appearRate.setAppearRate(rs.getInt("appear_rate"));
				rateList.add(appearRate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return rateList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#updateHuntQuarryAppearRate(java.lang.String)
	 */
	public boolean updateHuntQuarryAppearRate(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_hunt_quarry_appear_rate SET " + set
				+ " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * about weapon implements
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#addHuntWeapon(net.joycool.wap.bean.job.HuntWeaponBean)
	 */
	public boolean addHuntWeapon(HuntWeaponBean huntWeapon) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#deleteHuntWeapon(java.lang.String)
	 */
	public boolean deleteHuntWeapon(String condition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntWeapon(java.lang.String)
	 */
	public HuntWeaponBean getHuntWeapon(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntWeaponCount(java.lang.String)
	 */
	public int getHuntWeaponCount(String condition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntWeaponList(java.lang.String)
	 */
	public HashMap getHuntWeaponMap(String condition) {
		// TODO Auto-generated method stub
		HashMap weaponList = new HashMap();
		HuntWeaponBean huntWeapon = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_hunt_weapon  ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				huntWeapon = new HuntWeaponBean();
				huntWeapon.setId(rs.getInt("id"));
				huntWeapon.setName(rs.getString("name"));
				huntWeapon.setPrice(rs.getInt("price"));
				huntWeapon.setShotPrice(rs.getInt("shot_price"));
				huntWeapon.setHitRate(rs.getInt("hit_rate"));
				huntWeapon.setNotHitRate(rs.getInt("no_hit_rate"));
				huntWeapon.setHarmRate(rs.getInt("harm_rate"));
				weaponList.put(new Integer(huntWeapon.getId()), huntWeapon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return weaponList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#updateHuntWeapon(java.lang.String)
	 */
	public boolean updateHuntWeapon(String condition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * about hunt_quarry implements
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#addHuntQuarry(net.joycool.wap.bean.job.HuntQuarryBean)
	 */
	public boolean addHuntQuarry(HuntQuarryBean huntQuarry) {
		// TODO Auto-generated method stub
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_hunt_quarry(name, price, harm_price, hit_point, image) values(?,?,?,?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, huntQuarry.getName());
			pstmt.setInt(2, huntQuarry.getPrice());
			pstmt.setInt(3, huntQuarry.getHarmPrice());
			pstmt.setInt(4, huntQuarry.getHitPoint());
			pstmt.setString(5, huntQuarry.getImage());
			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#deleteHuntQuarry(java.lang.String)
	 */
	public boolean deleteHuntQuarry(String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_hunt_quarry WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntQuarry(java.lang.String)
	 */
	public HuntQuarryBean getHuntQuarry(String condition) {

		HuntQuarryBean quarry = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT id, name, price, harm_price, hit_point, image  FROM jc_hunt_quarry ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				quarry = new HuntQuarryBean();
				quarry.setId(rs.getInt("id"));
				quarry.setName(rs.getString("name"));
				quarry.setPrice(rs.getInt("price"));
				quarry.setHarmPrice(rs.getInt("harm_price"));
				quarry.setHitPoint(rs.getInt("hit_point"));
				quarry.setImage(rs.getString("image"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return quarry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntQuarryCount(java.lang.String)
	 */
	public int getHuntQuarryCount(String condition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntQuarryList(java.lang.String)
	 */
	public HashMap getHuntQuarryMap(String condition) {
		// TODO Auto-generated method stub
		HashMap quarryMap = new HashMap();
		HuntQuarryBean huntQuarry = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_hunt_quarry  ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				huntQuarry = new HuntQuarryBean();
				huntQuarry.setId(rs.getInt("id"));
				huntQuarry.setName(rs.getString("name"));
				huntQuarry.setPrice(rs.getInt("price"));
				huntQuarry.setHarmPrice(rs.getInt("harm_price"));
				huntQuarry.setHitPoint(rs.getInt("hit_point"));
				huntQuarry.setImage(rs.getString("image"));
				quarryMap.put(new Integer(huntQuarry.getId()), huntQuarry);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return quarryMap;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#updateHuntQuarry(java.lang.String)
	 */
	public boolean updateHuntQuarry(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_hunt_quarry SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#addHuntUserWeapon(net.joycool.wap.bean.job.HuntUserWeaponBean)
	 */
	public boolean addHuntUserWeapon(HuntUserWeaponBean huntUserWeapon) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(true);

		String query = "INSERT INTO jc_hunt_user_weapon(user_id,weapon_id,create_datetime,expire_datetime) values(?,?,NOW(),ADDDATE(NOW(),INTERVAL 1 HOUR))";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, huntUserWeapon.getUserId());
			pstmt.setInt(2, huntUserWeapon.getWeaponId());
			// 执行
			dbOp.executePstmt();
			huntUserWeapon.setId(dbOp.getLastInsertId());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#deleteHuntUserWeapon(java.lang.String)
	 */
	public boolean deleteHuntUserWeapon(String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_hunt_user_weapon WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntUserWeapon(java.lang.String)
	 */
	public HuntUserWeaponBean getHuntUserWeapon(String condition) {

		HuntUserWeaponBean userWeapon = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT a.id,a.user_id,a.weapon_id,a.create_datetime,a.expire_datetime,b.name as name"
				+ " FROM jc_hunt_user_weapon a ,jc_hunt_weapon b ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				userWeapon = new HuntUserWeaponBean();
				userWeapon.setId(rs.getInt("id"));
				userWeapon.setUserId(rs.getInt("user_id"));
				userWeapon.setWeaponId(rs.getInt("weapon_id"));
				userWeapon.setCreateDatetime(rs.getTimestamp("create_datetime").getTime());
				userWeapon.setExpireDatetime(rs.getTimestamp("expire_datetime").getTime());
				userWeapon.setName(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return userWeapon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntUserWeaponCount(java.lang.String)
	 */
	public int getHuntUserWeaponCount(String condition) {

		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_hunt_user_weapon";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntUserWeaponList(java.lang.String)
	 */
	public Vector getHuntUserWeaponList(String condition) {
		Vector userWeaponList = null;
		HuntUserWeaponBean userWeapon = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询语句
		String sql = "SELECT * FROM jc_hunt_user_weapon";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}
		// 将结果保存
		try {
			while (rs.next()) {
				if (userWeaponList == null)
					userWeaponList = new Vector();

				userWeapon = new HuntUserWeaponBean();
				userWeapon.setId(rs.getInt("id"));
				userWeapon.setUserId(rs.getInt("user_id"));
				userWeapon.setWeaponId(rs.getInt("weapon_id"));
				userWeapon.setCreateDatetime(rs.getTimestamp("create_datetime").getTime());
				userWeapon.setExpireDatetime(rs.getTimestamp("expire_datetime").getTime());
				userWeaponList.add(userWeapon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		// 返回结果
		return userWeaponList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#updateHuntUserWeapon(java.lang.String)
	 */
	public boolean updateHuntUserWeapon(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_hunt_user_weapon SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#addHuntUserWeapon(net.joycool.wap.bean.job.HuntUserQuarryBean)
	 */
	public boolean addHuntUserQuarry(HuntUserQuarryBean huntUserQuarry) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_hunt_user_quarry(user_id,quarry_id,quarry_count) values(?,?,1)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, huntUserQuarry.getUserId());
			pstmt.setInt(2, huntUserQuarry.getQuarryId());
			// 执行
			dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// 释放资源
			dbOp.release();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#deleteHuntUserQuarry(java.lang.String)
	 */
	public boolean deleteHuntUserQuarry(String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_hunt_user_quarry WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntUserQuarry(java.lang.String)
	 */
	public HuntUserQuarryBean getHuntUserQuarry(String condition) {

		HuntUserQuarryBean userQuarry = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_hunt_user_quarry";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				userQuarry = new HuntUserQuarryBean();
				userQuarry.setId(rs.getInt("id"));
				userQuarry.setUserId(rs.getInt("user_id"));
				userQuarry.setQuarryId(rs.getInt("quarry_id"));
				userQuarry.setQuarryCount(rs.getInt("quarry_count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return userQuarry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntUserQuarryCount(java.lang.String)
	 */
	public int getHuntUserQuarryCount(String condition) {

		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_hunt_user_quarry";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#getHuntUserQuarryList(java.lang.String)
	 */
	public Vector getHuntUserQuarryList(String condition) {
		Vector userQuarryList = null;
		HuntUserQuarryBean userQuarry = null;
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

		// 查询语句
		String sql = "SELECT * FROM jc_hunt_user_quarry";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}
		// 将结果保存
		try {
			while (rs.next()) {
				if (userQuarryList == null)
					userQuarryList = new Vector();

				userQuarry = new HuntUserQuarryBean();
				userQuarry.setId(rs.getInt("id"));
				userQuarry.setUserId(rs.getInt("user_id"));
				userQuarry.setQuarryId(rs.getInt("quarry_id"));
				userQuarry.setQuarryCount(rs.getInt("quarry_count"));
				// macq_2006-12-29_城帮拍卖物品类型字段_start
				userQuarry.setGoodsTpye(1);
				// macq_2006-12-29_城帮拍卖物品类型字段_start
				userQuarryList.add(userQuarry);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		// 返回结果
		return userQuarryList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.infc.IJobService#updateHuntUserQuarry(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateHuntUserQuarry(String set, String condition) {

		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_hunt_user_quarry SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// zhul_2006-07-17 新增打猎游戏 end
	// fanys 2006-08-29 心理测试 start

	public boolean addPsychology(PsychologyBean psychology) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO job_psychology(title,content)values(?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, psychology.getTitle());
			pstmt.setString(2, psychology.getContent());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	private PsychologyBean getPsychology(ResultSet rs) throws SQLException {
		PsychologyBean psychology = new PsychologyBean();
		psychology.setId(rs.getInt("id"));
		psychology.setTitle(rs.getString("title"));
		psychology.setContent(rs.getString("content"));
		return psychology;
	}

	public PsychologyBean getPsychology(String condition) {
		PsychologyBean psychology = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM job_psychology";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				psychology = getPsychology(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return psychology;
	}

	public Vector getPsychologyList(String condition) {
		Vector psychologyList = new Vector();
		PsychologyBean psychology = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM job_psychology";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				psychology = getPsychology(rs);
				psychologyList.add(psychology);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return psychologyList;
	}

	public int getPsychologyCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM job_psychology";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	public boolean updatePsychology(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE job_psychology SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deletePsychology(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM job_psychology WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean addPsychologyAnswer(PsychologyAnswerBean psychologyAnswer) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO job_psychology_answer(psychology_id,answer,explanation)values(?,?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, psychologyAnswer.getPsychologyId());
			pstmt.setString(2, psychologyAnswer.getAnswer());
			pstmt.setString(3, psychologyAnswer.getExplanation());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	private PsychologyAnswerBean getPsychologyAnswer(ResultSet rs)
			throws SQLException {
		PsychologyAnswerBean answer = new PsychologyAnswerBean();
		answer.setId(rs.getInt("id"));
		answer.setPsychologyId(rs.getInt("psychology_id"));
		answer.setAnswer(rs.getString("answer"));
		answer.setExplanation(rs.getString("explanation"));
		return answer;
	}

	public PsychologyAnswerBean getPsychologyAnswer(String condition) {
		PsychologyAnswerBean answer = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM job_psychology_answer";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				answer = getPsychologyAnswer(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return answer;
	}

	public Vector getPsychologyAnswerList(String condition) {
		Vector answerList = new Vector();
		PsychologyAnswerBean answer = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM job_psychology_answer";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				answer = getPsychologyAnswer(rs);
				answerList.add(answer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return answerList;
	}

	public boolean updatePsychologyAnswer(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE job_psychology_answer SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deletePsychologyAnswer(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM job_psychology_answer WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// fanys 2006-08-29 心理测试 end

	// fanys 2006-09-13 start 贺卡
	public boolean addHappyCard(HappyCardBean cardsBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_happy_card(title,content,image,type_id,hits,create_datetime)values(?,?,?,?,0,now())";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, cardsBean.getTitle());
			pstmt.setString(2, cardsBean.getContent());
			pstmt.setString(3, cardsBean.getImage());
			pstmt.setInt(4, cardsBean.getTypeId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	public HappyCardBean getHappyCard(String condition) {
		HappyCardBean card = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_happy_card ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				card = getHappyCard(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return card;
	}

	private HappyCardBean getHappyCard(ResultSet rs) throws SQLException {
		HappyCardBean card = null;
		card = new HappyCardBean();
		card.setId(rs.getInt("id"));
		card.setTitle(rs.getString("title"));
		card.setContent(rs.getString("content"));
		card.setImage(rs.getString("image"));
		card.setHits(rs.getInt("hits"));
		card.setTypeId(rs.getInt("type_id"));
		card.setCreateDateTime(rs.getString("create_datetime"));
		return card;
	}

	public Vector getHappyCardList(String condition) {
		Vector vecCard = new Vector();
		HappyCardBean cards = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_happy_card ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				cards = getHappyCard(rs);
				vecCard.add(cards);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return vecCard;
	}

	public int getHappyCardCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_happy_card ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	public boolean updateHappyCard(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_happy_card SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteHappyCard(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_happy_card WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// fanys 2006-09-13 end

	public boolean addHappyCardType(HappyCardTypeBean happyCardTypeBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_happy_card_type(name,description,category_id)values(?,?,?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, happyCardTypeBean.getName());
			pstmt.setString(2, happyCardTypeBean.getDescription());
			pstmt.setInt(3, happyCardTypeBean.getCategoryId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	public HappyCardTypeBean getHappyCardType(String condition) {
		HappyCardTypeBean cardType = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_happy_card_type ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				cardType = getHappyCardType(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return cardType;
	}

	private HappyCardTypeBean getHappyCardType(ResultSet rs)
			throws SQLException {
		HappyCardTypeBean cardType = new HappyCardTypeBean();
		cardType.setName(rs.getString("name"));
		cardType.setDescription(rs.getString("description"));
		cardType.setCategoryId(rs.getInt("category_id"));
		cardType.setId(rs.getInt("id"));
		return cardType;
	}

	public Vector getHappyCardTypeList(String condition) {
		Vector vecCardType = new Vector();
		HappyCardTypeBean cardType = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_happy_card_type ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				cardType = getHappyCardType(rs);
				vecCardType.add(cardType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return vecCardType;
	}

	public int getHappyCardTypeCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_happy_card_type ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;

	}

	public boolean updateHappyCardType(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_happy_card_type SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteHappyCardType(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_happy_card_type WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean addHappyCardCategory(
			HappyCardCategoryBean happyCardCategoryBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_happy_card_category(name)values(?)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, happyCardCategoryBean.getName());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	public HappyCardCategoryBean getHappyCardCategory(String condition) {
		HappyCardCategoryBean cardCategory = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_happy_card_category ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				cardCategory = getHappyCardCategory(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return cardCategory;
	}

	public Vector getHappyCardCategoryList(String condition) {
		Vector vecCardCategory = new Vector();
		HappyCardCategoryBean cardCategory = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_happy_card_category ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				cardCategory = getHappyCardCategory(rs);
				vecCardCategory.add(cardCategory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return vecCardCategory;
	}

	private HappyCardCategoryBean getHappyCardCategory(ResultSet rs)
			throws SQLException {
		HappyCardCategoryBean cardCategory = new HappyCardCategoryBean();
		cardCategory.setName(rs.getString("name"));
		cardCategory.setId(rs.getInt("id"));
		return cardCategory;
	}

	public int getHappyCardCategoryCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_happy_card_category ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	public boolean updateHappyCardCategory(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_happy_card_category SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteHappyCardCategory(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_happy_card_category WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean addHappyCardSend(HappyCardSendBean happyCardSendBean) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_happy_card_send("
				+ "user_id,mobile,sender,receiver,card_id,"
				+ "mark,send_mark,success_mark,receiver_id,"
				+ "in_or_out_mark,new_user_mark,send_datetime,view_datetime)"
				+ " values(?,?,?,?,?,?," + "?,?,?,?,?,now(),null)";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, happyCardSendBean.getUserId());
			pstmt.setString(2, happyCardSendBean.getMobile());
			pstmt.setString(3, happyCardSendBean.getSender());
			pstmt.setString(4, happyCardSendBean.getReceiver());

			pstmt.setInt(5, happyCardSendBean.getCardId());
			pstmt.setInt(6, happyCardSendBean.getMark());
			pstmt.setInt(7, happyCardSendBean.getSendMark());
			pstmt.setInt(8, happyCardSendBean.getSuccessMark());

			pstmt.setInt(9, happyCardSendBean.getReceiverId());
			pstmt.setInt(10, happyCardSendBean.getInOrOutMark());
			pstmt.setInt(11, happyCardSendBean.getNewUserMark());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	public HappyCardSendBean getHappyCardSend(String condition) {
		HappyCardSendBean cardSend = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_happy_card_send ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				cardSend = getHappyCardSend(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return cardSend;
	}

	private HappyCardSendBean getHappyCardSend(ResultSet rs)
			throws SQLException {
		HappyCardSendBean happyCardSendBean = null;
		happyCardSendBean = new HappyCardSendBean();
		happyCardSendBean.setId(rs.getInt("id"));

		happyCardSendBean.setUserId(rs.getInt("user_id"));
		happyCardSendBean.setMobile(rs.getString("mobile"));
		happyCardSendBean.setSender(rs.getString("sender"));
		happyCardSendBean.setReceiver(rs.getString("receiver"));

		happyCardSendBean.setCardId(rs.getInt("card_id"));
		happyCardSendBean.setMark(rs.getInt("mark"));
		happyCardSendBean.setSendMark(rs.getInt("send_mark"));
		happyCardSendBean.setSuccessMark(rs.getInt("success_mark"));

		happyCardSendBean.setReceiverId(rs.getInt("receiver_id"));
		happyCardSendBean.setInOrOutMark(rs.getInt("in_or_out_mark"));
		happyCardSendBean.setNewUserMark(rs.getInt("new_user_mark"));
		happyCardSendBean.setSendDateTime(rs.getString("send_datetime"));

		happyCardSendBean.setViewDateTime(rs.getString("view_datetime"));
		return happyCardSendBean;
	}

	public Vector getHappyCardSendList(String condition) {
		Vector vecCardCategory = new Vector();
		HappyCardSendBean cardSend = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_happy_card_send ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				cardSend = getHappyCardSend(rs);
				vecCardCategory.add(cardSend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return vecCardCategory;
	}

	public int getHappyCardSendCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_happy_card_send ";

		if (condition.indexOf("temp") != -1)
			query = condition;
		else {
			if (condition != null) {
				query = query + " WHERE " + condition;
			}
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;
	}

	public boolean updateHappyCardSend(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句

		String query = "UPDATE jc_happy_card_send SET " + set;

		if (condition != null)

		{
			query = "UPDATE jc_happy_card_send SET " + set + " WHERE "
					+ condition;
		}

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteHappyCardSend(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_happy_card_send WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean addHappyCardSendStat(HappyCardStatBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_happy_card_stat(stat_datetime,invite_count,accept_new_count,reply_count,reply_new_count,reach_limit_count,in_count) "
				+ "VALUES(?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getStatDatetime());
			pstmt.setInt(2, bean.getInviteCount());
			pstmt.setInt(3, bean.getAcceptNewCount());
			pstmt.setInt(4, bean.getReplyCount());
			pstmt.setInt(5, bean.getReplyNewCount());
			pstmt.setInt(6, bean.getReachLimitCount());
			pstmt.setInt(7, bean.getInCount());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;
	}

	public void deleteHappyCardSendStat(String condition) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "delete from jc_happy_card_stat where  " + condition;
		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	public boolean updateHappyCardSendStat(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_happy_card_stat SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// wucx 静国神社2006－9－25
	public boolean addSpirit(SpiritBean spiritBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_spirit(title,description,price,effect,effectdes) "
				+ "VALUES(?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, spiritBean.getTitle());
			pstmt.setString(2, spiritBean.getDescription());
			pstmt.setInt(3, spiritBean.getPrice());
			pstmt.setInt(4, spiritBean.getEffect());
			pstmt.setString(5, spiritBean.getEffectdes());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;
	}

	public SpiritBean getSpirit(String condition) {
		SpiritBean spiritbean = null;
		// 数据库操作类

		// 构建查询语句
		String query = "SELECT * FROM jc_spirit";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";
		spiritbean = (SpiritBean) OsCacheUtil.get(query,
				OsCacheUtil.SPIRIT_GROUP, OsCacheUtil.SPIRIT_FLUSH_PERIOD);
		if (spiritbean == null) {
			// 查询
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			ResultSet rs = dbOp.executeQuery(query);

			try {
				// 结果不为空
				if (rs.next()) {

					spiritbean = new SpiritBean();
					spiritbean.setId(rs.getInt("id"));

					spiritbean.setDescription(rs.getString("description"));
					spiritbean.setEffect(rs.getInt("effect"));
					spiritbean.setEffectdes(rs.getString("effectdes"));
					spiritbean.setPrice(rs.getInt("price"));

					spiritbean.setTitle(rs.getString("title"));
					if (null != spiritbean)
						OsCacheUtil.put(query, spiritbean,
								OsCacheUtil.SPIRIT_GROUP);
					else {
						spiritbean = new SpiritBean();
						spiritbean.setId(-1);
						OsCacheUtil.put(query, spiritbean,
								OsCacheUtil.SPIRIT_GROUP);
					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// 释放资源
				dbOp.release();
			}
		}
		if (spiritbean.getId() == -1)
			spiritbean = null;
		return spiritbean;
	}

	public Vector getSpiritList(String condition) {
		Vector vecSpirit = null;
		// new Vector();
		SpiritBean spirits = null;

		// 构建查询语句
		String query = "SELECT * FROM jc_spirit ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		vecSpirit = (Vector) OsCacheUtil.get(query, OsCacheUtil.SPIRIT_GROUP,
				OsCacheUtil.SPIRIT_FLUSH_PERIOD);
		if (vecSpirit == null) {
			// 查询
			// 数据库操作类
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			vecSpirit = new Vector();
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				while (rs.next()) {
					spirits = getSpirit(rs);
					vecSpirit.add(spirits);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// 释放资源
			dbOp.release();
		}
		if (vecSpirit != null)

			OsCacheUtil.put(query, vecSpirit, OsCacheUtil.SPIRIT_GROUP);
		else

			vecSpirit = new Vector();
		return vecSpirit;
	}

	public int getSpiritCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) as c_id FROM jc_spirit ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}

		return count;

	}

	private SpiritBean getSpirit(ResultSet rs) throws SQLException {
		SpiritBean spirit = new SpiritBean();
		spirit.setId(rs.getInt("id"));
		spirit.setTitle(rs.getString("title"));
		spirit.setDescription(rs.getString("description"));
		spirit.setEffectdes(rs.getString("effectdes"));
		spirit.setPrice(rs.getInt("price"));
		spirit.setEffect(rs.getInt("effect"));
		return spirit;
	}

	public boolean updateSpirit(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_spirit SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public void deleteSpirit(String condition) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "delete from jc_spirit where  " + condition;
		// 执行操作
		dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
	}

	// wucx静国神社2006－9－25

	// wucx 2006-11-8赌马游戏 start
	public boolean addHandbookinger(HandbookingerBean handbookingerBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "INSERT INTO jc_handbookinger(compensate,success) values(?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();

		try {
			pstmt.setInt(1, handbookingerBean.getCompensate());
			pstmt.setInt(2, handbookingerBean.getSuccess());
			pstmt.executeUpdate();

		} catch (Exception e) {
			return false;
		} finally {
			dbOp.release();
		}
		return true;
	}

	public HandbookingerBean getHandbookinger(String condition) {
		HandbookingerBean bean = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select * from jc_handbookinger ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return null;
		}
		// 传递参数
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				bean = new HandbookingerBean();
				bean.setId(rs.getInt("id"));
				bean.setCompensate(rs.getInt("compensate"));
				bean.setSuccess(rs.getInt("success"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return bean;

	}

	public Vector getHandbookingerList(String condition) {
		Vector chatStatList = new Vector();
		HandbookingerBean bean = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_handbookinger ";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				bean = new HandbookingerBean();
				bean.setId(rs.getInt("id"));
				bean.setCompensate(rs.getInt("compensate"));
				bean.setSuccess(rs.getInt("success"));
				chatStatList.add(bean);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		finally {
			dbOp.release();
		}

		return chatStatList;

	}

	public int getHandbookingerCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) AS c_id FROM jc_handbookinger";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
			return count;
		}

		// 释放资源
		dbOp.release();
		return count;

	}

	public boolean updateHandbookinger(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_handbookinger SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;

	}

	public boolean deleteHandbookinger(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_handbookinger WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;

	}

	public boolean addHandbookingerRecord(
			HandbookingerRecordBean handbookingerRecordBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "INSERT INTO jc_handbookinger_record(compensate_id,horse_id,user_id,mark,money,create_datetime) values(?,?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();

		try {
			pstmt.setInt(1, handbookingerRecordBean.getCompensateId());
			pstmt.setInt(2, handbookingerRecordBean.getHorseId());
			pstmt.setInt(3, handbookingerRecordBean.getUserId());
			pstmt.setInt(4, handbookingerRecordBean.getMark());
			pstmt.setInt(5, handbookingerRecordBean.getMoney());
			pstmt.executeUpdate();

		} catch (Exception e) {
			return false;
		} finally {
			dbOp.release();
		}
		return true;
	}

	public HandbookingerRecordBean getHandbookingerRecord(String condition) {
		HandbookingerRecordBean bean = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select * from jc_handbookinger_record ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return null;
		}
		// 传递参数
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				bean = new HandbookingerRecordBean();
				bean.setId(rs.getInt("id"));
				bean.setCompensateId(rs.getInt("compensate_id"));
				bean.setHorseId(rs.getInt("horse_id"));
				bean.setMark(rs.getInt("mark"));
				bean.setUserId(rs.getInt("user_id"));
				bean.setMoney(rs.getInt("money"));
				bean.setCreateDatetime(rs.getString("create_datetime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return bean;
	}

	public Vector getHandbookingerRecordList(String condition) {
		Vector chatStatList = new Vector();
		HandbookingerRecordBean bean = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_handbookinger_record ";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				bean = new HandbookingerRecordBean();
				bean.setId(rs.getInt("id"));
				bean.setCompensateId(rs.getInt("compensate_id"));
				bean.setHorseId(rs.getInt("horse_id"));
				bean.setMark(rs.getInt("mark"));
				bean.setUserId(rs.getInt("user_id"));
				bean.setMoney(rs.getInt("money"));
				bean.setCreateDatetime(rs.getString("create_datetime"));
				chatStatList.add(bean);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		finally {
			dbOp.release();
		}

		return chatStatList;
	}

	public int getHandbookingerRecordCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) AS c_id FROM jc_handbookinger_record";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
			return count;
		}

		// 释放资源
		dbOp.release();
		return count;

	}

	public boolean updateHandbookingerRecord(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_handbookinger_record SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteHandbookingerRecord(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_handbookinger_record WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	// wucx 2006-11-8赌马游戏 end
	// wucx 2006-11-20出气筒 start
	public boolean addAnger(AngerBean AngerBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "INSERT INTO jc_anger_record(user_id,name,mark,gender,relation,degree,create_datetime) values(?,?,?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();

		try {
			pstmt.setInt(1, AngerBean.getUserId());
			pstmt.setString(2, AngerBean.getName());
			pstmt.setInt(3, AngerBean.getMark());
			pstmt.setInt(4, AngerBean.getGender());
			pstmt.setInt(5, AngerBean.getRelation());
			pstmt.setInt(6, AngerBean.getDegree());
			pstmt.executeUpdate();

		} catch (Exception e) {
			return false;
		} finally {
			dbOp.release();
		}
		return true;
	}

	public AngerBean getAnger(String condition) {
		AngerBean bean = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select * from jc_anger_record ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return null;
		}
		// 传递参数
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				bean = new AngerBean();
				bean.setId(rs.getInt("id"));
				bean.setGender(rs.getInt("gender"));
				bean.setMark(rs.getInt("mark"));
				bean.setUserId(rs.getInt("user_id"));
				bean.setName(rs.getString("name"));
				bean.setRelation(rs.getInt("relation"));
				bean.setDegree(rs.getInt("degree"));
				bean.setCreateDatetime(rs.getString("create_datetime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return bean;
	}

	public Vector getAngerList(String condition) {
		Vector angerList = new Vector();
		AngerBean bean = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_anger_record ";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				bean = new AngerBean();
				bean.setId(rs.getInt("id"));
				bean.setGender(rs.getInt("gender"));
				bean.setMark(rs.getInt("mark"));
				bean.setUserId(rs.getInt("user_id"));
				bean.setName(rs.getString("name"));
				bean.setRelation(rs.getInt("relation"));
				bean.setDegree(rs.getInt("degree"));
				bean.setCreateDatetime(rs.getString("create_datetime"));
				angerList.add(bean);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		finally {
			dbOp.release();
		}

		return angerList;
	}

	public int getAngerCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) AS c_id FROM jc_anger_record";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
			return count;
		}

		// 释放资源
		dbOp.release();
		return count;
	}

	public boolean updateAnger(String set, String condition) {
		boolean result;

		// 
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_anger_record SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteAnger(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_anger_record WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean addAngerExpression(AngerExpressionBean AngerBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "INSERT INTO jc_anger_expression(relation,phase,title,content,rate) values(?,?,?,?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();

		try {
			pstmt.setInt(1, AngerBean.getRelation());
			pstmt.setInt (2, AngerBean.getPhase());
			pstmt.setString(3, AngerBean.getTitle());
			pstmt.setString(4, AngerBean.getContent());
			pstmt.setInt(5, AngerBean.getRate());
			pstmt.executeUpdate();

		} catch (Exception e) {
			return false;
		} finally {
			dbOp.release();
		}
		return true;
	}

	public AngerExpressionBean getAngerExpression(String condition) {
		AngerExpressionBean bean = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select * from jc_anger_expression ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return null;
		}
		// 传递参数
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				bean = new AngerExpressionBean();
				bean.setId(rs.getInt("id"));
				bean.setContent(rs.getString("content"));
				bean.setRate(rs.getInt("rate"));
				bean.setRelation(rs.getInt("relation"));
				bean.setPhase(rs.getInt("phase"));
				bean.setTitle(rs.getString("title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return bean;
	}

	public Vector getAngerExpressionList(String condition) {
		Vector expressionList = new Vector();
		AngerExpressionBean bean = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_anger_expression ";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				bean = new AngerExpressionBean();
				bean.setId(rs.getInt("id"));
				bean.setContent(rs.getString("content"));
				bean.setRate(rs.getInt("rate"));
				bean.setRelation(rs.getInt("relation"));
				bean.setPhase(rs.getInt("phase"));
				bean.setTitle(rs.getString("title"));
				expressionList.add(bean);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		finally {
			dbOp.release();
		}

		return expressionList;
	}

	public int getAngerExpressionCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) AS c_id FROM jc_anger_expression";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
			return count;
		}

		// 释放资源
		dbOp.release();
		return count;
	}

	public boolean updateAngerExpression(String set, String condition) {
		boolean result;

		// 
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_anger_expression SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteAngerExpression(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_anger_expression WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}
	public boolean addAngerCard(AngerCardBean AngerBean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "INSERT INTO jc_anger_card(name,number,title,content) values(?,?,?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();

		try {
			pstmt.setString(1, AngerBean.getName());
			pstmt.setInt (2, AngerBean.getNumber());
			pstmt.setString(3, AngerBean.getTitle());
			pstmt.setString(4, AngerBean.getContent());
					pstmt.executeUpdate();

		} catch (Exception e) {
			return false;
		} finally {
			dbOp.release();
		}
		return true;
	}

	public AngerCardBean getAngerCard(String condition) {
		AngerCardBean bean = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select * from jc_anger_card ";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return null;
		}
		// 传递参数
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				bean = new AngerCardBean();
				bean.setId(rs.getInt("id"));
				bean.setContent(rs.getString("content"));
				bean.setName(rs.getString("name"));
				bean.setNumber(rs.getInt("number"));
				bean.setTitle(rs.getString("title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return bean;
	}

	public Vector getAngerCardList(String condition) {
		Vector cardList = new Vector();
		AngerCardBean bean = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_anger_card ";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				bean = new AngerCardBean();
				bean.setId(rs.getInt("id"));
				bean.setContent(rs.getString("content"));
				bean.setName(rs.getString("name"));
				bean.setNumber(rs.getInt("number"));
				bean.setTitle(rs.getString("title"));
				cardList.add(bean);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		finally {
			dbOp.release();
		}

		return cardList;
	}

	public int getAngerCardCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT count(id) AS c_id FROM jc_anger_card";
		if (condition != null) {

			query = query + " WHERE " + condition;

		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			dbOp.release();
			e.printStackTrace();
			return count;
		}

		// 释放资源
		dbOp.release();
		return count;
	}

	public boolean updateAngerCard(String set, String condition) {
		boolean result;

		// 
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_anger_card SET " + set + " WHERE "
				+ condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	public boolean deleteAngerCard(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_anger_card WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}
	// wucx 2006-11-20出气筒 end
}
