package jc.family.game.pvz;

import java.sql.*;
import java.util.*;

import net.joycool.wap.util.db.DbOperation;

public class PVZService {

	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 * 僵尸表
	 * 
	 * @param cond
	 * @return
	 */
	public List getZombieList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_zombie where " + cond);
		try {
			while (rs.next()) {
				list.add(getZombie(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public ZombieProtoBean getZombie(ResultSet rs) throws SQLException {
		ZombieProtoBean bean = new ZombieProtoBean();
		bean.setId(rs.getInt("id"));
		bean.setType(rs.getInt("zombie_type"));
		bean.setMaxHp(rs.getInt("maxhp"));
		bean.setPrice(rs.getInt("price"));
		bean.setMoveCd(rs.getInt("movecd") * 1000);
		bean.setBuildCd(rs.getInt("buildcd") * 1000);
		bean.setAttack(rs.getInt("attack"));
		bean.setAttackCd(rs.getInt("attackcd") * 1000);
		bean.setAttackmun(rs.getInt("attackmun"));
		bean.setAttackspace(rs.getInt("attackspace"));
		bean.setPic(rs.getString("pic"));
		bean.setName(rs.getString("name"));
		bean.setShortName(rs.getString("short_name"));
		bean.setDescribe(rs.getString("describe"));
		return bean;
	}

	/**
	 * 植物表
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public List getPlantList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_plant where " + cond);
		try {
			while (rs.next()) {
				list.add(getPlant(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public PlantProtoBean getPlant(ResultSet rs) throws SQLException {
		PlantProtoBean bean = new PlantProtoBean();
		bean.setId(rs.getInt("id"));
		bean.setType(rs.getInt("plant_type"));
		bean.setMaxHp(rs.getInt("maxhp"));
		bean.setPrice(rs.getInt("price"));
		bean.setAttack(rs.getInt("attack"));
		bean.setBuildCd(rs.getInt("buildcd") * 1000);
		bean.setAttackCd(rs.getInt("attackcd") * 1000);
		bean.setAttackmun(rs.getInt("attackmun"));
		bean.setAttackspace(rs.getInt("attackspace"));
		bean.setPic(rs.getString("pic"));
		bean.setName(rs.getString("name"));
		bean.setShortName(rs.getString("short_name"));
		bean.setDescribe(rs.getString("describe"));
		return bean;
	}

}
