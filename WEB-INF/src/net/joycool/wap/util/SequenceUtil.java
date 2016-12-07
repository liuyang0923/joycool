/*
 * Created on 2006-7-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.joycool.wap.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.joycool.wap.util.db.DbUtil;

/**
 * @author liuyi
 * 
 * 功能：
 */
public class SequenceUtil {

	private static Hashtable seqHash = new Hashtable();

	public static final int SEQ_STEP = 10;

	public synchronized static int getSeq(String sequenceName) {
		if(sequenceName==null || sequenceName.equals(""))return -1;

		int ret = -1;

		List seqList = (List) seqHash.get(sequenceName);
		if (seqList == null) {
			seqList = new ArrayList();
			seqHash.put(sequenceName, seqList);
		}  

		if (seqList.size() < 1) {
			int seqStart = genSeqFromDB(sequenceName);
			if (seqStart < 0){
				return -1;
			}

			for (int seq = seqStart; seq < (seqStart + SEQ_STEP); seq++) {
                seqList.add(new Integer(seq));
			}
		}
		
		Integer nextValue = (Integer)seqList.get(0);
		if(nextValue!=null){
			ret = nextValue.intValue();
			seqList.remove(0);
		}
		else{
			ret = -1;
		}

		return ret;
	}

	private static int genSeqFromDB(String sequenceName) {
		int ret = -1;

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DbUtil.getConnection();
			conn.setAutoCommit(false);

			st = conn.createStatement();
			String sql = "update seq set seq_value=seq_value+" + SEQ_STEP
					+ " where seq_name='" + sequenceName + "'";

			st.execute(sql);

			sql = "select seq_value from seq where seq_name='" + sequenceName
					+ "'";

			rs = st.executeQuery(sql);
			if (rs != null && rs.next()) {
				ret = rs.getInt(1);
			}

			conn.commit();

		} catch (Exception e) {
			e.printStackTrace(System.out);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}
			try {
				if (st != null) {
					st.close();
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null) {
					conn.setAutoCommit(true);
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
			}
		}

		return ret;
	}

}
