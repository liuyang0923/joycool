package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.joycool.wap.bean.pk.PKActBean;
import net.joycool.wap.bean.pk.PKEquipBean;
import net.joycool.wap.bean.pk.PKMObjBean;
import net.joycool.wap.bean.pk.PKMedicineBean;
import net.joycool.wap.bean.pk.PKMissionBean;
import net.joycool.wap.bean.pk.PKMonsterBean;
import net.joycool.wap.bean.pk.PKMonsterSkillBean;
import net.joycool.wap.bean.pk.PKNpcBean;
import net.joycool.wap.bean.pk.PKObjTypeBean;
import net.joycool.wap.bean.pk.PKSceneBean;
import net.joycool.wap.bean.pk.PKUserBSkillBean;
import net.joycool.wap.bean.pk.PKUserBagBean;
import net.joycool.wap.bean.pk.PKUserBean;
import net.joycool.wap.bean.pk.PKUserHSkillBean;
import net.joycool.wap.bean.pk.PKUserSkillBean;
import net.joycool.wap.service.infc.IPKService;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author macq
 * @datetime 2007-1-29 下午04:07:14
 * @explain
 */
public class PKServiceImpl implements IPKService {
	public PKMonsterSkillBean getPKMonsterSkill(String condition) {
		PKMonsterSkillBean pkMonsterSkill = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_monster_skill";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkMonsterSkill = this.getPKMonsterSkill(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMonsterSkill;
	}

	public Vector getPKMonsterSkillList(String condition) {
		Vector pkMonsterSkillList = new Vector();
		PKMonsterSkillBean pkMonsterSkill = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_monster_skill";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkMonsterSkill = this.getPKMonsterSkill(rs);
				pkMonsterSkillList.add(pkMonsterSkill);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMonsterSkillList;
	}

	public boolean addPKMonsterSkill(PKMonsterSkillBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_monster_skill(description,aggress_growth_radix) VALUES(?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getDescription());
			pstmt.setString(2, bean.getAggressGrowthRadix());
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

	public boolean delPKMonsterSkill(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_monster_skill WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKMonsterSkill(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_monster_skill SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKMonsterSkillCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_monster_skill WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public PKMonsterBean getPKMonster(String condition) {
		PKMonsterBean pkMonster = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_monster";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkMonster = this.getPKMonster(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMonster;
	}

	public Vector getPKMonsterList(String condition) {
		Vector pkMonsterList = new Vector();
		PKMonsterBean pkMonster = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_monster";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkMonster = this.getPKMonster(rs);
				pkMonsterList.add(pkMonster);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMonsterList;
	}

	public boolean addPKMonster(PKMonsterBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_monster(name,experience,physical,energy,aggressivity,recovery,flying,luck,skill_type_id,drop_type_id,skill_id,rate) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getExperience());
			pstmt.setInt(3, bean.getPhysical());
			pstmt.setInt(4, bean.getEnergy());
			pstmt.setInt(5, bean.getAggressivity());
			pstmt.setInt(6, bean.getRecovery());
			pstmt.setInt(7, bean.getFlying());
			pstmt.setInt(8, bean.getLuck());
			pstmt.setInt(9, bean.getSkillTypeId());
			pstmt.setString(10, bean.getDropTypeId());
			pstmt.setString(11, bean.getSkillId());
			pstmt.setInt(12, bean.getRate());
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

	public boolean delPKMonster(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_monster WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKMonster(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_monster SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKMonsterCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_monster WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public PKUserSkillBean getPKUserSkill(String condition) {
		PKUserSkillBean pkUserSkill = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user_skill";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkUserSkill = this.getPKUserSkill(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUserSkill;
	}

	public Vector getPKUserSkillList(String condition) {
		Vector pkUserSkillList = new Vector();
		PKUserSkillBean pkUserSkill = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user_skill";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkUserSkill = this.getPKUserSkill(rs);
				pkUserSkillList.add(pkUserSkill);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUserSkillList;
	}

	public boolean addPKUserSkill(PKUserSkillBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_user_skill(name,description,description1,description2,grade_count,aggress_growth_radix,type,weapon_type,price,picture) VALUES(?,?,?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setString(3, bean.getDescription1());
			pstmt.setString(4, bean.getDescription2());
			pstmt.setInt(5, bean.getGradeCount());
			pstmt.setString(6, bean.getAggressGrowthRadix());
			pstmt.setInt(7, bean.getType());
			pstmt.setInt(8, bean.getWeaponType());
			pstmt.setInt(9, bean.getPrice());
			pstmt.setString(10, bean.getPicture());
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

	public boolean delPKUserSkill(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_user_skill WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKUserSkill(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_user_skill SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKUserSkillCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_user_skill WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public PKUserBean getPKUser(String condition) {
		PKUserBean pkUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkUser = this.getPKUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUser;
	}

	public Vector getPKUserList(String condition) {
		Vector pkUserList = new Vector();
		PKUserBean pkUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkUser = this.getPKUser(rs);
				pkUserList.add(pkUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUserList;
	}

	public boolean addPKUser(PKUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		//liuyi 2007-02-06 添加初始属性 start
		String query = "INSERT INTO pk_user(user_id, experience, base_physical, base_energy, base_aggressivity, base_recovery, base_flying, base_luck, current_physical, current_energy,mission_start,mission_end) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getExperience());
			pstmt.setInt(3, bean.getBasePhysical());
			pstmt.setInt(4, bean.getBaseEnergy());
			pstmt.setInt(5, bean.getBaseAggressivity());
			pstmt.setInt(6, bean.getBaseRecovery());
			pstmt.setInt(7, bean.getBaseFlying());
			pstmt.setInt(8, bean.getBaseLuck());
			pstmt.setInt(9, bean.getCurrentPhysical());
			pstmt.setInt(10, bean.getCurrentEnergy());
			pstmt.setString(11, bean.getMissionStart());
			pstmt.setString(12, bean.getMissionEnd());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		//liuyi 2007-02-06 添加初始属性 end
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;
	}

	public boolean delPKUser(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKUser(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_user SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_user WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public PKActBean getPKAct(String condition) {
		PKActBean pkAct = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_scene";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkAct = this.getPKAct(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkAct;
	}

	public Vector getPKActList(String condition) {
		Vector pkActList = new Vector();
		PKActBean pkAct = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_scene";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkAct = this.getPKAct(rs);
				pkActList.add(pkAct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkActList;
	}

	public boolean addPKScene(PKSceneBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_scene(name,description,monster,max_role_count,picture) VALUES(?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setString(3, bean.getMonster());
			pstmt.setInt(4, bean.getMaxRoleCount());
			pstmt.setString(5, bean.getPicture());
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

	public boolean delPKScene(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_scene WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKScene(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_scene SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKSceneCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_scene WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public PKUserHSkillBean getPKUserHSkill(String condition) {
		PKUserHSkillBean pkUserHSkill = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user_hskill";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkUserHSkill = this.getPKUserHSkill(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUserHSkill;
	}

	public Vector getPKUserHSkillList(String condition) {
		Vector pkUserHSkillList = new Vector();
		PKUserHSkillBean pkUserHSkill = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user_hskill";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkUserHSkill = this.getPKUserHSkill(rs);
				pkUserHSkillList.add(pkUserHSkill);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUserHSkillList;
	}

	public boolean addPKUserHSkill(PKUserHSkillBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_user_hskill(user_id, skill_id, skill_key, skill_type, excersize,rank) VALUES(?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getSkillId());
			pstmt.setInt(3, bean.getSkillKey());
			pstmt.setInt(4, bean.getSkillType());
			pstmt.setInt(5, bean.getExcersize());
			pstmt.setInt(6, bean.getRank());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		
		bean.setId(SqlUtil.getLastInsertId(dbOp, "pk_user_hskill"));
		
		// 释放资源
		dbOp.release();
		return true;
	}

	public boolean delPKUserHSkill(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_user_hskill WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKUserHSkill(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_user_hskill SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKUserHSkillCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_user_hskill WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}
	
	public PKUserBagBean getPKUserBag(String condition) {
		PKUserBagBean pkUserBag = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user_bag";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkUserBag = this.getPKUserBag(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUserBag;
	}

	public Vector getPKUserBagList(String condition) {
		Vector pkUserBagList = new Vector();
		PKUserBagBean pkUserBag = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user_bag";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkUserBag = this.getPKUserBag(rs);
				pkUserBagList.add(pkUserBag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUserBagList;
	}

	public boolean addPKUserBag(PKUserBagBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_user_bag(user_id,equip_id,site_id,endurance_degree,type) VALUES(?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getEquipId());
			pstmt.setInt(3, bean.getSiteId());
			pstmt.setInt(4, bean.getEnduranceDegree());
			pstmt.setInt(5, bean.getType());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		
		bean.setId(SqlUtil.getLastInsertId(dbOp, "pk_user_bag"));
		
		// 释放资源
		dbOp.release();
		return true;
	}

	public boolean delPKUserBag(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_user_bag WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKUserBag(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_user_bag SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKUserBagCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_user_bag WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}
	
	public PKNpcBean getPKNpc(String condition) {
		PKNpcBean pkNpc = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_npc";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkNpc = this.getPKNpc(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkNpc;
	}

	public Vector getPKNpcList(String condition) {
		Vector pkNpcList = new Vector();
		PKNpcBean pkNpc = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_npc";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkNpc = this.getPKNpc(rs);
				pkNpcList.add(pkNpc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkNpcList;
	}

	public boolean addPKNpc(PKNpcBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_npc(scene_id, name, equip,medicine,skill,bskill) VALUES(?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getSceneId());
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getEquip());
			pstmt.setString(4, bean.getMedicine());
			pstmt.setString(5, bean.getSkill());
			pstmt.setString(6, bean.getBSkill());
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

	public boolean delPKNpc(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_npc WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKNpc(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_npc SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKNpcCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_npc WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}
	
	public PKEquipBean getPKEuqip(String condition) {
		PKEquipBean pkEuqip = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_equip";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkEuqip = this.getPKEuqip(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkEuqip;
	}

	public Vector getPKEuqipList(String condition) {
		Vector pkEuqipList = new Vector();
		PKEquipBean pkEuqip = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_equip";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkEuqip = this.getPKEuqip(rs);
				pkEuqipList.add(pkEuqip);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkEuqipList;
	}

	public boolean addPKEuqip(PKEquipBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_equip(name,type,child_type,physical_growth_radix,physical_max_radix,energy_growth_radix," +
				"energy_max_radix,aggress_growth_radix,aggress_max_radix,recovery_growth_radix,recovery_max_radix," +
				"flying_growth_radix,flying_max_radix,luck_growth_radix,luck_max_radix,price,picture,site) VALUES" +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getType());
			pstmt.setInt(3, bean.getChildType());
			pstmt.setInt(4, bean.getPhysicalGrowthRadix());
			pstmt.setInt(5, bean.getPhysicalMaxRadix());
			pstmt.setInt(6, bean.getEnergyGrowthRadix());
			pstmt.setInt(7, bean.getEnergyMaxRadix());
			pstmt.setInt(8, bean.getAggressGrowthRadix());
			pstmt.setInt(9, bean.getAggressMaxRadix());
			pstmt.setInt(10, bean.getRecoveryGrowthRadix());
			pstmt.setInt(11, bean.getRecoveryMaxRadix());
			pstmt.setInt(12, bean.getFlyingGrowthRadix());
			pstmt.setInt(13, bean.getFlyingMaxRadix());
			pstmt.setInt(14, bean.getLuckGrowthRadix());
			pstmt.setInt(15, bean.getLuckMaxRadix());
			pstmt.setInt(16, bean.getPrice());
			pstmt.setString(17, bean.getPicture());
			pstmt.setInt(18, bean.getSite());
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

	public boolean delPKEuqip(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_equip WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKEuqip(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_equip SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKEuqipCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_equip WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}
	
	
	
	
	public PKMedicineBean getPKMedicine(String condition) {
		PKMedicineBean pkMedicine = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_medicine";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkMedicine = this.getPKMedicine(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMedicine;
	}

	public Vector getPKMedicineList(String condition) {
		Vector pkMedicineList = new Vector();
		PKMedicineBean pkMedicine = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_medicine";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkMedicine = this.getPKMedicine(rs);
				pkMedicineList.add(pkMedicine);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMedicineList;
	}

	public boolean addPKMedicine(PKMedicineBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_medicine(name,description,grade,physical_growth,energy_growth,skill_growth," +
				"aggress_growth,recovery_growth,price,picture) VALUES(?,?,?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setInt(3, bean.getGrade());
			pstmt.setInt(4, bean.getPhysicalGrowth());
			pstmt.setInt(5, bean.getEnergyGrowth());
			pstmt.setInt(6, bean.getSkillGrowth());
			pstmt.setInt(7, bean.getAggressGrowth());
			pstmt.setInt(8, bean.getRecoveryGrowth());
			pstmt.setInt(9, bean.getPrice());
			pstmt.setString(10, bean.getPicture());
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

	public boolean delPKMedicine(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_medicine WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKMedicine(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_medicine SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKMedicineCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_medicine WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}
	
	
	
	public PKUserBSkillBean getPKUserBSkill(String condition) {
		PKUserBSkillBean pkUserBSkill = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user_bskill";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkUserBSkill = this.getPKUserBSkill(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUserBSkill;
	}

	public Vector getPKUserBSkillList(String condition) {
		Vector pkUserBSkillList = new Vector();
		PKUserBSkillBean pkUserBSkill = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_user_bskill";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkUserBSkill = this.getPKUserBSkill(rs);
				pkUserBSkillList.add(pkUserBSkill);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkUserBSkillList;
	}

	/**
	 * 该方法尚无实现
	 */
	public boolean addPKUserBSkill(PKUserBSkillBean bean) {
//		// 数据库操作类
//		DbOperation dbOp = new DbOperation();
//		dbOp.init();
//		String query = "INSERT INTO pk_user_bskill(name,description,grade,physical_growth_radix,physical_max_radix,energy_growth_radix," +
//				"energy_max_radix,flying_growth_radix,flying_max_radix,aggress_growth_radix,aggress_max_radix," +
//				"recovery_growth_radix,recovery_max_radix,type,price,picture) VALUES" +
//				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		// 准备
//		if (!dbOp.prepareStatement(query)) {
//			dbOp.release();
//			return false;
//		}
//		// 传递参数
//		PreparedStatement pstmt = dbOp.getPStmt();
//		try {
//			pstmt.setString(1, bean.getName());
//			pstmt.setString(2, bean.getDescription());
//			pstmt.setInt(3, bean.getGrade());
//			pstmt.setInt(4, bean.getPhysicalGrowthRadix());
//			pstmt.setInt(5, bean.getPhysicalMaxRadix());
//			pstmt.setInt(6, bean.getEnergyGrowthRadix());
//			pstmt.setInt(7, bean.getEnergyMaxRadix());
//			pstmt.setInt(8, bean.getFlyingGrowthRadix());
//			pstmt.setInt(9, bean.getFlyingMaxRadix());
//			pstmt.setInt(10, bean.getAggressGrowthRadix());
//			pstmt.setInt(11, bean.getAggressMaxRadix());
//			pstmt.setInt(12, bean.getRecoveryGrowthRadix());
//			pstmt.setInt(13, bean.getRecoveryMaxRadix());
//			pstmt.setInt(14, bean.getType());
//			pstmt.setInt(15, bean.getPrice());
//			pstmt.setString(16, bean.getPicture());
//		} catch (SQLException e) {
//			e.printStackTrace();
//			dbOp.release();
//			return false;
//		}
//		// 执行
//		dbOp.executePstmt();
//		// 释放资源
//		dbOp.release();
		return true;
	}

	public boolean delPKUserBSkill(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_user_bskill WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKUserBSkill(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_user_bskill SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKUserBSkillCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_user_bskill WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}
	
	public PKMissionBean getPKMission(String condition) {
		PKMissionBean pkMission = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_mission";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkMission = this.getPKMission(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMission;
	}

	public Vector getPKMissionList(String condition) {
		Vector pkMissionList = new Vector();
		PKMissionBean pkMission = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_mission";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkMission = this.getPKMission(rs);
				pkMissionList.add(pkMission);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMissionList;
	}

	public boolean addPKMission(PKMissionBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_mission(name,description,objs,prize) VALUES(?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setString(3, bean.getObjs());
			pstmt.setString(4, bean.getPrize());
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

	public boolean delPKMission(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_mission WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updatePKMission(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_mission SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getPKMissionCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_mission WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}
	
	public PKMObjBean getPKMObj(String condition){
		PKMObjBean pkMObj = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_mobj";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				pkMObj = this.getPKMObj(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMObj;
	}
	public Vector getPKMObjList(String condition){
		Vector pkMObjList = new Vector();
		PKMObjBean pkMObj = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pk_mobj";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				pkMObj = this.getPKMObj(rs);
				pkMObjList.add(pkMObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return pkMObjList;
	}
	public boolean addPKMObj(PKMObjBean bean){
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pk_mobj(name) VALUES(?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
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
	public boolean delPKMObj(String condition){
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pk_mobj WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}
	public boolean updatePKMObj(String set, String condition){
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pk_mobj SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}
	public int getPKMObjCount(String condition){
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pk_mobj WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}
	
	
	private PKMonsterBean getPKMonster(ResultSet rs) throws SQLException {
		PKMonsterBean pkMonster = new PKMonsterBean();
		pkMonster.setId(rs.getInt("id"));
		pkMonster.setName(rs.getString("name"));
		pkMonster.setExperience(rs.getInt("experience"));
		pkMonster.setPhysical(rs.getInt("physical"));
		pkMonster.setEnergy(rs.getInt("energy"));
		pkMonster.setAggressivity(rs.getInt("aggressivity"));
		pkMonster.setRecovery(rs.getInt("recovery"));
		pkMonster.setFlying(rs.getInt("flying"));
		pkMonster.setLuck(rs.getInt("luck"));
		pkMonster.setSkillTypeId(rs.getInt("skill_type_id"));
		pkMonster.setDropTypeId(rs.getString("drop_type_id"));
		List dropIdList = stringtoList(rs.getString("drop_type_id"));
		pkMonster.setDropTypeIdList(dropIdList);
		pkMonster.setSkillId(rs.getString("skill_id"));
		pkMonster.setRate(rs.getInt("rate"));
		return pkMonster;
	}

	private PKMonsterSkillBean getPKMonsterSkill(ResultSet rs)
			throws SQLException {
		PKMonsterSkillBean pkMonsterSkill = new PKMonsterSkillBean();
		pkMonsterSkill.setId(rs.getInt("id"));
		pkMonsterSkill.setDescription(rs.getString("description"));
		pkMonsterSkill.setAggressGrowthRadix(rs
				.getString("aggress_growth_radix"));
		return pkMonsterSkill;
	}

	private PKActBean getPKAct(ResultSet rs) throws SQLException {
		PKActBean pkAct = new PKActBean();
		pkAct.setId(rs.getInt("id"));
		pkAct.setName(rs.getString("name"));
		pkAct.setDescription(rs.getString("description"));
		pkAct.setMonster(rs.getString("monster"));
		pkAct.setMaxRoleCount(rs.getInt("max_role_count"));
		pkAct.setPicture(rs.getString("picture"));
		return pkAct;
	}

	private PKUserBean getPKUser(ResultSet rs) throws SQLException {
		PKUserBean pkUser = new PKUserBean();
		pkUser.setId(rs.getInt("id"));
		pkUser.setUserId(rs.getInt("user_id"));
		pkUser.setExperience(rs.getInt("experience"));
		pkUser.setBasePhysical(rs.getInt("base_physical"));
		pkUser.setBaseEnergy(rs.getInt("base_energy"));
		pkUser.setBaseAggressivity(rs.getInt("base_aggressivity"));
		pkUser.setBaseRecovery(rs.getInt("base_recovery"));
		pkUser.setBaseFlying(rs.getInt("base_flying"));
		pkUser.setBaseLuck(rs.getInt("base_luck"));
		pkUser.setCurrentPhysical(rs.getInt("current_physical"));
		pkUser.setCurrentEnergy(rs.getInt("current_energy"));
		pkUser.setBag(rs.getInt("bag"));
		pkUser.setMissionStart(rs.getString("mission_start"));
		pkUser.setMissionEnd(rs.getString("mission_end"));
		Set mStartSet = this.setSetValues(rs.getString("mission_start"));
		pkUser.setMStartSet(mStartSet);
		Set mEndSet = this.setSetValues(rs.getString("mission_end"));
		pkUser.setMEndSet(mEndSet);
		pkUser.setKCount(rs.getInt("kcount"));
		pkUser.setOldKCount(rs.getInt("old_kcount"));
		return pkUser;
	}

	private PKUserSkillBean getPKUserSkill(ResultSet rs) throws SQLException {
		PKUserSkillBean pkUserSkill = new PKUserSkillBean();
		pkUserSkill.setId(rs.getInt("id"));
		pkUserSkill.setName(rs.getString("name"));
		pkUserSkill.setDescription(rs.getString("description"));
		pkUserSkill.setDescription1(rs.getString("description1"));
		pkUserSkill.setDescription2(rs.getString("description2"));
		pkUserSkill.setGradeCount(rs.getInt("grade_count"));
		pkUserSkill.setAggressGrowthRadix(rs.getString("aggress_growth_radix"));
		pkUserSkill.setType(rs.getInt("type"));
		pkUserSkill.setWeaponType(rs.getInt("weapon_type"));
		pkUserSkill.setPrice(rs.getInt("price"));
		pkUserSkill.setPicture(rs.getString("picture"));
		return pkUserSkill;
	}

	private PKUserHSkillBean getPKUserHSkill(ResultSet rs) throws SQLException {
		PKUserHSkillBean pkUserHSkill = new PKUserHSkillBean();
		pkUserHSkill.setId(rs.getInt("id"));
		pkUserHSkill.setUserId(rs.getInt("user_id"));
		pkUserHSkill.setSkillId(rs.getInt("skill_id"));
		pkUserHSkill.setSkillKey(rs.getInt("skill_key"));
		pkUserHSkill.setSkillType(rs.getInt("skill_type"));
		pkUserHSkill.setExcersize(rs.getInt("excersize"));
		pkUserHSkill.setRank(rs.getInt("rank"));
		return pkUserHSkill;
	}
	
	private PKNpcBean getPKNpc(ResultSet rs) throws SQLException {
		PKNpcBean pkNpc = new PKNpcBean();
		pkNpc.setId(rs.getInt("id"));
		pkNpc.setSceneId(rs.getInt("scene_id"));
		pkNpc.setName(rs.getString("name"));
		pkNpc.setEquip(rs.getString("equip"));
		pkNpc.setMedicine(rs.getString("medicine"));
		pkNpc.setSkill(rs.getString("skill"));
		pkNpc.setBSkill(rs.getString("bskill"));
		pkNpc.setMission(rs.getString("mission"));
		return pkNpc;
	}
	
	private PKUserBagBean getPKUserBag(ResultSet rs) throws SQLException {
		PKUserBagBean pkUserBag = new PKUserBagBean();
		pkUserBag.setId(rs.getInt("id"));
		pkUserBag.setUserId(rs.getInt("user_id"));
		pkUserBag.setEquipId(rs.getInt("equip_id"));
		pkUserBag.setSiteId(rs.getInt("site_id"));
		pkUserBag.setEnduranceDegree(rs.getInt("endurance_degree"));
		pkUserBag.setType(rs.getInt("type"));	
		return pkUserBag;
	}

	private PKEquipBean getPKEuqip(ResultSet rs) throws SQLException {
		PKEquipBean pkKEuqip = new PKEquipBean();
		pkKEuqip.setId(rs.getInt("id"));
		pkKEuqip.setName(rs.getString("name"));
		pkKEuqip.setType(rs.getInt("type"));
		pkKEuqip.setChildType(rs.getInt("child_type"));
		pkKEuqip.setPhysicalGrowthRadix(rs.getInt("physical_growth_radix"));
		pkKEuqip.setPhysicalMaxRadix(rs.getInt("physical_max_radix"));
		pkKEuqip.setEnergyGrowthRadix(rs.getInt("energy_growth_radix"));
		pkKEuqip.setEnergyMaxRadix(rs.getInt("energy_max_radix"));
		pkKEuqip.setAggressGrowthRadix(rs.getInt("aggress_growth_radix"));
		pkKEuqip.setAggressMaxRadix(rs.getInt("aggress_max_radix"));
		pkKEuqip.setRecoveryGrowthRadix(rs.getInt("recovery_growth_radix"));
		pkKEuqip.setRecoveryMaxRadix(rs.getInt("recovery_max_radix"));
		pkKEuqip.setFlyingGrowthRadix(rs.getInt("flying_growth_radix"));
		pkKEuqip.setFlyingMaxRadix(rs.getInt("flying_max_radix"));
		pkKEuqip.setLuckGrowthRadix(rs.getInt("luck_growth_radix"));
		pkKEuqip.setLuckMaxRadix(rs.getInt("luck_max_radix"));
		pkKEuqip.setPrice(rs.getInt("price"));
		pkKEuqip.setPicture(rs.getString("picture"));
		pkKEuqip.setSite(rs.getInt("site"));
		return pkKEuqip;
	}
	
	private PKMedicineBean getPKMedicine(ResultSet rs) throws SQLException {
		PKMedicineBean pkMedicine = new PKMedicineBean();
		pkMedicine.setId(rs.getInt("id"));
		pkMedicine.setName(rs.getString("name"));
		pkMedicine.setDescription(rs.getString("description"));
		pkMedicine.setGrade(rs.getInt("grade"));
		pkMedicine.setPhysicalGrowth(rs.getInt("physical_growth"));
		pkMedicine.setEnergyGrowth(rs.getInt("energy_growth"));
		pkMedicine.setSkillGrowth(rs.getInt("skill_growth"));
		pkMedicine.setAggressGrowth(rs.getInt("aggress_growth"));
		pkMedicine.setRecoveryGrowth(rs.getInt("recovery_growth"));
		pkMedicine.setPrice(rs.getInt("price"));
		pkMedicine.setPicture(rs.getString("picture"));
		return pkMedicine;
	}
	
	private PKUserBSkillBean getPKUserBSkill(ResultSet rs) throws SQLException {
		PKUserBSkillBean pkUserBSkill = new PKUserBSkillBean();
		pkUserBSkill.setId(rs.getInt("id"));
		pkUserBSkill.setName(rs.getString("name"));
		pkUserBSkill.setDescription(rs.getString("description"));
		pkUserBSkill.setGrade(rs.getInt("grade"));
		int[] physicalGrowth=setArrayValues(rs.getString("physical_growth_radix"));
		pkUserBSkill.setPhysicalGrowthRadix(physicalGrowth);
		int[] physicalMax=setArrayValues(rs.getString("physical_max_radix"));
		pkUserBSkill.setPhysicalMaxRadix(physicalMax);
		int[] energyGrowth=setArrayValues(rs.getString("energy_growth_radix"));
		pkUserBSkill.setEnergyGrowthRadix(energyGrowth);
		int[] energyMax=setArrayValues(rs.getString("energy_max_radix"));
		pkUserBSkill.setEnergyMaxRadix(energyMax);
		int[] flyingGrowth=setArrayValues(rs.getString("flying_growth_radix"));
		pkUserBSkill.setFlyingGrowthRadix(flyingGrowth);
		int[] flyingMax=setArrayValues(rs.getString("flying_max_radix"));
		pkUserBSkill.setFlyingMaxRadix(flyingMax);
		int[] aggressGrowth=setArrayValues(rs.getString("aggress_growth_radix"));
		pkUserBSkill.setAggressGrowthRadix(aggressGrowth);
		int[] aggressMax=setArrayValues(rs.getString("aggress_max_radix"));
		pkUserBSkill.setAggressMaxRadix(aggressMax);
		int[] recoveryGrowth=setArrayValues(rs.getString("recovery_growth_radix"));
		pkUserBSkill.setRecoveryGrowthRadix(recoveryGrowth);
		int[] recoveryMax=setArrayValues(rs.getString("recovery_max_radix"));
		pkUserBSkill.setRecoveryMaxRadix(recoveryMax);
		pkUserBSkill.setType(rs.getInt("type"));
		pkUserBSkill.setPrice(rs.getInt("price"));
		pkUserBSkill.setPicture(rs.getString("picture"));
		return pkUserBSkill;
	}
	
	private PKMissionBean getPKMission(ResultSet rs) throws SQLException {
		PKMissionBean pkMission = new PKMissionBean();
		pkMission.setId(rs.getInt("id"));
		pkMission.setName(rs.getString("name"));
		pkMission.setDescription(rs.getString("description"));
		pkMission.setObjs(rs.getString("objs"));
		pkMission.setPrize(rs.getString("prize"));
		return pkMission;
	}
	
	private PKMObjBean getPKMObj(ResultSet rs) throws SQLException {
		PKMObjBean pkMObj = new PKMObjBean();
		pkMObj.setId(rs.getInt("id"));
		pkMObj.setName(rs.getString("name"));
		return pkMObj;
	}
	/**
	 *  
	 * @author macq
	 * @explain： 转换String结果为int数组
	 * @datetime:2007-3-12 16:23:05
	 * @param a
	 * @return
	 * @return int[]
	 */
	public int[] setArrayValues(String a){
		//初始化int数组单位10个默认值0
		int[] b = new int[]{0,0,0,0,0,0,0,0,0,0};
		//拆分字符串
		String[]  c = a.split(",");
		//装换String[]为int[]
		for(int j = 0;j<c.length;j++){
			b[j]=StringUtil.toInt(c[j]);
			if(j==9)break;
		}
		return b;
	}
	
	/**
	 *  
	 * @author macq
	 * @explain： 转换String结果为Set
	 * @datetime:2007-4-18 10:22:07
	 * @param a
	 * @return
	 * @return Set
	 */
	public Set setSetValues(String a){
		Set set = new HashSet();
		//初始化int数组单位10个默认值0
		String[] b = a.split(",");
		for (int i = 0; i < b.length; i++) {
			int id = StringUtil.toInt(b[i]);
			if(id!=-1){
				set.add(new Integer(id));
			}
		}
		return set;
	}

	/**
	 *  
	 * @author macq
	 * @explain： 转换string到list
	 * @datetime:2007-4-19 9:57:09
	 * @param a
	 * @return
	 * @return int[]
	 */
	public List stringtoList(String a){
		List b = new ArrayList();
		String[] content = a.split(",");
		PKObjTypeBean objType = null;
		for (int i = 0; i < content.length; i++) {
			objType = new PKObjTypeBean();
			String[] string = content[i].split("-");
			int type = StringUtil.toInt(string[0]);
			int id = StringUtil.toInt(string[1]);
			objType.setType(type);
			objType.setId(id);
			 b.add(objType);
		}
		return  b;
	}
}
