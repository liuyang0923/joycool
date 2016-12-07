package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.friendlink.LinkRecordBean;
import net.joycool.wap.bean.friendlink.LinkTypeBean;
import net.joycool.wap.bean.friendlink.RandomLinkBean;
import net.joycool.wap.bean.friendlink.RandomSubLinkBean;
import net.joycool.wap.service.infc.IFriendLinkService;
import net.joycool.wap.util.SequenceUtil;
import net.joycool.wap.util.db.DbOperation;

public class FriendLinkServiceImpl implements IFriendLinkService {

    public LinkRecordBean getLinkRecord(String condition) {
        LinkRecordBean linkrecord = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }
        //查询语句
        String sql = "SELECT * FROM link_record";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }
        if (sql.indexOf("LIMIT") == -1) {
            sql = sql + " LIMIT 0, 1";
        }
        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            if (rs.next()) {
                linkrecord = new LinkRecordBean();
                linkrecord.setId(rs.getInt("id"));
                linkrecord.setLinkId(rs.getInt("link_id"));
                linkrecord.setName(rs.getString("name"));
                linkrecord.setUrl(rs.getString("url"));
                linkrecord.setType_Id(rs.getInt("type_id"));
                linkrecord.setMark(rs.getInt("mark"));
                linkrecord.setCreateDatetime(rs.getString("create_datetime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return linkrecord;
    }

    public boolean chenkLinkRecordName(String condition) {
        boolean result = false;

        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "select * from link_record WHERE " + condition;

        ResultSet rs = dbOp.executeQuery(query);
        try {
            if (rs!=null && rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbOp.release();
        return result;
    }

    public Vector getLinkRecordList(String condition) {
        Vector linkrecordsList = new Vector();

        LinkRecordBean linkrecord = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM link_record";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            while (rs.next()) {
                linkrecord = new LinkRecordBean();
                linkrecord.setId(rs.getInt("id"));
                linkrecord.setLinkId(rs.getInt("link_id"));
                linkrecord.setName(rs.getString("name"));
                linkrecord.setUrl(rs.getString("url"));
                linkrecord.setType_Id(rs.getInt("type_id"));
                linkrecord.setMark(rs.getInt("mark"));
                linkrecord.setCreateDatetime(rs.getString("create_datetime"));
                linkrecordsList.add(linkrecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return linkrecordsList;
    }

    public void InsertLinkRecord(int linkid, String friendlinkName,
            String friendlinkaddress, int linktype) {
        //    	数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();
        int count = linkid + 1;
        //构建更新语句
        String sql = "insert into link_record(link_id,name,url,type_id,mark,create_datetime) values("
                + count
                + ",'"
                + friendlinkName
                + "','"
                + friendlinkaddress
                + "'," + linktype + ",0,now())";

        //执行更新
        dbOp.executeUpdate(sql);

        //释放资源
        dbOp.release();
    }

    public boolean updateLinkRecord(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE link_record SET " + set + " WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }

    public int getLinkRecordsCount(String condition) {
        int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "SELECT count(id) as c_id FROM link_record WHERE "
                + condition;

        ResultSet rs = dbOp.executeQuery(query);

        try {
            if (rs.next()) {
                count = rs.getInt("c_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return count;
    }
    public int getLinkIdHuangYe(){
    	int linkid = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "SELECT Max(link_id) as c_id FROM link_record where link_id>=40000 and link_id<=50000";

        ResultSet rs = dbOp.executeQuery(query);

        try {
            if (rs.next()) {
                linkid = rs.getInt("c_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return linkid;
    }
    
    public int getLinkId() {
        int linkid = SequenceUtil.getSeq("friendlink");

        return linkid;
    }

    public LinkTypeBean getLinkType(String condition) {
        LinkTypeBean linktype = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }
        //查询语句
        String sql = "SELECT * FROM link_type";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }
        if (sql.indexOf("LIMIT") == -1) {
            sql = sql + " LIMIT 0, 1";
        }
        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            if (rs.next()) {
                linktype = new LinkTypeBean();
                linktype.setId(rs.getInt("id"));
                linktype.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return linktype;
    }

    public Vector getLinkTypeList() {
        Vector linktypesList = new Vector();

        LinkTypeBean linktype = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM link_type";

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            while (rs.next()) {
                linktype = new LinkTypeBean();
                linktype.setId(rs.getInt("id"));
                linktype.setName(rs.getString("name"));
                linktypesList.add(linktype);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return linktypesList;

    }

    /*
     * public Vector getLinkTypeList(String condition){ Vector linktypesList =
     * new Vector();
     * 
     * LinkTypeBean linktype = null; //数据操作类 DbOperation dbOp = new
     * DbOperation(); //初始化 if (!dbOp.init()) { return null; }
     * 
     * //查询语句 String sql = "SELECT * FROM link_type"; if (condition != null) {
     * sql = sql + " WHERE " + condition; }
     * 
     * //查询 ResultSet rs = dbOp.executeQuery(sql); if (rs == null) { //释放资源
     * dbOp.release(); return null; }
     * 
     * //将结果保存 try { while (rs.next()) { linktype = new LinkTypeBean();
     * linktype.setId(rs.getInt("id")); linktype.setName(rs.getString("name"));
     * linktypesList.add(linktype); } } catch (SQLException e) {
     * e.printStackTrace(); } //释放资源 dbOp.release(); //返回结果 return
     * linktypesList;
     *  }
     */
    public boolean updateLinkType(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE link_type SET " + set + " WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }

    public int getLinkTypesCount(String condition) {
        int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "SELECT count(id) as c_id FROM link_type WHERE "
                + condition;

        ResultSet rs = dbOp.executeQuery(query);

        try {
            if (rs.next()) {
                count = rs.getInt("c_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return count;
    }

    public int getLinkTypesId(String condition) {
        int id = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "SELECT id  FROM link_type WHERE " + condition;

        ResultSet rs = dbOp.executeQuery(query);

        try {
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return id;
    }         
    
    //mcq_2006-7-13_欺骗友链连接方法_start
    public RandomLinkBean getRandomLink(String condition){
    	RandomLinkBean randomLink = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_random_link";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				randomLink = new RandomLinkBean();
				randomLink.setId(rs.getInt("id"));
				randomLink.setLinkId(rs.getInt("link_id"));
				randomLink.setAppearRate(rs.getInt("appear_rate"));
				randomLink.setMark(rs.getInt("mark"));
				randomLink.setCount(rs.getInt("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return randomLink;
    }
    public Vector getRandomLinkList(String condition){
    	Vector RandomLinkBeanList =new Vector();
    	RandomLinkBean randomLink = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_random_link";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				randomLink = new RandomLinkBean();
				randomLink.setId(rs.getInt("id"));
				randomLink.setLinkId(rs.getInt("link_id"));
				randomLink.setAppearRate(rs.getInt("appear_rate"));
				randomLink.setMark(rs.getInt("mark"));
				randomLink.setCount(rs.getInt("count"));
				RandomLinkBeanList.add(randomLink);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return RandomLinkBeanList;
    }
    //mcq_2006-7-13_欺骗友链连接方法_end
    
	// mcq_2006-4-11_欺骗友链连接方法_start
	public RandomSubLinkBean getRandomSubLink(String condition) {
		RandomSubLinkBean randomSubLink = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_random_sub_link";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				randomSubLink = new RandomSubLinkBean();
				randomSubLink.setId(rs.getInt("id"));
				randomSubLink.setLinkId(rs.getInt("link_id"));
				randomSubLink.setUrl(rs.getString("url"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return randomSubLink;
	}

	public Vector getRandomSubLinkList(String condition) {
		Vector randomSubLinkBeanList = new Vector();
		RandomSubLinkBean randomSubLink = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_random_sub_link";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				randomSubLink = new RandomSubLinkBean();
				randomSubLink.setId(rs.getInt("id"));
				randomSubLink.setLinkId(rs.getInt("link_id"));
				randomSubLink.setUrl(rs.getString("url"));
				randomSubLinkBeanList.add(randomSubLink);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return randomSubLinkBeanList;
	}
	//mcq_2006-4-12_欺骗友链连接方法_start
}
