package jc.family.game.emperor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class EmperorService {

	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 * 人物表
	 * 
	 * @param cond
	 * @return
	 */
	public List getPeopleList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_emperor_role where " + cond);
		try {
			while (rs.next()) {
				list.add(getRole(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public EmperorRoleBean getRole (ResultSet rs) throws SQLException {
		EmperorRoleBean bean = new EmperorRoleBean();
		bean.setId(rs.getInt("id"));
//		bean.setPoison(rs.getInt("poison"));
		bean.setSumBlood(rs.getInt("blood"));
//		bean.setVampire(rs.getInt("vamPire"));
		bean.setSkillId(rs.getInt("skill_id"));
		bean.setSkillType(rs.getInt("skill_type"));
		bean.setEffectSide(rs.getInt("effect_side"));
//		bean.setEffectOther(rs.getInt("effect_other"));
		bean.setEffectRange(rs.getInt("effect_range"));
		bean.setSkillSpecilHurt(rs.getInt("skill_specil_hurt"));
		bean.setSkillSimpleHurt(rs.getInt("skill_simple_hurt"));
		bean.setSkillHurtContribute(rs.getInt("skill_contribute"));
		bean.setName(rs.getString("name"));
		bean.setSkillName(rs.getString("skill_name"));
		bean.setPeopleIntroduction(rs.getString("people_introduction"));
		bean.setSkillIntroduction(rs.getString("skill_introduction"));
		return bean;
	}
}
