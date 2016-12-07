package net.joycool.wap.spec.friend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class AstroService {
	
	public AstroSupei getSupei(String cond){
		AstroSupei bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from astro_sp where " + cond);
		try {
			if (rs.next()){
				bean = getSupei(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getSupeiList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from astro_sp where " + cond);
		try {
			while (rs.next()){
				list.add(getSupei(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addSupei(AstroSupei bean){
		DbOperation db = new DbOperation(4);
		String query = "insert into astro_sp (astro1,astro2,exp,proportion,content,flag) values (?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getAstro1());
			pstmt.setInt(2, bean.getAstro2());
			pstmt.setInt(3, bean.getExp());
			pstmt.setString(4, bean.getProportion());
			pstmt.setString(5, bean.getContent());
			pstmt.setInt(6, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean modifySupei(AstroSupei bean){
		DbOperation db = new DbOperation(4);
		String query = "update astro_sp set astro1=?,astro2=?,exp=?,proportion=?,content=?,flag=? where id=?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getAstro1());
			pstmt.setInt(2, bean.getAstro2());
			pstmt.setInt(3, bean.getExp());
			pstmt.setString(4, bean.getProportion());
			pstmt.setString(5, bean.getContent());
			pstmt.setInt(6, bean.getFlag());
			pstmt.setInt(7, bean.getId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	AstroSupei getSupei(ResultSet rs) throws SQLException{
		AstroSupei bean = new AstroSupei();
		bean.setId(rs.getInt("id"));
		bean.setAstro1(rs.getInt("astro1"));
		bean.setAstro2(rs.getInt("astro2"));
		bean.setExp(rs.getInt("exp"));
		bean.setProportion(rs.getString("proportion"));
		bean.setContent(rs.getString("content"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	
	public AstroCess getCess(String cond){
		AstroCess bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from astro_cess where " + cond);
		try {
			if (rs.next()){
				bean = getCess(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getCessList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from astro_cess where " + cond);
		try {
			while (rs.next()){
				list.add(getCess(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addCess(AstroCess bean){
		DbOperation db = new DbOperation(4);
		String query = "insert into astro_cess (astro_id,`all`,love,career,fain,health,business,color,num,other_astro,date_time,content,flag) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getAstroId());
			pstmt.setInt(2, bean.getAll());
			pstmt.setInt(3, bean.getLove());
			pstmt.setInt(4, bean.getCareer());
			pstmt.setInt(5, bean.getFain());
			pstmt.setInt(6, bean.getHealth());
			pstmt.setInt(7, bean.getBusiness());
			pstmt.setString(8, bean.getColor());
			pstmt.setInt(9, bean.getNum());
			pstmt.setInt(10, bean.getOtherAstro());
			pstmt.setString(11, bean.getDatetimeString());	
			pstmt.setString(12, bean.getContent());
			pstmt.setInt(13, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean modifyCess(AstroCess bean){
		DbOperation db = new DbOperation(4);
		String query = "update astro_cess set astro_id=?,`all`=?,love=?,career=?,fain=?,health=?,business=?,color=?,num=?,other_astro=?,date_time=?,content=?,flag=? where id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getAstroId());
			pstmt.setInt(2, bean.getAll());
			pstmt.setInt(3, bean.getLove());
			pstmt.setInt(4, bean.getCareer());
			pstmt.setInt(5, bean.getFain());
			pstmt.setInt(6, bean.getHealth());
			pstmt.setInt(7, bean.getBusiness());
			pstmt.setString(8, bean.getColor());
			pstmt.setInt(9, bean.getNum());
			pstmt.setInt(10, bean.getOtherAstro());
			pstmt.setString(11, bean.getDatetimeString());	
			pstmt.setString(12, bean.getContent());
			pstmt.setInt(13, bean.getFlag());
			pstmt.setInt(14, bean.getId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	AstroCess getCess(ResultSet rs) throws SQLException{
		AstroCess bean = new AstroCess();
		bean.setId(rs.getInt("id"));
		bean.setAstroId(rs.getInt("astro_id"));
		bean.setAll(rs.getInt("all"));
		bean.setLove(rs.getInt("love"));
		bean.setCareer(rs.getInt("career"));
		bean.setFain(rs.getInt("fain"));
		bean.setHealth(rs.getInt("health"));
		bean.setBusiness(rs.getInt("business"));
		bean.setColor(rs.getString("color"));
		bean.setNum(rs.getInt("num"));
		bean.setOtherAstro(rs.getInt("other_astro"));
		bean.setDatetime(rs.getTimestamp("date_time").getTime());
		bean.setContent(rs.getString("content"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public AstroStory getStory(String cond){
		AstroStory bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from astro_story where " + cond);
		try {
			if (rs.next()){
				bean = getStory(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getStoryList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from astro_story where " + cond);
		try {
			while (rs.next()){
				list.add(getStory(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addStory(AstroStory bean){
		DbOperation db = new DbOperation(4);
		String query = "insert into astro_story (astro_id,content,title,flag) values (?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getAstroId());
			pstmt.setString(2, bean.getContent());
			pstmt.setString(3, bean.getTitle());
			pstmt.setInt(4, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean modifyStory(AstroStory bean){
		DbOperation db = new DbOperation(4);
		String query = "update astro_story set astro_id=?,content=?,title=?,flag=? where id=?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getAstroId());
			pstmt.setString(2, bean.getContent());
			pstmt.setString(3, bean.getTitle());
			pstmt.setInt(4, bean.getFlag());
			pstmt.setInt(5, bean.getId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	AstroStory getStory(ResultSet rs) throws SQLException{
		AstroStory bean = new AstroStory();
		bean.setId(rs.getInt("id"));
		bean.setAstroId(rs.getInt("astro_id"));
		bean.setContent(rs.getString("content"));
		bean.setFlag(rs.getInt("flag"));
		bean.setTitle(rs.getString("title"));
		return bean;
	}
	
	public AstroUserSupei getUserSupei(String cond){
		AstroUserSupei bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from astro_sp2 where " + cond);
		try {
			if (rs.next()){
				bean = getUserSupei(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getUserSupeiList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("select * from astro_sp2 where " + cond);
		try {
			while (rs.next()){
				list.add(getUserSupei(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	// 插入新的用户速配时要调用这个方法，而不是“addUserSupei”。
	public boolean newUserSupei(AstroUserSupei bean){
		boolean result = false;
		AstroUserSupei bean2 = this.getUserSupei(" from_uid = " + bean.getFromUid() + " and to_uid = " + bean.getToUid());
		if (bean2 != null){
			// 如果数据库中已存在该速配记录，则只将相应记录的create_time刷新
			result = SqlUtil.executeUpdate("update astro_sp2 set create_time = now() where id = " + bean2.getId(), 4);
		} else {
			// 不存在时再插入新记录
			result = this.addUserSupei(bean);
		}
		return result;
	}
	
	public boolean addUserSupei(AstroUserSupei bean){
		DbOperation db = new DbOperation(4);
		String query = "insert into astro_sp2 (from_uid,to_uid,create_time,flag) values (?,?,now(),?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getFromUid());
			pstmt.setInt(2, bean.getToUid());
			pstmt.setInt(3, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	AstroUserSupei getUserSupei(ResultSet rs) throws SQLException{
		AstroUserSupei bean = new AstroUserSupei();
		bean.setId(rs.getInt("id"));
		bean.setFromUid(rs.getInt("from_uid"));
		bean.setToUid(rs.getInt("to_uid"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
}
