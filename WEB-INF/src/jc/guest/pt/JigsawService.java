package jc.guest.pt;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;


public class JigsawService {
	
	/**
	 * 前台得到所有的图片简单列表信息
	 * 
	 * @return
	 */
	public List selectJigsawLists(int level,int start,int end){
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select id, pic_name from jigsaw where pic_state=1 and game_level="+level+" limit "+start+","+end);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				JigsawBean bean=new JigsawBean();
				bean.setId(rs.getInt(1));
				bean.setPicName(rs.getString(2));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}
	
	// 后台
	public List selectJigsawDetailsLists(int start,int end){
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select id, pic_rows, pic_cols, pic_name,game_level,pic_num,pic_state,insert_time from jigsaw order by id desc limit "+start+","+end);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				JigsawBean bean=new JigsawBean();
				bean.setId(rs.getInt(1));
				bean.setPicRows(rs.getInt(2));
				bean.setPicCols(rs.getInt(3));
				bean.setPicName(rs.getString(4));
				bean.setPicGameLevel(rs.getInt(5));
				bean.setPicNum(rs.getInt(6));
				bean.setPicState(rs.getInt(7));
				bean.setUpdateTime(rs.getTimestamp("insert_time").getTime());
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}
	
	/**
	 * 得到一个拼图图片的信息
	 * 
	 * @param id
	 * @return
	 */
	public JigsawBean selectOneJigsaw(int id) {
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select id, pic_rows, pic_cols, pic_name,game_level,pic_num,pic_state from jigsaw  where pic_state=1 and id="+ id);
		try {
			if (rs.next()) {
				JigsawBean bean = new JigsawBean();
				bean.setId(rs.getInt(1));
				bean.setPicRows(rs.getInt(2));
				bean.setPicCols(rs.getInt(3));
				bean.setPicName(rs.getString(4));
				bean.setPicGameLevel(rs.getInt(5));
				bean.setPicNum(rs.getInt(6));
				bean.setPicState(rs.getInt(7));
				return bean;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}
	
	
	public JigsawBean selectOneJigsaw() {
		DbOperation db = new DbOperation(6);
		ResultSet rs = db.executeQuery("select id, pic_rows, pic_cols, pic_name,game_level from jigsaw limit 1");
		try {
			if (rs.next()) {
				JigsawBean bean = new JigsawBean();
				bean.setId(rs.getInt(1));
				bean.setPicRows(rs.getInt(2));
				bean.setPicCols(rs.getInt(3));
				bean.setPicName(rs.getString(4));
				bean.setPicGameLevel(rs.getInt(5));
				return bean;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}
	
	 /**
	  * 得到一个拼图图片的信息
	  * 
	  * @param id
	  * @return
	  */
	 public JigsawBean selectOneJigsaw2(int id) {
	  DbOperation db = new DbOperation(6);
	  ResultSet rs = db.executeQuery("select id, pic_rows, pic_cols, pic_name,game_level,pic_num,pic_state from jigsaw  where id="+ id);
	  try {
	   if (rs.next()) {
	    JigsawBean bean = new JigsawBean();
	    bean.setId(rs.getInt(1));
	    bean.setPicRows(rs.getInt(2));
	    bean.setPicCols(rs.getInt(3));
	    bean.setPicName(rs.getString(4));
	    bean.setPicGameLevel(rs.getInt(5));
	    bean.setPicNum(rs.getInt(6));
	    bean.setPicState(rs.getInt(7));
	    return bean;
	   }
	   return null;
	  } catch (SQLException e) {
	   e.printStackTrace();
	   return null;
	  } finally {
	   db.release();
	  }
	 }
	
	public JigsawUserBean selectJigsawUserDetatils(int uid){
		DbOperation db = new DbOperation(6);
		String sql="select uid, max_score, game_level from jigsaw_user where uid="+uid;
		ResultSet rs = db.executeQuery(sql);
		try {
			while(rs.next()){
				JigsawUserBean bean=new JigsawUserBean();
				bean.setUid(rs.getInt(1));
				bean.setMaxScore(rs.getInt(2));
				bean.setGameLevel(rs.getInt(3));
				return bean;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		return null;
	}
	
	public boolean insertJigsawUserDetails(JigsawUserBean bean){
		DbOperation db = new DbOperation(6);
		String sql="insert into jigsaw_user(uid, max_score, game_level)values("+bean.getUid()+","+bean.getMaxScore()+","+bean.getGameLevel()+") on duplicate key update max_score=max_score+"+bean.getMaxScore()+" ,game_level="+bean.getGameLevel();
		boolean insert=db.executeUpdate(sql);
		db.release();
		return insert;
	}
	
	public boolean updateJigsawUserDetails(int uid){
		DbOperation db = new DbOperation(6);
		String sql="update jigsaw_user set game_level=1 where uid="+uid;
		boolean update=db.executeUpdate(sql);
		db.release();
		return update;
	}
	
	public List selectUserList(){
		DbOperation db = new DbOperation(6);
		String sql="select uid, max_score, game_level from jigsaw_user where max_score!=0 order by max_score desc limit  100";
		ResultSet rs = db.executeQuery(sql);
		List list=new ArrayList();
		try {
			while(rs.next()){
				JigsawUserBean bean=new JigsawUserBean();
				bean.setUid(rs.getInt(1));
				bean.setMaxScore(rs.getInt(2));
				bean.setGameLevel(rs.getInt(3));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return null;
	}
	
	public int selectIntResult(String sql) {
		DbOperation db = new DbOperation(6);
		try {
			int c = db.getIntResult(sql);
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return 0;
	}
	
	public boolean intserJigsawBean(JigsawBean bean){
		  DbOperation db = new DbOperation(6);
		  String sql="insert into jigsaw(pic_rows, pic_cols, pic_name, insert_time, game_level,pic_state,pic_num) values ("+bean.getPicRows()+","+bean.getPicCols()+",'"+StringUtil.toSql(bean.getPicName())+"',now(),"+bean.getPicGameLevel()+","+bean.getPicState()+","+bean.getPicNum()+")";
		  boolean insert=db.executeUpdate(sql);
		  db.release();
		  return insert;
	}
	
	 // 拼图信息修改
	 public boolean updateJigsawBean(JigsawBean bean){
	  DbOperation db = new DbOperation(6);
	  String sql="update jigsaw set pic_name='"+StringUtil.toSql(bean.getPicName())+"',pic_rows="+bean.getPicRows()+",pic_cols="+bean.getPicCols()+",insert_time=now(),game_level="+bean.getPicGameLevel()+",pic_state="+bean.getPicState() +" where id=" + bean.getId();
	  boolean update=db.executeUpdate(sql);
	  db.release();
	  return update;
	 }

	
	// 删除拼图信息
	public boolean deletePicDetatils(int jigsawID){
		DbOperation db = new DbOperation(6);
		String sql="delete from jigsaw where id="+jigsawID;
		boolean delete=db.executeUpdate(sql);
		db.release();
		return delete;
	}
	
	// 搜索拼图信息
	public List searchJigsaw(String cmd,String str,int start,int end){
		DbOperation db = new DbOperation(6);
		String search="";
		
		if(cmd.equals("i")){// 按id搜索
			search="id ="+str;
		}
		if(cmd.equals("n")){// 按名字搜索
			// 此str在调用时已经tosqllike
			search=" pic_name like '%"+str+"%' ";
		}
		String sql="select id, pic_rows, pic_cols, pic_name,game_level,pic_num,pic_state from jigsaw where "+search+" order by id desc limit "+start+","+end;
		ResultSet rs = db.executeQuery(sql);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				JigsawBean bean=new JigsawBean();
				bean.setId(rs.getInt(1));
				bean.setPicRows(rs.getInt(2));
				bean.setPicCols(rs.getInt(3));
				bean.setPicName(rs.getString(4));
				bean.setPicGameLevel(rs.getInt(5));
				bean.setPicNum(rs.getInt(6));
				bean.setPicState(rs.getInt(7));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}
	
	// 修改图片的隐藏显示
	public boolean updateJigsawState(int id,int state){
		DbOperation db = new DbOperation(6);
		String sql="update jigsaw  set pic_state="+state+" where id="+id;
		boolean delete=db.executeUpdate(sql);
		db.release();
		return delete;
	}
	
	// 查看是否有重编号(没有返回false，有重名的返回true)
	public boolean  selectJigsawByNum(int picNum){
		 DbOperation db = new DbOperation(6);
		 String query="select id  from jigsaw where pic_num="+picNum;
		  ResultSet rs = db.executeQuery(query);
		  try {
		   if (rs.next()) {
			   return true;
		   }
		  } catch (SQLException e) {
		   e.printStackTrace();
		   return false;
		  } finally {
		   db.release();
		  }
		  return false;
	}
}
